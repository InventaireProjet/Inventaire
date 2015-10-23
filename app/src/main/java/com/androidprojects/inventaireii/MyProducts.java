package com.androidprojects.inventaireii;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
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
    private List<String> squareList = new ArrayList<String>(),
            artNbList = new ArrayList<String>(),
            nameList = new ArrayList<String>(),
            categoryList = new ArrayList<String>(),
            quantityList = new ArrayList<String>(),
            priceList = new ArrayList<String>() ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_products);

        // Créer les données provisoires
        squareList.add("todo");
        artNbList.add("1235-1");
        nameList.add("Sonotone Arfid");
        categoryList.add("Médical");
        quantityList.add("200");
        priceList.add("350 CHF");
        squareList.add("done");
        artNbList.add("46454-9");
        nameList.add("Balle");
        categoryList.add("Jeux");
        quantityList.add("3500");
        priceList.add("22 CHF");

        // Fill the ListView
        ListView lvProducts = (ListView) findViewById(R.id.lvProducts);
        lvProducts.setAdapter(new ProductsAdapter());

        /*
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

        */

        /*ArrayAdapter adapter = new ArrayAdapter(this,
                android.R.layout.simple_list_item_1, list);
        lvProducts.setAdapter(adapter);*/



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

    private class ProductsAdapter extends ArrayAdapter {
        // Ref: http://stackoverflow.com/questions/11281952/listview-with-customized-row-layout-android
        public ProductsAdapter() {
            super(MyProducts.this, R.layout.product_row);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            ViewHolder holder = null;
            LayoutInflater inflater = getLayoutInflater();
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.product_row, null, false);
                holder = new ViewHolder(convertView);
                convertView.setTag(holder);
            }
            else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.getSquare().setBackgroundColor(giveColor(squareList.get(position)));
            holder.getNo_art().setText(artNbList.get(position));
            holder.getName().setText(nameList.get(position));
            holder.getCategory().setText("fake"); // categoryList.get(position)
            holder.getQuantity().setText(quantityList.get(position));
            holder.getPrice().setText(priceList.get(position));
            return convertView;
        }
    }

    public class ViewHolder {
        private View row;
        private View square;
        private TextView no_art, name, category, quantity, price;

        public ViewHolder(View row) {
            this.row = row;
        }

        public View getSquare() {
            if (this.square == null) {
                this.square = (View) row.findViewById(R.id.square);
            }
            return this.square;
        }

        public TextView getNo_art() {
            if (this.no_art == null) {
                this.no_art = (TextView) row.findViewById(R.id.no_art);
            }
            return this.no_art;
        }

        public TextView getName() {
            if (this.name == null) {
                this.name = (TextView) row.findViewById(R.id.name);
            }
            return this.name;
        }

        public TextView getCategory() {
            if (this.category == null) {
                this.category = (TextView) row.findViewById(R.id.category);
            }
            return this.category;
        }

        public TextView getQuantity() {
            if (this.quantity == null) {
                this.quantity = (TextView) row.findViewById(R.id.quantity);
            }
            return this.quantity;
        }

        public TextView getPrice() {
            if (this.price == null) {
                this.price = (TextView) row.findViewById(R.id.price);
            }
            return this.price;
        }
    }
}
