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
package org.assertj.core.api.assumptions;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.util.AssertionsUtil.expectAssumptionNotMetException;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.withSettings;

import org.assertj.core.api.AbstractClassLoaderAssert;
import org.assertj.core.api.Assumptions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Tests for <code>{@link Assumptions#assumeThat(ClassLoader)}</code>.
 *
 * @author Ashley Scopes
 */
class Assumptions_assumeThat_with_ClassLoader_Test {

  private ClassLoader actual;

  @BeforeEach
  void before() {
    actual = mock(ClassLoader.class, withSettings().stubOnly());
  }

  @Test
  void should_create_assumption() {
    AbstractClassLoaderAssert<?> assumptions = Assumptions.assumeThat(actual);
    assertThat(assumptions).isNotNull();
  }

  @Test
  void should_pass_actual() {
    AbstractClassLoaderAssert<?> assumptions = Assumptions.assumeThat(actual);
    assertThatCode(() -> assumptions.isSameAs(actual))
      .doesNotThrowAnyException();
  }

  @Test
  void should_propagate_skipping_exception() {
    AbstractClassLoaderAssert<?> assumptions = Assumptions.assumeThat(actual);
    expectAssumptionNotMetException(() -> assumptions.isNotSameAs(actual));
  }
}
