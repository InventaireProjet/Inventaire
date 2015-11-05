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
import android.widget.EditText;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class WarehouseStock extends AppCompatActivity {


    //TODO get rid of method ?
    // Methods methods = new Methods();

    Button btnDelete;
    Button btnNext;
    Button btnPrevious;
    Button btnAdd;
    PopupWindow popupWindow;
    ArrayList<ObjectProducts> productsToDisplay = new ArrayList<ObjectProducts>();
    ArrayAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//Using the same layout as my products with some changes
        setContentView(R.layout.activity_my_products);
        btnAdd = (Button) findViewById(R.id.buttonAddProduct);
        btnAdd.setVisibility(View.INVISIBLE);

        btnNext = (Button) findViewById(R.id.buttonNext);
        btnNext.setVisibility(View.VISIBLE);

        btnPrevious = (Button) findViewById(R.id.buttonPrevious);
        btnPrevious.setVisibility(View.VISIBLE);


        View squareInventoryState = findViewById(R.id.squareInventoryState);
        squareInventoryState.setVisibility(View.VISIBLE);


        //Warehouse name retrieved from the previous screen
        final TextView title = (TextView) findViewById(R.id.txtTitle);
        Intent intent = getIntent();
        final String warehouseName = intent.getStringExtra("warehouseName");
        title.setText(warehouseName);

        //Define the products to display
        productsToDisplay =  Methods.getObjectsListbyWarehouse(warehouseName);
        squareInventoryState.setBackgroundColor(Methods.giveColor(squareInventoryState, Methods.getInventoryState(productsToDisplay)));

// Set onClickListener to button "Previous"

        btnPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), WarehouseStock.class);
                int nbItems = ObjectsLists.getWarehouseList().size();
                //TODO GéRER AVEC ID AVEC VRAIES DONNEES
                //intent.putExtra("position", (position+nbItems-1)%nbItems);
                startActivity(intent);
                finish();

            }
        });

        // Set onClickListener to button "Next"

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), WarehouseStock.class);
                int nbItems = ObjectsLists.getWarehouseList().size();
                //TODO GéRER AVEC ID AVEC VRAIES DONNEES
                //intent.putExtra("position", (position + nbItems + 1) % nbItems);
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

// Total quantity of products and value of stock
        int totalQuantity = 0;
        double totalValue = 0.0;
        for (ObjectProducts product : productsToDisplay) {
            totalQuantity += product.getQuantity();
            totalValue += product.getPrice()*product.getQuantity();
        }
        View squareTotalStock =  findViewById(R.id.squareTotalStock);
        TextView txtStock = (TextView) findViewById(R.id.txtStock);
        TextView txtStockValue = (TextView) findViewById(R.id.txtStockValue);
        squareTotalStock.setBackgroundColor(Methods.giveColor(squareTotalStock, Methods.getInventoryState(productsToDisplay)));
        txtStock.setText("Stock : " + Integer.toString(totalQuantity));
        txtStockValue.setText("Valeur : CHF " + Double.toString(totalValue));

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
                Button buttonDeleteWarehouse = (Button) popupView.findViewById(R.id.buttonDeleteWarehouse);
                Button buttonDeleteAll = (Button) popupView.findViewById(R.id.buttonDeleteAll);
                Button buttonCancel = (Button) popupView.findViewById(R.id.buttonCancel);


                //Deleting the warehouse
                buttonDeleteWarehouse.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        ArrayList<ObjectWarehouse> warehouses = ObjectsLists.getWarehouseList();

                        for (int i = 0; i < warehouses.size(); i++) {

                            if (warehouses.get(i).getName().equals(warehouseName)) {

                                ObjectsLists.getWarehouseList().remove(i);
                            }
                        }

                        popupWindow.dismiss();
                        Intent intent = new Intent(getBaseContext(), MyWarehouses.class);
                        startActivity(intent);
                    }
                });

                //Deleting the warehouse and all its stock
                buttonDeleteAll.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        ArrayList<ObjectWarehouse> warehouses = ObjectsLists.getWarehouseList();

                        //TODO Deleting stock product in this warehouse (data management)


                        for (int i = 0; i < warehouses.size(); i++) {

                            if (warehouses.get(i).getName().equals(warehouseName)) {

                                ObjectsLists.getWarehouseList().remove(i);
                            }
                        }

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

    }

    //Refreshing the adapter so it shows the changes
    @Override
    protected void onResume() {
        super.onResume();
        adapter.notifyDataSetChanged();
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
            txtCategory.setText(product.getCategory().getName());
            txtQuantity.setText(Integer.toString(product.getQuantity()));
            txtPrice.setText("CHF " + Double.toString(product.getPrice()));

            // Sending the product to the next field
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ObjectsLists.setProductList((ArrayList) productsToDisplay);
                    Intent intent = new Intent(getBaseContext(), Product.class);
                    // TODO in the final version, send only the product Id...
                    intent.putExtra("position", position);
                    startActivity(intent);
                }
            });

            return convertView;

        }
    }
}

