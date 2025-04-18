/*
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 *
 * Copyright 2012-2025 the original author or authors.
 */
package org.assertj.core.util;

import static org.assertj.core.util.Arrays.isArray;
import static org.assertj.core.util.Arrays.isNullOrEmpty;

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
    return isArray(o) && !o.getClass().getComponentType().isPrimitive()
        ? java.util.Arrays.deepHashCode((Object[]) o)
        : o.hashCode();
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
