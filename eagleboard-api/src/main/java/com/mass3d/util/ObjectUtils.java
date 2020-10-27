package com.mass3d.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.apache.commons.lang3.StringUtils;

public class ObjectUtils {

  /**
   * Returns the first non-null argument. Returns null if all arguments are null.
   *
   * @param objects the objects.
   * @return the first non-null argument.
   */
  @SafeVarargs
  public static <T> T firstNonNull(T... objects) {
    if (objects != null) {
      for (T object : objects) {
        if (object != null) {
          return object;
        }
      }
    }

    return null;
  }

  /**
   * Indicates whether all of the given argument object are not null.
   *
   * @param objects the objects.
   * @return true if all of the given argument object are not null.
   */
  public static boolean allNonNull(Object... objects) {
    if (objects == null) {
      return false;
    }

    for (Object object : objects) {
      if (object == null) {
        return false;
      }
    }

    return true;
  }

  /**
   * Indicates whether any of the given conditions are not null and true.
   *
   * @param conditions the conditions.
   * @return whether any of the given conditions are not null and true.
   */
  public static boolean anyIsTrue(Boolean... conditions) {
    if (conditions != null) {
      for (Boolean condition : conditions) {
        if (condition != null && condition.booleanValue()) {
          return true;
        }
      }
    }

    return false;
  }

  /**
   * Indicates whether any of the given conditions are not null and false.
   *
   * @param conditions the conditions.
   * @return whether any of the given conditions are not null and false.
   */
  public static boolean anyIsFalse(Boolean... conditions) {
    if (conditions != null) {
      for (Boolean condition : conditions) {
        if (condition != null && !condition.booleanValue()) {
          return true;
        }
      }
    }

    return false;
  }

  /**
   * Returns a list of strings, where the strings are the result of calling String.valueOf( Object )
   * of each object in the given collection.
   *
   * @param objects the collection of objects.
   * @return a list of strings.
   */
  public static List<String> asStringList(Collection<?> objects) {
    List<String> list = new ArrayList<>();

    for (Object object : objects) {
      list.add(String.valueOf(object));
    }

    return list;
  }

  /**
   * Joins the elements of the provided collection into a string. The provided string mapping
   * function is used to produce the string for each object. Null is returned if the provided
   * collection is null.
   *
   * @param collection the collection of elements.
   * @param separator the separator of elements in the returned string.
   * @param stringMapper the function to produce the string for each object.
   * @return the joined string.
   */
  public static <T> String join(Collection<T> collection, String separator,
      Function<T, String> stringMapper) {
    if (collection == null) {
      return null;
    }

    List<String> list = collection.stream().map(stringMapper).collect(Collectors.toList());

    return StringUtils.join(list, separator);
  }
}
