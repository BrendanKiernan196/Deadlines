package com.kiernan.deadlines;

import java.io.Serializable;
import java.util.ArrayList;

public class EventTypeBag implements Serializable {

    //The list of types
    //There will be an immutable type "Unclassified" as a default type which is best left at 0
    private ArrayList<EventType> typeList;

    //A reference to the EventBag for functions such as Remove
    private EventBag eventBag;

    //Constructor for the initial creation of the bag
    EventTypeBag(){

        typeList = new ArrayList<EventType>();
        typeList.add(new EventType("Unclassified"));

    }

    EventTypeBag(EventTypeBag e){
        this.typeList = e.getTypeList();
    }

    EventTypeBag(EventTypeBag e, EventBag eventBag){
        this.typeList = e.getTypeList();
        this.eventBag = eventBag;
    }

    //Constructor for ArrayList<EventType> - soft-copy
    EventTypeBag(ArrayList<EventType> list){
        typeList = list;
    }

    //Add type to list from String
    //Return true if String was unique and added
    public boolean add(String s){
        s = s.trim();
        for(int i = 0; i < typeList.size(); i++){
            if(s.equalsIgnoreCase(typeList.get(i).getName())) return false;
        }
        typeList.add(new EventType(s));
        return true;
    }

    //Change the title of a type
    //Return true if String was unique and EventType was updated
    public boolean update(String former, String newer){

        former = former.trim();
        newer = newer.trim();

        //Place of the EventType to be updated
        int updateIndex = -1;

        //Current EventType name being compared to former & newer
        String current;

        if(former.equalsIgnoreCase(typeList.get(0).getName()) == false){
            for(int i = 1; i < typeList.size(); i++){
                current = typeList.get(i).getName();
                if(current.equalsIgnoreCase(newer)) return false;
                if(current.equalsIgnoreCase(former)) updateIndex = i;
            }
        }

        if(updateIndex != -1) {
            typeList.get(updateIndex).setName(newer);
            return true;
        }

        return false;

    }

    //Remove EventType based on name
    //Return true if EventType was found and removed
    public boolean remove(String s){
        s = s.trim();
        for(int i = 1; i < typeList.size(); i++){
            if(s.equalsIgnoreCase(typeList.get(i).getName())){
                eventBag.replaceType(typeList.remove(i), typeList.get(0));
                return true;
            }
        }
        return false;
    }

    //typeList hard-copy getter
    public ArrayList<EventType> getTypeList(){
        ArrayList<EventType> copy = new ArrayList<EventType>();
        for(int i = 0; i < typeList.size(); i++){
            copy.add(typeList.get(i));
        }
        return copy;
    }

    //typeList soft-copy setter
    public void setTypeList(ArrayList<EventType> list){
        typeList = list;
    }

}
