package com.androidprojects.inventaireii;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class Category extends AppCompatActivity {

    //TODO get rid of method ?
    Methods methods = new Methods();
    Button btnModify;
    Button btnDelete;
    Button btnNext;
    Button btnPrevious;
    Button btnAdd;
    PopupWindow popupWindow;
    ArrayList<ObjectProducts> productsToDisplay = new ArrayList<ObjectProducts>();

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

        View squareInventoryState = (View) findViewById(R.id.squareInventoryState);
        squareInventoryState.setVisibility(View.VISIBLE);
        squareInventoryState.setBackgroundColor(Methods.giveColor(squareInventoryState, "indicator_doing"));

        //Category name retrieved from the previous screen
        final TextView title = (TextView) findViewById(R.id.txtTitle);
        Intent intent = getIntent();
        final String category = intent.getStringExtra("categoryName");
        title.setText(category);

// Set onClickListener to button "Previous"
        Button buttonPrevious = (Button) findViewById(R.id.buttonPrevious);
        buttonPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Category.class);
                int nbItems = ObjectsLists.getCategoryList().size();
                //TODO GéRER AVEC ID AVEC VRAIES DONNEES
                //intent.putExtra("position", (position+nbItems-1)%nbItems);
                startActivity(intent);
                finish();

            }
        });

        // Set onClickListener to button "Next"
        Button buttonNext = (Button) findViewById(R.id.buttonNext);
        buttonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Category.class);
                int nbItems = ObjectsLists.getCategoryList().size();
                //TODO GéRER AVEC ID AVEC VRAIES DONNEES
                //intent.putExtra("position", (position + nbItems + 1) % nbItems);
                startActivity(intent);
                finish();
            }
        });


// Fill the ListView with products
        ListView lvProducts = (ListView) findViewById(R.id.lvProducts);
        View header = (View) getLayoutInflater().inflate(R.layout.product_row_header, null);
        lvProducts.addHeaderView(header);
        productsToDisplay =  Methods.getObjectsListbyCategory(category);
        final ArrayAdapter adapter = new ProductsAdapter(this, productsToDisplay);
        lvProducts.setAdapter(adapter);

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

//TODO CHANGE WITH DATA
                        for (int i = 0; i < ObjectsLists.getCategoryList().size(); i++) {

                            if (ObjectsLists.getCategoryList().get(i).getName().equals(categoryName)) {

                                ObjectsLists.getCategoryList().get(i).setName(userEntry.getText().toString());
                            }
                        }
                        title.setText(userEntry.getText().toString());
                        adapter.notifyDataSetChanged();
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

                        ArrayList<ObjectCategories> categories = ObjectsLists.getCategoryList();

                        for (int i = 0; i <categories.size() ; i++) {

                            if (categories.get(i).getName().equals(category)){

                                ObjectsLists.getCategoryList().remove(i);
                            }
                        }

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
