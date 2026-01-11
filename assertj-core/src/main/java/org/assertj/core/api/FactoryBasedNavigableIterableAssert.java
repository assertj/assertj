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
package org.assertj.core.api;

/**
 * Provides helper methods for navigating a list property in a generated assertion class so we can chain assertions
 * through deeply nested models more easily.
 * 
 * @since 2.5.0 / 3.5.0
 * @deprecated Use {@link AbstractIterableAssert#withElementAssert(AssertFactory)} instead.
 */
//@format:off
@Deprecated
public class FactoryBasedNavigableIterableAssert<SELF extends FactoryBasedNavigableIterableAssert<SELF, ACTUAL, ELEMENT, ELEMENT_ASSERT>, 
                                                 ACTUAL extends Iterable<? extends ELEMENT>, 
                                                 ELEMENT, 
                                                 ELEMENT_ASSERT extends AbstractAssert<? extends ELEMENT_ASSERT, ELEMENT>>
       extends AbstractIterableAssert<SELF, ACTUAL, ELEMENT, ELEMENT_ASSERT> {
// @format:on

  private final AssertFactory<ELEMENT, ELEMENT_ASSERT> assertFactory;

  /**
   * @deprecated
   * This was added to help create type-specific assertions for the elements of an {@link Iterable} instance.
   * However, there are better ways to reach the same goal.
   * <p>
   * The preferred way is to use navigation method overload that expects an {@link InstanceOfAssertFactory} parameter.
   * For example, one of them is
   * {@link AbstractIterableAssert#first(InstanceOfAssertFactory) first(InstanceOfAssertFactory)}:
   * <pre><code class='java'>assertThat(hobbits).first(STRING) // static import of InstanceOfAssertFactories.STRING
   *                    .startsWith("fro")
   *                    .endsWith("do");</code></pre>
   *
   * Its main advantage is easier discoverability and the use of {@code InstanceOfAssertFactory}, which is the
   * preferred way to create type-specific assertions in AssertJ API.
   * <p>
   * Otherwise, the element assertion factory can be configured on the assertion object via
   * {@link AbstractIterableAssert#withElementAssert(AssertFactory) withElementAssert}:
   * <pre><code class='java'>assertThat(hobbits).withElementAssert(Assertions::assertThat)
   *                   .first()
   *                   .startsWith("fro")
   *                   .endsWith("do");</code></pre>
   *
   * This ensures that all navigation methods return element-specific assertions without needing additional parameters.
   */
  // @format:off
  @Deprecated
  @SuppressWarnings({ "unchecked", "rawtypes" })
  public static <ACTUAL extends Iterable<? extends ELEMENT>, ELEMENT, ELEMENT_ASSERT extends AbstractAssert<ELEMENT_ASSERT, ELEMENT>>
         FactoryBasedNavigableIterableAssert<?, ACTUAL, ELEMENT, ELEMENT_ASSERT> assertThat(Iterable<? extends ELEMENT> actual,
                                                                                 AssertFactory<ELEMENT, ELEMENT_ASSERT> assertFactory) {
    return new FactoryBasedNavigableIterableAssert(actual, FactoryBasedNavigableIterableAssert.class, assertFactory);
  }
  // @format:on

  public FactoryBasedNavigableIterableAssert(ACTUAL actual, Class<?> selfType,
                                             AssertFactory<ELEMENT, ELEMENT_ASSERT> assertFactory) {
    super(actual, selfType);
    this.assertFactory = assertFactory;
  }

  @SuppressWarnings("unchecked")
  @Override
  public ELEMENT_ASSERT toAssert(ELEMENT value, String description) {
    return assertFactory.createAssert(value).as(description);
  }

  @SuppressWarnings("unchecked")
  @Override
  protected SELF newAbstractIterableAssert(Iterable<? extends ELEMENT> iterable) {
    return (SELF) new FactoryBasedNavigableIterableAssert<>(iterable, FactoryBasedNavigableIterableAssert.class, assertFactory);
  }
}
