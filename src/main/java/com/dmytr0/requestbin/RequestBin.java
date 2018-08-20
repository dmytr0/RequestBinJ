package com.dmytr0.requestbin;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.ApplicationPidFileWriter;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

@SpringBootApplication
public class RequestBin {

    private Logger logger = LogManager.getLogger(getClass());

    public static void main(String[] args) {
        new SpringApplicationBuilder(RequestBin.class)
                .listeners(new ApplicationPidFileWriter("app.pid"))
                .run(args);
    }

    @PostConstruct
    public void init() throws InterruptedException {
        logger.info("STARTED");
    }

    @PreDestroy
    public void destroy() {
        logger.info("STOPPED");
    }
}