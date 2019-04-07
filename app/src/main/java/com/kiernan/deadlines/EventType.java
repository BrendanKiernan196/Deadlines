package com.kiernan.deadlines;

public class EventType {

    private String name;

    EventType(String name){
        this.name = name;
    }

    EventType(EventType e){ name = e.getName();}

    public String getName(){
        return name;
    }

    public void setName(String name){
        this.name = name;
    }

}
