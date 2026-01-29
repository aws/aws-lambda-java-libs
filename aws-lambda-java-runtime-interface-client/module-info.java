module aws.lambda.runtime.client {

  exports com.amazonaws.services.lambda.crac;
  exports com.amazonaws.services.lambda.runtime.api.client;
  exports com.amazonaws.services.lambda.runtime.api.client.api;
  exports com.amazonaws.services.lambda.runtime.api.client.logging;
  exports com.amazonaws.services.lambda.runtime.api.client.runtimeapi;
  exports com.amazonaws.services.lambda.runtime.api.client.runtimeapi.converters;
  exports com.amazonaws.services.lambda.runtime.api.client.runtimeapi.dto;
  exports com.amazonaws.services.lambda.runtime.api.client.util;

  requires transitive aws.lambda.core;
  requires transitive aws.lambda.serialization;
  requires jdk.unsupported;

}
