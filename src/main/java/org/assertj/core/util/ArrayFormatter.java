/*
 * Created on Mar 29, 2009
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
package org.assertj.core.util;

import org.assertj.core.presentation.Representation;

import static java.lang.reflect.Array.getLength;
import static org.assertj.core.util.Arrays.isArray;

import java.lang.reflect.Array;
import java.util.HashSet;
import java.util.Set;

/**
 * Creates a {@code String} representation of an array.
 *
 * @author Alex Ruiz
 * @author Joel Costigliola
 */
final class ArrayFormatter {
  private static final String NULL = "null";

  String format(Representation p, Object o) {
    if (!isArray(o)) {
      return null;
    }
    return isObjectArray(o) ? formatObjectArray(p, o) : formatPrimitiveArray(p, o);
  }

  private String formatObjectArray(Representation p, Object o) {
    Object[] array = (Object[]) o;
    int size = array.length;
    if (size == 0) {
      return "[]";
    }
    StringBuilder buffer = new StringBuilder((20 * (size - 1)));
    deepToString(p, array, buffer, new HashSet<Object[]>());
    return buffer.toString();
  }

  private void deepToString(Representation p, Object[] array, StringBuilder buffer, Set<Object[]> alreadyFormatted) {
    if (array == null) {
      buffer.append(NULL);
      return;
    }
    alreadyFormatted.add(array);
    buffer.append('[');
    int size = array.length;
    for (int i = 0; i < size; i++) {
      if (i != 0) {
        buffer.append(", ");
      }
      Object element = array[i];
      if (!isArray(element)) {
        buffer.append(element == null ? NULL : p.toStringOf(element));
        continue;
      }
      if (!isObjectArray(element)) {
        buffer.append(formatPrimitiveArray(p, element));
        continue;
      }
      if (alreadyFormatted.contains(element)) {
        buffer.append("[...]");
        continue;
      }
      deepToString(p, (Object[]) element, buffer, alreadyFormatted);
    }
    buffer.append(']');
    alreadyFormatted.remove(array);
  }

  private boolean isObjectArray(Object o) {
    return isArray(o) && !isArrayTypePrimitive(o);
  }

  private String formatPrimitiveArray(Representation p, Object o) {
    if (!isArray(o)) {
      return null;
    }
    if (!isArrayTypePrimitive(o)) {
      throw notAnArrayOfPrimitives(o);
    }
    int size = getLength(o);
    if (size == 0) {
      return "[]";
    }
    StringBuilder buffer = new StringBuilder();
    buffer.append('[');
    buffer.append(p.toStringOf(Array.get(o, 0)));
    for (int i = 1; i < size; i++) {
      Object element = Array.get(o, i);
      buffer.append(", ");
      buffer.append(p.toStringOf(element));
    }
    buffer.append("]");
    return buffer.toString();
  }

  private boolean isArrayTypePrimitive(Object o) {
    return o.getClass().getComponentType().isPrimitive();
  }

  private IllegalArgumentException notAnArrayOfPrimitives(Object o) {
    return new IllegalArgumentException(String.format("<%s> is not an array of primitives", o));
  }
}
