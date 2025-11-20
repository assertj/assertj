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
package org.assertj.core.internal.maps;

import static java.util.Collections.emptyMap;
import static org.assertj.core.data.MapEntry.entry;
import static org.assertj.core.error.ShouldNotBeEmpty.shouldNotBeEmpty;
import static org.assertj.core.testkit.Maps.mapOf;
import static org.assertj.core.util.AssertionsUtil.assertThatAssertionErrorIsThrownBy;
import static org.assertj.core.util.AssertionsUtil.expectAssertionError;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.mockito.Mockito.verify;

import java.util.Map;

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.internal.Maps;
import org.assertj.core.internal.MapsBaseTest;
import org.junit.jupiter.api.Test;

/**
 * Tests for <code>{@link Maps#assertNotEmpty(AssertionInfo, Map)}</code>.
 *
 * @author Alex Ruiz
 * @author Joel Costigliola
 */
class Maps_assertNotEmpty_Test extends MapsBaseTest {

  @Test
  void should_pass_if_actual_is_not_empty() {
    Map<?, ?> actual = mapOf(entry("name", "Yoda"));
    maps.assertNotEmpty(INFO, actual);
  }

  @Test
  void should_fail_if_actual_is_null() {
    assertThatAssertionErrorIsThrownBy(() -> maps.assertNotEmpty(INFO, null)).withMessage(actualIsNull());
  }

  @Test
  void should_fail_if_actual_is_empty() {
    expectAssertionError(() -> maps.assertNotEmpty(INFO, emptyMap()));
    verify(failures).failure(INFO, shouldNotBeEmpty());
  }

}
