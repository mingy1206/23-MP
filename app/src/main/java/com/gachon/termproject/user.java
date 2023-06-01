package com.gachon.termproject;

import java.util.ArrayList;
import java.util.Map;

public class user {
    String name;
    String ID;
    String PW;

    ArrayList<music_Info> music;

    //Constructor
    public user(){
        this.name = null;
        this.ID = null;
        this.PW = null;
        music = null;
    }
    public user(String name, String ID, String PW){
        this.name =name;
        this.ID = ID;
        this.PW = PW;
    }
}
