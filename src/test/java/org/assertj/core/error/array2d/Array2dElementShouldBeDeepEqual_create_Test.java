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

import static java.lang.String.format;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.error.array2d.Array2dElementShouldBeDeepEqual.elementShouldBeEqual;
import static org.assertj.core.presentation.StandardRepresentation.STANDARD_REPRESENTATION;

import org.assertj.core.description.TextDescription;
import org.assertj.core.error.ErrorMessageFactory;
import org.junit.jupiter.api.Test;

public class Array2dElementShouldBeDeepEqual_create_Test {

  @Test
  public void should_create_error_message() {
    // GIVEN
    ErrorMessageFactory errorMessageFactory = elementShouldBeEqual(999, 4, 1, 3);
    // WHEN
    String message = errorMessageFactory.create(new TextDescription("Test"), STANDARD_REPRESENTATION);
    // THEN
    then(message).isEqualTo(format("[Test] %n" +
                                   "actual and expected 2d arrays should be deeply equal but element[1, 3] differ:%n" +
                                   "actual[1, 3] was:%n" +
                                   "  999%n" +
                                   "while expected[1, 3] was:%n" +
                                   "  4"));
  }

}
