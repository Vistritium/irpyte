using System;
using System.Threading.Tasks;
using IrpyteRunner.DBPackage;
using IrpyteRunner.Downloader;
using IrpyteRunner.Utils;
using NLog;

namespace IrpyteRunner.Services
{
    public class VersionService
    {
        private static Logger logger = LogManager.GetCurrentClassLogger();

        private IrpyteDownloader _irpyteDownloader;

        private bool alreadyChecked = false;

        private String currentVersion = VersionUtils.getVersion();

        public VersionService(IrpyteDownloader irpyteDownloader)
        {
            _irpyteDownloader = irpyteDownloader;
        }

        public void AssureVersion()
        {
            lock (this)
            {
                var config = DB.Instance.GetConfig();
                var myVersion = VersionUtils.getVersion();
                if (config.NewVersionStatus != null && config.NewVersionStatus.myVersion != myVersion)
                {
                    logger.Info("New version has been downloaded: " + myVersion);
                    config.NewVersionStatus = null;
                    DB.Instance.UpdateConfig(config);
                }
            }
        }

        public async void CheckVersion()
        {
            try
            {
                if (!alreadyChecked)
                {
                    AssureVersion();
                    var myVersion = VersionUtils.getVersion();
                    var config = DB.Instance.GetConfig();
                    alreadyChecked = true;
                    VersionResponse checkVersion = await _irpyteDownloader.CheckVersion(currentVersion);
                    var newVersionStatus =
                        new NewVersionStatus(checkVersion.newestVersion, checkVersion.needToUpdate,
                            checkVersion.url, myVersion);
                    config.NewVersionStatus = newVersionStatus;
                    logger.Info("NewVersionStatus: " + newVersionStatus);
                    DB.Instance.UpdateConfig(config);
                }
            }
            catch (Exception e)
            {
                logger.Error(e, "Couldn't check version");
            }
        }

        public void VersionWasNotified()
        {
            var config = DB.Instance.GetConfig();
            var configNewVersionStatus = config.NewVersionStatus;
            if (configNewVersionStatus != null)
            {
                configNewVersionStatus.newVersionNotifiedTime = DateTime.Now;
                logger.Debug("Setting VersionWasNotified for now");
            }

            DB.Instance.UpdateConfig(config);
        }

        public bool ShouldNotify()
        {
            var config = DB.Instance.GetConfig();
            if (config.NewVersionStatus != null && config.NewVersionStatus.newVersionNotifiedTime != null)
            {
                var newVersionNotifiedTime = config.NewVersionStatus.newVersionNotifiedTime;
                if (newVersionNotifiedTime.Value.AddMonths(1) > DateTime.Now)
                {
                    logger.Info("Should notify");
                    return true;
                }
            }

            logger.Info("Should not notify");
            return false;
        }

        public bool IsNewerVersionAvailable()
        {
            var newVersionStatus = DB.Instance.GetConfig().NewVersionStatus;
            if (newVersionStatus != null)
            {
                var isNewerVersionAvailable = !newVersionStatus.newVersion.Equals(currentVersion) && newVersionStatus.newVersionAvailable;
                return isNewerVersionAvailable;
            }

            return false;
        }
    }
}