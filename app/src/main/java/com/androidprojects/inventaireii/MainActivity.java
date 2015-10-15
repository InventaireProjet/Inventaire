package com.androidprojects.inventaireii;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    // Données provisoires :
    boolean inventoryIsRunning = true;
    int nbItems = 56;
    int nbInventoredItems = 18;

    // Declarations
    TextView txtInventoryRunning;
    TextView txtInventoryState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
}
