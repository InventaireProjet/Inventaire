package com.androidprojects.inventaireii;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import java.util.ArrayList;

public class StockNewOrModify extends AppCompatActivity {

    ObjectProducts product;
    ObjectStock stock;
    int productPosition;
    int stockPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock_new_or_modify);

        // Fill the spinner with the warehouses
        final Spinner spinnerWarehouse = (Spinner) findViewById(R.id.spinnerWarehouse);

        ArrayList<String> warehousesNames = new ArrayList<>();
        for (ObjectWarehouse w : ObjectsLists.getWarehouseList()){
            warehousesNames.add(w.getName());
        }

        ArrayAdapter adapter = new ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, warehousesNames);
        spinnerWarehouse.setAdapter(adapter);

        // Get the elements
        TextView txtProductName = (TextView) findViewById(R.id.txtProductName);
        Switch switchControlled = (Switch) findViewById(R.id.switchControlled);
        EditText etQuantity = (EditText) findViewById(R.id.etQuantity);

        // Get the product and the stock Id
        Intent intent = getIntent();
        productPosition = intent.getIntExtra("productPosition", -1);
        stockPosition = intent.getIntExtra("stockPostion", -1);
        product = ObjectsLists.getProductList().get(productPosition);

        // If it's an existing stock, fill the fields
        if (stockPosition >= 0){
            stock = ObjectsLists.getStockList().get(stockPosition);
            txtProductName.setText(product.getName());
            switchControlled.setChecked(stock.isControlled());
            etQuantity.setText(Integer.toString(stock.getQuantity()));
            int p = ObjectsLists.getWarehouseList().indexOf(stock.getWarehouse());
            spinnerWarehouse.setSelection(p);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_stock_new_or_modify, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
