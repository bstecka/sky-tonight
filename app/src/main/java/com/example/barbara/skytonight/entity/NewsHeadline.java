package com.example.barbara.skytonight.entity;

import java.util.Calendar;

public class NewsHeadline {
    private String title;
    private String url;
    private Calendar pubDate;

    public NewsHeadline(String title, String url, Calendar pubDate) {
        this.title = title;
        this.url = url;
        this.pubDate = pubDate;
    }

    public String getTitle() {
        return title;
    }

    public String getUrl() {
        return url;
    }

    public Calendar getPubDate() {
        return pubDate;
    }
}
