using System;
using System.Threading.Tasks;
using NLog;

namespace IrpyteRunner.Utils
{
    public static class TaskUtils
    {
        private static Logger logger = LogManager.GetCurrentClassLogger();

        public static void HandleTask<T>(Task<T> task, TaskScheduler taskScheduler, String logContext,
            Action<T> onSuccess)
        {
            task.ContinueWith(t =>
            {
                if (t.IsFaulted)
                {
                    logger.Error(t.Exception, logContext);
                }
                else
                {
                    onSuccess.Invoke(t.Result);
                }
            }, taskScheduler);
        }
    }
}