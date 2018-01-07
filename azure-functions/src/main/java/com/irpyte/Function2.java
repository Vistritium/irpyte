package com.irpyte;

import com.microsoft.azure.serverless.functions.ExecutionContext;
import com.microsoft.azure.serverless.functions.HttpRequestMessage;
import com.microsoft.azure.serverless.functions.HttpResponseMessage;
import com.microsoft.azure.serverless.functions.annotation.AuthorizationLevel;
import com.microsoft.azure.serverless.functions.annotation.FunctionName;
import com.microsoft.azure.serverless.functions.annotation.HttpTrigger;
import org.apache.commons.lang3.StringUtils;

import java.util.Optional;

public class Function2 {

    @FunctionName("welcome")
    public HttpResponseMessage<String> hello(
            @HttpTrigger(name = "req", methods = {"get", "post"}, authLevel = AuthorizationLevel.ANONYMOUS) HttpRequestMessage<Optional<String>> request,
            final ExecutionContext context) {
        context.getLogger().info("Java HTTP trigger processed a request.");

        // Parse query parameter
        String param = request.getQueryParameters().get("name");

        if (param == null) {

            return request.createResponse(400, "Please pass a name on the query string or in the request body");
        } else {
            String query = StringUtils.capitalize(param.toLowerCase());
            String name = request.getBody().orElse(query);
            return request.createResponse(200, "Hello, " + name);
        }
    }

}
