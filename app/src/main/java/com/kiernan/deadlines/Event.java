package com.kiernan.deadlines;

public class Event {

    //Name & basic description of an event
    private String title;

    //Category of events to which an event belongs
    private EventType type;

    //Event deadline time ints
    private int year, month, day, hours, minutes;

    //Supplementary
    //Objective completion indicator
    //May be used if integrating a list of completed events in the app
    private Boolean completed;

    //Constructor with day, month, & year
    Event(String title, EventType type, int year, int month, int day){

        this.title = title.trim();
        this.type = type;
        this.year = year;
        this.month = month;
        this.day = day;
        hours = 0;
        minutes = 0;
        completed = false;

    }

    //Constructor with day, month, year, minutes, hours
    Event(String title, EventType type, int year, int month, int day, int hours, int minutes){

        this.title = title.trim();
        this.type = type;
        this.year = year;
        this.month = month;
        this.day = day;
        this.hours = hours;
        this.minutes = minutes;
        completed = false;

    }

    //Hard copy constructor
    Event(Event e){

        this.title = e.getTitle();
        this.type = e.getType();
        this.year = e.getYear();
        this.month = e.getMonth();
        this.day = e.getDay();
        this.hours = e.getHours();
        this.minutes = e.getMinutes();
        completed = false;

    }

    //Update event
    public void update(String title, EventType type, int year, int month, int day, int hours, int minutes) {
        this.title = title.trim();
        this.type = type;
        this.year = year;
        this.month = month;
        this.day = day;
        this.hours = hours;
        this.minutes = minutes;
    }

    //Information String for RecyclerView
    public String getInfo(){
        return title + "\t" + type.getName() + "\t" + month + "/" + day + "/" + year + "\t" +
                hours / 10 + "" + hours % 10 + ":" + minutes / 10 + "" + minutes % 10;
    }

    //Mark this event as completed
    public void complete(){
        completed = true;
    }

    //Getters and Setters

    public String getTitle() {
        return title;
    }

    public EventType getType(){
        return type;
    }

    public void setType(EventType type){ this.type = type; }

    public int getYear(){
        return year;
    }

    public int getMonth(){
        return month;
    }

    public int getDay(){
        return day;
    }

    public int getHours(){
        return hours;
    }

    public int getMinutes(){
        return minutes;
    }

}
