/**
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 *
 * Copyright 2012-2017 the original author or authors.
 */
package org.assertj.core.api;

import java.lang.reflect.Constructor;

/**
 * Build the Assert instance by reflection.
 * 
 * @since 2.5.0 / 3.5.0
 */
// @format:off
public class ClassBasedNavigableIterableAssert<SELF extends ClassBasedNavigableIterableAssert<SELF, ACTUAL, ELEMENT, ELEMENT_ASSERT>,
                                               ACTUAL extends Iterable<? extends ELEMENT>, 
                                               ELEMENT, 
                                               ELEMENT_ASSERT extends AbstractAssert<ELEMENT_ASSERT, ELEMENT>>
       extends AbstractIterableAssert<SELF, ACTUAL, ELEMENT, ELEMENT_ASSERT> {
// @format:on

  private Class<ELEMENT_ASSERT> assertClass;

  public ClassBasedNavigableIterableAssert(ACTUAL actual, Class<?> selfType, Class<ELEMENT_ASSERT> assertClass) {
    super(actual, selfType);
    this.assertClass = assertClass;
  }

  public ELEMENT_ASSERT toAssert(ELEMENT value, String description) {
    return buildAssert(value, description, value.getClass());
  }

  private <V> ELEMENT_ASSERT buildAssert(V value, String description, Class<?> clazz) {
    try {
      Constructor<?>[] declaredConstructors = assertClass.getDeclaredConstructors();
      // find a matching Assert constructor for E or one of its subclass.
      for (int i = 0; i < declaredConstructors.length; i++) {
        Constructor<?> constructor = declaredConstructors[i];
        if (constructor.getParameterTypes().length == 1 && constructor.getParameterTypes()[0].isAssignableFrom(clazz)) {
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
