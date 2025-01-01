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
package org.assertj.core.api;

/**
 * Provides helper methods for navigating a list property in a generated assertion class so we can chain assertions
 * through deeply nested models more easily.
 * 
 * @since 2.5.0 / 3.5.0
 */
//@format:off
public class FactoryBasedNavigableIterableAssert<SELF extends FactoryBasedNavigableIterableAssert<SELF, ACTUAL, ELEMENT, ELEMENT_ASSERT>, 
                                                 ACTUAL extends Iterable<? extends ELEMENT>, 
                                                 ELEMENT, 
                                                 ELEMENT_ASSERT extends AbstractAssert<ELEMENT_ASSERT, ELEMENT>>
       extends AbstractIterableAssert<SELF, ACTUAL, ELEMENT, ELEMENT_ASSERT> {

  private AssertFactory<ELEMENT, ELEMENT_ASSERT> assertFactory;


  /**
   * @deprecated
   * This was added to help creating type-specific assertions for the elements of an {@link Iterable} instance.
   * <p>
   * Deprecated way:
   * <pre><code class='java'> Iterable&lt;String&gt; hobbits = Set.of("frodo", "sam", "Pippin");
   * assertThat(hobbits, StringAssert::new).first()
   *                                       .startsWith("fro")
   *                                       .endsWith("do");</code></pre>
   *
   * However, there is a better way with {@link InstanceOfAssertFactory} and the corresponding
   * {@link AbstractIterableAssert#first(InstanceOfAssertFactory) first(InstanceOfAssertFactory)}.
   * <p>
   * New way:
   * <pre><code class='java'> assertThat(hobbits).first(STRING) // static import of InstanceOfAssertFactories.STRING
   *                    .startsWith("fro")
   *                    .endsWith("do");</code></pre>
   *
   * The main advantage of the latter is easier discoverability and the use of InstanceOfAssertFactory which is the
   * preferred way to create type-specific assertions in AssertJ API.
   */
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
