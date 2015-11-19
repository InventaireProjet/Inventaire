package com.androidprojects.inventaireii;


import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

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

        if(product.getStocks().isEmpty()) {
            return "done";
        }

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

        if (ObjectsLists.getProductList().isEmpty()){
            return "done";
        }

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

    public static String getInventoryState(List<ObjectProducts> productsToDisplay) {
        int nbControlled = 0;
        int nbNotControlled = 0;

        if (productsToDisplay.isEmpty()) {
            return "done";
        }

        for (ObjectProducts p : productsToDisplay){
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

    public static ArrayList getObjectsListbyWarehouse(String warehouse) {

        ArrayList <ObjectProducts> productsSelected = new ArrayList<ObjectProducts>();

        for (int i =0; i<ObjectsLists.getProductList().size();i++)
        {
            for (int j=0; j<ObjectsLists.getProductList().get(i).getStocks().size(); j++)
            {
                if (ObjectsLists.getProductList().get(i).getStocks().get(j).getWarehouse().getName().equals(warehouse))
                {
                    productsSelected.add(ObjectsLists.getProductList().get(i));
                }
            }
        }
        return  productsSelected;
    }

    // Method for management of the ActionBar
    public static Class onOptionsItemSelected(int id){

        switch (id) {
            case R.id.go_home:
                return MainActivity.class;

            case R.id.goto_products:
                return MyProducts.class;

            case R.id.goto_categories:
                return MyCategories.class;

            case R.id.goto_warehouses:
                return MyWarehouses.class;

            case R.id.action_search:
                return MyWarehouses.class;

        }
        return null;
    }


}
