package org.valuereporter.agent.http;

import com.github.kevinsawicki.http.HttpRequest;
import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.valuereporter.agent.ObservedMethod;

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
//        Client client = ClientBuilder.newClient();
//        String observationUrl = "http://"+reporterHost + ":" + reporterPort +"/reporter/observe";
//        log.info("Connection to ValueReporter on {}" , observationUrl);
//        final WebTarget observationTarget = client.target(observationUrl);
//        WebTarget webResource = observationTarget.path("observedmethods").path(prefix);
//        log.trace("Forwarding observedMethods as Json \n{}", observedMethodsJson);
        //        WebTarget webResource = observationTarget.path("observedmethods").path(prefix);
//        log.trace("Forwarding observedMethods as Json \n{}", observedMethodsJson);

//        Response response = webResource.request(MediaType.APPLICATION_JSON).post(Entity.entity(observedMethodsJson, MediaType.APPLICATION_JSON));


//        Response response = webResource.request(MediaType.APPLICATION_JSON).post(Entity.entity(observedMethodsJson, MediaType.APPLICATION_JSON));
//        int statusCode = response.getStatus();
//        String reporterHost = "localhost";
//        String reporterPort = "4901";
//        String prefix = "All";
//        String observedMethodsJson = "[]";
        String observationUrl = "http://"+reporterHost + ":" + reporterPort +"/reporter/observe" + "/observedmethods/" + prefix;
        log.info("Connection to ValueReporter on {}" , observationUrl);
        HttpRequest request = HttpRequest.post(observationUrl ).acceptJson().contentType(HttpSender.APPLICATION_JSON).send(observedMethodsJson);
        int statusCode = request.code();
        String responseBody = request.body();

        log.trace("Status {}, body {}", statusCode, responseBody);
        switch (statusCode) {
            case HttpSender.STATUS_OK:
                log.trace("Updated via http ok. Response is {}", responseBody);
                break;
            case HttpSender.STATUS_FORBIDDEN:
                log.warn("Can not access ValueReporter. The application will function as normally, though Observation statistics will not be stored. URL {}, HttpStatus {}, Response {}, ", observationUrl,statusCode, responseBody);
                break;
            default:
                log.trace("Retrying access to ValueReporter");
                request = HttpRequest.post(observationUrl).acceptJson().contentType(HttpSender.APPLICATION_JSON).send(observedMethodsJson);
                if (request.code() == HttpSender.STATUS_OK) {
                    log.trace("Retry via http ok. Response is {}", request.body());
                } else {
                    log.error("Error while accessing ValueReporter. The application will function as normally, though Observation statistics will not be stored. URL {}, HttpStatus {},Response from ValueReporter {}", observationUrl, statusCode, responseBody);
                }
        }
        return "OK";


    }

    @Override
    protected String getFallback() {
        return "FALLBACK";
    }



}