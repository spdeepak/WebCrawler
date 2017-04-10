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

/**
 * @author Deepak
 *
 */
@Configuration
public class WebDataExtractor {

    private static final Logger LOG = LoggerFactory.getLogger(WebDataExtractor.class);

    @Autowired
    private WebLinkRepository webLinkRepository;

    private UrlValidator validate = new UrlValidator();

    public WebData extract(final String url) {
        WebData webData = null;
        Document doc;
        try {
            webData = new WebData();
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
        meta.parallelStream()
            .forEach((i) ->
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

    public WebData fetchLinks(final String url, WebData webData, final Document doc) {
        Elements links = doc.select("a[href]");
        Set<WebLink> webLinks = new HashSet<>();
        UrlValidator validate = new UrlValidator();
        links.parallelStream()
             .forEach(link -> createWebLink(url, webLinks, link));
        webData.setWebLinks(webLinks);
        return webData;
    }

    private void createWebLink(final String url, Set<WebLink> webLinks, final Element element) {
        WebLink webLink = new WebLink();
        if (validateElementAnchorHref(element.attr("href"))) {
            if (element.attr("href")
                       .contains(url)) {
                validateAndSetWebLinkUrl(element, webLink);
                addWebLinkToWebDatasWebLinks(webLinks, webLink);
            } else if (element.attr("href")
                              .contains("http:")) {
                validateAndSetWebLinkUrl(element, webLink);
                addWebLinkToWebDatasWebLinks(webLinks, webLink);
            } else {
                LOG.info("Extracted URL: " + url.concat(element.attr("href")));
                webLink.setUrl(url.concat(element.attr("href")));
                addWebLinkToWebDatasWebLinks(webLinks, webLink);
            }
        }
    }

    public void addWebLinkToWebDatasWebLinks(Set<WebLink> webLinks, final WebLink webLink) {
        LOG.info("Adding WebLink: " + webLink.getUrl());
        if (webLink.getUrl()
                   .trim()
                   .endsWith("/")) {
            int len = webLink.getUrl()
                             .length()
                    - 1;
            if (!webLinkRepository.findByUrl(webLink.getUrl()
                                                    .substring(0, len))
                                  .isEmpty()) {
                webLinks.add(webLink);
            }
        }
        webLinks.add(webLink);
    }

    public boolean validateElementAnchorHref(String href) {
        return href != null && !href.trim()
                                    .isEmpty()
                && !href.trim()
                        .equals("/")
                && !href.toLowerCase()
                        .startsWith("javascript:");
    }

    private void validateAndSetWebLinkUrl(Element element, WebLink webLink) {
        if (validate.isValid(element.attr("href"))) {
            LOG.info("Extracted URL: " + element.attr("href"));
            webLink.setUrl(element.attr("href"));
        }
    }
}
