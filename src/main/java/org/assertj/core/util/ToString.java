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
package org.assertj.core.util;

import static org.assertj.core.util.Arrays.isArray;
import static org.assertj.core.util.Strings.quote;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Obtains the {@code toString} representation of an object.
 *
 * @author Alex Ruiz
 * @author Joel Costigliola
 * @author Yvonne Wang
 */
public final class ToString {
  
  /**
   * Returns the {@code toString} representation of the given object. It may or not the object's own implementation of
   * {@code toString}.
   *
   * @param o the given object.
   * @return the {@code toString} representation of the given object.
   */
  public static String toStringOf(Object o) {
    if (isArray(o)) {
      return Arrays.format(o);
    }
    if (o instanceof Calendar) {
      return toStringOf((Calendar) o);
    }
    if (o instanceof Class<?>) {
      return toStringOf((Class<?>) o);
    }
    if (o instanceof Collection<?>) {
      return toStringOf((Collection<?>) o);
    }
    if (o instanceof Date) {
      return toStringOf((Date) o);
    }
    if (o instanceof Float) {
      return toStringOf((Float) o);
    }
    if (o instanceof Long) {
      return toStringOf((Long) o);
    }
    if (o instanceof File) {
      return toStringOf((File) o);
    }
    if (o instanceof Map<?, ?>) {
      return toStringOf((Map<?, ?>) o);
    }
    if (o instanceof String) {
      return quote((String) o);
    }
    if (o instanceof Comparator) {
      return toStringOf((Comparator<?>) o);
    }
    if (o instanceof SimpleDateFormat) {
      return toStringOf((SimpleDateFormat) o);
    }
    return o == null ? null : o.toString();
  }

  private static String toStringOf(Comparator<?> comparator) {
    String comparatorSimpleClassName = comparator.getClass().getSimpleName();
    return quote(!comparatorSimpleClassName.isEmpty() ? comparatorSimpleClassName : "Anonymous Comparator class");
  }

  private static String toStringOf(Calendar c) {
    return Dates.formatAsDatetime(c);
  }

  private static String toStringOf(Class<?> c) {
    return c.getCanonicalName();
  }

  private static String toStringOf(Collection<?> c) {
    return Collections.format(c);
  }

  private static String toStringOf(Date d) {
    return Dates.formatAsDatetime(d);
  }

  private static String toStringOf(Float f) {
    return String.format("%sf", f);
  }

  private static String toStringOf(Long l) {
    return String.format("%sL", l);
  }

  private static String toStringOf(File f) {
    return f.getAbsolutePath();
  }

  private static String toStringOf(Map<?, ?> m) {
    return Maps.format(m);
  }

  private static String toStringOf(SimpleDateFormat dateFormat) {
    return dateFormat.toPattern();
  }

  private ToString() {}
}
