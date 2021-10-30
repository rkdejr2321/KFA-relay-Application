package com.example.kfa;

import androidx.recyclerview.widget.RecyclerView;

import java.io.Serializable;

public class Relay implements Serializable {
    private String relay;
    public Relay(String relay){
        this.relay = relay;
    }

    public String getRelay() {
        return relay;
    }
}
