package com.mass3d.trackedentity;

import java.util.Date;
import java.util.List;
import java.util.Set;
import com.mass3d.common.Grid;
import com.mass3d.common.IllegalQueryException;
import com.mass3d.trackedentityattributevalue.TrackedEntityAttributeValue;
import com.mass3d.user.User;

/**
 * <p>This interface is responsible for retrieving tracked entity instances (TEI).
 * The query methods accepts a TrackedEntityInstanceQueryParams object which
 * encapsulates all arguments.</p>
 * <p/>
 * <p>The TEIs are returned as a Grid object, which is a two-dimensional list with
 * headers. The TEI attribute values are returned in the same order as specified
 * in the arguments. The grid has a set of columns which are always present
 * starting at index 0, followed by attributes specified for the query. All
 * values in the grid are of type String. The order is:</p>
 * <p/>
 * <ul>
 * <li>0: Tracked entity instance UID</li>
 * <li>1: Created time stamp</li>
 * <li>2: Last updated time stamp</li>
 * <li>3: Organisation unit UID</li>
 * <li>4: Tracked entity UID</li>
 * <ul>
 * <p/>
 * <p>Attributes specified in the query follows on the next column indexes.
 * Example usage for retrieving TEIs with two attributes using one attribute as
 * filter:</p>
 *
 * <pre>
 * <code>
 * TrackedEntityInstanceQueryParams params = new TrackedEntityInstanceQueryParams();
 *
 * params.addAttribute( new QueryItem( gender, QueryOperator.EQ, "Male", false ) );
 * params.addAttribute( new QueryItem( age, QueryOperator.LT, "5", true ) );
 * params.addFilter( new QueryItem( weight, QueryOperator.GT, "2500", true ) );
 * params.addOrganistionUnit( unit );
 *
 * Grid instances = teiService.getTrackedEntityInstancesGrid( params );
 *
 * for ( List&lt;Object&gt; row : instances.getRows() )
 * {
 *     String tei = row.get( 0 );
 *     String ou = row.get( 3 );
 *     String gender = row.get( 5 );
 *     String age = row.get( 6 );
 * }
 * </code>
 * </pre>
 *
 */
public interface TrackedEntityInstanceService
{
    String ID = TrackedEntityInstanceService.class.getName();

    int ERROR_NONE = 0;
    int ERROR_DUPLICATE_IDENTIFIER = 1;
    int ERROR_ENROLLMENT = 2;

    String SEPARATOR = "_";

    /**
     * Returns a grid with tracked entity instance values based on the given
     * TrackedEntityInstanceQueryParams.
     *
     * @param params the TrackedEntityInstanceQueryParams.
     * @return a grid.
     */
    Grid getTrackedEntityInstancesGrid(TrackedEntityInstanceQueryParams params);

    /**
     * Returns a list with tracked entity instance values based on the given TrackedEntityInstanceQueryParams.
     *
     * @param params               the TrackedEntityInstanceQueryParams.
     * @param skipAccessValidation If true, access validation is skipped. Should be set to true only for internal
     *                             tasks (e.g. currently used by synchronization job)
     * @return List of TEIs matching the params
     */
    List<TrackedEntityInstance> getTrackedEntityInstances(TrackedEntityInstanceQueryParams params,
        boolean skipAccessValidation);

    List<Long> getTrackedEntityInstanceIds(TrackedEntityInstanceQueryParams params,
        boolean skipAccessValidation);

    /**
     * Return the count of the Tracked entity instances that meet the criteria specified in params
     *
     * @param params                    Parameteres that specify searching criteria
     * @param skipAccessValidation      If true, the access validation is skipped
     * @param skipSearchScopeValidation If true, the search scope validation is skipped
     * @return the count of the Tracked entity instances that meet the criteria specified in params
     */
    int getTrackedEntityInstanceCount(TrackedEntityInstanceQueryParams params,
        boolean skipAccessValidation, boolean skipSearchScopeValidation);

    /**
     * Decides whether current user is authorized to perform the given query.
     * IllegalQueryException is thrown if not.
     *
     * @param params the TrackedEntityInstanceQueryParams.
     */
    void decideAccess(TrackedEntityInstanceQueryParams params);

    /**
     * Validates scope of given TrackedEntityInstanceQueryParams. The params is
     * considered valid if no exception are thrown and the method returns
     * normally.
     *
     * @param params       the TrackedEntityInstanceQueryParams.
     * @param isGridSearch specifies whether search is made for a Grid response
     * @throws IllegalQueryException if the given params is invalid.
     */
    void validateSearchScope(TrackedEntityInstanceQueryParams params, boolean isGridSearch)
        throws IllegalQueryException;

    /**
     * Validates the given TrackedEntityInstanceQueryParams. The params is
     * considered valid if no exception are thrown and the method returns
     * normally.
     *
     * @param params the TrackedEntityInstanceQueryParams.
     * @throws IllegalQueryException if the given params is invalid.
     */
    void validate(TrackedEntityInstanceQueryParams params)
        throws IllegalQueryException;

    /**
     * Adds an {@link TrackedEntityInstance}
     *
     * @param entityInstance The to TrackedEntityInstance add.
     * @return A generated unique id of the added {@link TrackedEntityInstance}.
     */
    long addTrackedEntityInstance(TrackedEntityInstance entityInstance);

    /**
     * Soft deletes a {@link TrackedEntityInstance}.
     *
     * @param entityInstance the TrackedEntityInstance to delete.
     */
    void deleteTrackedEntityInstance(TrackedEntityInstance entityInstance);

    /**
     * Updates a {@link TrackedEntityInstance}.
     *
     * @param entityInstance the TrackedEntityInstance to update.
     */
    void updateTrackedEntityInstance(TrackedEntityInstance entityInstance);

    void updateTrackedEntityInstance(TrackedEntityInstance instance, User user);

    /**
     * Updates a last sync timestamp on specified TrackedEntityInstances
     *
     * @param trackedEntityInstanceUIDs UIDs of Tracked entity instances where the lastSynchronized flag should be updated
     * @param lastSynchronized          The date of last successful sync
     */
    void updateTrackedEntityInstancesSyncTimestamp(List<String> trackedEntityInstanceUIDs,
        Date lastSynchronized);

    /**
     * Returns a {@link TrackedEntityInstance}.
     *
     * @param id the id of the TrackedEntityInstanceAttribute to return.
     * @return the TrackedEntityInstanceAttribute with the given id
     */
    TrackedEntityInstance getTrackedEntityInstance(long id);

    /**
     * Returns the {@link TrackedEntityAttribute} with the given UID.
     *
     * @param uid the UID.
     * @return the TrackedEntityInstanceAttribute with the given UID, or null if
     * no match.
     */
    TrackedEntityInstance getTrackedEntityInstance(String uid);

    /**
     * Returns the {@link TrackedEntityAttribute} with the given UID.
     *
     * @param uid  the UID.
     * @param user User
     * @return the TrackedEntityInstanceAttribute with the given UID, or null if
     * no match.
     */
    TrackedEntityInstance getTrackedEntityInstance(String trackedEntityInstance, User user);

    /**
     * Checks for the existence of a TEI by UID. Deleted values are not taken into account.
     *
     * @param uid PSI UID to check for
     * @return true/false depending on result
     */
    boolean trackedEntityInstanceExists(String uid);

    /**
     * Checks for the existence of a TEI by UID. Takes into account also the deleted values.
     *
     * @param uid PSI UID to check for
     * @return true/false depending on result
     */
    boolean trackedEntityInstanceExistsIncludingDeleted(String uid);

    /**
     * Returns UIDs of existing TrackedEntityInstances (including deleted) from the provided UIDs
     *
     * @param uids TEI UIDs to check
     * @return Set containing UIDs of existing TEIs (including deleted)
     */
    List<String> getTrackedEntityInstancesUidsIncludingDeleted(List<String> uids);

    /**
     * Register a new entityInstance
     *
     * @param entityInstance  TrackedEntityInstance
     * @param attributeValues Set of attribute values
     * @return The error code after registering entityInstance
     */
    long createTrackedEntityInstance(TrackedEntityInstance entityInstance,
        Set<TrackedEntityAttributeValue> attributeValues);

    List<TrackedEntityInstance> getTrackedEntityInstancesByUid(List<String> uids, User user);
}
