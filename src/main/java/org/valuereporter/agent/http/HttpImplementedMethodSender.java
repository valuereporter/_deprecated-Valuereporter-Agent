package org.valuereporter.agent.http;

import org.valuereporter.agent.ImplementedMethod;

import java.util.List;

/**
 * @author <a href="bard.lind@gmail.com">Bard Lind</a>
 */
public class HttpImplementedMethodSender {
    private final String reporterHost;
    private final String reporterPort;
    private final String prefix;

    public HttpImplementedMethodSender(String reporterHost, String reporterPort, String prefix) {
        this.reporterHost = reporterHost;
        this.reporterPort = reporterPort;
        this.prefix = prefix;
    }

    public int addImplementedMethods(List<ImplementedMethod> implementedMethods) {

        return 0;
    }

    public boolean send() {

        return false;
    }
}
