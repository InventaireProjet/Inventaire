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

import com.androidprojects.inventaireii.db.adapter.CategoryDataSource;
import com.androidprojects.inventaireii.db.adapter.ProductDataSource;

import java.util.ArrayList;
import java.util.List;

public class Category extends AppCompatActivity {

    Button btnModify;
    Button btnDelete;
    Button btnNext;
    Button btnPrevious;
    Button btnAdd;
    PopupWindow popupWindow;
    List<ObjectProducts> productsToDisplay = new ArrayList<ObjectProducts>();
    ArrayAdapter adapter;
    View square;
    View squareInventoryState;
    View squareTotalStock;
    CategoryDataSource categoryDataSource;
    ProductDataSource productDataSource;
    ObjectCategories category;
    int nbCategories;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        categoryDataSource =  CategoryDataSource.getInstance(this);
         productDataSource =  ProductDataSource.getInstance(this);

//Using the same layout as my products with some changes
        setContentView(R.layout.activity_my_products);
        btnAdd = (Button) findViewById(R.id.buttonAddProduct);
        btnAdd.setVisibility(View.INVISIBLE);

        btnNext = (Button) findViewById(R.id.buttonNext);
        btnNext.setVisibility(View.VISIBLE);

        btnPrevious = (Button) findViewById(R.id.buttonPrevious);
        btnPrevious.setVisibility(View.VISIBLE);

        //Category name retrieved from the previous screen
        final TextView title = (TextView) findViewById(R.id.txtTitle);
        Intent intent = getIntent();
        final int categoryId = intent.getIntExtra("categoryId", 1);

        category = categoryDataSource.getCategoryById(categoryId);
        title.setText(category.getName());

        //Define the products to display
        //productsToDisplay =  Methods.getObjectsListbyCategory(category.getName());
        //TODO Disable that, enable this
        productsToDisplay = productDataSource.getAllProductsByCategory(categoryId);

        //Inventory state
        squareInventoryState = findViewById(R.id.squareInventoryState);
        squareInventoryState.setVisibility(View.VISIBLE);
        squareInventoryState.setBackgroundColor(Methods.giveColor(squareInventoryState, Methods.getInventoryState(productsToDisplay)));

        // Set onClickListener to button "Previous"

        btnPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Category.class);
                List<ObjectCategories> categories = categoryDataSource.getAllCategories();
                int position = 0;
                for(ObjectCategories c : categories)
                    if(c.getId() == categoryId)
                        position = categories.indexOf(c);

                nbCategories = categories.size();
                position = (position+nbCategories-1)%nbCategories;
                intent.putExtra("categoryId", categories.get(position).getId());
                startActivity(intent);
                finish();

            }
        });

        // Set onClickListener to button "Next"

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Category.class);
                List<ObjectCategories> categories = categoryDataSource.getAllCategories();
                int position = 0;
                for(ObjectCategories c : categories)
                    if(c.getId() == categoryId)
                        position = categories.indexOf(c);

                nbCategories = categories.size();
                position = (position+nbCategories+1)%nbCategories;
                intent.putExtra("categoryId", categories.get(position).getId());
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
        showTotalInStock();

        //Modify the category name
        btnModify = (Button) findViewById(R.id.buttonModify);
        btnModify.setVisibility(View.VISIBLE);
        btnModify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater layoutInflater =
                        (LayoutInflater) getBaseContext().getSystemService(LAYOUT_INFLATER_SERVICE);

                View popupView = layoutInflater.inflate(R.layout.new_category_popup, null);
                popupWindow = new PopupWindow(popupView, ViewGroup.LayoutParams.MATCH_PARENT,
                        500);

                //To view the keyboard
                popupWindow.setFocusable(true);

                // Catch the elements of the pop-up view
                final TextView title2 = (TextView) popupView.findViewById(R.id.txtTitle);
                Button buttonValidate = (Button) popupView.findViewById(R.id.buttonValidate);
                Button buttonCancel = (Button) popupView.findViewById(R.id.buttonCancel);
                final EditText userEntry = (EditText) popupView.findViewById(R.id.userEntry);

                final String categoryName = title.getText().toString();

                //Display the title and the category name in EditText
                title2.setText(getResources().getText(R.string.modify_Category));
                userEntry.setText(categoryName);

                //Saving the new category name by validating the text entry
                buttonValidate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

//TODO CHANGE WITH DATA in comment
                       category.setName(userEntry.getText().toString());

                        categoryDataSource.updateCategory(category);

                        title.setText(userEntry.getText().toString());

                        if (productsToDisplay!=null){
                        adapter.notifyDataSetChanged();
                        }
                        popupWindow.dismiss();

                    }
                });

                buttonCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        popupWindow.dismiss();
                    }
                });

                popupWindow.showAsDropDown(btnModify, 0, -100);
            }
        });

//Delete the category
        btnDelete = (Button) findViewById(R.id.buttonDelete);
        btnDelete.setVisibility(View.VISIBLE);
        btnDelete.setOnClickListener(new Button.OnClickListener() {

            @Override
            public void onClick(View v) {
                LayoutInflater layoutInflater =
                        (LayoutInflater) getBaseContext().getSystemService(LAYOUT_INFLATER_SERVICE);

                View popupView = layoutInflater.inflate(R.layout.delete_category_popup, null);
                popupWindow = new PopupWindow(popupView, ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT);


                // Catch the elements of the pop-up view
                Button buttonValidate = (Button) popupView.findViewById(R.id.buttonValidate);
                Button buttonCancel = (Button) popupView.findViewById(R.id.buttonCancel);



                //Deleting the category by validating the text entry
                buttonValidate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        categoryDataSource.deleteCategory(categoryId);


                        popupWindow.dismiss();
                        Intent intent = new Intent(getBaseContext(), MyCategories.class);
                        startActivity(intent);

                    }
                });

                buttonCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        popupWindow.dismiss();
                    }
                });

                popupWindow.showAsDropDown(buttonValidate, 0, -100);

            }
        });

    }

    private void showTotalInStock() {
        int totalQuantity = 0;
        double totalValue = 0.0;
        for (ObjectProducts product : productsToDisplay) {
            totalQuantity += product.getQuantity();
            totalValue += product.getPrice() * product.getQuantity();
        }
        squareTotalStock = findViewById(R.id.squareTotalStock);
        TextView txtStock = (TextView) findViewById(R.id.txtStock);
        TextView txtStockValue = (TextView) findViewById(R.id.txtStockValue);
        squareTotalStock.setBackgroundColor(Methods.giveColor(squareTotalStock, Methods.getInventoryState(productsToDisplay)));
        txtStock.setText("Stock : " + Integer.toString(totalQuantity));
        txtStockValue.setText("Valeur : CHF " + Double.toString(totalValue));
    }

    @Override
    protected void onResume() {
        super.onResume();

        adapter.notifyDataSetChanged();
        squareInventoryState.setBackgroundColor(Methods.giveColor(squareInventoryState, Methods.getInventoryState(productsToDisplay)));
        squareTotalStock.setBackgroundColor(Methods.giveColor(squareTotalStock, Methods.getInventoryState(productsToDisplay)));
        showTotalInStock();



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
            square = convertView.findViewById(R.id.square);
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

            // Sending the product to the next activity
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //TODO ASK WHAT IS THE USE OF THE LINE BELOW ???????????????????????????????
                    // there is no use ;-) DM // ObjectsLists.setProductList((ArrayList) productsToDisplay);
                    Intent intent = new Intent(getBaseContext(), Product.class);
                    intent.putExtra("position", product.getId());
                    startActivity(intent);
                }
            });

            return convertView;

        }
    }
}