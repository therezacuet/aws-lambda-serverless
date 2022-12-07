package com.me.awslambdatest.handlers;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.me.awslambdatest.ApiGatewayResponse;
import com.me.awslambdatest.models.Response;
import com.me.awslambdatest.models.UserDeviceToken;
import com.me.awslambdatest.service.UserDeviceTokenService;
import org.apache.log4j.Logger;
import java.util.Collections;
import java.util.Map;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class RegisterUserDeviceHandler implements RequestHandler<Map<String, Object>, ApiGatewayResponse> {
    
    private final Logger logger = Logger.getLogger(this.getClass());
    
    @Override
    public ApiGatewayResponse handleRequest(Map<String, Object> input, Context context) {
        UserDeviceTokenService service = new UserDeviceTokenService();
        try {
            // get the 'body' from input
            JsonNode body = new ObjectMapper().readTree((String) input.get("body"));
        
            // create the Device object for post
            UserDeviceToken userDeviceToken = new UserDeviceToken(
                    body.get("UserId").asText(),
                    body.get("DeviceId").asText(),
                    body.get("Token").asText(),
                    body.get("IsActive").asBoolean()
            );
    
            service.save(userDeviceToken);
            
            // send the response back
            return ApiGatewayResponse.builder()
                    .setStatusCode(200)
                    .setObjectBody(userDeviceToken)
                    .setHeaders(Collections.singletonMap("X-Powered-By", "AWS Lambda & Serverless"))
                    .build();
        
        } catch (Exception ex) {
            logger.error("Error in saving Device: " + ex);
        
            // send the error response back
            Response responseBody = new Response("Error in registering device: ", input);
            return ApiGatewayResponse.builder()
                    .setStatusCode(500)
                    .setObjectBody(responseBody)
                    .setHeaders(Collections.singletonMap("X-Powered-By", "AWS Lambda & Serverless"))
                    .build();
        }
    }
}
