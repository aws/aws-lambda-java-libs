package com.amazonaws.services.lambda.runtime.events;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Class to represent the EventBridge event. This is also the CloudWatch Events event format.
 *
 * @see <a href="https://docs.aws.amazon.com/lambda/latest/dg/services-cloudwatchevents.html">CloudWatch Events</a>
 */

@Data
@Builder(setterPrefix = "with")
@NoArgsConstructor
@AllArgsConstructor
public class EventBridgeEvent {

    private String version;
    private String id;
    private String detailType;
    private String source;
    private String account;
    private String time;
    private String region;
    private List<String> resources;
    private Object detail;
}
