package com.androidprojects.inventaireii;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class Product extends AppCompatActivity {
    ObjectCategories category;
    ObjectProducts product;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);

        // Get the product from the Intent
        Intent intent = getIntent();
        category = new ObjectCategories(intent.getStringExtra("categoryColor"),
                intent.getStringExtra("categoryName"), intent.getStringExtra("categoryInventoryState"));
        String productArtNb = intent.getStringExtra("productArtNb");
        String productName = intent.getStringExtra("productName");
        String productQty = intent.getStringExtra("productQuantity");
        int productQuantity = 0; //Integer.parseInt(productQty);
        double productPrice = 12.50; // Double.parseDouble(intent.getStringExtra("productPrice"));
        String productSquare = intent.getStringExtra("productSquare");
        product = new ObjectProducts(productArtNb, productName, category,
                productQuantity, productPrice, productSquare);

        // TODO suppress those FAKE VALUES for stocks :
        ObjectWarehouse biblio = new ObjectWarehouse( "Bibliothèque Elite", 0, 0, 0, "021 903 02 60", "Route des pives", "4c", "9876", "Ici", "Suisse");
        ObjectWarehouse armoire = new ObjectWarehouse( "Armoire", 0, 0, 0, "021 903 02 60", "Route des pives", "4c", "9876", "Ici", "Suisse");
        ObjectStock stockBiblio = new ObjectStock(35000, false, product, biblio);
        ObjectStock stockArmoire = new ObjectStock(1, true, product, armoire);
        product.addStock(stockBiblio);
        product.addStock(stockArmoire);
        product.setDescription("Balle jaune et rouge à pois verts dotée d'un effet retro");


        // Set values in top of screen
        TextView txtTitle = (TextView) findViewById(R.id.txtTitle);
        TextView txtArtNb = (TextView) findViewById(R.id.txtArtNb);
        TextView txtCategory = (TextView) findViewById(R.id.txtCategory);
        TextView txtPrice = (TextView) findViewById(R.id.txtPrice);
        txtTitle.setText(product.getName());
        txtArtNb.setText("N° Article : " + product.getArtNb());
        txtCategory.setText("Catégorie : " + product.getCategory().getName());
        txtPrice.setText("Prix : " + product.getPrice() + "CHF");

        // Fill the ListView

        // Set Values below the ListView
        TextView txtValueTotalStock = (TextView) findViewById(R.id.txtValueTotalStock);
        TextView txtValueTotalValue = (TextView) findViewById(R.id.txtValueStockValue);
        TextView txtDescription = (TextView) findViewById(R.id.txtDescription);
        txtValueTotalStock.setText(Integer.toString(product.getQuantity()));
        txtValueTotalValue.setText(Double.toString(product.getQuantity() * product.getPrice()));
        txtDescription.setText(product.getDescription());



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_product, menu);
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
}
