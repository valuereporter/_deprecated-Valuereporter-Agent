package org.valuereporter.agent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.valuereporter.agent.http.HttpObservationDistributer;

import java.lang.instrument.Instrumentation;
import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.util.HashMap;
import java.util.Map;


/**
 * Usage
 * java -javaagent:value-monitor-agent-jar-with-dependencies.jar=statistics.csv.sec:2,statistics.csv.dir:./access-logs -jar <single-jar-runtime>
 */
public class MonitorAgent {
    private static final Logger log = LoggerFactory.getLogger(MonitorAgent.class);
    private static final String DIR_NAME = "./access-logs";
    private static final int INTERVAL_SEC = 1;
    public static final String BASE_PACKAGE_KEY = "base.package";
    public static final String VALUE_REPORTER_HOST_KEY = "valuereporter.host";
    public static final String PREFIX_KEY = "prefix";


    public static void premain(String agentArguments, Instrumentation instrumentation) {
        RuntimeMXBean runtimeMxBean = ManagementFactory.getRuntimeMXBean();
        log.info("Runtime: {}: {}", runtimeMxBean.getName(), runtimeMxBean.getInputArguments());
        log.info("Starting agent with arguments {}" , agentArguments);
        String basePackage = "";

        if (agentArguments != null) {
            // parse the arguments:
            // graphite.host=localhost,graphite.port=2003
            Map<String, String> properties = new HashMap<String, String>();
            for(String propertyAndValue: agentArguments.split(",")) {
                String[] tokens = propertyAndValue.split(":", 2);
                if (tokens.length != 2) {
                    continue;
                }
                properties.put(tokens[0], tokens[1]);

            }
            basePackage = properties.get(BASE_PACKAGE_KEY);

        }

        // define the class transformer to use
        instrumentation.addTransformer(new TimedClassTransformer(basePackage));

        log.info("Starting HttpObservationDistributer");
        new Thread(new HttpObservationDistributer()).start();
    }

}
