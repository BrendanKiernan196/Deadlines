package com.kiernan.deadlines;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;

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
            eventTypeSelector, confirm, ceAlert, cetAlert, caAlert, adder;

    //For eventSelector and eventTypeSelector - name of event or type to be updated
    private String reference;

    //For eventSelector and eventTypeSelector - indicate if the selected object is to be deleted
    //True - delete
    //False - update or complete (See following bool)
    private boolean delete;

    /*
    For eventCreator and eventTypeCreator - indicate if the information obtained is for an update
    True - update object of name @string/reference
    False - create new object

    For eventSelect - indicate if selected event is to be updated
    True - update object of selected name
    False - complete (stretch goal)
     */
    private boolean update;

    private RelativeLayout eventCreator;

    private EditText eventTypeCreator;

    //Event Creator Spinners
    private Spinner creatorEventTypeSpinner, hourSpinner, minuteSpinner, monthSpinner, daySpinner,
            yearSpinner;

    private ArrayList hours, minutes, months, days, years;

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
        //eventBag.setList(eventBag.loadData(0);
        //eventBag.setPastList(eventBag.loadData(1));

        eventTypeBag = new EventTypeBag(eventBag);
        //eventTypeBag.setTypeList(eventTypeBag.loadData());
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

        prepRecyclerView();
    }

    public void makeTimeSpinners() {
        creatorEventTypeSpinner = (Spinner) eventCreator.getChildAt(3);
        ArrayList<String> options = eventTypeBag.getNames();
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, options);
        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        creatorEventTypeSpinner.setAdapter(adapter);

        hours = new ArrayList<String>();
        for (int i = 0; i < 10; i++) {
            hours.add("0" + i);
        }
        for (int i = 10; i < 24; i++) {
            hours.add(i);
        }
        hourSpinner = (Spinner) eventCreator.getChildAt(5);
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, hours);
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

    public void makeDialogue() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

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
        Clear All Events & Event Types Confirmation
         */
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                eventBag.setList(new ArrayList<Event>());
                eventTypeBag.clear();
                confirm.show();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                //Close
            }
        });
        builder.setMessage("Clear all current events and event types?");
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
                    if (s.isEmpty() == false) {
                        if (eventTypeBag.add(s)) {
                            //for(int i = 0; i < eventTypeBag.getListSize(); i++)System.out.println(eventTypeBag.getType(i).getName());
                            eventTypeCreator.setText("");
                            confirm.show();
                        } else {
                            eventTypeFail.setMessage("The type you entered already exists.");
                            eventTypeFail.show();
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
        builder.setMessage("Enter New Event Type:");
        builder.setView(eventCreator);
        builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                eventCreatorNewEvent();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                //Close
            }
        });
        detailEvent = builder.create();

        /*
        Add/Update/Complete event selection pop-up
         */
        //Get and set-up spinner for pop-up
        final Spinner spinner = (Spinner) getLayoutInflater().inflate(R.layout.event_selector, null);
        ArrayAdapter adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,
                eventBag.getNames());
        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        //Builder must be redeclared to avoid previous pop-up attributes from overlapping
        builder = new AlertDialog.Builder(this);
        builder.setTitle("Select an event to update");
        builder.setPositiveButton("Find", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                reference = spinner.getSelectedItem().toString();
                detailEvent.show();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                //Close
            }
        });
        eventSelector = builder.create();

        /*
        Add/Update event type selector pop-up

        final Spinner spinnerB = (Spinner) getLayoutInflater().inflate(R.layout.event_type_selector, null);
        ArrayAdapter adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,
                eventBag.getNames());
        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spinnerB.setAdapter(adapter);
        */

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
                if (which == 0) detailEvent.show();
                else detailEventType.show();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                //Close
            }
        });
        adder = builder.create();

    }

    public void eventCreatorNewEvent() {
        EditText et = (EditText) eventCreator.getChildAt(1);
        String s = et.getText().toString().trim();
        if (s != null) {
            if (s.isEmpty() == false) {
                int month = Integer.parseInt(monthSpinner.getSelectedItem().toString());
                int day = Integer.parseInt(daySpinner.getSelectedItem().toString());
                int year = Integer.parseInt(yearSpinner.getSelectedItem().toString());
                System.out.println(year);
                if (dayExists(month, day, year)) {
                    int hour = Integer.parseInt(hourSpinner.getSelectedItem().toString());
                    int minute = Integer.parseInt(minuteSpinner.getSelectedItem().toString());
                    //Due to some currently unknown glitch, the paramaters must be modified to provide the correct date
                    Date date = new Date(year - 1900, month - 1, day, hour, minute);
                    Date now = new Date();

                    System.out.println(year + " " + month + " " + day + " " + hour + " " + minute);
                    System.out.println(date.toString());
                    System.out.println(date.getTime());
                    System.out.println(now.toString());
                    System.out.println(now.getTime());

                    if (date.getTime() > now.getTime()) {
                        EventType type = eventTypeBag.getType(creatorEventTypeSpinner.getSelectedItem().toString());
                        //Insert conditional here
                        if (eventBag.add(s, type, year, month, day, hour, minute)) {
                            et.setText("");
                            confirm.show();
                        } else {
                            eventFail.setMessage("An object of the given objective title already exists.");
                            eventFail.show();
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

    public void makeHamburger() {
        //Make Hamburger Menu, assign operations to list items
        NavigationView nView = findViewById(R.id.nav_view);
        nView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        menuItem.setChecked(true);

                        if (menuItem.getTitle().equals("Add Event")) {
                            detailEvent.show();
                        } else if (menuItem.getTitle().equals("Add Event Type")) {
                            detailEventType.show();
                        } else if (menuItem.getTitle().equals("Clear Events")) {
                            ceAlert.show();
                        } else if (menuItem.getTitle().equals("Clear Event Types")) {
                            cetAlert.show();
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

    public void setAddButtonAction() {
        Button button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adder.show();
            }
        });
    }

    //Spins up the EventAdapter class and passes the EventBag ArrayList
    //Commented out so it doesn't break shit inadvertently due to EventBag not being active
    public void prepRecyclerView() {
        /*
        eventAdapter = new EventAdapter(eventBag.getList());
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        eventRecycler.setLayoutManager(mLayoutManager);
        eventRecycler.setItemAnimator(new DefaultItemAnimator());
        eventRecycler.setAdapter(eventAdapter);
        */
    }

    //Renders the events to screen, call this whenever a new event is added or things get sorted (maybe)
    //Commented out so it doesn't break shit inadvertently due to EventBag not being active
    public void renderEventsToView() {
        //eventAdapter.notifyDataSetChanged();
    }
}
