package com.deepak.webcrawler.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.deepak.webcrawler.entity.WebData;

@Repository
public interface WebDataRepository extends CrudRepository<WebData, Long> {

    public WebData findByUrl(String url);
}
