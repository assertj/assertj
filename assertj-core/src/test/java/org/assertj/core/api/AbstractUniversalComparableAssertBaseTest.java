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
package org.assertj.core.api;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.setRemoveAssertJRelatedElementsFromStackTrace;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;

import org.assertj.core.api.abstract_.AbstractAssert_isNull_Test;
import org.assertj.core.error.AssertionErrorCreator;
import org.assertj.core.internal.Comparables;
import org.assertj.core.internal.Conditions;
import org.assertj.core.internal.Objects;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public abstract class AbstractUniversalComparableAssertBaseTest {

  protected UniversalComparableAssert<String> assertions;
  protected Objects objects;
  protected Conditions conditions;
  protected AssertionErrorCreator assertionErrorCreator;

  @BeforeEach
  public final void setUp() {
    assertions = create_assertions();
    inject_internal_objects();
    setRemoveAssertJRelatedElementsFromStackTrace(false);
  }

  protected Comparables comparables;

  protected void inject_internal_objects() {
    objects = mock(Objects.class);
    assertions.objects = objects;
    conditions = mock(Conditions.class);
    assertions.conditions = conditions;
    assertionErrorCreator = spy(assertions.assertionErrorCreator);
    assertions.assertionErrorCreator = assertionErrorCreator;
    comparables = mock(Comparables.class);
    assertions.comparables = comparables;
  }

  protected UniversalComparableAssert<String> create_assertions() {
    return new UniversalComparableAssert<>("abc");
  }

  protected Comparables getComparables(UniversalComparableAssert<String> someAssertions) {
    return someAssertions.comparables;
  }

  @Test
  public void should_have_internal_effects() {
    invoke_api_method();
    verify_internal_effects();
  }

  /**
   * For the few API methods that don't return {@code this}, override this method to do nothing (see
   * {@link AbstractAssert_isNull_Test#should_return_this()} for an example).
   */
  @Test
  public void should_return_this() {
    UniversalComparableAssert<String> returned = invoke_api_method();
    assertThat(returned).isSameAs(assertions);
  }

  protected AssertionInfo getInfo(UniversalComparableAssert<String> someAssertions) {
    return someAssertions.info;
  }

  protected AssertionInfo info() {
    return getInfo(assertions);
  }

  protected Comparable<String> getActual(UniversalComparableAssert<String> someAssertions) {
    return someAssertions.actual;
  }

  protected Objects getObjects(UniversalComparableAssert<String> someAssertions) {
    return someAssertions.objects;
  }

  /**
   * Invokes the API method under test.
   *
   * @return the assertion object that is returned by the method. If the method is {@code void}, return {@code null} and override
   *         {@link #should_return_this()}.
   */
  protected abstract UniversalComparableAssert<String> invoke_api_method();

  /**
   * Verifies that invoking the API method had the expected effects (usually, setting some internal state or invoking an internal
   * object).
   */
  protected abstract void verify_internal_effects();

}
