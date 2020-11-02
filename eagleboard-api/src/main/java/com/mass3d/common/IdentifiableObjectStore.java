package com.mass3d.common;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Set;
import com.mass3d.attribute.Attribute;
import com.mass3d.user.User;
import com.mass3d.user.UserInfo;

public interface IdentifiableObjectStore<T>
    extends GenericStore<T>
{
    /**
     * Saves the given object instance.
     *
     * @param object the object instance.
     * @param clearSharing indicates whether to clear all sharing related properties.
     */
    void save(T object, boolean clearSharing);

    /**
     * Saves the given object instance.
     *
     * @param object the object instance.
     * @param user the user currently in the security context.
     */
    void save(T object, User user);

    /**
     * Updates the given object instance.
     *
     * @param object the object instance.
     * @param user   User
     */
    void update(T object, User user);

    /**
     * Update object. Bypasses the ACL system.
     *
     * @param object Object update
     */
    void updateNoAcl(T object);

    /**
     * Removes the given object instance.
     *
     * @param object the object instance to delete.
     * @param user   User
     */
    void delete(T object, User user);

    /**
     * Retrieves the object with the given uid.
     *
     * @param uid the uid.
     * @return the object with the given uid.
     */
    T getByUid(String uid);

    /**
     * Retrieves the object with the given uid. Bypasses the ACL system.
     *
     * @param uid the uid.
     * @return the object with the given uid.
     */
    T getByUidNoAcl(String uid);

    /**
     * Retrieves the object with the given name.
     *
     * @param name the name.
     * @return the object with the given name.
     */
    T getByName(String name);

    /**
     * Retrieves the object with the given code.
     *
     * @param code the code.
     * @return the object with the given code.
     */
    T getByCode(String code);

    /**
     * Retrieves the attribute value associated with the unique attribute and
     * the given value.
     *
     * @param attribute the attribute.
     * @param value     the value.
     * @return the attribute value.
     */
    T getByUniqueAttributeValue(Attribute attribute, String value);

    T getByUniqueAttributeValue(Attribute attribute, String value, UserInfo userInfo);

    /**
     * Retrieves a List of all objects (sorted on name).
     *
     * @return a List of all objects.
     */
    List<T> getAllOrderedName();

    /**
     * Retrieves the objects determined by the given first result and max result.
     *
     * @param first the first result object to return.
     * @param max   the max number of result objects to return.
     * @return list of objects.
     */
    List<T> getAllOrderedName(int first, int max);

    /**
     * Retrieves a List of objects where the name is equal the given name.
     *
     * @param name the name.
     * @return a List of objects.
     */
    List<T> getAllEqName(String name);

    /**
     * Retrieves a List of objects where the name is like the given name.
     *
     * @param name the name.
     * @return a List of objects.
     */
    List<T> getAllLikeName(String name);

    /**
     * Retrieves a List of objects where the name is like the given name.
     *
     * @param name the name.
     * @return a List of objects.
     */
    List<T> getAllLikeName(String name, boolean caseSensitive);

    /**
     * Retrieves a List of objects where the name is like the given name.
     *
     * @param name  the name.
     * @param first the first result object to return.
     * @param max   the max number of result objects to return.
     * @return a List of objects.
     */
    List<T> getAllLikeName(String name, int first, int max);

    /**
     * Retrieves a List of objects where the name is like the given name.
     *
     * @param name          the name.
     * @param first         the first result object to return.
     * @param max           the max number of result objects to return.
     * @param caseSensitive Case sensitive matches or not
     * @return a List of objects.
     */
    List<T> getAllLikeName(String name, int first, int max, boolean caseSensitive);

    /**
     * Retrieves a List of objects where the name matches the conjunction of the
     * given set of words.
     *
     * @param words the set of words.
     * @param first the first result object to return.
     * @param max   the max number of result objects to return.
     * @return a List of objects.
     */
    List<T> getAllLikeName(Set<String> words, int first, int max);

    /**
     * Retrieves the objects determined by the given first result and max result.
     * The returned list is ordered by the last updated property descending.
     *
     * @param first the first result object to return.
     * @param max   the max number of result objects to return.
     * @return List of objects.
     */
    List<T> getAllOrderedLastUpdated(int first, int max);

    /**
     * Gets the count of objects which name is like the given name.
     *
     * @param name the name which result object names must be like.
     * @return the count of objects.
     */
    int getCountLikeName(String name);

    /**
     * Retrieves a list of objects referenced by the given collection of ids.
     *
     * @param ids a collection of ids.
     * @return a list of objects.
     */
    List<T> getById(Collection<Long> ids);

    /**
     * Retrieves a list of objects referenced by the given collection of uids.
     *
     * @param uids a collection of uids.
     * @return a list of objects.
     */
    List<T> getByUid(Collection<String> uids);

    /**
     * Retrieves a list of objects referenced by the given collection of uids.
     *
     * @param uids a collection of uids.
     * @return a list of objects.
     */
    List<T> getByUid(Collection<String> uids, User user);

    /**
     * Retrieves a list of objects referenced by the given collection of codes.
     *
     * @param codes a collection of codes.
     * @return a list of objects.
     */
    List<T> getByCode(Collection<String> codes);

    /**
     * Retrieves a list of objects referenced by the given collection of codes.
     *
     * @param codes a collection of codes.
     * @return a list of objects.
     */
    List<T> getByCode(Collection<String> codes, User user);

    /**
     * Retrieves a list of objects referenced by the given collection of names.
     *
     * @param names a collection of names.
     * @return a list of objects.
     */
    List<T> getByName(Collection<String> names);

    /**
     * Retrieves a list of objects referenced by the given List of uids.
     * Bypasses the ACL system.
     *
     * @param uids a List of uids.
     * @return a list of objects.
     */
    List<T> getByUidNoAcl(Collection<String> uids);

    /**
     * Returns all objects that are equal to or newer than given date.
     *
     * @param created Date to compare with.
     * @return All objects equal or newer than given date.
     */
    List<T> getAllGeCreated(Date created);

    /**
     * Returns all objects which are equal to or older than the given date.
     *
     * @param created Date to compare with.
     * @return All objects equals to or older than the given date.
     */
    List<T> getAllLeCreated(Date created);

    /**
     * Returns all objects that are equal to or newer than given date.
     *
     * @param lastUpdated Date to compare with.
     * @return All objects equal or newer than given date.
     */
    List<T> getAllGeLastUpdated(Date lastUpdated);

    /**
     * Returns all objects without considering sharing.
     *
     * @return a list of all objects.
     */
    List<T> getAllNoAcl();

    /**
     * Returns the date of the last updated object.
     *
     * @return a Date / time stamp.
     */
    Date getLastUpdated();

    /**
     * Returns the number of objects that are equal to or newer than given last updated date.
     *
     * @param lastUpdated Date to compare to.
     * @return the number of objects equal or newer than given date.
     */
    int getCountGeLastUpdated(Date lastUpdated);

    /**
     * Returns the number of objects that are equal to or newer than given created date.
     *
     * @param created Date to compare to.
     * @return the number of objects equal or newer than given date.
     */
    int getCountGeCreated(Date created);

    /**
     * Returns the UID of all objects created before a given date.
     *
     * @param date the date.
     * @return the UID of all objects created before a given date.
     */
    List<String> getUidsCreatedBefore(Date date);

    List<T> getDataReadAll();

    List<T> getDataReadAll(User user);

    List<T> getDataWriteAll();

    List<T> getDataWriteAll(User user);

    List<T> getDataReadAll(int first, int max);
}
