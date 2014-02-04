/*
 * Created on Dec 21, 2013
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
import org.assertj.core.util.Collections;
import org.assertj.core.util.Dates;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;

import static org.assertj.core.util.Strings.concat;
import static org.assertj.core.util.Strings.quote;

/**
 * Standard java object representation.
 * 
 * @author Mariusz Smykula
 */
public class StandardRepresentation implements Representation {

  /**
   * Returns standard the {@code toString} representation of the given object. It may or not the object's own
   * implementation of {@code toString}.
   * 
   * @param object the given object.
   * @return the {@code toString} representation of the given object.
   */
  @Override
  public String toStringOf(Object object) {
    if (object instanceof Calendar) {
      return toStringOf((Calendar) object);
    } else if (object instanceof Class<?>) {
      return toStringOf((Class<?>) object);
    } else if (object instanceof Date) {
      return toStringOf((Date) object);
    } else if (object instanceof Number) {
      return toStringOf((Number) object, this);
    } else if (object instanceof File) {
      return toStringOf((File) object);
    } else if (object instanceof String) {
      return toStringOf((String) object);
    } else if (object instanceof Character) {
      return toStringOf((Character) object);
    } else if (object instanceof Comparator) {
      return toStringOf((Comparator<?>) object);
    } else if (object instanceof SimpleDateFormat) {
      return toStringOf((SimpleDateFormat) object);
    } else if (object instanceof Tuple) {
      return toStringOf((Tuple) object, this);
    }
    return defaultToString(object, this);
  }

  private static String toStringOf(Number number, Representation representation) {
    if (number instanceof Float) {
      return toStringOf((Float) number);
    }
    if (number instanceof Long) {
      return toStringOf((Long) number);
    }
    return defaultToString(number, representation);
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

  private static String toStringOf(String s) {
    return concat("\"", s, "\"");
  }

  private static String toStringOf(Character c) {
    return concat("'", c, "'");
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

  private static String toStringOf(SimpleDateFormat dateFormat) {
    return dateFormat.toPattern();
  }

  private static String toStringOf(Tuple tuple, Representation representation) {
    return Collections.format(representation, Arrays.asList(tuple.toArray()), "(", ")");
  }

  private static String defaultToString(Object object, Representation representation) {
    return object == null ? null : CollectionToString.toStringOf(representation, object);
  }

}
