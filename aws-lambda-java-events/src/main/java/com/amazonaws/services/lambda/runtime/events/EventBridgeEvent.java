package com.amazonaws.services.lambda.runtime.events;

/**
 * Class to represent the EventBridge event. This is also the CloudWatch Events event format.
 *
 * This is a duplicate of ScheduledEvent because EventBridge currently share the same schema.
 *
 * @see <a href="https://docs.aws.amazon.com/lambda/latest/dg/services-cloudwatchevents.html">CloudWatch Events</a>
 */

public class EventBridgeEvent extends ScheduledEvent {

}
