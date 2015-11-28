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
import com.androidprojects.inventaireii.Preferences.AppSettingsActivity;


import java.util.List;

public class MainActivity extends AppCompatActivity {


    // Declarations of variables
    int nbItems = 0;
    int nbInventoriedItems = 0;
    StockDataSource stockDataSource;
    ProductDataSource productDataSource;

    // Declarations of views
    TextView txtTitle;
    TextView txtInventoryRunning;
    TextView txtInventoryState;
    TextView txtProductAccess;
    TextView txtCategoryAccess;
    TextView txtWarehouseAccess;
    PopupWindow popupWindow;
    Button buttonStartInventory;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Methods.setLocale(this);
        setContentView(R.layout.activity_main);


        stockDataSource = StockDataSource.getInstance(this);
        productDataSource = ProductDataSource.getInstance(this);

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
                        popupWindow.dismiss();
                    }
                });

                buttonCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        popupWindow.dismiss();
                    }
                });

                popupWindow.showAsDropDown(buttonStartInventory, 0, -500);

            }
        });
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


        /* Display of Inventory-depending fields */
        nbInventoriedItems = 0;
        for (ObjectProducts p : productDataSource.getAllProducts()){
            if (Methods.getInventoryState(p).equals("done"))
                nbInventoriedItems++;
        }
        nbItems = productDataSource.getNumberOfProducts();

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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

    private void restartInventory() {
        List<ObjectStock> allStocks = stockDataSource.getAllStocks();
        nbInventoriedItems = 0;
        for (ObjectStock stock : allStocks){
            stock.setControlled(false);
            stockDataSource.updateStock(stock);
        }

        nbItems = productDataSource.getNumberOfProducts();

        txtInventoryState.setText(getResources().getString(R.string.progress_of_your_inventory_colon) + " "
                + nbInventoriedItems + "/" + nbItems);
        txtInventoryState.setVisibility(View.VISIBLE);

    }



}
