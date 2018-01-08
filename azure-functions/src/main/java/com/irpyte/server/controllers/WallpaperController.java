package com.irpyte.server.controllers;

import com.microsoft.azure.serverless.functions.ExecutionContext;
import com.microsoft.azure.serverless.functions.HttpRequestMessage;
import com.microsoft.azure.serverless.functions.HttpResponseMessage;
import com.microsoft.azure.serverless.functions.annotation.*;

import java.util.Optional;

public class WallpaperController {

    @StorageAccount(Constants.STORAGE_ACCOUNT)
    @FunctionName("wallpaper")
    public HttpResponseMessage<String> calculate(
            @HttpTrigger(name = "req", methods = {"get"}, authLevel = AuthorizationLevel.ANONYMOUS, route = "wallpaper/{id}")
                    HttpRequestMessage<Optional<String>> request,
            @BlobInput(name = "config", path = "data/settings.json") String config,
            @BlobInput(name = "userConfig", path = "data/{id}") String userConfig,
            ExecutionContext context
    ) {
        return FunctionController.wrap(context, request, config, () -> {
            return request.createResponse(200, "Hello " + request.getQueryParameters().get("name") + "\n config:" + config + "\n userConfig: " + userConfig);
        });
    }

}
