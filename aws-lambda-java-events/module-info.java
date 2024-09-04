module aws.lambda.events {

  exports com.amazonaws.services.lambda.runtime.events;
  exports com.amazonaws.services.lambda.runtime.events.models;
  exports com.amazonaws.services.lambda.runtime.events.models.dynamodb;
  exports com.amazonaws.services.lambda.runtime.events.models.kinesis;
  exports com.amazonaws.services.lambda.runtime.events.models.s3;

  requires static lombok;
  requires org.joda.time;
}
