package com.kiernan.deadlines;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;

/*
Event Type List with CRUD Functionality

NOTE:

1. The bag must always have a reference to the current state address of the event bag.  Else,
unfortunate glitching may occur.  This setup is intended to make allow events that are given a
certain type to be modified as the type given is removed.

2. The list must always have a reference to a type called "Unclassified."  This is the type an event
will receive if its original type is deleted from the list.  This is so that an event never has a
null type, which would make everything make complicated.
 */
public class EventTypeBag implements Serializable {

    /*
    The list of types
    There will be an immutable type "Unclassified" as a default type which is best left at 0
    */
    private ArrayList<EventType> typeList;

    //A reference to the EventBag for functions such as Remove
    private EventBag eventBag;

    /*
    Constructor - EventBag reference
    This should not be null
     */
    EventTypeBag(EventBag e){

        typeList = new ArrayList<EventType>();
        typeList.add(new EventType("Unclassified"));
        eventBag = e;

    }

    /*
    Constructor for ArrayList<EventType> - soft-copy && EventBag reference
    @Depreciated
     */
    EventTypeBag(EventTypeBag e, EventBag eventBag){
        this.typeList = e.getTypeList();
        this.eventBag = eventBag;
    }

    /*
    Constructor for ArrayList<EventType> - soft-copy
    @Depreciated
     */
    EventTypeBag(ArrayList<EventType> list){
        typeList = list;
    }

    /*
    Add type to list from String
    Return true if String was unique and added
     */
    public boolean add(String s){
        s = s.trim();
        for(int i = 0; i < typeList.size(); i++){
            if(s.equalsIgnoreCase(typeList.get(i).getName())) return false;
        }
        typeList.add(new EventType(s));
        return true;
    }

    //Get EventType by index - Soft Copy
    public EventType getType(int i){
        if(i < typeList.size()) return typeList.get(i);
        return null;
    }

    //Get EventType by name - Soft Copy
    public EventType getType(String name){
        for(int i = 0; i < typeList.size(); i++){
            if(typeList.get(i).getName().equalsIgnoreCase(name)) return typeList.get(i);
        }
        return null;
    }

    /*
    Change the title of a type
    Return true if String was unique and EventType was updated
    May include a method to replace type from index if needed
    */
    public boolean update(String former, String newer){

        former = former.trim();
        newer = newer.trim();

        //Place of the EventType to be updated
        int updateIndex = -1;

        //Current EventType name being compared to former & newer
        String current;

        //Ensure title new title is unique and former exists
        if(newer.equalsIgnoreCase(typeList.get(0).getName()) == false){
            for(int i = 1; i < typeList.size(); i++){
                current = typeList.get(i).getName();
                if(current.equalsIgnoreCase(newer)) return false;
                if(current.equalsIgnoreCase(former)) updateIndex = i;
            }
        }

        //Handle renaming type if conditions were met
        if(updateIndex != -1) {
            typeList.get(updateIndex).setName(newer);
            return true;
        }

        return false;

    }

    /*
    Remove EventType based on name in EventTypeBag and update events with the former type in the
    event bag
    Return true if EventType was found and removed
    */
    public boolean remove(String s){
        s = s.trim();
        for(int i = 1; i < typeList.size(); i++){
            if(s.equalsIgnoreCase(typeList.get(i).getName())){
                eventBag.replaceType(typeList.remove(i).getName(), typeList.get(0));
                return true;
            }
        }
        return false;
    }

    /*
    Clears the bag and updates the event bag accordingly
    Method to be invoked during the clear event type list method
    A NEW ARRAYLIST WITHOUT THE UNCLASSIFIED TYPE SHOULD NEVER OCCUR
    */
    public void clearAndUpdate(){
        typeList = new ArrayList<EventType>();
        typeList.add(new EventType("Unclassified"));
        eventBag.clearTypes(typeList.get(0));
    }

    /*
    Clear the bag but keep the unclassified type
    A NEW ARRAYLIST WITHOUT THE UNCLASSIFIED TYPE SHOULD NEVER OCCUR
    */
    public void clear(){
        typeList = new ArrayList<EventType>();
        typeList.add(new EventType("Unclassified"));
    }

    //TypeList hard-copy getter
    public ArrayList<EventType> getTypeList(){
        ArrayList<EventType> copy = new ArrayList<EventType>();
        for(int i = 0; i < typeList.size(); i++){
            copy.add(typeList.get(i));
        }
        return copy;
    }

    /*
    List getter - names of event types starting from a given index
    This class is meant to be used for appropriate spinners
    */
    public ArrayList<String> getNames(int i){
        ArrayList<String> names = new ArrayList<String>();
        while(i < typeList.size()){
            names.add(typeList.get(i).getName());
            i++;
        }
        return names;
    }

    //typeList soft-copy setter
    public void setTypeList(ArrayList<EventType> list){
        typeList = list;
    }

    //Get list size
    public int getListSize(){
        return typeList.size();
    }

    //Loads the ArrayList from a file.
    //NOTE: IOException includes FileNotFoundException, so a non-existent file shouldn't break shit.
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static ArrayList loadData(Context context) {
        ArrayList toLoad = new ArrayList<>();
        toLoad.add(new EventType("Unclassified"));
        try {
            FileInputStream fis =
                    context.openFileInput("type.dat");
            try (ObjectInputStream ois = new ObjectInputStream(fis)) {
                toLoad = (ArrayList)ois.readObject();
            } catch (ClassNotFoundException ex) {
                System.out.println("LOAD: " + ex.getMessage());
            }
            fis.close();
        } catch (IOException ex) {
            System.out.println("LOAD: " + ex.getMessage());
        }
        return toLoad;
    }

    //Saves the ArrayList to a file
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static void saveData(Context context, ArrayList<EventType> toSave) {
        try {
            FileOutputStream fos;
            fos = context.openFileOutput("type.dat", Context.MODE_PRIVATE);
            try (ObjectOutputStream oos = new ObjectOutputStream(fos)) {
                oos.writeObject(toSave);
            }
            fos.close();
        } catch (IOException ex) {
            System.out.println("SAVE: " + ex.getMessage());
        }
    }

    //Deletes the file from the Android file system
    public static void deleteData() {
        File file = new File("type.dat");

        if(file.delete())
            System.out.println("File deleted successfully.");
        else
            System.out.println("File deletion failed.");
    }

}
