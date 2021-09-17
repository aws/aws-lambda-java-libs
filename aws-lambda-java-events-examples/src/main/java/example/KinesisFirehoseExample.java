package example;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.KinesisAnalyticsInputPreprocessingResponse;
import com.amazonaws.services.lambda.runtime.events.KinesisFirehoseEvent;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

public class KinesisFirehoseExample implements RequestHandler<KinesisFirehoseEvent, KinesisAnalyticsInputPreprocessingResponse> {

    @Override
    public KinesisAnalyticsInputPreprocessingResponse handleRequest(KinesisFirehoseEvent kinesisFirehoseEvent, Context context) {
        List<KinesisAnalyticsInputPreprocessingResponse.Record> records = new ArrayList<>();

        for (KinesisFirehoseEvent.Record record : kinesisFirehoseEvent.getRecords()) {
            ByteBuffer data = record.getData();
            // Your business logic
            records.add(new KinesisAnalyticsInputPreprocessingResponse.Record(record.getRecordId(), KinesisAnalyticsInputPreprocessingResponse.Result.Ok, data));
        }

        return new KinesisAnalyticsInputPreprocessingResponse(records);
    }
}
