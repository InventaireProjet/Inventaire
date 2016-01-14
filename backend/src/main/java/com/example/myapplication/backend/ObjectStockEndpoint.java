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

//Auto-generated


@Api(
        name = "objectStockApi",
        version = "v1",
        resource = "objectStock",
        namespace = @ApiNamespace(
                ownerDomain = "backend.myapplication.example.com",
                ownerName = "backend.myapplication.example.com",
                packagePath = ""
        )
)
public class ObjectStockEndpoint {

    private static final Logger logger = Logger.getLogger(ObjectStockEndpoint.class.getName());

    private static final int DEFAULT_LIST_LIMIT = 20;

    static {
        // Typically you would register this inside an OfyServive wrapper. See: https://code.google.com/p/objectify-appengine/wiki/BestPractices
        ObjectifyService.register(ObjectStock.class);
    }

    /**
     * Returns the {@link ObjectStock} with the corresponding ID.
     *
     * @param id the ID of the entity to be retrieved
     * @return the entity with the corresponding ID
     * @throws NotFoundException if there is no {@code ObjectStock} with the provided ID.
     */
    @ApiMethod(
            name = "get",
            path = "objectStock/{id}",
            httpMethod = ApiMethod.HttpMethod.GET)
    public ObjectStock get(@Named("id") Long id) throws NotFoundException {
        logger.info("Getting ObjectStock with ID: " + id);
        ObjectStock objectStock = ofy().load().type(ObjectStock.class).id(id).now();
        if (objectStock == null) {
            throw new NotFoundException("Could not find ObjectStock with ID: " + id);
        }
        return objectStock;
    }

    /**
     * Inserts a new {@code ObjectStock}.
     */
    @ApiMethod(
            name = "insert",
            path = "objectStock",
            httpMethod = ApiMethod.HttpMethod.POST)
    public ObjectStock insert(ObjectStock objectStock) {
        // Typically in a RESTful API a POST does not have a known ID (assuming the ID is used in the resource path).
        // You should validate that objectStock.id has not been set. If the ID type is not supported by the
        // Objectify ID generator, e.g. long or String, then you should generate the unique ID yourself prior to saving.
        //
        // If your client provides the ID then you should probably use PUT instead.
        ofy().save().entity(objectStock).now();
        logger.info("Created ObjectStock with ID: " + objectStock.getId());

        return ofy().load().entity(objectStock).now();
    }

    /**
     * Updates an existing {@code ObjectStock}.
     *
     * @param id          the ID of the entity to be updated
     * @param objectStock the desired state of the entity
     * @return the updated version of the entity
     * @throws NotFoundException if the {@code id} does not correspond to an existing
     *                           {@code ObjectStock}
     */
    @ApiMethod(
            name = "update",
            path = "objectStock/{id}",
            httpMethod = ApiMethod.HttpMethod.PUT)
    public ObjectStock update(@Named("id") Long id, ObjectStock objectStock) throws NotFoundException {
        // TODO: You should validate your ID parameter against your resource's ID here.
        checkExists(id);
        ofy().save().entity(objectStock).now();
        logger.info("Updated ObjectStock: " + objectStock);
        return ofy().load().entity(objectStock).now();
    }

    /**
     * Deletes the specified {@code ObjectStock}.
     *
     * @param id the ID of the entity to delete
     * @throws NotFoundException if the {@code id} does not correspond to an existing
     *                           {@code ObjectStock}
     */
    @ApiMethod(
            name = "remove",
            path = "objectStock/{id}",
            httpMethod = ApiMethod.HttpMethod.DELETE)
    public void remove(@Named("id") Long id) throws NotFoundException {
        checkExists(id);
        ofy().delete().type(ObjectStock.class).id(id).now();
        logger.info("Deleted ObjectStock with ID: " + id);
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
            path = "objectStock",
            httpMethod = ApiMethod.HttpMethod.GET)
    public CollectionResponse<ObjectStock> list(@Nullable @Named("cursor") String cursor, @Nullable @Named("limit") Integer limit) {
        limit = limit == null ? DEFAULT_LIST_LIMIT : limit;
        Query<ObjectStock> query = ofy().load().type(ObjectStock.class).limit(limit);
        if (cursor != null) {
            query = query.startAt(Cursor.fromWebSafeString(cursor));
        }
        QueryResultIterator<ObjectStock> queryIterator = query.iterator();
        List<ObjectStock> objectStockList = new ArrayList<ObjectStock>(limit);
        while (queryIterator.hasNext()) {
            objectStockList.add(queryIterator.next());
        }
        return CollectionResponse.<ObjectStock>builder().setItems(objectStockList).setNextPageToken(queryIterator.getCursor().toWebSafeString()).build();
    }

    private void checkExists(Long id) throws NotFoundException {
        try {
            ofy().load().type(ObjectStock.class).id(id).safe();
        } catch (com.googlecode.objectify.NotFoundException e) {
            throw new NotFoundException("Could not find ObjectStock with ID: " + id);
        }
    }
}