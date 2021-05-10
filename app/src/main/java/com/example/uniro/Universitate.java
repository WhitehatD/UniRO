package com.example.uniro;

public class Universitate {

    private String nume, desc;

    private Universitate() {}{

    }

    private Universitate(String nume, String desc){
        this.nume = nume;
        this.desc = desc;
    }

    public String getName(){
        return nume;
    }

    public void setName(String nume){
        this.nume = nume;
    }

    public String getDesc(){
        return desc;
    }

    public void setDesc(String desc){
        this.desc = desc;
    }
}
