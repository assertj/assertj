/*
 * Copyright 2012-2026 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.assertj.core.presentation;

import static org.assertj.core.util.Strings.concat;

/**
 * Binary object representation instead of standard java representation.
 * 
 * @author Mariusz Smykula
 */
public class BinaryRepresentation extends StandardRepresentation {

  /** Shared binary representation instance. */
  public static final BinaryRepresentation BINARY_REPRESENTATION = new BinaryRepresentation();

  /** Prefix used for binary byte values. */
  public static final String BYTE_PREFIX = "0b";

  /** Creates a new binary representation. */
  public BinaryRepresentation() {}

  /**
   * Returns binary the {@code toString} representation of the given object. It may or not the object's own
   * implementation of {@code toString}.
   * 
   * @param object the given object.
   * @return the {@code toString} representation of the given object.
   */
  @Override
  public String toStringOf(Object object) {
    if (hasCustomFormatterFor(object)) return customFormat(object);
    if (object instanceof Character character) return toStringOf(character);
    if (object instanceof Number number) return toStringOf(number);
    if (object instanceof String string) return toStringOf(this, string);
    return super.toStringOf(object);
  }

  /**
   * Formats a string using the given representation for its characters.
   *
   * @param representation the character representation
   * @param s the string to format
   * @return the formatted string
   */
  protected String toStringOf(Representation representation, String s) {
    return concat("\"", representation.toStringOf(s.toCharArray()), "\"");
  }

  @Override
  protected String toStringOf(Number number) {
    if (number instanceof Byte b) return toStringOf(b);
    if (number instanceof Short s) return toStringOf(s);
    if (number instanceof Integer i) return toStringOf(i);
    if (number instanceof Long l) return toStringOf(l);
    if (number instanceof Float f) return toStringOf(f);
    if (number instanceof Double d) return toStringOf(d);
    return number == null ? null : number.toString();
  }

  /**
   * Formats a byte in binary.
   *
   * @param b the byte to format
   * @return the binary representation
   */
  protected String toStringOf(Byte b) {
    return toGroupedBinary(Integer.toBinaryString(b & 0xFF), 8);
  }

  /**
   * Formats a short in binary.
   *
   * @param s the short to format
   * @return the binary representation
   */
  protected String toStringOf(Short s) {
    return toGroupedBinary(Integer.toBinaryString(s & 0xFFFF), 16);
  }

  /**
   * Formats an integer in binary.
   *
   * @param i the integer to format
   * @return the binary representation
   */
  protected String toStringOf(Integer i) {
    return toGroupedBinary(Integer.toBinaryString(i), 32);
  }

  /** {@inheritDoc} */
  @Override
  protected String toStringOf(Long l) {
    return toGroupedBinary(Long.toBinaryString(l), 64);
  }

  /** {@inheritDoc} */
  @Override
  protected String toStringOf(Float f) {
    return toGroupedBinary(Integer.toBinaryString(Float.floatToIntBits(f)), 32);
  }

  /**
   * Formats a double in binary.
   *
   * @param d the double to format
   * @return the binary representation
   */
  protected String toStringOf(Double d) {
    return toGroupedBinary(Long.toBinaryString(Double.doubleToRawLongBits(d)), 64);
  }

  /** {@inheritDoc} */
  @Override
  protected String toStringOf(Character character) {
    return concat("'", toStringOf((short) (int) character), "'");
  }

  private static String toGroupedBinary(String value, int size) {
    return BYTE_PREFIX + NumberGrouping.toBinaryLiteral(toBinary(value, size));
  }

  private static String toBinary(String value, int size) {
    return String.format("%" + size + "s", value).replace(' ', '0');
  }

}
