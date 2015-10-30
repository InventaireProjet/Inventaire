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

public class MyCategories extends AppCompatActivity {

    PopupWindow popupWindow;
    Button addButton;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_categories);

        if (ObjectsLists.getCategoryList().size() == 0)
        {
            //Fake data
            ObjectCategories games = new ObjectCategories("done", "Jeux", "1/1");
            ObjectCategories fishing = new ObjectCategories("doing", "Pêche", "6/8");
            ObjectCategories medical = new ObjectCategories("todo", "Médical", "0/36");
            ObjectCategories biology = new ObjectCategories("done", "Biologie", "11/11");



            // Adding objects to the list
            ObjectsLists.getCategoryList().add(games);
            ObjectsLists.getCategoryList().add(fishing);
            ObjectsLists.getCategoryList().add(medical);
            ObjectsLists.getCategoryList().add(biology);
        }

        //Using adapter
        final ArrayAdapter adapter = new CategoriesAdapter(this, ObjectsLists.getCategoryList());


        // Fill the ListView
        ListView lvCategories = (ListView) findViewById(R.id.lvCategories);
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

                //Um die Tastatur zu sehen
                popupWindow.setFocusable(true);

                // Catch the elements of the pop-up view
                Button buttonValidate = (Button) popupView.findViewById(R.id.buttonValidate);
                Button buttonCancel = (Button) popupView.findViewById(R.id.buttonCancel);
                final EditText userEntry = (EditText) popupView.findViewById(R.id.userEntry);



                //Saving the new category by validating the text entry
                buttonValidate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ObjectCategories newCategory = new ObjectCategories("done", userEntry.getText().toString(), "0/0");
                        ObjectsLists.getCategoryList().add(newCategory);
                        popupWindow.dismiss();

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

    private int giveColor(String s) {
        if(s.equals("todo"))
            return getResources().getColor(R.color.indicator_to_do);
        if(s.equals("done"))
            return getResources().getColor(R.color.indicator_done);
        return getResources().getColor(R.color.indicator_doing);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_my_categories, menu);
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

    private class CategoriesAdapter extends ArrayAdapter {
        // Ref: https://github.com/codepath/android_guides/wiki/Using-an-ArrayAdapter-with-ListView


        public CategoriesAdapter(Context context,ArrayList <ObjectCategories>  categories) {
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
            View tvSquare = convertView.findViewById(R.id.square);
            TextView tvName = (TextView) convertView.findViewById(R.id.name);
            TextView tvState = (TextView) convertView.findViewById(R.id.inventoryState);

            //Data to display are retrieved
            tvName.setText(category.getName());
            tvSquare.setBackgroundColor(giveColor(category.getColor()));
            tvState.setText(category.getInventoryState());

            //Sending the category name to the next screen

            tvName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getBaseContext(), Category.class);
                    String categoryName = category.getName();
                    intent.putExtra( "categoryName",categoryName);
                    startActivity(intent);
                }
            });

            return convertView;
        }
    }




}
