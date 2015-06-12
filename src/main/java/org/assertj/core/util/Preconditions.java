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
package org.assertj.core.util;

import static java.lang.String.format;

import org.assertj.core.api.filter.FilterOperator;

/**
 * Verifies correct argument values and state. Borrowed from Guava.
 * 
 * @author alruiz@google.com (Alex Ruiz)
 */
public final class Preconditions {
  public static final String ARGUMENT_EMPTY = "Argument expected not to be empty!";

  /**
   * Verifies that the given {@code String} is not {@code null} or empty.
   * 
   * @param s the given {@code String}.
   * @return the validated {@code String}.
   * @throws NullPointerException if the given {@code String} is {@code null}.
   * @throws IllegalArgumentException if the given {@code String} is empty.
   */
  public static String checkNotNullOrEmpty(String s) {
    return checkNotNullOrEmpty(s, ARGUMENT_EMPTY);
  }

  /**
   * Verifies that the given {@code String} is not {@code null} or empty.
   * 
   * @param s the given {@code String}.
   * @param message error message in case of empty {@code String}.
   * @return the validated {@code String}.
   * @throws NullPointerException if the given {@code String} is {@code null}.
   * @throws IllegalArgumentException if the given {@code String} is empty.
   */
  public static String checkNotNullOrEmpty(String s, String message) {
    checkNotNull(s, message);
    if (s.isEmpty()) {
      throwExceptionForBeingEmpty(message);
    }
    return s;
  }

  /**
   * Verifies that the given array is not {@code null} or empty.
   * 
   * @param array the given array.
   * @return the validated array.
   * @throws NullPointerException if the given array is {@code null}.
   * @throws IllegalArgumentException if the given array is empty.
   */
  public static <T> T[] checkNotNullOrEmpty(T[] array) {
    T[] checked = checkNotNull(array);
    if (checked.length == 0) {
      throwExceptionForBeingEmpty();
    }
    return checked;
  }

  /**
   * Verifies that the given object reference is not {@code null}.
   * 
   * @param reference the given object reference.
   * @return the non-{@code null} reference that was validated.
   * @throws NullPointerException if the given object reference is {@code null}.
   */
  public static <T> T checkNotNull(T reference) {
    if (reference == null) {
      throw new NullPointerException();
    }
    return reference;
  }

  /**
   * Verifies that the given object reference is not {@code null}.
   * 
   * @param reference the given object reference.
   * @param message error message in case of null reference.
   * @return the non-{@code null} reference that was validated.
   * @throws NullPointerException if the given object reference is {@code null}.
   */
  public static <T> T checkNotNull(T reference, String message) {
    if (reference == null) {
      throw new NullPointerException(message);
    }
    return reference;
  }

  /**
   * Verifies that the given FilterOperator reference is not {@code null}.
   * 
   * @param reference the given object reference.
   * @throws NullPointerException if the given object reference is {@code null}.
   */
  public static <T> void checkNotNull(FilterOperator<T> filterOperator) {
    // @format:off
    if (filterOperator == null) throw new IllegalArgumentException(format("The expected value should not be null.%n"
        + "If you were trying to filter on a null value, please use filteredOnNull(String propertyOrFieldName) instead"));
    // @format:on
  }

  private Preconditions() {}

  private static void throwExceptionForBeingEmpty() {
    throwExceptionForBeingEmpty(ARGUMENT_EMPTY);
  }

  private static void throwExceptionForBeingEmpty(String message) {
    throw new IllegalArgumentException(message);
  }
}
