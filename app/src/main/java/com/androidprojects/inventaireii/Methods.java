package com.androidprojects.inventaireii;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.androidprojects.inventaireii.Preferences.AppSettingsActivity;
import com.androidprojects.inventaireii.db.adapter.ProductDataSource;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Methods extends AppCompatActivity {

    // Give the color for an inventory state
    public static int giveColor(View cl,  String s) {
        if(s.equals("todo"))
            return cl.getResources().getColor(R.color.indicator_to_do);
        if(s.equals("done"))
            return cl.getResources().getColor(R.color.indicator_done);
        return cl.getResources().getColor(R.color.indicator_doing);
    }

    // Get the inventory state (done, to do or doing) of one product
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

    // Get the inventory state (done, to do, or doing) of a list of products
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

            case R.id.action_settings:
                return  AppSettingsActivity.class;


        }
        return null;
    }

    // Method to set the language
    public static void setLocale(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        String lang = sharedPreferences.getString("pref_language", "en");
        Locale myLocale = new Locale(lang);
        Locale.setDefault(myLocale);
        android.content.res.Configuration config = new android.content.res.Configuration();
        config.locale = myLocale;
        context.getResources().updateConfiguration(config, context.getResources().getDisplayMetrics());
    }

}
