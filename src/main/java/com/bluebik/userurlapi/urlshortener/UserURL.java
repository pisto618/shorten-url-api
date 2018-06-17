package com.bluebik.userurlapi.urlshortener;

import javax.persistence.*;

@Entity
public class UserURL {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Integer id;

    @Column(name="key")
    private String key;

    @Column(name="actualurl")
    private String actualURL;

    @Column(name="urlaccessedcounter")
    private Integer urlAccessCounter = 0;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getActualURL() {
        return actualURL;
    }

    public void setActualURL(String actualURL) {
        this.actualURL = actualURL;
    }

    public Integer getUrlAccessCounter() {
        return urlAccessCounter;
    }

    public void setUrlAccessCounter(Integer urlAccessCounter) {
        this.urlAccessCounter = urlAccessCounter;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
