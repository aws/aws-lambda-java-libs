# Example using the AWS C++ Lambda runtime and Amazon API Gateway

In this example, we'll build a simple "Hello, World" lambda function that can be invoked using an api endpoint created using Amazon API gateway. This example can be viewed as the C++ counterpart to the NodeJS "Hello, World" API example as viewed [here](https://docs.aws.amazon.com/apigateway/latest/developerguide/api-gateway-create-api-as-simple-proxy-for-lambda.html). At the end of this example, you should be able to invoke your lambda via an api endpoint and receive a raw JSON response. This example employs the use of the AWS C++ SDK to parse the request and write the necessary response. 
 
## Build the AWS C++ SDK
Start by building the SDK from source.

```bash
$ mkdir ~/install
$ git clone https://github.com/aws/aws-sdk-cpp.git
$ cd aws-sdk-cpp
$ mkdir build
$ cd build
$ cmake .. -DBUILD_ONLY="core" \
  -DCMAKE_BUILD_TYPE=Release \
  -DBUILD_SHARED_LIBS=OFF \
  -DENABLE_UNITY_BUILD=ON \
  -DCUSTOM_MEMORY_MANAGEMENT=OFF \
  -DCMAKE_INSTALL_PREFIX=~/install \
  -DENABLE_UNITY_BUILD=ON
$ make
$ make install
```

## Build the Runtime
We need to build the C++ Lambda runtime as outlined in the other examples.

```bash
$ git clone https://github.com/awslabs/aws-lambda-cpp-runtime.git
$ cd aws-lambda-cpp-runtime
$ mkdir build
$ cd build
$ cmake .. -DCMAKE_BUILD_TYPE=Release \
  -DBUILD_SHARED_LIBS=OFF \
  -DCMAKE_INSTALL_PREFIX=~/install \
$ make
$ make install
```

## Build the application
The next step is to build the Lambda function in `main.cpp` and run the packaging command as follows:

```bash
$ mkdir build
$ cd build
$ cmake .. -DCMAKE_BUILD_TYPE=Release -DCMAKE_PREFIX_PATH=~/install
$ make
$ make aws-lambda-package-api
```

You should now have a zip file called `api.zip`. Follow the instructions in the main README to upload it and return here once complete.

## Using Amazon API Gateway 
For the rest of this example, we will use the AWS Management Console to create the API endpoint using Amazon API Gateway.

1. Navigate to AWS Lambda within the console [here](https://console.aws.amazon.com/lambda/home)   
1. Select the newly created function. Within the specific function, the "Designer" window should appear.   
1. Simply click "Add trigger" -> "API Gateway" -> "Create an API". Please view the settings below.
   * API Type: HTTP API
   * Security: Open
   * API name: Hello-World-API (or desired name)
   * Deployment stage: default
1. Once you have added the API gateway, locate the newly created endpoint. View how to test the endpoint below.

## Test the endpoint
Feel free to test the endpoint any way you desire. Below is a way to test using cURL: 

```
curl -v -X POST \
  '<YOUR-API-ENDPOINT>?name=Bradley&city=Chicago' \
  -H 'content-type: application/json' \
  -H 'day: Sunday' \
  -d '{ "time": "evening" }'
```

With the expected response being: 
```
{
  "message": "Good evening, Bradley of Chicago. Happy Sunday!"
}
```
