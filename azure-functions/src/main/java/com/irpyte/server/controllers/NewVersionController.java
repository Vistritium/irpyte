package com.irpyte.server.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.irpyte.server.Config;
import com.irpyte.server.form.SettingsForm;
import com.irpyte.server.services.MainService;
import com.irpyte.server.services.VersionController;
import com.microsoft.azure.serverless.functions.ExecutionContext;
import com.microsoft.azure.serverless.functions.HttpRequestMessage;
import com.microsoft.azure.serverless.functions.HttpResponseMessage;
import com.microsoft.azure.serverless.functions.annotation.*;
import lombok.SneakyThrows;

public class NewVersionController {

    @StorageAccount(Constants.STORAGE_ACCOUNT)
    @FunctionName("version")
    @SneakyThrows
    public HttpResponseMessage<Object> create(
            @HttpTrigger(name = "req", methods = {"get"}, authLevel = AuthorizationLevel.ANONYMOUS, route = "version/{version}")
                    HttpRequestMessage<Void> request,
            @BindingName("version") String version,
            @BlobInput(name = "config", path = Constants.CONTAINER + "/settings.json") String config,
            ExecutionContext context
    ) {
        return FunctionController.wrap(context, request, config, () -> {
            return performSneakely(request, version);
        });
    }

    @SneakyThrows
    private HttpResponseMessage<Object> performSneakely(HttpRequestMessage<Void> request, String version) {
        VersionController.VersionResponse check = VersionController.check(version);
        String json = Config.objectMapper().writeValueAsString(check);
        return request.createResponse(200, (Object) json);
    }


}
