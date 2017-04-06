package com.deepak.webcrawler.entity;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.OneToMany;

/**
 * @author Deepak
 *
 */
@Entity
public class WebData {

    @Id
    @GeneratedValue
    private long id;

    @Column(unique = true)
    @Lob
    private String url;

    private String title;

    @Lob
    private String metaDataDescription;

    @ElementCollection(fetch = FetchType.EAGER)
    private Set<String> metDataKeyWords = new HashSet<>();

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JoinColumn
    private Set<WebLink> webLinks = new HashSet<>();

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMetaDataDescription() {
        return metaDataDescription;
    }

    public void setMetaDataDescription(String metaDataDescription) {
        this.metaDataDescription = metaDataDescription;
    }

    public Set<String> getMetDataKeyWords() {
        return metDataKeyWords;
    }

    public void setMetDataKeyWords(Set<String> metDataKeyWords) {
        this.metDataKeyWords = metDataKeyWords;
    }

    public Set<WebLink> getWebLinks() {
        return webLinks;
    }

    public void setWebLinks(Set<WebLink> webLinks) {
        this.webLinks = webLinks;
    }

}
