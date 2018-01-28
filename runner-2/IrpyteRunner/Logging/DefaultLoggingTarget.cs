using System;
using System.Collections.Generic;
using System.IO;
using IrpyteRunner.DBPackage;
using NLog;
using NLog.Common;
using NLog.Targets;

namespace IrpyteRunner.Logging
{
    [Target("IrpyteDefault")]
    public class DefaultLoggingTarget : FileTarget
    {
        private FileStream output;

        public DefaultLoggingTarget()
        {
            this.FileName = Path.Combine(DB.Instance.AppPath, "app.log");
        }
    }
}