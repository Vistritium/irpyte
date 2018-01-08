package com.irpyte.server.controllers;

import com.irpyte.server.Config;
import com.irpyte.server.data.AppSettings;
import com.irpyte.server.data.UserData;
import com.irpyte.server.services.WallpaperService;
import com.irpyte.server.wallpaperprovider.ImageResult;
import com.irpyte.server.wallpaperprovider.SearchResult;
import com.microsoft.azure.serverless.functions.ExecutionContext;
import com.microsoft.azure.serverless.functions.HttpRequestMessage;
import com.microsoft.azure.serverless.functions.HttpResponseMessage;
import com.microsoft.azure.serverless.functions.OutputBinding;
import com.microsoft.azure.serverless.functions.annotation.*;
import lombok.Data;
import lombok.SneakyThrows;

import java.util.Optional;

public class WallpaperController {

    @StorageAccount(Constants.STORAGE_ACCOUNT)
    @FunctionName("wallpaper")
    @SneakyThrows
    public HttpResponseMessage<Object> calculate(
            @HttpTrigger(name = "req", methods = {"get"}, authLevel = AuthorizationLevel.ANONYMOUS, route = "wallpaper/{id}")
                    HttpRequestMessage<Optional<String>> request,
            @BlobInput(name = "config", path = "data/settings.json") String config,
            @BlobInput(name = "userConfig", path = Constants.CONTAINER + "/" + Constants.USER_DATA_LOCATION + "/{id}") String userConfig,
            @BlobOutput(name = "userConfigOut", path = Constants.CONTAINER + "/" + Constants.USER_DATA_LOCATION + "/{id}") OutputBinding<String> userConfigOut,
            ExecutionContext context
    ) {
        return FunctionController.wrap(context, request, config, () ->
                performSneakly(request, config, userConfig, userConfigOut));
    }

    @SneakyThrows
    private HttpResponseMessage<Object> performSneakly(
            HttpRequestMessage<Optional<String>> request, String config,
            String userConfig,
            OutputBinding<String> userConfigOut) {
        UserData userData = Config.objectMapper().readValue(userConfig, UserData.class);
        AppSettings appSettings = Config.objectMapper().readValue(config, AppSettings.class);

        SearchResult searchResult = WallpaperService.searchImages(userData.search(), userData, appSettings);
        userConfigOut.setValue(Config.objectMapper().writeValueAsString(searchResult.updatedUserData()));
        WallpaperResult wallpaperResult =
                new WallpaperResult(searchResult.results().toList());
        String jsonResult = Config.objectMapper().writeValueAsString(wallpaperResult);
        return request.createResponse(200, jsonResult);
    }

    @Data
    class WallpaperResult {
        final scala.collection.immutable.List<ImageResult> imageResults;//scala list master race
    }

}
