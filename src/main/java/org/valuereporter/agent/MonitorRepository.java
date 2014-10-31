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
        observedQueue = new LinkedBlockingQueue<>(10000);
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
                log.trace("Add to observedQueue {}", name);
                observedQueue.put(new ObservedMethod(name, startTimeMillis,endTimeMillis));
                log.trace("Added {}, totalSize {}", name, observedQueue.size());
            } catch (InterruptedException e) {
                log.warn("Could not add observation Name {}, startTime {}, endTime {}",name, startTimeMillis, endTimeMillis, e);
            }
        }
    }

    public boolean hasObservations() {
        boolean hasObservations = observedQueue.size() > 0;
        //log.debug("hasObservations {}", hasObservations);
        return hasObservations;
    }

    public ObservedMethod takeNext() {
        try {
            ObservedMethod observedMethod = observedQueue.take();
            //log.debug("takeNext-observedMethod {}", observedMethod.toString());
            return observedMethod;
        } catch (InterruptedException e) {
            log.warn("Nothing to take {}", e.getMessage());
            return null;
        }
    }
}
