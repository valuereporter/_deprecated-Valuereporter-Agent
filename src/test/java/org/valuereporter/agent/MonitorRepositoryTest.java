package org.valuereporter.agent;

import org.testng.annotations.Test;

import static org.testng.Assert.*;


/**
 * @author <a href="bard.lind@gmail.com">Bard Lind</a>
 */
public class MonitorRepositoryTest {
    @Test
    public void testObserved() throws Exception {

    }

    @Test
    public void testObservedMoreThanCapacity() throws Exception {
        MonitorRepository repository = MonitorRepository.getInstance(1);
        assertTrue(repository.observed("ok",System.currentTimeMillis()-2000, System.currentTimeMillis()));
        assertFalse(repository.observed("timeout",System.currentTimeMillis()-2000, System.currentTimeMillis()));
        ObservedMethod observedMethod = repository.takeNext();
        assertNotNull(observedMethod);
        assertEquals(observedMethod.getName(), "ok");
    }

    @Test
    public void testHasObservations() throws Exception {

    }

    @Test
    public void testTakeNext() throws Exception {

    }


    public static void main(String[] args) {
        MonitorRepository repository = MonitorRepository.getInstance();
        repository.observed("first", System.currentTimeMillis()-2000, System.currentTimeMillis());
        //repository.takeNext();
        repository.observed("second", System.currentTimeMillis()-1000, System.currentTimeMillis());
        repository.observed("third", System.currentTimeMillis()-500, System.currentTimeMillis());
    }
}
