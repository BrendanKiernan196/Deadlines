package com.kiernan.deadlines;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class EventAdapter extends RecyclerView.Adapter {

    private ArrayList<Event> events; //this would hold the ArrayList of events

    //Instantiates a ViewHolder
    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        TextView type;

        //find those views by id here
        public MyViewHolder(View view) {
            super(view);
            title = view.findViewById(R.id.titleView);
            type = view.findViewById(R.id.typeView);
        }
    }

    //Creates the adapter class using the ArrayList of events
    public EventAdapter(ArrayList<Event> events) {
        this.events = events;
    }

    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.event_list_row, parent, false);

        return new MyViewHolder(itemView);
    }

    //Binds individual rows to the RecyclerView on activity_main
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        MyViewHolder mvh = (MyViewHolder)viewHolder;
        Event temp = events.get(i);
        mvh.title.setText(temp.getInfo()); //set the data members here
        mvh.type.setText(temp.getType().getName());
    }

    public int getItemCount() {
        return events.size();
    }

    public void update(ArrayList<Event> events){
        this.events = events;
    }
}
