package example;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.KinesisAnalyticsInputPreprocessingResponse;
import com.amazonaws.services.lambda.runtime.events.KinesisFirehoseEvent;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import static com.amazonaws.services.lambda.runtime.events.KinesisAnalyticsInputPreprocessingResponse.Result.Ok;
import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * A sample KinesisFirehoseEvent handler
 *
 * For more information see the developer guide - https://docs.aws.amazon.com/firehose/latest/dev/data-transformation.html
 */
public class KinesisFirehoseEventHandler implements RequestHandler<KinesisFirehoseEvent, KinesisAnalyticsInputPreprocessingResponse> {

    @Override
    public KinesisAnalyticsInputPreprocessingResponse handleRequest(KinesisFirehoseEvent kinesisFirehoseEvent, Context context) {
        List<KinesisAnalyticsInputPreprocessingResponse.Record> records = new ArrayList<>();

        for (KinesisFirehoseEvent.Record record : kinesisFirehoseEvent.getRecords()) {
            String recordData = new String(record.getData().array());
            // Your business logic
            String reversedString = new StringBuilder(recordData).reverse().toString();

            records.add(new KinesisAnalyticsInputPreprocessingResponse.Record(record.getRecordId(), Ok, ByteBuffer.wrap(reversedString.getBytes(UTF_8))));
        }

        return new KinesisAnalyticsInputPreprocessingResponse(records);
    }
}
