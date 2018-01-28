using System;
using System.IO;
using System.Linq;
using System.Net.Http;
using System.Runtime.CompilerServices;
using System.Runtime.Remoting.Messaging;
using System.Threading;
using System.Threading.Tasks;
using IrpyteRunner.DBPackage;
using IrpyteRunner.Downloader;
using NLog;

namespace IrpyteRunner.Services
{
    public class WallpaperService
    {
        private static Logger logger = LogManager.GetCurrentClassLogger();

        private IrpyteDownloader _irpyteDownloader;
        private HttpClient _httpClient = new HttpClient();
        
        private SemaphoreSlim _semaphore = new SemaphoreSlim(1, 1);

        public WallpaperService(IrpyteDownloader irpyteDownloader)
        {
            _irpyteDownloader = irpyteDownloader;
        }

        public bool IsBusy()
        {
            return _semaphore.CurrentCount == 0;
        }

        public void WaitFor()
        {
            LockStart("WaitFor");
            LockEnd("WaitFor");
        }

        public void Tick()
        {

            LockStart("Tick");
            try
            {
                logger.Info("tick");
                DownloadNewIfNeeded().Wait();
                TryCleanUp();
            }
            finally
            {
                LockEnd("Tick");
            }
        }

        public async Task<CreateNewResult> CreateNew(String search)
        {
            Task<CreateNewResult> createNewTask = Task.Run(() =>
            {
                LockStart("CreateNew");

                try
                {
                    return _irpyteDownloader.Create(search)
                        .ContinueWith(task =>
                        {
                            if (task.IsFaulted)
                            {
                                logger.Error(task.Exception, "CreateNew task error");
                                return new CreateNewResult(task.Exception);
                            }
                            else
                            {
                                try
                                {
                                    var id = task.Result.id;
                                    var appConfig = DB.Instance.GetConfig();
                                    appConfig.wallpaperId = id;
                                    appConfig.imageFileNames.Clear();
                                    appConfig.searchTerms = search;
                                    DB.Instance.UpdateConfig(appConfig);
                                    logger.Info($"Successfully updated new wallpaper id {id}");
                                    
                                    return new CreateNewResult();
                                }
                                catch (Exception e)
                                {
                                    logger.Error(e, "CreateNew while updating db");
                                    return new CreateNewResult(e);
                                }
                            }
                        });
                }
                finally
                {
                    LockEnd("CreateNew");
                }
            });
            return await createNewTask;
        }

        public async Task<PotentialError> GetNewWallpaper()
        {
            LockStart("GetNewWallpaper");
            try
            {
                if (DB.Instance.GetConfig().wallpaperId == null)
                {
                    return new PotentialError("Search term is not set");
                }

                var downloadNewImagesResult = await DownloadNewIfNeeded();

                var imageFileNames = DB.Instance.GetConfig().imageFileNames;
                if (imageFileNames.Count > 0)
                {
                    var selectedWallpaper = Path.Combine(DB.Instance.ImagesPath, imageFileNames[0]);
                    imageFileNames.RemoveAt(0);
                    var appConfig = DB.Instance.GetConfig();
                    appConfig.currentWallpaperPath = selectedWallpaper;
                    appConfig.lastWallpaperSetTime = DateTime.Now;
                    DB.Instance.UpdateConfig(appConfig); //sad

                    WallpaperChanger.SetWallpaper(selectedWallpaper);
                    return new PotentialError();
                }
                else
                {
                    if (downloadNewImagesResult.ResultedIn0Images)
                    {
                        logger.Warn("Couldn't download new images");
                        return new PotentialError("Couldn't download new images");
                    }
                    else
                    {
                        logger.Error("Something went wrong..?");
                        return new PotentialError("Something went wrong");
                    }
                }
            }
            catch (Exception e)
            {
                logger.Error(e, "GetNewWallpaper error");
                return new PotentialError(e.Message);
            }
            finally
            {
                LockEnd("GetNewWallpaper");
            }
        }

        private void LockStart(String logContext)
        {
            logger.Info($"Lock EnterWriteLock {logContext} before");
            _semaphore.Wait();
            logger.Info($"Lock EnterWriteLock {logContext} after");
        }

        private void LockEnd(String logContext)
        {
            logger.Info($"Lock ExitWriteLock {logContext}");
            _semaphore.Release();
        }

        private async Task<DownloadNewImagesResult> DownloadNewIfNeeded()
        {
            Task.Run(() => TryCleanUp());
            
            var wallpaperId = DB.Instance.GetConfig().wallpaperId;
            if (wallpaperId != null && DB.Instance.GetConfig().imageFileNames.Count < 5)
            {
                return await DownloadNewImages(DB.Instance.GetConfig().wallpaperId);
            }
            else return new DownloadNewImagesResult(false);
        }

        private void TryCleanUp()
        {
            try
            {
                var deleted = Directory.GetFiles(DB.Instance.ImagesPath)
                    .Where(file => !DB.Instance.GetConfig().imageFileNames.Contains(Path.GetFileName(file)))
                    .OrderBy(file => File.GetLastWriteTime(file))
                    .Reverse()
                    .Skip(20)
                    .Select(file =>
                    {
                        try
                        {
                            logger.Info($"Tryong to delete {file}");
                            File.Delete(file);
                            return true;
                        }
                        catch (Exception e)
                        {
                            logger.Error(e, $"Couldn't delete {file}");
                            return false;
                        }
                    }).Count(x => x);
                logger.Info($"Deleted {deleted} files");
            }
            catch (Exception e)
            {
                logger.Error(e, "Cleanup failed");
            }
        }

        private async Task<DownloadNewImagesResult> DownloadNewImages(String id)
        {
            logger.Info("downloadNewImages");

            var wallpaperResponse = await _irpyteDownloader.Wallpaper(id);

            if (wallpaperResponse.imageResults.Count == 0)
            {
                logger.Info("Search resulted in 0 images");
            }

            var downloadedFilenamesMaybe = await Task.WhenAll(wallpaperResponse.imageResults.Select(entry =>
            {
                try
                {
                    var fileId = Guid.NewGuid();
                    var fileExtension = Path.GetExtension(entry.url);
                    var filePath = Path.Combine(DB.Instance.ImagesPath, fileId.ToString() + fileExtension);
                    var fileStream = new FileStream(filePath, FileMode.CreateNew);
                    var fileName = Path.GetFileName(filePath);

                    logger.Info($"Getting {entry.url} into {filePath}");
                    var responseStream = _httpClient.GetStreamAsync(entry.url);
                    return responseStream
                        .ContinueWith(r => r.Result.CopyToAsync(fileStream),
                            TaskContinuationOptions.OnlyOnRanToCompletion).Unwrap()
                        .ContinueWith(r => fileName, TaskContinuationOptions.OnlyOnRanToCompletion)
                        .ContinueWith(t =>
                        {
                            logger.Info($"Disposing fileStream {filePath} and responseStream");
                            fileStream.Dispose();
                            responseStream.Dispose();
                            return t;
                        }).Unwrap();
                }
                catch (Exception e)
                {
                    logger.Error(e, "Error while downloading a file");
                    return null;
                }
            }));

            var downloadedFilenames = downloadedFilenamesMaybe.Where(x => x != null);

            var downloadedImages = downloadedFilenames.ToList();
            logger.Info($"Downloaded {downloadedImages.Count}");

            var appConfig = DB.Instance.GetConfig();
            appConfig.imageFileNames.AddRange(downloadedImages);
            DB.Instance.UpdateConfig(appConfig);

            if (downloadedImages.Count == 0)
            {
                return new DownloadNewImagesResult(true);
            }
            else
            {
                return new DownloadNewImagesResult();
            }
        }
    }
}