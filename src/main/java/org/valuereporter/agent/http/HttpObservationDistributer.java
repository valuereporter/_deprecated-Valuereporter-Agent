package org.valuereporter.agent.http;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.valuereporter.agent.ObservationDistributer;
import org.valuereporter.agent.ObservedMethod;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by baardl on 07.05.14.
 */
public class HttpObservationDistributer extends ObservationDistributer {
    private static final Logger log = LoggerFactory.getLogger(HttpObservationDistributer.class);
    private static final int MAX_CACHE_SIZE = 10000;

    List<ObservedMethod> observedMethods = new ArrayList<>();
    HttpSender httpSender;

    public HttpObservationDistributer(String reporterHost, String reporterPort, String prefix) {
        super();
        httpSender = new HttpSender(reporterHost, reporterPort);
        this.prefix = prefix;
    }

    @Override
    public void run() {
        super.run();
    }

    @Override
    protected void updateObservation(ObservedMethod observedMethod) {
        if (observedMethod != null) {
            //log.info("Observed {}", observedMethod.toString());
            observedMethods.add(observedMethod);
            if (observedMethods.size() >= MAX_CACHE_SIZE) {
                forwardOutput();
            }
        } else {
            log.warn("Observed Method is null");
        }

    }

    private void forwardOutput() {
        //Forward to HTTP
        httpSender.forwardObservations(prefix, observedMethods);
        observedMethods.clear();
    }


}
