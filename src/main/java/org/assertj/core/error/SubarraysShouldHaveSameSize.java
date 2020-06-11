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
public class SubarraysShouldHaveSameSize extends BasicErrorMessageFactory {

  private static final String MESSAGE = "%n" +
                                        "Actual and expected should have same size but actual[%s] size is:%n" +
                                        " <%s>%n" +
                                        "while expected[%s] size is:%n" +
                                        " <%s>%n" +
                                        "Actual[%s] was:%n" +
                                        " %s%n" +
                                        "Expected[%s] was:%n" +
                                        " %s%n" +
                                        "Actual was:%n" +
                                        " %s%n" +
                                        "Expected was:%n" +
                                        " %s";

  /**
   * Creates a new <code>{@link SubarraysShouldHaveSameSize}</code>.
   * @param actual the actual value in the failed assertion.
   * @param expected the expected value in the failed assertion.
   * @param actualSubArray next dimension of the {@code actual} multidimensional array
   * @param expectedSubArray next dimension of the {@code expected} multidimensional array
   * @param index index of {@code actualSubArray}, e.g. {@code 3} when checking size (length) of {@code actual[3]}
   * @return the created {@code ErrorMessageFactory}
   */
  public static ErrorMessageFactory subarraysShouldHaveSameSize(int[][] actual, int[][] expected, int[] actualSubArray,
                                                                int[] expectedSubArray, int index) {
    return new SubarraysShouldHaveSameSize(actual, expected, actualSubArray, expectedSubArray, index);
  }

  /**
   * Creates a new <code>{@link SubarraysShouldHaveSameSize}</code>.
   * @param actual the actual value in the failed assertion.
   * @param expected the expected value in the failed assertion.
   * @param actualSubArray next dimension of the {@code actual} multidimensional array
   * @param expectedSubArray next dimension of the {@code expected} multidimensional array
   * @param index index of {@code actualSubArray}, e.g. {@code 3} when checking size (length) of {@code actual[3]}
   * @return the created {@code ErrorMessageFactory}
   */
  public static ErrorMessageFactory subarraysShouldHaveSameSize(float[][] actual, float[][] expected, float[] actualSubArray,
                                                                float[] expectedSubArray, int index) {
    return new SubarraysShouldHaveSameSize(actual, expected, actualSubArray, expectedSubArray, index);
  }

  /**
   * Creates a new <code>{@link SubarraysShouldHaveSameSize}</code>.
   * @param actual the actual value in the failed assertion.
   * @param expected the expected value in the failed assertion.
   * @param actualSubArray next dimension of the {@code actual} multidimensional array
   * @param expectedSubArray next dimension of the {@code expected} multidimensional array
   * @param index index of {@code actualSubArray}, e.g. {@code 3} when checking size (length) of {@code actual[3]}
   * @return the created {@code ErrorMessageFactory}
   */
  public static ErrorMessageFactory subarraysShouldHaveSameSize(boolean[][] actual, boolean[][] expected,
                                                                boolean[] actualSubArray, boolean[] expectedSubArray, int index) {
    return new SubarraysShouldHaveSameSize(actual, expected, actualSubArray, expectedSubArray, index);
  }

  /**
   * Creates a new <code>{@link SubarraysShouldHaveSameSize}</code>.
   * @param actual the actual value in the failed assertion.
   * @param expected the expected value in the failed assertion.
   * @param actualSubArray next dimension of the {@code actual} multidimensional array
   * @param expectedSubArray next dimension of the {@code expected} multidimensional array
   * @param index index of {@code actualSubArray}, e.g. {@code 3} when checking size (length) of {@code actual[3]}
   * @return the created {@code ErrorMessageFactory}
   */
  public static ErrorMessageFactory subarraysShouldHaveSameSize(byte[][] actual, byte[][] expected, byte[] actualSubArray,
                                                                byte[] expectedSubArray, int index) {
    return new SubarraysShouldHaveSameSize(actual, expected, actualSubArray, expectedSubArray, index);
  }

  /**
   * Creates a new <code>{@link SubarraysShouldHaveSameSize}</code>.
   * @param actual the actual value in the failed assertion.
   * @param expected the expected value in the failed assertion.
   * @param actualSubArray next dimension of the {@code actual} multidimensional array
   * @param expectedSubArray next dimension of the {@code expected} multidimensional array
   * @param index index of {@code actualSubArray}, e.g. {@code 3} when checking size (length) of {@code actual[3]}
   * @return the created {@code ErrorMessageFactory}
   */
  public static ErrorMessageFactory subarraysShouldHaveSameSize(double[][] actual, double[][] expected, double[] actualSubArray,
                                                                double[] expectedSubArray, int index) {
    return new SubarraysShouldHaveSameSize(actual, expected, actualSubArray, expectedSubArray, index);
  }

  /**
   * Creates a new <code>{@link SubarraysShouldHaveSameSize}</code>.
   * @param actual the actual value in the failed assertion.
   * @param expected the expected value in the failed assertion.
   * @param actualSubArray next dimension of the {@code actual} multidimensional array
   * @param expectedSubArray next dimension of the {@code expected} multidimensional array
   * @param index index of {@code actualSubArray}, e.g. {@code 3} when checking size (length) of {@code actual[3]}
   * @return the created {@code ErrorMessageFactory}
   */
  public static ErrorMessageFactory subarraysShouldHaveSameSize(short[][] actual, short[][] expected, short[] actualSubArray,
                                                                short[] expectedSubArray, int index) {
    return new SubarraysShouldHaveSameSize(actual, expected, actualSubArray, expectedSubArray, index);
  }

  /**
   * Creates a new <code>{@link SubarraysShouldHaveSameSize}</code>.
   * @param actual the actual value in the failed assertion.
   * @param expected the expected value in the failed assertion.
   * @param actualSubArray next dimension of the {@code actual} multidimensional array
   * @param expectedSubArray next dimension of the {@code expected} multidimensional array
   * @param index index of {@code actualSubArray}, e.g. {@code 3} when checking size (length) of {@code actual[3]}
   * @return the created {@code ErrorMessageFactory}
   */
  public static ErrorMessageFactory subarraysShouldHaveSameSize(long[][] actual, long[][] expected, long[] actualSubArray,
                                                                long[] expectedSubArray, int index) {
    return new SubarraysShouldHaveSameSize(actual, expected, actualSubArray, expectedSubArray, index);
  }

  /**
   * Creates a new <code>{@link SubarraysShouldHaveSameSize}</code>.
   * @param actual the actual value in the failed assertion.
   * @param expected the expected value in the failed assertion.
   * @param actualSubArray next dimension of the {@code actual} multidimensional array
   * @param expectedSubArray next dimension of the {@code expected} multidimensional array
   * @param index index of {@code actualSubArray}, e.g. {@code 3} when checking size (length) of {@code actual[3]}
   * @return the created {@code ErrorMessageFactory}
   */
  public static ErrorMessageFactory subarraysShouldHaveSameSize(char[][] actual, char[][] expected, char[] actualSubArray,
                                                                char[] expectedSubArray, int index) {
    return new SubarraysShouldHaveSameSize(actual, expected, actualSubArray, expectedSubArray, index);
  }

  /**
   * Creates a new <code>{@link SubarraysShouldHaveSameSize}</code>.
   * @param actual the actual value in the failed assertion.
   * @param expected the expected value in the failed assertion.
   * @param actualSubArray next dimension of the {@code actual} multidimensional array
   * @param expectedSubArray next dimension of the {@code expected} multidimensional array
   * @param index index of {@code actualSubArray}, e.g. {@code 3} when checking size (length) of {@code actual[3]}
   * @return the created {@code ErrorMessageFactory}
   */
  public static ErrorMessageFactory subarraysShouldHaveSameSize(Object[][] actual, Object[][] expected, Object[] actualSubArray,
                                                                Object[] expectedSubArray, int index) {
    return new SubarraysShouldHaveSameSize(actual, expected, actualSubArray, expectedSubArray, index);
  }

  private SubarraysShouldHaveSameSize(Object[][] actual, Object[][] expected, Object[] actualSubArray, Object[] expectedSubArray,
                                      int index) {
    super(format(MESSAGE, index, actualSubArray.length, index, expectedSubArray.length, index, Arrays.toString(actualSubArray),
                 index, Arrays.toString(expectedSubArray), "%s", "%s"),
          actual, expected);
  }

  private SubarraysShouldHaveSameSize(int[][] actual, int[][] expected, int[] actualSubArray, int[] expectedSubArray, int index) {
    super(format(MESSAGE, index, actualSubArray.length, index, expectedSubArray.length, index, Arrays.toString(actualSubArray),
                 index, Arrays.toString(expectedSubArray), "%s", "%s"),
          actual, expected);
  }

  private SubarraysShouldHaveSameSize(float[][] actual, float[][] expected, float[] actualSubArray, float[] expectedSubArray,
                                      int index) {
    super(format(MESSAGE, index, actualSubArray.length, index, expectedSubArray.length, index, Arrays.toString(actualSubArray),
                 index, Arrays.toString(expectedSubArray), "%s", "%s"),
          actual, expected);
  }

  private SubarraysShouldHaveSameSize(long[][] actual, long[][] expected, long[] actualSubArray, long[] expectedSubArray,
                                      int index) {
    super(format(MESSAGE, index, actualSubArray.length, index, expectedSubArray.length, index, Arrays.toString(actualSubArray),
                 index, Arrays.toString(expectedSubArray), "%s", "%s"),
          actual, expected);
  }

  private SubarraysShouldHaveSameSize(double[][] actual, double[][] expected, double[] actualSubArray, double[] expectedSubArray,
                                      int index) {
    super(format(MESSAGE, index, actualSubArray.length, index, expectedSubArray.length, index, Arrays.toString(actualSubArray),
                 index, Arrays.toString(expectedSubArray), "%s", "%s"),
          actual, expected);
  }

  private SubarraysShouldHaveSameSize(byte[][] actual, byte[][] expected, byte[] actualSubArray, byte[] expectedSubArray,
                                      int index) {
    super(format(MESSAGE, index, actualSubArray.length, index, expectedSubArray.length, index, Arrays.toString(actualSubArray),
                 index, Arrays.toString(expectedSubArray), "%s", "%s"),
          actual, expected);
  }

  private SubarraysShouldHaveSameSize(short[][] actual, short[][] expected, short[] actualSubArray, short[] expectedSubArray,
                                      int index) {
    super(format(MESSAGE, index, actualSubArray.length, index, expectedSubArray.length, index, Arrays.toString(actualSubArray),
                 index, Arrays.toString(expectedSubArray), "%s", "%s"),
          actual, expected);
  }

  private SubarraysShouldHaveSameSize(boolean[][] actual, boolean[][] expected, boolean[] actualSubArray,
                                      boolean[] expectedSubArray, int index) {
    super(format(MESSAGE, index, actualSubArray.length, index, expectedSubArray.length, index, Arrays.toString(actualSubArray),
                 index, Arrays.toString(expectedSubArray), "%s", "%s"),
          actual, expected);
  }

  private SubarraysShouldHaveSameSize(char[][] actual, char[][] expected, char[] actualSubArray, char[] expectedSubArray,
                                      int index) {
    super(format(MESSAGE, index, actualSubArray.length, index, expectedSubArray.length, index, Arrays.toString(actualSubArray),
                 index, Arrays.toString(expectedSubArray), "%s", "%s"),
          actual, expected);
  }
}
