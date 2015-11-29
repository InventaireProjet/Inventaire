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
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.androidprojects.inventaireii.db.adapter.ProductDataSource;
import com.androidprojects.inventaireii.db.adapter.StockDataSource;
import com.androidprojects.inventaireii.db.adapter.WarehouseDataSource;

import java.util.ArrayList;
import java.util.List;

public class StockNewOrModify extends AppCompatActivity {

    private ObjectProducts product;
    private ObjectStock stock;
    private int productId;
    private int stockId;
    private List<ObjectWarehouse> warehousesList;

    private StockDataSource stockDataSource;
    private ProductDataSource productDataSource;
    private WarehouseDataSource warehouseDataSource;
    private ArrayAdapter adapter;

    // Declaration of views
    TextView txtProductName;
    TextView txtControlled;
    TextView txtQuantity;
    TextView txtWarehouse;
    ToggleButton switchControlled;
    EditText etQuantity;
    Button buttonCancel;
    Button buttonSuppress;
    Button buttonSave;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock_new_or_modify);

        stockDataSource = StockDataSource.getInstance(this);
        productDataSource = ProductDataSource.getInstance(this);
        warehouseDataSource = WarehouseDataSource.getInstance(this);

        // Fill the spinner with the warehouses
        final Spinner spinnerWarehouse = (Spinner) findViewById(R.id.spinnerWarehouse);

        warehousesList = warehouseDataSource.getAllWarehouses();
        ArrayList<String> warehousesNames = new ArrayList<>();
        for (ObjectWarehouse w : warehousesList){
            warehousesNames.add(w.getName());
        }

        adapter = new ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, warehousesNames);
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
        txtControlled = (TextView)findViewById(R.id.txtControlled);
        txtQuantity = (TextView)findViewById(R.id.txtQuantity);
        txtWarehouse = (TextView) findViewById(R.id.txtWarehouse);
        switchControlled = (ToggleButton) findViewById(R.id.switchControlled);
        etQuantity = (EditText) findViewById(R.id.etQuantity);

        // Get the product and the stock Id
        final Intent intent = getIntent();
        productId = intent.getIntExtra("productPosition", -1);
        stockId = intent.getIntExtra("stockPosition", -1);
        product = productDataSource.getProductById(productId);

        // If it's an existing stock, fill the fields
        if (stockId >= 0){
            stock = stockDataSource.getStockById(stockId);
            switchControlled.setChecked(stock.isControlled());
            etQuantity.setText(Integer.toString(stock.getQuantity()));

            int warehousePos = 0;
            for (ObjectWarehouse w : warehousesList)
                if(w.getId() == stock.getWarehouse().getId())
                    warehousePos = warehousesList.indexOf(w);
            spinnerWarehouse.setSelection(warehousePos);
        }

        // Product name is known in both cases
        txtProductName.setText(product.getName());

        /* BUTTONS */
        // Get the buttons
        buttonCancel = (Button) findViewById(R.id.buttonCancel);
        buttonSuppress = (Button) findViewById(R.id.buttonSuppress);
        buttonSave = (Button) findViewById(R.id.buttonSave);

        // If it's a new stock, suppress the button suppress
        if (stockId == -1)
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
                stockDataSource.deleteStock(stock);
            }
        });

        // Set onClickListener to the button Save
        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = "";
                if (stockId == -1) {
                    // New stock, to create
                    stock = new ObjectStock(Integer.parseInt(etQuantity.getText().toString()),
                            switchControlled.isChecked(),
                            product,
                            warehousesList.get(spinnerWarehouse.getSelectedItemPosition()));

                    stockDataSource.createStock(stock);
                    product.addStock(stock);
                    message = getResources().getString(R.string.stock_added);
                } else {
                    // Modification of an existing stock
                    stock.setControlled(switchControlled.isChecked());
                    try {
                        stock.setQuantity(Integer.parseInt(etQuantity.getText().toString()));
                    } catch (NumberFormatException e) {
                        stock.setQuantity(0);
                    }
                    stock.setWarehouse(warehousesList.get(spinnerWarehouse.getSelectedItemPosition()));
                    stockDataSource.updateStock(stock);
                    message = getResources().getString(R.string.stock_updated);
                }

                Toast toast = Toast.makeText(getBaseContext(), message, Toast.LENGTH_LONG);
                toast.show();

                Intent intent = new Intent(getBaseContext(), Product.class);
                intent.putExtra("position", product.getId());
                startActivity(intent);

                finish();

            }
        });

        // If there is no Warehouse in DB, we cannot create any stock: hide elements
        if(warehouseDataSource.getAllWarehouses().size() == 0) {
            txtProductName.setText(R.string.no_warehouse);
            txtControlled.setText(R.string.you_have_to_create_warehouse);
            switchControlled.setVisibility(View.INVISIBLE);
            txtQuantity.setVisibility(View.INVISIBLE);
            etQuantity.setVisibility(View.INVISIBLE);
            txtWarehouse.setVisibility(View.INVISIBLE);
            buttonSave.setVisibility(View.INVISIBLE);
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        Methods.setLocale(this);
        txtControlled.setText(R.string.controlled_colon);
        switchControlled.setTextOn(getResources().getString(R.string.yes));
        switchControlled.setTextOff(getResources().getString(R.string.no));
        switchControlled.setChecked(switchControlled.isChecked());
        txtQuantity.setText(R.string.quantity_colon);
        txtWarehouse.setText(R.string.warehouse_colon);
        buttonCancel.setText(R.string.cancel);
        buttonSuppress.setText(R.string.suppress);
        buttonSave.setText(R.string.save);
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
