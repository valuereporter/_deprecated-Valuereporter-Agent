package org.valuereporter.agent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author <a href="bard.lind@gmail.com">Bard Lind</a>
 */
public abstract class ObservationDistributer implements Runnable {
    private static final Logger log = LoggerFactory.getLogger(ObservationDistributer.class);

    private static MonitorRepository monitorRepository;

    public ObservationDistributer() {
         monitorRepository = MonitorRepository.getInstance();
    }

    @Override
    public void run() {
        log.info("Starting ObservationDistributer");
        do {
            while (monitorRepository.hasObservations()) {
                ObservedMethod observedMethod = monitorRepository.takeNext();
                updateObservation(observedMethod);
            }
        } while (true);

    }

    protected abstract void updateObservation(ObservedMethod observedMethod);
}
