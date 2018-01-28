using System;
using System.Runtime.Serialization;

namespace IrpyteRunner.Downloader
{
    [DataContract]
    internal class CreateForm
    {
        [DataMember] internal String search;

        public CreateForm(string search)
        {
            this.search = search;
        }
    }
}