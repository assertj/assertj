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
 * Copyright 2012-2020 the original author or authors.
 */
package org.assertj.core.internal;

import static java.lang.String.format;
import static org.assertj.core.util.Strings.join;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.stream.Stream;

import org.assertj.core.util.DoubleComparator;
import org.assertj.core.util.FloatComparator;
import org.assertj.core.util.VisibleForTesting;
import org.assertj.core.util.introspection.ClassUtils;

/**
 * An internal holder of the comparators for type. It is used to store comparators for registered classes.
 * When looking for a Comparator for a given class the holder returns the most relevant comparator.
 *
 * @author Filip Hrisafov
 */
public class TypeComparators {

  private static final double DOUBLE_COMPARATOR_PRECISION = 1e-15;
  private static final DoubleComparator DEFAULT_DOUBLE_COMPARATOR = new DoubleComparator(DOUBLE_COMPARATOR_PRECISION);

  private static final float FLOAT_COMPARATOR_PRECISION = 1e-6f;
  private static final FloatComparator DEFAULT_FLOAT_COMPARATOR = new FloatComparator(FLOAT_COMPARATOR_PRECISION);

  // don't convert it to a lambda as it degrades TypeComparatorsPerfTest execution time by ~66%
  private static final Comparator<Class<?>> CLASS_COMPARATOR = new Comparator<Class<?>>() {

    @Override
    public int compare(Class<?> class1, Class<?> class2) {
      return class1.getName().compareTo(class2.getName());
    }
  };

  @VisibleForTesting
  Map<Class<?>, Comparator<?>> typeComparators;

  public static TypeComparators defaultTypeComparators() {
    TypeComparators comparatorByType = new TypeComparators();
    comparatorByType.put(Double.class, DEFAULT_DOUBLE_COMPARATOR);
    comparatorByType.put(Float.class, DEFAULT_FLOAT_COMPARATOR);
    return comparatorByType;
  }

  public TypeComparators() {
    typeComparators = new TreeMap<>(CLASS_COMPARATOR);
  }

  /**
   * This method returns the most relevant comparator for the given class. The most relevant comparator is the
   * comparator which is registered for the class that is closest in the inheritance chain of the given {@code clazz}.
   * The order of checks is the following:
   * 1. If there is a registered comparator for {@code clazz} then this one is used
   * 2. We check if there is a registered comparator for a superclass of {@code clazz}
   * 3. We check if there is a registered comparator for an interface of {@code clazz}
   *
   * @param clazz the class for which to find a comparator
   * @return the most relevant comparator, or {@code null} if no comparator could be found
   */
  public Comparator<?> get(Class<?> clazz) {
    Comparator<?> comparator = typeComparators.get(clazz);
    if (comparator == null) {
      for (Class<?> superClass : ClassUtils.getAllSuperclasses(clazz)) {
        if (typeComparators.containsKey(superClass)) {
          return typeComparators.get(superClass);
        }
      }
      for (Class<?> interfaceClass : ClassUtils.getAllInterfaces(clazz)) {
        if (typeComparators.containsKey(interfaceClass)) {
          return typeComparators.get(interfaceClass);
        }
      }
    }
    return comparator;
  }

  public boolean hasComparatorForType(Class<?> type) {
    return get(type) != null;
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

  /**
   * Removes all registered comparators.
   */
  public void clear() {
    typeComparators.clear();
  }

  public Stream<Entry<Class<?>, Comparator<?>>> comparatorByTypes() {
    return typeComparators.entrySet().stream();
  }

  @Override
  public int hashCode() {
    return typeComparators.hashCode();
  }

  @Override
  public boolean equals(Object obj) {
    return obj instanceof TypeComparators
           && java.util.Objects.equals(typeComparators, ((TypeComparators) obj).typeComparators);
  }

  @Override
  public String toString() {
    List<String> registeredComparatorsDescription = new ArrayList<>();
    for (Entry<Class<?>, Comparator<?>> registeredComparator : this.typeComparators.entrySet()) {
      registeredComparatorsDescription.add(formatRegisteredComparator(registeredComparator));
    }
    return format("{%s}", join(registeredComparatorsDescription).with(", "));
  }

  private static String formatRegisteredComparator(Entry<Class<?>, Comparator<?>> next) {
    return format("%s -> %s", next.getKey().getSimpleName(), next.getValue());
  }

}
