### September 30, 2026
`2.13.0`
- **Behavior change:** POJO handler input and output are now always serialized with the runtime's Jackson serializer. Previously, invocations whose client context reported `env.platform` as `Android` (as set by the archived AWS Mobile SDK for Android `LambdaInvokerFactory`) were serialized with Gson, which binds fields directly instead of bean properties. Invocations without that client context are unaffected. Android callers now receive the same JSON as every other caller, which differs from before as follows:
  - Private fields without getters or setters are no longer read from the request or written to the response. Add getters and setters, or make the fields public.
  - Response keys follow getter names instead of field names: a field `isActive` with `isActive()` becomes `active`, and a field `mName` with `getName()` becomes `name`. Name the accessors after the JSON key (`getIsActive`), or use a public field with that name.
  - Getters without a backing field, and `transient` fields that have getters, add keys to the response. Rename methods that should not be serialized so they are not getters.
  - Fields with a getter but no setter keep their default value instead of the request value. Add a setter.
  - Types without a no-argument constructor fail the invocation with `An error occurred during JSON parsing`. Add a no-argument constructor and setters.
  - Numbers in `Object` or `Map<String, Object>` fields are read as `Integer`, `Long` or `Double` instead of always `Double`, so `1` is no longer echoed as `1.0`.
  - `java.util.Date` values are written as numbers instead of formatted text.
  - All of the above also apply to nested objects and list elements.

  Each case and its fix is covered in [`GsonToJacksonPojoSerializationTest`](https://github.com/aws/aws-lambda-java-libs/blob/main/aws-lambda-java-tests/src/test/java/com/amazonaws/services/lambda/runtime/tests/GsonToJacksonPojoSerializationTest.java). If changing the types is not possible, register a [`CustomPojoSerializer`](https://github.com/aws/aws-lambda-java-libs/tree/main/samples/custom-serialization/gson); note that a custom serializer applies to every invocation of the function, not only to Android callers.

### September 2, 2026
`2.12.1`
- Emit a structured `runtime_worker_pool_initializing` DEBUG log event once during INIT in multi-concurrent (Lambda Managed Instances) mode, reporting the worker pool size (`workerCount`) and the maximum concurrency the execution environment supports (`executionEnvironmentMaxConcurrency`). Only visible when the function log level is DEBUG or lower; not emitted for standard on-demand functions.

### July 17, 2026
`2.12.0`
- Add `Lambda-Runtime-Invocation-Id` header support for cross-wiring protection. The RIC now echoes the invocation ID received from RAPID on `/next` back on `/response` and `/error`, enabling RAPID to detect and reject stale responses from timed-out invocations.

### May 13, 2026
`2.11.0`
- Update aws-lambda-java-serialization dependency to 1.4.1

### March 19, 2026
`2.10.1`
- Revert aws-lambda-java-serialization dependency to 1.2.0

### March 12, 2026
`2.10.0`
- Update aws-lambda-java-serialization dependency to 1.3.0

### March 12, 2026
`2.9.0`
- Update aws-lambda-java-serialization dependency to 1.2.0

### September 22, 2025
`2.8.7`
- Remove Minimum and Maximum Limits of AWS_LAMBDA_MAX_CONCURRENCY.

### September 22, 2025
`2.8.6`
- Set Multiconcurrent Trace ID using utils-lite.

### September 17, 2025
`2.8.5`
- Log errorType and errorMessage from RAPID in C++ Client.
- Performance Upgrade for Multiconcurrency Mode.

### September 9, 2025
`2.8.4`
- Make Trace ID Accessible through Context Object.

### July 19, 2025
`2.8.3`
- Ensure EventHandlerLoader Thread Safety.

### June 26, 2025
`2.8.2`
- Allow AWS_LAMBDA_MAX_CONCURRENCY to be One. Crash the RIC if it is set to an un-parsable string to an integer or an out of bounds value.

### June 26, 2025
`2.8.1`
- Refactoring

### June 26, 2025
`2.8.0`
- Refactoring

### May 21, 2025
`2.7.0`
- Adding support for multi tenancy ([#540](https://github.com/aws/aws-lambda-java-libs/pull/540))

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
