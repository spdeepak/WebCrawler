package com.deepak.webcrawler;

import java.util.List;
import java.util.Scanner;

import org.apache.commons.validator.routines.UrlValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Async;

import com.deepak.webcrawler.entity.WebData;
import com.deepak.webcrawler.entity.WebLink;
import com.deepak.webcrawler.extractor.WebDataExtractor;
import com.deepak.webcrawler.repository.WebDataRepository;
import com.deepak.webcrawler.repository.WebLinkRepository;

/**
 * @author Deepak
 *
 */
@Configuration
public class WebCrawler {

    @Autowired
    private WebLinkRepository webLinkRepository;

    @Autowired
    private WebDataRepository webDataRepository;

    @Autowired
    private WebDataExtractor webDataExtractor;

    UrlValidator validator = new UrlValidator();

    private boolean findWebLinksToCrawl() {
        return webLinkRepository.findAll()
                                .isEmpty();
    }

    @Async
    public void start() {
        if (findWebLinksToCrawl()) {
            Scanner scanner = new Scanner(System.in);
            System.out.println("It seems like there are no URLs to crawl so please enter a URL with the http protocol (example:http://google.com)");
            String url = scanner.next();
            while (!validator.isValid(url)) {
                System.out.println("Entered URL is not vlaid. Please enter again.");
                url = scanner.next();
            }
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
            if (validateWebData(webData)) {
                webDataRepository.save(webData);
            }
        }
        deleteWebLinkAfterWebDataExtraction(url);
        continueExtraction();
    }

    private boolean validateWebData(WebData webData) {
        return webData != null && webData.getUrl() != null && !webData.getUrl()
                                                                      .trim()
                                                                      .isEmpty()
                && ((webData.getMetaDataDescription() != null && !webData.getMetaDataDescription()
                                                                         .trim()
                                                                         .isEmpty())
                        || (webData.getTitle() != null && !webData.getTitle()
                                                                  .trim()
                                                                  .isEmpty()));
    }

    public void deleteWebLinkAfterWebDataExtraction(final String url) {
        List<WebLink> webLinks = webLinkRepository.findByUrl(url);
        if (!webLinks.isEmpty()) {
            webLinkRepository.delete(webLinks);
        }
    }

}
