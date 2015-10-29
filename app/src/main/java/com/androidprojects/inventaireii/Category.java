package com.androidprojects.inventaireii;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.TextView;

import java.util.ArrayList;

public class Category extends AppCompatActivity {

    ObjectsLists objectsLists = new ObjectsLists();
    Button btnAdd;
    Button btnModify;
    Button btnDelete;
    PopupWindow popupWindow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_my_products);


        //Category name retrieved from the previous screen
        TextView title = (TextView) findViewById(R.id.txtTitle);
        Intent intent = getIntent();
        final String category = intent.getStringExtra("categoryName");
        title.setText(category);


        btnModify = (Button) findViewById(R.id.buttonModify);
        btnModify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), CategoryModify.class);
                startActivity(intent);
            }
        });

        btnDelete = (Button) findViewById(R.id.buttonDelete);
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

                        ArrayList<ObjectCategories> categories = objectsLists.getCategoryList();

                        for (int i = 0; i <categories.size() ; i++) {

                            if (categories.get(i).getName().equals(category)){

                                objectsLists.getCategoryList().remove(i);
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
}
