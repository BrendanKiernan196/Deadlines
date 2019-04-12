package com.kiernan.deadlines;

import android.content.DialogInterface;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

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

    private DrawerLayout drawerLayout;

    private AlertDialog confirm, ceAlert, cetAlert, caAlert;

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

    public void makeHamburger() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        //Make confirmation alert
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                drawerLayout.closeDrawers();
            }
        });
        builder.setMessage("Completed");

        confirm = builder.create();

        //Make clear event confirmation dialogue
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                //add clear eventBag method
                confirm.show();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                //NA
            }
        });
        builder.setMessage("Clear all in-progress events?");

        ceAlert = builder.create();

        //Make clear event type confirmation dialogue
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                //add clear eventTypeBag method w/ update to eventBag
                confirm.show();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                //NA
            }
        });
        builder.setMessage("Clear all currently defined event types?");

        cetAlert = builder.create();

        //Make clear event type confirmation dialogue
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                //add clear eventBag method
                //add clear eventTypeBag method
                confirm.show();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                //NA
            }
        });
        builder.setMessage("Clear all current events and event types?");

        caAlert = builder.create();

        //Make Hamburger Menu, assign operations to list items
        NavigationView nView = findViewById(R.id.nav_view);
        nView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        menuItem.setChecked(true);

                        if (menuItem.getTitle().equals("Clear Events")) {
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


}
