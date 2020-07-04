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
package org.assertj.core.error.array2d;

import org.assertj.core.error.BasicErrorMessageFactory;
import org.assertj.core.error.ErrorMessageFactory;

/**
 * Creates an <code>{@link AssertionError}</code> indicating that an assertion that verifies that two 2D arrays are depply equal
 * failed.
 *
 * @author Maciej Wajcht
 * @since 3.17.0
 */
public class Array2dElementShouldBeDeepEqual extends BasicErrorMessageFactory {

  private static final String MESSAGE = "%n" +
                                        "actual and expected 2d arrays should be deeply equal but element[%s, %s] differ:%n" +
                                        "actual[%s, %s] was:%n" +
                                        "  %s%n" +
                                        "while expected[%s, %s] was:%n" +
                                        "  %s";

  public static ErrorMessageFactory elementShouldBeEqual(Object actualElement, Object expectedElement, int rowIndex,
                                                         int columnIndex) {
    return new Array2dElementShouldBeDeepEqual(actualElement, expectedElement, rowIndex, columnIndex);
  }

  private Array2dElementShouldBeDeepEqual(Object actualElement, Object expectedElement, int rowIndex, int columnIndex) {
    super(MESSAGE, rowIndex, columnIndex, rowIndex, columnIndex, actualElement, rowIndex, columnIndex, expectedElement);
  }

}
