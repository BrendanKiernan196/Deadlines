package com.kiernan.deadlines;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.net.Uri;

import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/*
    Deadlines Project - Members
    (Insert last names if you wish)

    Brendan Kiernan
    Brandon Ruiz
    Matthew
    Jordy
    Steven Correia
 */

public class MainActivity extends AppCompatActivity {

    private EventBag eventBag;
    private EventTypeBag eventTypeBag;

    RecyclerView eventRecycler;
    private EventAdapter eventAdapter;

    private DrawerLayout drawerLayout;

    private AlertDialog detailEvent, eventFail, detailEventType, eventTypeFail, eventSelector,
            eventTypeSelector, confirm, error, ceAlert, cetAlert, chAlert, caAlert, adder;

    //For eventSelector and eventTypeSelector - name of event or type to be updated
    private String reference;

    /*
    For eventCreator and eventTypeCreator - indicate if the information obtained is for an update
    True - update object of name @string/reference
    False - create new object

    For eventSelector and eventTypeSelector - indicate if selected event is to be updated
    True - update object of selected name
    False - delete/complete(stretch goal)
     */
    private boolean update;

    private RelativeLayout eventCreator;

    private EditText eventTypeCreator;

    //Event Creator Spinners
    private Spinner eventSpinner, eventTypeSpinner, creatorEventTypeSpinner, hourSpinner,
            minuteSpinner, monthSpinner, daySpinner, yearSpinner;

    private ArrayList hours, minutes, months, days, years;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.hamburger2);

        drawerLayout = findViewById(R.id.drawer_layout);
        eventRecycler = findViewById(R.id.eventRecycler);

        loadBags();
        makeViews();
        makeEventSpinners();
        makeTimeSpinners();
        makeDialogue();
        makeHamburger();
        setAddButtonAction();
    }

    //Method to give hamburger menu and add buttons onClick functionality
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            //Home button opens and closes hamburger menu
            case android.R.id.home:
                if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                    drawerLayout.closeDrawer(GravityCompat.START);
                } else {
                    drawerLayout.openDrawer(GravityCompat.START);
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //Instantiate bags and load data from files
    public void loadBags() {
        eventBag = new EventBag();
        eventTypeBag = new EventTypeBag(eventBag);

        try{
        eventBag.setList(eventBag.loadData(0));
        eventBag.setPastList(eventBag.loadData(1));
        eventTypeBag.setTypeList(eventTypeBag.loadData());
        } catch(Exception e){

        }
    }

    /*
    Create references for views to be used throughout the doc

    Views need to be inflated before being placed in a pop-up dialogue box.  However, the inflater
    function appears to make hard copies of each view.  It then becomes necessary to take into
    account that utilizing the findViewById will not access the view in the pop-up nor any of
    its components but instead will reference the mold and its objects.  The addresses of the
    inflated views are saved for this reason.
     */
    public void makeViews() {
        eventCreator = ((RelativeLayout) getLayoutInflater().inflate(R.layout.event_creator, null));
        eventTypeCreator = ((EditText) getLayoutInflater().inflate(R.layout.event_type_creator, null));

        eventSpinner = (Spinner) getLayoutInflater().inflate(R.layout.event_selector, null);
        eventTypeSpinner = (Spinner) getLayoutInflater().inflate(R.layout.event_type_selector, null);
        creatorEventTypeSpinner = (Spinner) eventCreator.getChildAt(3);

        prepRecyclerView();
    }

    public void makeEventSpinners(){
        //Get and set-up event spinner for event update/delete pop-up
        ArrayAdapter adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,
                eventBag.getNames());
        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        eventSpinner.setAdapter(adapter);

        //Get and set-up event type spinner for event update/delete pop-up
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,
                eventTypeBag.getNames(1));
        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        eventTypeSpinner.setAdapter(adapter);

        //Get and set-up event type spinner for event create/update pop-up
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,
                eventTypeBag.getNames(0));
        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        creatorEventTypeSpinner.setAdapter(adapter);

        renderEventsToView();
    }

    public void makeTimeSpinners() {

        hours = new ArrayList<String>();
        for (int i = 0; i < 10; i++) {
            hours.add("0" + i);
        }
        for (int i = 10; i < 24; i++) {
            hours.add(i);
        }
        hourSpinner = (Spinner) eventCreator.getChildAt(5);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, hours);
        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        hourSpinner.setAdapter(adapter);

        minutes = new ArrayList<String>();
        for (int i = 0; i < 10; i++) {
            minutes.add("0" + i);
        }
        for (int i = 10; i < 60; i++) {
            minutes.add(i);
        }
        minuteSpinner = (Spinner) eventCreator.getChildAt(7);
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, minutes);
        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        minuteSpinner.setAdapter(adapter);

        months = new ArrayList<Integer>();
        for (int i = 1; i < 13; i++) {
            months.add(i);
        }
        monthSpinner = (Spinner) eventCreator.getChildAt(9);
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, months);
        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        monthSpinner.setAdapter(adapter);

        days = new ArrayList<Integer>();
        for (int i = 1; i < 32; i++) {
            days.add(i);
        }
        daySpinner = (Spinner) eventCreator.getChildAt(11);
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, days);
        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        daySpinner.setAdapter(adapter);

        years = new ArrayList<Integer>();
        int y = Calendar.getInstance().get(Calendar.YEAR);
        for (int i = y; i < y + 50; i++) {
            years.add(i);
        }
        yearSpinner = (Spinner) eventCreator.getChildAt(13);
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, years);
        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        yearSpinner.setAdapter(adapter);

    }

    /*
    Make all pop-up objects
    The order in which a builder is re-declared and pop-ups are created are important and should not
    yet be tampered with
     */
    public void makeDialogue() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

         /*
        Error Alert
         */
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {public void onClick(DialogInterface dialog, int id) { }});
        error = builder.create();

        /*
        Success Alert
         */
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                drawerLayout.closeDrawers();
            }
        });
        builder.setMessage("Completed");
        confirm = builder.create();

        /*
        Event Addition Failure Dialogue
         */
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                detailEvent.show();
            }
        });
        builder.setMessage("");
        eventFail = builder.create();

        /*
        Event Type Addition Failure Dialogue
         */
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                detailEventType.show();
            }
        });
        builder.setMessage("");
        eventTypeFail = builder.create();

        /*
        Clear All Events Confirmation
         */
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                eventBag.setList(new ArrayList<Event>());
                confirm.show();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                //Close
            }
        });
        builder.setMessage("Clear all in-progress Tasks?");
        ceAlert = builder.create();

        /*
        Clear All Event Types Confirmation
         */
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                eventTypeBag.clearAndUpdate();
                confirm.show();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                //Close
            }
        });
        builder.setMessage("Clear all currently Task Categories?");
        cetAlert = builder.create();

        /*
        Clear Event History Confirmation
         */
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                eventBag.setPastList(new ArrayList<Event>());
                confirm.show();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                //Close
            }
        });
        builder.setMessage("Clear your history?");
        chAlert = builder.create();

        /*
        Clear All Events & Event Types Confirmation
         */
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                eventBag.setList(new ArrayList<Event>());
                eventBag.setPastList(new ArrayList<Event>());
                eventTypeBag.clear();
                confirm.show();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                //Close
            }
        });
        builder.setMessage("Clear all current and past tasks and categories?");
        caAlert = builder.create();

        /*
        Add New & Update Event Type Dialogue
         */
        builder.setMessage("Enter New Task Category:");
        builder.setView(eventTypeCreator);
        builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                String s = eventTypeCreator.getText().toString().trim();
                if (s != null) {
                    if (s.isEmpty() == false) { //Ensure input isn't empty
                        if(update == false){ //Add a new event type
                            if (eventTypeBag.add(s)) {
                                //for(int i = 0; i < eventTypeBag.getListSize(); i++)System.out.println(eventTypeBag.getType(i).getName());
                                eventTypeCreator.setText("");
                                confirm.show();
                                makeEventSpinners();
                            } else {
                                eventTypeFail.setMessage("The Category you entered already exists.");
                                eventTypeFail.show();
                            }
                        } else { //Update a previous event type
                            if(eventTypeBag.update(reference, s)){
                                eventTypeCreator.setText("");
                                confirm.show();
                                makeEventSpinners();
                            } else {
                                eventTypeFail.setMessage("The Category you entered already exists.");
                                eventTypeFail.show();
                            }
                        }
                    } else {
                        eventTypeFail.setMessage("Please enter a valid name.");
                        eventTypeFail.show();
                    }
                } else {
                    eventTypeFail.setMessage("The input string could not be accessed. Please try again.");
                    eventTypeFail.show();
                }
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                //Close
            }
        });
        detailEventType = builder.create();

        /*
        Add New & Update Event Dialogue
         */
        builder.setMessage("Enter New Task:");
        builder.setView(eventCreator);
        builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                detailEventNewEvent();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                //Close
            }
        });
        detailEvent = builder.create();

        /*
        Update/Delete/Complete event selection pop-up
         */
        //Builder must be redeclared to avoid previous pop-up attributes from overlapping
        builder = new AlertDialog.Builder(this);
        builder.setTitle("Select a Task:");
        builder.setView(eventSpinner);
        builder.setPositiveButton("Find", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                reference = eventSpinner.getSelectedItem().toString();
                if(update == true){
                    detailEvent.show();
                }
                else{
                    eventBag.remove(reference);
                    makeEventSpinners();
                    confirm.show();
                }
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                //Close
            }
        });
        eventSelector = builder.create();

        /*
        Update/Delete event type selector pop-up
        */
        builder.setTitle("Select a Task Category:");
        builder.setView(eventTypeSpinner);
        builder.setPositiveButton("Find", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                reference = eventTypeSpinner.getSelectedItem().toString();
                if(update == true){
                    detailEventType.show();
                }
                else{
                    eventTypeBag.remove(reference);
                    confirm.show();
                    makeEventSpinners();
                }
            }
        });
        eventTypeSelector = builder.create();

        /*
        Add button pop-up
         */
        //Builder must be redeclared to avoid previous pop-up attributes from overlapping
        builder = new AlertDialog.Builder(this);
        //Options of what type of new thing to add - pop-up choice
        String[] options = {"Task", "Task Category"};
        builder.setTitle("Add New:").setItems(options, new DialogInterface.OnClickListener() {
            @Override
            //which = index in options
            public void onClick(DialogInterface dialog, int which) {
                if (which == 0) detailEvent.show();
                else detailEventType.show();
            }
        });
        adder = builder.create();

    }

    /*
    The event creation/update window handle on click procedure
     */
    public void detailEventNewEvent() {
        //Get title
        EditText et = (EditText) eventCreator.getChildAt(1);
        String s = et.getText().toString().trim();
        if (s != null) {
            if (s.isEmpty() == false) { //Ensure title isn't empty
                int month = Integer.parseInt(monthSpinner.getSelectedItem().toString());
                int day = Integer.parseInt(daySpinner.getSelectedItem().toString());
                int year = Integer.parseInt(yearSpinner.getSelectedItem().toString());
                System.out.println(year);
                if (dayExists(month, day, year)) { //Ensure a false date wasn't provided
                    int hour = Integer.parseInt(hourSpinner.getSelectedItem().toString());
                    int minute = Integer.parseInt(minuteSpinner.getSelectedItem().toString());
                    //Due to some currently unknown glitch, the paramaters must be modified to provide the correct date
                    Date date = new Date(year - 1900, month - 1, day, hour, minute);
                    Date now = new Date();

                    //Debugger code
                    /*
                    System.out.println(year + " " + month + " " + day + " " + hour + " " + minute);
                    System.out.println(date.toString());
                    System.out.println(date.getTime());
                    System.out.println(now.toString());
                    System.out.println(now.getTime());
                    */

                    if (date.getTime() > now.getTime()) { //Ensure date and time hasn't yet passed
                        EventType type = eventTypeBag.getType(creatorEventTypeSpinner.getSelectedItem().toString());
                       if(update == false) { //Handle new event
                           if (eventBag.add(s, type, year, month, day, hour, minute)) {
                               et.setText("");
                               confirm.show();
                               makeEventSpinners();
                           } else {
                               eventFail.setMessage("An object of the given objective title already exists.");
                               eventFail.show();
                           }
                       }
                       else { //Handle update event
                           if(eventBag.update(reference, s, type, year, month, day, hour, minute)){
                               et.setText("");
                               confirm.show();
                               makeEventSpinners();
                           } else {
                               eventFail.setMessage("An object of the given objective title already exists.");
                               eventFail.show();
                           }
                       }
                    } else {
                        eventFail.setMessage("The date provided has already passed.");
                        eventFail.show();
                    }
                } else {
                    eventFail.setMessage("Please enter a valid date.");
                    eventFail.show();
                }
            } else {
                eventFail.setMessage("Please enter a valid objective title.");
                eventFail.show();
            }
        } else {
            eventFail.setMessage("The input string could not be accessed. Please try again.");
            eventFail.show();
        }
        /*
        else {
            eventFail.setMessage("");
            eventFail.show();
        }
         */

    }

    //Operation to test if a date given exists
    public boolean dayExists(int month, int day, int year) {
        if (day == 31) {
            if (month == 2 || month == 4 || month == 6 || month == 9 || month == 11) return false;
        } else if (month == 2) {
            if (day > 29) return false;
            else if (day == 29) {
                if (year % 4 != 0) return false;
            }
        }
        return true;
    }

    //Make Hamburger Menu, assign operations to list items, change update boolean accordingly
    public void makeHamburger() {
        NavigationView nView = findViewById(R.id.nav_view);
        nView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        menuItem.setChecked(true);

                        if (menuItem.getTitle().equals("Add Task")) {
                            update = false;
                            detailEvent.show();

                        } else if(menuItem.getTitle().equals("Update Tasks")){
                            if(eventBag.getListSize() > 0){
                                update = true;
                                eventSelector.show();
                            }

                            else{
                                error.setMessage("There are no tasks to update.");
                                error.show();
                            }

                        } else if(menuItem.getTitle().equals("Remove Task")){
                            if(eventBag.getListSize() > 0){
                                update = false;
                                eventSelector.show();
                            }

                            else{
                                error.setMessage("There are no tasks to remove.");
                                error.show();
                            }

                        }else if (menuItem.getTitle().equals("Add Task Category")) {
                            update = false;
                            detailEventType.show();

                        } else if(menuItem.getTitle().equals("Update Categories")){
                            if(eventTypeBag.getListSize() > 1){
                                update = true;
                                eventTypeSelector.show();
                            }

                            else{
                                error.setMessage("There are no categories to update.");
                                error.show();
                            }

                        } else if(menuItem.getTitle().equals("Remove Task Category")){
                            if(eventTypeBag.getListSize() > 1){
                                update = false;
                                eventTypeSelector.show();
                            }

                            else{
                                error.setMessage("There are no categories to remove.");
                                error.show();
                            }

                        } else if (menuItem.getTitle().equals("Clear All Tasks")) {
                            ceAlert.show();

                        } else if (menuItem.getTitle().equals("Clear All Task Categories")) {
                            cetAlert.show();

                        } else if (menuItem.getTitle().equals("Clear History")) {
                            chAlert.show();

                        }  else if (menuItem.getTitle().equals("Clear All")) {
                            caAlert.show();
                        }

                        else if (menuItem.getTitle().equals("Advice")) {
                            startActivity(new Intent(MainActivity.this, Main2Activity.class));
                        }
                        else if (menuItem.getTitle().equals("About")) {
                            startActivity(new Intent(MainActivity.this, About.class));
                        }

                        else if (menuItem.getTitle().equals("Tutorial")) {
                            Uri uri = Uri.parse("https://www.youtube.com/watch?v=2MM10X5HlQQ");
                            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                            startActivity(intent);

                        } else if (menuItem.getTitle().equals("Save")) {
                            eventBag.saveData(eventBag.getList(), 0);
                            eventBag.saveData(eventBag.getPastList(), 1);
                            eventTypeBag.saveData(eventTypeBag.getTypeList());
                            confirm.show();
                        }

                        return true;
                    }
                }
        );
    }

    //Set home button action
    public void setAddButtonAction() {
        Button button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                update = false;
                adder.show();

                //Debugger code
                /*
                for(int i = 0; i < eventBag.getListSize(); i++){
                    System.out.println(eventBag.getEvent(i).getInfo());
                }

                for(int i = 0; i < eventTypeBag.getListSize(); i++){
                    System.out.println(eventTypeBag.getType(i).getName());
                }
                */
            }
        });
    }

    //Spins up the EventAdapter class and passes the EventBag ArrayList
    //Commented out so it doesn't break shit inadvertently due to EventBag not being active
    public void prepRecyclerView() {

        eventAdapter = new EventAdapter(eventBag.getList());
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        eventRecycler.setLayoutManager(mLayoutManager);
        eventRecycler.setItemAnimator(new DefaultItemAnimator());
        eventRecycler.setAdapter(eventAdapter);

    }

    //Renders the events to screen, call this whenever a new event is added or things get sorted (maybe)
    //Commented out so it doesn't break shit inadvertently due to EventBag not being active
    public void renderEventsToView() {
        eventAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onStop(){
        eventBag.saveData(eventBag.getList(), 0);
        eventBag.saveData(eventBag.getPastList(), 1);
        eventTypeBag.saveData(eventTypeBag.getTypeList());
        super.onStop();
    }

    @Override
    protected void onDestroy(){
        eventBag.saveData(eventBag.getList(), 0);
        eventBag.saveData(eventBag.getPastList(), 1);
        eventTypeBag.saveData(eventTypeBag.getTypeList());
        super.onDestroy();
    }

}
