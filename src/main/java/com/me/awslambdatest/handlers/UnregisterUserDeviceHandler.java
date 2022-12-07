package com.me.awslambdatest.handlers;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.me.awslambdatest.ApiGatewayResponse;
import com.me.awslambdatest.models.Response;
import com.me.awslambdatest.service.UserDeviceTokenService;
import org.apache.log4j.Logger;
import java.util.Collections;
import java.util.Map;

public class UnregisterUserDeviceHandler implements RequestHandler<Map<String, Object>, ApiGatewayResponse> {
    private final Logger logger = Logger.getLogger(this.getClass());
    
    @Override
    public ApiGatewayResponse handleRequest(Map<String, Object> input, Context context) {
        try {
            // get the 'pathParameters' from input
            Map<String,String> pathParameters =  (Map<String,String>)input.get("pathParameters");
            String userIdId = pathParameters.get("id");
            String deviceId = pathParameters.get("deviceId");
        
            // get the Device by id
            Boolean success = new UserDeviceTokenService().delete(userIdId, deviceId);
        
            // send the response back
            if (success) {
                return ApiGatewayResponse.builder()
                        .setStatusCode(204)
                        .setHeaders(Collections.singletonMap("X-Powered-By", "AWS Lambda & Serverless"))
                        .build();
            } else {
                return ApiGatewayResponse.builder()
                        .setStatusCode(404)
                        .setObjectBody("Device with id: '" + deviceId + "' not found.")
                        .setHeaders(Collections.singletonMap("X-Powered-By", "AWS Lambda & Serverless"))
                        .build();
            }
        } catch (Exception ex) {
            logger.error("Error in deleting Device: " + ex);
        
            // send the error response back
            Response responseBody = new Response("Error in deleting device: ", input);
            return ApiGatewayResponse.builder()
                    .setStatusCode(500)
                    .setObjectBody(responseBody)
                    .setHeaders(Collections.singletonMap("X-Powered-By", "AWS Lambda & Serverless"))
                    .build();
        }
    }
}
