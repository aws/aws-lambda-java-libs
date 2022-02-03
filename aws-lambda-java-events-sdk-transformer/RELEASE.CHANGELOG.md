### February 03, 2022
`3.1.0`:
-  Make DynamodbAttributeValueTransformer v1 and v2 return empty list instead of null for empty list attribute ([#309](https://github.com/aws/aws-lambda-java-libs/pull/309))

### November 24, 2021
`3.0.7`:
- Bumped `aws-lambda-java-events` to version `3.11.0`

### September 02, 2021
`3.0.6`:
- Fixed NPE when UserIdentity, OldImage, or NewImage is null ([#264](https://github.com/aws/aws-lambda-java-libs/pull/264))

### August 26, 2021
`3.0.5`:
- Bumped `aws-lambda-java-events` to version `3.10.0`

### June 2, 2021
`3.0.4`:
- Bumped `aws-lambda-java-events` to version `3.9.0`

### March 24, 2021
`3.0.3`:
- Bumped `aws-lambda-java-events` to version `3.8.0`

### December 16, 2020
`3.0.2`:
- Bumped `aws-lambda-java-events` to version `3.7.0`

### December 10, 2020
`3.0.1`:
- Change visibility scope of `Map<String, AttributeValue> toAttributeValueMapVx(Map<String, AttributeValue>)` to `public`

### December 09, 2020
`3.0.0`:
- Added AWS SDK V1 transformers for `DynamodbEvent` in `aws-lambda-java-events` versions `3.0.0` and up
- Moved existing SDK v2 transformers into `v2` package (from `com.amazonaws.services.lambda.runtime.events.transformers` to `com.amazonaws.services.lambda.runtime.events.transformers.v2`)
- Bumped `software.amazon.awssdk:dynamodb` to version `2.15.40`

### November 06, 2020
`2.0.8`:
- Bumped `aws-lambda-java-events` to version `3.6.0`
- Bumped `junit-jupiter-engine` to version `5.7.0`

### October 28, 2020
`2.0.7`:
- Bumped `aws-lambda-java-events` to version `3.5.0`

### October 07, 2020
`2.0.6`:
- Fixed NPE when UserIdentity is null ([#169](https://github.com/aws/aws-lambda-java-libs/pull/169))
- Bumped `aws-lambda-java-events` to version `3.4.0`

### September 23, 2020
`2.0.5`:
- Bumped `aws-lambda-java-events` to version `3.3.1`

### September 14, 2020
`2.0.4`:
- Bumped `aws-lambda-java-events` to version `3.3.0`

### August 11, 2020
`2.0.3`:
- Bumped `aws-lambda-java-events` to version `3.2.0`

### July 31, 2020
`2.0.2`:
- Bumped `aws-lambda-java-events` to version `3.1.1`

### June 15, 2020
`2.0.1`:
- Fixed NPE when mapping insert/delete events ([#143](https://github.com/aws/aws-lambda-java-libs/pull/143))

### May 20, 2020
`2.0.0`:
- Updated AWS SDK V2 transformers for `DynamodbEvent` to work with `aws-lambda-java-events` versions `3.0.0` and up
- Bumped `software.amazon.awssdk:dynamodb` to version `2.13.18`

### April 29, 2020
`1.0.0`:
- Added AWS SDK V2 transformers for `DynamodbEvent` in `aws-lambda-java-events` versions up to and including `2.x`
