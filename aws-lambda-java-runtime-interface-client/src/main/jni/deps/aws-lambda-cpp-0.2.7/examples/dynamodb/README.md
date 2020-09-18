# Example using the AWS C++ SDK with Lambda & DynamoDB

We'll build a Lambda function that can be used as an API Gateway proxy to fetch records from a DynamoDB table.
To also show case how this can be done on a Linux distro other than Amazon Linux, you can use the Dockerfile in this directory to create an Alpine Linux environment in which you can run the following instructions.

That being said, the instructions below should work on any Linux distribution.

## Build the AWS C++ SDK
Start by building the SDK from source.
```bash
$ mkdir ~/install
$ git clone https://github.com/aws/aws-sdk-cpp.git
$ cd aws-sdk-cpp
$ mkdir build
$ cd build
$ cmake .. -DBUILD_ONLY="dynamodb" \
  -DCMAKE_BUILD_TYPE=Release \
  -DBUILD_SHARED_LIBS=OFF \
  -DENABLE_UNITY_BUILD=ON \
  -DCUSTOM_MEMORY_MANAGEMENT=OFF \
  -DCMAKE_INSTALL_PREFIX=~/install \
  -DENABLE_UNITY_BUILD=ON

$ make -j 4
$ make install
```

## Build the Runtime
Now let's build the C++ Lambda runtime, so in a separate directory clone this repository and follow these steps:

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
The last step is to build the Lambda function in `main.cpp` and run the packaging command as follows:

```bash
$ cmake .. -DCMAKE_BUILD_TYPE=Release -DCMAKE_PREFIX_PATH=~/install
$ make
$ make aws-lambda-package-ddb-demo
```

You should now have a zip file called `ddb-demo.zip`. Follow the instructions in the main README to upload it and invoke the lambda.
