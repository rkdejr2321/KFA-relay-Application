package com.example.kfa;

import java.io.Serializable;

public class GameInfo implements Serializable {
    private String homeName;
    private String awayName;
    private String title;
    private String info;
    private String homeScore;
    private String awayScore;
    private String homeFlag;
    private String awayFlag;
    private String relay_url;
    private String path;

    public GameInfo(String homeName, String awayName, String info, String title,String homeScore, String awayScore, String homeFlag, String awayFlag){
        this.homeName = homeName;
        this.awayName = awayName;
        this.info = info;
        this.title = title;
        this.homeScore = homeScore;
        this.awayScore = awayScore;
        this.homeFlag =  homeFlag;
        this.awayFlag = awayFlag;
    }
    public  GameInfo(){}

    public String getHomeName(){return homeName;}
    public String getAwayName(){return awayName;}
    public String getInfo(){return info;}
    public String getTitle(){return title;}
    public String getHomeScore(){return homeScore;}
    public String getAwayScore(){return awayScore;}
    public String getHomeFlag(){return homeFlag;}
    public String getAwayFlag(){return awayFlag;}
    public String getRelay_url(){return relay_url;}
    public String getPath(){return path;}

}
