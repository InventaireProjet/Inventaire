package com.androidprojects.inventaireii;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import java.util.ArrayList;

public class MyWarehouses extends AppCompatActivity {

   
    PopupWindow popupWindow;
    Button addButton;
     ArrayAdapter adapter;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //activity_my_categories reused because of same structure
        setContentView(R.layout.activity_my_categories);
        TextView title = (TextView) findViewById(R.id.txtTitle);
        title.setText("Mes magasins");

               //Using adapter
         adapter = new WarehousesAdapter(this, ObjectsLists.getWarehouseList());


        // Fill the ListView
        ListView lvWarehouses = (ListView) findViewById(R.id.lvCategories);
        lvWarehouses.setAdapter(adapter);


        //Adding Warehouses
        addButton = (Button) findViewById(R.id.btnAdd);
        addButton.setOnClickListener(new Button.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), NewWarehouse.class);
                startActivity(intent);
            }
        });
    }

    private int giveColor(String s) {
        if (s.equals("todo"))
            return getResources().getColor(R.color.indicator_to_do);
        if (s.equals("done"))
            return getResources().getColor(R.color.indicator_done);
        return getResources().getColor(R.color.indicator_doing);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_my_warehouses, menu);
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

    @Override
    protected void onResume() {
        super.onResume();
        adapter.notifyDataSetChanged();
    }

    private class WarehousesAdapter extends ArrayAdapter {
        // Ref: https://github.com/codepath/android_guides/wiki/Using-an-ArrayAdapter-with-ListView


        public WarehousesAdapter(Context context, ArrayList<ObjectWarehouse> warehouses) {
            super(context, 0, warehouses);

        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final ObjectWarehouse warehouse = (ObjectWarehouse) getItem(position);

            //Creation of the link with the categories_row (not useful to create a warehouses_row layout) layout if not already existing
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.categories_row, parent, false);
            }

            //Effective linking with the categories_row layout
            View tvSquare = convertView.findViewById(R.id.square);
            TextView tvName = (TextView) convertView.findViewById(R.id.name);
            TextView tvState = (TextView) convertView.findViewById(R.id.inventoryState);



            //Data to display are retrieved
            tvName.setText(warehouse.getName());
            //Retrieving the products in the warehouse to know which color to display
            ArrayList <ObjectProducts> productsInWarehouse =  Methods.getObjectsListbyWarehouse(warehouse.getName());
            tvSquare.setBackgroundColor(Methods.giveColor(tvSquare, Methods.getInventoryState(productsInWarehouse)));
            tvState.setText(warehouse.getInventoriedObjects() + "/" + warehouse.getNumberObjects());

            //Sending the warehouse name to the next screen

            tvName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getBaseContext(), Warehouse.class);
                    String warehouseName = warehouse.getName();
                    intent.putExtra("warehouseName", warehouseName);
                    startActivity(intent);
                }
            });

            return convertView;
        }
    }
}