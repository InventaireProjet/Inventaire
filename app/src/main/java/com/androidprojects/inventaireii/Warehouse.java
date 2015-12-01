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
    Button viewStockBtn;
    PopupWindow popupWindow;
    WarehouseDataSource warehouseDataSource;
    ProductDataSource productDataSource;
    StockDataSource stockDataSource;
    ObjectWarehouse warehouse;
    List<ObjectProducts> productsInWarehouse;
    TextView warehouseName ;
    TextView inventoriedElements;
    View squareInventoryState;
    TextView inventoryState ;
    TextView freeSpaceTitle;
    TextView freeSpaceNumber ;
    TextView freeSpacePercentage;
    TextView storageCapacityTitle;
    TextView capacityNumber;
    TextView address;
    TextView phoneTitle;
    TextView phoneEntry;
    TextView addressTitle;
    TextView street ;
    TextView streetNo ;
    TextView postalCode ;
    TextView city ;
    TextView country ;
    int freeSpace;
    int freeSpaceInPercent;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_warehouse);

        productDataSource = ProductDataSource.getInstance(this);
        warehouseDataSource = WarehouseDataSource.getInstance(this);
        stockDataSource = StockDataSource.getInstance(this);

        //Layout elements
        warehouseName = (TextView) findViewById(R.id.warehouseName);
        squareInventoryState = findViewById(R.id.squareInventoryState);
        inventoryState = (TextView) findViewById(R.id.inventoryState);
        freeSpaceNumber = (TextView) findViewById(R.id.freeSpaceNumber);
        freeSpacePercentage = (TextView) findViewById(R.id.freeSpacePercentage);
        capacityNumber = (TextView) findViewById(R.id.capacityNumber);
        phoneEntry = (TextView) findViewById(R.id.phoneEntry);
        street = (TextView) findViewById(R.id.street);
        streetNo = (TextView) findViewById(R.id.streetNo);
        postalCode = (TextView) findViewById(R.id.postalCode);
        city = (TextView) findViewById(R.id.city);
        country = (TextView) findViewById(R.id.country);
        inventoriedElements = (TextView) findViewById(R.id.inventoriedElements);
        freeSpaceTitle = (TextView) findViewById(R.id.freeSpace);
        storageCapacityTitle =(TextView) findViewById(R.id.capacity);
        address = (TextView) findViewById(R.id.address);
        phoneTitle= (TextView) findViewById(R.id.phone);
        addressTitle =(TextView) findViewById(R.id.location);
        viewStockBtn = (Button) findViewById(R.id.viewStockBtn);
        btnModify = (Button) findViewById(R.id.buttonModify);
        btnDelete = (Button) findViewById(R.id.buttonDelete);


        //Intent retrieving
        Intent intent = getIntent();
        final int warehouseId = intent.getIntExtra("warehouseId", 1);


        warehouse = warehouseDataSource.getWarehouseById(warehouseId);

        warehouseName.setText(warehouse.getName());

        productsInWarehouse = productDataSource.getAllProductsByWarehouse(warehouseId);

        //First part
        warehouse.setInventoriedObjects(stockDataSource.getInventoriedObjects(warehouse.getId()));
        warehouse.setNumberObjects(Methods.warehouseStockQuantity(productsInWarehouse,warehouse));
        squareInventoryState.setBackgroundColor(Methods.giveColor(squareInventoryState, Methods.getInventoryState(productsInWarehouse)));
        inventoryState.setText(warehouse.getInventoriedObjects() + "/" + warehouse.getNumberObjects());

        //Second part
        freeSpace = warehouse.getStockCapacity() - warehouse.getNumberObjects();
        freeSpaceNumber.setText(freeSpace +" " +getString(R.string.places));

        if (warehouse.getStockCapacity() != 0) {
            freeSpaceInPercent = freeSpace * 100 / warehouse.getStockCapacity();
            freeSpacePercentage.setText(freeSpaceInPercent + "%");
        }


        //Third part
        capacityNumber.setText(warehouse.getStockCapacity() +" " +getString(R.string.places));

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
        getSupportActionBar().setTitle(R.string.warehouse);


        inventoriedElements.setText(R.string.inventoried_elements);
        freeSpaceTitle.setText(R.string.free_space);
        storageCapacityTitle.setText(R.string.warehouse_capacity);
        address.setText(R.string.warehouse_address);
        phoneTitle.setText(R.string.phone_short);
        addressTitle.setText(R.string.warehouse_address_colon);
        viewStockBtn.setText(R.string.view_stock_button);
        btnModify.setText(R.string.modify);
        btnDelete.setText(R.string.delete);


    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        invalidateOptionsMenu();
        return true;
    }
}