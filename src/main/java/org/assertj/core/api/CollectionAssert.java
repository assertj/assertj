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

import org.assertj.core.api.iterable.ThrowingExtractor;
import org.assertj.core.groups.Tuple;

import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

public class CollectionAssert<ELEMENT> extends
  FactoryBasedNavigableCollectionAssert<CollectionAssert<ELEMENT>, Collection<? extends ELEMENT>, ELEMENT, ObjectAssert<ELEMENT>> {


  public CollectionAssert(Collection<? extends ELEMENT> elements) {
    super(elements, CollectionAssert.class, new ObjectAssertFactory<>());
  }

  @Override
  @SafeVarargs
  public final CollectionAssert<ELEMENT> contains(ELEMENT... values) {
    return super.contains(values);
  }

  @Override
  @SafeVarargs
  public final CollectionAssert<ELEMENT> containsOnly(ELEMENT... values) {
    return super.containsOnly(values);
  }

  @Override
  @SafeVarargs
  public final CollectionAssert<ELEMENT> containsOnlyOnce(ELEMENT... values) {
    return super.containsOnlyOnce(values);
  }

  @Override
  @SafeVarargs
  public final CollectionAssert<ELEMENT> containsExactly(ELEMENT... values) {
    return super.containsExactly(values);
  }

  @Override
  @SafeVarargs
  public final CollectionAssert<ELEMENT> containsExactlyInAnyOrder(ELEMENT... values) {
    return super.containsExactlyInAnyOrder(values);
  }

  @Override
  @SafeVarargs
  public final CollectionAssert<ELEMENT> containsAnyOf(ELEMENT... values) {
    return super.containsAnyOf(values);
  }

  @Override
  @SafeVarargs
  public final CollectionAssert<ELEMENT> isSubsetOf(ELEMENT... values) {
    return super.isSubsetOf(values);
  }

  @Override
  @SafeVarargs
  public final CollectionAssert<ELEMENT> containsSequence(ELEMENT... sequence) {
    return super.containsSequence(sequence);
  }

  @Override
  @SafeVarargs
  public final CollectionAssert<ELEMENT> doesNotContainSequence(ELEMENT... sequence) {
    return super.doesNotContainSequence(sequence);
  }

  @Override
  @SafeVarargs
  public final CollectionAssert<ELEMENT> containsSubsequence(ELEMENT... sequence) {
    return super.containsSubsequence(sequence);
  }

  @Override
  @SafeVarargs
  public final CollectionAssert<ELEMENT> doesNotContainSubsequence(ELEMENT... sequence) {
    return super.doesNotContainSubsequence(sequence);
  }

  @Override
  @SafeVarargs
  public final CollectionAssert<ELEMENT> doesNotContain(ELEMENT... values) {
    return super.doesNotContain(values);
  }

  @Override
  @SafeVarargs
  public final CollectionAssert<ELEMENT> endsWith(ELEMENT first, ELEMENT... rest) {
    return super.endsWith(first, rest);
  }

//  @Override
//  @SafeVarargs
//  public final AbstractCollectionAssert<?, Collection<? extends Tuple>, Tuple, ObjectAssert<Tuple>> extracting(Function<? super ELEMENT, ?>... extractors) {
//    return super.extracting(extractors);
//  }
//
//  @Override
//  @SafeVarargs
//  public final AbstractCollectionAssert<?, List<? extends Tuple>, Tuple, ObjectAssert<Tuple>> map(Function<? super ELEMENT, ?>... mappers) {
//    return super.map(mappers);
//  }
//
//  @Override
//  @SafeVarargs
//  public final <EXCEPTION extends Exception> AbstractCollectionAssert<?, List<?>, Object, ObjectAssert<Object>> flatExtracting(ThrowingExtractor<? super ELEMENT, ?, EXCEPTION>... extractors) {
//    return super.flatExtracting(extractors);
//  }
//
//  @Override
//  @SafeVarargs
//  public final <EXCEPTION extends Exception> AbstractCollectionAssert<?, List<? extends Object>, Object, ObjectAssert<Object>> flatMap(ThrowingExtractor<? super ELEMENT, ?, EXCEPTION>... mappers) {
//    return super.flatMap(mappers);
//  }
//
//  @Override
//  @SafeVarargs
//  public final AbstractCollectionAssert<?, List<?>, Object, ObjectAssert<Object>> flatExtracting(Function<? super ELEMENT, ?>... extractors) {
//    return super.flatExtracting(extractors);
//  }
//
//  @Override
//  @SafeVarargs
//  public final AbstractCollectionAssert<?, List<? extends Object>, Object, ObjectAssert<Object>> flatMap(Function<? super ELEMENT, ?>... mappers) {
//    return super.flatMap(mappers);
//  }

  @Override
  @SafeVarargs
  public final CollectionAssert<ELEMENT> satisfiesExactly(Consumer<? super ELEMENT>... requirements) {
    return super.satisfiesExactly(requirements);
  }

  @Override
  @SafeVarargs
  public final CollectionAssert<ELEMENT> satisfiesExactlyInAnyOrder(Consumer<? super ELEMENT>... requirements) {
    return super.satisfiesExactlyInAnyOrder(requirements);
  }
}
