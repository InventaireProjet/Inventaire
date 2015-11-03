package com.androidprojects.inventaireii;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class NewWarehouse extends AppCompatActivity {

    Button btnCancel;
    Button btnValidate;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_warehouse);

        btnCancel = (Button) findViewById(R.id.buttonCancel);
        btnValidate = (Button) findViewById(R.id.buttonValidate);

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), MyWarehouses.class);
                startActivity(intent);
            }
        });


        final EditText warehouseName = (EditText) findViewById(R.id.warehouseName);
        final EditText warehouseCapacity = (EditText) findViewById(R.id.warehouseCapacity);
        final EditText warehousePhone = (EditText) findViewById(R.id.warehousePhone);
        final EditText warehouseStreet = (EditText) findViewById(R.id.warehouseStreet);
        final EditText warehouseStreetNo = (EditText) findViewById(R.id.warehouseStreetNo);
        final EditText warehousePostalCode = (EditText) findViewById(R.id.warehousePostalCode);
        final EditText warehouseLocation = (EditText) findViewById(R.id.warehouseLocation);
        final EditText warehouseCountry = (EditText) findViewById(R.id.warehouseCountry);

        btnValidate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//TODO Manage the case of no entry
                ObjectWarehouse newWarehouse = new ObjectWarehouse(
                        warehouseName.getText().toString(),
                        0, 0,
                        Integer.parseInt(warehouseCapacity.getText().toString()),
                        warehousePhone.getText().toString(),
                        warehouseStreet.getText().toString(),
                        warehouseStreetNo.getText().toString(),
                        warehousePostalCode.getText().toString(),
                        warehouseLocation.getText().toString(),
                        warehouseCountry.getText().toString());

                ObjectsLists.getWarehouseList().add(newWarehouse);
                Intent intent = new Intent(getBaseContext(), MyWarehouses.class);
                startActivity(intent);
            }
        });

    }
}