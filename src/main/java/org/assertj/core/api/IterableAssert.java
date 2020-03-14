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
package org.assertj.core.api;

import static java.util.stream.Collectors.toList;

import java.util.Iterator;
import java.util.List;
import java.util.function.Function;

import org.assertj.core.api.iterable.ThrowingExtractor;
import org.assertj.core.groups.Tuple;
import org.assertj.core.util.Streams;

/**
 * Assertion methods for {@link Iterable}.
 * <p>
 * To create an instance of this class, invoke <code>{@link Assertions#assertThat(Iterable)}</code>.
 * </p>
 *
 * @param <ELEMENT> the type of elements of the "actual" value.
 *
 * @author Yvonne Wang
 * @author Alex Ruiz
 * @author Matthieu Baechler
 * @author Joel Costigliola
 * @author Mikhail Mazursky
 * @author Julien Meddah
 */
public class IterableAssert<ELEMENT> extends
    FactoryBasedNavigableIterableAssert<IterableAssert<ELEMENT>, Iterable<? extends ELEMENT>, ELEMENT, ObjectAssert<ELEMENT>> {

  public IterableAssert(Iterable<? extends ELEMENT> actual) {
    super(actual, IterableAssert.class, new ObjectAssertFactory<>());
  }

  @Override
  protected IterableAssert<ELEMENT> newAbstractIterableAssert(Iterable<? extends ELEMENT> iterable) {
    return new IterableAssert<>(iterable);
  }

  static <T> Iterable<T> toIterable(Iterator<T> iterator) {
    return Streams.stream(iterator).collect(toList());
  }

  @Override
  @SafeVarargs
  public final IterableAssert<ELEMENT> contains(ELEMENT... values) {
    return super.contains(values);
  }

  @Override
  @SafeVarargs
  public final IterableAssert<ELEMENT> containsOnly(ELEMENT... values) {
    return super.containsOnly(values);
  }

  @Override
  @SafeVarargs
  public final IterableAssert<ELEMENT> containsOnlyOnce(ELEMENT... values) {
    return super.containsOnlyOnce(values);
  }

  @Override
  @SafeVarargs
  public final IterableAssert<ELEMENT> containsExactly(ELEMENT... values) {
    return super.containsExactly(values);
  }

  @Override
  @SafeVarargs
  public final IterableAssert<ELEMENT> containsExactlyInAnyOrder(ELEMENT... values) {
    return super.containsExactlyInAnyOrder(values);
  }

  @Override
  @SafeVarargs
  public final IterableAssert<ELEMENT> containsAnyOf(ELEMENT... values) {
    return super.containsAnyOf(values);
  }

  @Override
  @SafeVarargs
  public final IterableAssert<ELEMENT> isSubsetOf(ELEMENT... values) {
    return super.isSubsetOf(values);
  }

  @Override
  @SafeVarargs
  public final IterableAssert<ELEMENT> containsSequence(ELEMENT... sequence) {
    return super.containsSequence(sequence);
  }

  @Override
  @SafeVarargs
  public final IterableAssert<ELEMENT> doesNotContainSequence(ELEMENT... sequence) {
    return super.doesNotContainSequence(sequence);
  }

  @Override
  @SafeVarargs
  public final IterableAssert<ELEMENT> containsSubsequence(ELEMENT... sequence) {
    return super.containsSubsequence(sequence);
  }

  @Override
  @SafeVarargs
  public final IterableAssert<ELEMENT> doesNotContainSubsequence(ELEMENT... sequence) {
    return super.doesNotContainSubsequence(sequence);
  }

  @Override
  @SafeVarargs
  public final IterableAssert<ELEMENT> doesNotContain(ELEMENT... values) {
    return super.doesNotContain(values);
  }

  @Override
  @SafeVarargs
  public final IterableAssert<ELEMENT> endsWith(ELEMENT first, ELEMENT... rest) {
    return super.endsWith(first, rest);
  }

  @Override
  @SafeVarargs
  public final <EXCEPTION extends Exception> AbstractListAssert<?, List<?>, Object, ObjectAssert<Object>> flatExtracting(ThrowingExtractor<? super ELEMENT, ?, EXCEPTION>... extractors) {
    return super.flatExtracting(extractors);
  }

  @Override
  @SafeVarargs
  public final AbstractListAssert<?, List<?>, Object, ObjectAssert<Object>> flatExtracting(Function<? super ELEMENT, ?>... extractors) {
    return super.flatExtracting(extractors);
  }
  
  @Override
  @SafeVarargs
  public final AbstractListAssert<?, List<? extends Tuple>, Tuple, ObjectAssert<Tuple>> extracting(Function<? super ELEMENT, ?>... extractors) {
    return super.extracting(extractors);
  }

}
