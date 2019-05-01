package com.kiernan.deadlines;

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
Event List with CRUD functionality
 */
public class EventBag implements Serializable {

    //List of events to be completed or failed
    private ArrayList<Event> eventList;

    //List of completed & failed events
    //Event boolean completed = 1 for completed or 0 for failed
    private ArrayList<Event> pastEventList;

    //Constructor for initial setup
    EventBag() {

        eventList = new ArrayList<Event>();
        pastEventList = new ArrayList<Event>();

    }

    //Constructor for EventBag passed
    //May be depreciated due to I/O handling format
    EventBag(EventBag e) {

        eventList = e.getList();
        pastEventList = e.getPastList();

    }

    /*
    Constructor for EventBag passed
    May be depreciated due to I/O handling in the main activity file
    */
    EventBag(ArrayList<Event> eventList, ArrayList<Event> pastEventList) {
        this.eventList = eventList;
        this.pastEventList = pastEventList;

    }

    /*
    Add event with a unique title
    Return true if added and title was unique
    Redundant method
    */
    public boolean add(Event e) {
        for (int i = 0; i < eventList.size(); i++) {
            if (eventList.get(i).getTitle().equalsIgnoreCase(e.getTitle())) return false;
        }
        eventList.add(e);
        return true;
    }

    /*
    Add event with all necessary details given
    Return true if added and title was unique
    */
    public boolean add(String title, EventType type, int year, int month, int day, int hours, int minutes) {
        title = title.trim();
        for (int i = 0; i < eventList.size(); i++) {
            if (eventList.get(i).getTitle().equalsIgnoreCase(title)) return false;
        }
        eventList.add(new Event(title, type, year, month, day, hours, minutes));
        return true;
    }

    //Update Event at a specific index
    public boolean update(int i, String title, EventType type, int year, int month, int day, int hours, int minutes) {
        for (int j = 0; j < eventList.size(); i++) {
            if (eventList.get(j).getTitle().equalsIgnoreCase(title)) {
                if (i != j) {
                    return false;
                } else break;
            }
        }
        eventList.get(i).update(title, type, year, month, day, hours, minutes);
        return true;
    }

    //Update Event - former and new titles given
    public boolean update(String former, String newer, EventType type, int year, int month, int day, int hours, int minutes) {

        //Place of the Event to be updated
        int updateIndex = -1;

        //Current Event name being compared to former & newer
        String current;

        //Ensure title new title is unique and former exists
        for (int i = 0; i < eventList.size(); i++) {
            current = eventList.get(i).getTitle();
            if (current.equalsIgnoreCase(newer)) return false;
            if (current.equalsIgnoreCase(former)) updateIndex = i;
        }

        //Handle renaming type if conditions were met
        if (updateIndex != -1) {
            eventList.get(updateIndex).update(newer, type, year, month, day, hours, minutes);
            return true;
        }

        return false;
    }

    /*
    Remove Event by index
    Return null if index out of bounds
     */
    public Event remove(int i) {
        if (i < eventList.size()) return eventList.remove(i);
        return null;
    }

    /*
    Remove Event by title
    Return null if title non-existent
     */
    public Event remove(String title) {
        for (int i = 0; i < eventList.size(); i++) {
            if (eventList.get(i).getTitle().equalsIgnoreCase(title)) return eventList.remove(i);
        }
        return null;
    }

    /*
    Get Event by index - Hard Copy
    Return null if index out of bounds
     */
    public Event getEvent(int i) {
        if (i < eventList.size()) return new Event(eventList.get(i));
        return null;
    }

    /*
    Get Event by title - Hard Copy
    Return null if title non-existent
     */
    public Event getEvent(String title) {
        title = title.trim();
        for (int i = 0; i < eventList.size(); i++) {
            if (eventList.get(i).getTitle().equalsIgnoreCase(title))
                return new Event(eventList.get(i));
        }
        return null;
    }

    /*
    Replaces all instances of a removed EventType with the default "Unclassified"
    Primarily for EventTypeBag use
    */
    public void replaceType(String removed, EventType unclassified) {
        for (int i = 0; i < eventList.size(); i++) {
            if (eventList.get(i).getType().getName().equalsIgnoreCase(removed)) {
                eventList.get(i).setType(unclassified);
            }
        }
    }

    //List getter - shallow copy
    public ArrayList<Event> getList() {
        return eventList;
    }

    /*
    List getter - names of events
    This class is meant to be used for selection spinners in the remove/delete event UI
    */
    public ArrayList<String> getNames() {
        ArrayList<String> names = new ArrayList<String>();
        for (int i = 0; i < eventList.size(); i++) {
            names.add(eventList.get(i).getTitle());
        }
        return names;
    }

    //List setter - soft copy
    public void setList(ArrayList<Event> list) {
        eventList = list;
    }

    //Replace types of events after the type bag was reset
    public void clearTypes(EventType unclassified) {
        for (int i = 0; i < pastEventList.size(); i++) {
            eventList.get(i).setType(unclassified);
        }
    }

    //Mark an event as completed - index passed
    public void complete(int i) {
        eventList.get(i).complete();
        pastEventList.add(eventList.remove(i));
    }

    /*
    Mark an event as completed - title passed
    This method is optimized to a procedure in which the user chooses an event to complete from a
    spinner
     */
    public void complete(String title) {
        title = title.trim();
        for (int i = 0; i < eventList.size(); i++) {
            if (eventList.get(i).getTitle().equalsIgnoreCase(title)) {
                eventList.get(i).complete();
                pastEventList.add(eventList.remove(i));
                break;
            }
        }
    }

    //Mark an event as failed - index passed
    public void fail(int i) {
        pastEventList.add(eventList.remove(i));
    }

    //Mark an event as failed - title passed
    public void fail(String title) {
        title = title.trim();
        for (int i = 0; i < eventList.size(); i++) {
            if (eventList.get(i).getTitle().equalsIgnoreCase(title)) {
                pastEventList.add(eventList.remove(i));
                break;
            }
        }
    }

    //Get list size
    public int getListSize() {
        return eventList.size();
    }

    //Completed list getter - hard copy
    public ArrayList<Event> getPastList() {
        ArrayList<Event> copy = new ArrayList<Event>();
        for (int i = 0; i < pastEventList.size(); i++) {
            copy.add(new Event(pastEventList.get(i)));
        }
        return copy;
    }

    //List getter - past event names
    public ArrayList<String> getPastNames() {
        ArrayList<String> names = new ArrayList<String>();
        for (int i = 0; i < pastEventList.size(); i++) {
            names.add(pastEventList.get(i).getTitle());
        }
        return names;
    }

    //Past list setter - soft copy
    public void setPastList(ArrayList<Event> list) {
        pastEventList = list;
    }

    //Get past list size
    public int getPastListSize() {
        return pastEventList.size();
    }

    //Loads the ArrayList from a file. Pass 0 for current events, not 0 for past events.
    //NOTE: IOException includes FileNotFoundException, so a non-existent file shouldn't break shit.
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static ArrayList loadData(int signal) {
        ArrayList toLoad = new ArrayList<>();

        try {
            FileInputStream fis;

            if (signal == 0)
                fis = new FileInputStream("event.dat");
            else
                fis = new FileInputStream("past_event.dat");

            try (ObjectInputStream ois = new ObjectInputStream(fis)) {
                toLoad = (ArrayList) ois.readObject();
            } catch (ClassNotFoundException ex) {
                System.out.println("LOAD: " + ex.getMessage());
            }
            fis.close();
        } catch (IOException ex) {
            System.out.println("LOAD: " + ex.getMessage());
        }
        return toLoad;
    }

    //Saves the ArrayList to a file. Pass 0 for current events, not 0 for past events.
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static void saveData(ArrayList<Event> toSave, int signal) {
        try {
            FileOutputStream fos;
            if (signal == 0)
                fos = new FileOutputStream("event.dat");
            else
                fos = new FileOutputStream("past_event.dat");
            try (ObjectOutputStream oos = new ObjectOutputStream(fos)) {
                oos.writeObject(toSave);
            }
            fos.close();
        } catch (IOException ex) {
            System.out.println("SAVE: " + ex.getMessage());
        }
    }

    //Deletes the file from the Android file system. Pass 0 for current events, not 0 for past events.
    public static void deleteData(int signal) {
        File file;

        if (signal == 0)
            file = new File("event.dat");
        else
            file = new File("past_event.dat");

        if (file.delete())
            System.out.println("File deleted successfully.");
        else
            System.out.println("File deletion failed.");
    }

}
