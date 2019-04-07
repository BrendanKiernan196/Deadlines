package com.kiernan.deadlines;
import java.util.ArrayList;

public class Debug {

    public static void main(String[] args){
        EventBag eb = new EventBag();
        EventTypeBag etb = new EventTypeBag(eb);

        //These should be true
        boolean wombo = etb.add("Wombo");
        boolean why = etb.add("Why");
        boolean wah = etb.add("Wah");

        //These should be false (duplicate names)
        boolean wombo2 = etb.add(" Wombo");
        boolean wah2 = etb.add("wah");

        boolean why2 = etb.remove("Why");
        boolean why3 = etb.remove("Why");

        ArrayList<EventType> et = etb.getTypeList();

        //T T F F
        System.out.println(wombo + " " + wah + " " + wombo2 + " " + wah2);

        //T T F
        System.out.println(why + " " + why2 + " " + why3);

        //Wombo and Wah
        for(int i = 0; i < etb.getListSize(); i++){
            System.out.println(et.get(i).getName());
        }

        System.out.println();

        boolean a1 = false, a2 = false, a3 = false, a4 = false;

        if(etb.getType(0) != null){
            a1 = true;
        }

        if(etb.getType(3) != null){
            a2 = true;
        }

        if(etb.getType("Wah") != null){
            a3 = true;
        }

        if(etb.getType("Why") != null){
            a4 = true;
        }

        //T F T F
        System.out.println(a1 + " " + a2 + " " + a3 + " " + a4);

        //These should be added
        eb.add("Code shit", et.get(2), 5, 4, 3, 2,10);
        eb.add("Study for tests", et.get(1),5, 4, 3, 2, 0);
        eb.add("Reclaim the Holy Lands", et.get(1), 5, 4, 3, 2,1 );

        //These shouldn't
        eb.add("cOdE sHiT", et.get(0), 2019, 4, 7, 14, 47);
        eb.add("   Study for tests   ", et.get(0), 2019, 4, 7, 16, 0);

        ArrayList<Event> el = eb.getList();

        for(int i = 0; i < eb.getListSize(); i++){
            System.out.println(el.get(i).getInfo());
        }

        System.out.println();

        //Code shit should print with an unclassified typing, wah should not print from etb
        etb.remove("Wah");

        et = etb.getTypeList();
        el = eb.getList();

        for(int i = 0; i < eb.getListSize(); i++){
            System.out.println(el.get(i).getInfo());
        }

        System.out.println();

        for(int i = 0; i < etb.getListSize(); i++){
            System.out.println(et.get(i).getName());
        }

    }

}
