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

    //Todo reprendre xml de l'autre, doubler les text en edit, switcher l'affichage, récupérer les noms, ...

    ObjectsLists objectsLists = new ObjectsLists();
    Button btnCancel;
    Button btnValidate;
    PopupWindow popupWindow;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_warehouse);

        //Layout elements
        TextView warehouseName = (TextView) findViewById(R.id.warehouseName);
        EditText editWarehouseName = (EditText) findViewById(R.id.editWarehouseName);
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
        EditText editCapacityNumber = (EditText) findViewById(R.id.editCapacityNumber);
        EditText editPhoneEntry = (EditText) findViewById(R.id.editPhoneEntry);
        EditText editStreet = (EditText) findViewById(R.id.editStreet);
        EditText editStreetNo = (EditText) findViewById(R.id.editStreetNo);
        EditText editPostalCode = (EditText) findViewById(R.id.editPostalCode);
        EditText editCity = (EditText) findViewById(R.id.editCity);
        EditText editCountry = (EditText) findViewById(R.id.editCountry);

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

        ArrayList<ObjectWarehouse> warehouses = objectsLists.getWarehouseList();

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

                editCapacityNumber.setText(warehouse.getStockCapacity() + " places");

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

                        Intent intent = new Intent(getBaseContext(), StockWarehouse.class);
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
                        String warehouseName = warehouse.getName();
                        intent.putExtra("warehouseName", warehouseName);
                        startActivity(intent);
                    }
                });

                btnValidate = (Button) findViewById(R.id.buttonValidate);
                btnValidate.setOnClickListener(new Button.OnClickListener() {

                    @Override
                    public void onClick(View v) {

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
