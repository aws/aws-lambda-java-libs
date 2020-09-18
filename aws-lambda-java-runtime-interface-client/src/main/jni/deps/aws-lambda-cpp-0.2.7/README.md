[![GitHub](https://img.shields.io/github/license/awslabs/aws-lambda-cpp.svg)](https://github.com/awslabs/aws-lambda-cpp/blob/master/LICENSE)
![CodeBuild](https://codebuild.us-west-2.amazonaws.com/badges?uuid=eyJlbmNyeXB0ZWREYXRhIjoiQkN1b0srbWtnUjNibFVyL2psNmdaM0l4RnVQNzVBeG84QnQvUjRmOEJVdXdHUXMxZ25iWnFZQUtGTkUxVGJhcGZaVEhXY2JOSTFHTlkvaGF2RDRIZlpVPSIsIml2UGFyYW1ldGVyU3BlYyI6IjRiS3hlRjFxVFZHSWViQmQiLCJtYXRlcmlhbFNldFNlcmlhbCI6MX0%3D&branch=master)
[![Language grade: C/C++](https://img.shields.io/lgtm/grade/cpp/g/awslabs/aws-lambda-cpp.svg?logo=lgtm&logoWidth=18)](https://lgtm.com/projects/g/awslabs/aws-lambda-cpp/context:cpp)
## AWS Lambda C++ Runtime

C++ implementation of the lambda runtime API

## Design Goals
1. Negligible cold-start overhead (single digit millisecond).
2. Freedom of choice in compilers, build platforms and C standard library versions.

## Building and Installing the Runtime
Since AWS Lambda runs on GNU/Linux, you should build this runtime library and your logic on GNU/Linux as well.

### Prerequisites
Make sure you have the following packages installed first:
1. CMake (version 3.9 or later)
1. git
1. Make or Ninja
1. zip
1. libcurl-devel (on Debian-basded distros it's libcurl4-openssl-dev)

In a terminal, run the following commands:
```bash
$ git clone https://github.com/awslabs/aws-lambda-cpp.git
$ cd aws-lambda-cpp
$ mkdir build
$ cd build
$ cmake .. -DCMAKE_BUILD_TYPE=Release -DCMAKE_INSTALL_PREFIX=~/lambda-install
$ make && make install
```

To consume this library in a project that is also using CMake, you would do:

```cmake
cmake_minimum_required(VERSION 3.9)
set(CMAKE_CXX_STANDARD 11)
project(demo LANGUAGES CXX)
find_package(aws-lambda-runtime)
add_executable(${PROJECT_NAME} "main.cpp")
target_link_libraries(${PROJECT_NAME} PRIVATE AWS::aws-lambda-runtime)
target_compile_features(${PROJECT_NAME} PRIVATE "cxx_std_11")
target_compile_options(${PROJECT_NAME} PRIVATE "-Wall" "-Wextra")

# this line creates a target that packages your binary and zips it up
aws_lambda_package_target(${PROJECT_NAME})
```

And here is how a sample `main.cpp` would look like:
```cpp
#include <aws/lambda-runtime/runtime.h>

using namespace aws::lambda_runtime;

static invocation_response my_handler(invocation_request const& req)
{
    if (req.payload.length() > 42) {
        return invocation_response::failure("error message here"/*error_message*/,
                                            "error type here" /*error_type*/);
    }

    return invocation_response::success("json payload here" /*payload*/,
                                        "application/json" /*MIME type*/);
}

int main()
{
    run_handler(my_handler);
    return 0;
}
```

And finally, here's how you would package it all. Run the following commands from your application's root directory:

```bash
$ mkdir build
$ cd build
$ cmake .. -DCMAKE_BUILD_TYPE=Debug -DCMAKE_INSTALL_PREFIX=~/lambda-install
$ make
$ make aws-lambda-package-demo
```
The last command above `make aws-lambda-package-demo` will create a zip file called `demo.zip` in the current directory.

Now, create an IAM role and the Lambda function via the AWS CLI.

First create the following trust policy JSON file

```
$ cat trust-policy.json
{
 "Version": "2012-10-17",
  "Statement": [
    {
      "Effect": "Allow",
      "Principal": {
        "Service": ["lambda.amazonaws.com"]
      },
      "Action": "sts:AssumeRole"
    }
  ]
}

```
Then create the IAM role:

```bash
$ aws iam create-role --role-name lambda-demo --assume-role-policy-document file://trust-policy.json
```

Note down the role Arn returned to you after running that command. We'll need it in the next steps:

Attach the following policy to allow Lambda to write logs in CloudWatch:
```bash
$ aws iam attach-role-policy --role-name lambda-demo --policy-arn arn:aws:iam::aws:policy/service-role/AWSLambdaBasicExecutionRole
```

Make sure you attach the appropriate policies and/or permissions for any other AWS services that you plan on using.

And finally, create the Lambda function:

```
$ aws lambda create-function --function-name demo \
--role <specify role arn from previous step here> \
--runtime provided --timeout 15 --memory-size 128 \
--handler demo --zip-file fileb://demo.zip
```

And to invoke the function:
```bash
$ aws lambda invoke --function-name demo --payload '{"answer":42}' output.txt
```

## Using the C++ SDK for AWS with this runtime
This library is completely independent from the AWS C++ SDK. You should treat the AWS C++ SDK as just another dependency in your application.
See [the examples section](https://github.com/awslabs/aws-lambda-cpp/tree/master/examples/) for a demo utilizing the AWS C++ SDK with this Lambda runtime.

## Supported Compilers
Any *fully* compliant C++11 compiler targeting GNU/Linux x86-64 should work. Please avoid compiler versions that provide half-baked C++11 support.

- Use GCC v5.x or above
- Use Clang v3.3 or above

## Packaging, ABI, GNU C Library, Oh My!
Lambda runs your code on some version of Amazon Linux. It would be a less than ideal customer  experience if you are forced to build your application on that platform and that platform only.

However, the freedom to build on any linux distro brings a challenge. The GNU C Library ABI. There is no guarantee the platform used to build the Lambda function has the same GLIBC version as the one used by AWS Lambda. In fact, you might not even be using GNU's implementation. For example you could build a C++ Lambda function using musl libc.

To ensure that your application will run correctly on Lambda, we must package the entire C runtime library with your function.
If you choose to build on the same [Amazon Linux version used by lambda](https://docs.aws.amazon.com/lambda/latest/dg/current-supported-versions.html), you can avoid packaging the C runtime in your zip file.
This can be done by passing the `NO_LIBC` flag in CMake as follows:

```cmake
aws_lambda_package_target(${PROJECT_NAME} NO_LIBC)
```
### Common Pitfalls with Packaging

* Any library dependency your Lambda function has that is dynamically loaded via `dlopen` will NOT be automatically packaged. You **must** add those dependencies manually to the zip file.
This applies to any configuration or resource files that your code depends on.

* If you are making HTTP calls over TLS (https), keep in mind that the CA bundle location is different between distros.
For example, if you are using the AWS C++ SDK, it's best to set the following configuration options:

```cpp
Aws::Client::ClientConfiguration config;
config.caFile = "/etc/pki/tls/certs/ca-bundle.crt";
```
If you are not using the AWS C++ SDK, but happen to be using libcurl directly, you can set the CA bundle location by doing:
```c
curl_easy_setopt(curl_handle, CURLOPT_CAINFO, "/etc/pki/tls/certs/ca-bundle.crt");
```

## FAQ & Troubleshooting
1. **Why is the zip file so large? what are all those files?**
   Typically, the zip file is large because we have to package the entire C standard library.
   You can reduce the size by doing some or all of the following:
   - Ensure you're building in release mode `-DCMAKE_BUILD_TYPE=Release`
   - If possible, build your function using musl libc, it's tiny. The easiest way to do this, assuming your code is portable, is to build on Alpine linux, which uses musl libc by default.
1. **How to upload a zip file that's bigger than 50MB via the CLI?**
    Upload your zip file to S3 first:
    ```bash
    $ aws s3 cp demo.zip s3://mys3bucket/demo.zip
    ```
    NOTE: you must use the same region for your S3 bucket as the lambda.

    Then you can create the Lambda function this way:

    ```bash
    $ aws lambda create-function --function-name demo \
    --role <specify role arn here> \
    --runtime provided --timeout 15 --memory-size 128 \
    --handler demo
    --code "S3Bucket=mys3bucket,S3Key=demo.zip"
    ```
1. **My code is crashing, how can I debug it?**

   - Starting with [v0.2.0](https://github.com/awslabs/aws-lambda-cpp/releases/tag/v0.2.0) you should see a stack-trace of the crash site in the logs (which are typically stored in CloudWatch).
     - To get a more detailed stack-trace with source-code information such as line numbers, file names, etc. you need to install one of the following packages:
       - On Debian-based systems -  `sudo apt install libdw-dev` or `sudo apt install binutils-dev`
       - On RHEL based systems -  `sudo yum install elfutils-devel` or `sudo yum install binutils-devel`
       If you have either of those packages installed, CMake will detect them and automatically link to them. No other
       steps are required.
   - Turn up the logging verbosity to the maximum.
     - Build the runtime in Debug mode. `-DCMAKE_BUILD_TYPE=Debug`. Verbose logs are enabled by default in Debug builds.
     - To enable verbose logs in Release builds, build the runtime with the following CMake flag `-DLOG_VERBOSITY=3`
     - If you are using the AWS C++ SDK, see [this FAQ](https://github.com/aws/aws-sdk-cpp/wiki#how-do-i-turn-on-logging) on how to adjust its logging verbosity
   - Run your code locally on an Amazon Linux AMI or Docker container to reproduce the problem
     - If you go the AMI route, [use the official one](https://docs.aws.amazon.com/lambda/latest/dg/current-supported-versions.html) recommended by AWS Lambda 
     - If you go the Docker route, use the following command to launch a container running AL2017.03
       `$ docker run -v /tmp:/tmp -it --security-opt seccomp=unconfined amazonlinux:2017.03`
       The `security-opt` argument is necessary to run `gdb`, `strace`, etc.
1. **CURL problem with the SSL CA cert**
   - Make sure you are using a `libcurl` version built with OpenSSL, or one of its flavors (BoringSSL, LibreSSL)
   - Make sure you tell `libcurl` where to find the CA bundle file.
   - You can try hitting the non-TLS version of the endpoint if available. (Not Recommended).
1. **No known conversion between `std::string` and `Aws::String`**
    - Either turn off custom memory management in the AWS C++ SDK or build it as a static library (`-DBUILD_SHARED_LIBS=OFF`)

## License

This library is licensed under the Apache 2.0 License. 
