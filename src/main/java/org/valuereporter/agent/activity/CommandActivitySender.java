package org.valuereporter.agent.activity;

import org.slf4j.Logger;

import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * Created by baardl on 25.07.15.
 */
public class CommandActivitySender implements Runnable {
    private static final Logger log = getLogger(CommandActivitySender.class);
    private final String prefix;
    private final List<ObservedActivity> observedActivities;
    private final String reporterHost;
    private final String reporterPort;

    public CommandActivitySender(final String reporterHost, final String reporterPort, final String prefix, final List<ObservedActivity> observedActivities) {
        this.reporterHost = reporterHost;
        this.reporterPort = reporterPort;
        this.prefix = prefix;
        this.observedActivities = observedActivities;
    }

    @Override
    public void run() {

        String commandStatus = new CommandSendActivities(reporterHost,reporterPort,prefix, observedActivities).execute();
        log.trace("Ran CommandSendObservations. Status {}", commandStatus);

    }
}
