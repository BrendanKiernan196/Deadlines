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
import android.widget.Spinner;
import java.util.ArrayList;

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

    private static EventBag eventBag;
    private static EventTypeBag eventTypeBag;

    RecyclerView eventRecycler;
    private EventAdapter eventAdapter;

    private DrawerLayout drawerLayout;

    private AlertDialog detailEventType, eventTypeFail, eventSelector,
            eventTypeSelector, confirm, error, ceAlert, cetAlert, chAlert, caAlert, adder;

    //For eventSelector and eventTypeSelector - name of event or type to be updated
    private static String reference;

    //Event Creator Spinners
    private Spinner eventSpinner, eventTypeSpinner;

    /*
    For eventCreator and eventTypeCreator - indicate if the information obtained is for an update
    True - update object of name @string/reference
    False - create new object

    For eventSelector and eventTypeSelector - indicate if selected event is to be updated
    True - update object of selected name
    False - delete/complete(stretch goal)
     */
    private static boolean update;

    private EditText eventTypeCreator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        Toolbar toolbar = findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.hamburger_icon);

        drawerLayout = findViewById(R.id.drawer_layout);
        eventRecycler = findViewById(R.id.eventRecycler);

        loadBags();
        makeViews();
        makeSpinners();
        makeDialogue();
        makeHamburger();
        setAddButtonAction();

        try{
            passFromEventCreator();
            System.out.println("Add/Update invoked");
        }
        catch(Exception e){
            System.out.println("Add/Update not invoked");
        }
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
        if(eventBag == null && eventTypeBag == null){

            eventBag = new EventBag();
            eventTypeBag = new EventTypeBag(eventBag);

            //eventBag.setList(eventBag.loadData(0);
            //eventBag.setPastList(eventBag.loadData(1));
            //eventTypeBag.setTypeList(eventTypeBag.loadData());
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
        eventTypeCreator = ((EditText) getLayoutInflater().inflate(R.layout.event_type_creator, null));

        eventSpinner = (Spinner) getLayoutInflater().inflate(R.layout.event_selector, null);
        eventTypeSpinner = (Spinner) getLayoutInflater().inflate(R.layout.event_type_selector, null);

        prepRecyclerView();
    }

    public void makeSpinners() {
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

        renderEventsToView();
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
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
            }
        });
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
        builder.setMessage("Clear all in-progress events?");
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
        builder.setMessage("Clear all currently defined event types?");
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
        builder.setMessage("Clear all current and past events and event types?");
        caAlert = builder.create();

        /*
        Add New & Update Event Type Dialogue
         */
        builder.setMessage("Enter New Event Type:");
        builder.setView(eventTypeCreator);
        builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                String s = eventTypeCreator.getText().toString().trim();
                if (s != null) {
                    if (s.isEmpty() == false) { //Ensure input isn't empty
                        if (update == false) { //Add a new event type
                            if (eventTypeBag.add(s)) {
                                //for(int i = 0; i < eventTypeBag.getListSize(); i++)System.out.println(eventTypeBag.getType(i).getName());
                                eventTypeCreator.setText("");
                                confirm.show();
                                makeSpinners();
                            } else {
                                eventTypeFail.setMessage("The type you entered already exists.");
                                eventTypeFail.show();
                            }
                        } else { //Update a previous event type
                            if (eventTypeBag.update(reference, s)) {
                                eventTypeCreator.setText("");
                                confirm.show();
                                makeSpinners();
                            } else {
                                eventTypeFail.setMessage("The type you entered already exists.");
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
        Update/Delete/Complete event selection pop-up
         */
        //Builder must be redeclared to avoid previous pop-up attributes from overlapping
        builder = new AlertDialog.Builder(this);
        builder.setTitle("Select an event:");
        builder.setView(eventSpinner);
        builder.setPositiveButton("Find", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                reference = eventSpinner.getSelectedItem().toString();
                if (update == true) {
                    switchViews();
                } else {
                    eventBag.remove(reference);
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
        builder.setTitle("Select an event type:");
        builder.setView(eventTypeSpinner);
        builder.setPositiveButton("Find", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                reference = eventTypeSpinner.getSelectedItem().toString();
                if (update == true) {
                    detailEventType.show();
                } else {
                    eventTypeBag.remove(reference);
                    confirm.show();
                    makeSpinners();
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
        String[] options = {"Event", "EventType"};
        builder.setTitle("Add New:").setItems(options, new DialogInterface.OnClickListener() {
            @Override
            //which = index in options
            public void onClick(DialogInterface dialog, int which) {
                if (which == 0) switchViews();
                else detailEventType.show();
            }
        });
        adder = builder.create();

    }

    //Make Hamburger Menu, assign operations to list items, change update boolean accordingly
    public void makeHamburger() {
        NavigationView nView = findViewById(R.id.nav_view);
        nView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        menuItem.setChecked(true);

                        if (menuItem.getTitle().equals("Add Event")) {
                            update = false;
                            switchViews();
                        } else if (menuItem.getTitle().equals("Update Event")) {
                            if (eventBag.getListSize() > 0) {
                                update = true;
                                eventSelector.show();
                            } else {
                                error.setMessage("There are no events to update.");
                                error.show();
                            }
                        } else if (menuItem.getTitle().equals("Remove Event")) {
                            if (eventBag.getListSize() > 0) {
                                update = false;
                                eventSelector.show();
                            } else {
                                error.setMessage("There are no events to remove.");
                                error.show();
                            }
                        } else if (menuItem.getTitle().equals("Add Event Type")) {
                            update = false;
                            detailEventType.show();
                        } else if (menuItem.getTitle().equals("Update Event Type")) {
                            if (eventTypeBag.getListSize() > 1) {
                                update = true;
                                eventTypeSelector.show();
                            } else {
                                error.setMessage("There are no types to update.");
                                error.show();
                            }
                        } else if (menuItem.getTitle().equals("Remove Event Type")) {
                            if (eventTypeBag.getListSize() > 1) {
                                update = false;
                                eventTypeSelector.show();
                            } else {
                                error.setMessage("There are no types to remove.");
                                error.show();
                            }
                        } else if (menuItem.getTitle().equals("Clear Events")) {
                            ceAlert.show();
                        } else if (menuItem.getTitle().equals("Clear Event Types")) {
                            cetAlert.show();
                        } else if (menuItem.getTitle().equals("Clear History")) {
                            chAlert.show();
                        } else if (menuItem.getTitle().equals("Clear All")) {
                            caAlert.show();
                        } else if (menuItem.getTitle().equals("Save")) {
                            //Add message variance for errors
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

    public void switchViews(){
        Intent intent = new Intent(MainActivity.this, EventCreator.class);
        Bundle bundle = intent.getExtras();
        bundle.putStringArrayList("eventNames", eventBag.getNames());
        bundle.putStringArrayList("typeNames", eventTypeBag.getNames(0));
        bundle.putBoolean("update", update);
        bundle.putString("reference", reference);
        startActivity(intent);
    }

    public void passFromEventCreator() throws Exception{

        Bundle bundle = getIntent().getExtras();
        String name = bundle.getString("name");
        String type = bundle.getString("type");
        EventType eventType = eventTypeBag.getType(type);
        int year = bundle.getInt("year");
        int month = bundle.getInt("month");
        int day = bundle.getInt("day");
        int hours = bundle.getInt("hours");
        int minutes = bundle.getInt("minutes");
        if(update == true) eventBag.update(name, reference, eventType, year, month, day, hours, minutes);
        else eventBag.add(name, eventType, year, month, day, hours, minutes);
    }

}
