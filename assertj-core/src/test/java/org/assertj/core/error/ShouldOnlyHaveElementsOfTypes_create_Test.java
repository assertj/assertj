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
package org.assertj.core.error;

import static java.lang.String.format;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.error.ShouldOnlyHaveElementsOfTypes.shouldOnlyHaveElementsOfTypes;
import static org.assertj.core.util.Arrays.array;
import static org.assertj.core.util.Lists.list;

import org.assertj.core.internal.TestDescription;
import org.assertj.core.presentation.StandardRepresentation;
import org.junit.jupiter.api.Test;

/**
 * @author Martin Winandy
 */
class ShouldOnlyHaveElementsOfTypes_create_Test {

  @Test
  void should_create_error_message() {
    // GIVEN
    ErrorMessageFactory factory = shouldOnlyHaveElementsOfTypes(list("Yoda", 42, "Luke"),
                                                                array(Number.class, Long.class),
                                                                list("Yoda", "Luke"));
    // WHEN
    String message = factory.create(new TestDescription("Test"), new StandardRepresentation());
    // THEN
    then(message).isEqualTo(format("[Test] %n" +
                                   "Expecting actual:%n" +
                                   "  [\"Yoda\", 42, \"Luke\"]%n" +
                                   "to only have instances of:%n" +
                                   "  [java.lang.Number, java.lang.Long]%n" +
                                   "but these elements are not:%n" +
                                   "  [\"Yoda\" (java.lang.String), \"Luke\" (java.lang.String)]"));
  }

}
