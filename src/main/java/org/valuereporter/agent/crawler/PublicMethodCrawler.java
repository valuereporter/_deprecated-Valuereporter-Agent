package org.valuereporter.agent.crawler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.valuereporter.agent.http.HttpImplementedMethodSender;

import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author <a href="bard.lind@gmail.com">Bard Lind</a>
 */
public class PublicMethodCrawler implements Runnable {
    private static final Logger log = LoggerFactory.getLogger(PublicMethodCrawler.class);
    private final String reporterHost;
    private final String reporterPort;
    private final String prefix;
    private final String basePackage;
    private final HttpImplementedMethodSender sender;

    public PublicMethodCrawler(String reporterHost, String reporterPort, String prefix, String basePackage) {
        this.reporterHost = reporterHost;
        this.reporterPort = reporterPort;
        this.prefix = prefix;
        this.basePackage = basePackage;
        this.sender = new HttpImplementedMethodSender(reporterHost, reporterPort, prefix);
    }

    @Override
    public void run() {
        crawlForPublicMethods();
    }

    private void crawlForPublicMethods() {
        log.info("Starting to crawl for Public Methods.");
        try {
            ArrayList<String> names = PublicMethodFinder.getClassNamesFromPackage(basePackage);
            log.info("Found these names {}", names.size());
            List<Class> classes = PublicMethodFinder.findClasses(names);

            for (Class clazz : classes) {
                List<Method> publicMethods = null;
                log.trace("Found a potential class. {}", clazz.getName());
                if (clazz.isPrimitive() || clazz.isArray() || clazz.isAnnotation()
                        || clazz.isEnum() || clazz.isInterface()) {
                    log.trace("Skip class {}: not a class", clazz.getName());
                } else {
                    publicMethods = PublicMethodFinder.findPublicMethods(clazz);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        log.info("Done crawling.");
    }
}
