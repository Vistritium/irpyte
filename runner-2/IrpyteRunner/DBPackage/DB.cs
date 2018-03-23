using System;
using System.Collections.Generic;
using System.IO;
using System.Runtime.CompilerServices;
using System.Runtime.Serialization.Json;
using IrpyteRunner.Properties;

namespace IrpyteRunner.DBPackage
{
    public class DB
    {
        #region INIT_SINGLETON

        public static DB Instance { get; }


        static DB()
        {
            Instance = new DB();
        }

        #endregion

        public string AppPath { get; }

        public string ImagesPath { get; }

        private String _configFilePath;

        private AppConfig _appConfig;

        private DataContractJsonSerializer _serializer = new DataContractJsonSerializer(typeof(AppConfig));

        private DB()
        {
            AppPath = Path.Combine(Environment.GetFolderPath(Environment.SpecialFolder.LocalApplicationData), "irpyte");
            if (!Directory.Exists(AppPath))
            {
                Directory.CreateDirectory(AppPath);
            }

            ImagesPath = Path.Combine(AppPath, "wallpapers");
            if (!Directory.Exists(ImagesPath))
            {
                Directory.CreateDirectory(ImagesPath);
            }

            _configFilePath = Path.Combine(AppPath, "config.json");

            if (!File.Exists(_configFilePath))
            {
                var appConfig = new AppConfig(
                    null,
                    null,
                    null,
                    Resources.defaultUri,
                    new List<string>(),
                    false,
                    null
                );

                UpdateConfig(appConfig);
            }

            using (var fileReader = new FileStream(_configFilePath, FileMode.Open))
            {
                _appConfig = _serializer.ReadObject(fileReader) as AppConfig;
                if (_appConfig.imageFileNames == null)
                {
                    _appConfig.imageFileNames = new List<string>();
                }
            }
        }

        [MethodImpl(MethodImplOptions.Synchronized)]
        public void UpdateConfig(AppConfig appConfig)
        {
            _appConfig = appConfig;
            using (var fileStream = new FileStream(_configFilePath, FileMode.Create))
            {
                _serializer.WriteObject(fileStream, _appConfig);
            }
        }

        [MethodImpl(MethodImplOptions.Synchronized)]
        public AppConfig GetConfig()
        {
            return _appConfig;
        }
    }
}