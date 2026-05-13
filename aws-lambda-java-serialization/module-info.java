module aws.lambda.serialization {

  exports com.amazonaws.services.lambda.runtime.serialization;
  exports com.amazonaws.services.lambda.runtime.serialization.events;
  exports com.amazonaws.services.lambda.runtime.serialization.events.mixins;
  exports com.amazonaws.services.lambda.runtime.serialization.events.modules;
  exports com.amazonaws.services.lambda.runtime.serialization.events.serializers;
  exports com.amazonaws.services.lambda.runtime.serialization.factories;
  exports com.amazonaws.services.lambda.runtime.serialization.util;

  requires transitive com.fasterxml.jackson.databind;
  requires static com.fasterxml.jackson.datatype.joda;
  requires static org.json;
  requires static com.google.gson;
  requires static com.fasterxml.jackson.datatype.jsr310;
  requires static com.fasterxml.jackson.datatype.jdk8;

}
