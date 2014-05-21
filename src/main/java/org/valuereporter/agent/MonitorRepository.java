package org.valuereporter.agent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.LinkedBlockingQueue;

/**
 * @author <a href="bard.lind@gmail.com">Bard Lind</a>
 */
public class MonitorRepository {
    private static final Logger log = LoggerFactory.getLogger(MonitorRepository.class);
    private static MonitorRepository instance = null;
    private static LinkedBlockingQueue<ObservedMethod> observedQueue;
    private MonitorRepository() {
        observedQueue = new LinkedBlockingQueue<ObservedMethod>(1000);
    }
    public static MonitorRepository getInstance() {
        if(instance == null) {
            instance = new MonitorRepository();
        }
        return instance;
    }
    public void observed(String name, long startTimeMillis, long endTimeMillis) {
        if (name != null) {
            try {
                log.debug("Add to observedQueue {}", name);
                observedQueue.put(new ObservedMethod(name, startTimeMillis,endTimeMillis));
                log.debug("Done adding {}", name);
            } catch (InterruptedException e) {
                log.warn("Could not add observation {}", e.getMessage());
            }
        }
    }

    public boolean hasObservations() {
        return (observedQueue.size() > 0);
    }

    public ObservedMethod takeNext() {
        try {
            return observedQueue.take();
        } catch (InterruptedException e) {
            log.warn("Nothing to take {}", e.getMessage());
            return null;
        }
    }
}
