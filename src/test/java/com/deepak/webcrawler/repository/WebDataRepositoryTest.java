package com.deepak.webcrawler.repository;

import static org.junit.Assert.assertEquals;

import javax.annotation.Resource;
import javax.transaction.Transactional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.deepak.webcrawler.entity.WebData;

/**
 * @author Deepak
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class WebDataRepositoryTest {

    @Resource
    private WebDataRepository webDataRepository;

    @Before
    public void addData() {
        WebData webData = new WebData();
        webData.setUrl("https://www.nngroup.com");
        webDataRepository.save(webData);
        webData = new WebData();
        webData.setUrl("https://www.google.com");
        webDataRepository.save(webData);
    }

    @Test
    public void testFindByUrl() {
        assertEquals(1, webDataRepository.findByUrl("https://www.nngroup.com")
                                         .size());
    }
}
