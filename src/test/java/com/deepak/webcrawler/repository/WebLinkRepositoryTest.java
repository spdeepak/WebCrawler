package com.deepak.webcrawler.repository;

import java.net.MalformedURLException;
import java.util.List;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.deepak.webcrawler.entity.WebLink;

@RunWith(SpringRunner.class)
@SpringBootTest
public class WebLinkRepositoryTest {

    @Resource
    private WebLinkRepository webLinkRepository;

    @Test
    public void test() throws MalformedURLException {
        List<WebLink> webLinks = webLinkRepository.findByUrl("https://www.nngroup.com/");
        webLinkRepository.delete(webLinks);
    }

}
