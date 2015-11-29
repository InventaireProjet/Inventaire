package com.androidprojects.inventaireii;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.androidprojects.inventaireii.Preferences.AppSettingsActivity;
import com.androidprojects.inventaireii.db.adapter.WarehouseDataSource;

public class NewWarehouse extends AppCompatActivity {

    Button btnCancel;
    Button btnValidate;
    WarehouseDataSource warehouseDataSource;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    protected void onResume() {
        super.onResume();


        //Refresh the language
        Methods.setLocale(this);
        getSupportActionBar().setTitle(R.string.title_activity_new_warehouse);

        warehouseDataSource = WarehouseDataSource.getInstance(this);

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


        final EditText etWarehouseName = (EditText) findViewById(R.id.warehouseName);
        final EditText etWarehouseCapacity = (EditText) findViewById(R.id.warehouseCapacity);
        final EditText etWarehousePhone = (EditText) findViewById(R.id.warehousePhone);
        final EditText etWarehouseStreet = (EditText) findViewById(R.id.warehouseStreet);
        final EditText etWarehouseStreetNo = (EditText) findViewById(R.id.warehouseStreetNo);
        final EditText etWarehousePostalCode = (EditText) findViewById(R.id.warehousePostalCode);
        final EditText etWarehouseLocation = (EditText) findViewById(R.id.warehouseLocation);
        final EditText etWarehouseCountry = (EditText) findViewById(R.id.warehouseCountry);

        btnValidate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (etWarehouseName.getText().toString().equals("")) {

                    Toast toast = Toast.makeText(getBaseContext(), R.string.no_warehousename, Toast.LENGTH_LONG);
                    toast.show();
                }

                else {
                    int warehouseCapacity;

                    if (etWarehouseCapacity.getText().toString().equals("")){
                        warehouseCapacity = 0;
                    }
                    else
                    {
                        warehouseCapacity=Integer.parseInt(etWarehouseCapacity.getText().toString());
                    }

                    ObjectWarehouse newWarehouse = new ObjectWarehouse(
                            etWarehouseName.getText().toString(),
                            0, 0,
                            warehouseCapacity,
                            etWarehousePhone.getText().toString(),
                            etWarehouseStreet.getText().toString(),
                            etWarehouseStreetNo.getText().toString(),
                            etWarehousePostalCode.getText().toString(),
                            etWarehouseLocation.getText().toString(),
                            etWarehouseCountry.getText().toString());


                    warehouseDataSource.createWarehouse(newWarehouse);

                    Intent intent = new Intent(getBaseContext(), MyWarehouses.class);
                    startActivity(intent);
                }
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