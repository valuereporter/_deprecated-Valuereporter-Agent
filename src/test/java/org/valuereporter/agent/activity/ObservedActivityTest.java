package org.valuereporter.agent.activity;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

import static org.testng.Assert.assertEquals;

/**
 * Created by baardl on 04.03.16.
 */
public class ObservedActivityTest {

    @BeforeMethod
    public void setUp() throws Exception {

    }

    @Test
    public void testGetName() throws Exception {
        String name = "testname";
        long timestamp = System.currentTimeMillis();

        ObservedActivity observedActivity = new ObservedActivity(name, timestamp);
        observedActivity.put("userid", new String("me"));
        assertEquals(name, observedActivity.getName());
        assertEquals(timestamp,observedActivity.getStartTime());
        assertEquals("me", observedActivity.getData().get("userid"));

    }
}