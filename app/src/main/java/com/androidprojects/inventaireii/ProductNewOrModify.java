package com.androidprojects.inventaireii;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.lang.reflect.Method;
import java.util.ArrayList;

public class ProductNewOrModify extends AppCompatActivity {

    ObjectProducts product = null;
    int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_new_or_modify);

        // Fill the Spinner with all categories
        final Spinner spinnerCategory = (Spinner) findViewById(R.id.spinnerCategory);

        ArrayList<String> categoriesNames = new ArrayList<>();
        for (ObjectCategories cat : ObjectsLists.categoryList) {
            categoriesNames.add(cat.getName());
        }

        ArrayAdapter adapter = new ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, categoriesNames );
        spinnerCategory.setAdapter(adapter);

        // Get the elements of the activity
        final EditText etProductName = (EditText) findViewById(R.id.productName);
        final EditText etArtNb = (EditText) findViewById(R.id.artNb);
        final EditText etPrice = (EditText) findViewById(R.id.price);
        final EditText etDescription = (EditText) findViewById(R.id.description);

        // Get the product Id from intent. If position = -1, it is a new product
        Intent intent = getIntent();
        position = intent.getIntExtra("position", -1);

        // If it is an existing product (modification), fill the fields with products data
        if (position >= 0) {
            product = ObjectsLists.getProductList().get(position);
            etProductName.setText(product.getName());
            etArtNb.setText(product.getArtNb());
            etPrice.setText(String.format("%,.2f", product.getPrice()));
            etDescription.setText(product.getDescription());
            int p = ObjectsLists.categoryList.indexOf(product.getCategory());
            spinnerCategory.setSelection(p);
        }

        // Button "Cancel"
        Button buttonCancel = (Button) findViewById(R.id.buttonCancel);
        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();

            }
        });

        // Button "Save"
        Button buttonSave = (Button) findViewById(R.id.buttonSave);
        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = "";
                if (position == -1) {
                    // New article
                    ObjectCategories cat = ObjectsLists.categoryList
                            .get(spinnerCategory.getSelectedItemPosition());
                    product = new ObjectProducts(etArtNb.getText().toString(), etProductName.getText().toString(),
                            cat, 0, Double.valueOf(etPrice.getText().toString()), "" );
                    product.setDescription(etDescription.getText().toString());
                    ObjectsLists.getProductList().add(product);
                    message = "Produit créé";
                }
                else {
                    // Modify article
                    int categoryPosition = spinnerCategory.getSelectedItemPosition();

                    product.setArtNb(etArtNb.getText().toString());
                    product.setName(etProductName.getText().toString());
                    product.setPrice(Double.parseDouble(etPrice.getText().toString()));
                    product.setDescription(etDescription.getText().toString());
                    product.setCategory(ObjectsLists.categoryList.get(categoryPosition));
                    message = "Produit modifié";
                }
                // Confirmation for user
                Toast toast = Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG);
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
