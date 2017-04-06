package com.deepak.webcrawler.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.deepak.webcrawler.entity.WebData;

/**
 * @author Deepak
 *
 */
@Repository
public interface WebDataRepository extends JpaRepository<WebData, Long> {

    public List<WebData> findByUrl(String url);

}
