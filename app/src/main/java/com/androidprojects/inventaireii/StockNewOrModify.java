package com.androidprojects.inventaireii;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.util.ArrayList;

public class StockNewOrModify extends AppCompatActivity {

    ObjectProducts product;
    ObjectStock stock;
    int productPosition;
    int stockPosition;
    TextView txtProductName;
    ToggleButton switchControlled;
    EditText etQuantity;

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
        // Give a color to the selected item of spinner
        spinnerWarehouse.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String item = (String) parent.getItemAtPosition(position);
                ((TextView) parent.getChildAt(0)).setTextColor(getResources().getColor(R.color.button_text));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        // Get the elements
        txtProductName = (TextView) findViewById(R.id.txtProductName);
        final ToggleButton switchControlled = (ToggleButton) findViewById(R.id.switchControlled);
        final EditText etQuantity = (EditText) findViewById(R.id.etQuantity);

        // Get the product and the stock Id
        Intent intent = getIntent();
        productPosition = intent.getIntExtra("productPosition", -1);
        stockPosition = intent.getIntExtra("stockPosition", -1);
        product = ObjectsLists.getProductList().get(productPosition);

        // If it's an existing stock, fill the fields
        if (stockPosition >= 0){
            stock = ObjectsLists.getStockList().get(stockPosition);
            switchControlled.setChecked(stock.isControlled());
            etQuantity.setText(Integer.toString(stock.getQuantity()));
            int p = ObjectsLists.getWarehouseList().indexOf(stock.getWarehouse());
            spinnerWarehouse.setSelection(p);
        }

        // Product name is known in both cases
        txtProductName.setText(product.getName());

        /* BUTTONS */
        // Get the buttons
        Button buttonCancel = (Button) findViewById(R.id.buttonCancel);
        Button buttonSuppress = (Button) findViewById(R.id.buttonSuppress);
        Button buttonSave = (Button) findViewById(R.id.buttonSave);

        // If it's a new stock, suppress the button suppress
        if (stockPosition == -1)
            buttonSuppress.setVisibility(View.INVISIBLE);

        // Set onClickListener to the button Cancel
        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // Set onClickListener to the button Suppress
        buttonSuppress.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                product.removeStock(stock);
                ObjectsLists.getStockList().remove(stock);
                finish();
            }
        });

        // Set onClickListener to the button Save
        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = "";
                if (stockPosition == -1) {
                    // New stock, to create
                    stock = new ObjectStock(Integer.parseInt(etQuantity.getText().toString()),
                            switchControlled.isChecked(),
                            product,
                            ObjectsLists.getWarehouseList().get(spinnerWarehouse.getSelectedItemPosition()));
                    ObjectsLists.getStockList().add(stock);
                    product.addStock(stock);
                    message = "Stock ajouté";
                }
                else {
                    // Modification of an existing stock
                    stock.setControlled(switchControlled.isChecked());
                    stock.setQuantity(Integer.parseInt(etQuantity.getText().toString()));
                    stock.setWarehouse(ObjectsLists.getWarehouseList().get(spinnerWarehouse.getSelectedItemPosition()));
                    message = "Stock modifié";
                }

                Toast toast = Toast.makeText(getBaseContext(), message, Toast.LENGTH_LONG);
                toast.show();

                finish();

            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_action_bar, menu);

        // Hide the buttons we don't need
        menu.findItem(R.id.goto_products).setVisible(false);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        Class c = Methods.onOptionsItemSelected(id);
        if (c != null) {
            Intent intent = new Intent(getBaseContext(), c);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }
}
