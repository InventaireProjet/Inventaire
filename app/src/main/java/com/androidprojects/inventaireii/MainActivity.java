package com.androidprojects.inventaireii;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    // Fake data :
    boolean inventoryIsRunning = true;
    int nbItems = 0;
    int nbInventoredItems = 0;

    ArrayList<ObjectCategories> categoriesList = new ArrayList<>();
    ArrayList<ObjectProducts> productsList = new ArrayList<>();
    ArrayList<ObjectStock> stocksList = new ArrayList<>();
    ArrayList<ObjectWarehouse> warehousesList = new ArrayList<>();


    // Declarations
    TextView txtInventoryRunning;
    TextView txtInventoryState;
    TextView txtProductAccess;
    TextView txtCategoryAccess;
    TextView txtWarehouseAccess;
    PopupWindow popupWindow;
    Button buttonStartInventory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // TODO suppress those Fake values
        ObjectCategories medical = new ObjectCategories("", "Médical", "done");
        ObjectCategories jeux = new ObjectCategories("", "Jeux", "doing");
        ObjectProducts sonotone = new ObjectProducts("1235-1", "Sonotone Arfid", medical, 200, 350.0, "todo");
        ObjectProducts balle = new ObjectProducts("46454-9", "Balle", jeux, 3500, 22.00, "doing");
        ObjectWarehouse biblio = new ObjectWarehouse("Bibliothèque Elite", 0, 0, 0, "021 903 02 60", "Route des pives", "4c", "9876", "Ici", "Suisse");
        ObjectWarehouse armoire = new ObjectWarehouse("Armoire", 0, 0, 0, "021 903 02 60", "Route des pives", "4c", "9876", "Ici", "Suisse");
        ObjectStock stockBiblio = new ObjectStock(35000, false, sonotone, biblio);
        ObjectStock stockArmoire = new ObjectStock(1, true, balle, armoire);
        sonotone.addStock(stockBiblio);
        balle.addStock(stockArmoire);
        balle.setDescription("Balle jaune et rouge à pois verts dotée d'un effet retro");
        sonotone.setDescription("Très joli sonotone, facile d'emploi, vraiment très facile. Le tout pour un coût très modique");

        productsList.add(sonotone);
        productsList.add(balle);

        categoriesList.add(medical);
        categoriesList.add(jeux);

        warehousesList.add(armoire);
        warehousesList.add(biblio);

        stocksList.add(stockArmoire);
        stocksList.add(stockBiblio);

        ObjectsLists.setProductList(productsList);
        ObjectsLists.setCategoryList(categoriesList);
        ObjectsLists.setStockList(stocksList);
        ObjectsLists.setWarehouseList(warehousesList);

        // Display of Inventory-depending fields
        for (ObjectStock stock : ObjectsLists.getStockList()){
            nbItems ++;
            if (stock.isControlled())
                nbInventoredItems ++;
        }
        txtInventoryRunning = (TextView) findViewById(R.id.txtInventoryRunning);
        txtInventoryState = (TextView) findViewById(R.id.txtInventoryState);
        if (inventoryIsRunning) {
            txtInventoryRunning.setVisibility(View.VISIBLE);
            txtInventoryState.setText("Avancement de votre inventaire : "
                    + nbInventoredItems + "/" + nbItems);
            txtInventoryState.setVisibility(View.VISIBLE);
        }
        else {
            txtInventoryRunning.setVisibility(View.INVISIBLE);
            txtInventoryState.setVisibility(View.INVISIBLE);
        }

        // Access by product
        txtProductAccess = (TextView) findViewById(R.id.txtProductAccess);
        txtProductAccess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), MyProducts.class);
                startActivity(intent);
            }
        });

        // Access by category
        txtCategoryAccess = (TextView) findViewById(R.id.txtCategoryAccess);
        txtCategoryAccess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), MyCategories.class);
                startActivity(intent);
            }
        });

        // Access by warehouse
        txtWarehouseAccess = (TextView) findViewById(R.id.txtWarehouseAccess);
        txtWarehouseAccess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), MyWarehouses.class);
                startActivity(intent);
            }
        });

        // Pop-up Window on click on 'New Inventory'
        buttonStartInventory = (Button) findViewById(R.id.buttonStartInventory);
        buttonStartInventory.setOnClickListener(new Button.OnClickListener() {

            @Override
            public void onClick(View v) {

                // Create the pop-up window
                LayoutInflater layoutInflater =
                        (LayoutInflater) getBaseContext().getSystemService(LAYOUT_INFLATER_SERVICE);
                View popupView =
                        layoutInflater.inflate(R.layout.activity_popup_ok_cancel, null);
                popupWindow = new PopupWindow(popupView, ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT);

                // Catch the elements of the pop-up view
                TextView txtQuestion = (TextView) popupView.findViewById(R.id.txtQuestion);
                Button buttonOk = (Button) popupView.findViewById(R.id.buttonOk);
                Button buttonCancel = (Button) popupView.findViewById(R.id.buttonCancel);

                // Set actions to the elements
                txtQuestion.setText("Êtes-vous sûr de vouloir lancer l'inventaire ?\n\n" +
                        "Si oui le processus d'inventaire sera remis à zéro et tous vos" +
                        "produits apparaîtront comme étant non inventoriés.");

                buttonOk.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        restartInventory();
                        popupWindow.dismiss();
                    }
                });

                buttonCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        popupWindow.dismiss();
                    }
                });

                popupWindow.showAsDropDown(buttonStartInventory, 0, -100);

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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

    private void restartInventory() {
        // TODO
        nbInventoredItems = 0;
        for (ObjectStock stock : ObjectsLists.getStockList()){
            stock.setControlled(false);
        }

        txtInventoryState.setText("Avancement de votre inventaire : "
                + nbInventoredItems + "/" + nbItems);
        txtInventoryState.setVisibility(View.VISIBLE);

    }
}
