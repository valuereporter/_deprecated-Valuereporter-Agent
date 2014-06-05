package org.valuereporter.agent.http;


import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.valuereporter.agent.ObservedMethod;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by baardl on 07.05.14.
 */
public class HttpSender implements Runnable {
    private static final Logger log = LoggerFactory.getLogger(HttpSender.class);


    private final String prefix;
    private final List<ObservedMethod> observedMethods;
    private final String reporterHost;
    private final String reporterPort;
    private ObjectMapper mapper = new ObjectMapper();
    private static final int STATUS_BAD_REQUEST = 400; //Response.Status.BAD_REQUEST.getStatusCode();
    private static final int STATUS_OK = 200; //Response.Status.OK.getStatusCode();
    private static final int STATUS_FORBIDDEN = 403;
    private Map<String, WebTarget> observedMethodTargets;
    private final String observedMethodsJson;

    public HttpSender(final String reporterHost, final String reporterPort, final String prefix, final List<ObservedMethod> observedMethods) {
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

    //public void forwardObservations(String prefix, List<ObservedMethod> observedMethods) {
    public void run() {
        Client client = ClientBuilder.newClient();
        String observationUrl = "http://"+reporterHost + ":" + reporterPort +"/reporter/observe";
        log.info("Connection to ValueReporter on {}" , observationUrl);
        final WebTarget observationTarget = client.target(observationUrl);
        observedMethodTargets = new HashMap<>();
        //WebTarget webResource = findWebResourceByPrefix(prefix);
        WebTarget webResource = observationTarget.path("observedmethods").path(prefix);
            //String observedMethodsJson = mapper.writeValueAsString(observedMethods);
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
                    log.error("Error while accessing ValueReporter. The application will function as normally, though Observation statistics will not be stored. URL {}, HttpStatus {},Response from ValueReporter {}",observationUrl, response.getStatus(), response.readEntity(String.class));
            }

    }

    /*
    private WebTarget findWebResourceByPrefix(String prefix) {
        WebTarget webTarget = observedMethodTargets.get(prefix);
        if (webTarget == null ) {
            webTarget = observationTarget.path("observedmethods").path(prefix);
            observedMethodTargets.put(prefix, webTarget);
        }
        return webTarget;
    }
    */

}
