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
package org.assertj.core.internal;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

import org.assertj.core.util.introspection.ClassUtils;

/**
 * An internal holder of the comparators for type. It is used to store comparators for registered classes.
 * When looking for a Comparator for a given class the holder returns the most relevant comparator.
 *
 * @author Filip Hrisafov
 */
public class TypeComparators {

  private Map<Class<?>, Comparator<?>> typeComparators;

  public TypeComparators() {
    typeComparators = new HashMap<>();
  }

  /**
   * This method returns the most relevant comparator for the given class. The most relevant comparator is the
   * comparator which is registered for the class that is closest in the inheritance chain of the given {@code clazz}.
   * The order of checks is the following:
   * 1. If there is a registered comparator for {@code clazz} then this one is used
   * 2. We check if there is a registered comparator for all the superclasses of {@code clazz}
   * 3. We check if there is a registered comparator for all the interfaces if {@code clazz}
   *
   * @param clazz the class for which to find a comparator
   * @return the most relevant comparator, or {@code null} if no comparator could be found
   */
  public Comparator<?> get(Class<?> clazz) {
    Comparator<?> comparator = typeComparators.get(clazz);

    if (comparator == null) {
      for (Class<?> superClass : ClassUtils.getAllSuperclasses(clazz)) {
        if (typeComparators.containsKey(superClass)) {
          comparator = typeComparators.get(superClass);
          break;
        }
      }

      if (comparator == null) {
        for (Class<?> interfaceClass : ClassUtils.getAllInterfaces(clazz)) {
          if (typeComparators.containsKey(interfaceClass)) {
            comparator = typeComparators.get(interfaceClass);
            break;
          }
        }
      }
    }
    return comparator;
  }

  /**
   * Puts the {@code comparator} for the given {@code clazz}.
   *
   * @param clazz the class for the comparator
   * @param comparator the comparator it self
   * @param <T> the type of the objects for the comparator
   */
  public <T> void put(Class<T> clazz, Comparator<? super T> comparator) {
    typeComparators.put(clazz, comparator);
  }

  /**
   * @return {@code true} is there are registered comparators, {@code false} otherwise
   */
  public boolean isEmpty() {
    return typeComparators.isEmpty();
  }

  @Override
  public int hashCode() {
    return typeComparators.hashCode();
  }

  @Override
  public boolean equals(Object obj) {
    return obj instanceof TypeComparators && typeComparators.equals(((TypeComparators) obj).typeComparators);
  }

  @Override
  public String toString() {
    return typeComparators.toString();
  }
}
