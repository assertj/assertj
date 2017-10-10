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

import static org.assertj.core.error.ShouldStartWith.shouldStartWith;
import static org.assertj.core.internal.CommonValidations.checkIsNotNull;

import java.util.AbstractList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.BaseStream;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import java.util.stream.Stream;

import org.assertj.core.internal.Failures;
import org.assertj.core.util.Lists;
import org.assertj.core.util.VisibleForTesting;

/**
 * Assertion methods for {@link List}s.
 * <p>
 * To create an instance of this class, invoke <code>{@link Assertions#assertThat(List)}</code>.
 * <p>
 *
 * @param <ELEMENT> the type of elements of the "actual" value.
 *
 * @author Yvonne Wang
 * @author Alex Ruiz
 * @author Joel Costigliola
 * @author Mikhail Mazursky
 */
public class ListAssert<ELEMENT> extends
    FactoryBasedNavigableListAssert<ListAssert<ELEMENT>, List<? extends ELEMENT>, ELEMENT, ObjectAssert<ELEMENT>> {

  public ListAssert(List<? extends ELEMENT> actual) {
    super(actual, ListAssert.class, new ObjectAssertFactory<ELEMENT>());
  }

  protected ListAssert(Stream<? extends ELEMENT> actual) {
    this(actual == null ? null : new ListFromStream<>(actual));
  }

  @SuppressWarnings({ "unchecked", "rawtypes" })
  protected ListAssert(IntStream actual) {
    this(actual == null ? null : new ListFromStream(actual));
  }

  @SuppressWarnings({ "unchecked", "rawtypes" })
  protected ListAssert(LongStream actual) {
    this(actual == null ? null : new ListFromStream(actual));
  }

  @SuppressWarnings({ "unchecked", "rawtypes" })
  protected ListAssert(DoubleStream actual) {
    this(actual == null ? null : new ListFromStream(actual));
  }

  @Override
  public ListAssert<ELEMENT> isEqualTo(Object expected) {
    if (actual instanceof ListFromStream && asListFromStream().stream == expected) {
      return myself;
    }
    return super.isEqualTo(expected);
  }

  @Override
  public ListAssert<ELEMENT> isInstanceOf(Class<?> type) {
    if (actual instanceof ListFromStream) {
      objects.assertIsInstanceOf(info, asListFromStream().stream, type);
      return myself;
    }
    return super.isInstanceOf(type);
  }

  @Override
  public ListAssert<ELEMENT> isInstanceOfAny(Class<?>... types) {
    if (actual instanceof ListFromStream) {
      objects.assertIsInstanceOfAny(info, asListFromStream().stream, types);
      return myself;
    }
    return super.isInstanceOfAny(types);
  }

  @Override
  public ListAssert<ELEMENT> isOfAnyClassIn(Class<?>... types) {
    if (actual instanceof ListFromStream) {
      objects.assertIsOfAnyClassIn(info, asListFromStream().stream, types);
      return myself;
    }
    return super.isOfAnyClassIn(types);
  }

  @Override
  public ListAssert<ELEMENT> isExactlyInstanceOf(Class<?> type) {
    if (actual instanceof ListFromStream) {
      objects.assertIsExactlyInstanceOf(info, asListFromStream().stream, type);
      return myself;
    }
    return super.isExactlyInstanceOf(type);
  }

  @Override
  public ListAssert<ELEMENT> isNotInstanceOf(Class<?> type) {
    if (actual instanceof ListFromStream) {
      objects.assertIsNotInstanceOf(info, asListFromStream().stream, type);
      return myself;
    }
    return super.isNotInstanceOf(type);
  }

  @Override
  public ListAssert<ELEMENT> isNotInstanceOfAny(Class<?>... types) {
    if (actual instanceof ListFromStream) {
      objects.assertIsNotInstanceOfAny(info, asListFromStream().stream, types);
      return myself;
    }
    return super.isNotInstanceOfAny(types);
  }

  @Override
  public ListAssert<ELEMENT> isNotOfAnyClassIn(Class<?>... types) {
    if (actual instanceof ListFromStream) {
      objects.assertIsNotOfAnyClassIn(info, asListFromStream().stream, types);
      return myself;
    }
    return super.isNotOfAnyClassIn(types);
  }

  @Override
  public ListAssert<ELEMENT> isNotExactlyInstanceOf(Class<?> type) {
    if (actual instanceof ListFromStream) {
      objects.assertIsNotExactlyInstanceOf(info, asListFromStream().stream, type);
      return myself;
    }
    return super.isNotExactlyInstanceOf(type);
  }

  @Override
  public ListAssert<ELEMENT> isSameAs(Object expected) {
    if (actual instanceof ListFromStream) {
      objects.assertSame(info, asListFromStream().stream, expected);
      return myself;
    }
    return super.isSameAs(expected);
  }

  @Override
  public ListAssert<ELEMENT> isNotSameAs(Object expected) {
    if (actual instanceof ListFromStream) {
      objects.assertNotSame(info, asListFromStream().stream, expected);
      return myself;
    }
    return super.isNotSameAs(expected);
  }

  @Override
  @SafeVarargs
  public final ListAssert<ELEMENT> startsWith(ELEMENT... sequence) {
    if (!(actual instanceof ListFromStream)) {
      return super.startsWith(sequence);
    }
    objects.assertNotNull(info, actual);
    checkIsNotNull(sequence);
    // NO SUPPORT FOR infinite streams as it prevents chaining other assertions afterward, it requires to consume the
    // Stream partially, if you chain another assertion, the stream is already consumed.
    @SuppressWarnings("unchecked")
    Iterator<? extends ELEMENT> iterator = asListFromStream().stream().iterator();
    if (sequence.length == 0 && iterator.hasNext()) throw new AssertionError("actual is not empty");
    int i = 0;
    while (iterator.hasNext()) {
      if (i >= sequence.length) break;
      if (iterables.getComparisonStrategy().areEqual(iterator.next(), sequence[i++])) continue;
      throw actualDoesNotStartWithSequence(info, sequence);
    }
    if (sequence.length > i) {
      // sequence has more elements than actual
      throw actualDoesNotStartWithSequence(info, sequence);
    }
    return myself;
  }

  private AssertionError actualDoesNotStartWithSequence(AssertionInfo info, Object[] sequence) {
    return Failures.instance()
                   .failure(info, shouldStartWith("Stream under test", sequence, iterables.getComparisonStrategy()));
  }

  @SuppressWarnings("rawtypes")
  private ListFromStream asListFromStream() {
    return (ListFromStream) actual;
  }

  @VisibleForTesting
  static class ListFromStream<ELEMENT, STREAM extends BaseStream<ELEMENT, STREAM>> extends AbstractList<ELEMENT> {
    private BaseStream<ELEMENT, STREAM> stream;
    private List<ELEMENT> list;

    public ListFromStream(BaseStream<ELEMENT, STREAM> stream) {
      this.stream = stream;
    }

    @Override
    public Stream<ELEMENT> stream() {
      initList();
      return list.stream();
    }

    private List<ELEMENT> initList() {
      if (list == null) list = Lists.newArrayList(stream.iterator());
      return list;
    }

    @Override
    public int size() {
      initList();
      return list.size();
    }

    @Override
    public ELEMENT get(int index) {
      initList();
      return list.get(index);
    }

  }

  @Override
  @SafeVarargs
  public final ListAssert<ELEMENT> contains(ELEMENT... values) {
    return super.contains(values);
  }

  @Override
  @SafeVarargs
  public final ListAssert<ELEMENT> containsOnly(ELEMENT... values) {
    return super.containsOnly(values);
  }

  @Override
  @SafeVarargs
  public final ListAssert<ELEMENT> containsOnlyOnce(ELEMENT... values) {
    return super.containsOnlyOnce(values);
  }

  @Override
  @SafeVarargs
  public final ListAssert<ELEMENT> containsExactly(ELEMENT... values) {
    return super.containsExactly(values);
  }

  @Override
  @SafeVarargs
  public final ListAssert<ELEMENT> containsExactlyInAnyOrder(ELEMENT... values) {
    return super.containsExactlyInAnyOrder(values);
  }

  @Override
  @SafeVarargs
  public final ListAssert<ELEMENT> isSubsetOf(ELEMENT... values) {
    return super.isSubsetOf(values);
  }

  @Override
  @SafeVarargs
  public final ListAssert<ELEMENT> containsSequence(ELEMENT... sequence) {
    return super.containsSequence(sequence);
  }

  @Override
  @SafeVarargs
  public final ListAssert<ELEMENT> doesNotContainSequence(ELEMENT... sequence) {
    return super.doesNotContainSequence(sequence);
  }

  @Override
  @SafeVarargs
  public final ListAssert<ELEMENT> containsSubsequence(ELEMENT... sequence) {
    return super.containsSubsequence(sequence);
  }

  @Override
  @SafeVarargs
  public final ListAssert<ELEMENT> doesNotContainSubsequence(ELEMENT... sequence) {
    return super.doesNotContainSubsequence(sequence);
  }

  @Override
  @SafeVarargs
  public final ListAssert<ELEMENT> doesNotContain(ELEMENT... values) {
    return super.doesNotContain(values);
  }

  @Override
  @SafeVarargs
  public final ListAssert<ELEMENT> endsWith(ELEMENT first, ELEMENT... rest) {
    return super.endsWith(first, rest);
  }

}
