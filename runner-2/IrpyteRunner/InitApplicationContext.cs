using System;
using System.Collections.Generic;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Forms;
using IrpyteRunner.DBPackage;
using IrpyteRunner.Properties;
using IrpyteRunner.Services;
using NLog;

namespace IrpyteRunner
{
    class InitApplicationContext : ApplicationContext
    {
        private static Logger logger = LogManager.GetCurrentClassLogger();
        
        private readonly WallpaperService _wallpaperService;

        private NotifyIcon _trayIcon;

        private MainWindow _mainWindow;

        public InitApplicationContext(WallpaperService wallpaperService, bool runMainWindow)
        {
            _wallpaperService = wallpaperService;
            // Initialize Tray Icon
            _trayIcon = new NotifyIcon()
            {
                Icon = Icon.FromHandle(Resources.tray.GetHicon()),
                ContextMenu = new ContextMenu(new MenuItem[] {
                    new MenuItem("Exit", Exit)
                }),
                Visible = true
            };

            _trayIcon.Click += OnTrayClick;

            var appConfig = DB.Instance.GetConfig();
            var nowDay = DateTime.Now.Day;
            if (appConfig.lastWallpaperSetTime.HasValue && appConfig.lastWallpaperSetTime.Value.Day != nowDay)
            {
                logger.Info(
                    $"lastWallpaperSetTime is {appConfig.lastWallpaperSetTime.Value} and now is day ${nowDay} so going to get next");
                _wallpaperService.GetNewWallpaper();
            }
            else if(appConfig.lastWallpaperSetTime.HasValue)
            {
                logger.Info($"lastWallpaperSetTime is {appConfig.lastWallpaperSetTime.Value} and now is day ${nowDay} so not going to get next");
            }

            if (runMainWindow)
            {
                RunNewMainWindow();
            }
            
        }

        private void RunNewMainWindow()
        {
            
            _mainWindow= new MainWindow(_wallpaperService);
            _mainWindow.Show();
        }

        private void OnTrayClick(object sender, EventArgs e)
        {
            if (_mainWindow == null || _mainWindow.IsDisposed)
            {
                RunNewMainWindow();
            }
            else
            {
                _mainWindow.Show();
            }
        }

        void Exit(object sender, EventArgs e)
        {
            // Hide tray icon, otherwise it will remain shown until user mouses over it
            _trayIcon.Visible = false;

            Application.Exit();
        }

    }
}
