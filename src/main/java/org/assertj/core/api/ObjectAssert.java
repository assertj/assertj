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
 * Copyright 2012-2021 the original author or authors.
 */
package org.assertj.core.api;

import java.util.Comparator;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;

import org.assertj.core.util.CheckReturnValue;

/**
 * Assertion methods for {@link Object}s.
 * <p>
 * To create a new instance of this class, invoke <code>{@link Assertions#assertThat(Object)}</code>.
 * </p>
 * @param <ACTUAL> the type of the "actual" value.
 *
 * @author Yvonne Wang
 * @author Alex Ruiz
 * @author Nicolas François
 * @author Mikhail Mazursky
 */
public class ObjectAssert<ACTUAL> extends AbstractObjectAssert<ObjectAssert<ACTUAL>, ACTUAL> {

  public ObjectAssert(ACTUAL actual) {
    super(actual, ObjectAssert.class);
  }

  public ObjectAssert(AtomicReference<ACTUAL> actual) {
    this(actual == null ? null : actual.get());
  }

  @Override
  @CheckReturnValue
  @SafeVarargs
  public final AbstractListAssert<?, List<?>, Object, ObjectAssert<Object>> extracting(Function<? super ACTUAL, ?>... extractors) {
    return super.extracting(extractors);
  }

  @Override
  public ComparatorBasedObjectAssert<ACTUAL> usingComparator(Comparator<? super ACTUAL> customComparator) {
    return usingComparator(customComparator, null);
  }

  @Override
  public ComparatorBasedObjectAssert<ACTUAL> usingComparator(Comparator<? super ACTUAL> customComparator,
                                                             String customComparatorDescription) {
    return new ComparatorBasedObjectAssert<>(actual, this, customComparator, customComparatorDescription);
  }

}
