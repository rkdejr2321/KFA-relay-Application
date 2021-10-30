package com.example.kfa;

import java.io.Serializable;

public class Reply implements Serializable {
    private String parentID;
    private String replyText;
    private String replyID;
    private boolean nest = false;

    public Reply(String parentID, String replyText, String replyID){
        this.parentID = parentID;
        this.replyText =replyText;
        this.replyID = replyID;
    }

    public Reply(){}

    public void setNest(boolean nest){this.nest = nest;}

    public String getParentID() {
        return parentID;
    }

    public String getReplyText() {
        return replyText;
    }

    public String getReplyID() {
        return replyID;
    }

    public boolean getNest(){return nest;}

}