package com.mass3d.hibernate;

import java.util.List;
import java.util.function.Function;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import org.hibernate.Criteria;
import org.hibernate.criterion.DetachedCriteria;
import com.mass3d.common.GenericStore;
import com.mass3d.user.User;
import com.mass3d.user.UserInfo;

/**
 * Interface which extends GenericStore and exposes support methods for retrieving
 * criteria.
 *
 */
public interface InternalHibernateGenericStore<T>
    extends GenericStore<T>
{
    Criteria getCriteria();

    Criteria getSharingCriteria();

    Criteria getSharingCriteria(User user);

    DetachedCriteria getDataSharingDetachedCriteria(User user);

    Criteria getExecutableCriteria(DetachedCriteria detachedCriteria);

    DetachedCriteria getSharingDetachedCriteria();

    DetachedCriteria getSharingDetachedCriteria(String access);

    DetachedCriteria getDataSharingDetachedCriteria(String access);

    DetachedCriteria getSharingDetachedCriteria(User user);

    List<Function<Root<T>, Predicate>> getSharingPredicates(CriteriaBuilder builder);

    List<Function<Root<T>, Predicate>> getSharingPredicates(CriteriaBuilder builder, UserInfo user);

    List<Function<Root<T>, Predicate>> getSharingPredicates(CriteriaBuilder builder, User user);

    List<Function<Root<T>, Predicate>> getSharingPredicates(CriteriaBuilder builder, String access);

    List<Function<Root<T>, Predicate>> getSharingPredicates(CriteriaBuilder builder, UserInfo user,
        String access);

    List<Function<Root<T>, Predicate>> getSharingPredicates(CriteriaBuilder builder, User user,
        String access);

    List<Function<Root<T>, Predicate>> getDataSharingPredicates(CriteriaBuilder builder);

    List<Function<Root<T>, Predicate>> getDataSharingPredicates(CriteriaBuilder builder,
        UserInfo user);

    List<Function<Root<T>, Predicate>> getDataSharingPredicates(CriteriaBuilder builder, User user);

    List<Function<Root<T>, Predicate>> getDataSharingPredicates(CriteriaBuilder builder,
        String access);

    List<Function<Root<T>, Predicate>> getDataSharingPredicates(CriteriaBuilder builder, User user,
        String access);

    List<Function<Root<T>, Predicate>> getDataSharingPredicates(CriteriaBuilder builder,
        UserInfo user, String access);
}
