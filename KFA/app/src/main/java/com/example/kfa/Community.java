package com.example.kfa;

import java.io.Serializable;

public class Community implements Serializable {
    private String community_title;
    private String community_content;
    private String postID;
    private long time;

    public Community(String community_title, String community_content,long time, String postID) {
        this.community_title = community_title;
        this.community_content = community_content;
        this.time = time;
        this.postID = postID;
    }

    public Community() {
    }

    public String getCommunity_title() {
        return community_title;
    }

    public String getCommunity_content() {
        return community_content;
    }

    public long getTime(){return time;}

    public String getPostID() { return postID; }
}
