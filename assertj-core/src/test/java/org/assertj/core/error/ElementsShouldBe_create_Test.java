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
import static org.assertj.core.error.ElementsShouldBe.elementsShouldBe;
import static org.assertj.core.presentation.StandardRepresentation.STANDARD_REPRESENTATION;
import static org.assertj.core.util.Lists.list;

import org.assertj.core.api.TestCondition;
import org.assertj.core.description.Description;
import org.assertj.core.description.TextDescription;
import org.junit.jupiter.api.Test;

/**
 * Tests for <code>{@link ElementsShouldBe#create(Description)}</code>.
 *
 * @author Nicolas François
 * @author Joel Costigliola
 */
class ElementsShouldBe_create_Test {

  @Test
  void should_create_error_message() {
    // GIVEN
    ErrorMessageFactory factory = elementsShouldBe(list("Yoda", "Luke", "Leia"), list("Leia"),
                                                   new TestCondition<>("a Jedi"));
    // WHEN
    String message = factory.create(new TextDescription("Test"), STANDARD_REPRESENTATION);
    // THEN
    then(message).isEqualTo(format("[Test] %nExpecting elements:%n" +
                                   "  [\"Leia\"]%n" +
                                   "of%n" +
                                   "  [\"Yoda\", \"Luke\", \"Leia\"]%n" +
                                   "to be a Jedi"));
  }

}
