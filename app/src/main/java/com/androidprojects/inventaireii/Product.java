package com.androidprojects.inventaireii;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.androidprojects.inventaireii.db.adapter.ProductDataSource;

import java.util.List;

public class Product extends AppCompatActivity {
    ObjectCategories category;
    ObjectProducts product;
    int productId;
    ArrayAdapter adapter;
    View squareTotalStock;
    ProductDataSource productDataSource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);
        productDataSource = new ProductDataSource(this);

        // Get the product from the Intent
        Intent intent = getIntent();
        productId = intent.getIntExtra("position", 1);

        // TODO suppress fake values
        //product = ObjectsLists.getProductList().get(productId);
        product = productDataSource.getProductById(productId);

        // Set values in top of screen
        TextView txtTitle = (TextView) findViewById(R.id.txtTitle);
        TextView txtArtNb = (TextView) findViewById(R.id.txtArtNb);
        TextView txtCategory = (TextView) findViewById(R.id.txtCategory);
        TextView txtPrice = (TextView) findViewById(R.id.txtPrice);
        txtTitle.setText(product.getName());
        txtArtNb.setText("N° Article : " + product.getArtNb());
        if (product.getCategory() != null)
            txtCategory.setText("Catégorie : " + product.getCategory().getName());
        else
            txtCategory.setText(R.string.no_category);
        txtPrice.setText("Prix : " + String.format("%,.2f", product.getPrice()) + " CHF");

        // Set onClickListener to button "All controlled"
        Button buttonAllControlled = (Button) findViewById(R.id.buttonAllControlled);
        buttonAllControlled.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Set all stocks to controlled
                for (ObjectStock s : product.getStocks()) {
                    s.setControlled(true);
                }
                adapter.notifyDataSetChanged();
                squareTotalStock.setBackgroundColor(giveColor(Methods.getInventoryState(product)));
            }
        });

        // Set onClickListener to button "Previous"
        Button buttonPrevious = (Button) findViewById(R.id.buttonPrevious);
        buttonPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Product.class);
                //TODO int nbItems = ObjectsLists.getProductList().size();
                // TODO créer la requête SQL
                int nbItems = productDataSource.getAllProducts().size();
                nbItems = productDataSource.getCountProduct();
                intent.putExtra("position", (productId+nbItems-1)%nbItems);
                startActivity(intent);
                finish();

            }
        });

        // Set onClickListener to button "Next"
        Button buttonNext = (Button) findViewById(R.id.buttonNext);
        buttonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Product.class);
                int nbItems = ObjectsLists.getProductList().size();
                intent.putExtra("position", (productId+nbItems+1)%nbItems);
                startActivity(intent);
                finish();
            }
        });

        // Set onClickListener to button "Add Stock"
        Button buttonAddStock = (Button) findViewById(R.id.buttonAddStock);
        buttonAddStock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), StockNewOrModify.class);
                intent.putExtra("stockPosition" , -1);
                intent.putExtra("productPosition", productId);
                startActivity(intent);
            }
        });

        // Fill the ListView
        ListView lvStock = (ListView) findViewById(R.id.lvStocks);
        View header = (View) getLayoutInflater().inflate(R.layout.stock_row_header, null);
        lvStock.addHeaderView(header);
        adapter = new StocksAdapter(this, product.getStocks());
        lvStock.setAdapter(adapter);

        // Set Values below the ListView
        TextView txtValueTotalStock = (TextView) findViewById(R.id.txtValueTotalStock);
        TextView txtValueTotalValue = (TextView) findViewById(R.id.txtValueStockValue);
        final TextView txtDescription = (TextView) findViewById(R.id.txtDescription);
        txtValueTotalStock.setText(Integer.toString(product.getQuantity()));
        double stockValue = product.getQuantity() * product.getPrice();
        txtValueTotalValue.setText(String.format("%,.2f", stockValue));
        txtDescription.setText(product.getDescription());

        // Set color of square below the ListView
        squareTotalStock = (View) findViewById(R.id.squareTotalStock);
        squareTotalStock.setBackgroundColor(giveColor(Methods.getInventoryState(product)));

        // Set listener to Button "MODIFY"
        Button buttonModify = (Button) findViewById(R.id.buttonModify);
        buttonModify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), ProductNewOrModify.class);
                intent.putExtra("position", productId);
                startActivity(intent);
            }
        });

        // Set listener to Button "SUPPRESS"
        final Button buttonSuppress = (Button) findViewById(R.id.buttonSuppress);
        buttonSuppress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Create a pop-up window for confirmation of suppression
                LayoutInflater layoutInflater =
                        (LayoutInflater) getBaseContext().getSystemService(LAYOUT_INFLATER_SERVICE);
                View popupView =
                        layoutInflater.inflate(R.layout.activity_popup_ok_cancel, null);
                final PopupWindow popupWindow = new PopupWindow(popupView,
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT);

                // Catch the elements of the pop-up Window
                TextView txtQuestion = (TextView) popupView.findViewById(R.id.txtQuestion);
                Button buttonOk = (Button) popupView.findViewById(R.id.buttonOk);
                Button buttonCancel = (Button) popupView.findViewById(R.id.buttonCancel);

                // Set actions to the elements of the pop up window
                txtQuestion.setText("Êtes-vous sûr de vouloir supprimer cet article ?");
                buttonOk.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // suppress all stocks linked with the product
                        for (ObjectStock s : product.getStocks()) {
                            ObjectsLists.getStockList().remove(s);
                            product.removeStock(s);
                        }
                        ObjectsLists.getProductList().remove(product);
                        popupWindow.dismiss();
                        Toast toast = Toast.makeText(getBaseContext(),
                                "Produit supprimé", Toast.LENGTH_LONG);

                        finish();
                    }
                });
                buttonCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        popupWindow.dismiss();
                    }
                });

                popupWindow.showAsDropDown(txtDescription, 0, -100);

            }
        });
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
            Intent intent = new Intent(getBaseContext(), c);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    private int giveColor(String s) {
        if(s.equals("todo"))
            return getResources().getColor(R.color.indicator_to_do);
        if(s.equals("done"))
            return getResources().getColor(R.color.indicator_done);
        return getResources().getColor(R.color.indicator_doing);
    }

    private int giveColor(Boolean controlled) {
        if(controlled)
            return giveColor("done");

        return giveColor("todo");
    }

    @Override
    protected void onResume() {
        super.onResume();
        adapter.notifyDataSetChanged();
        squareTotalStock.setBackgroundColor(giveColor(Methods.getInventoryState(product)));
    }

    private class StocksAdapter extends ArrayAdapter {

        public StocksAdapter(Context context, List<ObjectStock> stocksList) {
            super(context, 0, stocksList);
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            final ObjectStock stock = (ObjectStock) getItem(position);

            // Creation of the link with the stock_row layout if not already existing
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext())
                        .inflate(R.layout.stock_row, parent, false);
            }

            // Effective linking with the stock_row layout
            View square = convertView.findViewById(R.id.square);
            TextView txtWarehouse = (TextView) convertView.findViewById(R.id.warehouse);
            TextView txtQuantity = (TextView) convertView.findViewById(R.id.quantity);

            // Fill with data
            square.setBackgroundColor(giveColor(stock.isControlled()));
            txtWarehouse.setText(stock.getWarehouse().getName());
            txtQuantity.setText(Integer.toString(stock.getQuantity()));

            // Modify the stock
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getBaseContext(), StockNewOrModify.class);
                    intent.putExtra("stockPosition", ObjectsLists.getStockList().indexOf(stock));
                    intent.putExtra("productPosition", position);
                    startActivity(intent);
                }
            });

            return convertView;
        }
    }
}
