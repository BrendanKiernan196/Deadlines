package com.kiernan.deadlines;

import android.os.Build;
import android.support.annotation.RequiresApi;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
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

    EventTypeBag(EventBag e){

        typeList = new ArrayList<EventType>();
        typeList.add(new EventType("Unclassified"));
        eventBag = e;

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

    //Remove EventType based on name in EventTypeBag and corresponding EventBag
    //Return true if EventType was found and removed
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

    //Get list size
    public int getListSize(){
        return typeList.size();
    }

    //Loads the ArrayList from a file.
    //NOTE: IOException includes FileNotFoundException, so a non-existent file shouldn't break shit.
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static ArrayList loadData() {
        ArrayList toLoad = new ArrayList<>();
        toLoad.add(new EventType("Unclassified"));
        try {
            FileInputStream fis =
                    new FileInputStream("type.dat");
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
    public static void saveData(ArrayList<EventType> toSave) {
        try {
            FileOutputStream fos;
            fos = new FileOutputStream("type.dat");
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
