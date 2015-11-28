package com.androidprojects.inventaireii;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.androidprojects.inventaireii.db.adapter.ProductDataSource;
import com.androidprojects.inventaireii.db.adapter.StockDataSource;
import com.androidprojects.inventaireii.db.adapter.WarehouseDataSource;
import com.androidprojects.inventaireii.Preferences.AppSettingsActivity;


import java.util.List;

public class Warehouse extends AppCompatActivity {

    Button btnModify;
    Button btnDelete;
    PopupWindow popupWindow;
    WarehouseDataSource warehouseDataSource;
    ProductDataSource productDataSource;
    StockDataSource stockDataSource;
    ObjectWarehouse warehouse;
    List<ObjectProducts> productsInWarehouse;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Language management
        Methods.setLocale(this);

        setContentView(R.layout.activity_warehouse);

        productDataSource = ProductDataSource.getInstance(this);
        warehouseDataSource = WarehouseDataSource.getInstance(this);
        stockDataSource = StockDataSource.getInstance(this);

        //Layout elements
        TextView warehouseName = (TextView) findViewById(R.id.warehouseName);
        View squareInventoryState = findViewById(R.id.squareInventoryState);
        TextView inventoryState = (TextView) findViewById(R.id.inventoryState);
        TextView freeSpaceNumber = (TextView) findViewById(R.id.freeSpaceNumber);
        TextView freeSpacePercentage = (TextView) findViewById(R.id.freeSpacePercentage);
        TextView capacityNumber = (TextView) findViewById(R.id.capacityNumber);
        TextView phoneEntry = (TextView) findViewById(R.id.phoneEntry);
        TextView street = (TextView) findViewById(R.id.street);
        TextView streetNo = (TextView) findViewById(R.id.streetNo);
        TextView postalCode = (TextView) findViewById(R.id.postalCode);
        TextView city = (TextView) findViewById(R.id.city);
        TextView country = (TextView) findViewById(R.id.country);

        Button viewStockBtn = (Button) findViewById(R.id.viewStockBtn);
        btnModify = (Button) findViewById(R.id.buttonModify);
        btnDelete = (Button) findViewById(R.id.buttonDelete);


        //Intent retrieving
        Intent intent = getIntent();
        final int warehouseId = intent.getIntExtra("warehouseId", 1);


        warehouse = warehouseDataSource.getWarehouseById(warehouseId);

        warehouseName.setText(warehouse.getName());

        productsInWarehouse = productDataSource.getAllProductsByWarehouse(warehouseId);

        //First part;
        //todo : more simple
        warehouse.setInventoriedObjects(stockDataSource.getInventoriedObjects(warehouse.getId()));
        warehouse.setNumberObjects(stockDataSource.getNumberObjects(warehouse.getId()));
        squareInventoryState.setBackgroundColor(Methods.giveColor(squareInventoryState, Methods.getInventoryState(productsInWarehouse)));
        inventoryState.setText(warehouse.getInventoriedObjects() + "/" + warehouse.getNumberObjects());

        //Second part
        int freeSpace = warehouse.getStockCapacity() - warehouse.getNumberObjects();
        freeSpaceNumber.setText(freeSpace + " places");

        if (warehouse.getStockCapacity() != 0) {
            int freeSpaceInPercent = freeSpace * 100 / warehouse.getStockCapacity();
            freeSpacePercentage.setText(freeSpaceInPercent + "%");
        }


        //Third part
        capacityNumber.setText(warehouse.getStockCapacity() + " places");

        //Fourth part
        phoneEntry.setText(warehouse.getTelNumber());
        street.setText((warehouse.getStreet()));
        streetNo.setText(warehouse.getStreetNumber());
        postalCode.setText(warehouse.getPostalCode());
        city.setText(warehouse.getLocation());
        country.setText((warehouse.getCountry()));


        viewStockBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getBaseContext(), WarehouseStock.class);
                intent.putExtra("warehouseId", warehouseId);
                startActivity(intent);
            }
        });


        btnModify = (Button) findViewById(R.id.buttonModify);
        btnModify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), WarehouseModify.class);
                intent.putExtra("warehouseId", warehouseId);
                startActivity(intent);
            }
        });

        btnDelete = (Button) findViewById(R.id.buttonDelete);
        btnDelete.setOnClickListener(new Button.OnClickListener() {

            @Override
            public void onClick(View v) {
                LayoutInflater layoutInflater =
                        (LayoutInflater) getBaseContext().getSystemService(LAYOUT_INFLATER_SERVICE);

                View popupView = layoutInflater.inflate(R.layout.delete_warehouse_popup, null);
                popupWindow = new PopupWindow(popupView, ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT);


                // Catch the elements of the pop-up view
                Button buttonDeleteWarehouse = (Button) popupView.findViewById(R.id.buttonDeleteWarehouse);
                Button buttonCancel = (Button) popupView.findViewById(R.id.buttonCancel);


                //Deleting  the warehouse and the associated stocks
                buttonDeleteWarehouse.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        warehouseDataSource.deleteWarehouseAndProducts(warehouseId);

                        popupWindow.dismiss();
                        Intent intent = new Intent(getBaseContext(), MyWarehouses.class);
                        startActivity(intent);
                    }
                });


                buttonCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        popupWindow.dismiss();
                    }
                });

                popupWindow.showAsDropDown(buttonDeleteWarehouse, 0, -100);

            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_action_bar, menu);
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
                return true;

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


            case R.id.goto_warehouses:
                intent = new Intent(getBaseContext(), MyWarehouses.class);
                startActivity(intent);
                return true;


        }

        return super.onOptionsItemSelected(item);
    }

    protected void onResume() {
        super.onResume();

        //Language management
        Methods.setLocale(this);
    }
}