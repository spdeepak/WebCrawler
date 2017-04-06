package com.deepak.webcrawler.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.deepak.webcrawler.entity.WebLink;

/**
 * @author Deepak
 *
 */
@Repository
public interface WebLinkRepository extends JpaRepository<WebLink, Long> {

    List<WebLink> findByUrl(String url);

}
