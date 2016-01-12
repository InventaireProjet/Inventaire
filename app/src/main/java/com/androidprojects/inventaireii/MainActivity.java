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
import android.widget.Toast;

import com.androidprojects.inventaireii.db.adapter.CategoryDataSource;
import com.androidprojects.inventaireii.db.adapter.ChangeDataSource;
import com.androidprojects.inventaireii.db.adapter.ProductDataSource;
import com.androidprojects.inventaireii.db.adapter.StockDataSource;
import com.androidprojects.inventaireii.Preferences.AppSettingsActivity;
import com.androidprojects.inventaireii.db.adapter.WarehouseDataSource;
import com.example.myapplication.backend.objectCategoriesApi.model.ObjectCategories;
import com.example.myapplication.backend.objectProductsApi.model.ObjectProducts;
import com.example.myapplication.backend.objectWarehouseApi.model.ObjectWarehouse;



import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {


    // Declaration of variables
    int nbItems = 0;
    int nbInventoriedItems = 0;
    StockDataSource stockDataSource;
    ProductDataSource productDataSource;
    CategoryDataSource categoryDataSource;
    WarehouseDataSource warehouseDataSource;
    ChangeDataSource changeDataSource;
    boolean popupWindowIsOn;

    // Declaration of views
    TextView txtTitle;
    TextView txtInventoryRunning;
    TextView txtInventoryState;
    TextView txtProductAccess;
    TextView txtCategoryAccess;
    TextView txtWarehouseAccess;
    PopupWindow popupWindow;
    Button buttonStartInventory;
    Button buttonSynchronize;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Methods.setLocale(this);
        setContentView(R.layout.activity_main);

        stockDataSource = StockDataSource.getInstance(this);
        productDataSource = ProductDataSource.getInstance(this);
        categoryDataSource = CategoryDataSource.getInstance(this);
        changeDataSource = ChangeDataSource.getInstance(this);
        warehouseDataSource = WarehouseDataSource.getInstance(this);

        // Access by product
        txtProductAccess = (TextView) findViewById(R.id.txtProductAccess);
        txtProductAccess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), MyProducts.class);
                startActivity(intent);
            }
        });

        // Access by category
        txtCategoryAccess = (TextView) findViewById(R.id.txtCategoryAccess);
        txtCategoryAccess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), MyCategories.class);
                startActivity(intent);
            }
        });

        // Access by warehouse
        txtWarehouseAccess = (TextView) findViewById(R.id.txtWarehouseAccess);
        txtWarehouseAccess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), MyWarehouses.class);
                startActivity(intent);
            }
        });

        // Pop-up Window on click on 'New Inventory'
        buttonStartInventory = (Button) findViewById(R.id.buttonStartInventory);

        buttonStartInventory.setOnClickListener(new Button.OnClickListener() {

            @Override
            public void onClick(View v) {

                // Create the pop-up window
                Methods.setLocale(getBaseContext());
                LayoutInflater layoutInflater =
                        (LayoutInflater) getBaseContext().getSystemService(LAYOUT_INFLATER_SERVICE);
                View popupView =
                        layoutInflater.inflate(R.layout.activity_popup_ok_cancel, null);
                popupWindow = new PopupWindow(popupView, ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT);

                popupWindowIsOn = true;

                // Catch the elements of the pop-up view
                TextView txtQuestion = (TextView) popupView.findViewById(R.id.txtQuestion);
                Button buttonOk = (Button) popupView.findViewById(R.id.buttonOk);
                Button buttonCancel = (Button) popupView.findViewById(R.id.buttonCancel);

                // Set actions to the elements
                txtQuestion.setText(getString(R.string.question_restart_inventory));

                buttonOk.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        restartInventory();
                       popupWindowIsOn = false;
                        popupWindow.dismiss();
                    }
                });

                buttonCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        popupWindowIsOn = false;
                        popupWindow.dismiss();
                    }
                });
                buttonStartInventory.post(new Runnable() {
                    @Override
                    public void run() {
                        popupWindow.showAsDropDown(buttonStartInventory, 0, -1000);
                    }
                });


            }
        });

        //Creation of a synchronizing button
        buttonSynchronize = (Button) findViewById(R.id.buttonSynchronize);

        //Set the action of the buttonSynchronize
        buttonSynchronize.setOnClickListener(new Button.OnClickListener() {

            @Override
            public void onClick (View v){

                //Instatiation of the EndpointsAsyncTask by passing a context to it
                EndpointsAsyncTask endpointsAsyncTask = new EndpointsAsyncTask(getBaseContext());

                //Execution of the endpointsAsyncTask, all the methods are executed in background
               endpointsAsyncTask.execute();

                //Little piece of information to the user
                Toast toast = android.widget.Toast.makeText(getBaseContext(), R.string.Synchronized, android.widget.Toast.LENGTH_LONG);
                toast.show();

            }
        });

        if (savedInstanceState != null) {
            popupWindowIsOn = savedInstanceState.getBoolean("popupWindowIsOn");
            if (popupWindowIsOn) {
                buttonStartInventory.performClick();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Methods.setLocale(this);

        // Setting all texts, necessary for management of languages
        TextView txtTitle = (TextView) findViewById(R.id.txtTitle);
        txtTitle.setText(R.string.appTitle);
        txtProductAccess.setText(R.string.product_access);
        txtCategoryAccess.setText(R.string.category_access);
        txtWarehouseAccess.setText(R.string.warehouse_access);
        getSupportActionBar().setTitle(R.string.app_name);


        // Display of Inventory-depending fields
        setInventoryFields();


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        invalidateOptionsMenu();
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if( id== R.id.action_settings) {
           Intent intent = new Intent(this, AppSettingsActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putBoolean("popupWindowIsOn", popupWindowIsOn);
    }

    private void restartInventory() {
        List<ObjectStock> allStocks = stockDataSource.getAllStocks();
        for (ObjectStock stock : allStocks){
            stock.setControlled(false);
            stockDataSource.updateStock(stock);
        }
        setInventoryFields();
    }

    public void setInventoryFields() {
        nbInventoriedItems = 0;
        nbItems = 0;
        for (ObjectStock s : stockDataSource.getAllStocks()) {
            nbItems += s.getQuantity();
            if (s.isControlled())
                nbInventoriedItems += s.getQuantity();
        }

        txtInventoryRunning = (TextView) findViewById(R.id.txtInventoryRunning);
        txtInventoryState = (TextView) findViewById(R.id.txtInventoryState);

        txtInventoryState.setText(getString(R.string.progress_of_your_inventory_colon) + " "
                + nbInventoriedItems + "/" + nbItems);

        if (nbItems > nbInventoriedItems) {
            // Inventory is not finished
            txtInventoryRunning.setText(R.string.inventory_running);
            txtInventoryState.setTextColor(getResources().getColor(R.color.flashy));
            txtInventoryRunning.setTextColor(getResources().getColor(R.color.flashy));
        }
        else {
            // Inventory IS finished
            txtInventoryRunning.setText(R.string.inventory_finished_exclamation);
            txtInventoryRunning.setTextColor(getResources().getColor(R.color.indicator_done));
            txtInventoryState.setTextColor(getResources().getColor(R.color.indicator_done));
        }
    }



}
