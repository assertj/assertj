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

import static org.assertj.core.util.Preconditions.checkArgument;

import java.util.List;

/**
 * Provides helper methods for navigating a list property in a generated assertion class so we can chain assertions
 * through deeply nested models more easily.
 * 
 * @since 2.5.0 / 3.5.0
 */
//@format:off
public class FactoryBasedNavigableListAssert<SELF extends FactoryBasedNavigableListAssert<SELF, ACTUAL, ELEMENT, ELEMENT_ASSERT>, 
                                             ACTUAL extends List<? extends ELEMENT>, 
                                             ELEMENT, 
                                             ELEMENT_ASSERT extends AbstractAssert<ELEMENT_ASSERT, ELEMENT>>
       extends AbstractListAssert<SELF, ACTUAL, ELEMENT, ELEMENT_ASSERT> {
         
  private AssertFactory<ELEMENT, ELEMENT_ASSERT> assertFactory;

  /**
   * @deprecated
   * This was added to help creating type-specific assertions for the elements of an {@link List} instance.
   * <p>
   * Deprecated way:
   * <pre><code class='java'> List&lt;String&gt; hobbits = List.of("frodo", "sam", "Pippin");
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
  public static <ACTUAL extends List<? extends ELEMENT>, ELEMENT, ELEMENT_ASSERT extends AbstractAssert<ELEMENT_ASSERT, ELEMENT>>
         FactoryBasedNavigableListAssert<?, ACTUAL, ELEMENT, ELEMENT_ASSERT> assertThat(List<? extends ELEMENT> actual,
                                                                                        AssertFactory<ELEMENT, ELEMENT_ASSERT> assertFactory) {
    return new FactoryBasedNavigableListAssert(actual, FactoryBasedNavigableListAssert.class, assertFactory);
  }
  
// @format:on

  public FactoryBasedNavigableListAssert(ACTUAL actual, Class<?> selfType,
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
    checkArgument(iterable instanceof List, "Expecting %s to be a List", iterable);
    return (SELF) new FactoryBasedNavigableListAssert<>((List<? extends ELEMENT>) iterable,
                                                        FactoryBasedNavigableListAssert.class,
                                                        assertFactory);
  }

}
