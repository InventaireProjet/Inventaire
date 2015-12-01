package com.androidprojects.inventaireii;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.androidprojects.inventaireii.Preferences.AppSettingsActivity;
import com.androidprojects.inventaireii.db.adapter.ProductDataSource;
import com.androidprojects.inventaireii.db.adapter.StockDataSource;
import com.androidprojects.inventaireii.db.adapter.WarehouseDataSource;

import java.util.ArrayList;
import java.util.List;

public class WarehouseModify extends AppCompatActivity {

    TextView warehouseName;
    EditText editWarehouseName;
    TextView txtPlaces ;
    TextView street ;
    TextView streetNo;
    TextView postalCode;
    TextView city ;
    TextView country;
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
    EditText editCapacityNumber;
    EditText editPhoneEntry ;
    EditText editStreet ;
    EditText editStreetNo;
    EditText editPostalCode;
    EditText editCity ;
    EditText editCountry;
    int freeSpace;
    int freeSpaceInPercent;
    Button viewStockBtn ;
    Button btnModify ;
    Button btnDelete ;
    Button btnCancel;
    Button btnValidate;
    ObjectWarehouse warehouse;
    WarehouseDataSource warehouseDataSource;
    ProductDataSource productDataSource;
    StockDataSource stockDataSource;
    List<ObjectProducts> productsInWarehouse;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_warehouse);

        productDataSource =  ProductDataSource.getInstance(this);
        warehouseDataSource =  WarehouseDataSource.getInstance(this);
        stockDataSource = StockDataSource.getInstance(this);

        //Layout elements
        warehouseName = (TextView) findViewById(R.id.warehouseName);
        editWarehouseName = (EditText) findViewById(R.id.editWarehouseName);
        squareInventoryState = findViewById(R.id.squareInventoryState);
        inventoryState = (TextView) findViewById(R.id.inventoryState);
        freeSpaceNumber = (TextView) findViewById(R.id.freeSpaceNumber);
        freeSpacePercentage = (TextView) findViewById(R.id.freeSpacePercentage);
        capacityNumber = (TextView) findViewById(R.id.capacityNumber);
        txtPlaces = (TextView) findViewById(R.id.txtPlaces);
        phoneEntry = (TextView) findViewById(R.id.phoneEntry);
        street = (TextView) findViewById(R.id.street);
        streetNo = (TextView) findViewById(R.id.streetNo);
        postalCode = (TextView) findViewById(R.id.postalCode);
        city = (TextView) findViewById(R.id.city);
        country = (TextView) findViewById(R.id.country);
        editCapacityNumber = (EditText) findViewById(R.id.editCapacityNumber);
        editPhoneEntry = (EditText) findViewById(R.id.editPhoneEntry);
        editStreet = (EditText) findViewById(R.id.editStreet);
        editStreetNo = (EditText) findViewById(R.id.editStreetNo);
        editPostalCode = (EditText) findViewById(R.id.editPostalCode);
        editCity = (EditText) findViewById(R.id.editCity);
        editCountry = (EditText) findViewById(R.id.editCountry);

        inventoriedElements = (TextView) findViewById(R.id.inventoriedElements);
        freeSpaceTitle = (TextView) findViewById(R.id.freeSpace);
        storageCapacityTitle =(TextView) findViewById(R.id.capacity);
        address = (TextView) findViewById(R.id.address);
        phoneTitle= (TextView) findViewById(R.id.phone);
        addressTitle =(TextView) findViewById(R.id.location);


        viewStockBtn = (Button) findViewById(R.id.viewStockBtn);
        btnModify = (Button) findViewById(R.id.buttonModify);
        btnDelete = (Button) findViewById(R.id.buttonDelete);
        btnCancel = (Button) findViewById(R.id.buttonCancel);
        btnValidate = (Button) findViewById(R.id.buttonValidate);

        //Changing button visibility
        btnModify.setVisibility(View.INVISIBLE);
        btnDelete.setVisibility(View.INVISIBLE);
        btnCancel.setVisibility(View.VISIBLE);
        btnValidate.setVisibility(View.VISIBLE);

        //Intent retrieving
        Intent intent = getIntent();
        final int warehouseId = intent.getIntExtra("warehouseId", 0);

        warehouse = warehouseDataSource.getWarehouseById(warehouseId);

        editWarehouseName.setText(warehouse.getName());

        //Changing element visibility, EditText replaces TextView
        warehouseName.setVisibility(View.INVISIBLE);
        editWarehouseName.setVisibility(View.VISIBLE);


        //Retrieving the products in the warehouse to know which color to display
        productsInWarehouse = productDataSource.getAllProductsByWarehouse(warehouseId);


        //First part
        warehouse.setInventoriedObjects(stockDataSource.getInventoriedObjects(warehouse.getId()));
        warehouse.setNumberObjects(Methods.warehouseStockQuantity(productsInWarehouse,warehouse));
        squareInventoryState.setBackgroundColor(Methods.giveColor(squareInventoryState, Methods.getInventoryState(productsInWarehouse)));
        inventoryState.setText(warehouse.getInventoriedObjects() + "/" + warehouse.getNumberObjects());

        //Second part
        freeSpace = warehouse.getStockCapacity() - warehouse.getNumberObjects();
        freeSpaceNumber.setText(freeSpace + " " + getString(R.string.places));

        if (warehouse.getStockCapacity() != 0) {
            freeSpaceInPercent = freeSpace * 100 / warehouse.getStockCapacity();
            freeSpacePercentage.setText(freeSpaceInPercent + "%");
        }


        //Third part
        //Visibility change
        capacityNumber.setVisibility(View.INVISIBLE);
        editCapacityNumber.setVisibility(View.VISIBLE);
        txtPlaces.setVisibility(View.VISIBLE);

        editCapacityNumber.setText(String.valueOf(warehouse.getStockCapacity()));

        //Fourth part
        //Visibility change
        phoneEntry.setVisibility(View.INVISIBLE);
        editPhoneEntry.setVisibility(View.VISIBLE);
        street.setVisibility(View.INVISIBLE);
        editStreet.setVisibility(View.VISIBLE);
        streetNo.setVisibility(View.INVISIBLE);
        editStreetNo.setVisibility(View.VISIBLE);
        postalCode.setVisibility(View.INVISIBLE);
        editPostalCode.setVisibility(View.VISIBLE);
        city.setVisibility(View.INVISIBLE);
        editCity.setVisibility(View.VISIBLE);
        country.setVisibility(View.INVISIBLE);
        editCountry.setVisibility(View.VISIBLE);


        editPhoneEntry.setText(warehouse.getTelNumber());
        editStreet.setText((warehouse.getStreet()));
        editStreetNo.setText(warehouse.getStreetNumber());
        editPostalCode.setText(warehouse.getPostalCode());
        editCity.setText(warehouse.getLocation());
        editCountry.setText((warehouse.getCountry()));

        viewStockBtn.setText(R.string.stock_modify);
        viewStockBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getBaseContext(), WarehouseStock.class);
                intent.putExtra("warehouseId", warehouseId);
                startActivity(intent);
            }
        });


        btnCancel = (Button) findViewById(R.id.buttonCancel);
        btnCancel.isActivated();
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), Warehouse.class);
                intent.putExtra("warehouseId", warehouseId);
                startActivity(intent);
            }
        });

        btnValidate = (Button) findViewById(R.id.buttonValidate);
        btnValidate.setOnClickListener(new Button.OnClickListener() {

            @Override
            public void onClick(View v) {

                if (editWarehouseName.getText().toString().equals("")){
                    Toast toast = android.widget.Toast.makeText(getBaseContext(), R.string.no_warehousename, android.widget.Toast.LENGTH_LONG);
                    toast.show();
                }

                else {
                    //Saving modifications
                    int warehouseCapacity;

                    if (editCapacityNumber.getText().toString().equals("")){
                        warehouseCapacity = 0;
                    }
                    else
                    {
                        warehouseCapacity=Integer.parseInt(editCapacityNumber.getText().toString());
                    }

                    warehouse.setName(editWarehouseName.getText().toString());
                    warehouse.setStockCapacity(warehouseCapacity);
                    warehouse.setTelNumber(editPhoneEntry.getText().toString());
                    warehouse.setStreet(editStreet.getText().toString());
                    warehouse.setStreetNumber(editStreetNo.getText().toString());
                    warehouse.setPostalCode(editPostalCode.getText().toString());
                    warehouse.setLocation(editCity.getText().toString());
                    warehouse.setCountry(editCountry.getText().toString());

                    warehouseDataSource.updateWarehouse(warehouse);

                    Intent intent = new Intent(getBaseContext(), Warehouse.class);
                    intent.putExtra("warehouseId", warehouseId);
                    startActivity(intent);
                }
            }

        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        //Refresh the language
        Methods.setLocale(this);
        getSupportActionBar().setTitle(R.string.warehouse_modification);

        inventoriedElements.setText(R.string.inventoried_elements);
        freeSpaceTitle.setText(R.string.free_space);
        storageCapacityTitle.setText(R.string.warehouse_capacity);
        address.setText(R.string.warehouse_address);
        phoneTitle.setText(R.string.phone_short);
        addressTitle.setText(R.string.warehouse_address_colon);
        viewStockBtn.setText(R.string.view_stock_button);
        btnCancel.setText(R.string.cancel);
        btnValidate.setText(R.string.validate);

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

            case android.R.id.home:
                finish();
                return true;

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


            case R.id.goto_warehouses:
                intent = new Intent(getBaseContext(), MyWarehouses.class);
                startActivity(intent);
                return true;

        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        invalidateOptionsMenu();
        return true;
    }

}
