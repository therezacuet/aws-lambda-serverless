# Serverless REST API in Java/Maven using DynamoDB


![image](https://user-images.githubusercontent.com/8188/38645675-ec708d0e-3db2-11e8-8f8b-a4a37ed612b9.png)


The sample serverless service will create a REST API. It will be deployed to AWS. The data will be stored in a DynamoDB table.


## Install Pre-requisites

* `node` and `npm`
* Install the JDK and NOT the Java JRE from [Oracle JDK](http://www.oracle.com/technetwork/java/javase/downloads/index.html). And set the following:
`export JAVA_HOME=/Library/Java/JavaVirtualMachines/jdk-10.jdk/Contents/Home`
* [Apache Maven](https://maven.apache.org/). After [downloading](https://maven.apache.org/download.html) and [installing](https://maven.apache.org/install.html) Apache Maven, please add the `apache-maven-x.x.x` folder to the `PATH` environment variable.

### Test Pre-requisites

Test Java installation:

```
$ java --version

jopenjdk 11.0.14 2022-01-18 LTS
OpenJDK Runtime Environment Corretto-11.0.14.9.1 (build 11.0.14+9-LTS)
OpenJDK 64-Bit Server VM Corretto-11.0.14.9.1 (build 11.0.14+9-LTS, mixed mode)
```

Test Maven installation:

```
$ mvn -v

Apache Maven 3.8.1 (05c21c65bdfed0f71a2f2ada8b84da59348c4c5d)
Maven home: /opt/homebrew/Cellar/maven/3.8.1/libexec
Java version: 11.0.14, vendor: Amazon.com Inc., runtime: /Library/Java/JavaVirtualMachines/corretto-11.0.14/Contents/Home
Default locale: en_BD, platform encoding: UTF-8
OS name: "mac os x", version: "12.5", arch: "x86_64", family: "mac"
```

## Build the Java project

Create the java artifact (jar) by:

```
$ cd aws-lambda-serverless
$ mvn clean install

[INFO] Scanning for projects...
[INFO]
[INFO] --------------------< org.awslambda:pushnotification >---------------------
[INFO] Building pushnotification dev
[INFO] --------------------------------[ jar ]---------------------------------
[INFO]
[INFO] --- maven-clean-plugin:2.5:clean (default-clean) @ pushnotification ---

...
...

```

We can see that we have an artifact in the `target` folder named `pushnotification-dev.jar`.

## Deploy the serverless app

```
$ sls deploy

Serverless: Packaging service...
Serverless: Creating Stack...
Serverless: Checking Stack create progress...
.....
Serverless: Stack create finished...
Serverless: Uploading CloudFormation file to S3...
Serverless: Uploading artifacts...
Serverless: Validating template...
Serverless: Updating Stack...
Serverless: Checking Stack update progress...
..................................
Serverless: Stack update finished...
Service Information
service: pushnotification
stage: dev
region: ap-northeast-1
stack: pushnotification-dev
api keys:
  None
endpoints:
  GET - https://xxxxxxxxx.execute-api.ap-northeast-1.amazonaws.com/dev/user/{id}/subscriptions
  POST - https://xxxxxxxxx.execute-api.ap-northeast-1.amazonaws.com/dev/user/subscription
  DELETE - https://xxxxxxxxx.execute-api.ap-northeast-1.amazonaws.com/dev/user/{id}/device/{deviceId}
functions:
  getUserSubscription: pushnotification-dev-getUserSubscription
  registerUserDevice: pushnotification-dev-registerUserDevice
  unregisterUserDevice: pushnotification-dev-unregisterUserDevice
```

## Test the API

Let's invoke each of the four functions that we created as part of the app.

### Register User Device

```
$ curl -X POST https://xxxxxxxxx.execute-api.ap-northeast-1.amazonaws.com/dev/user/subscription -d '{
    "UserId": "2",
    "DeviceId": "7384658734443685734",
    "Token": "34878934653gf3434764578463587634",
    "IsActive": true
}'

{
    "partitionKey": "UserDeviceToken#UserId,2;DeviceId,7384658734443685734;Token,34878934653gf3434764578463587634;IsActive,true",
    "id": "UserDeviceToken#26b027a8-90e3-4127-8985-3041cae0c5fc",
    "createdTimestampIso": null,
    "createdTimestampUnix": null,
    "updatedTimestampIso": null,
    "updatedTimestampUnix": null,
    "version": 1,
    "ttl": null,
    "gsi1PartitionKey": "UserDeviceToken#UserId,2;DeviceId,7384658734443685734;IsActive,true",
    "userId": "2",
    "deviceId": "7384658734443685734",
    "token": "34878934653gf3434764578463587634",
    "active": true
}
```

### List User Device

```
$ curl https://xxxxxxxxx.execute-api.ap-northeast-1.amazonaws.com/dev/user/{id}/subscriptions

[
    {
        "partitionKey": "UserDeviceToken#UserId,2;DeviceId,7384658734443685734;Token,34878934653gf3434764578463587634;IsActive,true",
        "id": "UserDeviceToken#26b027a8-90e3-4127-8985-3041cae0c5fc",
        "createdTimestampIso": null,
        "createdTimestampUnix": null,
        "updatedTimestampIso": null,
        "updatedTimestampUnix": null,
        "version": 1,
        "ttl": null,
        "gsi1PartitionKey": "UserDeviceToken#UserId,2;DeviceId,7384658734443685734;IsActive,true",
        "userId": "2",
        "deviceId": "7384658734443685734",
        "token": "34878934653gf3434764578463587634",
        "active": true
    }
]
```

**No Device(s) Found:**

```
$ curl https://xxxxxxxxx.execute-api.ap-northeast-1.amazonaws.com/dev/user/{id}/subscriptions

[]
```

### UnRegister Device

```
$ curl -X DELETE https://xxxxxxxxx.execute-api.ap-northeast-1.amazonaws.com/dev/user/{id}/device/{deviceId}
```
