package org.valuereporter.agent.http;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.testng.Assert.assertTrue;

/**
 * Created by baardl on 26.07.15.
 */
public class HttpObservationDistributerTest {

    private HttpObservationDistributer observationDistributer;

    @BeforeMethod
    public void setUp() throws Exception {
        observationDistributer = new HttpObservationDistributer("","","");

    }

    @Test
    public void testUpdateLatestTimeForwarding() throws Exception {
        long currentTime = System.currentTimeMillis();
        observationDistributer.updateLatestTimeForwarding();
        long nextForwardAtLatest = observationDistributer.getNextForwardAtLatest();
        assertTrue(nextForwardAtLatest >= (currentTime + observationDistributer.getMaxWaitPeriodMs()));

    }

    @Test
    public void testWaitedLongEnough() throws Exception {

    }
}