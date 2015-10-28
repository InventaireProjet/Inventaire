package com.androidprojects.inventaireii;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.TextView;

import java.util.ArrayList;

public class Warehouse extends AppCompatActivity {

    ObjectsLists objectsLists = new ObjectsLists();
    Button btnModify;
    Button btnDelete;
    PopupWindow popupWindow;

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
        final String name = intent.getStringExtra("warehouseName");
        warehouseName.setText(name);

        ArrayList<ObjectWarehouse> warehouses = objectsLists.getWarehouseList();

        for (int i = 0; i <warehouses.size() ; i++) {

            if (warehouses.get(i).getName().equals(name)){

                final ObjectWarehouse warehouse = warehouses.get(i);

                //First part
                squareInventoryState.setBackgroundColor(giveColor(warehouse.getColor()));
                inventoryState.setText(warehouse.getInventoriedObjects() + "/" + warehouse.getNumberObjects());

                //Second part
                int freeSpace = warehouse.getStockCapacity()-warehouse.getNumberObjects();
                freeSpaceNumber.setText(freeSpace + " places");

                if (warehouse.getStockCapacity()!=0) {
                    int freeSpaceInPercent = freeSpace * 100 / warehouse.getStockCapacity();
                    freeSpacePercentage.setText(freeSpaceInPercent + "%");
                }


                //Third part
                capacityNumber.setText(warehouse.getStockCapacity() +" places");

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

                        Intent intent = new Intent(getBaseContext(), StockWarehouse.class);
                        intent.putExtra("warehouseName", name);
                        startActivity(intent);
                    }
                });


                btnModify = (Button) findViewById(R.id.buttonModify);
                btnModify.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getBaseContext(), WarehouseModify.class);
                        String warehouseName = warehouse.getName();
                        intent.putExtra("warehouseName", warehouseName);
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
                        Button buttonDeleteAll = (Button) popupView.findViewById(R.id.buttonDeleteAll);
                        Button buttonCancel = (Button) popupView.findViewById(R.id.buttonCancel);


                        //Deleting the warehouse
                        buttonDeleteWarehouse.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                ArrayList<ObjectWarehouse> warehouses = objectsLists.getWarehouseList();

                                for (int i = 0; i < warehouses.size(); i++) {

                                    if (warehouses.get(i).getName().equals(name)) {

                                        objectsLists.getWarehouseList().remove(i);
                                    }
                                }

                                popupWindow.dismiss();
                                Intent intent = new Intent(getBaseContext(), MyWarehouses.class);
                                startActivity(intent);
                            }
                        });

                        //Deleting the warehouse and all its stock
                        buttonDeleteAll.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                ArrayList<ObjectWarehouse> warehouses = objectsLists.getWarehouseList();

                                //TODO Deleting stock product in this warehouse (data management)


                                for (int i = 0; i < warehouses.size(); i++) {

                                    if (warehouses.get(i).getName().equals(name)) {

                                        objectsLists.getWarehouseList().remove(i);
                                    }
                                }

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