package com.irpyte.server.controllers;

import com.microsoft.azure.serverless.functions.ExecutionContext;
import com.microsoft.azure.serverless.functions.HttpRequestMessage;
import com.microsoft.azure.serverless.functions.HttpResponseMessage;
import com.microsoft.azure.serverless.functions.annotation.*;

import java.util.Optional;

public class JavoController {

    @StorageAccount("irpytedata")
    @FunctionName("hello")
    public HttpResponseMessage<String> calculate(
            @HttpTrigger(name = "req", methods = {"get"}, authLevel = AuthorizationLevel.ANONYMOUS)
                    HttpRequestMessage<Optional<String>> request,
             @BlobInput(name = "config", path = "data/settings.json") String config,
            ExecutionContext context
    ) {
        return FunctionController.wrap(context, request, () -> {
            return request.createResponse(200, "Hello " + request.getQueryParameters().get("name") + "\n config:" + config);
        });
    }

}
