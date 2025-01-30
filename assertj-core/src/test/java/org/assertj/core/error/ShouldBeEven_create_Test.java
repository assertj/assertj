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
import static org.assertj.core.error.ShouldBeEven.shouldBeEven;
import static org.assertj.core.presentation.StandardRepresentation.STANDARD_REPRESENTATION;

import org.assertj.core.description.Description;
import org.assertj.core.internal.TestDescription;
import org.assertj.core.presentation.Representation;
import org.junit.jupiter.api.Test;

/**
 * Tests for <code>{@link ShouldBeEven#create(Description, Representation)}</code>.
 *
 * @author Cal027
 */
class ShouldBeEven_create_Test {

  @Test
  void should_create_error_message() {
    // GIVEN
    Number actual = 1;
    // WHEN
    String message = shouldBeEven(actual).create(new TestDescription("TEST"), STANDARD_REPRESENTATION);
    // THEN
    then(message).isEqualTo("[TEST] %nExpecting %d to be even".formatted(actual));
  }
}
