package com.deepak.webcrawler.extractor;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
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

    public WebData extract(String url) {
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

    private WebData fetchMetaData(WebData webData, Document doc) {
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

    private void fetchLinks(String url, WebData webData, Document doc) {
        Elements links = doc.select("a[href]");
        Set<WebLink> webLinks = new HashSet<>();
        links.forEach((i) ->
            {
                WebLink webLink = new WebLink();
                if (i.attr("href")
                     .contains(url)) {
                    LOG.info("Extracting URLs");
                    webLink.setUrl(i.attr("href"));
                } else if (i.attr("href")
                            .contains("http:")) {
                    LOG.info("Extracting URLs");
                    webLink.setUrl(i.attr("href"));
                } else {
                    LOG.info("Extracting URLs");
                    webLink.setUrl(url.concat("/")
                                      .concat(i.attr("href")));
                }
                LOG.info("Adding WebLink to Set");
                webLinks.add(webLink);
            });
        webData.setWebLinks(webLinks);
    }
}
