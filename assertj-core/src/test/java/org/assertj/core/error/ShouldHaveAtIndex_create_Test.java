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

import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.data.Index.atIndex;
import static org.assertj.core.error.ShouldHaveAtIndex.shouldHaveAtIndex;
import static org.assertj.core.util.Lists.list;

import org.assertj.core.api.TestCondition;
import org.assertj.core.description.TextDescription;
import org.assertj.core.presentation.StandardRepresentation;
import org.junit.jupiter.api.Test;

/**
 * Tests for <code>{@link ShouldHaveAtIndex#create(org.assertj.core.description.Description, org.assertj.core.presentation.Representation)}</code>.
 *
 * @author Bo Gotthardt
 */
class ShouldHaveAtIndex_create_Test {

  @Test
  void should_create_error_message() {
    // GIVEN
    ErrorMessageFactory factory = shouldHaveAtIndex(list("Yoda", "Luke"), new TestCondition<>("red lightsaber"), atIndex(1),
                                                    "Luke");
    // WHEN
    String message = factory.create(new TextDescription("Test"), new StandardRepresentation());
    // THEN
    then(message).isEqualTo("[Test] %nExpecting actual:%n  \"Luke\"%nat index 1 to have:%n  red lightsaber%nin:%n  [\"Yoda\", \"Luke\"]%n".formatted());
  }
}
