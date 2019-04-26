package com.kiernan.deadlines;

import android.content.DialogInterface;
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
import android.widget.EditText;
import android.widget.RelativeLayout;

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

    private EventBag eventBag;
    private EventTypeBag eventTypeBag;

    RecyclerView eventRecycler;
    private EventAdapter eventAdapter;

    private DrawerLayout drawerLayout;

    private AlertDialog newEvent, newEventType, eventTypeFail,
            confirm, ceAlert, cetAlert, caAlert;

    private RelativeLayout eventCreator;

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
        makeDialogue();
        makeHamburger();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void loadBags() {
        eventBag = new EventBag();
        //eventBag.setList(eventBag.loadData(0);
        //eventBag.setPastList(eventBag.loadData(1));

        eventTypeBag = new EventTypeBag(eventBag);
        //eventTypeBag.setTypeList(eventTypeBag.loadData());
    }

    public void makeViews(){
        eventCreator = ((RelativeLayout) getLayoutInflater().inflate(R.layout.event_creator, null));
        eventTypeCreator = ((EditText) getLayoutInflater().inflate(R.layout.event_type_creator, null));
        prepRecyclerView();
    }

    public void makeDialogue(){
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
        Event Type Addition Failure Dialogue
         */
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                newEventType.show();
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
        Add New Event Type Dialogue
         */
        builder.setMessage("Enter New Event Type:");
        builder.setView(eventTypeCreator);
        builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                String s = eventTypeCreator.getText().toString().trim();
                if(s != null){
                    if(s.isEmpty() == false){
                        if(eventTypeBag.add(s)) {
                            //for(int i = 0; i < eventTypeBag.getListSize(); i++)System.out.println(eventTypeBag.getType(i).getName());
                            eventTypeCreator.setText("");
                            confirm.show();
                        }
                        else{
                            eventTypeFail.setMessage("The type you entered already exists.");
                            eventTypeFail.show();
                        }
                    }
                    else{
                        eventTypeFail.setMessage("Please enter a valid name");
                        eventTypeFail.show();
                    }
                }
                else{
                    eventTypeFail.setMessage("The input string could not be accessed. Please try again");
                    eventTypeFail.show();
                }
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                //Close
            }
        });
        newEventType = builder.create();

        /*
        Add new event dialogue
         */
        builder.setMessage("Enter New Event Type:");
        builder.setView(eventCreator);
        builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

                //To be added

            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                //Close
            }
        });
        newEvent = builder.create();

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
                            newEvent.show();
                        }
                        else if (menuItem.getTitle().equals("Add Event Type")) {
                            newEventType.show();
                        }
                        else if (menuItem.getTitle().equals("Clear Events")) {
                            ceAlert.show();
                        }
                        else if(menuItem.getTitle().equals("Clear Event Types")){
                            cetAlert.show();
                        }
                        else if(menuItem.getTitle().equals("Clear All")){
                            caAlert.show();
                        }
                        else if(menuItem.getTitle().equals("Save")){
                            //Add message variance for errors
                            confirm.show();
                        }

                        return true;
                    }
                }
        );
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
