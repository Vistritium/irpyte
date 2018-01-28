using System;

namespace IrpyteRunner.Services
{
    public class PotentialError
    {
        public string ErrorMessage { get; }

        public PotentialError()
        {
        }

        public PotentialError(string errorMessage)
        {
            ErrorMessage = errorMessage;
        }
    }
}