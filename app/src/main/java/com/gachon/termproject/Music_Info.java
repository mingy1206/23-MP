package com.gachon.termproject;

import java.util.ArrayList;

public class Music_Info {
    public ArrayList url;

    public ArrayList getUrl() {
        return url;
    }
    public void setUrl(ArrayList url){
        this.url = url;
    }
    public void add(String u){
        this.url.add(u);
    }
}
