package com.deepak.webcrawler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Configuration;

/**
 * @author Deepak
 *
 */
@SpringBootApplication
@Configuration
public class Application {

    @Autowired
    private WebCrawler webCrawler;

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(Application.class, args);
        context.getBean(Application.class)
               .startCrawler();
    }

    public void startCrawler() {
        webCrawler.start();
    }
}
