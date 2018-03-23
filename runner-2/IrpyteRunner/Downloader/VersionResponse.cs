using System;
using System.Collections.Generic;
using System.Linq;
using System.Runtime.Serialization;
using System.Text;
using System.Threading.Tasks;

namespace IrpyteRunner.Downloader
{
    [DataContract]
    public class VersionResponse
    {
        
        [DataMember] internal String newestVersion;
        
        [DataMember] internal String url;
        
        [DataMember] internal Boolean needToUpdate;
    }
}
