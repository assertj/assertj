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
 * Copyright 2012-2022 the original author or authors.
 */
package org.assertj.core.util;

import static org.assertj.core.api.Assertions.assertThat;

import java.nio.Buffer;
import java.util.function.Predicate;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link Types}.
 *
 * @author Ashley Scopes
 */
class TypesTest {
  @SuppressWarnings("rawtypes")
  @Test
  void can_cast_predicate_type() {
    // This test is a constant condition. If the API does not work due to type incompatibility,
    // then this should immediately fail to compile at all.

    // Given
    Class<Predicate> rawType = Predicate.class;

    // When
    Class<Predicate<? super Buffer>> castType = Types.castClass(rawType);

    // Then
    assertThat(castType).isSameAs(rawType);
  }

  @SuppressWarnings("rawtypes")
  @Test
  void can_cast_iterable_type() {
    // This test is a constant condition. If the API does not work due to type incompatibility,
    // then this should immediately fail to compile at all.

    // Given
    Class<Iterable> rawType = Iterable.class;

    // When
    Class<Iterable<StringBuilder>> castType = Types.castClass(rawType);

    // Then
    assertThat(castType).isSameAs(rawType);
  }
}
