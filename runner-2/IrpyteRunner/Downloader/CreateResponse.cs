using System;
using System.Runtime.Serialization;
// ReSharper disable InconsistentNaming

namespace IrpyteRunner.Downloader
{
    [DataContract]
    public class CreateResponse
    {
        [DataMember] internal String id;
    }
}