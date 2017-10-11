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
 * Copyright 2012-2017 the original author or authors.
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
   * Verifies that the given {@code CharSequence} is not {@code null} or empty.
   * 
   * @param s the given {@code CharSequence}.
   * @return the validated {@code CharSequence}.
   * @throws NullPointerException if the given {@code CharSequence} is {@code null}.
   * @throws IllegalArgumentException if the given {@code CharSequence} is empty.
   */
  public static CharSequence checkNotNullOrEmpty(CharSequence s) {
    return checkNotNullOrEmpty(s, ARGUMENT_EMPTY);
  }

  /**
   * Verifies that the given {@code CharSequence} is not {@code null} or empty.
   * 
   * @param s the given {@code CharSequence}.
   * @param message error message in case of empty {@code String}.
   * @return the validated {@code CharSequence}.
   * @throws NullPointerException if the given {@code CharSequence} is {@code null}.
   * @throws IllegalArgumentException if the given {@code CharSequence} is empty.
   */
  public static CharSequence checkNotNullOrEmpty(CharSequence s, String message) {
    checkNotNull(s, message);
    if (s.length() == 0) throwExceptionForBeingEmpty(message);
    return s;
  }

  /**
   * Verifies that the given array is not {@code null} or empty.
   * 
   * @param <T> the type of elements of the array.
   * @param array the given array.
   * @return the validated array.
   * @throws NullPointerException if the given array is {@code null}.
   * @throws IllegalArgumentException if the given array is empty.
   */
  public static <T> T[] checkNotNullOrEmpty(T[] array) {
    T[] checked = checkNotNull(array);
    if (checked.length == 0) throwExceptionForBeingEmpty();
    return checked;
  }

  /**
   * Verifies that the given object reference is not {@code null}.
   * 
   * @param <T> the type of the reference to check.
   * @param reference the given object reference.
   * @return the non-{@code null} reference that was validated.
   * @throws NullPointerException if the given object reference is {@code null}.
   */
  public static <T> T checkNotNull(T reference) {
    if (reference == null) throw new NullPointerException();
    return reference;
  }

  /**
   * Verifies that the given object reference is not {@code null}.
   * 
   * @param <T> the type of the reference to check.
   * @param reference the given object reference.
   * @param message error message in case of null reference.
   * @return the non-{@code null} reference that was validated.
   * @throws NullPointerException if the given object reference is {@code null}.
   */
  public static <T> T checkNotNull(T reference, String message) {
    if (reference == null) throw new NullPointerException(message);
    return reference;
  }

  /**
   * Verifies that the given {@link FilterOperator} reference is not {@code null}.
   * 
   * @param <T> the type of the FilterOperator to check.
   * @param filterOperator the given {@link FilterOperator} reference.
   * @throws IllegalArgumentException if the given {@link FilterOperator} reference is {@code null}.
   */
  public static <T> void checkNotNull(FilterOperator<T> filterOperator) {
    checkArgument(filterOperator != null, "The expected value should not be null.%n"
        + "If you were trying to filter on a null value, please use filteredOnNull(String propertyOrFieldName) instead");
  }
  
  /**
   * Ensures the truth of an expression involving one or more parameters to the calling method.
   * <p>
   * Borrowed from Guava.
   *
   * @param expression a boolean expression
   * @param errorMessageTemplate a template for the exception message should the check fail. The
   *     message is formed by calling {@link String#format(String, Object...)} with the given parameters.
   * @param errorMessageArgs the arguments to be substituted into the message template.
   * @throws IllegalArgumentException if {@code expression} is false
   * @throws NullPointerException if the check fails and either {@code errorMessageTemplate} or
   *     {@code errorMessageArgs} is null (don't let this happen)
   */
  public static void checkArgument(boolean expression, String errorMessageTemplate, Object... errorMessageArgs) {
    if (!expression) throw new IllegalArgumentException(format(errorMessageTemplate, errorMessageArgs));
  }

  /**
   * Ensures the truth of an expression involving the state of the calling instance, but not
   * involving any parameters to the calling method.
   * <p>
   * Borrowed from Guava.
   *
   * @param expression a boolean expression
   * @param errorMessageTemplate a template for the exception message should the check fail.The
   *     message is formed by calling {@link String#format(String, Object...)} with the given parameters.
   * @param errorMessageArgs the arguments to be substituted into the message template. Arguments
   *     are converted to strings using {@link String#valueOf(Object)}.
   * @throws IllegalStateException if {@code expression} is false
   * @throws NullPointerException if the check fails and either {@code errorMessageTemplate} or
   *     {@code errorMessageArgs} is null (don't let this happen)
   */
  public static void checkState(boolean expression, String errorMessageTemplate, Object... errorMessageArgs) {
    if (!expression) {
      throw new IllegalStateException(format(errorMessageTemplate, errorMessageArgs));
    }
  }

  private Preconditions() {}

  private static void throwExceptionForBeingEmpty() {
    throwExceptionForBeingEmpty(ARGUMENT_EMPTY);
  }

  private static void throwExceptionForBeingEmpty(String message) {
    throw new IllegalArgumentException(message);
  }
}
