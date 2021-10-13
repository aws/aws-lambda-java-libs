package example;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.events.KinesisAnalyticsInputPreprocessingResponse;
import com.amazonaws.services.lambda.runtime.events.KinesisFirehoseEvent;
import com.amazonaws.services.lambda.runtime.tests.annotations.Event;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;

import static java.nio.charset.StandardCharsets.UTF_8;

public class KinesisFirehoseEventHandlerTest {

    private Context context; // intentionally null as it's not used in the test

    @ParameterizedTest
    @Event(value = "event.json", type = KinesisFirehoseEvent.class)
    public void testEventHandler(KinesisFirehoseEvent event) {
        KinesisFirehoseEventHandler kinesisFirehoseEventHandler = new KinesisFirehoseEventHandler();
        KinesisAnalyticsInputPreprocessingResponse response = kinesisFirehoseEventHandler.handleRequest(event, context);

        String expectedString = "\n!dlroW olleH";
        KinesisAnalyticsInputPreprocessingResponse.Record firstRecord = response.getRecords().get(0);
        Assertions.assertEquals(expectedString, UTF_8.decode(firstRecord.getData()).toString());
        Assertions.assertEquals(KinesisAnalyticsInputPreprocessingResponse.Result.Ok, firstRecord.getResult());
    }
}