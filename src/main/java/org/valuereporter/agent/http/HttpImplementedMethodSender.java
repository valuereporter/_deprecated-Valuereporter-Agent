package org.valuereporter.agent.http;

import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.valuereporter.agent.ImplementedMethod;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.List;

/**
 * @author <a href="bard.lind@gmail.com">Bard Lind</a>
 */
public class HttpImplementedMethodSender implements Runnable {
    private static final Logger log = LoggerFactory.getLogger(HttpImplementedMethodSender.class);
    private final String reporterHost;
    private final String reporterPort;
    private final String prefix;
    private ObjectMapper mapper = new ObjectMapper();
    private static final int STATUS_BAD_REQUEST = 400; //Response.Status.BAD_REQUEST.getStatusCode();
    private static final int STATUS_OK = 200; //Response.Status.OK.getStatusCode();
    private static final int STATUS_FORBIDDEN = 403;
    private String implementedMethodsAsJson;

    public HttpImplementedMethodSender(String reporterHost, String reporterPort, String prefix,List<ImplementedMethod> implementedMethods) {
        this.reporterHost = reporterHost;
        this.reporterPort = reporterPort;
        this.prefix = prefix;
        this.implementedMethodsAsJson = buildJson(implementedMethods);
    }

    private String buildJson(List<ImplementedMethod> implementedMethods)  {
        String json = null;
        try {
            json = mapper.writeValueAsString(implementedMethods);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return json;
    }



    @Override
    public void run() {
        Client client = ClientBuilder.newClient();
        String observationUrl = "http://"+reporterHost + ":" + reporterPort +"/reporter/observe";
        log.info("Connection to ValueReporter on {}" , observationUrl);
        final WebTarget observationTarget = client.target(observationUrl);
        //WebTarget webResource = findWebResourceByPrefix(prefix);
        WebTarget webResource = observationTarget.path("implementedmethods").path(prefix);
        //String observedMethodsJson = mapper.writeValueAsString(observedMethods);
        log.trace("Forwarding implementedMethods as Json \n{}", implementedMethodsAsJson);


        Response response = webResource.request(MediaType.APPLICATION_JSON).post(Entity.entity(implementedMethodsAsJson, MediaType.APPLICATION_JSON));
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
}
