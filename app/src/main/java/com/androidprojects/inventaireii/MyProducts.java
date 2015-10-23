package com.androidprojects.inventaireii;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MyProducts extends AppCompatActivity {
    // Données provisoires
    private List<String> list = new ArrayList<String>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_products);

        // Créer les données provisoires
        list.add("todo");
        list.add("1235-1");
        list.add("Sonotone Arfid");
        list.add("Médical");
        list.add("200");
        list.add("350 CHF");
        list.add("done");
        list.add("46454-9");
        list.add("Balle");
        list.add("Jeux");
        list.add("3500");
        list.add("22 CHF");


        // Fill the ListView : without ArrayAdapter, while the column
        // width is not allways the same and the first object
        // in each line is a square
        ListView lvProducts = (ListView) findViewById(R.id.lvProducts);

        int nbLines = 2;        // TODO change that !!!
        for (int i = 0; i < nbLines; i++) {
            LinearLayout ll = new LinearLayout(this);
            ll.setId(i);
            // color square
            View square = new View(this);
            square.setMinimumWidth(R.dimen.square_width);
            square.setMinimumHeight(R.dimen.square_height);
            square.setBackgroundColor(giveColor(list.get(i * 6))); //TODO
            ll.addView(square);

            // String fields
            for (int j = 1; j < 6; j++){
                TextView tv = new TextView(this);
                tv.setText(list.get(i*6+j));
                tv.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
                ll.addView(tv);
            }

        }



        /*ArrayAdapter adapter = new ArrayAdapter(this,
                android.R.layout.simple_list_item_1, list);
        lvProducts.setAdapter(adapter);*/



    }

    private int giveColor(String s) {
        if(s.equals("todo"))
            return getResources().getColor(R.color.indicator_to_do, getTheme());
        if(s.equals("done"))
            return getResources().getColor(R.color.indicator_done, getTheme());
        return getResources().getColor(R.color.indicator_doing, getTheme());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_my_products, menu);
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
}
