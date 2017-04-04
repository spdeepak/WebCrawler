package com.deepak.webcrawler;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import com.deepak.webcrawler.entity.WebData;
import com.deepak.webcrawler.entity.WebLink;
import com.deepak.webcrawler.extractor.WebDataExtractor;
import com.deepak.webcrawler.repository.WebDataRepository;
import com.deepak.webcrawler.repository.WebLinkRepository;

@Configuration
public class WebCrawler {

    @Autowired
    private WebLinkRepository webLinkRepository;

    @Autowired
    private WebDataRepository webDataRepository;

    @Autowired
    private WebDataExtractor webDataExtractor;

    private boolean findWebLinksToCrawl() {
        List<WebLink> webLinks = new ArrayList<>();
        webLinkRepository.findAll()
                         .forEach(webLinks::add);
        return webLinks.isEmpty();
    }

    public void start() {
        if (findWebLinksToCrawl()) {
            Scanner scanner = new Scanner(System.in);
            System.out.println("It seems like there are no URLs to crawl so please enter a URL with the http protocol (example:http://google.com)");
            String url = scanner.next();
            scanner.close();
            extract(url);
        } else {
            continueExtraction();
        }
    }

    private void continueExtraction() {
        Iterable<WebLink> webLinks = webLinkRepository.findAll();
        for (WebLink webLink : webLinks) {
            extract(webLink.getUrl());
        }
    }

    public void extract(String url) {
        if (webDataRepository.findByUrl(url) == null) {
            WebData webData = webDataExtractor.extract(url);
            webDataRepository.save(webData);
            WebLink webLink = webLinkRepository.findByUrl(url);
            if (webLink != null) {
                webLinkRepository.delete(webLink);
            }
            continueExtraction();
        }
    }

}
