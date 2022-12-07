package com.me.awslambdatest.dynamodb;

import com.amazonaws.services.dynamodbv2.datamodeling.*;
import com.amazonaws.services.dynamodbv2.datamodeling.encryption.DoNotTouch;
import java.util.Calendar;
import java.util.UUID;

@DynamoDBDocument
public abstract class AbstractDynamoDBEntity {
    // Primary Key (PartitionKey or PartitionKey + SortKey)
    public static final String PARTITION_KEY = "PartitionKey";
    public static final String SORT_KEY      = "SortKey";
    
    // Global Secondary Index Names
    public static final String GSI_1_NAME  = "GSI1";
    public static final String GSI_2_NAME  = "GSI2";
    public static final String GSI_3_NAME  = "GSI3";
    
    // GSI PrimaryKey
    public static final String GSI1_PARTITION_KEY  = "GSI1PartitionKey";
    public static final String GSI2_PARTITION_KEY  = "GSI2PartitionKey";
    public static final String GSI3_PARTITION_KEY  = "GSI3PartitionKey";
    
    public static final String GSI1_SORT_KEY       = "GSI1SortKey";
    public static final String GSI2_SORT_KEY       = "GSI2SortKey";
    public static final String GSI3_SORT_KEY       = "GSI3SortKey";
    
    // primary key attributes
    protected String partitionKey;
    // metadata attributes
    protected String id;
    protected Calendar createdTimestampIso;
    protected Long createdTimestampUnix;
    protected Calendar updatedTimestampIso;
    protected Long updatedTimestampUnix;
    protected Long version;
    protected Long ttl;
    
    protected AbstractDynamoDBEntity() {
        this.id = generateId();
    }
    
    @DynamoDBHashKey(attributeName = PARTITION_KEY)
    public String getPartitionKey() {
        return partitionKey;
    }
    
    public void setPartitionKey(String partitionKey) {
        this.partitionKey = partitionKey;
    }
    
    @DynamoDBAttribute(attributeName = "Id")
    @DoNotTouch
    public String getId() {
        return id;
    }
    
    public void setId(String id) {
        this.id = id;
    }
    
    @DynamoDBAttribute(attributeName = "CreatedTimestampISO")
    @DoNotTouch
    public Calendar getCreatedTimestampIso() {
        return createdTimestampIso;
    }
    
    public void setCreatedTimestampIso(Calendar createdTimestampIso) {
        this.createdTimestampIso = createdTimestampIso;
    }
    
    @DynamoDBAttribute(attributeName = "CreatedTimestampUnix")
    @DoNotTouch
    public Long getCreatedTimestampUnix() {
        return createdTimestampUnix;
    }
    
    public void setCreatedTimestampUnix(Long createdTimestampUnix) {
        this.createdTimestampUnix = createdTimestampUnix;
    }
    
    @DynamoDBAttribute(attributeName = "UpdatedTimestampISO")
    @DoNotTouch
    public Calendar getUpdatedTimestampIso() {
        return updatedTimestampIso;
    }
    
    public void setUpdatedTimestampIso(Calendar updatedTimestampIso) {
        this.updatedTimestampIso = updatedTimestampIso;
    }
    
    @DynamoDBAttribute(attributeName = "UpdatedTimestampUnix")
    @DoNotTouch
    public Long getUpdatedTimestampUnix() {
        return updatedTimestampUnix;
    }
    
    public void setUpdatedTimestampUnix(Long updatedTimestampUnix) {
        this.updatedTimestampUnix = updatedTimestampUnix;
    }
    
    @DynamoDBVersionAttribute(attributeName = "Version")
    @DoNotTouch
    public Long getVersion() {
        return version;
    }
    
    public void setVersion(Long version) {
        this.version = version;
    }
    
    @DynamoDBAttribute(attributeName = "TTL")
    @DoNotTouch
    public Long getTtl() {
        return ttl;
    }
    
    public void setTtl(Long ttl) {
        this.ttl = ttl;
    }
    
    protected abstract String generatePartitionKey();
    
    /**
     * Generates a UUID prefixed with the entity's class name and a `#` character.
     * Intended to be used to populate an entity's `Id` attribute.
     *
     * @return a unique value for the `Id` attribute
     */
    @DynamoDBIgnore
    protected String generateId() {
        return String.join("#", this.getClass().getSimpleName(), UUID.randomUUID().toString());
    }
    
    @Override
    public String toString() {
        return "BaseDynamoDBTable{"
                + "partitionKey='" + partitionKey + '\''
                + ", id='" + id + '\''
                + ", createdTimestampIso=" + createdTimestampIso
                + ", createdTimestampUnix=" + createdTimestampUnix
                + ", updatedTimestampIso=" + updatedTimestampIso
                + ", updatedTimestampUnix=" + updatedTimestampUnix
                + ", version=" + version
                + ", ttl=" + ttl
                + '}';
    }
    
    public void setCreatedTimestamp(Calendar date) {
        this.createdTimestampIso = date;
        this.createdTimestampUnix = date.getTimeInMillis();
    }
    
    public void setUpdatedTimestamp(Calendar date) {
        this.updatedTimestampIso = date;
        this.updatedTimestampUnix = date.getTimeInMillis();
    }
}
