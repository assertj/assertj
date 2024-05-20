/*
 * Copyright © 2024 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.assertj.core.internal;

import static org.assertj.core.api.BDDAssertions.then;
import static org.mockito.BDDMockito.given;

import org.assertj.core.presentation.Representation;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * Tests for {@link UnambiguousRepresentation}.
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("UnambiguousRepresentation")
class UnambiguousRepresentation_Test {

  @Mock
  private Representation representation;

  @Test
  void should_use_toStringOf_given_they_are_different() {
    // GIVEN
    Object actual = new Object();
    Object expected = new Object();
    given(representation.toStringOf(actual)).willReturn("actual");
    given(representation.toStringOf(expected)).willReturn("expected");
    // WHEN
    UnambiguousRepresentation actualRepresentation = new UnambiguousRepresentation(representation, actual, expected);
    // THEN
    then(actualRepresentation.getActual()).isEqualTo("actual");
    then(actualRepresentation.getExpected()).isEqualTo("expected");
  }

  @Test
  void should_use_unambiguousToStringOf_whe_toStringOf_are_equal() {
    // GIVEN
    Object actual = new Object();
    Object expected = new Object();
    given(representation.toStringOf(actual)).willReturn("representation");
    given(representation.toStringOf(expected)).willReturn("representation");
    given(representation.unambiguousToStringOf(actual)).willReturn("actual");
    given(representation.unambiguousToStringOf(expected)).willReturn("expected");
    // WHEN
    UnambiguousRepresentation actualRepresentation = new UnambiguousRepresentation(representation, actual, expected);
    // THEN
    then(actualRepresentation.getActual()).isEqualTo("actual");
    then(actualRepresentation.getExpected()).isEqualTo("expected");
  }

}
