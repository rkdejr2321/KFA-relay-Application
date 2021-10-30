package com.example.kfa;

import java.io.Serializable;

public class StartingName implements Serializable {
    private String name;
    public StartingName(String name){
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
