package com.androidprojects.inventaireii;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.androidprojects.inventaireii.db.adapter.ChangeDataSource;
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
    private ArrayList <ObjectCategories> categoriesToInsert;
    private ArrayList <ObjectCategories> categoriesToUpdate;
    private ArrayList <ObjectCategories> categoriesToDelete;
    private ArrayList <ObjectProducts> productsToInsert;
    private ArrayList <ObjectProducts> productsToUpdate;
    private ArrayList <ObjectProducts> productsToDelete;
    private ArrayList <ObjectStock> stocksToInsert;
    private ArrayList <ObjectStock> stocksToUpdate;
    private ArrayList <ObjectStock> stocksToDelete;
    private ArrayList <ObjectWarehouse> warehousesToInsert;
    private ArrayList <ObjectWarehouse> warehousesToUpdate;
    private ArrayList <ObjectWarehouse> warehousesToDelete;
    private ChangeDataSource changeDataSource;



    EndpointsAsyncTask(){}

    EndpointsAsyncTask(Context context){

        changeDataSource = ChangeDataSource.getInstance(context);


    }

    @Override
    protected List<ObjectCategories> doInBackground(Void... params) {



        categoriesToInsert = changeDataSource.getCategoriesTo(ObjectChange.TypeOfChange.insertObject);

        for (ObjectCategories category : categoriesToInsert) {

            insertCategory(category);
        }

        categoriesToUpdate = changeDataSource.getCategoriesTo(ObjectChange.TypeOfChange.updateObject);

        for (ObjectCategories category : categoriesToUpdate) {

            updateCategory(category);
        }

        categoriesToDelete = changeDataSource.getCategoriesTo(ObjectChange.TypeOfChange.deleteObject);

        for (ObjectCategories category : categoriesToDelete) {

            deleteCategories(category);
        }


        warehousesToInsert = changeDataSource.getWarehousesTo(ObjectChange.TypeOfChange.insertObject);

        for (ObjectWarehouse warehouse : warehousesToInsert) {

            insertWarehouse(warehouse);
        }

        warehousesToUpdate = changeDataSource.getWarehousesTo(ObjectChange.TypeOfChange.updateObject);

        for (ObjectWarehouse warehouse : warehousesToUpdate) {

            updateWarehouse(warehouse);
        }

        warehousesToDelete = changeDataSource.getWarehousesTo(ObjectChange.TypeOfChange.deleteObject);

        for (ObjectWarehouse warehouse : warehousesToDelete) {

            deleteWarehouse(warehouse);
        }


        productsToInsert = changeDataSource.getProductsTo(ObjectChange.TypeOfChange.insertObject);

        for (ObjectProducts product : productsToInsert) {

            insertProduct(product);
        }

        productsToUpdate = changeDataSource.getProductsTo(ObjectChange.TypeOfChange.updateObject);

        for (ObjectProducts product : productsToUpdate) {

            updateProducts(product);
        }

        productsToDelete = changeDataSource.getProductsTo(ObjectChange.TypeOfChange.deleteObject);

        for (ObjectProducts product : productsToDelete) {

            deleteProducts(product);
        }

        stocksToInsert = changeDataSource.getStocksTo(ObjectChange.TypeOfChange.insertObject);

        for (ObjectStock stock : stocksToInsert) {

            insertStock(stock);
        }

        stocksToUpdate = changeDataSource.getStocksTo(ObjectChange.TypeOfChange.updateObject);

        for (ObjectStock stock : stocksToUpdate) {

            updateStock(stock);
        }

        stocksToDelete = changeDataSource.getStocksTo(ObjectChange.TypeOfChange.deleteObject);

        for (ObjectStock stock : stocksToDelete) {

            deleteStock(stock);
        }

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
                objectCategoriesApi.update(objectCategories.getId(), objectCategories).execute();
                Log.i(TAG, "update objectCategories");
            }


        } catch (IOException e){
            Log.e(TAG, e.toString());

        }
    }

    private void insertProduct(ObjectProducts objectProducts) {
        if(objectProductsApi == null){

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
            if(objectStock != null){
                objectStockApi.update(objectStock.getId(), objectStock).execute();
                Log.i(TAG, "update objectStock");
            }


        } catch (IOException e){
            Log.e(TAG, e.toString());

        }
    }


}
