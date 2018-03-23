using System;
using System.IO;
using System.Net.Http;
using System.Runtime.Serialization.Json;
using System.Text;
using System.Threading.Tasks;
using NLog;

namespace IrpyteRunner.Downloader
{
    public class IrpyteDownloader
    {
        private static Logger logger = LogManager.GetCurrentClassLogger();

        private String _createPath;
        private String _wallpaperPath;
        private String _versionPath;


        private HttpClient _client = new HttpClient();


        public IrpyteDownloader(String address)
        {
            _createPath = address + "/api/create";
            _wallpaperPath = address + "/api/wallpaper";
            _versionPath = address + "/api/version";
            _client.Timeout = TimeSpan.FromMinutes(3);
        }

        public async Task<VersionResponse> CheckVersion(String currentVersion)
        {
            logger.Info($"checking version");
            var requestUri = _versionPath + "/" + currentVersion;
            logger.Debug("performing request " + requestUri);
            using (HttpResponseMessage response = await _client.GetAsync(requestUri))
            {
                if (response.IsSuccessStatusCode)
                {
                    var serializer = new DataContractJsonSerializer(typeof(VersionResponse));

                    Stream stream = await response.Content.ReadAsStreamAsync();
                    return serializer.ReadObject(stream) as VersionResponse;
                }
                else
                {
                    throw new InvalidOperationException(
                        $"Not successfull response: {response.EnsureSuccessStatusCode()}");
                }
            }
        }

        public async Task<WallpaperResponse> Wallpaper(String id)
        {
            logger.Info($"wallpaper request with id {id}");
            using (HttpResponseMessage response = await _client.GetAsync(_wallpaperPath + "/" + id))
            {
                if (response.IsSuccessStatusCode)
                {
                    var serializer = new DataContractJsonSerializer(typeof(WallpaperResponse));

                    Stream stream = await response.Content.ReadAsStreamAsync();
                    return serializer.ReadObject(stream) as WallpaperResponse;
                }
                else
                {
                    throw new InvalidOperationException(
                        $"Not successfull response: {response.EnsureSuccessStatusCode()}");
                }
            }
        }

        public async Task<CreateResponse> Create(String search)
        {
            logger.Info($"create request with search {search}");
            var serializer = new DataContractJsonSerializer(typeof(CreateForm));
            var createForm = new CreateForm(search);

            String postContent;
            using (var memoryStream = new MemoryStream())
            {
                serializer.WriteObject(memoryStream, createForm);
                memoryStream.Position = 0;
                postContent = new StreamReader(memoryStream).ReadToEnd();
            }


            logger.Debug($"posting with {postContent}");

            using (var res = await _client.PostAsync(_createPath,
                new StringContent(postContent, Encoding.UTF8, "application/json")))
            {
                if (res.IsSuccessStatusCode)
                {
                    var stream = await res.Content.ReadAsStreamAsync();
                    var responseSerializer = new DataContractJsonSerializer(typeof(CreateResponse));
                    return responseSerializer.ReadObject(stream) as CreateResponse;
                }
                else
                {
                    return await HandleError(res);
                }
            }
        }

        private static async Task<CreateResponse> HandleError(HttpResponseMessage res)
        {
            var result = await res.Content.ReadAsStringAsync();
            throw new InvalidOperationException(
                $"Not successfull response: {res.StatusCode} \n {result}");
        }
    }
}