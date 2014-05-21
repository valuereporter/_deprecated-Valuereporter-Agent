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
    private static final int MAX_CACHE_SIZE = 100;

    List<ObservedMethod> observedMethods = new ArrayList<>();
    HttpSender httpSender;

    public HttpObservationDistributer() {
        super();
        httpSender = new HttpSender();
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
        /*
        String csv = "";
        if (observedMethods != null) {
            for (ObservedMethod observedMethod : observedMethods) {
                csv += observedMethod.toCsv() + "\n";
            }
        }
        log.info("Observed methods \n {}", csv);
        */

        //Forward to HTTP

        httpSender.forwardObservations("receipt-control-test", observedMethods);
        observedMethods.clear();
    }


}
