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

public class MainActivity extends AppCompatActivity {
    // Données provisoires :
    boolean inventoryIsRunning = true;
    int nbItems = 56;
    int nbInventoredItems = 18;

    // Declarations
    TextView txtInventoryRunning;
    TextView txtInventoryState;
    TextView txtProductAccess;
    TextView txtCategoryAccess;
    PopupWindow popupWindow;
    Button buttonStartInventory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Display of Inventory-depending fields
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
        txtInventoryState.setText("Avancement de votre inventaire : "
                + nbInventoredItems + "/" + nbItems);
        txtInventoryState.setVisibility(View.VISIBLE);

    }
}
