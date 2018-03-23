using System;
using System.Runtime.Serialization;

namespace IrpyteRunner.DBPackage
{
    
    [DataContract]
    public class NewVersionStatus
    {
        
        [DataMember]
        public String newVersion;

        [DataMember]
        public Boolean newVersionAvailable;

        [DataMember]
        public String newVersionUrl;

        [DataMember]
        public DateTime? newVersionNotifiedTime;

        [DataMember]
        public String myVersion;

        public NewVersionStatus(string newVersion, bool newVersionAvailable, string newVersionUrl, string myVersion)
        {
            this.newVersion = newVersion;
            this.newVersionAvailable = newVersionAvailable;
            this.newVersionUrl = newVersionUrl;
            this.myVersion = myVersion;
            newVersionNotifiedTime = null;
        }


        public override string ToString()
        {
            return $"{nameof(newVersion)}: {newVersion}, {nameof(newVersionAvailable)}: {newVersionAvailable}, {nameof(newVersionUrl)}: {newVersionUrl}, {nameof(newVersionNotifiedTime)}: {newVersionNotifiedTime}, {nameof(myVersion)}: {myVersion}";
        }
    }
}
