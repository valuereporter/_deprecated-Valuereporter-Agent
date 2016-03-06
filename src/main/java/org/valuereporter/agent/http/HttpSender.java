package org.valuereporter.agent.http;


import com.github.kevinsawicki.http.HttpRequest;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.valuereporter.agent.ObservedMethod;

import java.io.IOException;
import java.util.List;

/**
 * Created by baardl on 07.05.14.
 */
public class HttpSender implements Runnable {
    private static final Logger log = LoggerFactory.getLogger(HttpSender.class);
    public static final String APPLICATION_JSON = "application/json";
    public static final int STATUS_OK = 200; //Response.Status.OK.getStatusCode();
    public static final int STATUS_FORBIDDEN = 403;


    private final String prefix;
    private final String reporterHost;
    private final String reporterPort;
    private ObjectMapper mapper = new ObjectMapper();
 
    private final String observedMethodsJson;

    public HttpSender(final String reporterHost, final String reporterPort, final String prefix, final List<ObservedMethod> observedMethods) {
        observedMethodsJson = buildJson(observedMethods);
        this.reporterHost = reporterHost;
        this.reporterPort = reporterPort;
        this.prefix = prefix;
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
//        Client client = ClientBuilder.newClient();
//        String observationUrl = "http://"+reporterHost + ":" + reporterPort +"/reporter/observe";
//        log.info("Connection to ValueReporter on {}" , observationUrl);
//        final WebTarget observationTarget = client.target(observationUrl);
//        WebTarget webResource = observationTarget.path("observedmethods").path(prefix);
//        log.trace("Forwarding observedMethods as Json \n{}", observedMethodsJson);
//        Response response = webResource.request(MediaType.APPLICATION_JSON).post(Entity.entity(observedMethodsJson, MediaType.APPLICATION_JSON));
//        int statusCode = statusCode;

        String observationUrl = "http://"+reporterHost + ":" + reporterPort +"/reporter/observe" + "/observedmethods/" + prefix;
        log.info("Connection to ValueReporter on {}" , observationUrl);
        log.trace("Forwarding observedMethods as Json \n{}", observedMethodsJson);
        HttpRequest request = HttpRequest.post(observationUrl ).acceptJson().contentType(HttpSender.APPLICATION_JSON).send(observedMethodsJson);
        int statusCode = request.code();
        String responseBody = request.body();
        switch (statusCode) {
            case STATUS_OK:
                log.trace("Updated via http ok. Response is {}", responseBody);
                break;
            case STATUS_FORBIDDEN:
                log.warn("Can not access ValueReporter. The application will function as normally, though Observation statistics will not be stored. URL {}, HttpStatus {}, Response {}, ", observationUrl,statusCode, responseBody);
                break;
            default:
                log.error("Error while accessing ValueReporter. The application will function as normally, though Observation statistics will not be stored. URL {}, HttpStatus {},Response from ValueReporter {}",observationUrl, statusCode, responseBody);
        }

    }

}
