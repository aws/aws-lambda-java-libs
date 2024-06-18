/*
Copyright Amazon.com, Inc. or its affiliates. All Rights Reserved.
SPDX-License-Identifier: Apache-2.0
*/

package com.amazonaws.services.lambda.crac;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.amazonaws.services.lambda.runtime.api.client.runtimeapi.JniHelper;
 
import java.util.Map;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.lang.reflect.Field;
 
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
 
public class DNSCacheManagerTest {
 
    static String CACHE_FIELD_NAME = "cache";
 
    // this should have no effect, as the DNS cache is cleared explicitly in these tests
    static {
        java.security.Security.setProperty("networkaddress.cache.ttl" , "10000");
        java.security.Security.setProperty("networkaddress.cache.negative.ttl" , "10000");
    }
 
    @BeforeAll
    public static void jniLoad() {
        JniHelper.load();
    }

    @BeforeEach
    public void setup() {
        Core.resetGlobalContext();
        DNSManager.clearCache();
    }

    static class StatefulResource implements Resource {

        int state = 0;

        @Override
        public void afterRestore(Context<? extends Resource> context) {
            state += 1;
        }

        @Override
        public void beforeCheckpoint(Context<? extends Resource> context) {
            state += 2;
        }

        int getValue() {
            return state;
        }
    }

    @Test
    public void positiveDnsCacheShouldBeEmpty() throws CheckpointException, RestoreException, UnknownHostException, ReflectiveOperationException {
        int baselineDNSEntryCount = getDNSEntryCount();

        StatefulResource resource = new StatefulResource();
        Core.getGlobalContext().register(resource);

        String[] hosts = {"www.stackoverflow.com", "www.amazon.com", "www.yahoo.com"};
        for(String singleHost : hosts) {
            InetAddress address = InetAddress.getByName(singleHost);
        } 
        // n hosts -> n DNS entries
        assertEquals(hosts.length, getDNSEntryCount() - baselineDNSEntryCount);
 
        // this should call the native static method clearDNSCache
        Core.getGlobalContext().beforeCheckpoint(null);
 
        // cache should be cleared
        assertEquals(0, getDNSEntryCount());
    }
 
    @Test
    public void negativeDnsCacheShouldBeEmpty() throws CheckpointException, RestoreException, UnknownHostException, ReflectiveOperationException {
        int baselineDNSEntryCount = getDNSEntryCount();

        StatefulResource resource = new StatefulResource();
        Core.getGlobalContext().register(resource);

        String invalidHost = "not.a.valid.host";
        try {
            InetAddress address = InetAddress.getByName(invalidHost);
            fail();
        } catch(UnknownHostException uhe) {
            // this is actually fine
        }

        // 1 host -> 1 DNS entry
        assertEquals(1, getDNSEntryCount() - baselineDNSEntryCount);
 
        // this should the native static method clearDNSCache
        Core.getGlobalContext().beforeCheckpoint(null);
 
        // cache should be cleared
        assertEquals(0, getDNSEntryCount());
    }
 
    // helper functions to access the cache via reflection (see maven-surefire-plugin command args)
    protected static Map<?, ?> getDNSCache() throws ReflectiveOperationException {
        Class<InetAddress> klass = InetAddress.class;
        Field acf = klass.getDeclaredField(CACHE_FIELD_NAME);
        acf.setAccessible(true);
        Object addressCache = acf.get(null);
        return (Map<?, ?>) acf.get(addressCache);
    }
 
    protected static int getDNSEntryCount() throws ReflectiveOperationException {
        Map<?, ?> cache = getDNSCache();
        return cache.size();
    }
}
