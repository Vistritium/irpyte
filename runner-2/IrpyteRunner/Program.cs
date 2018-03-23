using System;
using System.Collections.Generic;
using System.Diagnostics;
using System.Linq;
using System.Runtime.Remoting.Messaging;
using System.Threading.Tasks;
using System.Windows.Forms;
using IrpyteRunner.DBPackage;
using IrpyteRunner.Downloader;
using IrpyteRunner.Logging;
using IrpyteRunner.Services;
using IrpyteRunner.Utils;
using NLog;
using NLog.Internal;
using NLog.Targets;

namespace IrpyteRunner
{
    static class Program
    {
        /// <summary>
        /// The main entry point for the application.
        /// </summary>
        [STAThread]
        static void Main(string[] args)
        {
            Console.WriteLine("Starting");
            try
            {
                Target.Register<DefaultLoggingTarget>("IrpyteDefault");
                var logger = LogManager.GetCurrentClassLogger();
                if (DuplicateInstanceUtil.HandleDuplicate())
                {
                    logger.Info("Oops, app already running. Closing");
                    Application.Exit();
                }
                else
                {
                    bool autostarted = false;
                    if (args.Length > 0 && args[0].ToLower().Equals("autostart"))
                    {
                        autostarted = true;
                        Application.Exit();
                    }

                    logger.Info($"App args: {String.Join(", ", args.ToList())}");


                    
                    var downloader = new IrpyteDownloader(DB.Instance.GetConfig().wallpaperUri);
                    var versionService = new VersionService(downloader);

                    versionService.AssureVersion();
                    versionService.CheckVersion();
                    
                    
                    var wallpaperService = new WallpaperService(downloader, versionService);

                    if (autostarted)
                    {
                        logger.Info("autostarted going to tick");
                        wallpaperService.Tick();
                        logger.Info("autostarted going to GetNewWallpaper");
                        wallpaperService.GetNewWallpaper().Wait();
                    }
                    else
                    {
                        Application.EnableVisualStyles();
                        Application.SetCompatibleTextRenderingDefault(false);
                        var mainWindow = new MainWindow(wallpaperService, versionService);
                        logger.Info("setting  DuplicateInstanceUtil.OnShow ");

                        DuplicateInstanceUtil.OnShow = mainWindow.Show;
                        Application.Run(mainWindow);
                    }
                }
            }
            catch (Exception e)
            {
                Console.WriteLine(e.GetType());
                Console.WriteLine(e.Message);
                Console.WriteLine(e.StackTrace);
                Application.Exit();
            }
        }
    }
}