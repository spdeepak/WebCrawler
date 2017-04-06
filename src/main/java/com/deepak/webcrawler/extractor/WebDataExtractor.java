package com.deepak.webcrawler.extractor;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.validator.routines.UrlValidator;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import com.deepak.webcrawler.entity.WebData;
import com.deepak.webcrawler.entity.WebLink;
import com.deepak.webcrawler.repository.WebLinkRepository;

@Configuration
public class WebDataExtractor {

    private static final Logger LOG = LoggerFactory.getLogger(WebDataExtractor.class);

    @Autowired
    private WebLinkRepository webLinkRepository;

    public WebData extract(final String url) {
        WebData webData = new WebData();
        Document doc;
        try {
            doc = Jsoup.connect(url)
                       .get();

            webData.setUrl(url);
            LOG.info("extracted document Data");
            String title = doc.title();
            if (!title.isEmpty() && !title.trim()
                                          .isEmpty()) {
                LOG.info("Setting Title to WebData");
                webData.setTitle(title);
            }
            fetchMetaData(webData, doc);
            fetchLinks(url, webData, doc);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return webData;
    }

    private WebData fetchMetaData(WebData webData, final Document doc) {
        Elements meta = doc.select("meta[name]");
        meta.forEach((i) ->
            {
                if (i.attr("name")
                     .equalsIgnoreCase("description")) {
                    LOG.info("Extracting Meta Data Description");
                    webData.setMetaDataDescription(i.attr("content"));
                } else if (i.attr("name")
                            .equalsIgnoreCase("keywords")) {
                    String[] keys = i.attr("content")
                                     .split(",");
                    LOG.info("Extracting Meta Data Keywords");
                    webData.setMetDataKeyWords(Arrays.stream(keys)
                                                     .map(j -> j = j.trim())
                                                     .collect(Collectors.toSet()));
                }
            });
        return webData;
    }

    private void fetchLinks(final String url, WebData webData, final Document doc) {
        Elements links = doc.select("a[href]");
        Set<WebLink> webLinks = new HashSet<>();
        UrlValidator validate = new UrlValidator();
        links.forEach(link -> createWebLink(url, webLinks, validate, link));
        webData.setWebLinks(webLinks);
    }

    public void createWebLink(final String url, Set<WebLink> webLinks, UrlValidator validate, final Element element) {
        WebLink webLink = new WebLink();
        if (validateElementAnchorHref(element)) {
            if (element.attr("href")
                       .contains(url)) {
                validateAndSetWebLinkUrl(validate, element, webLink);
            } else if (element.attr("href")
                              .contains("http:")) {
                validateAndSetWebLinkUrl(validate, element, webLink);
            } else {
                LOG.info("Extracted URL: " + url.concat(element.attr("href")));
                webLink.setUrl(url.concat(element.attr("href")));
            }
        }
        LOG.info("Adding WebLink to Set");
        webLinks.add(webLink);
    }

    public boolean validateElementAnchorHref(final Element i) {
        return i.attr("href") != null && !i.attr("href")
                                           .trim()
                                           .isEmpty()
                && !i.attr("href")
                     .trim()
                     .equals("/");
    }

    private void validateAndSetWebLinkUrl(UrlValidator validate, Element element, WebLink webLink) {
        if (validate.isValid(element.attr("href"))) {
            LOG.info("Extracted URL: " + element.attr("href"));
            webLink.setUrl(element.attr("href"));
        }
    }
}
