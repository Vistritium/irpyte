namespace IrpyteRunner.Utils
{
    public class VersionUtils
    {

        public static string getVersion()
        {
            var version = System.Reflection.Assembly.GetEntryAssembly().GetName().Version;
            return version.Major + "." + version.Minor;
        }
        
    }
}