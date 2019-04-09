package com.kiernan.deadlines;

import java.io.Serializable;
import java.util.ArrayList;

public class EventBag implements Serializable {

    //List of events to be completed or failed
    private ArrayList<Event> eventList;

    //List of completed & failed events
    //Event boolean completed = 1 for completed or 0 for failed
    private ArrayList<Event> pastEventList;

    //Constructor for initial setup
    EventBag(){

        eventList = new ArrayList<Event>();
        pastEventList = new ArrayList<Event>();

    }

    EventBag(EventBag e){

        eventList = e.getList();
        pastEventList = e.getPastList();

    }

    EventBag(ArrayList<Event> eventList, ArrayList<Event> pastEventList){
        this.eventList = eventList;
        this.pastEventList = pastEventList;

    }

    //Add event
    //Return true if added and title was unique
    public boolean add(Event e){
        for(int i = 0; i < eventList.size(); i++){
            if(eventList.get(i).getTitle().equalsIgnoreCase(e.getTitle())) return false;
        }
        eventList.add(e);
        return true;
    }

    //Add event from details
    //Return true if added and title was unique
    public boolean add(String title, EventType type, int year, int month, int day, int hours, int minutes){
        title = title.trim();
        for(int i = 0; i < eventList.size(); i++){
            if(eventList.get(i).getTitle().equalsIgnoreCase(title)) return false;
        }
        eventList.add(new Event(title, type, year, month, day, hours, minutes));
        return true;
    }

    //Update Event at a specific index
    public void update(int i, String title, EventType type, int year, int month, int day, int hours, int minutes){
        eventList.get(i).update(title, type, year, month, day, hours, minutes);
    }

    //Remove Event by index
    public Event remove(int i){
        if(i < eventList.size()) return eventList.remove(i);
        return null;
    }

    //Remove Event by title
    public Event remove(String title){
        for(int i = 0; i < eventList.size(); i++){
            if(eventList.get(i).getTitle().equalsIgnoreCase(title)) return eventList.remove(i);
        }
        return null;
    }

    //Get Event by index - Hard Copy
    public Event getEvent(int i){
        if(i < eventList.size()) return new Event(eventList.get(i));
        return null;
    }

    //Get Event by title - Hard Copy
    public Event getEvent(String title){
        title = title.trim();
        for(int i = 0; i < eventList.size(); i++){
            if(eventList.get(i).getTitle().equalsIgnoreCase(title)) return new Event(eventList.get(i));
        }
        return null;
    }

    //For EventTypeBag use
    //Replaces all instances of a removed EventType with the default
    public void replaceType(String removed, EventType unclassified){
        for(int i = 0; i < eventList.size(); i++) {
            if (eventList.get(i).getType().getName().equalsIgnoreCase(removed)) {
                eventList.get(i).setType(unclassified);
            }
        }
    }

    //List getter - hard copy
    public ArrayList<Event>  getList(){
        ArrayList<Event> copy = new ArrayList<Event>();
        for(int i = 0; i < eventList.size(); i++){
            copy.add(new Event(eventList.get(i)));
        }
        return copy;
    }

    //List setter - soft copy
    public void setList(ArrayList<Event> list){
        eventList = list;
    }

    //Mark an event as completed - index passed
    public void complete(int i){
        eventList.get(i).complete();
        pastEventList.add(eventList.remove(i));
    }

    //Mark an event as completed - title passed
    public void complete(String title){
        title = title.trim();
        for(int i = 0; i < eventList.size(); i++){
            if(eventList.get(i).getTitle().equalsIgnoreCase(title)) {
                eventList.get(i).complete();
                pastEventList.add(eventList.remove(i));
                break;
            }
        }
    }

    //Mark an event as failed - index passed
    public void fail(int i){
        pastEventList.add(eventList.remove(i));
    }

    //Mark an event as failed - title passed
    public void fail(String title){
        title = title.trim();
        for(int i = 0; i < eventList.size(); i++){
            if(eventList.get(i).getTitle().equalsIgnoreCase(title)) {
                pastEventList.add(eventList.remove(i));
                break;
            }
        }
    }

    //Get list size
    public int getListSize(){
        return eventList.size();
    }

    //Completed list getter - hard copy
    public ArrayList<Event> getPastList(){
        ArrayList<Event> copy = new ArrayList<Event>();
        for(int i = 0; i < pastEventList.size(); i++){
            copy.add(new Event(pastEventList.get(i)));
        }
        return copy;
    }

    //Past list setter - soft copy
    public void setPastList(ArrayList<Event> list){
        pastEventList = list;
    }

    //Get past list size
    public int getPastListSize(){
        return pastEventList.size();
    }

}
