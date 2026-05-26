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
package org.assertj.core.api.objectarray;

import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.api.InstanceOfAssertFactories.THROWABLE;
import static org.assertj.core.util.Arrays.array;
import static org.mockito.Mockito.verify;

import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;

import org.assertj.core.api.ObjectArrayAssert;
import org.assertj.core.api.ObjectArrayAssertBaseTest;
import org.assertj.core.api.SoftAssertions;
import org.assertj.core.presentation.PredicateDescription;
import org.junit.jupiter.api.Test;

class ObjectArrayAssert_allMatch_with_description_Test extends ObjectArrayAssertBaseTest {

  private final Predicate<Object> predicate = Objects::nonNull;

  @Override
  protected ObjectArrayAssert<Object> invoke_api_method() {
    return assertions.allMatch(predicate, "custom");
  }

  @Override
  protected void verify_internal_effects() {
    verify(iterables).assertAllMatch(getInfo(assertions), List.of(getActual(assertions)), predicate,
                                     new PredicateDescription("custom"));
  }

  @Test
  public void should_work_with_soft_assertions() {
    // GIVEN
    SoftAssertions softly = new SoftAssertions();
    // WHEN
    softly.assertThat(array("Luke", "Yoda"))
          .overridingErrorMessage("error message")
          .allMatch(obj -> false, "custom");
    // THEN
    then(softly.assertionErrorsCollected()).singleElement(THROWABLE).hasMessage("error message");
  }
}
