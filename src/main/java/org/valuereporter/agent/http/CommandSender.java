package org.valuereporter.agent.http;

import org.slf4j.Logger;
import org.valuereporter.agent.ObservedMethod;

import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * Created by baardl on 25.07.15.
 */
public class CommandSender implements Runnable {
    private static final Logger log = getLogger(CommandSender.class);
    private final String prefix;
    private final List<ObservedMethod> observedMethods;
    private final String reporterHost;
    private final String reporterPort;

    public CommandSender(final String reporterHost, final String reporterPort, final String prefix, final List<ObservedMethod> observedMethods) {
        this.reporterHost = reporterHost;
        this.reporterPort = reporterPort;
        this.prefix = prefix;
        this.observedMethods = observedMethods;
    }

    @Override
    public void run() {

        String commandStatus = new CommandSendObservations(reporterHost,reporterPort,prefix,observedMethods).execute();
        log.trace("Ran CommandSendObservations. Status {}", commandStatus);

    }
}
