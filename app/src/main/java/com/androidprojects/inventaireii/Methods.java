package com.androidprojects.inventaireii;


import android.support.v7.app.AppCompatActivity;
import android.view.View;

import java.util.ArrayList;

public class Methods extends AppCompatActivity {


    public static int giveColor(View cl,  String s) {
        if(s.equals("todo"))
            return cl.getResources().getColor(R.color.indicator_to_do);
        if(s.equals("done"))
            return cl.getResources().getColor(R.color.indicator_done);
        return cl.getResources().getColor(R.color.indicator_doing);
    }

    public static String getInventoryState(ObjectProducts product) {
        int nbControlled = 0;
        int nbNotControlled = 0;
        for (ObjectStock s : product.getStocks()) {
            if(s.isControlled())
                nbControlled ++;
            else
                nbNotControlled ++;
        }
        if (nbNotControlled == 0)
            return "done";

        if(nbControlled == 0)
            return "todo";

        return "doing";
    }

    public static String getInventoryState() {
        int nbControlled = 0;
        int nbNotControlled = 0;
        for (ObjectProducts p : ObjectsLists.getProductList()){
            switch (getInventoryState(p)) {
                case "done": nbControlled++ ; break ;
                case "todo": nbNotControlled++ ; break;
            }
        }

        if (nbControlled == 0)
            return "todo";

        if (nbNotControlled == 0)
            return "done";

        return "doing";
    }


    public static ArrayList getObjectsListbyCategory(String category) {

        ArrayList <ObjectProducts> productsSelected = new ArrayList<ObjectProducts>();
        for (int i =0; i<ObjectsLists.getProductList().size();i++)
        {
            if (ObjectsLists.getProductList().get(i).getCategory().getName().equals(category))
            {
                productsSelected.add(ObjectsLists.getProductList().get(i));
            }
        }
        return  productsSelected;
    }


}
