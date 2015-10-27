package com.androidprojects.inventaireii;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.ArrayList;

public class ProductNewOrModify extends AppCompatActivity {

    ObjectProducts product = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_new_or_modify);

        // Fill the Spinner with all categories
        Spinner spinnerCategory = (Spinner) findViewById(R.id.spinnerCategory);

        ArrayList<String> categoriesNames = new ArrayList<>();
        for (ObjectCategories cat : ObjectsLists.categoryList) {
            categoriesNames.add(cat.getName());
        }

        ArrayAdapter adapter = new ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, categoriesNames );
        spinnerCategory.setAdapter(adapter);

        // Get the product from intent. If position = -1, it is a new product
        Intent intent = getIntent();
        int pos = intent.getIntExtra("position", -1);

        // If it is an existing product (modification), fill the fields with products data
        if (pos >= 0) {
            EditText etProductName = (EditText) findViewById(R.id.productName);
            EditText etArtNb = (EditText) findViewById(R.id.artNb);
            EditText etPrice = (EditText) findViewById(R.id.price);
            EditText etDescription = (EditText) findViewById(R.id.description);
            product = ObjectsLists.getProductList().get(pos);
            etProductName.setText(product.getName());
            etArtNb.setText(product.getArtNb());
            etPrice.setText(Double.toString(product.getPrice()));
            etDescription.setText(product.getDescription());
            int p = ObjectsLists.categoryList.indexOf(product.getCategory());
            spinnerCategory.setSelection(p);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_product_new_or_modify, menu);
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
