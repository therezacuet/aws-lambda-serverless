package com.me.awslambdatest.handlers;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.me.awslambdatest.ApiGatewayResponse;
import com.me.awslambdatest.models.Response;
import com.me.awslambdatest.models.UserDeviceToken;
import com.me.awslambdatest.service.UserDeviceTokenService;
import org.apache.log4j.Logger;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public class GetUserSubscriptionHandler implements RequestHandler<Map<String, Object>, ApiGatewayResponse> {
    private final Logger logger = Logger.getLogger(this.getClass());
    
    @Override
    public ApiGatewayResponse handleRequest(Map<String, Object> input, Context context) {
        try {
            Map<String,String> pathParameters =  (Map<String,String>)input.get("pathParameters");
            if (pathParameters != null) {
                String userId = pathParameters.get("id");
                if (userId != null) {
                    // get all Subscribed Device
                    List<UserDeviceToken> devices = new UserDeviceTokenService().list(userId);
                    // send the response back
                    return ApiGatewayResponse.builder()
                            .setStatusCode(200)
                            .setObjectBody(devices)
                            .setHeaders(Collections.singletonMap("X-Powered-By", "AWS Lambda & Serverless"))
                            .build();
                } else {
                    Response responseBody = new Response("Error in path parameter: ", input);
                    return ApiGatewayResponse.builder()
                            .setStatusCode(500)
                            .setObjectBody(responseBody)
                            .setHeaders(Collections.singletonMap("X-Powered-By", "AWS Lambda & Serverless"))
                            .build();
                }
            }
            else {
                Response responseBody = new Response("Error in path parameter: ", input);
                return ApiGatewayResponse.builder()
                        .setStatusCode(500)
                        .setObjectBody(responseBody)
                        .setHeaders(Collections.singletonMap("X-Powered-By", "AWS Lambda & Serverless"))
                        .build();
            }
        } catch (Exception ex) {
            logger.error("Error in listing devices: " + ex);
            // send the error response back
            Response responseBody = new Response("Error in listing devices: ", input);
            return ApiGatewayResponse.builder()
                    .setStatusCode(500)
                    .setObjectBody(ex)
                    .setHeaders(Collections.singletonMap("X-Powered-By", "AWS Lambda & Serverless"))
                    .build();
        }
    }
}
