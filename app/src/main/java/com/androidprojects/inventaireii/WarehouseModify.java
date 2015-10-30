package com.androidprojects.inventaireii;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.TextView;

import java.util.ArrayList;

public class WarehouseModify extends AppCompatActivity {

    Button btnCancel;
    Button btnValidate;
    PopupWindow popupWindow;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_warehouse);

        //Layout elements
        TextView warehouseName = (TextView) findViewById(R.id.warehouseName);
        final EditText editWarehouseName = (EditText) findViewById(R.id.editWarehouseName);
        View squareInventoryState = (View) findViewById(R.id.squareInventoryState);
        TextView inventoryState = (TextView) findViewById(R.id.inventoryState);
        //TODO ?
        View squareFreeSpace = (View) findViewById(R.id.squareFreeSpace);
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
        final String name = intent.getStringExtra("warehouseName");
        editWarehouseName.setText(name);

        //Changing element visibility, EditText replaces TextView
        warehouseName.setVisibility(View.INVISIBLE);
        editWarehouseName.setVisibility(View.VISIBLE);

        ArrayList<ObjectWarehouse> warehouses = ObjectsLists.getWarehouseList();

        for (int i = 0; i <warehouses.size() ; i++) {

            if (warehouses.get(i).getName().equals(name)) {

                final ObjectWarehouse warehouse = warehouses.get(i);

                //First part
                squareInventoryState.setBackgroundColor(giveColor(warehouse.getColor()));
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

                viewStockBtn.setText("Modifier le stock");
                viewStockBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent intent = new Intent(getBaseContext(), MyProducts.class);
                        intent.putExtra("warehouseName", name);
                        startActivity(intent);
                    }
                });


                btnCancel = (Button) findViewById(R.id.buttonCancel);
                btnCancel.isActivated();
                btnCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getBaseContext(), Warehouse.class);
                        intent.putExtra("warehouseName", name);
                        startActivity(intent);
                    }
                });

                btnValidate = (Button) findViewById(R.id.buttonValidate);
                btnValidate.setOnClickListener(new Button.OnClickListener() {

                    @Override
                    public void onClick(View v) {

                        //Saving modifications
                        warehouse.setName(editWarehouseName.getText().toString());
                        warehouse.setStockCapacity(Integer.parseInt(editCapacityNumber.getText().toString()));
                        warehouse.setTelNumber(editPhoneEntry.getText().toString());
                        warehouse.setStreet(editStreet.getText().toString());
                        warehouse.setStreetNumber(editStreetNo.getText().toString());
                        warehouse.setPostalCode(editPostalCode.getText().toString());
                        warehouse.setLocation(editCity.getText().toString());
                        warehouse.setCountry(editCountry.getText().toString());


                        Intent intent = new Intent(getBaseContext(), Warehouse.class);
                        intent.putExtra("warehouseName", name);
                        startActivity(intent);

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
