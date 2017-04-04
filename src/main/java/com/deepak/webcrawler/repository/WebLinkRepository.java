package com.deepak.webcrawler.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.deepak.webcrawler.entity.WebLink;

@Repository
public interface WebLinkRepository extends CrudRepository<WebLink, Long> {

    WebLink findByUrl(String url);

}
