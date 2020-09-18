# Example using the AWS C++ SDK with Lambda

We'll build a lambda that downloads an image file from S3 and sends it back in the response as Base64 encoded that can be displayed in a web page for example.
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
$ cmake .. -DBUILD_ONLY="s3" \
  -DCMAKE_BUILD_TYPE=Release \
  -DBUILD_SHARED_LIBS=OFF \
  -DCUSTOM_MEMORY_MANAGEMENT=OFF \
  -DCMAKE_INSTALL_PREFIX=~/install \
  -DENABLE_UNITY_BUILD=ON

$ make
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
$ make aws-lambda-package-encoder
```

You should now have a zip file called `encoder.zip`. Follow the instructions in the main README to upload it and invoke the lambda.
