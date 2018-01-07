package com.irpyte.server;

import com.microsoft.azure.serverless.functions.ExecutionContext;
import com.microsoft.azure.serverless.functions.HttpRequestMessage;
import com.microsoft.azure.serverless.functions.HttpResponseMessage;
import com.microsoft.azure.serverless.functions.annotation.AuthorizationLevel;
import com.microsoft.azure.serverless.functions.annotation.FunctionName;
import com.microsoft.azure.serverless.functions.annotation.HttpTrigger;
import io.vavr.collection.List;

import java.util.Optional;

public class Function2 {

    @FunctionName("welcome")
    public HttpResponseMessage<String> hello(
            @HttpTrigger(name = "req", methods = {"get", "post"}, authLevel = AuthorizationLevel.ANONYMOUS) HttpRequestMessage<Optional<String>> request,
            final ExecutionContext context) {
        context.getLogger().info("Java HTTP trigger processed a request.");

        try {
            // Parse query parameter
            String param = request.getQueryParameters().get("name");
            context.getLogger().info("1");
            if (param == null) {
                context.getLogger().info("2");
                return request.createResponse(400, "Please pass a name on the query string or in the request body");
            } else {
                context.getLogger().info("3" + param);
                String query = List.ofAll(param.toLowerCase().toCharArray()).shuffle().mkString();
                context.getLogger().info("3.5");
                String name = request.getBody().orElse(query);
                return request.createResponse(200, "Hello, " + name);
            }
        } catch (Exception e) {
            context.getLogger().info("4");
            context.getLogger().severe("OOps?" + e.getMessage());
            return request.createResponse(500, e.getMessage());
        } catch (Throwable e) {
            context.getLogger().severe("Throwable log: " + e.getClass().getName() + " " + e.getMessage());
            throw e;
        }
    }

}
