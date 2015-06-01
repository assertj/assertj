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

import static org.assertj.core.util.Strings.concat;
import static org.assertj.core.util.Strings.quote;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;

import org.assertj.core.util.DateUtil;

/**
 * Standard java object representation.
 * 
 * @author Mariusz Smykula
 */
public class StandardRepresentation implements Representation {

  // can share this at StandardRepresentation has no state
  public static final StandardRepresentation STANDARD_REPRESENTATION = new StandardRepresentation();

  /**
   * Returns standard the {@code toString} representation of the given object. It may or not the object's own
   * implementation of {@code toString}.
   * 
   * @param object the given object.
   * @return the {@code toString} representation of the given object.
   */
  @Override
  public String toStringOf(Object object) {
    if (object instanceof Calendar) return toStringOf((Calendar) object);
    if (object instanceof Class<?>) return toStringOf((Class<?>) object);
    if (object instanceof Date) return toStringOf((Date) object);
    if (object instanceof Number) return toStringOf((Number) object, this);
    if (object instanceof File) return toStringOf((File) object);
    if (object instanceof String) return toStringOf((String) object);
    if (object instanceof Character) return toStringOf((Character) object);
    if (object instanceof Comparator) return toStringOf((Comparator<?>) object);
    if (object instanceof SimpleDateFormat) return toStringOf((SimpleDateFormat) object);
    return DefaultToString.toStringOf(this, object);
  }

  private static String toStringOf(Number number, Representation representation) {
    if (number instanceof Float) return toStringOf((Float) number);
    if (number instanceof Long) return toStringOf((Long) number);
    return DefaultToString.toStringOf(representation, number);
  }

  private static String toStringOf(Comparator<?> comparator) {
    String comparatorSimpleClassName = comparator.getClass().getSimpleName();
    if (comparatorSimpleClassName.length() == 0) return quote("anonymous comparator class");
    // if toString has not been redefined, let's use comparator simple class name.
    if (comparator.toString().contains(comparatorSimpleClassName + "@")) return quote(comparatorSimpleClassName);
    return quote(comparator.toString());
  }

  private static String toStringOf(Calendar c) {
    return DateUtil.formatAsDatetime(c);
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
    return DateUtil.formatAsDatetimeWithMs(d);
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

}
