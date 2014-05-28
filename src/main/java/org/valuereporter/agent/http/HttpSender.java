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
public class HttpSender  {
    private static final Logger log = LoggerFactory.getLogger(HttpSender.class);

    private final WebTarget observationTarget;
    ObjectMapper mapper = new ObjectMapper();
    private static final int STATUS_BAD_REQUEST = 400; //Response.Status.BAD_REQUEST.getStatusCode();
    private static final int STATUS_OK = 200; //Response.Status.OK.getStatusCode();
    private static final int STATUS_FORBIDDEN = 403;
    Map<String, WebTarget> observedMethodTargets;

    public HttpSender(String reporterHost, String reporterPort) {
        Client client = ClientBuilder.newClient();
        String observationUrl = "http://"+reporterHost + ":" + reporterPort +"/reporter/observe";
        log.info("Connection to ValueReporter on {}" , observationUrl);
        observationTarget = client.target(observationUrl);
        observedMethodTargets = new HashMap<>();
    }

    public void forwardObservations(String prefix, List<ObservedMethod> observedMethods) {
        WebTarget webResource = findWebResourceByPrefix(prefix);
        try {
            String observedMethodsJson = mapper.writeValueAsString(observedMethods);
            log.trace("Forwarding observedMethods as Json \n{}", observedMethodsJson);


            Response response = webResource.request(MediaType.APPLICATION_JSON).post(Entity.entity(observedMethodsJson, MediaType.APPLICATION_JSON));
            int statusCode = response.getStatus();
            switch (statusCode) {
                case STATUS_OK:
                    log.debug("Updated via http ok. Response is {}", response.readEntity(String.class));
                    break;
                case STATUS_FORBIDDEN:
                    log.error("Not allowed to access ValueReporter. Status {}, Response {}", response.getStatus(), response.readEntity(String.class));
                    break;
                default:
                    log.error("Error while accessing ValueReporter. Status {},Response {}", response.getStatus(), response.readEntity(String.class));
            }

        } catch (IOException e) {
            log.error("Error forwarding the observations. The application will not try to fix this.", e);
        }
    }

    private WebTarget findWebResourceByPrefix(String prefix) {
        WebTarget webTarget = observedMethodTargets.get(prefix);
        if (webTarget == null ) {
            webTarget = observationTarget.path("observedmethods").path(prefix);
            observedMethodTargets.put(prefix, webTarget);
        }
        return webTarget;
    }

}
