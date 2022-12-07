package com.me.awslambdatest.models;
import com.amazonaws.services.dynamodbv2.datamodeling.*;
import com.me.awslambdatest.dynamodb.AbstractDynamoDBEntity;

@DynamoDBTable(tableName = "UserDeviceToken")
public class UserDeviceToken extends AbstractDynamoDBEntity {
    
    public static final String GSI_NAME = "GSI1";
    
    // global secondary index attributes
    private String gsi1PartitionKey;
    
    // data attributes
    private String userId;
    private String deviceId;
    private String token;
    private boolean isActive;
    
    public UserDeviceToken() {
    }
    
    public UserDeviceToken(String userId, String deviceId, String token, boolean isActive) {
        super();
        this.userId = userId;
        this.deviceId = deviceId;
        this.token = token;
        this.isActive = isActive;
        this.partitionKey = generatePartitionKey();
        this.gsi1PartitionKey = generateGsiPartitionKey();
    }
    
    @DynamoDBIndexHashKey(attributeName = "GSI1PartitionKey", globalSecondaryIndexName = UserDeviceToken.GSI_NAME)
    public String getGsi1PartitionKey() {
        return gsi1PartitionKey;
    }
    
    public void setGsi1PartitionKey(String gsi1PartitionKey) {
        this.gsi1PartitionKey = gsi1PartitionKey;
    }
    
    @DynamoDBAttribute(attributeName="UserId")
    public String getUserId() {
        return this.userId;
    }
    
    public void setUserId(String userId) {
        this.userId = userId;
    }
    
    @DynamoDBAttribute(attributeName="DeviceId")
    public String getDeviceId() {
        return this.deviceId;
    }
    
    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }
    
    @DynamoDBAttribute(attributeName="Token")
    public String getToken() {
        return this.token;
    }
    
    public void setToken(String token) {
        this.token = token;
    }
    
    @DynamoDBAttribute(attributeName="IsActive")
    public boolean isActive() {
        return this.isActive;
    }
    
    public void setActive(boolean active) {
        this.isActive = active;
    }
    
    @Override
    protected String generatePartitionKey() {
        return generatePartitionKey(this.userId, this.deviceId, this.token, this.isActive);
    }
    
    protected String generateGsiPartitionKey() {
        return generateGsiPartitionKey(this.userId, this.deviceId, this.isActive);
    }
    
    @Override
    public String toString() {
        return "UserDeviceToken{"
                + "gsi1PartitionKey='" + gsi1PartitionKey + '\''
                + ", userId=" + userId
                + ", deviceId=" + deviceId
                + ", token=" + token
                + ", isActive=" + isActive
                + "} " + super.toString();
    }
    
    /**
     * Generates a partition key using the entity's class name, a user ID, device ID and device Token.
     *
     * @return a partition key, or null if any parameters are null
     */
    public static String generatePartitionKey(String userId, String deviceId, String token, boolean isActive) {
        if (userId == null || deviceId == null || token == null) {
            return null;
        }
        return String.join("#",
                UserDeviceToken.class.getSimpleName(),
                String.join(";",
                        String.join(",",
                                "UserId",
                                userId
                        ),
                        String.join(",",
                                "DeviceId",
                                deviceId
                        ),
                        String.join(",",
                                "Token",
                                token
                        ),
                        String.join(",",
                                "IsActive",
                                String.valueOf(isActive)
                        )
                )
        );
    }
    
    /**
     * Generates a partition key for the GSI using the entity's class name and a a user ID, device ID and IsActive
     *
     * @return a partition key, or null if any parameters are null
     */
    public static String generateGsiPartitionKey(String userId, String deviceId, boolean isActive) {
        if (userId == null || deviceId == null) {
            return null;
        }
        
        return String.join("#",
                UserDeviceToken.class.getSimpleName(),
                String.join(";",
                        String.join(",",
                                "UserId",
                                userId
                        ),
                        String.join(",",
                                "DeviceId",
                                deviceId
                        ),
                        String.join(",",
                                "IsActive",
                                String.valueOf(isActive)
                        )
                )
        );
    }
}
