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
import android.widget.ListView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class MyProducts extends AppCompatActivity {
    // Données provisoires
    private List<ObjectProducts> productsList = new ArrayList<ObjectProducts>();
    public List<ObjectProducts> getProductsList(){
        return productsList;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_products);

        // Créer les données provisoires
        ObjectCategories medical = new ObjectCategories("", "Médical", "done");
        ObjectCategories jeux = new ObjectCategories("", "Jeux", "doing");
        ObjectProducts sonotone = new ObjectProducts("1235-1", "Sonotone Arfid", medical, 200, 350.0, "todo");
        ObjectProducts balle = new ObjectProducts("46454-9", "Balle", jeux, 3500, 22.00, "doing");
        productsList.add(sonotone);
        productsList.add(balle);

        // Fill the ListView
        ListView lvProducts = (ListView) findViewById(R.id.lvProducts);
        View header = (View) getLayoutInflater().inflate(R.layout.product_row_header, null);
        lvProducts.addHeaderView(header);
        lvProducts.setAdapter(new ProductsAdapter(this, productsList));


        // Total quantity of products and value of stock
        int totalQuantity = 0;
        double totalValue = 0.0;
        for (ObjectProducts product : productsList) {
            totalQuantity += product.getQuantity();
            totalValue += product.getPrice()*product.getQuantity();
        }
        TextView txtStock = (TextView) findViewById(R.id.txtStock);
        TextView txtStockValue = (TextView) findViewById(R.id.txtStockValue);
        txtStock.setText("Stock : " + Integer.toString(totalQuantity));
        txtStockValue.setText("Valeur : CHF " + Double.toString(totalValue));

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
        getMenuInflater().inflate(R.menu.menu_my_products, menu);
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

    private class ProductsAdapter extends ArrayAdapter {
        // Ref toute pourrie: http://stackoverflow.com/questions/11281952/listview-with-customized-row-layout-android
        // TODO suppress public ProductsAdapter() { super(MyProducts.this, R.layout.product_row);}
        public ProductsAdapter(Context context, List<ObjectProducts> productsList) {
            super(context, 0, productsList);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

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
            square.setBackgroundColor(giveColor(product.getInventoryState()));
            txtArtNb.setText(product.getArtNb());
            txtName.setText(product.getName());
            txtCategory.setText(product.getCategory().getName());
            txtQuantity.setText(Integer.toString(product.getQuantity()));
            txtPrice.setText("CHF " + Double.toString(product.getPrice()));

            // Sending the product to the next field
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getBaseContext(), Product.class);
                    // TODO in the final version, send only the product Id...
                    intent.putExtra("productName", product.getName());
                    intent.putExtra("productSquare", product.getInventoryState());
                    intent.putExtra("productArtNb", product.getArtNb());
                    intent.putExtra("productQuantity", Integer.toString(product.getQuantity()));
                    intent.putExtra("productPrice", Double.toString(product.getPrice()));
                    intent.putExtra("categoryName", product.getCategory().getName());
                    intent.putExtra("categoryInventoryState", product.getCategory().getInventoryState());
                    intent.putExtra("category", product.getCategory().getColor());
                    startActivity(intent);
                }
            });

            return convertView;

        }
    }
}
