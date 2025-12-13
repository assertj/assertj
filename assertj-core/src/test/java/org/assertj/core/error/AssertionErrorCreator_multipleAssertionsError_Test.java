/*
 * Copyright 2012-2025 the original author or authors.
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
package org.assertj.core.error;

import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.util.Lists.list;

import java.util.List;

import org.assertj.core.description.Description;
import org.assertj.core.internal.TestDescription;
import org.assertj.core.presentation.StandardRepresentation;
import org.junit.jupiter.api.Test;

class AssertionErrorCreator_multipleAssertionsError_Test {

  private final AssertionErrorCreator assertionErrorCreator = new AssertionErrorCreator();

  @Test
  void should_create_MultipleAssertionsError() {
    // GIVEN
    Description description = new TestDescription("description");
    AssertionError error1 = new AssertionError("error1");
    AssertionError error2 = new AssertionError("error2");
    List<AssertionError> errors = list(error1, error2);
    // WHEN
    var assertionError = assertionErrorCreator.multipleAssertionsError(description, null, errors);
    // THEN
    then(assertionError).isInstanceOf(MultipleAssertionsError.class)
                        .hasMessage(StandardRepresentation.STANDARD_REPRESENTATION.toStringOf(assertionError));
    then(((MultipleAssertionsError) assertionError).getErrors()).containsExactly(error1, error2);
  }

}
