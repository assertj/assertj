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
 * Copyright @2009-2013 the original author or authors.
 */
package org.assertj.core.presentation;

import java.util.Calendar;
import java.util.Date;

import static org.assertj.core.util.Strings.concat;

/**
 * Binary object representation instead of standard java representation.
 * 
 * @author Mariusz Smykula
 */
public class BinaryRepresentation implements Representation {

  public static final String BYTE_PREFIX = "0b";

  /**
   * Returns binary the {@code toString} representation of the given object. It may or not the object's own
   * implementation of {@code toString}.
   * 
   * @param object the given object.
   * @return the {@code toString} representation of the given object.
   */
  @Override
  public String toStringOf(Object object) {
    if (object instanceof Character) {
      return toStringOf((Character) object);
    } else if (object instanceof Number) {
      return toStringOf((Number) object);
    } else if (object instanceof String) {
      return toStringOf(this, (String) object);
    }
    return object == null ? null : CollectionToString.toStringOf(this, object);
  }

  private static String toStringOf(Representation representation, String s) {
    return concat("\"", representation.toStringOf(s.toCharArray()), "\"");
  }

  private static String toStringOf(Number number) {
    if (number instanceof Byte) {
      return toStringOf((Byte) number);
    } else if (number instanceof Short) {
      return toStringOf((Short) number);
    } else if (number instanceof Integer) {
      return toStringOf((Integer) number);
    } else if (number instanceof Long) {
      return toStringOf((Long) number);
    } else if (number instanceof Float) {
      return toStringOf((Float) number);
    } else if (number instanceof Double) {
      return toStringOf((Double) number);
    }
    return number == null ? null : number.toString();
  }

  private static String toStringOf(Byte b) {
    return toGroupedBinary(Integer.toBinaryString(b & 0xFF), 8);
  }

  private static String toStringOf(Short s) {
    return toGroupedBinary(Integer.toBinaryString(s & 0xFFFF), 16);
  }

  private static String toStringOf(Integer i) {
    return toGroupedBinary(Integer.toBinaryString(i), 32);
  }

  private static String toStringOf(Long l) {
    return toGroupedBinary(Long.toBinaryString(l), 64);
  }

  private static String toStringOf(Float f) {
    return toGroupedBinary(Integer.toBinaryString(Float.floatToIntBits(f)), 32);
  }

  private static String toStringOf(Double d) {
    return toGroupedBinary(Long.toBinaryString(Double.doubleToRawLongBits(d)), 64);
  }

  private static String toStringOf(Character character) {
    return concat("'", toStringOf((short) (int) character), "'");
  }

  private static String toGroupedBinary(String value, int size) {
    return BYTE_PREFIX + NumberGrouping.toBinaryLiteral(toBinary(value, size));
  }

  private static String toBinary(String value, int size) {
    return String.format("%" + size + "s", value).replace(' ', '0');
  }

}
