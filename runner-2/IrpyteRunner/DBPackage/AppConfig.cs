using System;
using System.Collections.Generic;
using System.Runtime.Serialization;

namespace IrpyteRunner.DBPackage
{
    [DataContract]
    public class AppConfig
    {

        [DataMember]
        public String wallpaperId;

        [DataMember]
        public String searchTerms;

        [DataMember]
        public String currentWallpaperPath;

        [DataMember]
        public String wallpaperUri;

        [DataMember]
        public List<String> imageFileNames;

        [DataMember]
        public bool wasAutoStartSet;

        [DataMember]
        public DateTime? lastWallpaperSetTime;


        public AppConfig(string wallpaperId, string searchTerms, string currentWallpaperPath, string wallpaperUri, List<string> imageFileNames, bool wasAutoStartSet, DateTime? lastWallpaperSetTime)
        {
            this.wallpaperId = wallpaperId;
            this.searchTerms = searchTerms;
            this.currentWallpaperPath = currentWallpaperPath;
            this.wallpaperUri = wallpaperUri;
            this.imageFileNames = imageFileNames;
            this.wasAutoStartSet = wasAutoStartSet;
            this.lastWallpaperSetTime = lastWallpaperSetTime;
        }
    }
}