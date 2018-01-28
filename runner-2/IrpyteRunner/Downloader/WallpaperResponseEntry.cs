using System;
using System.Runtime.Serialization;

namespace IrpyteRunner.Downloader
{
    [DataContract]
    public class WallpaperResponseEntry
    {
        [DataMember] public String id;

        [DataMember] public String url;
    }
}