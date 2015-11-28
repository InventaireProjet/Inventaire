package com.androidprojects.inventaireii;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.androidprojects.inventaireii.db.adapter.ProductDataSource;

import org.w3c.dom.Text;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class MyProducts extends AppCompatActivity {
    private ProductDataSource productDataSource;
    private List<ObjectProducts> productsList;
    ArrayAdapter adapter;

    // Declaration of views
    View squareTotalStock;
    View square;
    Button buttonAddProduct;
    ListView lvProducts;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Methods.setLocale(this);
        setContentView(R.layout.activity_my_products);

        // Catch views of the activity
        square = (View) findViewById(R.id.squareInventoryState);
        lvProducts = (ListView) findViewById(R.id.lvProducts);

        productDataSource = ProductDataSource.getInstance(this);
        productsList = productDataSource.getAllProducts();

        // Set OnClickListener to Button "Add product"
        buttonAddProduct = (Button) findViewById(R.id.buttonAddProduct);
        buttonAddProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), ProductNewOrModify.class);
                intent.putExtra("position", -1);
                startActivity(intent);
            }
        });

        // Fill the ListView
        View header = getLayoutInflater().inflate(R.layout.product_row_header, null);
        lvProducts.addHeaderView(header);
        adapter = new ProductsAdapter(this, productsList);
        lvProducts.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Methods.setLocale(this);
        adapter.notifyDataSetChanged();

        // Three elements below the ListView : total quantity of products and value of stock
        int totalQuantity = 0;
        double totalValue = 0.0;
        for (ObjectProducts product : productsList) {
            totalQuantity += product.getQuantity();
            totalValue += product.getPrice()*product.getQuantity();
        }
        squareTotalStock = findViewById(R.id.squareTotalStock);
        TextView txtStock = (TextView) findViewById(R.id.txtStock);
        TextView txtStockValue = (TextView) findViewById(R.id.txtStockValue);
        squareTotalStock.setBackgroundColor(Methods.giveColor(squareTotalStock, Methods.getInventoryState(productsList)));
        txtStock.setText(getResources().getString(R.string.stock_colon) + " "
                + Integer.toString(totalQuantity));
        txtStockValue.setText(getResources().getString(R.string.value_colon)
                + " CHF " + String.format("%,.2f", totalValue));
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
            Intent intent = new Intent(getBaseContext(), Methods.onOptionsItemSelected(id));
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    private class ProductsAdapter extends ArrayAdapter {
        // TODO suppress public ProductsAdapter() { super(MyProducts.this, R.layout.product_row);}

        public ProductsAdapter(Context context, List<ObjectProducts> productsList) {
            super(context, 0, productsList);
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            final ObjectProducts product = (ObjectProducts) getItem(position);

            // Creation of the link with the product_row layout if not already existing
            if (convertView == null){
                convertView = LayoutInflater.from(getContext())
                        .inflate(R.layout.product_row, parent, false);
            }

            // Effective linking with the product_row layout
            View square = convertView.findViewById(R.id.square);
            TextView txtArtNb = (TextView) convertView.findViewById(R.id.no_art);
            TextView txtName = (TextView) convertView.findViewById(R.id.name);
            TextView txtCategory = (TextView) convertView.findViewById(R.id.category);
            TextView txtQuantity = (TextView) convertView.findViewById(R.id.quantity);
            TextView txtPrice = (TextView) convertView.findViewById(R.id.price);

            // Fill with data
            square.setBackgroundColor(Methods.giveColor(square, Methods.getInventoryState(product)));
            txtArtNb.setText(product.getArtNb());
            txtName.setText(product.getName());
            if (product.getCategory() != null)
                txtCategory.setText(product.getCategory().getName());
            else
                txtCategory.setText(R.string.no_category);
            txtQuantity.setText(Integer.toString(product.getQuantity()));
            txtPrice.setText("CHF " + String.format("%,.2f", product.getPrice()));

            // Sending the product to the next activity
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getBaseContext(), Product.class);
                    intent.putExtra("position", product.getId());
                    startActivity(intent);
                }
            });

            return convertView;

        }
    }
}
