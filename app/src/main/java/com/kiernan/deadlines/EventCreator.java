package com.kiernan.deadlines;

import android.app.Application;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class EventCreator extends AppCompatActivity {

    private ArrayList<String> eventNames, typeNames;
    private boolean update;
    private String reference;

    private AlertDialog fail, confirm;

    private ArrayList hours, minutes, months, days, years;

    private String name, type;
    private int year, month, day, hour, minute;

    //Event Creator Spinners
    private Spinner creatorEventTypeSpinner, hourSpinner, minuteSpinner, monthSpinner, daySpinner,
            yearSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_creator);

        Bundle bundle = getIntent().getExtras();
        eventNames = bundle.getStringArrayList("eventNames");
        typeNames = bundle.getStringArrayList("typeNames");
        update = bundle.getBoolean("update");
        reference = bundle.getString("reference");

        setDialogueBox();
        makeSpinners();

    }

    private void setDialogueBox(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        /*
        Event Addition Failure Dialogue
         */
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

            }
        });
        builder.setMessage("");
        fail = builder.create();

        /*
        Success Alert
         */
        builder.setPositiveButton( "Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                Intent intent = new Intent(EventCreator.this, MainActivity.class);
                Bundle bundle = intent.getExtras();
                bundle.putString("name", name);
                bundle.putString("type", type);
                bundle.putInt("year", year);
                bundle.putInt("month", month);
                bundle.putInt("day", day);
                bundle.putInt("hours", hour);
                bundle.putInt("minutes", minute);
                startActivity(intent);
            }
        });
        builder.setMessage("Completed");
        confirm = builder.create();
    }

    public void makeSpinners() {

        hours = new ArrayList<String>();
        for (int i = 0; i < 10; i++) {
            hours.add("0" + i);
        }
        for (int i = 10; i < 24; i++) {
            hours.add(i);
        }
        hourSpinner = findViewById(R.id.hourSpinner);
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
        minuteSpinner = findViewById(R.id.minuteSpinner);
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, minutes);
        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        minuteSpinner.setAdapter(adapter);

        months = new ArrayList<Integer>();
        for (int i = 1; i < 13; i++) {
            months.add(i);
        }
        monthSpinner = findViewById(R.id.monthSpinner);
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, months);
        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        monthSpinner.setAdapter(adapter);

        days = new ArrayList<Integer>();
        for (int i = 1; i < 32; i++) {
            days.add(i);
        }
        daySpinner = findViewById(R.id.daySpinner);
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, days);
        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        daySpinner.setAdapter(adapter);

        years = new ArrayList<Integer>();
        int y = Calendar.getInstance().get(Calendar.YEAR);
        for (int i = y; i < y + 50; i++) {
            years.add(i);
        }
        yearSpinner = findViewById(R.id.yearSpinner);
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, years);
        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        yearSpinner.setAdapter(adapter);

        //Get and set-up event type spinner for event create/update pop-up
        creatorEventTypeSpinner = findViewById(R.id.typeSpinner);
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, typeNames);
        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        creatorEventTypeSpinner.setAdapter(adapter);

    }

    /*
    The event creation/update window handle on click procedure
     */
    public void setButtons() {

        Button add = findViewById(R.id.addButton);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Get title
                EditText et = findViewById(R.id.newEventName);
                name = et.getText().toString().trim();

                if (name.isEmpty() == false) { //Ensure title isn't empty
                    month = Integer.parseInt(monthSpinner.getSelectedItem().toString());
                    day = Integer.parseInt(daySpinner.getSelectedItem().toString());
                    year = Integer.parseInt(yearSpinner.getSelectedItem().toString());
                    System.out.println(year);
                    if (dayExists()) { //Ensure a false date wasn't provided
                        hour = Integer.parseInt(hourSpinner.getSelectedItem().toString());
                        minute = Integer.parseInt(minuteSpinner.getSelectedItem().toString());
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
                            if (update == false) { //Handle new event
                                if (addViable() == true) {
                                    type = creatorEventTypeSpinner.getSelectedItem().toString();
                                    confirm.show();
                                } else {
                                    fail.setMessage("An object of the given objective title already exists.");
                                    fail.show();
                                }
                            } else { //Handle update event
                                if (updateViable() == true) {
                                    type = creatorEventTypeSpinner.getSelectedItem().toString();
                                    confirm.show();
                                } else {
                                    fail.setMessage("An object of the given objective title already exists.");
                                    fail.show();
                                }
                            }
                        } else {
                            fail.setMessage("The date provided has already passed.");
                            fail.show();
                        }
                    } else {
                        fail.setMessage("Please enter a valid date.");
                        fail.show();
                    }
                } else {
                    fail.setMessage("Please enter a valid objective title.");
                    fail.show();
                }
            }
        });

        Button cancel = findViewById(R.id.cancelButton);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }

    //Operation to test if a date given exists
    public boolean dayExists() {
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

    public boolean addViable(){
        for(int i = 0; i < eventNames.size(); i++){
            if(name.equals(eventNames.get(i))) return false;
        }
        return true;
    }

    public boolean updateViable(){
        for(int i = 0; i < eventNames.size(); i++){
            if(name.equals(eventNames.get(i))) {
                if(reference.equals(eventNames.get(i))) return true;
                return false;
            }
        }
        return true;
    }

}
