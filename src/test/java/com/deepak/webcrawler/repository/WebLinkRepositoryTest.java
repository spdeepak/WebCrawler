package com.deepak.webcrawler.repository;

import static org.junit.Assert.assertEquals;

import javax.annotation.Resource;
import javax.transaction.Transactional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.deepak.webcrawler.entity.WebLink;

/**
 * @author Deepak
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class WebLinkRepositoryTest {

    @Resource
    private WebLinkRepository webLinkRepository;

    @Before
    public void addData() {
        webLinkRepository.save(new WebLink("https://www.nngroup.com"));
    }

    @Test
    public void testFindByUrlContains() {
        assertEquals(1, webLinkRepository.findByUrlContains("nngroup.com")
                                         .size());
        assertEquals(1, webLinkRepository.findByUrl("https://www.nngroup.com")
                                         .size());
    }

}
