using System;
using System.Collections.Generic;
using System.Linq;
using System.Runtime.Remoting.Messaging;
using System.Threading.Tasks;
using System.Windows.Forms;
using IrpyteRunner.DBPackage;
using IrpyteRunner.Downloader;
using IrpyteRunner.Logging;
using IrpyteRunner.Services;
using NLog;
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
            bool autostarted = false;
            if (args.Length > 0 && args[0].ToLower().Equals("autostart"))
            {
                autostarted = true;
            }

            Target.Register<DefaultLoggingTarget>("IrpyteDefault");

            var downloader = new IrpyteDownloader(DB.Instance.GetConfig().wallpaperUri);
            var wallpaperService = new WallpaperService(downloader);

            Application.EnableVisualStyles();
            Application.SetCompatibleTextRenderingDefault(false);
            Application.Run(new InitApplicationContext(wallpaperService, !autostarted));
            
            if (autostarted)
            {
                wallpaperService.Tick();
                wallpaperService.GetNewWallpaper().Wait();
            }
        }
    }
}