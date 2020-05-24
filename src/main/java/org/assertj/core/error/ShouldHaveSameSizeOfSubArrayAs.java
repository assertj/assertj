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
 * Copyright 2012-2020 the original author or authors.
 */
package org.assertj.core.error;

import static java.lang.String.format;

import java.util.Arrays;

/**
 * Creates an error message indicating that an assertion that verifies that some subarray (in multidimensional arrays)
 * has certain size failed.
 *
 * @author Maciej Wajcht
 */
public class ShouldHaveSameSizeOfSubArrayAs extends BasicErrorMessageFactory {

  private static final String MESSAGE = "%n"
    + "Actual and expected should have same size but actual[%s] size is:%n"
    + " <%s>%n"
    + "while expected[%s] size is:%n"
    + " <%s>%n"
    + "Actual[%s] was:%n"
    + " %s%n"
    + "Expected[%s] was:%n"
    + " %s%n"
    + "Actual was:%n"
    + " %s%n"
    + "Expected was:%n"
    + " %s";

  /**
   * Creates a new <code>{@link ShouldHaveSameSizeOfSubArrayAs}</code>.
   * @param actual the actual value in the failed assertion.
   * @param expected the expected value in the failed assertion.
   * @param actualSubArray next dimension of the {@code actual} multidimensional array
   * @param expectedSubArray next dimension of the {@code expected} multidimensional array
   * @param actualSubArraySize the size of next dimension of the {@code actual}.
   * @param expectedSubArraySize the size of next dimension of the{@code expected}.
   * @param index index of {@code actualSubArray}, e.g. {@code 3} when checking size (length) of {@code actual[3]}
   * @return the created {@code ErrorMessageFactory}
   */
  public static ErrorMessageFactory shouldHaveSameSizeOfSubarrayAs(int[][] actual, int[][] expected,
    int[] actualSubArray, int[] expectedSubArray, int actualSubArraySize, int expectedSubArraySize, int index) {
    return new ShouldHaveSameSizeOfSubArrayAs(actual, expected, actualSubArray, expectedSubArray, actualSubArraySize,
      expectedSubArraySize, index);
  }

  /**
   * Creates a new <code>{@link ShouldHaveSameSizeOfSubArrayAs}</code>.
   * @param actual the actual value in the failed assertion.
   * @param expected the expected value in the failed assertion.
   * @param actualSubArray next dimension of the {@code actual} multidimensional array
   * @param expectedSubArray next dimension of the {@code expected} multidimensional array
   * @param actualSubArraySize the size of next dimension of the {@code actual}.
   * @param expectedSubArraySize the size of next dimension of the{@code expected}.
   * @param index index of {@code actualSubArray}, e.g. {@code 3} when checking size (length) of {@code actual[3]}
   * @return the created {@code ErrorMessageFactory}
   */
  public static ErrorMessageFactory shouldHaveSameSizeOfSubarrayAs(float[][] actual, float[][] expected,
    float[] actualSubArray, float[] expectedSubArray, int actualSubArraySize, int expectedSubArraySize, int index) {
    return new ShouldHaveSameSizeOfSubArrayAs(actual, expected, actualSubArray, expectedSubArray, actualSubArraySize,
      expectedSubArraySize, index);
  }

  /**
   * Creates a new <code>{@link ShouldHaveSameSizeOfSubArrayAs}</code>.
   * @param actual the actual value in the failed assertion.
   * @param expected the expected value in the failed assertion.
   * @param actualSubArray next dimension of the {@code actual} multidimensional array
   * @param expectedSubArray next dimension of the {@code expected} multidimensional array
   * @param actualSubArraySize the size of next dimension of the {@code actual}.
   * @param expectedSubArraySize the size of next dimension of the{@code expected}.
   * @param index index of {@code actualSubArray}, e.g. {@code 3} when checking size (length) of {@code actual[3]}
   * @return the created {@code ErrorMessageFactory}
   */
  public static ErrorMessageFactory shouldHaveSameSizeOfSubarrayAs(boolean[][] actual, boolean[][] expected,
    boolean[] actualSubArray, boolean[] expectedSubArray, int actualSubArraySize, int expectedSubArraySize, int index) {
    return new ShouldHaveSameSizeOfSubArrayAs(actual, expected, actualSubArray, expectedSubArray, actualSubArraySize,
      expectedSubArraySize, index);
  }

  /**
   * Creates a new <code>{@link ShouldHaveSameSizeOfSubArrayAs}</code>.
   * @param actual the actual value in the failed assertion.
   * @param expected the expected value in the failed assertion.
   * @param actualSubArray next dimension of the {@code actual} multidimensional array
   * @param expectedSubArray next dimension of the {@code expected} multidimensional array
   * @param actualSubArraySize the size of next dimension of the {@code actual}.
   * @param expectedSubArraySize the size of next dimension of the{@code expected}.
   * @param index index of {@code actualSubArray}, e.g. {@code 3} when checking size (length) of {@code actual[3]}
   * @return the created {@code ErrorMessageFactory}
   */
  public static ErrorMessageFactory shouldHaveSameSizeOfSubarrayAs(byte[][] actual, byte[][] expected,
    byte[] actualSubArray, byte[] expectedSubArray, int actualSubArraySize, int expectedSubArraySize, int index) {
    return new ShouldHaveSameSizeOfSubArrayAs(actual, expected, actualSubArray, expectedSubArray, actualSubArraySize,
      expectedSubArraySize, index);
  }

  /**
   * Creates a new <code>{@link ShouldHaveSameSizeOfSubArrayAs}</code>.
   * @param actual the actual value in the failed assertion.
   * @param expected the expected value in the failed assertion.
   * @param actualSubArray next dimension of the {@code actual} multidimensional array
   * @param expectedSubArray next dimension of the {@code expected} multidimensional array
   * @param actualSubArraySize the size of next dimension of the {@code actual}.
   * @param expectedSubArraySize the size of next dimension of the{@code expected}.
   * @param index index of {@code actualSubArray}, e.g. {@code 3} when checking size (length) of {@code actual[3]}
   * @return the created {@code ErrorMessageFactory}
   */
  public static ErrorMessageFactory shouldHaveSameSizeOfSubarrayAs(double[][] actual, double[][] expected,
    double[] actualSubArray, double[] expectedSubArray, int actualSubArraySize, int expectedSubArraySize, int index) {
    return new ShouldHaveSameSizeOfSubArrayAs(actual, expected, actualSubArray, expectedSubArray, actualSubArraySize,
      expectedSubArraySize, index);
  }

  /**
   * Creates a new <code>{@link ShouldHaveSameSizeOfSubArrayAs}</code>.
   * @param actual the actual value in the failed assertion.
   * @param expected the expected value in the failed assertion.
   * @param actualSubArray next dimension of the {@code actual} multidimensional array
   * @param expectedSubArray next dimension of the {@code expected} multidimensional array
   * @param actualSubArraySize the size of next dimension of the {@code actual}.
   * @param expectedSubArraySize the size of next dimension of the{@code expected}.
   * @param index index of {@code actualSubArray}, e.g. {@code 3} when checking size (length) of {@code actual[3]}
   * @return the created {@code ErrorMessageFactory}
   */
  public static ErrorMessageFactory shouldHaveSameSizeOfSubarrayAs(short[][] actual, short[][] expected,
    short[] actualSubArray, short[] expectedSubArray, int actualSubArraySize, int expectedSubArraySize, int index) {
    return new ShouldHaveSameSizeOfSubArrayAs(actual, expected, actualSubArray, expectedSubArray, actualSubArraySize,
      expectedSubArraySize, index);
  }

  /**
   * Creates a new <code>{@link ShouldHaveSameSizeOfSubArrayAs}</code>.
   * @param actual the actual value in the failed assertion.
   * @param expected the expected value in the failed assertion.
   * @param actualSubArray next dimension of the {@code actual} multidimensional array
   * @param expectedSubArray next dimension of the {@code expected} multidimensional array
   * @param actualSubArraySize the size of next dimension of the {@code actual}.
   * @param expectedSubArraySize the size of next dimension of the{@code expected}.
   * @param index index of {@code actualSubArray}, e.g. {@code 3} when checking size (length) of {@code actual[3]}
   * @return the created {@code ErrorMessageFactory}
   */
  public static ErrorMessageFactory shouldHaveSameSizeOfSubarrayAs(long[][] actual, long[][] expected,
    long[] actualSubArray, long[] expectedSubArray, int actualSubArraySize, int expectedSubArraySize, int index) {
    return new ShouldHaveSameSizeOfSubArrayAs(actual, expected, actualSubArray, expectedSubArray, actualSubArraySize,
      expectedSubArraySize, index);
  }

  /**
   * Creates a new <code>{@link ShouldHaveSameSizeOfSubArrayAs}</code>.
   * @param actual the actual value in the failed assertion.
   * @param expected the expected value in the failed assertion.
   * @param actualSubArray next dimension of the {@code actual} multidimensional array
   * @param expectedSubArray next dimension of the {@code expected} multidimensional array
   * @param actualSubArraySize the size of next dimension of the {@code actual}.
   * @param expectedSubArraySize the size of next dimension of the{@code expected}.
   * @param index index of {@code actualSubArray}, e.g. {@code 3} when checking size (length) of {@code actual[3]}
   * @return the created {@code ErrorMessageFactory}
   */
  public static ErrorMessageFactory shouldHaveSameSizeOfSubarrayAs(char[][] actual, char[][] expected,
    char[] actualSubArray, char[] expectedSubArray, int actualSubArraySize, int expectedSubArraySize, int index) {
    return new ShouldHaveSameSizeOfSubArrayAs(actual, expected, actualSubArray, expectedSubArray, actualSubArraySize,
      expectedSubArraySize, index);
  }

  /**
   * Creates a new <code>{@link ShouldHaveSameSizeOfSubArrayAs}</code>.
   * @param actual the actual value in the failed assertion.
   * @param expected the expected value in the failed assertion.
   * @param actualSubArray next dimension of the {@code actual} multidimensional array
   * @param expectedSubArray next dimension of the {@code expected} multidimensional array
   * @param actualSubArraySize the size of next dimension of the {@code actual}.
   * @param expectedSubArraySize the size of next dimension of the{@code expected}.
   * @param index index of {@code actualSubArray}, e.g. {@code 3} when checking size (length) of {@code actual[3]}
   * @return the created {@code ErrorMessageFactory}
   */
  public static ErrorMessageFactory shouldHaveSameSizeOfSubarrayAs(Object[][] actual, Object[][] expected,
    Object[] actualSubArray, Object[] expectedSubArray, int actualSubArraySize, int expectedSubArraySize, int index) {
    return new ShouldHaveSameSizeOfSubArrayAs(actual, expected, actualSubArray, expectedSubArray, actualSubArraySize,
      expectedSubArraySize, index);
  }

  private ShouldHaveSameSizeOfSubArrayAs(Object[][] actual, Object[][] expected, Object[] actualSubArray,
    Object[] expectedSubArray, int actualSubArraySize, int expectedSubArraySize, int index) {
    super(format(MESSAGE, index, actualSubArraySize, index, expectedSubArraySize, index, Arrays.toString(actualSubArray),
          index, Arrays.toString(expectedSubArray), "%s", "%s"),
          actual, expected);
  }

  private ShouldHaveSameSizeOfSubArrayAs(int[][] actual, int[][] expected, int[] actualSubArray,
    int[] expectedSubArray, int actualSubArraySize, int expectedSubArraySize, int index) {
    super(format(MESSAGE, index, actualSubArraySize, index, expectedSubArraySize, index, Arrays.toString(actualSubArray),
      index, Arrays.toString(expectedSubArray), "%s", "%s"),
      actual, expected);
  }

  private ShouldHaveSameSizeOfSubArrayAs(float[][] actual, float[][] expected, float[] actualSubArray,
    float[] expectedSubArray, int actualSubArraySize, int expectedSubArraySize, int index) {
    super(format(MESSAGE, index, actualSubArraySize, index, expectedSubArraySize, index, Arrays.toString(actualSubArray),
      index, Arrays.toString(expectedSubArray), "%s", "%s"),
      actual, expected);
  }

  private ShouldHaveSameSizeOfSubArrayAs(long[][] actual, long[][] expected, long[] actualSubArray,
    long[] expectedSubArray, int actualSubArraySize, int expectedSubArraySize, int index) {
    super(format(MESSAGE, index, actualSubArraySize, index, expectedSubArraySize, index, Arrays.toString(actualSubArray),
      index, Arrays.toString(expectedSubArray), "%s", "%s"),
      actual, expected);
  }

  private ShouldHaveSameSizeOfSubArrayAs(double[][] actual, double[][] expected, double[] actualSubArray,
    double[] expectedSubArray, int actualSubArraySize, int expectedSubArraySize, int index) {
    super(format(MESSAGE, index, actualSubArraySize, index, expectedSubArraySize, index, Arrays.toString(actualSubArray),
      index, Arrays.toString(expectedSubArray), "%s", "%s"),
      actual, expected);
  }

  private ShouldHaveSameSizeOfSubArrayAs(byte[][] actual, byte[][] expected, byte[] actualSubArray,
    byte[] expectedSubArray, int actualSubArraySize, int expectedSubArraySize, int index) {
    super(format(MESSAGE, index, actualSubArraySize, index, expectedSubArraySize, index, Arrays.toString(actualSubArray),
      index, Arrays.toString(expectedSubArray), "%s", "%s"),
      actual, expected);
  }

  private ShouldHaveSameSizeOfSubArrayAs(short[][] actual, short[][] expected, short[] actualSubArray,
    short[] expectedSubArray, int actualSubArraySize, int expectedSubArraySize, int index) {
    super(format(MESSAGE, index, actualSubArraySize, index, expectedSubArraySize, index, Arrays.toString(actualSubArray),
      index, Arrays.toString(expectedSubArray), "%s", "%s"),
      actual, expected);
  }

  private ShouldHaveSameSizeOfSubArrayAs(boolean[][] actual, boolean[][] expected, boolean[] actualSubArray,
    boolean[] expectedSubArray, int actualSubArraySize, int expectedSubArraySize, int index) {
    super(format(MESSAGE, index, actualSubArraySize, index, expectedSubArraySize, index, Arrays.toString(actualSubArray),
      index, Arrays.toString(expectedSubArray), "%s", "%s"),
      actual, expected);
  }

  private ShouldHaveSameSizeOfSubArrayAs(char[][] actual, char[][] expected, char[] actualSubArray,
    char[] expectedSubArray, int actualSubArraySize, int expectedSubArraySize, int index) {
    super(format(MESSAGE, index, actualSubArraySize, index, expectedSubArraySize, index, Arrays.toString(actualSubArray),
      index, Arrays.toString(expectedSubArray), "%s", "%s"),
      actual, expected);
  }
}
