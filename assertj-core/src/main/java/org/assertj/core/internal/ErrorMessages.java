/*
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 *
 * Copyright 2012-2025 the original author or authors.
 */
package org.assertj.core.internal;

/**
 * @author Alex Ruiz
 * @author Yvonne Wang
 * @author Nicolas François
 */
public final class ErrorMessages {

  public static String arrayIsNull() {
    return "The given array should not be null";
  }

  public static String iterableIsNull() {
    return "The given iterable should not be null";
  }

  public static String keysToLookForIsEmpty(String placeholder) {
    return "The %s to look for should not be empty".formatted(placeholder);
  }

  public static String keysToLookForIsNull(String placeholder) {
    return "The %s to look for should not be null".formatted(placeholder);
  }

  public static String entriesToLookForIsEmpty() {
    return "The array of entries to look for should not be empty";
  }

  public static String entriesToLookForIsNull() {
    return "The array of entries to look for should not be null";
  }

  public static String mapOfEntriesToLookForIsNull() {
    return "The map of entries to look for should not be null";
  }

  public static String entryToLookForIsNull() {
    return "Entries to look for should not be null";
  }

  public static String iterableToLookForIsNull() {
    return "The iterable to look for should not be null";
  }

  public static String offsetIsNull() {
    return "The given offset should not be null";
  }

  public static String offsetValueIsNotPositive() {
    return "An offset value should be greater than or equal to zero";
  }

  public static String strictOffsetValueIsNotStrictlyPositive() {
    return "A strict offset value should be greater than zero";
  }

  public static String percentageValueIsInRange(Number number) {
    return "The percentage value <%s> should be greater than or equal to zero".formatted(number.doubleValue());
  }

  public static String regexPatternIsNull() {
    return "The regular expression pattern to match should not be null";
  }

  public static String charSequenceToLookForIsNull() {
    return "The char sequence to look for should not be null";
  }

  public static String valuesToLookForIsEmpty() {
    return "The array of values to look for should not be empty";
  }

  public static String valuesToLookForIsNull() {
    return "The array of values to look for should not be null";
  }

  public static String iterableValuesToLookForIsEmpty() {
    return "The iterable of values to look for should not be empty";
  }

  public static String iterableValuesToLookForIsNull() {
    return "The iterable of values to look for should not be null";
  }

  public static String dateToCompareActualWithIsNull() {
    return "The date to compare actual with should not be null";
  }

  public static String startDateToCompareActualWithIsNull() {
    return "The start date of period to compare actual with should not be null";
  }

  public static String endDateToCompareActualWithIsNull() {
    return "The end date of period to compare actual with should not be null";
  }

  public static String arrayOfValuesToLookForIsNull() {
    return "The array of values to look for should not be null";
  }

  public static String arrayOfValuesToLookForIsEmpty() {
    return "The array of values to look for should not be empty";
  }

  public static String predicateIsNull() {
    return "The predicate must not be null";
  }

  public static String emptySequence() {
    return "The sequence of values should not be empty";
  }

  public static String emptySubsequence() {
    return "The subsequence of values should not be empty";
  }

  public static String nullSequence() {
    return "The sequence of values should not be null";
  }

  public static String nullSubsequence() {
    return "The subsequence of values should not be null";
  }

  private ErrorMessages() {}
}
