package com.androidprojects.inventaireii;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

public class Warehouse extends AppCompatActivity {

    ObjectsLists objectsLists = new ObjectsLists();
    Button btnModify;
    Button btnDelete;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_warehouse);

        //Layout elements
        TextView warehouseName = (TextView) findViewById(R.id.warehouseName);
        View squareInventoryState = (View) findViewById(R.id.squareInventoryState);
        TextView inventoryState = (TextView) findViewById(R.id.inventoryState);
        //TODO ?
        View squareFreeSpace = (View) findViewById(R.id.squareFreeSpace);
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
        String name = intent.getStringExtra("warehouseName");
        warehouseName.setText(name);

        ArrayList<ObjectWarehouse> warehouses = objectsLists.getWarehouseList();

        for (int i = 0; i <warehouses.size() ; i++) {

            if (warehouses.get(i).getName().equals(name)){

                ObjectWarehouse warehouse = warehouses.get(i);

                //First part
                squareInventoryState.setBackgroundColor(giveColor(warehouse.getColor()));
                inventoryState.setText(warehouse.getInventoriedObjects() +"/" +warehouse.getNumberObjects());

                //Second part
                int freeSpace = warehouse.getStockCapacity()-warehouse.getNumberObjects();
                int freeSpaceInPercent = freeSpace*100/warehouse.getStockCapacity();
                freeSpaceNumber.setText(freeSpace +" places");
               freeSpacePercentage.setText(freeSpaceInPercent +"%");

                //Third part
               capacityNumber.setText(warehouse.getStockCapacity() +" places");

                //Fourth part
                phoneEntry.setText(warehouse.getTelNumber());
                street.setText((warehouse.getStreet()));
                streetNo.setText(warehouse.getStreetNumber());
                postalCode.setText(warehouse.getPostalCode());
                city.setText(warehouse.getLocation());
                country.setText((warehouse.getCountry()));


            }
        }




    }

    private int giveColor(String s) {
        if(s.equals("todo"))
            return getResources().getColor(R.color.indicator_to_do);
        if(s.equals("done"))
            return getResources().getColor(R.color.indicator_done);
        return getResources().getColor(R.color.indicator_doing);
    }
}