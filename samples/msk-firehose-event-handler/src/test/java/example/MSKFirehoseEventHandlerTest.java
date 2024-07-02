package example;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.tests.annotations.Event;
import com.amazonaws.services.lambda.runtime.events.MSKFirehoseEvent;
import com.amazonaws.services.lambda.runtime.events.MSKFirehoseResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;

import static java.nio.charset.StandardCharsets.UTF_8;

public class MSKFirehoseEventHandlerTest {

    private Context context; // intentionally null as it's not used in the test

    @ParameterizedTest
    @Event(value = "event.json", type = MSKFirehoseEvent.class)
    public void testEventHandler(MSKFirehoseEvent event) {
        Sample Sample = new Sample();
        MSKFirehoseResponse response = Sample.handleRequest(event, context);

        String expectedString = "{\"Name\":\"Hello World\"}";
        MSKFirehoseResponse.Record firstRecord = response.getRecords().get(0);
        Assertions.assertEquals(expectedString, UTF_8.decode(firstRecord.getKafkaRecordValue()).toString());
        Assertions.assertEquals(MSKFirehoseResponse.Result.Ok, firstRecord.getResult());
    }
}
