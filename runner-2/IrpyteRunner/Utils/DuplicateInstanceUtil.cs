using System;
using System.Diagnostics;
using System.IO;
using System.Linq;
using System.Runtime.InteropServices;
using IrpyteRunner.DBPackage;
using NLog;

namespace IrpyteRunner.Utils
{
    public class DuplicateInstanceUtil
    {

        private static Logger _logger = LogManager.GetCurrentClassLogger();

        public  static Action OnShow { get; set; }

        public static bool HandleDuplicate ()
        {
            string procName = Process.GetCurrentProcess().ProcessName;
            // get the list of all processes by that name

            Process[] processes = Process.GetProcessesByName(procName);
            string watcherPath = Path.Combine(DB.Instance.AppPath, "watcher");
            if (processes.Length > 1)
            {
                File.WriteAllText(watcherPath, "Bump");
                return true;
            }
            else
            {
               
                File.Delete(watcherPath);
                File.WriteAllText(watcherPath, "file start");
                var fileSystemWatcher = new FileSystemWatcher();
                fileSystemWatcher.Path = Path.GetDirectoryName(watcherPath);
                fileSystemWatcher.Filter = Path.GetFileName(watcherPath);
                fileSystemWatcher.Changed += OnChanged;
                fileSystemWatcher.EnableRaisingEvents = true;
                return false;
            }
            
        }
        
        private static void OnChanged(object source, FileSystemEventArgs e)
        {
            _logger.Info($"OnChanged {OnShow}");
            if (OnShow != null)
            {
                _logger.Info("Invoking OnShow");
                OnShow.Invoke();
            }    
        }
        
    }
    

}