module aws.lambda.log4j2 {

  exports com.amazonaws.services.lambda.runtime.log4j2;

  requires aws.lambda.core;
  requires org.apache.logging.log4j;
  requires org.apache.logging.log4j.core;

}
