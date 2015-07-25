package org.valuereporter.agent.http;

import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.valuereporter.agent.ObservedMethod;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * Created by totto on 25.06.15.
 */
public class CommandSendObservations extends HystrixCommand<String>  {

    private static final Logger log = getLogger(CommandSendObservations.class);

    private final String prefix;
    private final List<ObservedMethod> observedMethods;
    private final String reporterHost;
    private final String reporterPort;
    private ObjectMapper mapper = new ObjectMapper();
    private static final int STATUS_OK = 200;
    private static final int STATUS_FORBIDDEN = 403;
    private final String observedMethodsJson;

    public CommandSendObservations(final String reporterHost, final String reporterPort, final String prefix, final List<ObservedMethod> observedMethods) {
        super(HystrixCommandGroupKey.Factory.asKey("ValueReporterAgent-group"));
        observedMethodsJson = buildJson(observedMethods);
        this.reporterHost = reporterHost;
        this.reporterPort = reporterPort;
        this.prefix = prefix;
        this.observedMethods = observedMethods;
    }

    private String buildJson(List<ObservedMethod> observedMethods)  {
        String json = null;
        try {
            json = mapper.writeValueAsString(observedMethods);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return json;
    }

    @Override
    protected String run() {
        Client client = ClientBuilder.newClient();
        String observationUrl = "http://"+reporterHost + ":" + reporterPort +"/reporter/observe";
        log.info("Connection to ValueReporter on {}" , observationUrl);
        final WebTarget observationTarget = client.target(observationUrl);
        WebTarget webResource = observationTarget.path("observedmethods").path(prefix);
        log.trace("Forwarding observedMethods as Json \n{}", observedMethodsJson);


        Response response = webResource.request(MediaType.APPLICATION_JSON).post(Entity.entity(observedMethodsJson, MediaType.APPLICATION_JSON));
        int statusCode = response.getStatus();
        switch (statusCode) {
            case STATUS_OK:
                log.trace("Updated via http ok. Response is {}", response.readEntity(String.class));
                break;
            case STATUS_FORBIDDEN:
                log.warn("Can not access ValueReporter. The application will function as normally, though Observation statistics will not be stored. URL {}, HttpStatus {}, Response {}, ", observationUrl,response.getStatus(), response.readEntity(String.class));
                break;
            default:
                log.trace("Retrying access to ValueReporter");
                response = webResource.request(MediaType.APPLICATION_JSON).post(Entity.entity(observedMethodsJson, MediaType.APPLICATION_JSON));
                if (response.getStatus() == STATUS_OK) {
                    log.trace("Retry via http ok. Response is {}", response.readEntity(String.class));
                } else {
                    log.error("Error while accessing ValueReporter. The application will function as normally, though Observation statistics will not be stored. URL {}, HttpStatus {},Response from ValueReporter {}", observationUrl, response.getStatus(), response.readEntity(String.class));
                }
        }
        return "OK";


    }

    @Override
    protected String getFallback() {
        return "FALLBACK";
    }



}