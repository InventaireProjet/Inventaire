package com.androidprojects.inventaireii;

import android.os.AsyncTask;
import android.util.Log;

import com.example.myapplication.backend.objectCategoriesApi.model.ObjectCategories;
import com.example.myapplication.backend.objectProductsApi.ObjectProductsApi;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.google.api.client.googleapis.services.AbstractGoogleClientRequest;
import com.google.api.client.googleapis.services.GoogleClientRequestInitializer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.example.myapplication.backend.objectCategoriesApi.ObjectCategoriesApi;

public class EndpointsAsyncTask extends AsyncTask<Void, Void, List<ObjectCategories>> {
    private static final String TAG = EndpointsAsyncTask.class.getName();
    private static ObjectCategoriesApi objectCategoriesApi = null;
    private static ObjectCategoriesApi objectProductsApi = null;
    private static ObjectCategoriesApi objectStockApi = null;
    private static ObjectCategoriesApi objectWarehouseApi = null;
    private ObjectCategories objectCategories;
    private ObjectProducts objectProducts;
    private ObjectStock objectStock;
    private ObjectWarehouse objectWarehouse;



    EndpointsAsyncTask(){}

    EndpointsAsyncTask(ObjectCategories objectCategories){
        this.objectCategories = objectCategories;
    }

    @Override
    protected List<ObjectCategories> doInBackground(Void... params) {

        if(objectCategoriesApi == null){
            // Only do this once
            ObjectCategoriesApi.Builder builder = new ObjectCategoriesApi.Builder(AndroidHttp.newCompatibleTransport(),
                    new AndroidJsonFactory(), null)
                    // options for running against local devappserver
                    // - 10.0.2.2 is localhost's IP address in Android emulator
                    // - turn off compression when running against local devappserver
                    // if you deploy on the cloud backend, use your app name
                    // such as https://<your-app-id>.appspot.com
                    .setRootUrl("https://inventaireii.appspot.com")
                    .setGoogleClientRequestInitializer(new GoogleClientRequestInitializer() {
                        @Override
                        public void initialize(AbstractGoogleClientRequest<?> abstractGoogleClientRequest) throws IOException {
                            abstractGoogleClientRequest.setDisableGZipContent(true);
                        }
                    });
            objectCategoriesApi = builder.build();

        }

        try{
            // Call here the wished methods on the Endpoints
            // For instance insert
            if(objectCategories != null){
                objectCategoriesApi.insert(objectCategories).execute();
                Log.i(TAG, "insert employee");
            }
            // and for instance return the list of all employees
            return objectCategoriesApi.list().execute().getItems();

        } catch (IOException e){
            Log.e(TAG, e.toString());
            return new ArrayList<ObjectCategories>();
        }
    }






   /* EndpointsAsyncTask(){}

    @Override
    protected Object doInBackground(Object[] params) {

        this.objectCategories = new ObjectCategories();

        insertCategories();

        return null;
    }

    EndpointsAsyncTask(ObjectCategories objectCategories){
        this.objectCategories = objectCategories;
    }



    private void insertCategories(){
        if(objectCategoriesApi == null){
            // Only do this once
            ObjectCategoriesApi.Builder builder = new ObjectCategoriesApi.Builder(AndroidHttp.newCompatibleTransport(),
                    new AndroidJsonFactory(), null)
                    // options for running against local devappserver
                    // - 10.0.2.2 is localhost's IP address in Android emulator
                    // - turn off compression when running against local devappserver
                    // if you deploy on the cloud backend, use your app name
                    // such as https://<your-app-id>.appspot.com
                    .setRootUrl("https://inventaireii.appspot.com/_ah/api#p/")
                    .setGoogleClientRequestInitializer(new GoogleClientRequestInitializer() {
                        @Override
                        public void initialize(AbstractGoogleClientRequest<?> abstractGoogleClientRequest) throws IOException {
                            abstractGoogleClientRequest.setDisableGZipContent(true);
                        }
                    });
            objectCategoriesApi = builder.build();
        }

        try{

            // Call here the wished methods on the Endpoints
            // For instance insert
            if(objectCategories != null){
                objectCategoriesApi.insert(objectCategories).execute();
                Log.i(TAG, "insert objectCategories");
            }
            // and for instance return the list of all objectCategoriess

        } catch (IOException e){
            Log.e(TAG, e.toString());
        }
    }

   /* private void insertProducts(){
        if(objectProductsApi == null){
            // Only do this once
            ObjectProductsApi.Builder builder2 = new ObjectProductsApi.Builder(AndroidHttp.newCompatibleTransport(),
                    new AndroidJsonFactory(), null)
                    // options for running against local devappserver
                    // - 10.0.2.2 is localhost's IP address in Android emulator
                    // - turn off compression when running against local devappserver
                    // if you deploy on the cloud backend, use your app name
                    // such as https://<your-app-id>.appspot.com
                    .setRootUrl("http://10.0.2.2:8080/_ah/api/")
                    .setGoogleClientRequestInitializer(new GoogleClientRequestInitializer() {
                        @Override
                        public void initialize(AbstractGoogleClientRequest<?> abstractGoogleClientRequest) throws IOException {
                            abstractGoogleClientRequest.setDisableGZipContent(true);
                        }
                    });
            objectProductsApi = builder.build();
        }

        try{

            // Call here the wished methods on the Endpoints
            // For instance insert
            if(objectProducts != null){
                objectProductsApi.insert(objectProducts).execute();
                Log.i(TAG, "insert objectProducts");
            }
            // and for instance return the list of all objectCategoriess

        } catch (IOException e){
            Log.e(TAG, e.toString());
        }
    }*/
}
