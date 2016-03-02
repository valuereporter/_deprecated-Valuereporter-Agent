package org.valuereporter.agent.activity;

import org.codehaus.jackson.map.ObjectMapper;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

/**
 * Created by baardl on 02.03.16.
 */
public class CommandSendActivitiesTest {

    private ObjectMapper objectMapper;
    private CommandSendActivities sendActivities;

    @BeforeMethod
    public void setUp() throws Exception {
        objectMapper = new ObjectMapper();



    }

    @Test
    public void testBuildJson() throws Exception {
        List<ObservedActivity> observedActivities = new ArrayList();
        Map<String, Object> logonData = new HashMap<>();
        logonData.put("userId", "me");
        observedActivities.add(new ObservedActivity("logon",1456904461955L, logonData));
        sendActivities = new CommandSendActivities(null,null,"test",observedActivities);
        String observedActivitiesJson = sendActivities.getObservedActivitiesJson();
        assertNotNull(observedActivitiesJson);
        assertEquals(observedActivitiesJson, expectedSingle);

    }

    private static final String expectedSingle = "[{\"name\":\"logon\",\"startTime\":1456904461955,\"data\":{\"userId\":\"me\"}}]";
}