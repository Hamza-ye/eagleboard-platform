package com.mass3d.trackedentitycomment;

import java.util.List;
import com.mass3d.common.IdentifiableObjectStore;

public interface TrackedEntityCommentStore
    extends IdentifiableObjectStore<TrackedEntityComment>
{
    /**
     * Checks for the existence of a TrackedEntityComment by UID
     *
     * @param uid TrackedEntityComment UID to check for.
     * @return true/false depending on result.
     */
    boolean exists(String uid);

    /**
     * Filters out existing {@see TrackedEntityComment} uid.
     *
     * Given:
     *
     * uid: abcd
     * uid: cdef
     * uid: ghil
     * uid: mnop
     *
     * and assuming that "cdef" and "abcd" are associated to two TrackedEntityComment in the database,
     * this method returns "ghil" and "mnop"
     *
     *
     * @param noteUids a List of {@see TrackedEntityComment} uid
     * @return a List of uid that are not present in the database
     */
    List<String> filterExisting(List<String> noteUids);

}
