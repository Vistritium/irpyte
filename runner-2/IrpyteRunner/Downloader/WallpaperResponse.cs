using System;
using System.Collections.Generic;
using System.Runtime.Serialization;

namespace IrpyteRunner.Downloader
{
    [DataContract]
    public class WallpaperResponse
    {
        [DataMember] public List<WallpaperResponseEntry> imageResults;
    }
}