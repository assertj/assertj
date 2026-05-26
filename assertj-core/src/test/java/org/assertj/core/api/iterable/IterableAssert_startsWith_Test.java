/*
 * Copyright 2012-2026 the original author or authors.
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
package org.assertj.core.api.iterable;

import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.api.InstanceOfAssertFactories.THROWABLE;
import static org.assertj.core.util.Arrays.array;
import static org.mockito.Mockito.verify;

import java.util.List;

import org.assertj.core.api.AbstractIterableAssert;
import org.assertj.core.api.ConcreteIterableAssert;
import org.assertj.core.api.IterableAssertBaseTest;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Test;

/**
 * Tests for <code>{@link AbstractIterableAssert#startsWith(Object...)}</code>.
 *
 * @author Alex Ruiz
 * @author Joel Costigliola
 */
class IterableAssert_startsWith_Test extends IterableAssertBaseTest {

  @Override
  protected ConcreteIterableAssert<Object> invoke_api_method() {
    return assertions.startsWith("Luke", "Yoda");
  }

  @Override
  protected void verify_internal_effects() {
    verify(iterables).assertStartsWith(getInfo(assertions), getActual(assertions), array("Luke", "Yoda"));
  }

  @Test
  public void should_work_with_soft_assertions() {
    // GIVEN
    SoftAssertions softly = new SoftAssertions();
    // WHEN
    softly.assertThat(List.of("Luke", "Yoda")).startsWith("Obiwan");
    // THEN
    List<AssertionError> errors = softly.assertionErrorsCollected();
    then(errors).singleElement(THROWABLE).hasMessageContaining("Obiwan");
  }
}
