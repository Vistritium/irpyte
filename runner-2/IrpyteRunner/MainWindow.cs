using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Drawing;
using System.IO;
using System.Threading;
using System.Threading.Tasks;
using System.Windows.Forms;
using IrpyteRunner.DBPackage;
using IrpyteRunner.Properties;
using IrpyteRunner.Services;
using NLog;

namespace IrpyteRunner
{
    public partial class MainWindow : Form
    {
        private readonly WallpaperService _wallpaperService;

        private static Logger logger = LogManager.GetCurrentClassLogger();

        private List<Control> interactiveComponents = new List<Control>();

        public MainWindow(WallpaperService wallpaperService)
        {
            CreateHandle();
            logger.Info("App is starting.");
            logger.Info($"App directory: {DB.Instance.AppPath}");
            _wallpaperService = wallpaperService;
            this.Icon = Icon.FromHandle(Resources.icon.GetHicon());

            InitializeComponent();
            interactiveComponents.Add(searchBox);
            interactiveComponents.Add(searchButton);
            interactiveComponents.Add(findNewButton);

            if (!string.IsNullOrEmpty(DB.Instance.GetConfig().searchTerms))
            {
                this.searchBox.Text = DB.Instance.GetConfig().searchTerms;
            }

            LoadCurrentWallpaper();

            if (_wallpaperService.IsBusy())
            {
                logger.Info("Oops, servcie busy, waiting..");
                LoadingIndicatorStart();
                
                new Thread(() =>
                {
                    logger.Info("Wait thread start");
                    _wallpaperService.WaitFor();
                    this.BeginInvoke((MethodInvoker) delegate
                    {
                        if (!(this.Disposing || this.IsDisposed))
                        {
                            logger.Info("Done waiting, stopping indicator");
                            LoadingIndicatorStop();
                        }
                        else
                        {
                            logger.Warn("Window is beeing disposed, won't change indicator state");
                        }
                    });
                }).Start();
            }
        }

        private void UnloadResources()
        {
            if (this.pictureBox1.Image != null)
            {
                this.pictureBox1.Image.Dispose();
            }

            if (this.Icon != null)
            {
                this.Icon.Dispose();
            }
        }

        protected override void OnClosing(CancelEventArgs e)
        {
            logger.Info("OnClosing");
            UnloadResources();
            base.OnClosing(e);
        }

        protected override void OnClosed(EventArgs e)
        {
            logger.Info("OnClosed");
            base.OnClosed(e);
            System.GC.Collect();
        }

        private void findNewButton_Click(object sender, EventArgs e)
        {
            LoadingIndicatorStart();
            _wallpaperService.GetNewWallpaper()
                .ContinueWith(task =>
                {
                    LoadingIndicatorStop();
                    handlePotentialErrorTask(task);
                    LoadCurrentWallpaper();
                }, TaskScheduler.FromCurrentSynchronizationContext());
        }

        private void LoadCurrentWallpaper()
        {
            var currentWallpaperPath = DB.Instance.GetConfig().currentWallpaperPath;
            if (!string.IsNullOrEmpty(currentWallpaperPath) && File.Exists(currentWallpaperPath))
            {
                /*if (this.pictureBox1.Image != null)
                {
                    this.pictureBox1.Image.Dispose();
                }

                var bitmapImage = new BitmapImage();
                bitmapImage.BeginInit();
                bitmapImage.UriSource = new Uri(currentWallpaperPath);
                bitmapImage.DecodePixelWidth = 800;
                bitmapImage.EndInit();
                bitmapImage.Freeze();

                Bitmap bitmap;
                using (MemoryStream outStream = new MemoryStream())
                {
                    BitmapEncoder enc = new BmpBitmapEncoder();
                    enc.Frames.Add(BitmapFrame.Create(bitmapImage));
                    enc.Save(outStream);
                    bitmap = new System.Drawing.Bitmap(outStream);


                    this.pictureBox1.Image = Image.FromHbitmap(bitmap.GetHbitmap());
                    bitmap.Dispose();
                }*/

                this.pictureBox1.Image = Image.FromFile(currentWallpaperPath);
            }
        }

        private void searchButton_Click(object sender, EventArgs e)
        {
            HandleSearch();
        }

        private void LoadingIndicatorStart()
        {
            progressIndicator.Show();
            interactiveComponents.ForEach(control => control.Enabled = false);
        }

        private void LoadingIndicatorStop()
        {
            progressIndicator.Hide();
            interactiveComponents.ForEach(control => control.Enabled = true);
        }

        private void HandleSearch()
        {
            logger.Info("HandleSearch");
            var searchBoxText = searchBox.Text;

            if (!(String.IsNullOrEmpty(searchBoxText) || String.IsNullOrWhiteSpace(searchBoxText)))
            {
                LoadingIndicatorStart();
                _wallpaperService.CreateNew(searchBoxText)
                    .ContinueWith(task =>
                    {
                        if (task.Result.Exception1 != null)
                        {
                            return new PotentialError(task.Result.Exception1.Message);
                        }
                        else
                        {
                            return new PotentialError();
                        }
                    }, TaskContinuationOptions.OnlyOnRanToCompletion)
                    .ContinueWith(task => { return _wallpaperService.GetNewWallpaper(); },
                        TaskContinuationOptions.OnlyOnRanToCompletion).Unwrap()
                    .ContinueWith(task =>
                        {
                            logger.Info($"HandleSearch finishing. Is completed: {task.IsCompleted}");

                            LoadCurrentWallpaper();
                            LoadingIndicatorStop();
                            handlePotentialErrorTask(task);
                        },
                        TaskScheduler.FromCurrentSynchronizationContext());
            }
        }

        private void handlePotentialErrorTask(Task<PotentialError> potentialErrorTask)
        {
            if (potentialErrorTask.IsFaulted)
            {
                logger.Error(potentialErrorTask.Exception, "handlePotentialErrorTask");
                MessageBox.Show(potentialErrorTask.Exception.Message, "Oops", MessageBoxButtons.OK,
                    MessageBoxIcon.Error);
            }
            else if (!string.IsNullOrEmpty(potentialErrorTask.Result.ErrorMessage))
            {
                MessageBox.Show(potentialErrorTask.Result.ErrorMessage, "Oops", MessageBoxButtons.OK,
                    MessageBoxIcon.Error);
            }
        }

       
        private void searchBox_KeyPress(object sender, KeyPressEventArgs e)
        {
            if (e.KeyChar == Convert.ToChar(Keys.Return))
            {
                e.Handled = true;
                HandleSearch();
            }
        }
    }
}