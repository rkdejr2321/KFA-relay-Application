package com.example.kfa;

import java.io.Serializable;
import java.util.ArrayList;

public class Player implements Serializable {
    private String name;
    private int age;
    private String team;
    private String pos;
    private int height;
    private int weight;
    private String img;

    public Player(String name, int age,  String team, String pos, int height, int weight, String img){
        this.name = name;
        this.age = age;
        this.team = team;
        this.pos = pos;
        this.height = height;
        this.weight = weight;
        this.img = img;
    }
    public Player(){}

    public String getName(){return name;}
    public int getAge(){return age;}
    public String getTeam(){return team;}
    public String getPos(){return pos;}
    public String getHeight(){return String.valueOf(height);}
    public String getWeight(){return String.valueOf(weight);}
    public String getImg(){return img;}

}
