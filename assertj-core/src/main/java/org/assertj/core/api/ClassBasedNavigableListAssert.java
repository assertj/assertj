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

import java.lang.reflect.Constructor;
import java.util.List;

/**
 * Build the Assert instance by reflection.
 * 
 * @since 2.5.0 / 3.5.0
 * @deprecated Use {@link FactoryBasedNavigableListAssert} instead.
 */
// @format:off
@Deprecated
public class ClassBasedNavigableListAssert<SELF extends ClassBasedNavigableListAssert<SELF, ACTUAL, ELEMENT, ELEMENT_ASSERT>, 
                                           ACTUAL extends List<? extends ELEMENT>, 
                                           ELEMENT, 
                                           ELEMENT_ASSERT extends AbstractAssert<ELEMENT_ASSERT, ELEMENT>>
       extends AbstractListAssert<SELF, ACTUAL, ELEMENT, ELEMENT_ASSERT> {
  // @format:on

  private final Class<ELEMENT_ASSERT> assertClass;

  /**
   * @deprecated
   * This was added to help create type-specific assertions for the elements of a {@link List} instance.
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
   * <pre><code class='java'>assertThat(hobbits).withElementAssert(StringAssert::new)
   *                   .first()
   *                   .startsWith("fro")
   *                   .endsWith("do");</code></pre>
   *
   * This ensures that all navigation methods return element-specific assertions without needing additional parameters.
   */
  // @format:off
  @Deprecated
   @SuppressWarnings({ "unchecked", "rawtypes" })
   public static <ELEMENT, ACTUAL extends List<? extends ELEMENT>, ELEMENT_ASSERT extends AbstractAssert<ELEMENT_ASSERT, ELEMENT>>
          ClassBasedNavigableListAssert<?, ACTUAL, ELEMENT, ELEMENT_ASSERT> assertThat(List<? extends ELEMENT> actual,
                                                                                       Class<ELEMENT_ASSERT> assertClass) {
     return new ClassBasedNavigableListAssert(actual, assertClass);
   }
  // @format:on

  public ClassBasedNavigableListAssert(ACTUAL actual, Class<ELEMENT_ASSERT> assertClass) {
    super(actual, ClassBasedNavigableListAssert.class);
    this.assertClass = assertClass;
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

  @SuppressWarnings("unchecked")
  @Override
  protected SELF newAbstractIterableAssert(Iterable<? extends ELEMENT> iterable) {
    checkArgument(iterable instanceof List, "Expecting %s to be a List", iterable);
    return (SELF) new ClassBasedNavigableListAssert<>((List<? extends ELEMENT>) iterable, assertClass);
  }
}
