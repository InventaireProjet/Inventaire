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
        name = "objectWarehouseApi",
        version = "v1",
        resource = "objectWarehouse",
        namespace = @ApiNamespace(
                ownerDomain = "backend.myapplication.example.com",
                ownerName = "backend.myapplication.example.com",
                packagePath = ""
        )
)
public class ObjectWarehouseEndpoint {

    private static final Logger logger = Logger.getLogger(ObjectWarehouseEndpoint.class.getName());

    private static final int DEFAULT_LIST_LIMIT = 20;

    static {
        // Typically you would register this inside an OfyServive wrapper. See: https://code.google.com/p/objectify-appengine/wiki/BestPractices
        ObjectifyService.register(ObjectWarehouse.class);
    }

    /**
     * Returns the {@link ObjectWarehouse} with the corresponding ID.
     *
     * @param id the ID of the entity to be retrieved
     * @return the entity with the corresponding ID
     * @throws NotFoundException if there is no {@code ObjectWarehouse} with the provided ID.
     */
    @ApiMethod(
            name = "get",
            path = "objectWarehouse/{id}",
            httpMethod = ApiMethod.HttpMethod.GET)
    public ObjectWarehouse get(@Named("id") Long id) throws NotFoundException {
        logger.info("Getting ObjectWarehouse with ID: " + id);
        ObjectWarehouse objectWarehouse = ofy().load().type(ObjectWarehouse.class).id(id).now();
        if (objectWarehouse == null) {
            throw new NotFoundException("Could not find ObjectWarehouse with ID: " + id);
        }
        return objectWarehouse;
    }

    /**
     * Inserts a new {@code ObjectWarehouse}.
     */
    @ApiMethod(
            name = "insert",
            path = "objectWarehouse",
            httpMethod = ApiMethod.HttpMethod.POST)
    public ObjectWarehouse insert(ObjectWarehouse objectWarehouse) {
        // Typically in a RESTful API a POST does not have a known ID (assuming the ID is used in the resource path).
        // You should validate that objectWarehouse.id has not been set. If the ID type is not supported by the
        // Objectify ID generator, e.g. long or String, then you should generate the unique ID yourself prior to saving.
        //
        // If your client provides the ID then you should probably use PUT instead.
        ofy().save().entity(objectWarehouse).now();
        logger.info("Created ObjectWarehouse with ID: " + objectWarehouse.getId());

        return ofy().load().entity(objectWarehouse).now();
    }

    /**
     * Updates an existing {@code ObjectWarehouse}.
     *
     * @param id              the ID of the entity to be updated
     * @param objectWarehouse the desired state of the entity
     * @return the updated version of the entity
     * @throws NotFoundException if the {@code id} does not correspond to an existing
     *                           {@code ObjectWarehouse}
     */
    @ApiMethod(
            name = "update",
            path = "objectWarehouse/{id}",
            httpMethod = ApiMethod.HttpMethod.PUT)
    public ObjectWarehouse update(@Named("id") Long id, ObjectWarehouse objectWarehouse) throws NotFoundException {
        // TODO: You should validate your ID parameter against your resource's ID here.
        checkExists(id);
        ofy().save().entity(objectWarehouse).now();
        logger.info("Updated ObjectWarehouse: " + objectWarehouse);
        return ofy().load().entity(objectWarehouse).now();
    }

    /**
     * Deletes the specified {@code ObjectWarehouse}.
     *
     * @param id the ID of the entity to delete
     * @throws NotFoundException if the {@code id} does not correspond to an existing
     *                           {@code ObjectWarehouse}
     */
    @ApiMethod(
            name = "remove",
            path = "objectWarehouse/{id}",
            httpMethod = ApiMethod.HttpMethod.DELETE)
    public void remove(@Named("id") Long id) throws NotFoundException {
        checkExists(id);
        ofy().delete().type(ObjectWarehouse.class).id(id).now();
        logger.info("Deleted ObjectWarehouse with ID: " + id);
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
            path = "objectWarehouse",
            httpMethod = ApiMethod.HttpMethod.GET)
    public CollectionResponse<ObjectWarehouse> list(@Nullable @Named("cursor") String cursor, @Nullable @Named("limit") Integer limit) {
        limit = limit == null ? DEFAULT_LIST_LIMIT : limit;
        Query<ObjectWarehouse> query = ofy().load().type(ObjectWarehouse.class).limit(limit);
        if (cursor != null) {
            query = query.startAt(Cursor.fromWebSafeString(cursor));
        }
        QueryResultIterator<ObjectWarehouse> queryIterator = query.iterator();
        List<ObjectWarehouse> objectWarehouseList = new ArrayList<ObjectWarehouse>(limit);
        while (queryIterator.hasNext()) {
            objectWarehouseList.add(queryIterator.next());
        }
        return CollectionResponse.<ObjectWarehouse>builder().setItems(objectWarehouseList).setNextPageToken(queryIterator.getCursor().toWebSafeString()).build();
    }

    private void checkExists(Long id) throws NotFoundException {
        try {
            ofy().load().type(ObjectWarehouse.class).id(id).safe();
        } catch (com.googlecode.objectify.NotFoundException e) {
            throw new NotFoundException("Could not find ObjectWarehouse with ID: " + id);
        }
    }
}