package com.deepak.webcrawler;

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
        return webLinkRepository.findAll()
                                .isEmpty();
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
        WebLink wl = null;
        List<WebLink> webLinks = webLinkRepository.findAll();
        if (!webLinks.isEmpty()) {
            wl = webLinks.get(0);
            extract(wl.getUrl());
        }
    }

    public void extract(final String url) {
        if (webDataRepository.findByUrl(url)
                             .isEmpty()) {
            WebData webData = webDataExtractor.extract(url);
            webDataRepository.save(webData);
        }
        deleteWebLinkAfterWebDataExtraction(url);
        continueExtraction();
    }

    public void deleteWebLinkAfterWebDataExtraction(final String url) {
        List<WebLink> webLinks = webLinkRepository.findByUrl(url);
        if (!webLinks.isEmpty()) {
            webLinkRepository.delete(webLinks);
        }
    }

}
