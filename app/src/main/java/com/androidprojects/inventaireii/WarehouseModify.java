package com.androidprojects.inventaireii;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.androidprojects.inventaireii.db.adapter.ProductDataSource;
import com.androidprojects.inventaireii.db.adapter.WarehouseDataSource;

import java.util.ArrayList;
import java.util.List;

public class WarehouseModify extends AppCompatActivity {

    Button btnCancel;
    Button btnValidate;
    ObjectWarehouse warehouse;
    WarehouseDataSource warehouseDataSource;
    ProductDataSource productDataSource;
    List<ObjectProducts> productsInWarehouse;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_warehouse);

        productDataSource =  ProductDataSource.getInstance(this);
        warehouseDataSource =  WarehouseDataSource.getInstance(this);

        //Layout elements
        TextView warehouseName = (TextView) findViewById(R.id.warehouseName);
        final EditText editWarehouseName = (EditText) findViewById(R.id.editWarehouseName);
        View squareInventoryState = findViewById(R.id.squareInventoryState);
        TextView inventoryState = (TextView) findViewById(R.id.inventoryState);
        TextView freeSpaceNumber = (TextView) findViewById(R.id.freeSpaceNumber);
        TextView freeSpacePercentage = (TextView) findViewById(R.id.freeSpacePercentage);
        TextView capacityNumber = (TextView) findViewById(R.id.capacityNumber);
        TextView txtPlaces = (TextView) findViewById(R.id.txtPlaces);
        TextView phoneEntry = (TextView) findViewById(R.id.phoneEntry);
        TextView street = (TextView) findViewById(R.id.street);
        TextView streetNo = (TextView) findViewById(R.id.streetNo);
        TextView postalCode = (TextView) findViewById(R.id.postalCode);
        TextView city = (TextView) findViewById(R.id.city);
        TextView country = (TextView) findViewById(R.id.country);
        final EditText editCapacityNumber = (EditText) findViewById(R.id.editCapacityNumber);
        final EditText editPhoneEntry = (EditText) findViewById(R.id.editPhoneEntry);
        final EditText editStreet = (EditText) findViewById(R.id.editStreet);
        final EditText editStreetNo = (EditText) findViewById(R.id.editStreetNo);
        final EditText editPostalCode = (EditText) findViewById(R.id.editPostalCode);
        final EditText editCity = (EditText) findViewById(R.id.editCity);
        final EditText editCountry = (EditText) findViewById(R.id.editCountry);

        Button viewStockBtn = (Button) findViewById(R.id.viewStockBtn);
        Button btnModify = (Button) findViewById(R.id.buttonModify);
        Button btnDelete = (Button) findViewById(R.id.buttonDelete);
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



        //First part
        //Retrieving the products in the warehouse to know which color to display
        productsInWarehouse = productDataSource.getAllProductsByWarehouse(warehouseId);


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
}
