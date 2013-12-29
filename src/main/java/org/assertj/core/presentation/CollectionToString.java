/*
 * Created on Oct 7, 2009
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 * 
 * Copyright @2009-2012 the original author or authors.
 */
package org.assertj.core.presentation;

import org.assertj.core.groups.Tuple;
import org.assertj.core.util.*;
import org.assertj.core.util.Collections;

import static org.assertj.core.util.Arrays.isArray;

import java.util.*;
import java.util.Arrays;

/**
 * Obtains the {@code toString} representation of an collection.
 * 
 * @author Alex Ruiz
 * @author Joel Costigliola
 * @author Yvonne Wang
 * @author Mariusz Smykula
 */
final class CollectionToString {

  /**
   * Returns the {@code toString} representation of the given collection. It may or not the object's own implementation
   * of {@code toString}.
   * 
   * @param o the given object.
   * @return the {@code toString} representation of the given object.
   */
  public static String toStringOf(Representation representation, Object o) {
    if (isArray(o)) {
      return org.assertj.core.util.Arrays.format(representation, o);
    } else if (o instanceof Collection<?>) {
      return toStringOf((Collection<?>) o, representation);
    } else if (o instanceof Map<?, ?>) {
      return toStringOf((Map<?, ?>) o, representation);
    } else if (o instanceof Tuple) {
      return toStringOf((Tuple) o, representation);
    }
    return defaultToString(o);
  }

  private static String toStringOf(Collection<?> c, Representation p) {
    return org.assertj.core.util.Collections.format(p, c);
  }

  private static String toStringOf(Map<?, ?> m, Representation p) {
    return Maps.format(p, m);
  }

  private static String toStringOf(Tuple tuple, Representation representation) {
    return Collections.format(representation, Arrays.asList(tuple.toArray()), "(", ")");
  }

  private static String defaultToString(Object o) {
    return o == null ? null : o.toString();
  }

  private CollectionToString() {

  }
}
