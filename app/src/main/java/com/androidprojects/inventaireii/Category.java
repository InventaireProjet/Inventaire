package com.androidprojects.inventaireii;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class Category extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        TextView title = (TextView) findViewById(R.id.txtTitle);
        Intent intent = getIntent();
        String category = intent.getStringExtra("categoryName");
        title.setText(category);
    }
}
