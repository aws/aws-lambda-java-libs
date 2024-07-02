package example;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.MSKFirehoseResponse;
import com.amazonaws.services.lambda.runtime.events.MSKFirehoseEvent;
import org.json.JSONObject;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

/**
 * A sample MSKFirehoseEvent handler
 * For more information see the developer guide - <a href="https://docs.aws.amazon.com/firehose/latest/dev/data-transformation.html">...</a>
 */
public class MSKFirehoseEventHandler implements RequestHandler<MSKFirehoseEvent, MSKFirehoseResponse> {

    @Override
    public MSKFirehoseResponse handleRequest(MSKFirehoseEvent kinesisFirehoseEvent, Context context) {
        List<MSKFirehoseResponse.Record> records = new ArrayList<>();

        for (MSKFirehoseEvent.Record record : kinesisFirehoseEvent.getRecords()) {
            String recordData = new String(record.getKafkaRecordValue().array());
            // Your business logic
            JSONObject jsonObject = new JSONObject(recordData);
            records.add(new MSKFirehoseResponse.Record(record.getRecordId(), MSKFirehoseResponse.Result.Ok, encode(jsonObject.toString())));
        }
        return new MSKFirehoseResponse(records);
    }
    private ByteBuffer encode(String content) {
        return ByteBuffer.wrap(content.getBytes());
    }
}
