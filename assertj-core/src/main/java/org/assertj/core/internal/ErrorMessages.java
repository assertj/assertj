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
package org.assertj.core.internal;

/**
 * Provides validation error messages used by internal assertion implementations.
 *
 * @author Alex Ruiz
 * @author Yvonne Wang
 * @author Nicolas François
 */
public final class ErrorMessages {

  /**
   * Returns the message for a null array.
   *
   * @return the validation message
   */
  public static String arrayIsNull() {
    return "The given array should not be null";
  }

  /**
   * Returns the message for a null iterable.
   *
   * @return the validation message
   */
  public static String iterableIsNull() {
    return "The given iterable should not be null";
  }

  /**
   * Returns the message for empty keys.
   *
   * @param placeholder the key description
   * @return the validation message
   */
  public static String keysToLookForIsEmpty(String placeholder) {
    return "The %s to look for should not be empty".formatted(placeholder);
  }

  /**
   * Returns the message for null keys.
   *
   * @param placeholder the key description
   * @return the validation message
   */
  public static String keysToLookForIsNull(String placeholder) {
    return "The %s to look for should not be null".formatted(placeholder);
  }

  /**
   * Returns the message for empty entries.
   *
   * @return the validation message
   */
  public static String entriesToLookForIsEmpty() {
    return "The array of entries to look for should not be empty";
  }

  /**
   * Returns the message for null entries.
   *
   * @return the validation message
   */
  public static String entriesToLookForIsNull() {
    return "The array of entries to look for should not be null";
  }

  /**
   * Returns the message for a null map of entries.
   *
   * @return the validation message
   */
  public static String mapOfEntriesToLookForIsNull() {
    return "The map of entries to look for should not be null";
  }

  /**
   * Returns the message for a null entry.
   *
   * @return the validation message
   */
  public static String entryToLookForIsNull() {
    return "Entries to look for should not be null";
  }

  /**
   * Returns the message for a null iterable to search.
   *
   * @return the validation message
   */
  public static String iterableToLookForIsNull() {
    return "The iterable to look for should not be null";
  }

  /**
   * Returns the message for a null offset.
   *
   * @return the validation message
   */
  public static String offsetIsNull() {
    return "The given offset should not be null";
  }

  /**
   * Returns the message for a negative offset.
   *
   * @return the validation message
   */
  public static String offsetValueIsNotPositive() {
    return "An offset value should be greater than or equal to zero";
  }

  /**
   * Returns the message for a non-positive strict offset.
   *
   * @return the validation message
   */
  public static String strictOffsetValueIsNotStrictlyPositive() {
    return "A strict offset value should be greater than zero";
  }

  /**
   * Returns the message for an invalid percentage.
   *
   * @param number the invalid percentage
   * @return the validation message
   */
  public static String percentageValueIsInRange(Number number) {
    return "The percentage value <%s> should be greater than or equal to zero".formatted(number.doubleValue());
  }

  /**
   * Returns the message for a null regular expression.
   *
   * @return the validation message
   */
  public static String regexPatternIsNull() {
    return "The regular expression pattern to match should not be null";
  }

  /**
   * Returns the message for a null character sequence.
   *
   * @return the validation message
   */
  public static String charSequenceToLookForIsNull() {
    return "The char sequence to look for should not be null";
  }

  /**
   * Returns the message for empty values.
   *
   * @return the validation message
   */
  public static String valuesToLookForIsEmpty() {
    return "The array of values to look for should not be empty";
  }

  /**
   * Returns the message for null values.
   *
   * @return the validation message
   */
  public static String valuesToLookForIsNull() {
    return "The array of values to look for should not be null";
  }

  /**
   * Returns the message for an empty iterable of values.
   *
   * @return the validation message
   */
  public static String iterableValuesToLookForIsEmpty() {
    return "The iterable of values to look for should not be empty";
  }

  /**
   * Returns the message for a null iterable of values.
   *
   * @return the validation message
   */
  public static String iterableValuesToLookForIsNull() {
    return "The iterable of values to look for should not be null";
  }

  /**
   * Returns the message for a null comparison date.
   *
   * @return the validation message
   */
  public static String dateToCompareActualWithIsNull() {
    return "The date to compare actual with should not be null";
  }

  /**
   * Returns the message for a null period start date.
   *
   * @return the validation message
   */
  public static String startDateToCompareActualWithIsNull() {
    return "The start date of period to compare actual with should not be null";
  }

  /**
   * Returns the message for a null period end date.
   *
   * @return the validation message
   */
  public static String endDateToCompareActualWithIsNull() {
    return "The end date of period to compare actual with should not be null";
  }

  /**
   * Returns the message for a null array of values.
   *
   * @return the validation message
   */
  public static String arrayOfValuesToLookForIsNull() {
    return "The array of values to look for should not be null";
  }

  /**
   * Returns the message for an empty array of values.
   *
   * @return the validation message
   */
  public static String arrayOfValuesToLookForIsEmpty() {
    return "The array of values to look for should not be empty";
  }

  /**
   * Returns the message for a null predicate.
   *
   * @return the validation message
   */
  public static String predicateIsNull() {
    return "The predicate must not be null";
  }

  /**
   * Returns the message for an empty sequence.
   *
   * @return the validation message
   */
  public static String emptySequence() {
    return "The sequence of values should not be empty";
  }

  /**
   * Returns the message for an empty subsequence.
   *
   * @return the validation message
   */
  public static String emptySubsequence() {
    return "The subsequence of values should not be empty";
  }

  /**
   * Returns the message for a null sequence.
   *
   * @return the validation message
   */
  public static String nullSequence() {
    return "The sequence of values should not be null";
  }

  /**
   * Returns the message for a null subsequence.
   *
   * @return the validation message
   */
  public static String nullSubsequence() {
    return "The subsequence of values should not be null";
  }

  private ErrorMessages() {}
}
