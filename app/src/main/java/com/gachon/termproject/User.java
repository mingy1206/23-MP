package com.gachon.termproject;

import java.util.ArrayList;

public class User {
    String name;
    String ID;
    String PW;

    //Constructor
    public User(){
        this.name = null;
        this.ID = null;
        this.PW = null;
    }
    public User(String name, String ID, String PW){
        this.name =name;
        this.ID = ID;
        this.PW = PW;

    }

    public String getName() {
        return name;
    }
    public String getID() {
        return ID;
    }
    public String getPW() {
        return PW;
    }

}
