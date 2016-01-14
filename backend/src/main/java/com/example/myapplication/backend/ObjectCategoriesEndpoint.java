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
        name = "objectCategoriesApi",
        version = "v1",
        resource = "objectCategories",
        namespace = @ApiNamespace(
                ownerDomain = "backend.myapplication.example.com",
                ownerName = "backend.myapplication.example.com",
                packagePath = ""
        )
)
public class ObjectCategoriesEndpoint {

    private static final Logger logger = Logger.getLogger(ObjectCategoriesEndpoint.class.getName());

    private static final int DEFAULT_LIST_LIMIT = 20;

    static {
        // Typically you would register this inside an OfyServive wrapper. See: https://code.google.com/p/objectify-appengine/wiki/BestPractices
        ObjectifyService.register(ObjectCategories.class);
    }

    /**
     * Returns the {@link ObjectCategories} with the corresponding ID.
     *
     * @param id the ID of the entity to be retrieved
     * @return the entity with the corresponding ID
     * @throws NotFoundException if there is no {@code ObjectCategories} with the provided ID.
     */
    @ApiMethod(
            name = "get",
            path = "objectCategories/{id}",
            httpMethod = ApiMethod.HttpMethod.GET)
    public ObjectCategories get(@Named("id") Long id) throws NotFoundException {
        logger.info("Getting ObjectCategories with ID: " + id);
        ObjectCategories objectCategories = ofy().load().type(ObjectCategories.class).id(id).now();
        if (objectCategories == null) {
            throw new NotFoundException("Could not find ObjectCategories with ID: " + id);
        }
        return objectCategories;
    }

    /**
     * Inserts a new {@code ObjectCategories}.
     */
    @ApiMethod(
            name = "insert",
            path = "objectCategories",
            httpMethod = ApiMethod.HttpMethod.POST)
    public ObjectCategories insert(ObjectCategories objectCategories) {
        // Typically in a RESTful API a POST does not have a known ID (assuming the ID is used in the resource path).
        // You should validate that objectCategories.id has not been set. If the ID type is not supported by the
        // Objectify ID generator, e.g. long or String, then you should generate the unique ID yourself prior to saving.
        //
        // If your client provides the ID then you should probably use PUT instead.
        ofy().save().entity(objectCategories).now();
        logger.info("Created ObjectCategories with ID: " + objectCategories.getId());

        return ofy().load().entity(objectCategories).now();
    }

    /**
     * Updates an existing {@code ObjectCategories}.
     *
     * @param id               the ID of the entity to be updated
     * @param objectCategories the desired state of the entity
     * @return the updated version of the entity
     * @throws NotFoundException if the {@code id} does not correspond to an existing
     *                           {@code ObjectCategories}
     */
    @ApiMethod(
            name = "update",
            path = "objectCategories/{id}",
            httpMethod = ApiMethod.HttpMethod.PUT)
    public ObjectCategories update(@Named("id") Long id, ObjectCategories objectCategories) throws NotFoundException {
        // TODO: You should validate your ID parameter against your resource's ID here.
        checkExists(id);
        ofy().save().entity(objectCategories).now();
        logger.info("Updated ObjectCategories: " + objectCategories);
        return ofy().load().entity(objectCategories).now();
    }

    /**
     * Deletes the specified {@code ObjectCategories}.
     *
     * @param id the ID of the entity to delete
     * @throws NotFoundException if the {@code id} does not correspond to an existing
     *                           {@code ObjectCategories}
     */
    @ApiMethod(
            name = "remove",
            path = "objectCategories/{id}",
            httpMethod = ApiMethod.HttpMethod.DELETE)
    public void remove(@Named("id") Long id) throws NotFoundException {
        checkExists(id);
        ofy().delete().type(ObjectCategories.class).id(id).now();
        logger.info("Deleted ObjectCategories with ID: " + id);
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
            path = "objectCategories",
            httpMethod = ApiMethod.HttpMethod.GET)
    public CollectionResponse<ObjectCategories> list(@Nullable @Named("cursor") String cursor, @Nullable @Named("limit") Integer limit) {
        limit = limit == null ? DEFAULT_LIST_LIMIT : limit;
        Query<ObjectCategories> query = ofy().load().type(ObjectCategories.class).limit(limit);
        if (cursor != null) {
            query = query.startAt(Cursor.fromWebSafeString(cursor));
        }
        QueryResultIterator<ObjectCategories> queryIterator = query.iterator();
        List<ObjectCategories> objectCategoriesList = new ArrayList<ObjectCategories>(limit);
        while (queryIterator.hasNext()) {
            objectCategoriesList.add(queryIterator.next());
        }
        return CollectionResponse.<ObjectCategories>builder().setItems(objectCategoriesList).setNextPageToken(queryIterator.getCursor().toWebSafeString()).build();
    }

    private void checkExists(Long id) throws NotFoundException {
        try {
            ofy().load().type(ObjectCategories.class).id(id).safe();
        } catch (com.googlecode.objectify.NotFoundException e) {
            throw new NotFoundException("Could not find ObjectCategories with ID: " + id);
        }
    }
}