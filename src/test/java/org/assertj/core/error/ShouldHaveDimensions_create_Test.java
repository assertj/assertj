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
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.error.ShouldHaveDimensions.shouldHaveFirstDimension;
import static org.assertj.core.error.ShouldHaveDimensions.shouldHaveSize;
import static org.assertj.core.presentation.StandardRepresentation.STANDARD_REPRESENTATION;

import org.assertj.core.description.TextDescription;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("ShouldHaveDimensions create")
public class ShouldHaveDimensions_create_Test {

  @Test
  public void should_create_error_message_for_first_dimension() {
    // GIVEN
    int[][] array = { { 1, 2 }, { 3, 4 } };
    ErrorMessageFactory factory = shouldHaveFirstDimension(array, 3, 2);
    // WHEN
    String message = factory.create(new TextDescription("Test"), STANDARD_REPRESENTATION);
    // THEN
    then(message).isEqualTo(format("[Test] %n" +
                                   "Expecting 2D array to have 2 rows but had 3, array was:%n" +
                                   "<[[1, 2], [3, 4]]>"));
  }

  @Test
  public void should_create_error_message_for_row_dimension() {
    // GIVEN
    int[][] array = { { 1, 2 }, { 3, 4 } };
    ErrorMessageFactory factory = shouldHaveSize(array, 2, 3, 0);
    // WHEN
    String actualErrorMessage = factory.create(new TextDescription("Test"), STANDARD_REPRESENTATION);
    // THEN
    then(actualErrorMessage).isEqualTo(format("[Test] %n" +
                                              "Expecting actual[0] size to be 3 but was 2.%n" +
                                              "actual[0] was:%n" +
                                              "  [1, 2]%n" +
                                              "actual array was:%n" +
                                              "  [[1, 2], [3, 4]]"));
  }
}
