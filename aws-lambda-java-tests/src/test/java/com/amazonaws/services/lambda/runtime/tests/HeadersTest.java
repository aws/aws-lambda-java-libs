package com.amazonaws.services.lambda.runtime.tests;

import com.amazonaws.services.lambda.runtime.events.ApplicationLoadBalancerRequestEvent;
import com.amazonaws.services.lambda.runtime.events.apigateway.APIGatewayCustomAuthorizerEvent;
import com.amazonaws.services.lambda.runtime.events.apigateway.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.apigateway.APIGatewayV2CustomAuthorizerEvent;
import com.amazonaws.services.lambda.runtime.events.apigateway.APIGatewayV2HTTPEvent;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class HeadersTest {

    @Test
    public void testHeadersApiGatewayRestEvent() {
        APIGatewayProxyRequestEvent event = EventLoader.loadApiGatewayRestEvent("apigw_rest_event.json");

        assertThat(event.getHeaders().get("Header1")).isEqualTo("value1");
        assertThat(event.getHeaders().get("header1")).isEqualTo("value1");
        assertThat(event.getMultiValueHeaders().get("header1")).contains("value1", "value11");
        assertThat(event.getMultiValueHeaders().get("Header1")).contains("value1", "value11");
        assertThat(event.getHeaders()).hasSize(2);
    }

    @Test
    public void testHeadersApiGatewayHttpEvent() {
        APIGatewayV2HTTPEvent event = EventLoader.loadApiGatewayHttpEvent("apigw_http_event.json");

        assertThat(event.getHeaders().get("Header1")).isEqualTo("value1");
        assertThat(event.getHeaders().get("header1")).isEqualTo("value1");
    }

    @Test
    public void testHeadersAPIGatewayCustomAuthorizerEvent() {
        APIGatewayCustomAuthorizerEvent event = EventLoader.loadAPIGatewayCustomAuthorizerEvent("apigw_auth.json");

        assertThat(event.getHeaders().get("Accept")).isEqualTo("*/*");
        assertThat(event.getHeaders().get("accept")).isEqualTo("*/*");
    }

    @Test
    public void testHeadersAPIGatewayV2CustomAuthorizerEvent() {
        APIGatewayV2CustomAuthorizerEvent event = EventLoader.loadAPIGatewayV2CustomAuthorizerEvent("apigw_auth_v2.json");

        assertThat(event.getHeaders().get("Header1")).isEqualTo("Value1");
        assertThat(event.getHeaders().get("header1")).isEqualTo("Value1");
    }

    @Test
    public void testHeadersApplicationLoadBalancerRequestEvent() {
        ApplicationLoadBalancerRequestEvent event = EventLoader.loadApplicationLoadBalancerRequestEvent("elb_event.json");

        assertThat(event.getHeaders().get("Accept")).isEqualTo("text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
        assertThat(event.getHeaders().get("accept")).isEqualTo("text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
    }
}