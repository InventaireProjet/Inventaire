package com.androidprojects.inventaireii;

import android.os.AsyncTask;
import android.util.Log;

import com.example.myapplication.backend.objectCategoriesApi.model.ObjectCategories;

import com.example.myapplication.backend.objectProductsApi.ObjectProductsApi;
import com.example.myapplication.backend.objectProductsApi.model.ObjectProducts;
import com.example.myapplication.backend.objectStockApi.model.ObjectStock;
import com.example.myapplication.backend.objectWarehouseApi.model.ObjectWarehouse;
import com.example.myapplication.backend.objectStockApi.ObjectStockApi;
import com.example.myapplication.backend.objectWarehouseApi.ObjectWarehouseApi;
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
    private static ObjectProductsApi objectProductsApi = null;
    private static ObjectStockApi objectStockApi = null;
    private static ObjectWarehouseApi objectWarehouseApi = null;
    private ObjectCategories objectCategories;
    private ObjectCategories objectCategories2;
    private ObjectCategories objectCategories3;
    private ObjectProducts objectProducts;
    private ObjectStock objectStock;
    private ObjectWarehouse objectWarehouse;



    EndpointsAsyncTask(){}

    EndpointsAsyncTask(ObjectStock objectStock, ObjectWarehouse objectWarehouse){
        this.objectCategories = objectCategories;
        this.objectCategories2 = objectCategories2;
        this.objectCategories3 = objectCategories3;
        this.objectProducts = objectProducts;
        this.objectStock = objectStock;
        this.objectWarehouse= objectWarehouse;
    }

    @Override
    protected List<ObjectCategories> doInBackground(Void... params) {

        /*insertCategory(objectCategories);
        insertCategory(objectCategories2);
        insertCategory(objectCategories3);
        insertProduct(objectProducts);*/
        insertWarehouse(objectWarehouse);
        insertStock(objectStock);

        return null;
    }

    private void insertCategory(ObjectCategories objectCategories) {
        if(objectCategoriesApi == null){


            ObjectCategoriesApi.Builder builder = new ObjectCategoriesApi.Builder(AndroidHttp.newCompatibleTransport(),
                    new AndroidJsonFactory(), null).setGoogleClientRequestInitializer(new GoogleClientRequestInitializer() {
                @Override
                public void initialize(AbstractGoogleClientRequest<?> abstractGoogleClientRequest) throws IOException {
                    abstractGoogleClientRequest.setDisableGZipContent(true);
                }
            });
            objectCategoriesApi = builder.build();

        }

        try{

            if(objectCategories != null){
                objectCategoriesApi.insert(objectCategories).execute();
                Log.i(TAG, "insert objectCategories");
            }


        } catch (IOException e){
            Log.e(TAG, e.toString());

        }
    }


    private void deleteCategories(ObjectCategories objectCategories) {
        if (objectCategoriesApi == null) {
            // Only do this once
            ObjectCategoriesApi.Builder builder = new ObjectCategoriesApi.Builder(AndroidHttp.newCompatibleTransport(),
                    new AndroidJsonFactory(), null)
                    .setGoogleClientRequestInitializer(new GoogleClientRequestInitializer() {
                        @Override
                        public void initialize(AbstractGoogleClientRequest<?> abstractGoogleClientRequest) throws IOException {
                            abstractGoogleClientRequest.setDisableGZipContent(true);
                        }
                    });
            objectCategoriesApi = builder.build();
        }

        try {

            // Call here the wished methods on the Endpoints
            // For instance insert
            if (objectCategories != null) {
                objectCategoriesApi.remove(objectCategories.getId()).execute();
                Log.i(TAG, "remove objectCategories");
            }

        } catch (IOException e) {
            Log.e(TAG, e.toString());
        }
    }

    private void updateCategory (ObjectCategories objectCategories) {
        if(objectCategoriesApi == null){
            // Only do this once
            ObjectCategoriesApi.Builder builder = new ObjectCategoriesApi.Builder(AndroidHttp.newCompatibleTransport(),
                    new AndroidJsonFactory(), null).setGoogleClientRequestInitializer(new GoogleClientRequestInitializer() {
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
                objectCategoriesApi.update(objectCategories.getId(), objectCategories).execute();
                Log.i(TAG, "update objectCategories");
            }


        } catch (IOException e){
            Log.e(TAG, e.toString());

        }
    }

    private void insertProduct(ObjectProducts objectProducts) {
        if(objectProductsApi == null){
            // Only do this once
            ObjectProductsApi.Builder builder = new ObjectProductsApi.Builder(AndroidHttp.newCompatibleTransport(),
                    new AndroidJsonFactory(), null).setGoogleClientRequestInitializer(new GoogleClientRequestInitializer() {
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
                Log.i(TAG, "insert objectCategories");
            }


        } catch (IOException e){
            Log.e(TAG, e.toString());

        }
    }


    private void deleteProducts(ObjectProducts objectProducts) {
        if (objectProductsApi == null) {
            // Only do this once
            ObjectProductsApi.Builder builder = new ObjectProductsApi.Builder(AndroidHttp.newCompatibleTransport(),
                    new AndroidJsonFactory(), null)
                    .setGoogleClientRequestInitializer(new GoogleClientRequestInitializer() {
                        @Override
                        public void initialize(AbstractGoogleClientRequest<?> abstractGoogleClientRequest) throws IOException {
                            abstractGoogleClientRequest.setDisableGZipContent(true);
                        }
                    });
            objectProductsApi = builder.build();
        }

        try {

            // Call here the wished methods on the Endpoints
            // For instance insert
            if (objectProducts != null) {
                objectProductsApi.remove(objectProducts.getId()).execute();
                Log.i(TAG, "remove objectProducts");
            }

        } catch (IOException e) {
            Log.e(TAG, e.toString());
        }
    }

    private void updateProducts (ObjectProducts objectProducts) {
        if(objectProductsApi == null){
            // Only do this once
            ObjectProductsApi.Builder builder = new ObjectProductsApi.Builder(AndroidHttp.newCompatibleTransport(),
                    new AndroidJsonFactory(), null).setGoogleClientRequestInitializer(new GoogleClientRequestInitializer() {
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
                objectProductsApi.update(objectProducts.getId(), objectProducts).execute();
                Log.i(TAG, "update objectProducts");
            }


        } catch (IOException e){
            Log.e(TAG, e.toString());

        }
    }
    private void insertWarehouse(ObjectWarehouse objectWarehouse) {
        if(objectWarehouseApi == null){


            ObjectWarehouseApi.Builder builder = new ObjectWarehouseApi.Builder(AndroidHttp.newCompatibleTransport(),
                    new AndroidJsonFactory(), null).setGoogleClientRequestInitializer(new GoogleClientRequestInitializer() {
                @Override
                public void initialize(AbstractGoogleClientRequest<?> abstractGoogleClientRequest) throws IOException {
                    abstractGoogleClientRequest.setDisableGZipContent(true);
                }
            });
            objectWarehouseApi = builder.build();

        }

        try{

            if(objectWarehouse != null){
                objectWarehouseApi.insert(objectWarehouse).execute();
                Log.i(TAG, "insert objectWarehouse");
            }


        } catch (IOException e){
            Log.e(TAG, e.toString());

        }
    }


    private void deleteWarehouse(ObjectWarehouse objectWarehouse) {
        if (objectWarehouseApi == null) {
            // Only do this once
            ObjectWarehouseApi.Builder builder = new ObjectWarehouseApi.Builder(AndroidHttp.newCompatibleTransport(),
                    new AndroidJsonFactory(), null)
                    .setGoogleClientRequestInitializer(new GoogleClientRequestInitializer() {
                        @Override
                        public void initialize(AbstractGoogleClientRequest<?> abstractGoogleClientRequest) throws IOException {
                            abstractGoogleClientRequest.setDisableGZipContent(true);
                        }
                    });
            objectWarehouseApi = builder.build();
        }

        try {

            // Call here the wished methods on the Endpoints
            // For instance insert
            if (objectWarehouse != null) {
                objectWarehouseApi.remove(objectWarehouse.getId()).execute();
                Log.i(TAG, "remove objectWarehouse");
            }

        } catch (IOException e) {
            Log.e(TAG, e.toString());
        }
    }

    private void updateWarehouse (ObjectWarehouse objectWarehouse) {
        if(objectWarehouseApi == null){
            // Only do this once
            ObjectWarehouseApi.Builder builder = new ObjectWarehouseApi.Builder(AndroidHttp.newCompatibleTransport(),
                    new AndroidJsonFactory(), null).setGoogleClientRequestInitializer(new GoogleClientRequestInitializer() {
                @Override
                public void initialize(AbstractGoogleClientRequest<?> abstractGoogleClientRequest) throws IOException {
                    abstractGoogleClientRequest.setDisableGZipContent(true);
                }
            });
            objectWarehouseApi = builder.build();

        }

        try{
            // Call here the wished methods on the Endpoints
            // For instance insert
            if(objectWarehouse != null){
                objectWarehouseApi.update(objectWarehouse.getId(), objectWarehouse).execute();
                Log.i(TAG, "update objectWarehouse");
            }


        } catch (IOException e){
            Log.e(TAG, e.toString());

        }
    }

    private void insertStock(ObjectStock objectStock) {
        if(objectStockApi == null){


            ObjectStockApi.Builder builder = new ObjectStockApi.Builder(AndroidHttp.newCompatibleTransport(),
                    new AndroidJsonFactory(), null).setGoogleClientRequestInitializer(new GoogleClientRequestInitializer() {
                @Override
                public void initialize(AbstractGoogleClientRequest<?> abstractGoogleClientRequest) throws IOException {
                    abstractGoogleClientRequest.setDisableGZipContent(true);
                }
            });
            objectStockApi = builder.build();

        }

        try{

            if(objectStock != null){
                objectStockApi.insert(objectStock).execute();
                Log.i(TAG, "insert objectStock");
            }


        } catch (IOException e){
            Log.e(TAG, e.toString());

        }
    }


    private void deleteStock(ObjectStock objectStock) {
        if (objectStockApi == null) {
            // Only do this once
            ObjectStockApi.Builder builder = new ObjectStockApi.Builder(AndroidHttp.newCompatibleTransport(),
                    new AndroidJsonFactory(), null)
                    .setGoogleClientRequestInitializer(new GoogleClientRequestInitializer() {
                        @Override
                        public void initialize(AbstractGoogleClientRequest<?> abstractGoogleClientRequest) throws IOException {
                            abstractGoogleClientRequest.setDisableGZipContent(true);
                        }
                    });
            objectStockApi = builder.build();
        }

        try {

            // Call here the wished methods on the Endpoints
            // For instance insert
            if (objectStock != null) {
                objectStockApi.remove(objectStock.getId()).execute();
                Log.i(TAG, "remove objectStock");
            }

        } catch (IOException e) {
            Log.e(TAG, e.toString());
        }
    }

    private void updateStock (ObjectStock objectStock) {
        if(objectStockApi == null){


            ObjectStockApi.Builder builder = new ObjectStockApi.Builder(AndroidHttp.newCompatibleTransport(),
                    new AndroidJsonFactory(), null).setGoogleClientRequestInitializer(new GoogleClientRequestInitializer() {
                @Override
                public void initialize(AbstractGoogleClientRequest<?> abstractGoogleClientRequest) throws IOException {
                    abstractGoogleClientRequest.setDisableGZipContent(true);
                }
            });
            objectStockApi = builder.build();

        }

        try{
            // Call here the wished methods on the Endpoints
            // For instance insert
            if(objectStock != null){
                objectStockApi.update(objectStock.getId(), objectStock).execute();
                Log.i(TAG, "update objectStock");
            }


        } catch (IOException e){
            Log.e(TAG, e.toString());

        }
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

