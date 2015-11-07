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
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class MyProducts extends AppCompatActivity {
    // TODO Fake values
    private List<ObjectProducts> productsList = new ArrayList<ObjectProducts>();
    private List<ObjectCategories> categoriesList = new ArrayList<>();
    public List<ObjectProducts> getProductsList(){
        return productsList;
    }
    ArrayAdapter adapter;
    View squareTotalStock;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_products);

        // TODO suppress this FAKE value :
        productsList = ObjectsLists.getProductList();

        // Set OnClickListener to Button "Add product"
        Button buttonAddProduct = (Button) findViewById(R.id.buttonAddProduct);
        buttonAddProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), ProductNewOrModify.class);
                intent.putExtra("position", -1);
                startActivity(intent);
            }
        });


        //Todo replace at the right place
        View square = (View) findViewById(R.id.squareInventoryState);
        Button buttonNext =(Button) findViewById(R.id.buttonNext);
        Button buttonPrevious = (Button) findViewById(R.id.buttonPrevious);



        // Fill the ListView
        ListView lvProducts = (ListView) findViewById(R.id.lvProducts);
        View header = (View) getLayoutInflater().inflate(R.layout.product_row_header, null);
        lvProducts.addHeaderView(header);
        adapter = new ProductsAdapter(this, productsList);
        lvProducts.setAdapter(adapter);


        // Total quantity of products and value of stock
        int totalQuantity = 0;
        double totalValue = 0.0;
        for (ObjectProducts product : productsList) {
            totalQuantity += product.getQuantity();
            totalValue += product.getPrice()*product.getQuantity();
        }
        squareTotalStock = (View) findViewById(R.id.squareTotalStock);
        TextView txtStock = (TextView) findViewById(R.id.txtStock);
        TextView txtStockValue = (TextView) findViewById(R.id.txtStockValue);
        squareTotalStock.setBackgroundColor(giveColor(getInventoryState()));
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

    private String getInventoryState() {
        int nbControlled = 0;
        int nbNotControlled = 0;
        for (ObjectProducts p : ObjectsLists.getProductList()){
            switch (Methods.getInventoryState(p)) {
                case "done": nbControlled++ ; break ;
                case "todo": nbNotControlled++ ; break;
            }
        }

        if (nbControlled == 0)
            return "todo";

        if (nbNotControlled == 0)
            return "done";

        return "doing";
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_action_bar, menu);

        // Hide the buttons we don't need
        menu.findItem(R.id.goto_products).setVisible(false);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        Class c = Methods.onOptionsItemSelected(id);
        if (c != null) {
            Intent intent = new Intent(getBaseContext(), Methods.onOptionsItemSelected(id));
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        adapter.notifyDataSetChanged();
        squareTotalStock.setBackgroundColor(giveColor(getInventoryState()));
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
            square.setBackgroundColor(giveColor(Methods.getInventoryState(product)));
            txtArtNb.setText(product.getArtNb());
            txtName.setText(product.getName());
            txtCategory.setText(product.getCategory().getName());
            txtQuantity.setText(Integer.toString(product.getQuantity()));
            txtPrice.setText("CHF " + Double.toString(product.getPrice()));

            // Sending the product to the next field
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ObjectsLists.setProductList((ArrayList) productsList);
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
