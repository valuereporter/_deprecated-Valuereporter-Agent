package org.valuereporter.agent;

import org.testng.annotations.Test;

/**
 * @author <a href="bard.lind@gmail.com">Bard Lind</a>
 */
public class MonitorRepositoryTest {
    @Test
    public void testObserved() throws Exception {

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
