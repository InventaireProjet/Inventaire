package com.example.myapplication.backend;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiNamespace;
import com.google.api.server.spi.response.CollectionResponse;
import com.google.api.server.spi.response.NotFoundException;
import com.google.appengine.api.datastore.Cursor;
import com.google.appengine.api.datastore.QueryResultIterator;
import com.googlecode.objectify.ObjectifyService;
import com.googlecode.objectify.cmd.Query;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.annotation.Nullable;
import javax.inject.Named;

import static com.googlecode.objectify.ObjectifyService.ofy;

///Auto-generated

@Api(
        name = "objectProductsApi",
        version = "v1",
        resource = "objectProducts",
        namespace = @ApiNamespace(
                ownerDomain = "backend.myapplication.example.com",
                ownerName = "backend.myapplication.example.com",
                packagePath = ""
        )
)
public class ObjectProductsEndpoint {

    private static final Logger logger = Logger.getLogger(ObjectProductsEndpoint.class.getName());

    private static final int DEFAULT_LIST_LIMIT = 20;

    static {
        // Typically you would register this inside an OfyServive wrapper. See: https://code.google.com/p/objectify-appengine/wiki/BestPractices
        ObjectifyService.register(ObjectProducts.class);
    }

    /**
     * Returns the {@link ObjectProducts} with the corresponding ID.
     *
     * @param id the ID of the entity to be retrieved
     * @return the entity with the corresponding ID
     * @throws NotFoundException if there is no {@code ObjectProducts} with the provided ID.
     */
    @ApiMethod(
            name = "get",
            path = "objectProducts/{id}",
            httpMethod = ApiMethod.HttpMethod.GET)
    public ObjectProducts get(@Named("id") Long id) throws NotFoundException {
        logger.info("Getting ObjectProducts with ID: " + id);
        ObjectProducts objectProducts = ofy().load().type(ObjectProducts.class).id(id).now();
        if (objectProducts == null) {
            throw new NotFoundException("Could not find ObjectProducts with ID: " + id);
        }
        return objectProducts;
    }

    /**
     * Inserts a new {@code ObjectProducts}.
     */
    @ApiMethod(
            name = "insert",
            path = "objectProducts",
            httpMethod = ApiMethod.HttpMethod.POST)
    public ObjectProducts insert(ObjectProducts objectProducts) {
        // Typically in a RESTful API a POST does not have a known ID (assuming the ID is used in the resource path).
        // You should validate that objectProducts.id has not been set. If the ID type is not supported by the
        // Objectify ID generator, e.g. long or String, then you should generate the unique ID yourself prior to saving.
        //
        // If your client provides the ID then you should probably use PUT instead.
        ofy().save().entity(objectProducts).now();
        logger.info("Created ObjectProducts with ID: " + objectProducts.getId());

        return ofy().load().entity(objectProducts).now();
    }

    /**
     * Updates an existing {@code ObjectProducts}.
     *
     * @param id             the ID of the entity to be updated
     * @param objectProducts the desired state of the entity
     * @return the updated version of the entity
     * @throws NotFoundException if the {@code id} does not correspond to an existing
     *                           {@code ObjectProducts}
     */
    @ApiMethod(
            name = "update",
            path = "objectProducts/{id}",
            httpMethod = ApiMethod.HttpMethod.PUT)
    public ObjectProducts update(@Named("id") Long id, ObjectProducts objectProducts) throws NotFoundException {
        // TODO: You should validate your ID parameter against your resource's ID here.
        checkExists(id);
        ofy().save().entity(objectProducts).now();
        logger.info("Updated ObjectProducts: " + objectProducts);
        return ofy().load().entity(objectProducts).now();
    }

    /**
     * Deletes the specified {@code ObjectProducts}.
     *
     * @param id the ID of the entity to delete
     * @throws NotFoundException if the {@code id} does not correspond to an existing
     *                           {@code ObjectProducts}
     */
    @ApiMethod(
            name = "remove",
            path = "objectProducts/{id}",
            httpMethod = ApiMethod.HttpMethod.DELETE)
    public void remove(@Named("id") Long id) throws NotFoundException {
        checkExists(id);
        ofy().delete().type(ObjectProducts.class).id(id).now();
        logger.info("Deleted ObjectProducts with ID: " + id);
    }

    /**
     * List all entities.
     *
     * @param cursor used for pagination to determine which page to return
     * @param limit  the maximum number of entries to return
     * @return a response that encapsulates the result list and the next page token/cursor
     */
    @ApiMethod(
            name = "list",
            path = "objectProducts",
            httpMethod = ApiMethod.HttpMethod.GET)
    public CollectionResponse<ObjectProducts> list(@Nullable @Named("cursor") String cursor, @Nullable @Named("limit") Integer limit) {
        limit = limit == null ? DEFAULT_LIST_LIMIT : limit;
        Query<ObjectProducts> query = ofy().load().type(ObjectProducts.class).limit(limit);
        if (cursor != null) {
            query = query.startAt(Cursor.fromWebSafeString(cursor));
        }
        QueryResultIterator<ObjectProducts> queryIterator = query.iterator();
        List<ObjectProducts> objectProductsList = new ArrayList<ObjectProducts>(limit);
        while (queryIterator.hasNext()) {
            objectProductsList.add(queryIterator.next());
        }
        return CollectionResponse.<ObjectProducts>builder().setItems(objectProductsList).setNextPageToken(queryIterator.getCursor().toWebSafeString()).build();
    }

    private void checkExists(Long id) throws NotFoundException {
        try {
            ofy().load().type(ObjectProducts.class).id(id).safe();
        } catch (com.googlecode.objectify.NotFoundException e) {
            throw new NotFoundException("Could not find ObjectProducts with ID: " + id);
        }
    }
}