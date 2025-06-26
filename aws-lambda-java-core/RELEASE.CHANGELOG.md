### May 26, 2025
`1.3.0`
- Adding support for multi tenancy ([#545](https://github.com/aws/aws-lambda-java-libs/pull/545))

### August 17, 2023
`1.2.3`:
- Extended logger interface with level-aware logging backend functions

### November 09, 2022
`1.2.2`:
- Added new `CustomPojoSerializer` interface
- Removed unnecessary usage of public on interface methods (aws#172) 

### April 28, 2020
`1.2.1`:
- Added missing XML namespace declarations to `pom.xml` file ([#97](https://github.com/aws/aws-lambda-java-libs/issues/97))
- Updated `nexusUrl` in `pom.xml` file ([#108](https://github.com/aws/aws-lambda-java-libs/issues/108))

### November 21, 2017
`1.2.0`:
- Added method to log byte array to `LambdaLogger`

### October 07, 2015
`1.1.0`:
- Added `LambdaRuntime` and `LambdaRuntimeInternal`
- Added `getInstallationId()` to `Client`
- Added `getFunctionVersion()` and `getInvokedFunctionArn()` to `Context`

### June 15, 2015
`1.0.0`:
- Initial support for java in AWS Lambda
