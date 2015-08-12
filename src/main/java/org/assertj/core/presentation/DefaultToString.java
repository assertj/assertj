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
 * Copyright 2012-2015 the original author or authors.
 */
package org.assertj.core.presentation;

import static org.assertj.core.util.Arrays.isArray;
import static org.assertj.core.util.IterableUtil.singleLineFormat;

import java.nio.file.Path;
import java.util.Collection;
import java.util.Map;

import org.assertj.core.data.MapEntry;
import org.assertj.core.groups.Tuple;
import org.assertj.core.util.Arrays;
import org.assertj.core.util.IterableUtil;
import org.assertj.core.util.Maps;

/**
 * Obtains the {@code toString} representation of a group of values (array, iterable, map, tuple).
 * 
 * @author Alex Ruiz
 * @author Joel Costigliola
 * @author Yvonne Wang
 * @author Mariusz Smykula
 */
public final class DefaultToString {

  private static final String TUPPLE_START = "(";
  private static final String TUPPLE_END = ")";

  /**
   * Returns the {@code toString} representation of the given group. It may or not the object's own implementation of
   * {@code toString}.
   * 
   * @param o the given object.
   * @return the {@code toString} representation of the given object.
   */
  public static String toStringOf(Representation representation, Object o) {
    if (o instanceof Path) return o.toString();
    if (isArray(o)) return Arrays.format(representation, o);
    if (o instanceof Collection<?>) return IterableUtil.smartFormat(representation, (Collection<?>) o);
    if (o instanceof Map<?, ?>) return Maps.format(representation, (Map<?, ?>) o);
    if (o instanceof Tuple) return toStringOf((Tuple) o, representation);
    if (o instanceof MapEntry) return toStringOf((MapEntry<?, ?>) o, representation);
    return o == null ? null : o.toString();
  }

  public static String toStringOf(Tuple tuple, Representation representation) {
    return singleLineFormat(representation, tuple.toList(), TUPPLE_START, TUPPLE_END);
  }

  public static String toStringOf(MapEntry<?, ?> mapEntry, Representation representation) {
    return String.format("MapEntry[key=%s, value=%s]", representation.toStringOf(mapEntry.key), representation.toStringOf(mapEntry.value));
  }

  private DefaultToString() {}
}
