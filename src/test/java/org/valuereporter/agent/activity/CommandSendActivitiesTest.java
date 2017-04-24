package org.valuereporter.agent.activity;

import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

/**
 * Created by baardl on 02.03.16.
 */
public class CommandSendActivitiesTest {
    private static final Logger log = getLogger(CommandSendActivitiesTest.class);

    private ObjectMapper objectMapper;
    private CommandSendActivities sendActivities;

    @BeforeMethod
    public void setUp() throws Exception {
        objectMapper = new ObjectMapper();



    }

    @Test
    public void testBuildJson() throws Exception {
        List<ObservedActivity> observedActivities = new ArrayList();
        ObservedActivity observedActivity = new ObservedActivity("logon",1456904461955L);
        observedActivity.put("userId", "me");
        observedActivities.add(observedActivity);
        sendActivities = new CommandSendActivities(null,null,"test",observedActivities);
        String observedActivitiesJson = sendActivities.getObservedActivitiesJson();
        assertNotNull(observedActivitiesJson);
        assertEquals(observedActivitiesJson, expectedSingle);

    }

    public static void main(String[] args) {
        List<ObservedActivity> observedActivities = new ArrayList();
        ObservedActivity userSession = new ObservedActivity("userSession", System.currentTimeMillis());
        userSession.put("usersessionfunction","userSessionAccess");
        userSession.put("applicationid","100");
        userSession.put("userid", "test-only");
        userSession.put("applicationtokenid", "to-be-set");
        observedActivities.add(userSession);
        ObservedActivity userLogon = new ObservedActivity("userLogon", System.currentTimeMillis());
        userLogon.put("usersessionfunction","userLogon");
        userLogon.put("applicationid","100");
        userLogon.put("userid", "test-only");
        userLogon.put("applicationtokenid", "to-be-set");
        observedActivities.add(userLogon);
        String host = "localhost";
        String port = "4901";
        String prefix = "initial";
        //Send
        CommandSendActivities sendActivities = new CommandSendActivities(host,port,prefix,observedActivities);
        //Validate
        String observedActivitiesJson = sendActivities.getObservedActivitiesJson();
        log.info("Received {}", observedActivitiesJson);


    }

    private static final String expectedSingle = "[{\"name\":\"logon\",\"startTime\":1456904461955,\"data\":{\"userId\":\"me\"}}]";
}