package com.irpyte.server.controllers;


import com.irpyte.server.form.SettingsForm;
import com.irpyte.server.services.MainService;
import com.microsoft.azure.serverless.functions.ExecutionContext;
import com.microsoft.azure.serverless.functions.HttpRequestMessage;
import com.microsoft.azure.serverless.functions.HttpResponseMessage;
import com.microsoft.azure.serverless.functions.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

public class MainController {

    @StorageAccount(Constants.STORAGE_ACCOUNT)
    @FunctionName("create")
    public HttpResponseMessage<Object> create(
            @HttpTrigger(name = "req", methods = {"post"}, authLevel = AuthorizationLevel.ANONYMOUS)
                    HttpRequestMessage<SettingsForm> request,
            @BlobInput(name = "config", path = Constants.CONTAINER + "/settings.json") String config,
            ExecutionContext context
    ) {
        return FunctionController.wrap(context, request, config, () -> {
            MainService.NewUser newUser = MainService.createNewUser(request.getBody());
            return request.createResponse(200, (Object) new CreateResponse(newUser.id()));
        });
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Setter
    public static class CreateResponse {
        private String id;
    }

}
