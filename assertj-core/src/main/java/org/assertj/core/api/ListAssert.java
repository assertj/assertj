/*
 * Copyright 2012-2026 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.assertj.core.api;

import static org.assertj.core.error.ShouldStartWith.shouldStartWith;
import static org.assertj.core.internal.CommonValidations.checkIsNotNull;
import static org.assertj.core.util.Lists.newArrayList;

import java.util.AbstractList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.BaseStream;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import java.util.stream.Stream;

import org.assertj.core.internal.Failures;
import org.jspecify.annotations.Nullable;

/**
 * Assertion methods for {@link List}s.
 * <p>
 * To create an instance of this class, invoke <code>{@link Assertions#assertThat(List)}</code>.
 *
 * @param <ELEMENT> the type of elements of the "actual" value.
 * @author Yvonne Wang
 * @author Alex Ruiz
 * @author Joel Costigliola
 * @author Mikhail Mazursky
 */
public class ListAssert<ELEMENT> extends
    FactoryBasedNavigableListAssert<ListAssert<ELEMENT>, List<? extends ELEMENT>, ELEMENT, ObjectAssert<ELEMENT>> {

  /**
   * Creates a new list assertion.
   *
   * @param <ELEMENT> the element type
   * @param actual the actual list to verify
   * @return the created assertion
   */
  public static <ELEMENT> ListAssert<ELEMENT> assertThatList(List<? extends ELEMENT> actual) {
    return new ListAssert<>(actual);
  }

  /**
   * Creates a list assertion from a stream.
   *
   * @param <ELEMENT> the element type
   * @param actual the actual stream to verify
   * @return the created assertion
   */
  public static <ELEMENT> ListAssert<ELEMENT> assertThatStream(@Nullable Stream<? extends ELEMENT> actual) {
    return new ListAssert<>(actual);
  }

  /**
   * Creates a list assertion from a double stream.
   *
   * @param actual the actual stream to verify
   * @return the created assertion
   */
  public static ListAssert<Double> assertThatDoubleStream(@Nullable DoubleStream actual) {
    return new ListAssert<>(actual);
  }

  /**
   * Creates a list assertion from a long stream.
   *
   * @param actual the actual stream to verify
   * @return the created assertion
   */
  public static ListAssert<Long> assertThatLongStream(@Nullable LongStream actual) {
    return new ListAssert<>(actual);
  }

  /**
   * Creates a list assertion from an int stream.
   *
   * @param actual the actual stream to verify
   * @return the created assertion
   */
  public static ListAssert<Integer> assertThatIntStream(@Nullable IntStream actual) {
    return new ListAssert<>(actual);
  }

  /**
   * Creates a list assertion whose actual value is {@code null}.
   *
   * @param <ELEMENT> the element type
   * @return a null list assertion
   */
  // used only for null-object navigation: the underlying AbstractAssert.actual is documented to tolerate null
  // even though ListAssert's own ACTUAL type argument isn't modeled as nullable.
  @SuppressWarnings("NullAway")
  public static <ELEMENT> ListAssert<ELEMENT> nullListAssert() {
    return new ListAssert<>((List<? extends ELEMENT>) null);
  }

  /**
   * Creates a new list assertion.
   *
   * @param actual the actual list to verify
   */
  public ListAssert(List<? extends ELEMENT> actual) {
    super(actual, ListAssert.class, ObjectAssert::new);
  }

  /**
   * Creates a list assertion from a stream.
   *
   * @param actual the actual stream to verify
   */
  // the ListFromStream branch is always non-null; the null branch only exists as defensive handling for a null
  // stream and is not reflected in ListAssert's own (non-nullable) ACTUAL type argument.
  @SuppressWarnings("NullAway")
  public ListAssert(@Nullable Stream<? extends ELEMENT> actual) {
    this(actual == null ? null : new ListFromStream<>(actual));
  }

  /**
   * Creates a list assertion from an int stream.
   *
   * @param actual the actual stream to verify
   */
  @SuppressWarnings({ "unchecked", "rawtypes", "NullAway" })
  public ListAssert(@Nullable IntStream actual) {
    this(actual == null ? null : new ListFromStream(actual));
  }

  /**
   * Creates a list assertion from a long stream.
   *
   * @param actual the actual stream to verify
   */
  @SuppressWarnings({ "unchecked", "rawtypes", "NullAway" })
  public ListAssert(@Nullable LongStream actual) {
    this(actual == null ? null : new ListFromStream(actual));
  }

  /**
   * Creates a list assertion from a double stream.
   *
   * @param actual the actual stream to verify
   */
  @SuppressWarnings({ "unchecked", "rawtypes", "NullAway" })
  public ListAssert(@Nullable DoubleStream actual) {
    this(actual == null ? null : new ListFromStream(actual));
  }

  @Override
  protected ListAssert<ELEMENT> newAbstractIterableAssert(Iterable<? extends ELEMENT> iterable) {
    return new ListAssert<>(newArrayList(iterable));
  }

  @Override
  public ListAssert<ELEMENT> isEqualTo(@Nullable Object expected) {
    return executeAssertion(() -> {
      if (actual instanceof ListFromStream && asListFromStream().stream == expected) {
        return;
      }
      super.isEqualTo(expected);
    });
  }

  @Override
  public ListAssert<ELEMENT> isInstanceOf(Class<?> type) {
    return executeAssertion(() -> {
      if (actual instanceof ListFromStream) {
        objects.assertIsInstanceOf(info, asListFromStream().stream, type);
      } else {
        super.isInstanceOf(type);
      }
    });
  }

  @Override
  public ListAssert<ELEMENT> isInstanceOfAny(Class<?>... types) {
    return executeAssertion(() -> {
      if (actual instanceof ListFromStream) {
        objects.assertIsInstanceOfAny(info, asListFromStream().stream, types);
      } else {
        super.isInstanceOfAny(types);
      }
    });
  }

  @Override
  public ListAssert<ELEMENT> isOfAnyClassIn(Class<?>... types) {
    return executeAssertion(() -> {
      if (actual instanceof ListFromStream) {
        objects.assertIsOfAnyClassIn(info, asListFromStream().stream, types);
      } else {
        super.isOfAnyClassIn(types);
      }
    });
  }

  @Override
  public ListAssert<ELEMENT> isExactlyInstanceOf(Class<?> type) {
    return executeAssertion(() -> {
      if (actual instanceof ListFromStream) {
        objects.assertIsExactlyInstanceOf(info, asListFromStream().stream, type);
      } else {
        super.isExactlyInstanceOf(type);
      }
    });
  }

  @Override
  public ListAssert<ELEMENT> isNotInstanceOf(Class<?> type) {
    return executeAssertion(() -> {
      if (actual instanceof ListFromStream) {
        objects.assertIsNotInstanceOf(info, asListFromStream().stream, type);
      } else {
        super.isNotInstanceOf(type);
      }
    });
  }

  @Override
  public ListAssert<ELEMENT> isNotInstanceOfAny(Class<?>... types) {
    return executeAssertion(() -> {
      if (actual instanceof ListFromStream) {
        objects.assertIsNotInstanceOfAny(info, asListFromStream().stream, types);
      } else {
        super.isNotInstanceOfAny(types);
      }
    });
  }

  @Override
  public ListAssert<ELEMENT> isNotOfAnyClassIn(Class<?>... types) {
    return executeAssertion(() -> {
      if (actual instanceof ListFromStream) {
        objects.assertIsNotOfAnyClassIn(info, asListFromStream().stream, types);
      } else {
        super.isNotOfAnyClassIn(types);
      }
    });
  }

  @Override
  public ListAssert<ELEMENT> isNotExactlyInstanceOf(Class<?> type) {
    return executeAssertion(() -> {
      if (actual instanceof ListFromStream) {
        objects.assertIsNotExactlyInstanceOf(info, asListFromStream().stream, type);
      } else {
        super.isNotExactlyInstanceOf(type);
      }
    });
  }

  @Override
  public ListAssert<ELEMENT> isSameAs(@Nullable Object expected) {
    return executeAssertion(() -> {
      if (actual instanceof ListFromStream) {
        objects.assertSame(info, asListFromStream().stream, expected);
      } else {
        super.isSameAs(expected);
      }
    });
  }

  @Override
  public ListAssert<ELEMENT> isNotSameAs(@Nullable Object expected) {
    return executeAssertion(() -> {
      if (actual instanceof ListFromStream) {
        objects.assertNotSame(info, asListFromStream().stream, expected);
      } else {
        super.isNotSameAs(expected);
      }
    });
  }

  protected void assertStartsWith(ELEMENT[] sequence) {
    if (!(actual instanceof ListFromStream)) {
      iterables.assertStartsWith(info, actual, sequence);
      return;
    }
    objects.assertNotNull(info, actual);
    checkIsNotNull(sequence);
    Iterator<? extends ELEMENT> iterator = asListFromStream().stream().iterator();
    if (sequence.length == 0 && iterator.hasNext()) throw new AssertionError("actual is not empty");
    int i = 0;
    while (iterator.hasNext()) {
      if (i >= sequence.length) break;
      if (iterables.getComparisonStrategy().areEqual(iterator.next(), sequence[i++])) continue;
      throw actualDoesNotStartWithSequence(info, sequence);
    }
    if (sequence.length > i) {
      throw actualDoesNotStartWithSequence(info, sequence);
    }
  }

  private AssertionError actualDoesNotStartWithSequence(AssertionInfo info, Object[] sequence) {
    return Failures.instance()
                   .failure(info, shouldStartWith("Stream under test", sequence, iterables.getComparisonStrategy()));
  }

  @SuppressWarnings("unchecked")
  private ListFromStream<ELEMENT, Stream<ELEMENT>> asListFromStream() {
    return (ListFromStream<ELEMENT, Stream<ELEMENT>>) actual;
  }

  // TODO reduce the visibility of the fields annotated with @VisibleForTesting
  static class ListFromStream<ELEMENT, STREAM extends BaseStream<ELEMENT, STREAM>> extends AbstractList<ELEMENT> {
    private final BaseStream<ELEMENT, STREAM> stream;
    private @Nullable List<ELEMENT> list;

    public ListFromStream(BaseStream<ELEMENT, STREAM> stream) {
      this.stream = stream;
    }

    @Override
    public Stream<ELEMENT> stream() {
      return initList().stream();
    }

    private List<ELEMENT> initList() {
      if (list == null) {
        list = newArrayList(stream.iterator());
        stream.close();
      }
      return list;
    }

    @Override
    public int size() {
      return initList().size();
    }

    @Override
    public ELEMENT get(int index) {
      return initList().get(index);
    }

  }

}
