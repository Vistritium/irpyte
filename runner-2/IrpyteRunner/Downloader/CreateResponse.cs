using System;
using System.Runtime.Serialization;

namespace IrpyteRunner.Downloader
{
    [DataContract]
    public class CreateResponse
    {
        [DataMember] internal String id;
    }
}