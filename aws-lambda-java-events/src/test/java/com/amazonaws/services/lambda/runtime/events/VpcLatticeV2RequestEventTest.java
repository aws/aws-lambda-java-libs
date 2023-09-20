package com.amazonaws.services.lambda.runtime.events;

import org.junit.jupiter.api.Test;

import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.assertEquals;

class VpcLatticeV2RequestEventTest {

    @Test
    public void epochAsTime() {
        VpcLatticeV2RequestEvent.RequestContext requestContext = new VpcLatticeV2RequestEvent.RequestContext().builder()
                .withTimeEpoch("1690497599177430")
                .build();

        assertEquals("2023-07-27T22:39:59.177", requestContext.getlocalDateTime().format(DateTimeFormatter.ISO_DATE_TIME));
    }
}