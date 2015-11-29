package com.androidprojects.inventaireii;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.androidprojects.inventaireii.db.adapter.ProductDataSource;
import com.androidprojects.inventaireii.db.adapter.WarehouseDataSource;
import com.androidprojects.inventaireii.Preferences.AppSettingsActivity;


import java.util.ArrayList;
import java.util.List;

public class WarehouseStock extends AppCompatActivity {

    Button btnDelete;
    Button btnNext;
    Button btnPrevious;
    Button btnAdd;
    PopupWindow popupWindow;
    List<ObjectProducts> productsToDisplay;
    ArrayAdapter adapter;
    View squareInventoryState;
    View squareTotalStock;
    WarehouseDataSource warehouseDataSource;
    ProductDataSource productDataSource;
    ObjectWarehouse  warehouse;
    int nbWarehouses;
    Button buttonDeleteWarehouse;
    Button buttonCancel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



    }

    // Total quantity of products and value of stock
    private void showTotalInStock() {
        int totalQuantity = 0;
        double totalValue = 0.0;
        for (ObjectProducts product : productsToDisplay) {
            totalQuantity += product.getQuantity();
            totalValue += product.getPrice()*product.getQuantity();
        }
        squareTotalStock =  findViewById(R.id.squareTotalStock);
        TextView txtStock = (TextView) findViewById(R.id.txtStock);
        TextView txtStockValue = (TextView) findViewById(R.id.txtStockValue);
        squareTotalStock.setBackgroundColor(Methods.giveColor(squareTotalStock, Methods.getInventoryState(productsToDisplay)));
        txtStock.setText(getResources().getString(R.string.stock_colon) + " " + Integer.toString(totalQuantity));
        txtStockValue.setText(getResources().getString(R.string.value_colon) +" CHF " + String.format("%,.2f",totalValue));
    }


    //Refreshing the adapter so it shows the changes
    @Override
    protected void onResume() {
        super.onResume();


        //Language management to refresh
        Methods.setLocale(this);
        getSupportActionBar().setTitle(R.string.warehouse_stock);

        productDataSource =  ProductDataSource.getInstance(this);
        warehouseDataSource =  WarehouseDataSource.getInstance(this);

//Using the same layout as my products with some changes
        setContentView(R.layout.activity_my_products);
        btnAdd = (Button) findViewById(R.id.buttonAddProduct);
        btnAdd.setVisibility(View.INVISIBLE);

        btnNext = (Button) findViewById(R.id.buttonNext);
        btnNext.setVisibility(View.VISIBLE);

        btnPrevious = (Button) findViewById(R.id.buttonPrevious);
        btnPrevious.setVisibility(View.VISIBLE);

        squareInventoryState = findViewById(R.id.squareInventoryState);
        squareInventoryState.setVisibility(View.VISIBLE);


        //Warehouse name retrieved from the previous screen
        final TextView title = (TextView) findViewById(R.id.txtTitle);
        Intent intent = getIntent();
        final int warehouseId = intent.getIntExtra("warehouseId", 1);

        warehouse = warehouseDataSource.getWarehouseById(warehouseId);

        title.setText(warehouse.getName());

        //Define the products to display
        productsToDisplay = productDataSource.getAllProductsByWarehouse(warehouseId);


        squareInventoryState.setBackgroundColor(Methods.giveColor(squareInventoryState, Methods.getInventoryState(productsToDisplay)));

// Set onClickListener to button "Previous"

        btnPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), WarehouseStock.class);
                List<ObjectWarehouse> warehouses = warehouseDataSource.getAllWarehouses();
                int position = 0;
                for (ObjectWarehouse w : warehouses)
                    if(w.getId() == warehouse.getId())
                        position = warehouses.indexOf(w);

                nbWarehouses = warehouses.size();
                position = (position+nbWarehouses-1)%nbWarehouses;
                intent.putExtra("warehouseId", warehouses.get(position).getId());
                startActivity(intent);
                finish();

            }
        });

        // Set onClickListener to button "Next"

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), WarehouseStock.class);
                List<ObjectWarehouse> warehouses = warehouseDataSource.getAllWarehouses();
                int position = 0;
                for (ObjectWarehouse w : warehouses)
                    if (w.getId() == warehouse.getId())
                        position = warehouses.indexOf(w);

                nbWarehouses = warehouses.size();
                position = (position+nbWarehouses+1)%nbWarehouses;
                intent.putExtra("warehouseId", warehouses.get(position).getId());
                startActivity(intent);
                finish();
            }
        });


// Fill the ListView with products
        ListView lvProducts = (ListView) findViewById(R.id.lvProducts);
        View header = getLayoutInflater().inflate(R.layout.product_row_header, null);
        lvProducts.addHeaderView(header);
        adapter = new ProductsAdapter(this, productsToDisplay);
        lvProducts.setAdapter(adapter);

        showTotalInStock();


//Delete the warehouse
        btnDelete = (Button) findViewById(R.id.buttonDelete);
        btnDelete.setVisibility(View.VISIBLE);
        btnDelete.setOnClickListener(new Button.OnClickListener() {

            @Override
            public void onClick(View v) {
                LayoutInflater layoutInflater =
                        (LayoutInflater) getBaseContext().getSystemService(LAYOUT_INFLATER_SERVICE);

                View popupView = layoutInflater.inflate(R.layout.delete_warehouse_popup, null);
                popupWindow = new PopupWindow(popupView, ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT);


                // Catch the elements of the pop-up view
                buttonDeleteWarehouse = (Button) popupView.findViewById(R.id.buttonDeleteWarehouse);
                buttonCancel = (Button) popupView.findViewById(R.id.buttonCancel);


                //Deleting  the warehouse and the stocks of its products
                buttonDeleteWarehouse.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {


                        warehouseDataSource.deleteWarehouseAndProducts(warehouseId);

                        popupWindow.dismiss();
                        Intent intent = new Intent(getBaseContext(), MyWarehouses.class);
                        startActivity(intent);
                    }
                });


                buttonCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        popupWindow.dismiss();
                    }
                });

                popupWindow.showAsDropDown(buttonDeleteWarehouse, 0, -100);

            }
        });

/*        adapter.notifyDataSetChanged();
        squareInventoryState.setBackgroundColor(Methods.giveColor(squareInventoryState, Methods.getInventoryState(productsToDisplay)));
        squareTotalStock.setBackgroundColor(Methods.giveColor(squareTotalStock, Methods.getInventoryState(productsToDisplay)));
        showTotalInStock();
*/

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
            case android.R.id.home:
                finish();
                return true;


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

