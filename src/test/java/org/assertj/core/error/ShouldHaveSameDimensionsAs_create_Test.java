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
import static org.assertj.core.error.ShouldHaveSameDimensionsAs.shouldHaveSameDimensionsAs;
import static org.assertj.core.presentation.StandardRepresentation.STANDARD_REPRESENTATION;

import org.assertj.core.description.TextDescription;
import org.junit.jupiter.api.Test;

public class ShouldHaveSameDimensionsAs_create_Test {

  @Test
  public void should_create_error_message_for_first_dimension() {
    // GIVEN
    ErrorMessageFactory factory = shouldHaveSameDimensionsAs(new String[][] { { "a", "b", }, { "c", "d" }, { "e", "f" } },
                                                             new String[][] { { "a", "b" }, { "c", "d" } },
                                                             3, 2);
    // WHEN
    String message = factory.create(new TextDescription("Test"), STANDARD_REPRESENTATION);
    // THEN
    then(message).isEqualTo(format("[Test] %n"
                                   + "Actual and expected should have same dimensions but actual and expected have different number of rows.%n"
                                   + "Actual has 3 rows while expected has 2.%n"
                                   + "Actual was:%n"
                                   + "  [[\"a\", \"b\"], [\"c\", \"d\"], [\"e\", \"f\"]]%n"
                                   + "Expected was:%n"
                                   + "  [[\"a\", \"b\"], [\"c\", \"d\"]]"));
  }

  @Test
  public void should_create_error_message_for_second_dimension() {
    // GIVEN
    ErrorMessageFactory factory = shouldHaveSameDimensionsAs(1, 3, 2,
                                                             new String[] { "c", "d", "e" },
                                                             new String[] { "c", "d" },
                                                             new String[][] { { "a", "b", }, { "c", "d", "e" } },
                                                             new String[][] { { "a", "b" }, { "c", "d" } });
    // WHEN
    String message = factory.create(new TextDescription("Test"), STANDARD_REPRESENTATION);
    // THEN
    then(message).isEqualTo(format("[Test] %n"
                                   + "Actual and expected should have same dimensions but rows at index 1 don't have the same size.%n"
                                   + "Actual row size is 3 while expected row size is 2%n"
                                   + "Actual row was:%n"
                                   + " [\"c\", \"d\", \"e\"]%n"
                                   + "Expected row was:%n"
                                   + " [\"c\", \"d\"]%n"
                                   + "Actual was:%n"
                                   + " [[\"a\", \"b\"], [\"c\", \"d\", \"e\"]]%n"
                                   + "Expected was:%n"
                                   + " [[\"a\", \"b\"], [\"c\", \"d\"]]"));
  }
}
