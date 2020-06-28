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

import java.lang.reflect.Array;

public class ShouldHaveDimensions extends BasicErrorMessageFactory {

  public static ErrorMessageFactory shouldHaveFirstDimension(Object actual, int actualSize, int expectedSize) {
    return new ShouldHaveDimensions(actual, actualSize, expectedSize);
  }

  public static ErrorMessageFactory shouldHaveSize(Object actual, int actualSize, int expectedSize, int rowIndex) {
    return new ShouldHaveDimensions(actual, actualSize, expectedSize, rowIndex);
  }

  private ShouldHaveDimensions(Object actual, int actualSize, int expectedSize) {
    // format the sizes in a standard way, otherwise if we use (for ex) an Hexadecimal representation
    // it will format sizes in hexadecimal while we only want actual to be formatted in hexadecimal
    super(format("%nExpecting 2D array to have %s rows but had %s, array was:%n<%s>", expectedSize, actualSize, "%s"), actual);
  }

  private ShouldHaveDimensions(Object actual, int actualSize, int expectedSize, int rowIndex) {
    // format the sizes in a standard way, otherwise if we use (for ex) an Hexadecimal representation
    // it will format sizes in hexadecimal while we only want actual to be formatted in hexadecimal
    super(format("%n" +
                 "Expecting actual[%s] size to be %s but was %s.%n" +
                 "actual[%s] was:%n" +
                 "  %s%n" +
                 "actual array was:%n" +
                 "  %s", rowIndex, expectedSize, actualSize, rowIndex, "%s", "%s"),
          Array.get(actual, rowIndex), actual);
  }

}
