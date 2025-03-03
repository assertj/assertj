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

import java.lang.reflect.Constructor;

/**
 * Build the Assert instance by reflection.
 * 
 * @since 2.5.0 / 3.5.0
 * @deprecated Use {@link FactoryBasedNavigableIterableAssert} instead.
 */
//@format:off
@Deprecated(since = "3", forRemoval = true)
public class ClassBasedNavigableIterableAssert<SELF extends ClassBasedNavigableIterableAssert<SELF, ACTUAL, ELEMENT, ELEMENT_ASSERT>,
                                               ACTUAL extends Iterable<? extends ELEMENT>, 
                                               ELEMENT, 
                                               ELEMENT_ASSERT extends AbstractAssert<ELEMENT_ASSERT, ELEMENT>>
       extends AbstractIterableAssert<SELF, ACTUAL, ELEMENT, ELEMENT_ASSERT> {
   private Class<ELEMENT_ASSERT> assertClass;


  /**
   * @deprecated
   * This was added to help creating type-specific assertions for the elements of an {@link Iterable} instance.
   * <p>
   * Deprecated way:
   * <pre><code class='java'> Iterable&lt;String&gt; hobbits = Set.of("frodo", "sam", "Pippin");
   * assertThat(hobbits, StringAssert.class).first()
   *                                        .startsWith("fro")
   *                                        .endsWith("do");</code></pre>
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
  @Deprecated(since = "3", forRemoval = true)
  @SuppressWarnings({ "rawtypes", "unchecked" })
   public static <ACTUAL extends Iterable<? extends ELEMENT>, ELEMENT, ELEMENT_ASSERT extends AbstractAssert<ELEMENT_ASSERT, ELEMENT>>
          ClassBasedNavigableIterableAssert<?, ACTUAL, ELEMENT, ELEMENT_ASSERT> assertThat(ACTUAL actual,
                                                                                           Class<ELEMENT_ASSERT> assertClass) {
     return new ClassBasedNavigableIterableAssert(actual, ClassBasedNavigableIterableAssert.class, assertClass);
   }
   
// @format:on

  public ClassBasedNavigableIterableAssert(ACTUAL actual, Class<?> selfType, Class<ELEMENT_ASSERT> assertClass) {
    super(actual, selfType);
    this.assertClass = assertClass;
  }

  @SuppressWarnings("unchecked")
  @Override
  protected SELF newAbstractIterableAssert(Iterable<? extends ELEMENT> iterable) {
    return (SELF) new ClassBasedNavigableIterableAssert<>(iterable, ClassBasedNavigableIterableAssert.class, assertClass);
  }

  @Override
  public ELEMENT_ASSERT toAssert(ELEMENT value, String description) {
    return buildAssert(value, description, value.getClass());
  }

  private <V> ELEMENT_ASSERT buildAssert(V value, String description, Class<?> clazz) {
    try {
      Constructor<?>[] declaredConstructors = assertClass.getDeclaredConstructors();
      // find a matching Assert constructor for E or one of its subclass.
      for (Constructor<?> constructor : declaredConstructors) {
        if (constructor.getParameterCount() == 1 && constructor.getParameterTypes()[0].isAssignableFrom(clazz)) {
          @SuppressWarnings("unchecked")
          ELEMENT_ASSERT newAssert = (ELEMENT_ASSERT) constructor.newInstance(value);
          return newAssert.as(description);
        }
      }
      throw new RuntimeException("Failed to find a constructor matching " + value
                                 + " class to build the expected Assert class");
    } catch (Exception e) {
      throw new RuntimeException("Failed to build an assert object with " + value + ": " + e.getMessage(), e);
    }
  }
}
