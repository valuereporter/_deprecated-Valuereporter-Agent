package org.valuereporter.agent.http;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.valuereporter.agent.ObservationDistributer;
import org.valuereporter.agent.ObservedMethod;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * There must be only one Observation Distributer in the JVM.
 * Created by baardl on 07.05.14.
 */
public class HttpObservationDistributer extends ObservationDistributer {
    private static final Logger log = LoggerFactory.getLogger(HttpObservationDistributer.class);
    private static final int MAX_CACHE_SIZE = 500;
    private static final int MAX_WAIT_PERIOD_MS = 60000;
    private static final  int THREAD_POOL_DEFAULT_SIZE = 10;
    private final String reporterHost;
    private final String reporterPort;

    List<ObservedMethod> observedMethods = new ArrayList<>();
   // HttpSender httpSender;
    private long nextForwardAtLatest = -1;

    private ThreadPoolExecutor executor = null;

    public HttpObservationDistributer(String reporterHost, String reporterPort, String prefix) {
        super();
        this.reporterHost = reporterHost;
        this.reporterPort = reporterPort;
        this.prefix = prefix;
        updateLatestTimeForwarding();
        int threadPoolSize = THREAD_POOL_DEFAULT_SIZE;
        executor = new ThreadPoolExecutor(threadPoolSize,threadPoolSize, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue());
    }

    protected void updateLatestTimeForwarding() {
        DateTime nextTime = new DateTime();
        nextTime.plusMillis(MAX_WAIT_PERIOD_MS);
        nextForwardAtLatest = nextTime.getMillis();
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
            //TODO add check on timeout, to ensure the content is sent every x seconds.
            if (observedMethods.size() >= MAX_CACHE_SIZE ||waitedLongEnough()) {
                forwardOutput();
                updateLatestTimeForwarding();
            }
        } else {
            log.warn("Observed Method is null");
        }

    }

    boolean waitedLongEnough() {
        return  System.currentTimeMillis() > nextForwardAtLatest;
    }

    /**
     * TODO use ThreadPool to fetch a Thread/Worker.
     * This worker will call a Hystrix Command to forward the payload.
     */
    private void forwardOutput() {
        //Forward to HTTP
        log.trace("Forwarding ObservedMethods. Local cache size {}", observedMethods.size());
//        new Thread(new HttpSender(reporterHost, reporterPort, prefix, observedMethods)).start();
        //httpSender.forwardObservations(prefix, observedMethods);
        HttpSender httpSender = new HttpSender(reporterHost, reporterPort, prefix, observedMethods);
        if (executor.getActiveCount() < executor.getMaximumPoolSize()) {
            executor.submit(httpSender);
            //Prepare for Hystrix
//            CommandSender commandSender = new CommandSender(reporterHost,reporterPort,prefix,observedMethods);
//            executor.submit(commandSender);
        }else {
            log.info("No threads available for HttpSender. Will discard content {}", httpSender);
        }
        observedMethods.clear();
    }


}
