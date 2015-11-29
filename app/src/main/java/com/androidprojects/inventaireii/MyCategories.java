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
import android.widget.Toast;

import com.androidprojects.inventaireii.Preferences.AppSettingsActivity;
import com.androidprojects.inventaireii.db.adapter.CategoryDataSource;
import com.androidprojects.inventaireii.db.adapter.ProductDataSource;

import java.util.List;

public class MyCategories extends AppCompatActivity {

    PopupWindow popupWindow;
    Button addButton;
    ArrayAdapter adapter;
    View tvSquare;
    List <ObjectProducts> productsInCategory;
    CategoryDataSource categoryDataSource;
    ProductDataSource productDataSource;
    ListView lvCategories;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_action_bar, menu);
        menu.findItem(R.id.goto_categories).setVisible(false);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        int id = item.getItemId();

        Intent intent;

        switch (id) {

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


            case R.id.goto_warehouses:
                intent = new Intent(getBaseContext(), MyWarehouses.class);
                startActivity(intent);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onResume() {
        super.onResume();

        //Refresh the language
        Methods.setLocale(this);
        getSupportActionBar().setTitle(R.string.my_categories);

        setContentView(R.layout.activity_my_categories);

        categoryDataSource =CategoryDataSource.getInstance(this);
        productDataSource = ProductDataSource.getInstance(this);


        //Using adapter
        adapter = new CategoriesAdapter(this, categoryDataSource.getAllCategories());


        // Fill the ListView
        lvCategories = (ListView) findViewById(R.id.lvCategories);
        lvCategories.setAdapter(adapter);


        //Adding Categories
        addButton = (Button) findViewById(R.id.btnAdd);
        addButton.setOnClickListener(new Button.OnClickListener() {

            @Override
            public void onClick(View v) {

                LayoutInflater layoutInflater =
                        (LayoutInflater) getBaseContext().getSystemService(LAYOUT_INFLATER_SERVICE);

                View popupView = layoutInflater.inflate(R.layout.new_category_popup, null);
                popupWindow = new PopupWindow(popupView, ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT);

                //To view the keyboard
                popupWindow.setFocusable(true);



                // Catch the elements of the pop-up view
                Button buttonValidate = (Button) popupView.findViewById(R.id.buttonValidate);
                Button buttonCancel = (Button) popupView.findViewById(R.id.buttonCancel);
                final EditText userEntry = (EditText) popupView.findViewById(R.id.userEntry);


                //Saving the new category by validating the text entry
                buttonValidate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (userEntry.getText().toString().equals("")) {
                            Toast toast = Toast.makeText(getBaseContext(), R.string.no_categoryName, Toast.LENGTH_LONG);
                            toast.show();
                        } else {
                            ObjectCategories newCategory = new ObjectCategories("done", userEntry.getText().toString(), "0/0");

                            categoryDataSource.createCategory(newCategory);
                            finish();
                            popupWindow.dismiss();
                            startActivity(new Intent(getBaseContext(), MyCategories.class));

                        }
                    }
                });

                buttonCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        popupWindow.dismiss();
                    }
                });

                popupWindow.showAsDropDown(addButton, 0, -100);

            }
        });

    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        invalidateOptionsMenu();
        return true;
    }

    private class CategoriesAdapter extends ArrayAdapter {
        // Ref: https://github.com/codepath/android_guides/wiki/Using-an-ArrayAdapter-with-ListView


        public CategoriesAdapter(Context context, List<ObjectCategories> categories) {
            super(context, 0, categories);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final ObjectCategories category = (ObjectCategories) getItem(position);

            //Creation of the link with the categories_row layout if not already existing
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.categories_row, parent, false);
            }

            //Effective linking with the categories_row layout
            tvSquare = convertView.findViewById(R.id.square);
            TextView tvName = (TextView) convertView.findViewById(R.id.name);
            TextView tvState = (TextView) convertView.findViewById(R.id.inventoryState);

            //Data to display are retrieved
            tvName.setText(category.getName());



            //Retrieving the products in the category to know which color to display
            productsInCategory = productDataSource.getAllProductsByCategory(category.getId());
            tvSquare.setBackgroundColor(Methods.giveColor(tvSquare, Methods.getInventoryState(productsInCategory)));

            //Setting and displaying the inventory state
            int inventoriedObjects = Methods.getNumberOfInventoried(productsInCategory);
            int numberOfProducts = Methods.getNumberObjects(productsInCategory);
            category.setInventoryState(inventoriedObjects +"/" +numberOfProducts);
            tvState.setText(category.getInventoryState());

            //Sending the category name to the next screen

            tvName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getBaseContext(), Category.class);
                    int categoryId = category.getId();
                    intent.putExtra("categoryId",categoryId);
                    startActivity(intent);
                }
            });

            return convertView;
        }
    }
}