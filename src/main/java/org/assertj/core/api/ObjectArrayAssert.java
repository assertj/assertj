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

import static org.assertj.core.util.Arrays.array;

import java.util.List;
import java.util.concurrent.atomic.AtomicReferenceArray;
import java.util.function.Function;

import org.assertj.core.groups.Tuple;

/**
 * Assertion methods for arrays of objects.
 * <p>
 * To create an instance of this class, invoke <code>{@link Assertions#assertThat(Object[])}</code>.
 * </p>
 * 
 * @param <ELEMENT> the type of elements of the "actual" value.
 * 
 * @author Yvonne Wang
 * @author Alex Ruiz
 * @author Joel Costigliola
 * @author Nicolas François
 * @author Mikhail Mazursky
 */
public class ObjectArrayAssert<ELEMENT> extends AbstractObjectArrayAssert<ObjectArrayAssert<ELEMENT>, ELEMENT> {

  public ObjectArrayAssert(ELEMENT[] actual) {
    super(actual, ObjectArrayAssert.class);
  }

  public ObjectArrayAssert(AtomicReferenceArray<ELEMENT> actual) {
    this(array(actual));
  }

  @Override
  protected ObjectArrayAssert<ELEMENT> newObjectArrayAssert(ELEMENT[] array) {
    return new ObjectArrayAssert<>(array);
  }

  @Override
  @SafeVarargs
  public final AbstractListAssert<?, List<? extends Tuple>, Tuple, ObjectAssert<Tuple>> extracting(Function<? super ELEMENT, ?>... extractors) {
    return super.extracting(extractors);
  }

  @Override
  @SafeVarargs
  public final ObjectArrayAssert<ELEMENT> contains(ELEMENT... values) {
    return super.contains(values);
  }

  @Override
  @SafeVarargs
  public final ObjectArrayAssert<ELEMENT> containsOnly(ELEMENT... values) {
    return super.containsOnly(values);
  }

  @Override
  @SafeVarargs
  public final ObjectArrayAssert<ELEMENT> containsOnlyOnce(ELEMENT... values) {
    return super.containsOnlyOnce(values);
  }

  @Override
  @SafeVarargs
  public final ObjectArrayAssert<ELEMENT> containsExactly(ELEMENT... values) {
    return super.containsExactly(values);
  }

  @Override
  @SafeVarargs
  public final ObjectArrayAssert<ELEMENT> containsExactlyInAnyOrder(ELEMENT... values) {
    return super.containsExactlyInAnyOrder(values);
  }

  @Override
  @SafeVarargs
  public final ObjectArrayAssert<ELEMENT> containsAnyOf(ELEMENT... values) {
    return super.containsAnyOf(values);
  }

  @Override
  @SafeVarargs
  public final ObjectArrayAssert<ELEMENT> isSubsetOf(ELEMENT... values) {
    return super.isSubsetOf(values);
  }

  @Override
  @SafeVarargs
  public final ObjectArrayAssert<ELEMENT> containsSequence(ELEMENT... sequence) {
    return super.containsSequence(sequence);
  }

  @Override
  @SafeVarargs
  public final ObjectArrayAssert<ELEMENT> doesNotContainSequence(ELEMENT... sequence) {
    return super.doesNotContainSequence(sequence);
  }

  @Override
  @SafeVarargs
  public final ObjectArrayAssert<ELEMENT> containsSubsequence(ELEMENT... sequence) {
    return super.containsSubsequence(sequence);
  }

  @Override
  @SafeVarargs
  public final ObjectArrayAssert<ELEMENT> doesNotContainSubsequence(ELEMENT... sequence) {
    return super.doesNotContainSubsequence(sequence);
  }

  @Override
  @SafeVarargs
  public final ObjectArrayAssert<ELEMENT> doesNotContain(ELEMENT... values) {
    return super.doesNotContain(values);
  }

  @Override
  @SafeVarargs
  public final ObjectArrayAssert<ELEMENT> endsWith(ELEMENT first, ELEMENT... rest) {
    return super.endsWith(first, rest);
  }

}
