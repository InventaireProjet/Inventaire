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

import com.androidprojects.inventaireii.db.adapter.ProductDataSource;
import com.androidprojects.inventaireii.db.adapter.StockDataSource;
import com.androidprojects.inventaireii.db.adapter.WarehouseDataSource;
import com.androidprojects.inventaireii.preferences.AppSettingsActivity;

import java.util.ArrayList;
import java.util.List;

public class MyWarehouses extends AppCompatActivity {


    PopupWindow popupWindow;
    Button addButton;
    ArrayAdapter adapter;
    WarehouseDataSource warehouseDataSource;
    ProductDataSource productDataSource;
    StockDataSource stockDataSource;
    List <ObjectProducts> productsInWarehouse;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        warehouseDataSource = WarehouseDataSource.getInstance(this);
        productDataSource =  ProductDataSource.getInstance(this);
        stockDataSource = StockDataSource.getInstance(this);

        //activity_my_categories reused because of same structure
        setContentView(R.layout.activity_my_categories);
        TextView title = (TextView) findViewById(R.id.txtTitle);
        title.setText(R.string.my_warehouses);

        //Using adapter
        //adapter = new WarehousesAdapter(this, ObjectsLists.getWarehouseList());
        //TODO ERASE UP, ENABLE DOWN
        adapter = new WarehousesAdapter(this, warehouseDataSource.getAllWarehouses());

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


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_action_bar, menu);
        menu.findItem(R.id.goto_warehouses).setVisible(false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        int id = item.getItemId();

        Intent intent;

        switch (id) {

            case R.id.action_settings:
                intent = new Intent(this, AppSettingsActivity.class);
                startActivity(intent);
                return  true;

            case R.id.go_home:
                intent = new Intent(getBaseContext(), MainActivity.class);
                startActivity(intent);
                return true;

            case R.id.goto_products:
                intent = new Intent(getBaseContext(), MyProducts.class);
                startActivity(intent);
                return true;

            case R.id.goto_categories:
                intent = new Intent(getBaseContext(), MyCategories.class);
                startActivity(intent);
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


        public WarehousesAdapter(Context context, List<ObjectWarehouse> warehouses) {
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
           // productsInWarehouse =  Methods.getObjectsListbyWarehouse(warehouse.getName());
            //TODO UNDO UP, DO DOWN
            productsInWarehouse = productDataSource.getAllProductsByWarehouse(warehouse.getId());


            tvSquare.setBackgroundColor(Methods.giveColor(tvSquare, Methods.getInventoryState(productsInWarehouse)));

            //todo more simple:
            warehouse.setInventoriedObjects(stockDataSource.getInventoriedObjects(warehouse.getId()));
            warehouse.setNumberObjects(stockDataSource.getNumberObjects(warehouse.getId()));
            tvState.setText(warehouse.getInventoriedObjects() + "/" + warehouse.getNumberObjects());

            //Sending the warehouse name to the next screen

            tvName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getBaseContext(), Warehouse.class);
                    int warehouseId = warehouse.getId();
                    intent.putExtra("warehouseId", warehouseId);
                    startActivity(intent);
                }
            });

            return convertView;
        }
    }
}