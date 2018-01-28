namespace IrpyteRunner.Services
{
    public class DownloadNewImagesResult
    {
        public bool ResultedIn0Images { get; set; }

        public DownloadNewImagesResult()
        {
            ResultedIn0Images = false;
        }

        public DownloadNewImagesResult(bool resultedIn0Images)
        {
            ResultedIn0Images = resultedIn0Images;
        }
    }
}