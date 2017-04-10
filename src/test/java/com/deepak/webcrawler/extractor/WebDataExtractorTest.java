package com.deepak.webcrawler.extractor;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import javax.annotation.Resource;
import javax.transaction.Transactional;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.deepak.webcrawler.entity.WebData;
import com.deepak.webcrawler.entity.WebLink;

/**
 * @author Deepak
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class WebDataExtractorTest {

    @Resource
    private WebDataExtractor webDataExtractor;

    @Test
    public void testExtract() {
        WebData webData = webDataExtractor.extract("http://www.google.com");
        assertEquals("Google", webData.getTitle());
        assertTrue(webData.getMetaDataDescription() == null);
        assertTrue(webData.getMetDataKeyWords()
                          .isEmpty());
        assertEquals(41, webData.getWebLinks()
                                .size());
    }

    @Test
    public void testValidateElementAnchorHref() {
        assertTrue(webDataExtractor.validateElementAnchorHref("http://www.google.com"));
        assertFalse(webDataExtractor.validateElementAnchorHref(null));
        assertFalse(webDataExtractor.validateElementAnchorHref("  "));
        assertFalse(webDataExtractor.validateElementAnchorHref(" / "));
        assertFalse(webDataExtractor.validateElementAnchorHref("javascript: document.dummy.submitForm()"));
    }

    @Test
    public void testFetchLinks() throws IOException {
        String url = "http://www.github.com";
        WebData webData = new WebData();
        Document doc = Jsoup.connect(url)
                            .get();
        webDataExtractor.fetchLinks(url, webData, doc);
        assertEquals(51, webData.getWebLinks()
                                .size());
    }

    @Test
    public void testAddWebLinkToWebDatasWebLinks() {
        WebLink webLink = new WebLink();
        webLink.setUrl("http:www.github.com");
        Set<WebLink> webLinks = new HashSet();
        webDataExtractor.addWebLinkToWebDatasWebLinks(webLinks, webLink);
        assertEquals(1, webLinks.size());
    }

}
