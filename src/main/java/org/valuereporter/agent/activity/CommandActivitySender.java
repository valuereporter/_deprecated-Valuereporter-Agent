package org.valuereporter.agent.activity;

import org.slf4j.Logger;
import org.valuereporter.agent.ObservedMethod;
import org.valuereporter.agent.http.CommandSendObservations;

import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * Created by baardl on 25.07.15.
 */
public class CommandActivitySender implements Runnable {
    private static final Logger log = getLogger(CommandActivitySender.class);
    private final String prefix;
    private final List<ObservedMethod> observedMethods;
    private final String reporterHost;
    private final String reporterPort;

    public CommandActivitySender(final String reporterHost, final String reporterPort, final String prefix, final List<ObservedMethod> observedMethods) {
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
