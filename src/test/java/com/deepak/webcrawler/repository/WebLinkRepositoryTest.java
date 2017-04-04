package com.deepak.webcrawler.repository;

import java.net.MalformedURLException;

import javax.annotation.Resource;

import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

import com.deepak.webcrawler.entity.WebData;

@SpringBootTest
public class WebLinkRepositoryTest {

    @Resource
    private WebDataRepository webLinkRepository;

    @Test
    public void test() throws MalformedURLException {
        WebData web = new WebData();
    }

}
