package com.mass3d.trackedentitycomment;

import java.util.List;

public interface TrackedEntityCommentService
{
    String ID = TrackedEntityCommentService.class.getName();

    /**
     * Adds an {@link TrackedEntityComment}
     *
     * @param comment The to TrackedEntityComment add.
     * @return A generated unique id of the added {@link TrackedEntityComment}.
     */
    long addTrackedEntityComment(TrackedEntityComment comment);

    /**
     * Deletes a {@link TrackedEntityComment}.
     *
     * @param comment the TrackedEntityComment to delete.
     */
    void deleteTrackedEntityComment(TrackedEntityComment comment);

    /**
     * Checks for the existence of a TrackedEntityComment by UID.
     *
     * @param uid TrackedEntityComment UID to check for
     * @return true/false depending on result
     */
    boolean trackedEntityCommentExists(String uid);

    /**
     * Filters out existing {@see TrackedEntityComment} uids from a List of uids.
     *
     * Given a List:
     *
     * uid: abcd
     * uid: cdef
     * uid: ghil
     * uid: mnop
     *
     * and assuming that "cdef" and "abcd" are associated to two TrackedEntityComment in the database,
     * this method returns "ghil" and "mnop"
     *
     * @param uids a List of {@see TrackedEntityComment} uid
     * @return a List of uid that are not present in the database
     */
    List<String> filterExistingNotes(List<String> uids);

    /**
     * Updates an {@link TrackedEntityComment}.
     *
     * @param comment the TrackedEntityComment to update.
     */
    void updateTrackedEntityComment(TrackedEntityComment comment);

    /**
     * Returns a {@link TrackedEntityComment}.
     *
     * @param id the id of the TrackedEntityComment to return.
     * @return the TrackedEntityComment with the given id
     */
    TrackedEntityComment getTrackedEntityComment(long id);

}
