package com.gachon.termproject;

import java.util.ArrayList;

public class User {
    String name;
    String ID;
    String PW;

    ArrayList<Music_Info> music;

    //Constructor
    public User(){
        this.name = null;
        this.ID = null;
        this.PW = null;
        music = null;
    }
    public User(String name, String ID, String PW){
        this.name =name;
        this.ID = ID;
        this.PW = PW;
        music = null;

    }
}
