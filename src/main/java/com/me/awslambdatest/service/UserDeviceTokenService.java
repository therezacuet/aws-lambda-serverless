package com.me.awslambdatest.service;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.datamodeling.*;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.me.awslambdatest.dynamodb.DynamoDBAdapter;
import com.me.awslambdatest.models.UserDeviceToken;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserDeviceTokenService {
    private static final String USER_DEVICE_TOKEN_TABLE_NAME = System.getenv("USER_DEVICE_TOKEN_TABLE_NAME");
    private Logger logger = Logger.getLogger(this.getClass());
    private static DynamoDBAdapter dbAdapter;
    private AmazonDynamoDB client;
    private DynamoDBMapper mapper;
    
    public UserDeviceTokenService() {
        // build the mapper config
        DynamoDBMapperConfig mapperConfig = DynamoDBMapperConfig.builder()
                .withTableNameOverride(new DynamoDBMapperConfig.TableNameOverride(USER_DEVICE_TOKEN_TABLE_NAME))
                .build();
        // get the db adapter
        this.dbAdapter = DynamoDBAdapter.getInstance();
        this.client = this.dbAdapter.getDbClient();
        // create the mapper with config
        this.mapper = this.dbAdapter.createDbMapper(mapperConfig);
    }
    
    public Boolean ifTableExists() {
        return this.client.describeTable(USER_DEVICE_TOKEN_TABLE_NAME).getTable().getTableStatus().equals("ACTIVE");
    }
    
    public List<UserDeviceToken> list(String id) throws IOException {
        Map<String, AttributeValue> eav = new HashMap<String, AttributeValue>();
        eav.put(":UserId", new AttributeValue().withS(id));
        
        DynamoDBScanExpression scanExpression = new DynamoDBScanExpression()
                .withFilterExpression("UserId = :UserId").withExpressionAttributeValues(eav);
        List<UserDeviceToken> results = this.mapper.scan(UserDeviceToken.class, scanExpression);
        return results;
    }
    
    // TODO: need to modify function
    public UserDeviceToken get(String userId, String deviceId) throws IOException {
        UserDeviceToken device = null;
        
        Map<String, AttributeValue> eav = new HashMap<>();
        eav.put(":UserId", new AttributeValue().withS(userId));
        eav.put(":DeviceId", new AttributeValue().withS(deviceId));
    
        DynamoDBScanExpression scanExp = new DynamoDBScanExpression()
                .withFilterExpression("UserId = :UserId")
                .withFilterExpression("DeviceId = :DeviceId")
                .withExpressionAttributeValues(eav);
    
        PaginatedScanList<UserDeviceToken> result = this.mapper.scan(UserDeviceToken.class, scanExp);
        if (result.size() > 0) {
            device = result.get(0);
            logger.info("UserDevices - get(): device - " + device.toString());
        } else {
            logger.info("UserDevices - get(): device - Not Found.");
        }
        return device;
    }
    
    public void save(UserDeviceToken deviceToken) throws IOException {
        logger.info("UserDevices - save(): " + deviceToken.toString());
        this.mapper.save(deviceToken);
    }
    
    public Boolean delete(String userId, String deviceId) throws IOException {
        UserDeviceToken deviceToken = null;
        
        // get device if exists
        deviceToken = get(userId, deviceId);
        if (deviceToken != null) {
            this.mapper.delete(deviceToken);
        }
        else {
            logger.info("DeviceToken - delete(): Device - does not exist.");
            return false;
        }
        return true;
    }
}
