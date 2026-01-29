module aws.lambda.events.sdk.transformer {

  exports com.amazonaws.services.lambda.runtime.events.transformers.v1;
  exports com.amazonaws.services.lambda.runtime.events.transformers.v1.dynamodb;
  exports com.amazonaws.services.lambda.runtime.events.transformers.v2;
  exports com.amazonaws.services.lambda.runtime.events.transformers.v2.dynamodb;

  requires transitive aws.lambda.events;
  requires static aws.java.sdk.dynamodb;
}
