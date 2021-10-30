package com.example.kfa;

public class News {
    private String news_title;
    private String news_content;
    private String img_url;

    public News(String news_title, String news_content, String img_url) {
        this.news_title = news_title;
        this.news_content = news_content;
        this.img_url = img_url;
    }

    public News() {
    }

    public String getNews_title() {
        return news_title;
    }

    public String getNews_content() {
        return news_content;
    }

    public String getUrl() {
        return img_url;
    }
}
