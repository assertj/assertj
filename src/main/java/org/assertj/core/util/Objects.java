/**
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 *
 * Copyright 2012-2017 the original author or authors.
 */
package org.assertj.core.util;

import static org.assertj.core.util.Arrays.*;

import java.lang.reflect.Array;

/**
 * Utility methods related to objects.
 *
 * @author Alex Ruiz
 * @author Joel Costigliola
 */
public final class Objects {

  /** Prime number used to calculate the hash code of objects. */
  public static final int HASH_CODE_PRIME = 31;

  /**
   * Returns {@code true} if the given objects are equal or if both objects are {@code null}.
   *
   * @param o1 one of the objects to compare.
   * @param o2 one of the objects to compare.
   * @return {@code true} if the given objects are equal or if both objects are {@code null}.
   */
  public static boolean areEqual(Object o1, Object o2) {
    if (o1 == null) {
      return o2 == null;
    }
    if (o1.equals(o2)) {
      return true;
    }
    return areEqualArrays(o1, o2);
  }

  public static boolean areEqualArrays(Object o1, Object o2) {
    if (!isArray(o1) || !isArray(o2)) {
      return false;
    }
    if (o1 == o2) {
      return true;
    }
    int size = Array.getLength(o1);
    if (Array.getLength(o2) != size) {
      return false;
    }
    for (int i = 0; i < size; i++) {
      Object e1 = Array.get(o1, i);
      Object e2 = Array.get(o2, i);
      if (!areEqual(e1, e2)) {
        return false;
      }
    }
    return true;
  }

  /**
   * Returns an array containing the names of the given types.
   *
   * @param types the given types.
   * @return the names of the given types stored in an array.
   */
  public static String[] namesOf(Class<?>... types) {
    if (isNullOrEmpty(types)) {
      return new String[0];
    }
    String[] names = new String[types.length];
    for (int i = 0; i < types.length; i++) {
      names[i] = types[i].getName();
    }
    return names;
  }

  /**
   * Returns the hash code for the given object. If the object is {@code null}, this method returns zero. Otherwise
   * calls the method {@code hashCode} of the given object.
   *
   * @param o the given object.
   * @return the hash code for the given object
   */
  public static int hashCodeFor(Object o) {
    if (o == null) return 0;
    return isArray(o) && !o.getClass().getComponentType().isPrimitive() ? java.util.Arrays.deepHashCode((Object[]) o) : o.hashCode() ;
  }

  /**
   * Casts the given object to the given type only if the object is of the given type. If the object is not of the given
   * type, this method returns {@code null}.
   *
   * @param <T> the generic type to cast the given object to.
   * @param o the object to cast.
   * @param type the given type.
   * @return the casted object, or {@code null} if the given object is not to the given type.
   */
  public static <T> T castIfBelongsToType(Object o, Class<T> type) {
    if (o != null && type.isAssignableFrom(o.getClass())) {
      return type.cast(o);
    }
    return null;
  }

  private Objects() {}
}
