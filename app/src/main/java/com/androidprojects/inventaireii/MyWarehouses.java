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

    final ArrayList<ObjectWarehouse> warehouseList = new ArrayList<>();
    PopupWindow popupWindow;
    Button addButton;

    public ArrayList<ObjectWarehouse> getWarehouseList() {
        return warehouseList;
    }


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_warehouses);
//int stockCapacity, String telNumber, String strasse, String strasseNumber, String npa, String ort, String land
        //Fake data (ObjectWarehouse used again because the structure of data needed is the same
        ObjectWarehouse freezer = new ObjectWarehouse("doing", "Frigo", 5, 39, 52, "0900 985 65 32", "Rue Veermeil", "22B", "32AE6", "Wistchick", "Inconnu");
        ObjectWarehouse eliteLib = new ObjectWarehouse("doing", "Bibliothèque Elite", 1, 3, 68, "024 656 98 76", "Rue des Tonneliers", "7", "1006", "Lausanne", "Suisse" );
        ObjectWarehouse polarisLib = new ObjectWarehouse("todo", "Bibliothèque Polaris", 0,2, 36, "041 156 98 76", "Rue des Camps", "4", "1265", "Terre-Pleine", "Suisse" );
        ObjectWarehouse cabinet = new ObjectWarehouse("done", "Armoire", 12, 12, 658, "056 874 98 12", "Strada  Egoisti", "70", "3814", "Stereo", "Italie" );

        // Adding objects to the list
        warehouseList.add(freezer);
        warehouseList.add(eliteLib);
        warehouseList.add(polarisLib);
        warehouseList.add(cabinet);


        //Using adapter
        final ArrayAdapter adapter = new WarehousesAdapter(this, warehouseList);


        // Fill the ListView
        ListView lvWarehouses = (ListView) findViewById(R.id.lvWarehouses);
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
        if(s.equals("todo"))
            return getResources().getColor(R.color.indicator_to_do);
        if(s.equals("done"))
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

    private class WarehousesAdapter extends ArrayAdapter {
        // Ref: https://github.com/codepath/android_guides/wiki/Using-an-ArrayAdapter-with-ListView


        public WarehousesAdapter(Context context,ArrayList <ObjectWarehouse>  warehouses) {
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
            tvSquare.setBackgroundColor(giveColor(warehouse.getColor()));
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
