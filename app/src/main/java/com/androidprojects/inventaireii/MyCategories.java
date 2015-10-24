package com.androidprojects.inventaireii;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class MyCategories extends AppCompatActivity {



    private ArrayList<ObjectCategories> categoryList = new ArrayList<>();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_categories);


        //Fake data
        ObjectCategories games = new ObjectCategories("done", "Jeux", "1/1");
        ObjectCategories fishing = new ObjectCategories("doing", "Pêche", "6/8");
        ObjectCategories medical = new ObjectCategories("todo", "Médical", "0/36");
        ObjectCategories biology = new ObjectCategories("done", "Biologie", "11/11");

        // Adding objects to the list
        categoryList.add(games);
        categoryList.add(fishing);
        categoryList.add(medical);
        categoryList.add(biology);

        //Using adapter
        ArrayAdapter adapter = new CategoriesAdapter(this, categoryList);


        // Fill the ListView
        ListView lvCategories = (ListView) findViewById(R.id.lvCategories);
        lvCategories.setAdapter(adapter);

    }

    private int giveColor(String s) {
        if(s.equals("todo"))
            return getResources().getColor(R.color.indicator_to_do);
        if(s.equals("done"))
            return getResources().getColor(R.color.indicator_done);
        return getResources().getColor(R.color.indicator_doing);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_my_categories, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private class CategoriesAdapter extends ArrayAdapter {
        // Ref: https://github.com/codepath/android_guides/wiki/Using-an-ArrayAdapter-with-ListView


        public CategoriesAdapter(Context context,ArrayList <ObjectCategories>  categories) {
            super(context, 0, categories);

        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ObjectCategories category = (ObjectCategories) getItem(position);

            //Creation of the link with the categories_row layout if not already existing
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.categories_row, parent, false);
            }

            //Effective linking with the categories_row layout
            View tvSquare = convertView.findViewById(R.id.square);
            TextView tvName = (TextView) convertView.findViewById(R.id.name);
            TextView tvState = (TextView) convertView.findViewById(R.id.inventoryState);

            //Data to display are retrieved
            tvName.setText(category.name);
            tvSquare.setBackgroundColor(giveColor(category.color));
            tvState.setText(category.inventoryState);

            return convertView;
        }
    }




}
