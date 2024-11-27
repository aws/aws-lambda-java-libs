### August 7, 2024
`2.6.0`
- Runtime API client improvements: use Lambda-Runtime-Function-Error-Type for reporting errors in format "Runtime.<Error>" 

### June 28, 2024
`2.5.1`
- Runtime API client improvements: fix a DNS cache issue
- Runtime API client improvements: fix circular exception references causing stackOverflow

### March 20, 2024
`2.5.0`
- Runtime API client improvements ([#471](https://github.com/aws/aws-lambda-java-libs/pull/471))

### February 27, 2024
`2.4.2`
- Exceptions caught by the runtime are logged as ERROR in JSON mode

### September 4, 2023
`2.4.1`
- Null pointer bugfix ([#439](https://github.com/aws/aws-lambda-java-libs/pull/439))

### August 29, 2023
`2.4.0`
- Logging improvements ([#436](https://github.com/aws/aws-lambda-java-libs/pull/436))

### July 17, 2023
`2.3.3`
- Build platform specific JAR files
- NativeClient optimisations

### April 14, 2023
`2.3.2`
- Add curl patch

### March 16, 2023
`2.3.1`
- ignore module-info for CDS preparation purposes
- clear thread interrupted flag instead of exiting Lambda Runtime

### March 14, 2023
`2.3.0`
- added CRaC context implementation
- added runtime hooks execution logic
- updated serialisation dependency
- reduced Reflection API usage

### February 3, 2023
`2.2.0`
- Added timestamps to TLV
- Removed legacy `init` method support
- libcurl updated to version 7.86
- Support sockets as transport for framed telemetry
- Updated aws-lambda-java-core to 1.2.2

### April 11, 2022
`2.1.1`
- fix: Re-build of the x86_64/aarch64 artifacts

### January 20, 2022
`2.1.0`
- fix: Added support for ARM64 architecture

### Sept 29, 2021
`2.0.0`
- Added support for ARM64 architecture

### June 02, 2021
`1.1.0`:
- Added reserved environment variables constants ([#238](https://github.com/aws/aws-lambda-java-libs/pull/238))
- Updated libcurl dependency to `7.77.0` ([#249](https://github.com/aws/aws-lambda-java-libs/pull/249))

### December 01, 2020
`1.0.0`:
- Initial release of AWS Lambda Java Runtime Interface Client
