using System;

namespace IrpyteRunner.Services
{
    public class CreateNewResult
    {
        public Exception Exception1 { get; }

        public CreateNewResult(Exception exception1)
        {
            Exception1 = exception1;
        }

        public CreateNewResult()
        {
            
        }
    }
}