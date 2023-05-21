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
 * Copyright 2012-2023 the original author or authors.
 */
package org.assertj.core.internal;

import java.nio.file.Path;
import java.util.Comparator;
import java.util.Map.Entry;
import java.util.stream.Stream;

import org.assertj.core.util.DoubleComparator;
import org.assertj.core.util.FloatComparator;
import org.assertj.core.util.PathNaturalOrderComparator;

/**
 * An internal holder of the comparators for type. It is used to store comparators for registered classes.
 * When looking for a Comparator for a given class the holder returns the most relevant comparator.
 *
 * @author Filip Hrisafov
 */
public class TypeComparators extends TypeHolder<Comparator<?>> {

  private static final double DOUBLE_COMPARATOR_PRECISION = 1e-15;
  private static final DoubleComparator DEFAULT_DOUBLE_COMPARATOR = new DoubleComparator(DOUBLE_COMPARATOR_PRECISION);

  private static final float FLOAT_COMPARATOR_PRECISION = 1e-6f;
  private static final FloatComparator DEFAULT_FLOAT_COMPARATOR = new FloatComparator(FLOAT_COMPARATOR_PRECISION);

  private static final Comparator<Path> DEFAULT_PATH_COMPARATOR = PathNaturalOrderComparator.INSTANCE;

  public static TypeComparators defaultTypeComparators() {
    TypeComparators comparatorByType = new TypeComparators();
    comparatorByType.registerComparator(Double.class, DEFAULT_DOUBLE_COMPARATOR);
    comparatorByType.registerComparator(Float.class, DEFAULT_FLOAT_COMPARATOR);
    comparatorByType.registerComparator(Path.class, DEFAULT_PATH_COMPARATOR);
    return comparatorByType;
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
  public Comparator<?> getComparatorForType(Class<?> clazz) {
    return super.get(clazz);
  }

  /**
   * Checks, whether an any custom comparator is associated with the giving type.
   *
   * @param type the type for which to check a comparator
   * @return is the giving type associated with any custom comparator
   */
  public boolean hasComparatorForType(Class<?> type) {
    return super.hasEntity(type);
  }

  /**
   * Puts the {@code comparator} for the given {@code clazz}.
   *
   * @param clazz the class for the comparator
   * @param comparator the comparator itself
   * @param <T> the type of the objects for the comparator
   */
  public <T> void registerComparator(Class<T> clazz, Comparator<? super T> comparator) {
    super.put(clazz, comparator);
  }

  /**
   * Returns a sequence of all type-comparator pairs which the current holder supplies.
   *
   * @return sequence of field-comparator pairs
   */
  public Stream<Entry<Class<?>, Comparator<?>>> comparatorByTypes() {
    return super.entityByTypes();
  }
}
