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
 * Copyright 2012-2024 the original author or authors.
 */
package org.assertj.core.error;

import static java.lang.String.format;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.error.ShouldBeAssignableFrom.shouldBeAssignableFrom;
import static org.assertj.core.presentation.StandardRepresentation.STANDARD_REPRESENTATION;

import org.assertj.core.description.TextDescription;
import org.assertj.core.util.Sets;
import org.junit.jupiter.api.Test;

/**
 * Tests for
 * <code>{@link org.assertj.core.error.ShouldBeAssignableFrom#shouldBeAssignableFrom(Class, java.util.Set, java.util.Set)}</code>
 *
 * @author William Delanoue
 */
class ShouldBeAssignableFrom_create_Test {

  @Test
  void should_create_error_message() {
    // GIVEN
    ErrorMessageFactory factory = shouldBeAssignableFrom(ShouldBeAssignableFrom_create_Test.class,
                                                         Sets.newLinkedHashSet(String.class, Integer.class),
                                                         Sets.newLinkedHashSet((String.class)));
    // WHEN
    String message = factory.create(new TextDescription("Test"), STANDARD_REPRESENTATION);
    // THEN
    then(message).isEqualTo(format("[Test] %n"
                                   + "Expecting%n"
                                   + "  org.assertj.core.error.ShouldBeAssignableFrom_create_Test%n"
                                   + "to be assignable from:%n"
                                   + "  [java.lang.String, java.lang.Integer]%n"
                                   + "but was not assignable from:%n"
                                   + "  [java.lang.String]"));
  }
}
