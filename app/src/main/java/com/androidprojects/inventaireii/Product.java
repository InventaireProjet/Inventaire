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
import com.androidprojects.inventaireii.db.adapter.StockDataSource;

import java.util.List;

public class Product extends AppCompatActivity {
    ObjectProducts product;
    int productId;
    int nbProducts;

    ArrayAdapter adapter;
    ProductDataSource productDataSource;
    StockDataSource stockDataSource;

    // Declaration of the views
    View squareTotalStock;
    TextView txtTitle;
    TextView txtArtNb;
    TextView txtCategory;
    TextView txtPrice;
    TextView txtQuantityStorage;
    TextView txtDescriptionTitle;
    ListView lvStock;
    View header;
    TextView txtLabelTotalStock;
    TextView txtLabelTotalValue;
    TextView txtValueTotalStock;
    TextView txtValueTotalValue;
    TextView txtDescription;
    Button buttonModify;
    Button buttonSuppress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Methods.setLocale(this);
        setContentView(R.layout.activity_product);

        productDataSource = ProductDataSource.getInstance(this);
        stockDataSource = StockDataSource.getInstance(this);

        // Get some views
        txtQuantityStorage = (TextView) findViewById(R.id.txtQuantityStorage);
        txtDescriptionTitle = (TextView) findViewById(R.id.txtDescriptionTitle);
        txtTitle = (TextView) findViewById(R.id.txtTitle); // TODO: 29.11.2015 suppress this 4 lines ?
        txtArtNb = (TextView) findViewById(R.id.txtArtNb);
        txtCategory = (TextView) findViewById(R.id.txtCategory);
        txtPrice = (TextView) findViewById(R.id.txtPrice);

        // Get the product from the Intent
        Intent intent = getIntent();
        productId = intent.getIntExtra("position", 1);

        // Get the product. Note that we don't have to manage a productId=0, while the user have to click on a product to come here...
        product = productDataSource.getProductById(productId);

        // Set onClickListener to button "All controlled"
        Button buttonAllControlled = (Button) findViewById(R.id.buttonAllControlled);
        buttonAllControlled.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Set all stocks to controlled
                for (ObjectStock s : product.getStocks()) {
                    s.setControlled(true);
                    stockDataSource.updateStock(s);
                }
                adapter.notifyDataSetChanged();
                squareTotalStock.setBackgroundColor(
                        Methods.giveColor(squareTotalStock,
                                Methods.getInventoryState(product)));
            }
        });

        // Set onClickListener to button "Previous"
        Button buttonPrevious = (Button) findViewById(R.id.buttonPrevious);
        buttonPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Product.class);
                List<ObjectProducts> products = productDataSource.getAllProducts();
                int position = 0;
                for(ObjectProducts p : products)
                        if (p.getId() == product.getId())
                            position = products.indexOf(p);
                
                nbProducts = products.size();
                position = (position+nbProducts-1)%nbProducts;
                intent.putExtra("position", products.get(position).getId());
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
                List<ObjectProducts> products = productDataSource.getAllProducts();
                int position = 0;
                for (ObjectProducts p : products)
                    if (p.getId() == product.getId())
                        position = products.indexOf(p);
                
                nbProducts = products.size();
                position = (position+nbProducts+1)%nbProducts;
                intent.putExtra("position", products.get(position).getId());
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
                intent.putExtra("stockPosition", -1);
                intent.putExtra("productPosition", productId);
                startActivity(intent);
            }
        });

        // Fill the ListView
        lvStock = (ListView) findViewById(R.id.lvStocks);
        header = getLayoutInflater().inflate(R.layout.stock_row_header, null);
        lvStock.addHeaderView(header);
        adapter = new StocksAdapter(this, product.getStocks());
        lvStock.setAdapter(adapter);

        // Set Values below the ListView
        txtLabelTotalStock = (TextView) findViewById(R.id.txtLabelTotalStock);
        txtLabelTotalValue = (TextView) findViewById(R.id.txtLabelStockValue);
        txtValueTotalStock = (TextView) findViewById(R.id.txtValueTotalStock);
        txtValueTotalValue = (TextView) findViewById(R.id.txtValueStockValue);
        txtDescription = (TextView) findViewById(R.id.txtDescription);
        txtValueTotalStock.setText(Integer.toString(product.getQuantity()));
        double stockValue = product.getQuantity() * product.getPrice();
        txtValueTotalValue.setText(String.format("%,.2f", stockValue));
        txtDescription.setText(product.getDescription());

        // Set color of square below the ListView
        squareTotalStock = findViewById(R.id.squareTotalStock);
        squareTotalStock.setBackgroundColor(
                Methods.giveColor(squareTotalStock,
                        Methods.getInventoryState(product)));

        // Set listener to Button "MODIFY"
        buttonModify = (Button) findViewById(R.id.buttonModify);
        buttonModify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), ProductNewOrModify.class);
                intent.putExtra("position", productId);
                startActivity(intent);
            }
        });

        // Set listener to Button "SUPPRESS"
        buttonSuppress = (Button) findViewById(R.id.buttonSuppress);
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
                txtQuestion.setText(R.string.suppress_product_are_you_sure);
                buttonOk.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // suppress all stocks linked with the product
                        for (ObjectStock s : product.getStocks()) {
                            stockDataSource.deleteStock(s);
                        }

                        productDataSource.deleteProduct(product);
                        popupWindow.dismiss();
                        Toast toast = Toast.makeText(getBaseContext(),
                                R.string.product_deleted, Toast.LENGTH_LONG);
                        
                        toast.show();

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
    protected void onResume() {
        super.onResume();
        Methods.setLocale(this);

        // Set values in top of screen
        txtTitle.setText(product.getName());
        txtArtNb.setText(getResources().getString(R.string.article_number_colon) + " " + product.getArtNb());
        if (product.getCategory() != null)
            txtCategory.setText(getResources().getString(R.string.category_colon) + " " + product.getCategory().getName());
        else
            txtCategory.setText(R.string.no_category);
        txtPrice.setText(getResources().getString(R.string.price_colon) + " "
                + String.format("%,.2f", product.getPrice()) + " CHF");

        // Set value to section Quantity & Storage
        txtQuantityStorage.setText(R.string.quantity_and_storage);
        ((TextView) header.findViewById(R.id.square)).setText(R.string.inventory_shorted);
        ((TextView) header.findViewById(R.id.warehouse)).setText(R.string.warehouse);
        ((TextView) header.findViewById(R.id.quantity)).setText(R.string.quantity_short);


        // Set values in the bottom of the screen
        txtLabelTotalStock.setText(R.string.total_stock);
        txtLabelTotalValue.setText(R.string.stock_value);
        txtDescriptionTitle.setText(R.string.product_description);
        buttonModify.setText(R.string.modify);
        buttonSuppress.setText(R.string.suppress);

        adapter.notifyDataSetChanged();
        squareTotalStock.setBackgroundColor(
                Methods.giveColor(squareTotalStock,
                        Methods.getInventoryState(product)));
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

    private int giveColor(Boolean controlled) {
        if(controlled)
            return Methods.giveColor(squareTotalStock, "done");

        return Methods.giveColor(squareTotalStock, "todo");
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
                    intent.putExtra("stockPosition", stock.getId());
                    intent.putExtra("productPosition", product.getId());
                    startActivity(intent);
                }
            });

            return convertView;
        }
    }
}
