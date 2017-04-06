package com.deepak.webcrawler.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.deepak.webcrawler.entity.WebData;

@Repository
public interface WebDataRepository extends JpaRepository<WebData, Long> {

    public WebData findByUrl(String url);
}
