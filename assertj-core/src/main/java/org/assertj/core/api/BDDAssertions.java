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
 * Copyright 2012-2024 the original author or authors.
 */
package org.assertj.core.api;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URI;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.text.DateFormat;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.OffsetTime;
import java.time.Period;
import java.time.YearMonth;
import java.time.ZonedDateTime;
import java.time.temporal.Temporal;
import java.time.temporal.TemporalUnit;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.OptionalDouble;
import java.util.OptionalInt;
import java.util.OptionalLong;
import java.util.Spliterator;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicIntegerArray;
import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicLongArray;
import java.util.concurrent.atomic.AtomicLongFieldUpdater;
import java.util.concurrent.atomic.AtomicMarkableReference;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.atomic.AtomicReferenceArray;
import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;
import java.util.concurrent.atomic.AtomicStampedReference;
import java.util.concurrent.atomic.LongAdder;
import java.util.function.Consumer;
import java.util.function.DoublePredicate;
import java.util.function.Function;
import java.util.function.IntPredicate;
import java.util.function.LongPredicate;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import java.util.stream.Stream;

import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.assertj.core.api.filter.FilterOperator;
import org.assertj.core.api.filter.InFilter;
import org.assertj.core.api.filter.NotFilter;
import org.assertj.core.api.filter.NotInFilter;
import org.assertj.core.condition.AllOf;
import org.assertj.core.condition.AnyOf;
import org.assertj.core.condition.DoesNotHave;
import org.assertj.core.condition.Not;
import org.assertj.core.configuration.Configuration;
import org.assertj.core.data.Index;
import org.assertj.core.data.MapEntry;
import org.assertj.core.data.Offset;
import org.assertj.core.data.Percentage;
import org.assertj.core.data.TemporalUnitOffset;
import org.assertj.core.description.Description;
import org.assertj.core.groups.Properties;
import org.assertj.core.groups.Tuple;
import org.assertj.core.presentation.BinaryRepresentation;
import org.assertj.core.presentation.HexadecimalRepresentation;
import org.assertj.core.presentation.Representation;
import org.assertj.core.presentation.StandardRepresentation;
import org.assertj.core.presentation.UnicodeRepresentation;
import org.assertj.core.util.CanIgnoreReturnValue;
import org.assertj.core.util.CheckReturnValue;

/**
 * Behavior-driven development style entry point for assertion methods for different types. Each method in this class is a static factory
 * for a type-specific assertion object.
 * <p>
 * The difference with the {@link Assertions} class is that entry point methods are named <code>then</code> instead of
 * <code>assertThat</code>.
 * <p>
 * For example:
 * <pre><code class='java'> {@literal @}Test
 * public void bdd_assertions_example() {
 *   //given
 *   List&lt;BasketBallPlayer&gt; bulls = new ArrayList&lt;&gt;();
 *
 *   //when
 *   bulls.add(rose);
 *   bulls.add(noah);
 *
 *   then(bulls).contains(rose, noah).doesNotContain(james);
 * }</code></pre>
 * Use <code>{@link #and}</code> to avoid clash with other libraries (like BDDMockito) exposing '<code>then(object)</code>'.
 * You might have to ignore a warning like: <i>The static method BDDAssertions.then() should be accessed in a static way</i>.
 *
 *
 * @author Gonzalo Müller
 * @author Alex Ruiz
 * @author Yvonne Wang
 * @author David DIDIER
 * @author Ted Young
 * @author Joel Costigliola
 * @author Matthieu Baechler
 * @author Mikhail Mazursky
 * @author Nicolas François
 * @author Julien Meddah
 * @author William Delanoue
 * @author Mariusz Smykula
 */
@CheckReturnValue
public class BDDAssertions extends Assertions {

  /**
   * Creates a new <code>{@link org.assertj.core.api.BDDAssertions}</code>.
   */
  protected BDDAssertions() {}

  /**
   * A <code>BDDAssertions</code> which allows to blend assertions with other libraries when the name '<code>then</code>' cause clash.
   * <p>
   * Examples:
   * <pre><code class='java'> import static org.assertj.core.api.BDDAssertions.and;
   * import static org.mockito.BDDMockito.then;
   * import static org.mockito.Mockito.mock;
   * import static org.mockito.Mockito.times;
   *
   * // suppress and.then warning: The static method BDDAssertions.then() should be accessed in a static way
   * {@literal @SuppressWarnings}("static-access")
   * {@literal @Test}
   * public void bdd_assertions_with_bdd_mockito() {
   *   // GIVEN
   *   Person person = mock(Person.class)
   *   // WHEN
   *   person.ride(bike);
   *   person.ride(bike);
   *   // THEN
   *   // mockito then()
   *   then(person).should(times(2)).ride(bike);
   *   // use AssertJ and.then(person) as then(person) would clash with mockito then(person)
   *   and.then(person.hasBike()).isTrue();
   * }</code></pre>
   * <p>
   * Can also be used to add extra readability:
   * <pre><code class='java'> import static org.assertj.core.api.BDDAssertions.and;
   * import static org.assertj.core.api.BDDAssertions.then;
   *
   * // suppress and.then warning: The static method BDDAssertions.then() should be accessed in a static way
   * {@literal @SuppressWarnings}("static-access")
   * {@literal @Test}
   * public void bdd_assertions_with_and_field() {
   *   // ...
   *   then(person.hasBike()).isTrue()
   *   and.then(bike.isNew()).isFalse();
   * }</code></pre>
   *
   * @since 3.14.0
   */
  public static final BDDAssertions and = new BDDAssertions();

  /**
   * Create assertion for {@link Predicate}.
   *
   * @param actual the actual value.
   * @param <T> the type of the value contained in the {@link Predicate}.
   * @return the created assertion object.
   *
   * @since 3.5.0
   */
  public static <T> PredicateAssert<T> then(Predicate<T> actual) {
    return assertThat(actual);
  }

  /**
   * Create assertion for {@link Predicate}.
   * <p>
   * Use this over {@link #then(Predicate)} in case of ambiguous method resolution when the object under test 
   * implements several interfaces Assertj provides <code>then</code> for. 
   *
   * @param actual the actual value.
   * @param <T> the type of the value contained in the {@link Predicate}.
   * @return the created assertion object.
   * @since 3.23.0
   */
  public static <T> PredicateAssert<T> thenPredicate(Predicate<T> actual) {
    return then(actual);
  }

  /**
   * Create assertion for {@link IntPredicate}.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   *
   * @since 3.5.0
   */
  public static IntPredicateAssert then(IntPredicate actual) {
    return assertThat(actual);
  }

  /**
   * Create assertion for {@link LongPredicate}.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   *
   * @since 3.5.0
   */
  public static LongPredicateAssert then(LongPredicate actual) {
    return assertThat(actual);
  }

  /**
   * Create assertion for {@link DoublePredicate}.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   *
   * @since 3.5.0
   */
  public static DoublePredicateAssert then(DoublePredicate actual) {
    return assertThat(actual);
  }

  /**
   * Create assertion for {@link java.util.Optional}.
   *
   * @param <VALUE> the type of the value contained in the {@link java.util.Optional}.
   * @param optional the actual value.
   *
   * @return the created assertion object.
   */
  public static <VALUE> OptionalAssert<VALUE> then(Optional<VALUE> optional) {
    return assertThat(optional);
  }

  /**
   * Create assertion for {@link java.util.OptionalInt}.
   *
   * @param optional the actual value.
   *
   * @return the created assertion object.
   */
  public static OptionalIntAssert then(OptionalInt optional) {
    return assertThat(optional);
  }

  /**
   * Create assertion for {@link java.util.OptionalLong}.
   *
   * @param optional the actual value.
   *
   * @return the created assertion object.
   */
  public static OptionalLongAssert then(OptionalLong optional) {
    return assertThat(optional);
  }

  /**
   * Create assertion for {@link java.util.regex.Matcher}
   *
   * @param actual the actual matcher
   *
   * @return the created assertion object.
   */
  public static MatcherAssert then(Matcher actual) {
    return assertThat(actual);
  }

  /**
   * Create assertion for {@link java.util.OptionalDouble}.
   *
   * @param optional the actual value.
   *
   * @return the created assertion object.
   */
  public static OptionalDoubleAssert then(OptionalDouble optional) {
    return assertThat(optional);
  }

  /**
   * Creates a new instance of <code>{@link org.assertj.core.api.BigDecimalAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   */
  public static AbstractBigDecimalAssert<?> then(BigDecimal actual) {
    return assertThat(actual);
  }

  /**
   * Creates a new instance of <code>{@link org.assertj.core.api.BigIntegerAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   * @since 2.7.0 / 3.7.0
   */
  public static AbstractBigIntegerAssert<?> then(BigInteger actual) {
    return assertThat(actual);
  }

  /**
   * Creates a new instance of <code>{@link org.assertj.core.api.BooleanAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   */
  public static AbstractBooleanAssert<?> then(boolean actual) {
    return assertThat(actual);
  }

  /**
   * Creates a new instance of <code>{@link org.assertj.core.api.BooleanAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   */
  public static AbstractBooleanAssert<?> then(Boolean actual) {
    return assertThat(actual);
  }

  /**
   * Creates a new instance of <code>{@link org.assertj.core.api.BooleanArrayAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   */
  public static AbstractBooleanArrayAssert<?> then(boolean[] actual) {
    return assertThat(actual);
  }

  /**
   * Creates a new instance of <code>{@link org.assertj.core.api.Boolean2DArrayAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   * @since 3.17.0
   */
  public static Boolean2DArrayAssert then(boolean[][] actual) {
    return assertThat(actual);
  }

  /**
   * Creates a new instance of <code>{@link org.assertj.core.api.ByteAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   */
  public static AbstractByteAssert<?> then(byte actual) {
    return assertThat(actual);
  }

  /**
   * Creates a new instance of <code>{@link org.assertj.core.api.ByteAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   */
  public static AbstractByteAssert<?> then(Byte actual) {
    return assertThat(actual);
  }

  /**
   * Creates a new instance of <code>{@link org.assertj.core.api.ByteArrayAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   */
  public static AbstractByteArrayAssert<?> then(byte[] actual) {
    return assertThat(actual);
  }

  /**
   * Creates a new instance of <code>{@link org.assertj.core.api.Byte2DArrayAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   * @since 3.17.0
   */
  public static Byte2DArrayAssert then(byte[][] actual) {
    return assertThat(actual);
  }

  /**
   * Creates a new instance of <code>{@link org.assertj.core.api.CharacterAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   */
  public static AbstractCharacterAssert<?> then(char actual) {
    return assertThat(actual);
  }

  /**
   * Creates a new instance of <code>{@link org.assertj.core.api.CharArrayAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   */
  public static AbstractCharArrayAssert<?> then(char[] actual) {
    return assertThat(actual);
  }

  /**
   * Creates a new instance of <code>{@link org.assertj.core.api.Char2DArrayAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   * @since 3.17.0
   */
  public static Char2DArrayAssert then(char[][] actual) {
    return assertThat(actual);
  }

  /**
   * Creates a new instance of <code>{@link org.assertj.core.api.CharacterAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   */
  public static AbstractCharacterAssert<?> then(Character actual) {
    return assertThat(actual);
  }

  /**
   * Creates a new instance of <code>{@link org.assertj.core.api.ClassAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   */
  public static ClassAssert then(Class<?> actual) {
    return assertThat(actual);
  }

  /**
   * Creates a new instance of <code>{@link org.assertj.core.api.GenericComparableAssert}</code> with
   * standard comparison semantics.
   *
   * @param <T> the actual type
   * @param actual the actual value.
   * @return the created assertion object.
   */
  public static <T extends Comparable<? super T>> AbstractComparableAssert<?, T> then(T actual) {
    return assertThat(actual);
  }

  /**
   * Creates a new instance of <code>{@link UniversalComparableAssert}</code> with
   * standard comparison semantics.
   * <p>
   * Use this over {@link #then(Comparable)} in case of ambiguous method resolution when the object under test 
   * implements several interfaces Assertj provides <code>then</code> for. 
   *
   * @param <T> the type of actual.
   * @param actual the actual value.
   * @return the created assertion object.
   * @since 3.23.0
   */
  public static <T> AbstractUniversalComparableAssert<?, T> thenComparable(Comparable<T> actual) {
    return assertThatComparable(actual);
  }

  /**
   * Creates a new instance of <code>{@link org.assertj.core.api.IterableAssert}</code>.
   *
   * @param <T> the actual elements type
   * @param actual the actual value.
   * @return the created assertion object.
   */
  public static <T> IterableAssert<T> then(Iterable<? extends T> actual) {
    return assertThat(actual);
  }

  /**
   * Creates a new instance of <code>{@link IterableAssert}</code>.
   * <p>
   * Use this over {@link #then(Iterable)} in case of ambiguous method resolution when the object under test 
   * implements several interfaces Assertj provides <code>then</code> for. 
   *
   * @param <ELEMENT> the type of elements.
   * @param actual the actual value.
   * @return the created assertion object.
   * @since 3.23.0
   */
  public static <ELEMENT> IterableAssert<ELEMENT> thenIterable(Iterable<? extends ELEMENT> actual) {
    return then(actual);
  }

  /**
   * Creates a new instance of <code>{@link IteratorAssert}</code>.
   * <p>
   * <b>Breaking change in version 3.12.0:</b> this method does not return anymore an {@link IterableAssert} but an {@link IteratorAssert}.<br>
   * In order to access assertions from {@link IterableAssert}, use {@link IteratorAssert#toIterable()}.
   * <p>
   * {@link IteratorAssert} instances have limited assertions because it does not consume iterator's elements.
   * <p>
   * Examples:
   * <pre><code class='java'> Iterator&lt;String&gt; bestBasketBallPlayers = getBestBasketBallPlayers();
   *
   * then(bestBasketBallPlayers).hasNext() // Iterator assertion
   *                            .toIterable() // switch to Iterable assertions
   *                            .contains("Jordan", "Magic", "Lebron"); // Iterable assertion </code></pre>
   *
   *
   * @param <T> the actual elements type
   * @param actual the actual value.
   * @return the created assertion object.
   */
  public static <T> IteratorAssert<T> then(Iterator<? extends T> actual) {
    return assertThat(actual);
  }

  /**
   * Creates a new instance of <code>{@link IteratorAssert}</code>.
   * <p>
   * Use this over {@link #then(Iterator)} in case of ambiguous method resolution when the object under test 
   * implements several interfaces Assertj provides <code>then</code> for. 
   *
   * @param <ELEMENT> the type of elements.
   * @param actual the actual value.
   * @return the created assertion object.
   * @since 3.23.0
   */
  public static <ELEMENT> IteratorAssert<ELEMENT> thenIterator(Iterator<? extends ELEMENT> actual) {
    return then(actual);
  }

  /**
   * Creates a new instance of <code>{@link FactoryBasedNavigableIterableAssert}</code> allowing to navigate to any {@code Iterable} element
   * in order to perform assertions on it.
   * <p>
   * Navigational methods provided:<ul>
   * <li>{@link AbstractIterableAssert#first() first()}</li>
   * <li>{@link AbstractIterableAssert#last() last()}</li>
   * <li>{@link AbstractIterableAssert#element(int) element(index)}</li>
   * <li>{@link AbstractIterableAssert#elements(int...) elements(int...)}</li>
   * </ul>
   * <p>
   * The available assertions after navigating to an element depend on the {@code ELEMENT_ASSERT} parameter of the given
   * {@link AssertFactory AssertFactory&lt;ELEMENT, ELEMENT_ASSERT&gt;} (AssertJ can't figure it out because of Java type erasure).
   * <p>
   * Example with {@code String} element assertions:
   * <pre><code class='java'> Iterable&lt;String&gt; hobbits = newHashSet("frodo", "sam", "pippin");
   *
   * // build an AssertFactory for StringAssert (much nicer with Java 8 lambdas)
   * AssertFactory&lt;String, StringAssert&gt; stringAssertFactory = new AssertFactory&lt;String, StringAssert&gt;() {
   *   {@literal @}Override
   *   public StringAssert createAssert(String string) {
   *     return new StringAssert(string);
   *   }
   * };
   *
   * // assertion succeeds with String assertions chained after first()
   * then(hobbits, stringAssertFactory).first()
   *                                   .startsWith("fro")
   *                                   .endsWith("do");</code></pre>
   *
   * @param <ACTUAL> The actual type
   * @param <ELEMENT> The actual elements type
   * @param <ELEMENT_ASSERT> The actual elements AbstractAssert type
   * @param actual the actual value.
   * @param assertFactory the factory used to create the elements assert instance.
   * @return the created assertion object.
   * @deprecated
   * This was added to help creating type-specific assertions for the elements of an {@link Iterable} instance.
   * <p>
   * Deprecated way:
   * <pre><code class='java'> Iterable&lt;String&gt; hobbits = Set.of("frodo", "sam", "Pippin");
   * then(hobbits, StringAssert::new).first()
   *                                 .startsWith("fro")
   *                                 .endsWith("do");</code></pre>
   *
   * However, there is a better way with {@link InstanceOfAssertFactory} and the corresponding
   * {@link AbstractIterableAssert#first(InstanceOfAssertFactory) first(InstanceOfAssertFactory)}.
   * <p>
   * New way:
   * <pre><code class='java'> then(hobbits).first(STRING) // static import of InstanceOfAssertFactories.STRING
   *              .startsWith("fro")
   *              .endsWith("do");</code></pre>
   *
   * The main advantage of the latter is easier discoverability and the use of InstanceOfAssertFactory which is the
   * preferred way to create type-specific assertions in AssertJ API.
   */
  @Deprecated
//@format:off
  public static <ACTUAL extends Iterable<? extends ELEMENT>, ELEMENT, ELEMENT_ASSERT extends AbstractAssert<ELEMENT_ASSERT, ELEMENT>>
         FactoryBasedNavigableIterableAssert<?, ACTUAL, ELEMENT, ELEMENT_ASSERT> then(Iterable<? extends ELEMENT> actual,
                                                                                 AssertFactory<ELEMENT, ELEMENT_ASSERT> assertFactory) {
    return assertThat(actual, assertFactory);
  }

  /**
   * Creates a new instance of <code>{@link ClassBasedNavigableIterableAssert}</code> allowing to navigate to any {@code Iterable} element
   * in order to perform assertions on it.
   * <p>
   * Navigational methods provided:<ul>
   * <li>{@link AbstractIterableAssert#first() first()}</li>
   * <li>{@link AbstractIterableAssert#last() last()}</li>
   * <li>{@link AbstractIterableAssert#element(int) element(index)}</li>
   * <li>{@link AbstractIterableAssert#elements(int...) elements(int...)}</li>
   * </ul>
   * <p>
   * The available assertions after navigating to an element depend on the given {@code assertClass}
   * (AssertJ can't find the element assert type by itself because of Java type erasure).
   * <p>
   * Example with {@code String} element assertions:
   * <pre><code class='java'> Iterable&lt;String&gt; hobbits = newHashSet("frodo", "sam", "pippin");
   *
   * // assertion succeeds with String assertions chained after first()
   * then(hobbits, StringAssert.class).first()
   *                                  .startsWith("fro")
   *                                  .endsWith("do");</code></pre>
   *
   * @param <ACTUAL> The actual type
   * @param <ELEMENT> The actual elements type
   * @param <ELEMENT_ASSERT> The actual elements AbstractAssert type
   * @param actual the actual value.
   * @param assertClass the class used to create the elements assert instance.
   * @return the created assertion object.
   * @deprecated
   * This was added to help creating type-specific assertions for the elements of an {@link Iterable} instance.
   * <p>
   * Deprecated way:
   * <pre><code class='java'> Iterable&lt;String&gt; hobbits = Set.of("frodo", "sam", "Pippin");
   * then(hobbits, StringAssert.class).first()
   *                                  .startsWith("fro")
   *                                  .endsWith("do");</code></pre>
   *
   * However, there is a better way with {@link InstanceOfAssertFactory} and the corresponding
   * {@link AbstractIterableAssert#first(InstanceOfAssertFactory) first(InstanceOfAssertFactory)}.
   * <p>
   * New way:
   * <pre><code class='java'> then(hobbits).first(STRING) // static import of InstanceOfAssertFactories.STRING
   *              .startsWith("fro")
   *              .endsWith("do");</code></pre>
   *
   * The main advantage of the latter is easier discoverability and the use of InstanceOfAssertFactory which is the
   * preferred way to create type-specific assertions in AssertJ API.
   */
  @Deprecated
  public static <ACTUAL extends Iterable<? extends ELEMENT>, ELEMENT, ELEMENT_ASSERT extends AbstractAssert<ELEMENT_ASSERT, ELEMENT>>
         ClassBasedNavigableIterableAssert<?, ACTUAL, ELEMENT, ELEMENT_ASSERT> then(ACTUAL actual,
                                                                                    Class<ELEMENT_ASSERT> assertClass) {
    return assertThat(actual, assertClass);
  }

  /**
   * Creates a new instance of <code>{@link FactoryBasedNavigableListAssert}</code> allowing to navigate to any {@code List} element
   * in order to perform assertions on it.
   * <p>
   * Navigational methods provided:<ul>
   * <li>{@link AbstractIterableAssert#first() first()}</li>
   * <li>{@link AbstractIterableAssert#last() last()}</li>
   * <li>{@link AbstractIterableAssert#element(int) element(index)}</li>
   * <li>{@link AbstractIterableAssert#elements(int...) elements(int...)}</li>
   * </ul>
   * <p>
   * The available assertions after navigating to an element depend on the {@code ELEMENT_ASSERT} parameter of the given
   * {@link AssertFactory AssertFactory&lt;ELEMENT, ELEMENT_ASSERT&gt;} (AssertJ can't figure it out because of Java type erasure).
   * <p>
   * Example with {@code String} element assertions:
   * <pre><code class='java'> List&lt;String&gt; hobbits = newArrayList("frodo", "sam", "pippin");
   *
   * // build an AssertFactory for StringAssert (much nicer with Java 8 lambdas)
   * AssertFactory&lt;String, StringAssert&gt; stringAssertFactory = new AssertFactory&lt;String, StringAssert&gt;() {
   *   {@literal @}Override
   *   public StringAssert createAssert(String string) {
   *     return new StringAssert(string);
   *   }
   * };
   *
   * // assertion succeeds with String assertions chained after first()
   * then(hobbits, stringAssertFactory).first()
   *                                   .startsWith("fro")
   *                                   .endsWith("do");</code></pre>
   *
   * @param <ACTUAL> The actual type
   * @param <ELEMENT> The actual elements type
   * @param <ELEMENT_ASSERT> The actual elements AbstractAssert type
   * @param actual the actual value.
   * @param assertFactory the factory used to create the elements assert instance.
   * @return the created assertion object.
   * @deprecated
   * This was added to help creating type-specific assertions for the elements of an {@link List} instance.
   * <p>
   * Deprecated way:
   * <pre><code class='java'> List&lt;String&gt; hobbits = List.of("frodo", "sam", "Pippin");
   * then(hobbits, StringAssert::new).first()
   *                                 .startsWith("fro")
   *                                 .endsWith("do");</code></pre>
   *
   * However, there is a better way with {@link InstanceOfAssertFactory} and the corresponding
   * {@link AbstractIterableAssert#first(InstanceOfAssertFactory) first(InstanceOfAssertFactory)}.
   * <p>
   * New way:
   * <pre><code class='java'> then(hobbits).first(STRING) // static import of InstanceOfAssertFactories.STRING
   *              .startsWith("fro")
   *              .endsWith("do");</code></pre>
   *
   * The main advantage of the latter is easier discoverability and the use of InstanceOfAssertFactory which is the
   * preferred way to create type-specific assertions in AssertJ API.
   */
  @Deprecated
  public static <ACTUAL extends List<? extends ELEMENT>, ELEMENT, ELEMENT_ASSERT extends AbstractAssert<ELEMENT_ASSERT, ELEMENT>>
         FactoryBasedNavigableListAssert<?, ACTUAL, ELEMENT, ELEMENT_ASSERT> then(List<? extends ELEMENT> actual,
                                                                                        AssertFactory<ELEMENT, ELEMENT_ASSERT> assertFactory) {
    return assertThat(actual, assertFactory);
  }

  /**
   * Creates a new instance of <code>{@link ClassBasedNavigableListAssert}</code> allowing to navigate to any {@code List} element
   * in order to perform assertions on it.
   * <p>
   * Navigational methods provided:<ul>
   * <li>{@link AbstractIterableAssert#first() first()}</li>
   * <li>{@link AbstractIterableAssert#last() last()}</li>
   * <li>{@link AbstractIterableAssert#element(int) element(index)}</li>
   * <li>{@link AbstractIterableAssert#elements(int...) elements(int...)}</li>
   * </ul>
   * <p>
   * The available assertions after navigating to an element depend on the given {@code assertClass}
   * (AssertJ can't find the element assert type by itself because of Java type erasure).
   * <p>
   * Example with {@code String} element assertions:
   * <pre><code class='java'> List&lt;String&gt; hobbits = newArrayList("frodo", "sam", "pippin");
   *
   * // assertion succeeds with String assertions chained after first()
   * then(hobbits, StringAssert.class).first()
   *                                  .startsWith("fro")
   *                                  .endsWith("do");</code></pre>
   *
   * @param <ACTUAL> The actual type
   * @param <ELEMENT> The actual elements type
   * @param <ELEMENT_ASSERT> The actual elements AbstractAssert type
   * @param actual the actual value.
   * @param assertClass the class used to create the elements assert instance.
   * @return the created assertion object.
   * @deprecated
   * This was added to help creating type-specific assertions for the elements of an {@link Iterable} instance.
   * <p>
   * Deprecated way:
   * <pre><code class='java'> List&lt;String&gt; hobbits = List.of("frodo", "sam", "Pippin");
   * then(hobbits, StringAssert.class).first()
   *                                  .startsWith("fro")
   *                                  .endsWith("do");</code></pre>
   *
   * However, there is a better way with {@link InstanceOfAssertFactory} and the corresponding
   * {@link AbstractIterableAssert#first(InstanceOfAssertFactory) first(InstanceOfAssertFactory)}.
   * <p>
   * New way:
   * <pre><code class='java'> then(hobbits).first(STRING) // static import of InstanceOfAssertFactories.STRING
   *              .startsWith("fro")
   *              .endsWith("do");</code></pre>
   *
   * The main advantage of the latter is easier discoverability and the use of InstanceOfAssertFactory which is the
   * preferred way to create type-specific assertions in AssertJ API.
   */
  @Deprecated
  public static <ELEMENT, ACTUAL extends List<? extends ELEMENT>, ELEMENT_ASSERT extends AbstractAssert<ELEMENT_ASSERT, ELEMENT>>
         ClassBasedNavigableListAssert<?, ACTUAL, ELEMENT, ELEMENT_ASSERT> then(List<? extends ELEMENT> actual,
                                                                                Class<ELEMENT_ASSERT> assertClass) {
    return assertThat(actual, assertClass);
  }

//@format:on

  /**
   * Creates a new instance of <code>{@link org.assertj.core.api.DoubleAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   */
  public static AbstractDoubleAssert<?> then(double actual) {
    return assertThat(actual);
  }

  /**
   * Creates a new instance of <code>{@link org.assertj.core.api.DoubleAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   */
  public static AbstractDoubleAssert<?> then(Double actual) {
    return assertThat(actual);
  }

  /**
   * Creates a new instance of <code>{@link org.assertj.core.api.DoubleArrayAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   */
  public static AbstractDoubleArrayAssert<?> then(double[] actual) {
    return assertThat(actual);
  }

  /**
   * Creates a new instance of <code>{@link org.assertj.core.api.Double2DArrayAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   * @since 3.17.0
   */
  public static Double2DArrayAssert then(double[][] actual) {
    return assertThat(actual);
  }

  /**
   * Creates a new instance of <code>{@link org.assertj.core.api.FileAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   */
  public static AbstractFileAssert<?> then(File actual) {
    return assertThat(actual);
  }

  /**
   * Creates a new instance of {@link PathAssert}
   *
   * @param actual the path to test
   * @return the created assertion object
   */
  public static AbstractPathAssert<?> then(Path actual) {
    return assertThat(actual);
  }

  /**
   * Creates a new instance of {@link PathAssert}
   * <p>
   * Use this over {@link #then(Path)} in case of ambiguous method resolution when the object under test 
   * implements several interfaces Assertj provides <code>then</code> for. 
   *
   * @param actual the path to test
   * @return the created assertion object
   * @since 3.23.0
   */
  public static AbstractPathAssert<?> thenPath(Path actual) {
    return then(actual);
  }

  /**
   * Creates a new instance of {@link FutureAssert}
   *
   * @param <RESULT> the type of the value contained in the {@link java.util.concurrent.Future}.
   * @param actual the future to test
   * @return the created assertion object
   * @since 2.7.0 / 3.7.0
   */
  public static <RESULT> FutureAssert<RESULT> then(Future<RESULT> actual) {
    return assertThat(actual);
  }

  /**
   * Creates a new instance of <code>{@link org.assertj.core.api.InputStreamAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   */
  public static AbstractInputStreamAssert<?, ? extends InputStream> then(InputStream actual) {
    return assertThat(actual);
  }

  /**
   * Creates a new instance of <code>{@link org.assertj.core.api.FloatAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   */
  public static AbstractFloatAssert<?> then(float actual) {
    return assertThat(actual);
  }

  /**
   * Creates a new instance of <code>{@link org.assertj.core.api.FloatAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   */
  public static AbstractFloatAssert<?> then(Float actual) {
    return assertThat(actual);
  }

  /**
   * Creates a new instance of <code>{@link org.assertj.core.api.FloatArrayAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   */
  public static AbstractFloatArrayAssert<?> then(float[] actual) {
    return assertThat(actual);
  }

  /**
   * Creates a new instance of <code>{@link org.assertj.core.api.Float2DArrayAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   * @since 3.17.0
   */
  public static Float2DArrayAssert then(float[][] actual) {
    return assertThat(actual);
  }

  /**
   * Creates a new instance of <code>{@link org.assertj.core.api.IntegerAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   */
  public static AbstractIntegerAssert<?> then(int actual) {
    return assertThat(actual);
  }

  /**
   * Creates a new instance of <code>{@link org.assertj.core.api.IntArrayAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   */
  public static AbstractIntArrayAssert<?> then(int[] actual) {
    return assertThat(actual);
  }

  /**
   * Creates a new instance of <code>{@link org.assertj.core.api.Int2DArrayAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   * @since 3.17.0
   */
  public static Int2DArrayAssert then(int[][] actual) {
    return assertThat(actual);
  }

  /**
   * Creates a new instance of <code>{@link org.assertj.core.api.IntegerAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   */
  public static AbstractIntegerAssert<?> then(Integer actual) {
    return assertThat(actual);
  }

  /**
   * Creates a new instance of <code>{@link CollectionAssert}</code>.
   *
   * @param <E> the type of elements.
   * @param actual the actual value.
   * @return the created assertion object.
   * @since 3.21.0
   */
  public static <E> AbstractCollectionAssert<?, Collection<? extends E>, E, ObjectAssert<E>> then(Collection<? extends E> actual) {
    return assertThat(actual);
  }

  /**
   * Creates a new instance of <code>{@link CollectionAssert}</code>.
   * <p>
   * Use this over {@link #then(Collection)} in case of ambiguous method resolution when the object under test 
   * implements several interfaces Assertj provides <code>then</code> for. 
   *
   * @param <E> the type of elements.
   * @param actual the actual value.
   * @return the created assertion object.
   * @since 3.23.0
   */
  public static <E> AbstractCollectionAssert<?, Collection<? extends E>, E, ObjectAssert<E>> thenCollection(Collection<? extends E> actual) {
    return then(actual);
  }

  /**
   * Creates a new instance of <code>{@link org.assertj.core.api.ListAssert}</code>.
   *
   * @param <T> the type of elements.
   * @param actual the actual value.
   * @return the created assertion object.
   */
  public static <T> ListAssert<T> then(List<? extends T> actual) {
    return assertThat(actual);
  }

  /**
   * Creates a new instance of <code>{@link ListAssert}</code>.
   * <p>
   * Use this over {@link #then(List)} in case of ambiguous method resolution when the object under test 
   * implements several interfaces Assertj provides <code>then</code> for. 
   *
   * @param <ELEMENT> the type of elements.
   * @param actual the actual value.
   * @return the created assertion object.
   * @since 3.23.0
   */
  public static <ELEMENT> ListAssert<ELEMENT> thenList(List<? extends ELEMENT> actual) {
    return then(actual);
  }

  /**
   * Creates a new instance of <code>{@link org.assertj.core.api.LongAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   */
  public static AbstractLongAssert<?> then(long actual) {
    return assertThat(actual);
  }

  /**
   * Creates a new instance of <code>{@link org.assertj.core.api.LongAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   */
  public static AbstractLongAssert<?> then(Long actual) {
    return assertThat(actual);
  }

  /**
   * Creates a new instance of <code>{@link org.assertj.core.api.LongArrayAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   */
  public static AbstractLongArrayAssert<?> then(long[] actual) {
    return assertThat(actual);
  }

  /**
   * Creates a new instance of <code>{@link org.assertj.core.api.Long2DArrayAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   * @since 3.17.0
   */
  public static Long2DArrayAssert then(long[][] actual) {
    return assertThat(actual);
  }

  /**
   * Creates a new instance of <code>{@link org.assertj.core.api.ObjectAssert}</code>.
   *
   * @param <T> the type of the actual value.
   * @param actual the actual value.
   * @return the created assertion object.
   */
  public static <T> ObjectAssert<T> then(T actual) {
    return assertThat(actual);
  }

  /**
   * Creates a new instance of <code>{@link org.assertj.core.api.ObjectArrayAssert}</code>.
   *
   * @param <T> the actual's elements type.
   * @param actual the actual value.
   * @return the created assertion object.
   */
  public static <T> ObjectArrayAssert<T> then(T[] actual) {
    return assertThat(actual);
  }

  /**
   * Creates a new instance of <code>{@link org.assertj.core.api.Object2DArrayAssert}</code>.
   *
   * @param <T> the actual's elements type.
   * @param actual the actual value.
   * @return the created assertion object.
   * @since 3.17.0
   */
  public static <T> Object2DArrayAssert<T> then(T[][] actual) {
    return assertThat(actual);
  }

  /**
   * Creates a new instance of <code>{@link org.assertj.core.api.MapAssert}</code>.
   *
   * @param <K> the type of keys in the map.
   * @param <V> the type of values in the map.
   * @param actual the actual value.
   * @return the created assertion object.
   */
  public static <K, V> MapAssert<K, V> then(Map<K, V> actual) {
    return assertThat(actual);
  }

  /**
   * Creates a new instance of <code>{@link org.assertj.core.api.ShortAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   */
  public static AbstractShortAssert<?> then(short actual) {
    return assertThat(actual);
  }

  /**
   * Creates a new instance of <code>{@link org.assertj.core.api.ShortAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   */
  public static AbstractShortAssert<?> then(Short actual) {
    return assertThat(actual);
  }

  /**
   * Creates a new instance of <code>{@link org.assertj.core.api.ShortArrayAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   */
  public static AbstractShortArrayAssert<?> then(short[] actual) {
    return assertThat(actual);
  }

  /**
   * Creates a new instance of <code>{@link org.assertj.core.api.Short2DArrayAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   * @since 3.17.0
   */
  public static Short2DArrayAssert then(short[][] actual) {
    return assertThat(actual);
  }

  /**
   * Creates a new instance of <code>{@link org.assertj.core.api.CharSequenceAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   */
  public static AbstractCharSequenceAssert<?, ? extends CharSequence> then(CharSequence actual) {
    return assertThat(actual);
  }

  /**
   * Creates a new instance of <code>{@link org.assertj.core.api.CharSequenceAssert}</code>.
   * <p>
   * Use this over {@link #then(CharSequence)} in case of ambiguous method resolution when the object under test
   * implements several interfaces Assertj provides <code>then</code> for.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   * @since 3.25.0
   */
  public static AbstractCharSequenceAssert<?, ? extends CharSequence> thenCharSequence(CharSequence actual) {
    return then(actual);
  }

  /**
   * Creates a new instance of <code>{@link org.assertj.core.api.CharSequenceAssert}</code> from a {@link StringBuilder}.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   * @since 3.11.0
   */
  public static AbstractCharSequenceAssert<?, ? extends CharSequence> then(StringBuilder actual) {
    return assertThat(actual);
  }

  /**
   * Creates a new instance of <code>{@link org.assertj.core.api.CharSequenceAssert}</code> from a {@link StringBuffer}.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   * @since 3.11.0
   */
  public static AbstractCharSequenceAssert<?, ? extends CharSequence> then(StringBuffer actual) {
    return assertThat(actual);
  }

  /**
   * Creates a new instance of <code>{@link org.assertj.core.api.StringAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   */
  public static AbstractStringAssert<?> then(String actual) {
    return assertThat(actual);
  }

  /**
   * Creates a new instance of <code>{@link org.assertj.core.api.DateAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   */
  public static AbstractDateAssert<?> then(Date actual) {
    return assertThat(actual);
  }

  /**
   * Create assertion for {@link AtomicBoolean}.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   * @since 2.7.0 / 3.7.0
   */
  public static AtomicBooleanAssert then(AtomicBoolean actual) {
    return assertThat(actual);
  }

  /**
   * Create assertion for {@link AtomicInteger}.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   * @since 2.7.0 / 3.7.0
   */
  public static AtomicIntegerAssert then(AtomicInteger actual) {
    return assertThat(actual);
  }

  /**
   * Create assertion for {@link AtomicIntegerArray}.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   * @since 2.7.0 / 3.7.0
   */
  public static AtomicIntegerArrayAssert then(AtomicIntegerArray actual) {
    return assertThat(actual);
  }

  /**
   * Create assertion for {@link AtomicIntegerFieldUpdater}.
   *
   * @param actual the actual value.
   * @param <OBJECT> the type of the object holding the updatable field.
   * @return the created assertion object.
   * @since 2.7.0 / 3.7.0
   */
  public static <OBJECT> AtomicIntegerFieldUpdaterAssert<OBJECT> then(AtomicIntegerFieldUpdater<OBJECT> actual) {
    return assertThat(actual);
  }

  /**
   * Create assertion for {@link LongAdder}.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   * @since 3.16.0
   */
  public static LongAdderAssert then(LongAdder actual) {
    return assertThat(actual);
  }

  /**
   * Create assertion for {@link AtomicLong}.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   * @since 2.7.0 / 3.7.0
   */
  public static AtomicLongAssert then(AtomicLong actual) {
    return assertThat(actual);
  }

  /**
   * Create assertion for {@link AtomicLongArray}.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   * @since 2.7.0 / 3.7.0
   */
  public static AtomicLongArrayAssert then(AtomicLongArray actual) {
    return assertThat(actual);
  }

  /**
   * Create assertion for {@link AtomicLongFieldUpdater}.
   *
   * @param actual the actual value.
   * @param <OBJECT> the type of the object holding the updatable field.
   * @return the created assertion object.
   * @since 2.7.0 / 3.7.0
   */
  public static <OBJECT> AtomicLongFieldUpdaterAssert<OBJECT> then(AtomicLongFieldUpdater<OBJECT> actual) {
    return assertThat(actual);
  }

  /**
   * Create assertion for {@link AtomicReference}.
   *
   * @param actual the actual value.
   * @param <VALUE> the type of the value contained in the {@link AtomicReference}.
   * @return the created assertion object.
   * @since 2.7.0 / 3.7.0
   */
  public static <VALUE> AtomicReferenceAssert<VALUE> then(AtomicReference<VALUE> actual) {
    return assertThat(actual);
  }

  /**
   * Create assertion for {@link AtomicReferenceArray}.
   *
   * @param actual the actual value.
   * @param <ELEMENT> the type of the value contained in the {@link AtomicReferenceArray}.
   * @return the created assertion object.
   * @since 2.7.0 / 3.7.0
   */
  public static <ELEMENT> AtomicReferenceArrayAssert<ELEMENT> then(AtomicReferenceArray<ELEMENT> actual) {
    return assertThat(actual);
  }

  /**
   * Create assertion for {@link AtomicReferenceFieldUpdater}.
   *
   * @param actual the actual value.
   * @param <FIELD> the type of the field which gets updated by the {@link AtomicReferenceFieldUpdater}.
   * @param <OBJECT> the type of the object holding the updatable field.
   * @return the created assertion object.
   * @since 2.7.0 / 3.7.0
   */
  public static <FIELD, OBJECT> AtomicReferenceFieldUpdaterAssert<FIELD, OBJECT> then(AtomicReferenceFieldUpdater<OBJECT, FIELD> actual) {
    return assertThat(actual);
  }

  /**
   * Create assertion for {@link AtomicMarkableReference}.
   *
   * @param actual the actual value.
   * @param <VALUE> the type of the value contained in the {@link AtomicMarkableReference}.
   * @return the created assertion object.
   * @since 2.7.0 / 3.7.0
   */
  public static <VALUE> AtomicMarkableReferenceAssert<VALUE> then(AtomicMarkableReference<VALUE> actual) {
    return assertThat(actual);
  }

  /**
   * Create assertion for {@link AtomicStampedReference}.
   *
   * @param actual the actual value.
   * @param <VALUE> the type of the value contained in the {@link AtomicStampedReference}.
   * @return the created assertion object.
   * @since 2.7.0 / 3.7.0
   */
  public static <VALUE> AtomicStampedReferenceAssert<VALUE> then(AtomicStampedReference<VALUE> actual) {
    return assertThat(actual);
  }

  /**
   * Creates a new instance of <code>{@link org.assertj.core.api.ThrowableAssert}</code>.
   *
   * @param <T> the type of the actual throwable.
   * @param actual the actual value.
   * @return the created assertion Throwable.
   */
  public static <T extends Throwable> AbstractThrowableAssert<?, T> then(T actual) {
    return assertThat(actual);
  }

  /**
   * Allows to capture and then assert on a {@link Throwable} (easier done with lambdas).
   * <p>
   * Example :
   * <pre><code class='java'> {@literal @}Test
   *  public void testException() {
   *    thenThrownBy(() -&gt; { throw new Exception("boom!") }).isInstanceOf(Exception.class)
   *                                                        .hasMessageContaining("boom");
   * }</code></pre>
   *
   * If the provided {@link ThrowingCallable} does not raise an exception, an error is immediately thrown,
   * in that case the test description provided with {@link AbstractAssert#as(String, Object...) as(String, Object...)} is not honored.<br>
   * To use a test description, use {@link #catchThrowable(ThrowingCallable)} as shown below:
   * <pre><code class='java'> // assertion will fail but "display me" won't appear in the error
   * thenThrownBy(() -&gt; {}).as("display me")
   *                       .isInstanceOf(Exception.class);
   *
   * // assertion will fail AND "display me" will appear in the error
   * Throwable thrown = catchThrowable(() -&gt; {});
   * assertThat(thrown).as("display me")
   *                   .isInstanceOf(Exception.class);</code></pre>
   *
   * Alternatively you can also use <code>assertThatCode(ThrowingCallable)</code> for the test description provided
   * with {@link AbstractAssert#as(String, Object...) as(String, Object...)} to always be honored.
   *
   * @param shouldRaiseThrowable The {@link ThrowingCallable} or lambda with the code that should raise the throwable.
   * @return the created {@link ThrowableAssert}.
   */
  @CanIgnoreReturnValue
  public static AbstractThrowableAssert<?, ? extends Throwable> thenThrownBy(ThrowingCallable shouldRaiseThrowable) {
    return assertThat(catchThrowable(shouldRaiseThrowable)).hasBeenThrown();
  }

  /**
   * Allows to capture and then assert on a {@link Throwable} like {@code thenThrownBy(ThrowingCallable)} but this method
   * let you set the assertion description the same way you do with {@link AbstractAssert#as(String, Object...) as(String, Object...)}.
   * <p>
   * Example:
   * <pre><code class='java'> {@literal @}Test
   *  public void testException() {
   *    // if this assertion failed (but it doesn't), the error message would start with [Test explosive code]
   *    thenThrownBy(() -&gt; { throw new IOException("boom!") }, "Test explosive code")
   *             .isInstanceOf(IOException.class)
   *             .hasMessageContaining("boom");
   * }</code></pre>
   *
   * If the provided {@link ThrowingCallable ThrowingCallable} does not raise an exception, an error is immediately thrown.
   * <p>
   * The test description provided is honored but not the one with {@link AbstractAssert#as(String, Object...) as(String, Object...)}, example:
   * <pre><code class='java'> // assertion will fail but "display me" won't appear in the error message
   * thenThrownBy(() -&gt; {}).as("display me")
   *                       .isInstanceOf(Exception.class);
   *
   * // assertion will fail AND "display me" will appear in the error message
   * thenThrownBy(() -&gt; {}, "display me").isInstanceOf(Exception.class);</code></pre>
   *
   * @param shouldRaiseThrowable The {@link ThrowingCallable} or lambda with the code that should raise the throwable.
   * @param description the new description to set.
   * @param args optional parameter if description is a format String.
   *
   * @return the created {@link ThrowableAssert}.
   *
   * @since 3.9.0
   */
  @CanIgnoreReturnValue
  public static AbstractThrowableAssert<?, ? extends Throwable> thenThrownBy(ThrowingCallable shouldRaiseThrowable,
                                                                             String description, Object... args) {
    return assertThat(catchThrowable(shouldRaiseThrowable)).as(description, args).hasBeenThrown();
  }

  /**
   * Allows to capture and then assert on a {@link Throwable} more easily when used with Java 8 lambdas.
   *
   * <p>
   * Example :
   * </p>
   *
   * <pre><code class='java'> ThrowingCallable callable = () -&gt; {
   *   throw new Exception("boom!");
   * };
   *
   * // assertion succeeds
   * thenCode(callable).isInstanceOf(Exception.class)
   *                   .hasMessageContaining("boom");
   *
   * // assertion fails
   * thenCode(callable).doesNotThrowAnyException();</code></pre>
   *
   * If the provided {@link ThrowingCallable} does not validate against next assertions, an error is immediately raised,
   * in that case the test description provided with {@link AbstractAssert#as(String, Object...) as(String, Object...)} is not honored.<br>
   * To use a test description, use {@link #catchThrowable(ThrowingCallable)} as shown below.
   *
   * <pre><code class='java'> ThrowingCallable doNothing = () -&gt; {
   *   // do nothing
   * };
   *
   * // assertion fails and "display me" appears in the assertion error
   * thenCode(doNothing).as("display me")
   *                    .isInstanceOf(Exception.class);
   *
   * // assertion will fail AND "display me" will appear in the error
   * Throwable thrown = catchThrowable(doNothing);
   * thenCode(thrown).as("display me")
   *                 .isInstanceOf(Exception.class); </code></pre>
   * <p>
   * This method was not named {@code then} because the java compiler reported it ambiguous when used directly with a lambda :(
   *
   * @param shouldRaiseOrNotThrowable The {@link ThrowingCallable} or lambda with the code that should raise the throwable.
   * @return The captured exception or <code>null</code> if none was raised by the callable.
   * @since 3.7.0
   */
  public static AbstractThrowableAssert<?, ? extends Throwable> thenCode(ThrowingCallable shouldRaiseOrNotThrowable) {
    return assertThat(catchThrowable(shouldRaiseOrNotThrowable));
  }

  /**
   * Creates a new instance of <code>{@link org.assertj.core.api.ObjectAssert}</code> for any object.
   * <p>
   * This overload is useful, when an overloaded method of then(...) takes precedence over the generic {@link #then(Object)}.
   * <p>
   * Example:
   * <p>
   * Cast necessary because {@link #then(List)} "forgets" actual type:
   * <pre>{@code then(new LinkedList<>(asList("abc"))).matches(list -> ((Deque<String>) list).getFirst().equals("abc")); }</pre>
   * No cast needed, but also no additional list assertions:
   * <pre>{@code thenObject(new LinkedList<>(asList("abc"))).matches(list -> list.getFirst().equals("abc")); }</pre>
   *
   * @param <T> the type of the actual value.
   * @param actual the actual value.
   * @return the created assertion object.
   * @since 3.12.0
   */
  public static <T> ObjectAssert<T> thenObject(T actual) {
    return then(actual);
  }

  /**
   * Uses the given instance as the instance under test for all the assertions expressed as the passed {@link Consumer}s.
   * <p>
   * This is useful to avoid repeating getting the instance to test, a bit like a <a href="https://mrhaki.blogspot.com/2009/09/groovy-goodness-with-method.html">with</a> block which turns the target into
   * the equivalent of {@code this} (as  in Groovy for example).
   * <p>
   * Example:
   * <pre><code> thenWith(team.getPlayers().get(0).getStats(),
   *            stat -&gt; assertThat(stats.pointPerGame).isGreaterThan(25.7),
   *            stat -&gt; assertThat(stats.assistsPerGame).isGreaterThan(7.2),
   *            stat -&gt; assertThat(stats.reboundsPerGame).isBetween(9, 12)
   *            );</code></pre>
   * <p>
   * {@code thenWith} is variation of {@link AbstractAssert#satisfies(Consumer[])} hopefully easier to find for some users.
   *
   * @param <T> the type of the actual value.
   * @param actual the actual value.
   * @param requirements to assert on the actual object - must not be null.
   * @return the created assertion object.
   * @since 3.20.0
   */
  @CanIgnoreReturnValue
  @SafeVarargs
  public static <T> ObjectAssert<T> thenWith(T actual, Consumer<T>... requirements) {
    return then(actual).satisfies(requirements);
  }

  /**
   * Creates a new instance of <code>{@link org.assertj.core.api.TemporalAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   * @since 3.26.1
   */
  public static TemporalAssert thenTemporal(Temporal actual) {
    return assertThatTemporal(actual);
  }

  /**
   * Creates a new instance of <code>{@link org.assertj.core.api.LocalDateAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   */
  public static AbstractLocalDateAssert<?> then(LocalDate actual) {
    return assertThat(actual);
  }

  /**
   * Creates a new instance of <code>{@link org.assertj.core.api.YearMonthAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   * @since 3.26.0
   */
  public static AbstractYearMonthAssert<?> then(YearMonth actual) {
    return assertThat(actual);
  }

  /**
   * Creates a new instance of <code>{@link org.assertj.core.api.LocalDateTimeAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   */
  public static AbstractLocalDateTimeAssert<?> then(LocalDateTime actual) {
    return assertThat(actual);
  }

  /**
   * Creates a new instance of <code>{@link org.assertj.core.api.ZonedDateTimeAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   */
  public static AbstractZonedDateTimeAssert<?> then(ZonedDateTime actual) {
    return assertThat(actual);
  }

  /**
   * Creates a new instance of <code>{@link LocalTimeAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   */
  public static AbstractLocalTimeAssert<?> then(LocalTime actual) {
    return assertThat(actual);
  }

  /**
   * Creates a new instance of <code>{@link OffsetTimeAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   */
  public static AbstractOffsetTimeAssert<?> then(OffsetTime actual) {
    return assertThat(actual);
  }

  /**
   * Creates a new instance of <code>{@link InstantAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   * @since 3.7.0
   */
  public static AbstractInstantAssert<?> then(Instant actual) {
    return assertThat(actual);
  }

  /**
   * Creates a new instance of <code>{@link DurationAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   * @since 3.15.0
   */
  public static AbstractDurationAssert<?> then(Duration actual) {
    return assertThat(actual);
  }

  /**
   * Creates a new instance of <code>{@link PeriodAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   * @since 3.17.0
   */
  public static AbstractPeriodAssert<?> then(Period actual) {
    return assertThat(actual);
  }

  /**
   * Creates a new instance of <code>{@link UriAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   */
  public static AbstractUriAssert<?> then(URI actual) {
    return assertThat(actual);
  }

  /**
   * Creates a new instance of <code>{@link UrlAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   */
  public static AbstractUrlAssert<?> then(URL actual) {
    return assertThat(actual);
  }

  /**
   * Creates a new instance of <code>{@link OffsetTimeAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   */
  public static AbstractOffsetDateTimeAssert<?> then(OffsetDateTime actual) {
    return assertThat(actual);
  }

  /**
   * Create assertion for {@link java.util.concurrent.CompletableFuture}.
   *
   * @param future the actual value.
   * @param <RESULT> the type of the value contained in the {@link java.util.concurrent.CompletableFuture}.
   *
   * @return the created assertion object.
   */
  public static <RESULT> CompletableFutureAssert<RESULT> then(CompletableFuture<RESULT> future) {
    return assertThat(future);
  }

  /**
   * Create assertion for {@link java.util.concurrent.CompletionStage} by converting it to a {@link CompletableFuture} and returning a {@link CompletableFutureAssert}.
   * <p>
   * If the given {@link java.util.concurrent.CompletionStage} is null, the {@link CompletableFuture} in the returned {@link CompletableFutureAssert} will also be null.
   *
   * @param actual the actual value.
   * @param <RESULT> the type of the value contained in the {@link java.util.concurrent.CompletionStage}.
   *
   * @return the created assertion object.
   */
  public static <RESULT> CompletableFutureAssert<RESULT> then(CompletionStage<RESULT> actual) {
    return assertThat(actual);
  }

  /**
   * Returns the given assertion. This method improves code readability by surrounding the given assertion with
   * <code>then</code>.
   * <p>
   * Consider for example the following MyButton and MyButtonAssert classes:
   * <pre><code class='java'> public class MyButton extends JButton {
   *
   *   private boolean blinking;
   *
   *   public boolean isBlinking() { return this.blinking; }
   *
   *   public void setBlinking(boolean blink) { this.blinking = blink; }
   * }
   *
   * private static class MyButtonAssert implements AssertDelegateTarget {
   *
   *   private MyButton button;
   *   MyButtonAssert(MyButton button) { this.button = button; }
   *
   *   void isBlinking() {
   *     // standard assertion from core Assertions.then
   *     then(button.isBlinking()).isTrue();
   *   }
   *
   *   void isNotBlinking() {
   *     // standard assertion from core Assertions.then
   *     then(button.isBlinking()).isFalse();
   *   }
   * }</code></pre>
   *
   * As MyButtonAssert implements AssertDelegateTarget, you can use <code>then(buttonAssert).isBlinking();</code>
   * instead of <code>buttonAssert.isBlinking();</code> to have easier to read assertions:
   * <pre><code class='java'> {@literal @}Test
   * public void AssertDelegateTarget_example() {
   *
   *   MyButton button = new MyButton();
   *   MyButtonAssert buttonAssert = new MyButtonAssert(button);
   *
   *   // you can encapsulate MyButtonAssert assertions methods within then
   *   then(buttonAssert).isNotBlinking(); // same as : buttonAssert.isNotBlinking();
   *
   *   button.setBlinking(true);
   *
   *   then(buttonAssert).isBlinking(); // same as : buttonAssert.isBlinking();
   * }</code></pre>
   *
   * @param <T> the generic type of the user-defined assert.
   * @param assertion the assertion to return.
   * @return the given assertion.
   */
  public static <T extends AssertDelegateTarget> T then(T assertion) {
    return assertion;
  }

  /**
   * Delegates the creation of the {@link Assert} to the {@link AssertProvider#assertThat()} of the given component.
   *
   * <p>
   * Read the comments on {@link AssertProvider} for an example of its usage.
   * </p>
   *
   * @param <T> the AssertProvider wrapped type.
   * @param component
   *          the component that creates its own assert
   * @return the associated {@link Assert} of the given component
   */
  public static <T> T then(final AssertProvider<T> component) {
    return component.assertThat();
  }

  /**
   * Creates a new instance of <code>{@link ListAssert}</code> from the given {@link Stream}.
   * <p>
   * <b>Be aware that the {@code Stream} under test will be converted to a {@code List} when an assertions require to inspect its content.
   * Once this is done the {@code Stream} can't reused as it would have been consumed.</b>
   * <p>
   * Calling multiple methods on the returned {@link ListAssert} is safe as it only interacts with the {@link List} built from the {@link Stream}.
   * <p>
   * Examples:
   * <pre><code class='java'> // you can chain multiple assertions on the Stream as it is converted to a List
   * then(Stream.of(1, 2, 3)).contains(1)
   *                         .doesNotContain(42);</code></pre>
   * <p>
   * The following assertion fails as the Stream under test is converted to a List before being compared to the expected Stream:
   * <pre><code class='java'> // FAIL: the Stream under test is converted to a List and compared to a Stream but a List is not a Stream.
   * then(Stream.of(1, 2, 3)).isEqualTo(Stream.of(1, 2, 3));</code></pre>
   * <p>
   * These assertions succeed as {@code isEqualTo} and {@code isSameAs} checks references which does not require to convert the Stream to a List.
   * <pre><code class='java'> // The following assertions succeed as it only performs reference checking which does not require to convert the Stream to a List
   * Stream&lt;Integer&gt; stream = Stream.of(1, 2, 3);
   * then(stream).isEqualTo(stream)
   *             .isSameAs(stream);</code></pre>
   *
   * @param <ELEMENT> the type of elements.
   * @param actual the actual {@link Stream} value.
   * @return the created assertion object.
   */
  public static <ELEMENT> ListAssert<ELEMENT> then(Stream<? extends ELEMENT> actual) {
    return assertThat(actual);
  }

  /**
   * Creates a new instance of <code>{@link ListAssert}</code> from the given {@link Stream}.
   * <p>
   * Use this over {@link #then(Stream)} in case of ambiguous method resolution when the object under test 
   * implements several interfaces Assertj provides <code>then</code> for. 
   * <p>
   * <b>Be aware that the {@code Stream} under test will be converted to a {@code List} when an assertions require to inspect its content.
   * Once this is done the {@code Stream} can't reused as it would have been consumed.</b>
   * <p>
   * Calling multiple methods on the returned {@link ListAssert} is safe as it only interacts with the {@link List} built from the {@link Stream}.
   * <p>
   * Examples:
   * <pre><code class='java'> // you can chain multiple assertions on the Stream as it is converted to a List
   * then(Stream.of(1, 2, 3)).contains(1)
   *                         .doesNotContain(42);</code></pre>
   * <p>
   * The following assertion fails as the Stream under test is converted to a List before being compared to the expected Stream:
   * <pre><code class='java'> // FAIL: the Stream under test is converted to a List and compared to a Stream but a List is not a Stream.
   * then(Stream.of(1, 2, 3)).isEqualTo(Stream.of(1, 2, 3));</code></pre>
   * <p>
   * These assertions succeed as {@code isEqualTo} and {@code isSameAs} checks references which does not require to convert the Stream to a List.
   * <pre><code class='java'> // The following assertions succeed as it only performs reference checking which does not require to convert the Stream to a List
   * Stream&lt;Integer&gt; stream = Stream.of(1, 2, 3);
   * then(stream).isEqualTo(stream)
   *             .isSameAs(stream);</code></pre>
   *
   * @param <ELEMENT> the type of elements.
   * @param actual the actual value.
   * @return the created assertion object.
   * @since 3.23.0
   */
  public static <ELEMENT> ListAssert<ELEMENT> thenStream(Stream<? extends ELEMENT> actual) {
    return then(actual);
  }

  /**
   * Creates a new instance of <code>{@link ListAssert}</code> from the given {@link DoubleStream}.
   * <p>
   * <b>Be aware that the {@code DoubleStream} under test will be converted to a {@code List} when an assertions require to inspect its content.
   * Once this is done the {@code DoubleStream} can't reused as it would have been consumed.</b>
   * <p>
   * Calling multiple methods on the returned {@link ListAssert} is safe as it only interacts with the {@link List} built from the {@link DoubleStream}.
   * <p>
   * Examples:
   * <pre><code class='java'> // you can chain multiple assertions on the DoubleStream as it is converted to a List
   * then(DoubleStream.of(1.0, 2.0, 3.0)).contains(1.0)
   *                                     .doesNotContain(42.0);</code></pre>
   * <p>
   * The following assertion fails as the DoubleStream under test is converted to a List before being compared to the expected DoubleStream:
   * <pre><code class='java'> // FAIL: the DoubleStream under test is converted to a List and compared to a DoubleStream but a List is not a DoubleStream.
   * then(DoubleStream.of(1.0, 2.0, 3.0)).isEqualTo(DoubleStream.of(1.0, 2.0, 3.0));</code></pre>
   * <p>
   * These assertions succeed as {@code isEqualTo} and {@code isSameAs} checks references which does not require to convert the DoubleStream to a List.
   * <pre><code class='java'> // The following assertions succeed as it only performs reference checking which does not require to convert the DoubleStream to a List
   * DoubleStream stream = DoubleStream.of(1.0, 2.0, 3.0);
   * then(stream).isEqualTo(stream)
   *             .isSameAs(stream);</code></pre>
   *
   * @param actual the actual {@link DoubleStream} value.
   * @return the created assertion object.
   */
  public static ListAssert<Double> then(DoubleStream actual) {
    return assertThat(actual);
  }

  /**
   * Creates a new instance of <code>{@link ListAssert}</code> from the given {@link LongStream}.
   * <p>
   * <b>Be aware that the {@code LongStream} under test will be converted to a {@code List} when an assertions require to inspect its content.
   * Once this is done the {@code LongStream} can't reused as it would have been consumed.</b>
   * <p>
   * Calling multiple methods on the returned {@link ListAssert} is safe as it only interacts with the {@link List} built from the {@link LongStream}.
   * <p>
   * Examples:
   * <pre><code class='java'> // you can chain multiple assertions on the LongStream as it is converted to a List
   * then(LongStream.of(1, 2, 3)).contains(1)
   *                             .doesNotContain(42);</code></pre>
   * <p>
   * The following assertion fails as the LongStream under test is converted to a List before being compared to the expected LongStream:
   * <pre><code class='java'> // FAIL: the LongStream under test is converted to a List and compared to a LongStream but a List is not a LongStream.
   * then(LongStream.of(1, 2, 3)).isEqualTo(LongStream.of(1, 2, 3));</code></pre>
   * <p>
   * These assertions succeed as {@code isEqualTo} and {@code isSameAs} checks references which does not require to convert the LongStream to a List.
   * <pre><code class='java'> // The following assertions succeed as it only performs reference checking which does not require to convert the LongStream to a List
   * LongStream stream = LongStream.of(1, 2, 3);
   * then(stream).isEqualTo(stream)
   *             .isSameAs(stream);</code></pre>
   *
   * @param actual the actual {@link LongStream} value.
   * @return the created assertion object.
   */
  public static ListAssert<Long> then(LongStream actual) {
    return assertThat(actual);
  }

  /**
   * Creates a new instance of <code>{@link ListAssert}</code> from the given {@link IntStream}.
   * <p>
   * <b>Be aware that the {@code IntStream} under test will be converted to a {@code List} when an assertions require to inspect its content.
   * Once this is done the {@code IntStream} can't reused as it would have been consumed.</b>
   * <p>
   * Calling multiple methods on the returned {@link ListAssert} is safe as it only interacts with the {@link List} built from the {@link IntStream}.
   * <p>
   * Examples:
   * <pre><code class='java'> // you can chain multiple assertions on the IntStream as it is converted to a List
   * then(IntStream.of(1, 2, 3)).contains(1)
   *                            .doesNotContain(42);</code></pre>
   * <p>
   * The following assertion fails as the IntStream under test is converted to a List before being compared to the expected IntStream:
   * <pre><code class='java'> // FAIL: the IntStream under test is converted to a List and compared to a IntStream but a List is not a IntStream.
   * then(IntStream.of(1, 2, 3)).isEqualTo(IntStream.of(1, 2, 3));</code></pre>
   * <p>
   * These assertions succeed as {@code isEqualTo} and {@code isSameAs} checks references which does not require to convert the IntStream to a List.
   * <pre><code class='java'> // The following assertions succeed as it only performs reference checking which does not require to convert the IntStream to a List
   * IntStream stream = IntStream.of(1, 2, 3);
   * then(stream).isEqualTo(stream)
   *             .isSameAs(stream);</code></pre>
   *
   * @param actual the actual {@link IntStream} value.
   * @return the created assertion object.
   */
  public static ListAssert<Integer> then(IntStream actual) {
    return assertThat(actual);
  }

  /**
   * Creates a new instance of <code>{@link SpliteratorAssert}</code> from the given {@link Spliterator}.
   * <p>
   * Example:
   * <pre><code class='java'> Spliterator&lt;Integer&gt; spliterator = Stream.of(1, 2, 3).spliterator();
   * then(spliterator).hasCharacteristics(Spliterator.SIZED); </code></pre>
   *
   * @param <ELEMENT> the type of elements.
   * @param actual the spliterator to test.
   * @return the created assertion object.
   * @since 3.14.0
   */
  public static <ELEMENT> SpliteratorAssert<ELEMENT> then(Spliterator<ELEMENT> actual) {
    return assertThat(actual);
  }

  /**
   * Allows catching a {@link Throwable} more easily when used with Java 8 lambdas.
   * <p>
   * This caught {@link Throwable} can then be asserted.
   * <p>
   * If you need to assert on the real type of Throwable caught (e.g. IOException), use {@link #catchThrowableOfType(Class, ThrowingCallable)}.
   * <p>
   * Example:
   * <pre><code class='java'>{@literal @}Test
   * public void testException() {
   *   // when
   *   Throwable thrown = catchThrowable(() -&gt; { throw new Exception("boom!"); });
   *
   *   // then
   *   assertThat(thrown).isInstanceOf(Exception.class)
   *                     .hasMessageContaining("boom");
   * } </code></pre>
   *
   * @param throwingCallable The lambda with the code that should raise the exception.
   * @return The captured exception or <code>null</code> if none was raised by the callable.
   * @see #catchThrowableOfType(Class, ThrowingCallable)
   *
   * @since 3.20.0
   */
  public static Throwable catchThrowable(ThrowingCallable throwingCallable) {
    return AssertionsForClassTypes.catchThrowable(throwingCallable);
  }

  /**
   * Allows catching a {@link Throwable} of a specific type.
   * <p>
   * A call is made to {@code catchThrowable(ThrowingCallable)}, if no exception is thrown it returns null
   * otherwise it checks that the caught {@link Throwable} has the specified type and casts it making it convenient to perform subtype-specific assertions on it.
   * <p>
   * Example:
   * <pre><code class='java'> class TextException extends Exception {
   *   int line;
   *   int column;
   *
   *   public TextException(String msg, int line, int column) {
   *     super(msg);
   *     this.line = line;
   *     this.column = column;
   *   }
   * }
   *
   * TextException textException = catchThrowableOfType(() -&gt; { throw new TextException("boom!", 1, 5); },
   *                                                    TextException.class);
   * // assertions succeed
   * assertThat(textException).hasMessage("boom!");
   * assertThat(textException.line).isEqualTo(1);
   * assertThat(textException.column).isEqualTo(5);
   *
   * // succeeds as catchThrowableOfType returns null when the code does not thrown any exceptions
   * assertThat(catchThrowableOfType(() -&gt; {}, Exception.class)).isNull();
   *
   * // fails as TextException is not a RuntimeException
   * catchThrowableOfType(() -&gt; { throw new TextException("boom!", 1, 5); }, RuntimeException.class);</code></pre>
   *
   * @param <THROWABLE> the {@link Throwable} type.
   * @param throwingCallable The lambda with the code that should raise the exception.
   * @param type The type of exception that the code is expected to raise.
   * @return The captured exception or <code>null</code> if none was raised by the callable.
   * @see #catchThrowable(ThrowingCallable)
   * @since 3.20.0
   * @deprecated use {@link #catchThrowableOfType(Class, ThrowingCallable)} instead.
   */
  @Deprecated
  public static <THROWABLE extends Throwable> THROWABLE catchThrowableOfType(ThrowingCallable throwingCallable,
                                                                             Class<THROWABLE> type) {
    return catchThrowableOfType(type, throwingCallable);
  }

  /**
   * Allows catching a {@link Throwable} of a specific type.
   * <p>
   * A call is made to {@code catchThrowable(ThrowingCallable)}, if no exception is thrown it returns null
   * otherwise it checks that the caught {@link Throwable} has the specified type and casts it making it convenient to perform subtype-specific assertions on it.
   * <p>
   * Example:
   * <pre><code class='java'> class TextException extends Exception {
   *   int line;
   *   int column;
   *
   *   public TextException(String msg, int line, int column) {
   *     super(msg);
   *     this.line = line;
   *     this.column = column;
   *   }
   * }
   *
   * TextException textException = catchThrowableOfType(TextException.class() ,
   *                                                    -&gt; { throw new TextException("boom!", 1, 5); });
   * // assertions succeed
   * assertThat(textException).hasMessage("boom!");
   * assertThat(textException.line).isEqualTo(1);
   * assertThat(textException.column).isEqualTo(5);
   *
   * // succeeds as catchThrowableOfType returns null when the code does not thrown any exceptions
   * assertThat(catchThrowableOfType(Exception.class, () -&gt; {})).isNull();
   *
   * // fails as TextException is not a RuntimeException
   * catchThrowableOfType(RuntimeException.class, () -&gt; { throw new TextException("boom!", 1, 5); });</code></pre>
   *
   * @param <THROWABLE> the {@link Throwable} type.
   * @param throwingCallable The lambda with the code that should raise the exception.
   * @param type The type of exception that the code is expected to raise.
   * @return The captured exception or <code>null</code> if none was raised by the callable.
   * @see #catchThrowable(ThrowingCallable)
   * @since 3.26.0
   */
  public static <THROWABLE extends Throwable> THROWABLE catchThrowableOfType(Class<THROWABLE> type,
                                                                             ThrowingCallable throwingCallable) {
    return ThrowableAssert.catchThrowableOfType(type, throwingCallable);
  }

  /**
   * Allows catching an instance of {@link Exception}.
   * <p>
   * A call is made to {@code catchThrowable(ThrowingCallable)}, if no exception is thrown it returns null
   * otherwise it checks that the caught {@link Throwable} is of type {@link Exception} and casts it making it convenient to perform subtype-specific assertions on it.
   * <p>
   * Example:
   * <pre><code class='java'>
   * Exception exception = catchException(() -&gt; {throw new Exception("boom!");});
   * // assertions succeed
   * assertThat(exception).hasMessage("boom!");
   *
   * // succeeds as catchException returns null when the code does not throw any exceptions
   * assertThat(catchException(() -&gt; {})).isNull();
   *
   * // fails as the thrown instance is not an Exception
   * catchException(() -&gt; {throw new Throwable("boom!");});</code></pre>
   *
   * @param throwingCallable The lambda with the code that should raise the exception.
   * @return The captured exception or <code>null</code> if none was raised by the callable.
   * @see #catchThrowable(ThrowingCallable)
   * @since 3.22.0
   */
  public static Exception catchException(ThrowingCallable throwingCallable) {
    return catchThrowableOfType(Exception.class, throwingCallable);
  }

  /**
   * Allows catching an instance of {@link RuntimeException}.
   * <p>
   * A call is made to {@code catchThrowable(ThrowingCallable)}, if no exception is thrown it returns null
   * otherwise it checks that the caught {@link Throwable} is of type {@link RuntimeException} and casts it making it convenient to perform subtype-specific assertions on it.
   * <p>
   * Example:
   * <pre><code class='java'>
   * RuntimeException runtimeException = catchRuntimeException(() -&gt; {throw new RuntimeException("boom!");});
   * // assertions succeed
   * assertThat(runtimeException).hasMessage("boom!");
   *
   * // succeeds as catchRuntimeException returns null when the code does not throw any exceptions
   * assertThat(catchRuntimeException(() -&gt; {})).isNull();
   *
   * // fails as the thrown instance is not a RuntimeException
   * catchRuntimeException(() -&gt; {throw new Exception("boom!");});</code></pre>
   *
   * @param throwingCallable The lambda with the code that should raise the exception.
   * @return The captured exception or <code>null</code> if none was raised by the callable.
   * @see #catchThrowable(ThrowingCallable)
   * @since 3.22.0
   */
  public static RuntimeException catchRuntimeException(ThrowingCallable throwingCallable) {
    return catchThrowableOfType(RuntimeException.class, throwingCallable);
  }

  /**
   * Allows catching an instance of {@link NullPointerException}.
   * <p>
   * A call is made to {@code catchThrowable(ThrowingCallable)}, if no exception is thrown it returns null
   * otherwise it checks that the caught {@link Throwable} is of type {@link RuntimeException} and casts it making it convenient to perform subtype-specific assertions on it.
   * <p>
   * Example:
   * <pre><code class='java'>
   * NullPointerException nullPointerException = catchNullPointerException(() -&gt; {throw new NullPointerException("boom!");});
   * // assertions succeed
   * assertThat(nullPointerException).hasMessage("boom!");
   *
   * // succeeds as catchNullPointerException returns null when the code does not throw any exceptions
   * assertThat(catchNullPointerException(() -&gt; {})).isNull();
   *
   * // fails as the thrown instance is not a NullPointerException
   * catchNullPointerException(() -&gt; {throw new Exception("boom!");});</code></pre>
   *
   * @param throwingCallable The lambda with the code that should raise the exception.
   * @return The captured exception or <code>null</code> if none was raised by the callable.
   * @see #catchThrowable(ThrowingCallable)
   * @since 3.22.0
   */
  public static NullPointerException catchNullPointerException(ThrowingCallable throwingCallable) {
    return catchThrowableOfType(NullPointerException.class, throwingCallable);
  }

  /**
   * Allows catching an instance of {@link IllegalArgumentException}.
   * <p>
   * A call is made to {@code catchThrowable(ThrowingCallable)}, if no exception is thrown it returns null
   * otherwise it checks that the caught {@link Throwable} is of type {@link IllegalArgumentException} and casts it making it convenient to perform subtype-specific assertions on it.
   * <p>
   * Example:
   * <pre><code class='java'>
   * IllegalArgumentException illegalArgumentException = catchIllegalArgumentException(() -&gt; {throw new IllegalArgumentException("boom!");});
   * // assertions succeed
   * assertThat(illegalArgumentException).hasMessage("boom!");
   *
   * // succeeds as catchNullPointerException returns null when the code does not throw any exceptions
   * assertThat(catchIllegalArgumentException(() -&gt; {})).isNull();</code></pre>
   *
   * // fails as the thrown instance is not an IllegalArgumentException
   * catchIllegalArgumentException(() -&gt; {throw new Exception("boom!");});
   *
   * @param throwingCallable The lambda with the code that should raise the exception.
   * @return The captured exception or <code>null</code> if none was raised by the callable.
   * @see #catchThrowable(ThrowingCallable)
   * @since 3.22.0
   */
  public static IllegalArgumentException catchIllegalArgumentException(ThrowingCallable throwingCallable) {
    return catchThrowableOfType(IllegalArgumentException.class, throwingCallable);
  }

  /**
   * Allows catching an instance of {@link IOException}.
   * <p>
   * A call is made to {@code catchThrowable(ThrowingCallable)}, if no exception is thrown it returns null
   * otherwise it checks that the caught {@link Throwable} is of type {@link IOException} and casts it making it convenient to perform subtype-specific assertions on it.
   * <p>
   * Example:
   * <pre><code class='java'>
   * IOException iOException = catchIOException(() -&gt; {throw new IOException("boom!");});
   * // assertions succeed
   * assertThat(iOException).hasMessage("boom!");
   *
   * // succeeds as catchIOException returns null when the code does not throw any exceptions
   * assertThat(catchIOException(() -&gt; {})).isNull();
   *
   * // fails as the thrown instance is not an IOException
   * catchIOException(() -&gt; {throw new Exception("boom!");});</code></pre>
   *
   * @param throwingCallable The lambda with the code that should raise the exception.
   * @return The captured exception or <code>null</code> if none was raised by the callable.
   * @see #catchThrowable(ThrowingCallable)
   * @since 3.22.0
   */
  public static IOException catchIOException(ThrowingCallable throwingCallable) {
    return catchThrowableOfType(IOException.class, throwingCallable);
  }

  /**
   * Allows catching an instance of {@link ReflectiveOperationException}.
   * <p>
   * A call is made to {@code catchThrowable(ThrowingCallable)}, if no exception is thrown it returns null
   * otherwise it checks that the caught {@link Throwable} is of type {@link ReflectiveOperationException} and casts it making it convenient to perform subtype-specific assertions on it.
   * <p>
   * Example:
   * <pre><code class='java'>
   * ReflectiveOperationException reflectiveOperationException = catchReflectiveOperationException(() -&gt; {throw new ReflectiveOperationException("boom!");});
   * // assertions succeed
   * assertThat(reflectiveOperationException).hasMessage("boom!");
   *
   * // succeeds as catchReflectiveOperationException returns null when the code does not throw any exceptions
   * assertThat(catchReflectiveOperationException(() -&gt; {})).isNull();
   *
   * // fails as the thrown instance is not an IOException
   * catchReflectiveOperationException(() -&gt; {throw new Exception("boom!");});</code></pre>
   *
   * @param throwingCallable The lambda with the code that should raise the exception.
   * @return The captured exception or <code>null</code> if none was raised by the callable.
   * @see #catchThrowable(ThrowingCallable)
   * @since 3.22.0
   */
  public static ReflectiveOperationException catchReflectiveOperationException(ThrowingCallable throwingCallable) {
    return catchThrowableOfType(ReflectiveOperationException.class, throwingCallable);
  }

  /**
   * Allows catching an instance of {@link IllegalStateException}.
   * <p>
   * A call is made to {@code catchThrowable(ThrowingCallable)}, if no exception is thrown it returns null
   * otherwise it checks that the caught {@link Throwable} is of type {@link IllegalStateException} and casts it making it convenient to perform subtype-specific assertions on it.
   * <p>
   * Example:
   * <pre><code class='java'>
   * IllegalStateException illegalStateException = catchIllegalStateException(() -&gt; {throw new IllegalStateException("boom!");});
   * // assertions succeed
   * assertThat(illegalStateException).hasMessage("boom!");
   *
   * // succeeds as catchReflectiveOperationException returns null when the code does not throw any exceptions
   * assertThat(catchIllegalStateException(() -&gt; {})).isNull();
   *
   * // fails as the thrown instance is not an IOException
   * catchIllegalStateException(() -&gt; {throw new Exception("boom!");});</code></pre>
   *
   * @param throwingCallable The lambda with the code that should raise the exception.
   * @return The captured exception or <code>null</code> if none was raised by the callable.
   * @see #catchThrowable(ThrowingCallable)
   * @since 3.22.0
   */
  public static IllegalStateException catchIllegalStateException(ThrowingCallable throwingCallable) {
    return catchThrowableOfType(IllegalStateException.class, throwingCallable);
  }

  /**
   * Allows catching an instance of {@link IndexOutOfBoundsException}.
   * <p>
   * A call is made to {@code catchThrowable(ThrowingCallable)}, if no exception is thrown it returns null
   * otherwise it checks that the caught {@link Throwable} is of type {@link IndexOutOfBoundsException} and casts it making it convenient to perform subtype-specific assertions on it.
   * <p>
   * Example:
   * <pre><code class='java'>
   * IndexOutOfBoundsException indexOutOfBoundsException = catchIndexOutOfBoundsException(() -&gt; {throw new IndexOutOfBoundsException("boom!");});
   * // assertions succeed
   * assertThat(indexOutOfBoundsException).hasMessage("boom!");
   *
   * // succeeds as catchIndexOutOfBoundsException returns null when the code does not throw any exceptions
   * assertThat(catchIndexOutOfBoundsException(() -&gt; {})).isNull();
   *
   * // fails as the thrown instance is not an IOException
   * catchIndexOutOfBoundsException(() -&gt; {throw new Exception("boom!");});</code></pre>
   *
   * @param throwingCallable The lambda with the code that should raise the exception.
   * @return The captured exception or <code>null</code> if none was raised by the callable.
   * @see #catchThrowable(ThrowingCallable)
   * @since 3.22.0
   */
  public static IndexOutOfBoundsException catchIndexOutOfBoundsException(ThrowingCallable throwingCallable) {
    return catchThrowableOfType(IndexOutOfBoundsException.class, throwingCallable);
  }

  /**
   * Entry point to check that an exception of type T is thrown by a given {@code throwingCallable}
   * which allows to chain assertions on the thrown exception.
   * <p>
   * Example:
   * <pre><code class='java'> thenExceptionOfType(IOException.class)
   *           .isThrownBy(() -&gt; { throw new IOException("boom!"); })
   *           .withMessage("boom!"); </code></pre>
   *
   * This method is more or less the same of {@link #thenThrownBy(ThrowingCallable)} but in a more natural way.
   *
   * @param <T> the exception type.
   * @param exceptionType the exception type class.
   * @return the created {@link ThrowableTypeAssert}.
   */
  public static <T extends Throwable> ThrowableTypeAssert<T> thenExceptionOfType(final Class<? extends T> exceptionType) {
    return assertThatExceptionOfType(exceptionType);
  }

  /**
   * Entry point to check that no exception of any type is thrown by a given {@code throwingCallable}.
   * <p>
   * Example:
   * <pre><code class='java'>thenNoException().isThrownBy(() -&gt; { System.out.println("OK"); });</code></pre>
   *
   * This method is more or less the same of {@code thenCode(...).doesNotThrowAnyException();} but in a more natural way.
   *
   * @return the created {@link NotThrownAssert}.
   * @since 3.17.0
   */
  public static NotThrownAssert thenNoException() {
    return assertThatNoException();
  }

  /**
   * Alias for {@link #thenExceptionOfType(Class)} for {@link NullPointerException}.
   *
   * @return the created {@link ThrowableTypeAssert}.
   */
  public static ThrowableTypeAssert<NullPointerException> thenNullPointerException() {
    return assertThatNullPointerException();
  }

  /**
   * Alias for {@link #thenExceptionOfType(Class)} for {@link IllegalArgumentException}.
   *
   * @return the created {@link ThrowableTypeAssert}.
   */
  public static ThrowableTypeAssert<IllegalArgumentException> thenIllegalArgumentException() {
    return assertThatIllegalArgumentException();
  }

  /**
   * Alias for {@link #thenExceptionOfType(Class)} for {@link IOException}.
   *
   * @return the created {@link ThrowableTypeAssert}.
   */
  public static ThrowableTypeAssert<IOException> thenIOException() {
    return assertThatIOException();
  }

  /**
   * Alias for {@link #thenExceptionOfType(Class)} for {@link IllegalStateException}.
   *
   * @return the created {@link ThrowableTypeAssert}.
   */
  public static ThrowableTypeAssert<IllegalStateException> thenIllegalStateException() {
    return assertThatIllegalStateException();
  }

  /**
   * Alias for {@link #thenExceptionOfType(Class)} for {@link Exception}.
   *
   * @return the created {@link ThrowableTypeAssert}.
   * @since 3.23.0
   */
  public static ThrowableTypeAssert<Exception> thenException() {
    return assertThatException();
  }

  /**
   * Alias for {@link #thenExceptionOfType(Class)} for {@link IndexOutOfBoundsException}.
   *
   * @return the created {@link ThrowableTypeAssert}.
   * @since 3.23.0
   */
  public static ThrowableTypeAssert<IndexOutOfBoundsException> thenIndexOutOfBoundsException() {
    return assertThatIndexOutOfBoundsException();
  }

  /**
   * Alias for {@link #thenExceptionOfType(Class)} for {@link ReflectiveOperationException}.
   *
   * @return the created {@link ThrowableTypeAssert}.
   * @since 3.23.0
   */
  public static ThrowableTypeAssert<ReflectiveOperationException> thenReflectiveOperationException() {
    return assertThatReflectiveOperationException();
  }

  /**
   * Alias for {@link #thenExceptionOfType(Class)} for {@link RuntimeException}.
   *
   * @return the created {@link ThrowableTypeAssert}.
   * @since 3.23.0
   */
  public static ThrowableTypeAssert<RuntimeException> thenRuntimeException() {
    return assertThatRuntimeException();
  }

  // -------------------------------------------------------------------------------------------------
  // fail methods : not assertions but here to have a single entry point to all AssertJ features.
  // -------------------------------------------------------------------------------------------------

  /**
   * Sets whether we remove elements related to AssertJ from assertion error stack trace.
   * <p>
   * Default is {@value org.assertj.core.configuration.Configuration#REMOVE_ASSERTJ_RELATED_ELEMENTS_FROM_STACK_TRACE}.
   *
   * @param removeAssertJRelatedElementsFromStackTrace flag.
   *
   * @since 3.20.0
   */
  public static void setRemoveAssertJRelatedElementsFromStackTrace(boolean removeAssertJRelatedElementsFromStackTrace) {
    Assertions.setRemoveAssertJRelatedElementsFromStackTrace(removeAssertJRelatedElementsFromStackTrace);
  }

  /**
   * Throws an {@link AssertionError} with the given message.
   *
   * @param <T> dummy return value type
   * @param failureMessage error message.
   * @return nothing, it's just to be used in {@code doSomething(optional.orElseGet(() -> fail("boom")));}.
   * @throws AssertionError with the given message.
   *
   * @since 3.20.0
   */
  @CanIgnoreReturnValue
  public static <T> T fail(String failureMessage) {
    return Assertions.fail(failureMessage);
  }

  /**
   * Throws an {@link AssertionError} with an empty message to be used in code like:
   * <pre><code class='java'> doSomething(optional.orElseGet(() -> fail()));</code></pre>
   *
   * @param <T> dummy return value type
   * @return nothing, it's just to be used in {@code doSomething(optional.orElseGet(() -> fail()));}.
   * @throws AssertionError with an empty message.
   * @since 3.26.0
   */
  @CanIgnoreReturnValue
  public static <T> T fail() {
    return Assertions.fail();
  }

  /**
   * Throws an {@link AssertionError} with the given message built as {@link String#format(String, Object...)}.
   *
   * @param <T> dummy return value type
   * @param failureMessage error message.
   * @param args Arguments referenced by the format specifiers in the format string.
   * @return nothing, it's just to be used in {@code doSomething(optional.orElseGet(() -> fail("b%s", ""oom)));}.
   * @throws AssertionError with the given built message.
   *
   * @since 3.20.0
   */
  @CanIgnoreReturnValue
  public static <T> T fail(String failureMessage, Object... args) {
    return Assertions.fail(failureMessage, args);
  }

  /**
   * Throws an {@link AssertionError} with the given message and with the {@link Throwable} that caused the failure.
   * @param <T> dummy return value type
   * @param failureMessage the description of the failed assertion. It can be {@code null}.
   * @param realCause cause of the error.
   * @return nothing, it's just to be used in {@code doSomething(optional.orElseGet(() -> fail("boom", cause)));}.
   * @throws AssertionError with the given message and with the {@link Throwable} that caused the failure.
   *
   * @since 3.20.0
   */
  @CanIgnoreReturnValue
  public static <T> T fail(String failureMessage, Throwable realCause) {
    return Assertions.fail(failureMessage, realCause);
  }

  /**
   * Throws an {@link AssertionError} with the {@link Throwable} that caused the failure.
   * @param <T> dummy return value type
   * @param realCause cause of the error.
   * @return nothing, it's just to be used in {@code doSomething(optional.orElseGet(() -> fail(cause)));}.
   * @throws AssertionError with the {@link Throwable} that caused the failure.
   */
  @CanIgnoreReturnValue
  public static <T> T fail(Throwable realCause) {
    return fail(null, realCause);
  }

  /**
   * Throws an {@link AssertionError} with a message explaining that a {@link Throwable} of given class was expected to be thrown
   * but had not been.
   * @param <T> dummy return value type
   * @param throwableClass the Throwable class that was expected to be thrown.
   * @return nothing, it's just to be used in {@code doSomething(optional.orElseGet(() -> shouldHaveThrown(IOException.class)));}.
   * @throws AssertionError with a message explaining that a {@link Throwable} of given class was expected to be thrown but had
   *           not been.
   *
   * @since 3.20.0
   */
  @CanIgnoreReturnValue
  public static <T> T shouldHaveThrown(Class<? extends Throwable> throwableClass) {
    return Assertions.shouldHaveThrown(throwableClass);
  }

  /**
   * In error messages, sets the threshold when iterable/array formatting will be on one line (if their String description
   * length &lt;= this parameter) or it will be formatted with one element per line.
   * <p>
   * The default value for maxLengthForSingleLineDescription is {@value Configuration#MAX_LENGTH_FOR_SINGLE_LINE_DESCRIPTION}.
   * <p>
   * The following array will be formatted on one line as its length &lt;= 80:
   * <pre><code class='java'> String[] greatBooks = array("A Game of Thrones", "The Lord of the Rings", "Assassin's Apprentice");
   * // formatted as:
   * ["A Game of Thrones", "The Lord of the Rings", "Assassin's Apprentice"]</code></pre>
   * whereas this array is formatted on multiple lines (one element per line)
   *
   * <pre><code class='java'> String[] greatBooks = array("A Game of Thrones", "The Lord of the Rings", "Assassin's Apprentice", "Guards! Guards! (Discworld)");
   * // formatted as:
   * ["A Game of Thrones",
   *  "The Lord of the Rings",
   *  "Assassin's Apprentice",
   *  "Guards! Guards! (Discworld)"]</code></pre>
   *
   * @param maxLengthForSingleLineDescription the maximum length for an iterable/array to be displayed on one line
   *
   * @since 3.20.0
   */
  public static void setMaxLengthForSingleLineDescription(int maxLengthForSingleLineDescription) {
    Assertions.setMaxLengthForSingleLineDescription(maxLengthForSingleLineDescription);
  }

  /**
   * Sets the maximum number of elements to display in error messages for iterables, arrays and map .
   * <p>
   * Example with a value of {@code 4}.
   * <p>
   * The following array will be formatted entirely as it's length is &lt;= 4:
   * <pre><code class='java'> String[] greatBooks = array("A Game of Thrones", "The Lord of the Rings", "Assassin's Apprentice");
   * // formatted as:
   * ["A Game of Thrones", "The Lord of the Rings", "Assassin's Apprentice"]</code></pre>
   *
   * whereas for this 6 elements array, only the first and last two elements are displayed (4 in total):
   * <pre><code class='java'> String[] greatBooks = array("A Game of Thrones", "The Lord of the Rings", "Assassin's Apprentice", "Guards! Guards!", "The Lies of Locke Lamora", "Aux Ombres d’Abyme");
   * // formatted as:
   * ["A Game of Thrones", "The Lord of the Rings", ... "The Lies of Locke Lamora", "Aux Ombres d’Abyme"]</code></pre>
   *
   * @param maxElementsForPrinting the maximum elements that would be printed for iterables, arrays and maps.
   *
   * @since 3.20.0
   */
  public static void setMaxElementsForPrinting(int maxElementsForPrinting) {
    Assertions.setMaxElementsForPrinting(maxElementsForPrinting);
  }

  /**
   * Enable/disable printing assertions description to the console (disabled by default).
   * <p>
   * The printed assertions description include all the successful assertions description and respectively the first failed one for standard assertions and all failed ones for soft assertions.
   * <p>
   * If you want to process the description differently, create a {@link Consumer Consumer&lt;Description&gt;} and register it with {@link #setDescriptionConsumer(Consumer)}.
   *
   * @param printAssertionsDescription whether to print assertions description.
   *
   * @since 3.20.0
   */
  public static void setPrintAssertionsDescription(boolean printAssertionsDescription) {
    Assertions.setPrintAssertionsDescription(printAssertionsDescription);
  }

  /**
   * All assertions description will be consumed by the given {@link Consumer Consumer&lt;Description&gt;} allowing for example to record them in a file.
   * <p>
   * The consumed descriptions include all the successful assertions description and respectively the first failed one for standard assertions and all failed ones for soft assertions.
   * <p>
   * To unset the descriptionConsumer, call {@code setDescriptionConsumer(null);}
   *
   * @param descriptionConsumer the {@link Description} consumer
   *
   * @since 3.20.0
   */
  public static void setDescriptionConsumer(Consumer<Description> descriptionConsumer) {
    Assertions.setDescriptionConsumer(descriptionConsumer);
  }

  /**
   * Sets how many stacktrace elements are included in {@link Throwable} representation (by default this set to 3).
   * <p>
   * Examples:
   * <pre><code class='java'>  static class Test1 {
   *
   *   static void boom() {
   *     Test2.boom2();
   *   }
   *
   *   static class Test2 {
   *     static void boom2() {
   *       throw new RuntimeException();
   *     }
   *   }
   * }</code></pre>
   *
   * {@code Test1.boom()} exception should be represented like this in error messages:
   * <pre><code class='text'> java.lang.RuntimeException
   *  at org.assertj.core.presentation.Test1$Test2.boom2(StandardRepresentation_throwable_format_Test.java:35)
   *  at org.assertj.core.presentation.Test1.boom(StandardRepresentation_throwable_format_Test.java:40);java.lang.RuntimeException
   *  at org.assertj.core.presentation.Test1.lambda$1(StandardRepresentation_throwable_format_Test.java:63)org.assertj.core.util.Throwables_Description_Test$test1$test2.exception_layer_2(Throwables_Description_Test.java:24)
   *  ...(69 remaining lines not displayed - this can be changed with Assertions.setMaxStackTraceElementsDisplayed)org.assertj.core.util.Throwables_Description_Test$test1.exception_layer_1(Throwables_Description_Test.java:30)</code></pre>
   *
   * @param maxStackTraceElementsDisplayed  the maximum number of lines for a stacktrace to be displayed on one throw.
   * @see Configuration
   *
   * @since 3.20.0
   */
  public static void setMaxStackTraceElementsDisplayed(int maxStackTraceElementsDisplayed) {
    Assertions.setMaxStackTraceElementsDisplayed(maxStackTraceElementsDisplayed);
  }

  // ------------------------------------------------------------------------------------------------------
  // properties methods : not assertions but here to have a single entry point to all AssertJ features.
  // ------------------------------------------------------------------------------------------------------

  /**
   * Only delegate to {@link Properties#extractProperty(String)} so that Assertions offers a full feature entry point
   * to
   * all AssertJ features (but you can use {@link Properties} if you prefer).
   * <p>
   * Typical usage is to chain <code>extractProperty</code> with <code>from</code> method, see examples below :
   *
   * <pre><code class='java'> // extract simple property values having a java standard type (here String)
   * assertThat(extractProperty(&quot;name&quot;, String.class).from(fellowshipOfTheRing))
   *           .contains(&quot;Boromir&quot;, &quot;Gandalf&quot;, &quot;Frodo&quot;, &quot;Legolas&quot;)
   *           .doesNotContain(&quot;Sauron&quot;, &quot;Elrond&quot;);
   *
   * // extracting property works also with user's types (here Race)
   * assertThat(extractProperty(&quot;race&quot;, String.class).from(fellowshipOfTheRing))
   *           .contains(HOBBIT, ELF).doesNotContain(ORC);
   *
   * // extract nested property on Race
   * assertThat(extractProperty(&quot;race.name&quot;, String.class).from(fellowshipOfTheRing))
   *           .contains(&quot;Hobbit&quot;, &quot;Elf&quot;)
   *           .doesNotContain(&quot;Orc&quot;);</code></pre>
   * @param <T> the type of value to extract.
   * @param propertyName the name of the property to be read from the elements of a {@code Iterable}. It may be a nested
   *          property (e.g. "address.street.number").
   * @param propertyType the type of property to extract
   * @return the created {@code Properties}.
   * @throws NullPointerException if the given property name is {@code null}.
   * @throws IllegalArgumentException if the given property name is empty.
   *
   * @since 3.20.0
   */
  public static <T> Properties<T> extractProperty(String propertyName, Class<T> propertyType) {
    return Assertions.extractProperty(propertyName, propertyType);
  }

  /**
   * Only delegate to {@link Properties#extractProperty(String)} so that Assertions offers a full feature entry point
   * to
   * all AssertJ features (but you can use {@link Properties} if you prefer).
   * <p>
   * Typical usage is to chain <code>extractProperty</code> with <code>from</code> method, see examples below :
   *
   * <pre><code class='java'> // extract simple property values, as no type has been defined the extracted property will be considered as Object
   * // to define the real property type (here String) use extractProperty(&quot;name&quot;, String.class) instead.
   * assertThat(extractProperty(&quot;name&quot;).from(fellowshipOfTheRing))
   *           .contains(&quot;Boromir&quot;, &quot;Gandalf&quot;, &quot;Frodo&quot;, &quot;Legolas&quot;)
   *           .doesNotContain(&quot;Sauron&quot;, &quot;Elrond&quot;);
   *
   * // extracting property works also with user's types (here Race), even though it will be considered as Object
   * // to define the real property type (here String) use extractProperty(&quot;name&quot;, Race.class) instead.
   * assertThat(extractProperty(&quot;race&quot;).from(fellowshipOfTheRing)).contains(HOBBIT, ELF).doesNotContain(ORC);
   *
   * // extract nested property on Race
   * assertThat(extractProperty(&quot;race.name&quot;).from(fellowshipOfTheRing)).contains(&quot;Hobbit&quot;, &quot;Elf&quot;).doesNotContain(&quot;Orc&quot;); </code></pre>
   *
   * @param propertyName the name of the property to be read from the elements of a {@code Iterable}. It may be a nested
   *          property (e.g. "address.street.number").
   * @return the created {@code Properties}.
   * @throws NullPointerException if the given property name is {@code null}.
   * @throws IllegalArgumentException if the given property name is empty.
   *
   * @since 3.20.0
   */
  public static Properties<Object> extractProperty(String propertyName) {
    return Assertions.extractProperty(propertyName);
  }

  /**
   * Utility method to build nicely a {@link Tuple} when working with {@link IterableAssert#extracting(String...)} or
   * {@link ObjectArrayAssert#extracting(String...)}
   *
   * @param values the values stored in the {@link Tuple}
   * @return the built {@link Tuple}
   *
   * @since 3.20.0
   */
  public static Tuple tuple(Object... values) {
    return Assertions.tuple(values);
  }

  /**
   * Globally sets whether
   * <code>{@link org.assertj.core.api.AbstractIterableAssert#extracting(String) IterableAssert#extracting(String)}</code>
   * and
   * <code>{@link org.assertj.core.api.AbstractObjectArrayAssert#extracting(String) ObjectArrayAssert#extracting(String)}</code>
   * should be allowed to extract private fields, if not and they try it fails with exception.
   *
   * @param allowExtractingPrivateFields allow private fields extraction. Default is {@value org.assertj.core.configuration.Configuration#ALLOW_EXTRACTING_PRIVATE_FIELDS}.
   *
   * @since 3.20.0
   */
  public static void setAllowExtractingPrivateFields(boolean allowExtractingPrivateFields) {
    Assertions.setAllowExtractingPrivateFields(allowExtractingPrivateFields);
  }

  /**
   * Globally sets whether the use of private fields is allowed for comparison.
   * The following (incomplete) list of methods will be impacted by this change :
   * <ul>
   * <li>
   * <code>{@link org.assertj.core.api.AbstractIterableAssert#usingElementComparatorOnFields(java.lang.String...)}</code>
   * </li>
   * <li><code>{@link org.assertj.core.api.AbstractObjectAssert#isEqualToComparingFieldByField(Object)}</code></li>
   * </ul>
   *
   * If the value is <code>false</code> and these methods try to compare private fields, it will fail with an exception.
   *
   * @param allowComparingPrivateFields allow private fields comparison. Default is {@value org.assertj.core.configuration.Configuration#ALLOW_COMPARING_PRIVATE_FIELDS}.
   *
   * @since 3.20.0
   */
  public static void setAllowComparingPrivateFields(boolean allowComparingPrivateFields) {
    Assertions.setAllowComparingPrivateFields(allowComparingPrivateFields);
  }

  /**
   * Globally sets whether the extractor considers bare-named property methods like {@code String name()}.
   * Defaults to enabled.
   * @param barenamePropertyMethods whether bare-named property methods are found
   *
   * @since 3.20.0
   */
  public static void setExtractBareNamePropertyMethods(boolean barenamePropertyMethods) {
    Assertions.setExtractBareNamePropertyMethods(barenamePropertyMethods);
  }

  // ------------------------------------------------------------------------------------------------------
  // Data utility methods : not assertions but here to have a single entry point to all AssertJ features.
  // ------------------------------------------------------------------------------------------------------

  /**
   * Only delegate to {@link MapEntry#entry(Object, Object)} so that Assertions offers a full feature entry point to
   * all
   * AssertJ features (but you can use {@link MapEntry} if you prefer).
   * <p>
   * Typical usage is to call <code>entry</code> in MapAssert <code>contains</code> assertion, see examples below :
   *
   * <pre><code class='java'> Map&lt;Ring, TolkienCharacter&gt; ringBearers = ... // init omitted
   *
   * assertThat(ringBearers).contains(entry(oneRing, frodo), entry(nenya, galadriel));</code></pre>
   * @param <K> the type of keys in the map.
   * @param <V> the type of values in the map.
   * @param key the key of the entry to create.
   * @param value the value of the entry to create.
   * @return the created {@code MapEntry}.
   *
   * @since 3.20.0
   */
  public static <K, V> MapEntry<K, V> entry(K key, V value) {
    return Assertions.entry(key, value);
  }

  /**
   * Only delegate to {@link Index#atIndex(int)} so that Assertions offers a full feature entry point to all AssertJ
   * features (but you can use {@link Index} if you prefer).
   * <p>
   * Typical usage :
   * <pre><code class='java'> List&lt;Ring&gt; elvesRings = newArrayList(vilya, nenya, narya);
   * assertThat(elvesRings).contains(vilya, atIndex(0)).contains(nenya, atIndex(1)).contains(narya, atIndex(2));</code></pre>
   *
   * @param index the value of the index.
   * @return the created {@code Index}.
   * @throws IllegalArgumentException if the given value is negative.
   *
   * @since 3.20.0
   */
  public static Index atIndex(int index) {
    return Assertions.atIndex(index);
  }

  /**
   * Builds an {@link Offset} to use with {@code isCloseTo} assertions.
   * <p>
   * Typical usage :
   * <pre><code class='java'> assertThat(0.1).isCloseTo(0.0, within(0.1));</code></pre>
   * @param value the allowed offset
   * @return the created {@code Offset}.
   * @throws NullPointerException if the given value is {@code null}.
   * @throws IllegalArgumentException if the given value is negative.
   *
   * @since 3.20.0
   */
  public static Offset<Double> within(Double value) {
    return Assertions.offset(value);
  }

  /**
   * Builds an {@link Offset} to use with real number assertions.
   * <p>
   * Typical usage :
   * <pre><code class='java'> assertThat(0.1).isEqualTo(0.0, withPrecision(0.1));</code></pre>
   * @param value the required precision
   * @return the created {@code Offset}.
   * @throws NullPointerException if the given value is {@code null}.
   * @throws IllegalArgumentException if the given value is negative.
   *
   * @since 3.20.0
   */
  public static Offset<Double> withPrecision(Double value) {
    return Assertions.offset(value);
  }

  /**
   * Builds an {@link Offset} to use with isCloseTo assertions.
   * <p>
   * Typical usage :
   * <pre><code class='java'> assertThat(8.2f).isCloseTo(8.0f, within(0.2f));</code></pre>
   *
   * @param value the allowed offset
   * @return the created {@code Offset}.
   * @throws NullPointerException if the given value is {@code null}.
   * @throws IllegalArgumentException if the given value is negative.
   *
   * @since 3.20.0
   */
  public static Offset<Float> within(Float value) {
    return Assertions.offset(value);
  }

  /**
   * Builds an {@link Offset} to use with real number assertions.
   * <p>
   * Typical usage :
   * <pre><code class='java'> assertThat(0.2f).isEqualTo(0.0f, withPrecision(0.2f));</code></pre>
   * @param value the required precision
   * @return the created {@code Offset}.
   * @throws NullPointerException if the given value is {@code null}.
   * @throws IllegalArgumentException if the given value is negative.
   *
   * @since 3.20.0
   */
  public static Offset<Float> withPrecision(Float value) {
    return Assertions.offset(value);
  }

  /**
   * Assertions entry point for BigDecimal {@link Offset} to use with isCloseTo assertions.
   * <p>
   * Typical usage :
   * <pre><code class='java'> assertThat(BigDecimal.TEN).isCloseTo(new BigDecimal("10.5"), within(BigDecimal.ONE));</code></pre>
   *
   * @param value the allowed offset
   * @return the created {@code Offset}.
   * @throws NullPointerException if the given value is {@code null}.
   * @throws IllegalArgumentException if the given value is negative.
   *
   * @since 3.20.0
   */
  public static Offset<BigDecimal> within(BigDecimal value) {
    return Assertions.within(value);
  }

  /**
   * Assertions entry point for BigInteger {@link Offset} to use with isCloseTo assertions.
   * <p>
   * Typical usage :
   * <pre><code class='java'> assertThat(BigInteger.TEN).isCloseTo(new BigInteger("11"), within(new BigInteger("2")));</code></pre>
   *
   * @param value the allowed offset
   * @return the created {@code Offset}.
   * @throws NullPointerException if the given value is {@code null}.
   * @throws IllegalArgumentException if the given value is negative.
   *
   * @since 3.20.0
   */
  public static Offset<BigInteger> within(BigInteger value) {
    return Assertions.within(value);
  }

  /**
   * Assertions entry point for Byte {@link Offset} to use with isCloseTo assertions.
   * <p>
   * Typical usage :
   * <pre><code class='java'> assertThat((byte) 10).isCloseTo((byte) 11, within((byte) 1));</code></pre>
   *
   * @param value the value of the offset.
   * @return the created {@code Offset}.
   * @throws NullPointerException if the given value is {@code null}.
   * @throws IllegalArgumentException if the given value is negative.
   *
   * @since 3.20.0
   */
  public static Offset<Byte> within(Byte value) {
    return Assertions.within(value);
  }

  /**
   * Assertions entry point for Integer {@link Offset} to use with isCloseTo assertions.
   * <p>
   * Typical usage :
   * <pre><code class='java'> assertThat(10).isCloseTo(11, within(1));</code></pre>
   *
   * @param value the value of the offset.
   * @return the created {@code Offset}.
   * @throws NullPointerException if the given value is {@code null}.
   * @throws IllegalArgumentException if the given value is negative.
   *
   * @since 3.20.0
   */
  public static Offset<Integer> within(Integer value) {
    return Assertions.within(value);
  }

  /**
   * Assertions entry point for Short {@link Offset} to use with isCloseTo assertions.
   * <p>
   * Typical usage :
   * <pre><code class='java'> assertThat(10).isCloseTo(11, within(1));</code></pre>
   *
   * @param value the allowed offset
   * @return the created {@code Offset}.
   * @throws NullPointerException if the given value is {@code null}.
   * @throws IllegalArgumentException if the given value is negative.
   *
   * @since 3.20.0
   */
  public static Offset<Short> within(Short value) {
    return Assertions.within(value);
  }

  /**
   * Assertions entry point for Long {@link Offset} to use with {@link AbstractLongAssert#isCloseTo(long, Offset) isCloseTo} assertions.
   * <p>
   * Typical usage :
   * <pre><code class='java'> assertThat(5l).isCloseTo(7l, within(2l));</code></pre>
   *
   * @param value the allowed offset
   * @return the created {@code Offset}.
   * @throws NullPointerException if the given value is {@code null}.
   * @throws IllegalArgumentException if the given value is negative.
   *
   * @since 3.20.0
   */
  public static Offset<Long> within(Long value) {
    return Assertions.within(value);
  }

  /**
   * Assertions entry point for {@link TemporalUnitOffset} with less than or equal condition
   * to use with isCloseTo temporal assertions.
   * <p>
   * Typical usage :
   * <pre><code class='java'> LocalTime _07_10 = LocalTime.of(7, 10);
   * LocalTime _07_12 = LocalTime.of(7, 12);
   * assertThat(_07_10).isCloseTo(_07_12, within(5, ChronoUnit.MINUTES));</code></pre>
   *
   * @param value the allowed offset
   * @param unit the {@link TemporalUnit} of the offset
   * @return the created {@code Offset}.
   *
   * @since 3.20.0
   */
  public static TemporalUnitOffset within(long value, TemporalUnit unit) {
    return Assertions.within(value, unit);
  }

  /**
   * Syntactic sugar method to use with {@link AbstractDurationAssert#isCloseTo(Duration, Duration)} assertion.
   * <p>
   * Example:
   * <pre><code class='java'> assertThat(Duration.ofMinutes(2)).isCloseTo(Duration.ofMinutes(3), withMarginOf(Duration.ofMinutes(1)));</code></pre>
   *
   * @param allowedDifference the allowed difference {@link Duration}.
   * @return the given value.
   *
   * @since 3.20.0
   */
  public static Duration withMarginOf(Duration allowedDifference) {
    return Assertions.withMarginOf(allowedDifference);
  }

  /**
   * Assertions entry point for Double {@link org.assertj.core.data.Percentage} to use with isCloseTo assertions for
   * percentages.
   * <p>
   * Typical usage :
   * <pre><code class='java'> assertThat(11.0).isCloseTo(10.0, withinPercentage(10.0));</code></pre>
   *
   * @param value the required precision percentage
   * @return the created {@code Percentage}.
   * @throws NullPointerException if the given value is {@code null}.
   * @throws IllegalArgumentException if the given value is negative.
   *
   * @since 3.20.0
   */
  public static Percentage withinPercentage(Double value) {
    return Assertions.withinPercentage(value);
  }

  /**
   * Assertions entry point for Integer {@link org.assertj.core.data.Percentage} to use with isCloseTo assertions for
   * percentages.
   * <p>
   * Typical usage :
   * <pre><code class='java'> assertThat(11).isCloseTo(10, withinPercentage(10));</code></pre>
   *
   * @param value the required precision percentage
   * @return the created {@code Percentage}.
   * @throws NullPointerException if the given value is {@code null}.
   * @throws IllegalArgumentException if the given value is negative.
   *
   * @since 3.20.0
   */
  public static Percentage withinPercentage(Integer value) {
    return Assertions.withinPercentage(value);
  }

  /**
   * Assertions entry point for Long {@link org.assertj.core.data.Percentage} to use with isCloseTo assertions for
   * percentages.
   * <p>
   * Typical usage :
   * <pre><code class='java'> assertThat(11L).isCloseTo(10L, withinPercentage(10L));</code></pre>
   *
   * @param value the required precision percentage
   * @return the created {@code Percentage}.
   * @throws NullPointerException if the given value is {@code null}.
   * @throws IllegalArgumentException if the given value is negative.
   *
   * @since 3.20.0
   */
  public static Percentage withinPercentage(Long value) {
    return Assertions.withinPercentage(value);
  }

  /**
   * Build a {@link Offset#strictOffset(Number) <b>strict</b> Offset} to use with {@link AbstractDoubleAssert#isCloseTo(double, Offset)} and {@link AbstractDoubleAssert#isNotCloseTo(double, Offset)} assertions.
   * <p>
   * A strict offset implies a strict comparison which means that {@code isCloseTo} will fail when <i>abs(actual - expected) == offset</i>.
   * <p>
   * Examples:
   * <pre><code class='java'> // assertion succeeds
   * assertThat(8.1).isCloseTo(8.0, byLessThan(0.2));
   *
   * // assertions fail
   * assertThat(8.1).isCloseTo(8.0, byLessThan(0.1)); // strict comparison!
   * assertThat(8.1).isCloseTo(8.0, byLessThan(0.01));</code></pre>
   *
   * @param value the value of the offset.
   * @return the created {@code Offset}.
   * @throws NullPointerException if the given value is {@code null}.
   * @throws IllegalArgumentException if the given value is negative.
   *
   * @since 3.20.0
   */
  public static Offset<Double> byLessThan(Double value) {
    return Assertions.byLessThan(value);
  }

  /**
   * Alias for {@link #offset(Float)} to use with isCloseTo assertions.
   * <p>
   * Typical usage :
   * <pre><code class='java'> assertThat(8.2f).isCloseTo(8.0f, byLessThan(0.5f));</code></pre>
   *
   * @param value the value of the offset.
   * @return the created {@code Offset}.
   * @throws NullPointerException if the given value is {@code null}.
   * @throws IllegalArgumentException if the given value is negative.
   *
   * @since 3.20.0
   */
  public static Offset<Float> byLessThan(Float value) {
    return Assertions.byLessThan(value);
  }

  /**
   * Assertions entry point for BigDecimal {@link Offset} to use with isCloseTo assertions.
   * <p>
   * Typical usage :
   * <pre><code class='java'> assertThat(BigDecimal.TEN).isCloseTo(new BigDecimal("10.5"), byLessThan(BigDecimal.ONE));</code></pre>
   *
   * @param value the value of the offset.
   * @return the created {@code Offset}.
   * @throws NullPointerException if the given value is {@code null}.
   * @throws IllegalArgumentException if the given value is negative.
   *
   * @since 3.20.0
   */
  public static Offset<BigDecimal> byLessThan(BigDecimal value) {
    return Assertions.byLessThan(value);
  }

  /**
   * Assertions entry point for BigInteger {@link Offset} to use with isCloseTo assertions.
   * <p>
   * Typical usage :
   * <pre><code class='java'> assertThat(BigInteger.TEN).isCloseTo(new BigInteger("11"), byLessThan(new BigInteger("2")));</code></pre>
   *
   * @param value the value of the offset.
   * @return the created {@code Offset}.
   * @throws NullPointerException if the given value is {@code null}.
   * @throws IllegalArgumentException if the given value is negative.
   *
   * @since 3.20.0
   */
  public static Offset<BigInteger> byLessThan(BigInteger value) {
    return Assertions.byLessThan(value);
  }

  /**
   * Assertions entry point for Byte {@link Offset} to use with isCloseTo assertions.
   * <p>
   * Typical usage :
   * <pre><code class='java'> assertThat((byte) 10).isCloseTo((byte) 11, byLessThan((byte) 2));</code></pre>
   *
   * @param value the value of the offset.
   * @return the created {@code Offset}.
   * @throws NullPointerException if the given value is {@code null}.
   * @throws IllegalArgumentException if the given value is negative.
   *
   * @since 3.20.0
   */
  public static Offset<Byte> byLessThan(Byte value) {
    return Assertions.byLessThan(value);
  }

  /**
   * Assertions entry point for Long {@link Offset} to use with strict {@link AbstractIntegerAssert#isCloseTo(int, Offset) isCloseTo} assertions.
   * <p>
   * Typical usage :
   * <pre><code class='java'> assertThat(10).isCloseTo(12, byLessThan(1));</code></pre>
   *
   * @param value the value of the offset.
   * @return the created {@code Offset}.
   * @throws NullPointerException if the given value is {@code null}.
   * @throws IllegalArgumentException if the given value is negative.
   *
   * @since 3.20.0
   */
  public static Offset<Integer> byLessThan(Integer value) {
    return Assertions.byLessThan(value);
  }

  /**
   * Assertions entry point for Short {@link Offset} to use with isCloseTo assertions.
   * <p>
   * Typical usage :
   * <pre><code class='java'> assertThat((short) 10).isCloseTo((short) 11, byLessThan((short) 2));</code></pre>
   *
   * @param value the value of the offset.
   * @return the created {@code Offset}.
   * @throws NullPointerException if the given value is {@code null}.
   * @throws IllegalArgumentException if the given value is negative.
   *
   * @since 3.20.0
   */
  public static Offset<Short> byLessThan(Short value) {
    return Assertions.byLessThan(value);
  }

  /**
   * Assertions entry point for Long {@link Offset} to use with strict {@link AbstractLongAssert#isCloseTo(long, Offset) isCloseTo} assertions.
   * <p>
   * Typical usage :
   * <pre><code class='java'> assertThat(5l).isCloseTo(7l, byLessThan(3l));</code></pre>
   *
   * @param value the value of the offset.
   * @return the created {@code Offset}.
   * @throws NullPointerException if the given value is {@code null}.
   * @throws IllegalArgumentException if the given value is negative.
   *
   * @since 3.20.0
   */
  public static Offset<Long> byLessThan(Long value) {
    return Assertions.byLessThan(value);
  }

  /**
   * Assertions entry point for {@link TemporalUnitOffset} with strict less than condition
   * to use with {@code isCloseTo} temporal assertions.
   * <p>
   * Typical usage :
   * <pre><code class='java'> LocalTime _07_10 = LocalTime.of(7, 10);
   * LocalTime _07_12 = LocalTime.of(7, 12);
   * assertThat(_07_10).isCloseTo(_07_12, byLessThan(5, ChronoUnit.MINUTES));</code></pre>
   *
   * @param value the value of the offset.
   * @param unit the {@link TemporalUnit} of the offset.
   * @return the created {@code Offset}.
   *
   * @since 3.20.0
   */
  public static TemporalUnitOffset byLessThan(long value, TemporalUnit unit) {
    return Assertions.byLessThan(value, unit);
  }

  /**
   * A syntax sugar to write fluent assertion using {@link ObjectAssert#returns(Object, Function)} and
   * {@link ObjectAssert#doesNotReturn(Object, Function)}.
   * <p>
   * Example:
   * <pre><code class="java"> Jedi yoda = new Jedi("Yoda", "Green");
   * assertThat(yoda).returns("Yoda", from(Jedi::getName))
   *                 .returns(2.4, from(Jedi::getHeight))
   *                 .doesNotReturn(null, from(Jedi::getWeight)); </code></pre>
   *
   * @param extractor A function to extract test subject's property
   * @param <F> Type of test subject
   * @param <T> Type of the property under the assertion
   * @return same instance of {@code extractor}
   *
   * @since 3.20.0
   */
  public static <F, T> Function<F, T> from(Function<F, T> extractor) {
    return Assertions.from(extractor);
  }

  /**
   * A syntax sugar to write fluent assertion with methods having an {@link InstanceOfAssertFactory} parameter.
   * <p>
   * Example:
   * <pre><code class="java"> Jedi yoda = new Jedi("Yoda", "Green");
   * assertThat(yoda).extracting(Jedi::getName, as(InstanceOfAssertFactories.STRING))
   *                 .startsWith("Yo");</code></pre>
   *
   * @param assertFactory the factory which verifies the type and creates the new {@code Assert}
   * @param <T>           the type to use for the cast.
   * @param <ASSERT>      the type of the resulting {@code Assert}
   * @return same instance of {@code assertFactory}
   *
   * @see AbstractObjectAssert#extracting(String, InstanceOfAssertFactory)
   * @see AbstractObjectAssert#extracting(Function, InstanceOfAssertFactory)
   * @see AbstractMapAssert#extractingByKey(Object, InstanceOfAssertFactory)
   * @see AbstractOptionalAssert#get(InstanceOfAssertFactory)
   * @see AbstractIterableAssert#first(InstanceOfAssertFactory)
   * @see AbstractIterableAssert#last(InstanceOfAssertFactory)
   * @see AbstractIterableAssert#element(int, InstanceOfAssertFactory)
   * @since 3.20.0
   *
   */
  public static <T, ASSERT extends AbstractAssert<?, ?>> InstanceOfAssertFactory<T, ASSERT> as(InstanceOfAssertFactory<T, ASSERT> assertFactory) {
    return Assertions.as(assertFactory);
  }
  // ------------------------------------------------------------------------------------------------------
  // Condition methods : not assertions but here to have a single entry point to all AssertJ features.
  // ------------------------------------------------------------------------------------------------------

  /**
   * Creates a new <code>{@link AllOf}</code>
   *
   * @param <T> the type of object the given condition accept.
   * @param conditions the conditions to evaluate.
   * @return the created {@code AllOf}.
   * @throws NullPointerException if the given array is {@code null}.
   * @throws NullPointerException if any of the elements in the given array is {@code null}.
   *
   * @since 3.20.0
   */
  @SafeVarargs
  public static <T> Condition<T> allOf(Condition<? super T>... conditions) {
    return Assertions.allOf(conditions);
  }

  /**
   * Creates a new <code>{@link AllOf}</code>
   *
   * @param <T> the type of object the given condition accept.
   * @param conditions the conditions to evaluate.
   * @return the created {@code AllOf}.
   * @throws NullPointerException if the given iterable is {@code null}.
   * @throws NullPointerException if any of the elements in the given iterable is {@code null}.
   *
   * @since 3.20.0
   */
  public static <T> Condition<T> allOf(Iterable<? extends Condition<? super T>> conditions) {
    return Assertions.allOf(conditions);
  }

  /**
   * Create a new <code>{@link ThrowingConsumer}</code> that delegates the evaluation of the
   * given consumers to {@link AbstractAssert#satisfies(ThrowingConsumer[])}.
   *
   * @param <T> the type of object the given consumers accept
   * @param consumers the consumers to evaluate
   * @return the {@code ThrowingConsumer} instance
   *
   * @since 3.25.0
   */
  @SafeVarargs
  public static <T> ThrowingConsumer<T> allOf(ThrowingConsumer<? super T>... consumers) {
    return Assertions.allOf(consumers);
  }

  /**
   * Only delegate to {@link AnyOf#anyOf(Condition...)} so that Assertions offers a full feature entry point to all
   * AssertJ features (but you can use {@link AnyOf} if you prefer).
   * <p>
   * Typical usage (<code>jedi</code> and <code>sith</code> are {@link Condition}) :
   *
   * <pre><code class='java'> assertThat(&quot;Vader&quot;).is(anyOf(jedi, sith));</code></pre>
   *
   * @param <T> the type of object the given condition accept.
   * @param conditions the conditions to evaluate.
   * @return the created {@code AnyOf}.
   *
   * @since 3.20.0
   */
  @SafeVarargs
  public static <T> Condition<T> anyOf(Condition<? super T>... conditions) {
    return Assertions.anyOf(conditions);
  }

  /**
   * Creates a new <code>{@link AnyOf}</code>
   *
   * @param <T> the type of object the given condition accept.
   * @param conditions the conditions to evaluate.
   * @return the created {@code AnyOf}.
   * @throws NullPointerException if the given iterable is {@code null}.
   * @throws NullPointerException if any of the elements in the given iterable is {@code null}.
   *
   * @since 3.20.0
   */
  public static <T> Condition<T> anyOf(Iterable<? extends Condition<? super T>> conditions) {
    return Assertions.anyOf(conditions);
  }

  /**
   * Create a new <code>{@link ThrowingConsumer}</code> that delegates the evaluation of the
   * given consumers to {@link AbstractAssert#satisfiesAnyOf(ThrowingConsumer[])}.
   *
   * @param <T> the type of object the given consumers accept
   * @param consumers the consumers to evaluate
   * @return the {@code ThrowingConsumer} instance
   *
   * @since 3.25.0
   */
  @SafeVarargs
  public static <T> ThrowingConsumer<T> anyOf(ThrowingConsumer<? super T>... consumers) {
    return Assertions.anyOf(consumers);
  }

  /**
   * Creates a new <code>{@link DoesNotHave}</code>.
   *
   * @param <T> the type of object the given condition accept.
   * @param condition the condition to inverse.
   * @return The DoesNotHave condition created.
   *
   * @since 3.20.0
   */
  public static <T> DoesNotHave<T> doesNotHave(Condition<? super T> condition) {
    return Assertions.doesNotHave(condition);
  }

  /**
   * Creates a new <code>{@link Not}</code>.
   *
   * @param <T> the type of object the given condition accept.
   * @param condition the condition to inverse.
   * @return The Not condition created.
   *
   * @since 3.20.0
   */
  public static <T> Not<T> not(Condition<? super T> condition) {
    return Assertions.not(condition);
  }

  // --------------------------------------------------------------------------------------------------
  // Filter methods : not assertions but here to have a single entry point to all AssertJ features.
  // --------------------------------------------------------------------------------------------------

  /**
   * Create a {@link FilterOperator} to use in {@link AbstractIterableAssert#filteredOn(String, FilterOperator)
   * filteredOn(String, FilterOperation)} to express a filter keeping all Iterable elements whose property/field
   * value matches one of the given values.
   * <p>
   * As often, an example helps:
   * <pre><code class='java'> Employee yoda   = new Employee(1L, new Name("Yoda"), 800);
   * Employee obiwan = new Employee(2L, new Name("Obiwan"), 800);
   * Employee luke   = new Employee(3L, new Name("Luke", "Skywalker"), 26);
   * Employee noname = new Employee(4L, null, 50);
   *
   * List&lt;Employee&gt; employees = newArrayList(yoda, luke, obiwan, noname);
   *
   * assertThat(employees).filteredOn("age", in(800, 26))
   *                      .containsOnly(yoda, obiwan, luke);</code></pre>
   *
   * @param values values to match (one match is sufficient)
   * @return the created "in" filter
   *
   * @since 3.20.0
   */
  public static InFilter in(Object... values) {
    return Assertions.in(values);
  }

  /**
   * Create a {@link FilterOperator} to use in {@link AbstractIterableAssert#filteredOn(String, FilterOperator)
   * filteredOn(String, FilterOperation)} to express a filter keeping all Iterable elements whose property/field
   * value matches does not match any of the given values.
   * <p>
   * As often, an example helps:
   * <pre><code class='java'> Employee yoda   = new Employee(1L, new Name("Yoda"), 800);
   * Employee obiwan = new Employee(2L, new Name("Obiwan"), 800);
   * Employee luke   = new Employee(3L, new Name("Luke", "Skywalker"), 26);
   * Employee noname = new Employee(4L, null, 50);
   *
   * List&lt;Employee&gt; employees = newArrayList(yoda, luke, obiwan, noname);
   *
   * assertThat(employees).filteredOn("age", notIn(800, 50))
   *                      .containsOnly(luke);</code></pre>
   *
   * @param valuesNotToMatch values not to match (none of the values must match)
   * @return the created "not in" filter
   *
   * @since 3.20.0
   */
  public static NotInFilter notIn(Object... valuesNotToMatch) {
    return Assertions.notIn(valuesNotToMatch);
  }

  /**
   * Create a {@link FilterOperator} to use in {@link AbstractIterableAssert#filteredOn(String, FilterOperator)
   * filteredOn(String, FilterOperation)} to express a filter keeping all Iterable elements whose property/field
   * value matches does not match the given value.
   * <p>
   * As often, an example helps:
   * <pre><code class='java'> Employee yoda   = new Employee(1L, new Name("Yoda"), 800);
   * Employee obiwan = new Employee(2L, new Name("Obiwan"), 800);
   * Employee luke   = new Employee(3L, new Name("Luke", "Skywalker"), 26);
   * Employee noname = new Employee(4L, null, 50);
   *
   * List&lt;Employee&gt; employees = newArrayList(yoda, luke, obiwan, noname);
   *
   * assertThat(employees).filteredOn("age", not(800))
   *                      .containsOnly(luke, noname);</code></pre>
   *
   * @param valueNotToMatch the value not to match
   * @return the created "not" filter
   *
   * @since 3.20.0
   */
  public static NotFilter not(Object valueNotToMatch) {
    return Assertions.not(valueNotToMatch);
  }

  // --------------------------------------------------------------------------------------------------
  // File methods : not assertions but here to have a single entry point to all AssertJ features.
  // --------------------------------------------------------------------------------------------------

  /**
   * Loads the text content of a file, so that it can be passed to {@link #assertThat(String)}.
   * <p>
   * Note that this will load the entire file in memory; for larger files, there might be a more efficient alternative
   * with {@link #assertThat(File)}.
   * </p>
   *
   * @param file the file.
   * @param charset the character set to use.
   * @return the content of the file.
   * @throws NullPointerException if the given charset is {@code null}.
   * @throws UncheckedIOException if an I/O exception occurs.
   *
   * @since 3.20.0
   */
  public static String contentOf(File file, Charset charset) {
    return Assertions.contentOf(file, charset);
  }

  /**
   * Loads the text content of a file, so that it can be passed to {@link #assertThat(String)}.
   * <p>
   * Note that this will load the entire file in memory; for larger files, there might be a more efficient alternative
   * with {@link #assertThat(File)}.
   * </p>
   *
   * @param file the file.
   * @param charsetName the name of the character set to use.
   * @return the content of the file.
   * @throws IllegalArgumentException if the given character set is not supported on this platform.
   * @throws UncheckedIOException if an I/O exception occurs.
   *
   * @since 3.20.0
   */
  public static String contentOf(File file, String charsetName) {
    return Assertions.contentOf(file, charsetName);
  }

  /**
   * Loads the text content of a file with the default character set, so that it can be passed to
   * {@link #assertThat(String)}.
   * <p>
   * Note that this will load the entire file in memory; for larger files, there might be a more efficient alternative
   * with {@link #assertThat(File)}.
   * </p>
   *
   * @param file the file.
   * @return the content of the file.
   * @throws UncheckedIOException if an I/O exception occurs.
   *
   * @since 3.20.0
   */
  public static String contentOf(File file) {
    return Assertions.contentOf(file, Charset.defaultCharset());
  }

  /**
   * Loads the text content of a file into a list of strings with the default charset, each string corresponding to a
   * line.
   * The line endings are either \n, \r or \r\n.
   *
   * @param file the file.
   * @return the content of the file.
   * @throws NullPointerException if the given charset is {@code null}.
   * @throws UncheckedIOException if an I/O exception occurs.
   *
   * @since 3.20.0
   */
  public static List<String> linesOf(File file) {
    return Assertions.linesOf(file, Charset.defaultCharset());
  }

  /**
   * Loads the text content of a file into a list of strings, each string corresponding to a line.
   * The line endings are either \n, \r or \r\n.
   *
   * @param file the file.
   * @param charset the character set to use.
   * @return the content of the file.
   * @throws NullPointerException if the given charset is {@code null}.
   * @throws UncheckedIOException if an I/O exception occurs.
   *
   * @since 3.20.0
   */
  public static List<String> linesOf(File file, Charset charset) {
    return Assertions.linesOf(file, charset);
  }

  /**
   * Loads the text content of a file into a list of strings, each string corresponding to a line. The line endings are
   * either \n, \r or \r\n.
   *
   * @param file the file.
   * @param charsetName the name of the character set to use.
   * @return the content of the file.
   * @throws NullPointerException if the given charset is {@code null}.
   * @throws UncheckedIOException if an I/O exception occurs.
   *
   * @since 3.20.0
   */
  public static List<String> linesOf(File file, String charsetName) {
    return Assertions.linesOf(file, charsetName);
  }

  /**
   * Loads the text content of a file at a given path into a list of strings with the default charset, each string corresponding to a
   * line.
   * The line endings are either \n, \r or \r\n.
   *
   * @param path the path.
   * @return the content of the file at the given path.
   * @throws NullPointerException if the given charset is {@code null}.
   * @throws UncheckedIOException if an I/O exception occurs.
   *
   * @since 3.23.0
   */
  public static List<String> linesOf(Path path) {
    return Assertions.linesOf(path, Charset.defaultCharset());
  }

  /**
   * Loads the text content of a file at a given path into a list of strings, each string corresponding to a line.
   * The line endings are either \n, \r or \r\n.
   *
   * @param path the path.
   * @param charset the character set to use.
   * @return the content of the file at the given path.
   * @throws NullPointerException if the given charset is {@code null}.
   * @throws UncheckedIOException if an I/O exception occurs.
   *
   * @since 3.23.0
   */
  public static List<String> linesOf(Path path, Charset charset) {
    return Assertions.linesOf(path, charset);
  }

  /**
   * Loads the text content of a file at a given path into a list of strings, each string corresponding to a line. The line endings are
   * either \n, \r or \r\n.
   *
   * @param path the path.
   * @param charsetName the name of the character set to use.
   * @return the content of the file at the given path.
   * @throws NullPointerException if the given charset is {@code null}.
   * @throws UncheckedIOException if an I/O exception occurs.
   *
   * @since 3.23.0
   */
  public static List<String> linesOf(Path path, String charsetName) {
    return Assertions.linesOf(path, charsetName);
  }

  // --------------------------------------------------------------------------------------------------
  // URL/Resource methods : not assertions but here to have a single entry point to all AssertJ features.
  // --------------------------------------------------------------------------------------------------

  /**
   * Loads the text content of a URL, so that it can be passed to {@link #assertThat(String)}.
   * <p>
   * Note that this will load the entire contents in memory.
   * </p>
   *
   * @param url the URL.
   * @param charset the character set to use.
   * @return the content of the URL.
   * @throws NullPointerException if the given charset is {@code null}.
   * @throws UncheckedIOException if an I/O exception occurs.
   *
   * @since 3.20.0
   */
  public static String contentOf(URL url, Charset charset) {
    return Assertions.contentOf(url, charset);
  }

  /**
   * Loads the text content of a URL, so that it can be passed to {@link #assertThat(String)}.
   * <p>
   * Note that this will load the entire contents in memory.
   * </p>
   *
   * @param url the URL.
   * @param charsetName the name of the character set to use.
   * @return the content of the URL.
   * @throws IllegalArgumentException if the given character set is not supported on this platform.
   * @throws UncheckedIOException if an I/O exception occurs.
   *
   * @since 3.20.0
   */
  public static String contentOf(URL url, String charsetName) {
    return Assertions.contentOf(url, charsetName);
  }

  /**
   * Loads the text content of a URL with the default character set, so that it can be passed to
   * {@link #assertThat(String)}.
   * <p>
   * Note that this will load the entire file in memory; for larger files.
   * </p>
   *
   * @param url the URL.
   * @return the content of the file.
   * @throws UncheckedIOException if an I/O exception occurs.
   *
   * @since 3.20.0
   */
  public static String contentOf(URL url) {
    return Assertions.contentOf(url, Charset.defaultCharset());
  }

  /**
   * Loads the text content of a URL into a list of strings with the default charset, each string corresponding to a
   * line.
   * The line endings are either \n, \r or \r\n.
   *
   * @param url the URL.
   * @return the content of the file.
   * @throws NullPointerException if the given charset is {@code null}.
   * @throws UncheckedIOException if an I/O exception occurs.
   *
   * @since 3.20.0
   */
  public static List<String> linesOf(URL url) {
    return Assertions.linesOf(url, Charset.defaultCharset());
  }

  /**
   * Loads the text content of a URL into a list of strings, each string corresponding to a line.
   * The line endings are either \n, \r or \r\n.
   *
   * @param url the URL.
   * @param charset the character set to use.
   * @return the content of the file.
   * @throws NullPointerException if the given charset is {@code null}.
   * @throws UncheckedIOException if an I/O exception occurs.
   *
   * @since 3.20.0
   */
  public static List<String> linesOf(URL url, Charset charset) {
    return Assertions.linesOf(url, charset);
  }

  /**
   * Loads the text content of a URL into a list of strings, each string corresponding to a line. The line endings are
   * either \n, \r or \r\n.
   *
   * @param url the URL.
   * @param charsetName the name of the character set to use.
   * @return the content of the file.
   * @throws NullPointerException if the given charset is {@code null}.
   * @throws UncheckedIOException if an I/O exception occurs.
   *
   * @since 3.20.0
   */
  public static List<String> linesOf(URL url, String charsetName) {
    return Assertions.linesOf(url, charsetName);
  }

  // --------------------------------------------------------------------------------------------------
  // Date formatting methods : not assertions but here to have a single entry point to all AssertJ features.
  // --------------------------------------------------------------------------------------------------

  /**
   * Instead of using default strict date/time parsing, it is possible to use lenient parsing mode for default date
   * formats parser to interpret inputs that do not precisely match supported date formats (lenient parsing).
   * <p>
   * With strict parsing, inputs must match exactly date/time format.
   *
   * <p>
   * Example:
   * <pre><code class='java'> final Date date = Dates.parse("2001-02-03");
   * final Date dateTime = parseDatetime("2001-02-03T04:05:06");
   * final Date dateTimeWithMs = parseDatetimeWithMs("2001-02-03T04:05:06.700");
   *
   * Assertions.setLenientDateParsing(true);
   *
   * // assertions will pass
   * assertThat(date).isEqualTo("2001-01-34");
   * assertThat(date).isEqualTo("2001-02-02T24:00:00");
   * assertThat(date).isEqualTo("2001-02-04T-24:00:00.000");
   * assertThat(dateTime).isEqualTo("2001-02-03T04:05:05.1000");
   * assertThat(dateTime).isEqualTo("2001-02-03T04:04:66");
   * assertThat(dateTimeWithMs).isEqualTo("2001-02-03T04:05:07.-300");
   *
   * // assertions will fail
   * assertThat(date).hasSameTimeAs("2001-02-04"); // different date
   * assertThat(dateTime).hasSameTimeAs("2001-02-03 04:05:06"); // leniency does not help here</code></pre>
   *
   * To revert to default strict date parsing, call {@code setLenientDateParsing(false)}.
   *
   * @param value whether lenient parsing mode should be enabled or not
   *
   * @since 3.20.0
   */
  public static void setLenientDateParsing(boolean value) {
    Assertions.setLenientDateParsing(value);
  }

  /**
   * Add the given date format to the ones used to parse date String in String based Date assertions like
   * {@link org.assertj.core.api.AbstractDateAssert#isEqualTo(String)}.
   * <p>
   * User date formats are used before default ones in the order they have been registered (first registered, first
   * used).
   * <p>
   * AssertJ is gonna use any date formats registered with one of these methods :
   * <ul>
   * <li>{@link org.assertj.core.api.AbstractDateAssert#withDateFormat(String)}</li>
   * <li>{@link org.assertj.core.api.AbstractDateAssert#withDateFormat(java.text.DateFormat)}</li>
   * <li>{@link #registerCustomDateFormat(java.text.DateFormat)}</li>
   * <li>{@link #registerCustomDateFormat(String)}</li>
   * </ul>
   * <p>
   * Beware that AssertJ will use the newly registered format for <b>all remaining Date assertions in the test suite</b>
   * <p>
   * To revert to default formats only, call {@link #useDefaultDateFormatsOnly()} or
   * {@link org.assertj.core.api.AbstractDateAssert#withDefaultDateFormatsOnly()}.
   * <p>
   * Code examples:
   * <pre><code class='java'> Date date = ... // set to 2003 April the 26th
   * assertThat(date).isEqualTo("2003-04-26");
   *
   * try {
   *   // date with a custom format : failure since the default formats don't match.
   *   assertThat(date).isEqualTo("2003/04/26");
   * } catch (AssertionError e) {
   *   assertThat(e).hasMessage("Failed to parse 2003/04/26 with any of these date formats: " +
   *                            "[yyyy-MM-dd'T'HH:mm:ss.SSSX, yyyy-MM-dd'T'HH:mm:ss.SSS, " +
   *                            "yyyy-MM-dd'T'HH:mm:ssX, " +
   *                            "yyyy-MM-dd'T'HH:mm:ss, yyyy-MM-dd]");
   * }
   *
   * // registering a custom date format to make the assertion pass
   * registerCustomDateFormat(new SimpleDateFormat("yyyy/MM/dd")); // registerCustomDateFormat("yyyy/MM/dd") would work to.
   * assertThat(date).isEqualTo("2003/04/26");
   *
   * // the default formats are still available and should work
   * assertThat(date).isEqualTo("2003-04-26");</code></pre>
   *
   * @param userCustomDateFormat the new Date format used for String based Date assertions.
   *
   * @since 3.20.0
   */
  public static void registerCustomDateFormat(DateFormat userCustomDateFormat) {
    Assertions.registerCustomDateFormat(userCustomDateFormat);
  }

  /**
   * Add the given date format to the ones used to parse date String in String based Date assertions like
   * {@link org.assertj.core.api.AbstractDateAssert#isEqualTo(String)}.
   * <p>
   * User date formats are used before default ones in the order they have been registered (first registered, first
   * used).
   * <p>
   * AssertJ is gonna use any date formats registered with one of these methods :
   * <ul>
   * <li>{@link org.assertj.core.api.AbstractDateAssert#withDateFormat(String)}</li>
   * <li>{@link org.assertj.core.api.AbstractDateAssert#withDateFormat(java.text.DateFormat)}</li>
   * <li>{@link #registerCustomDateFormat(java.text.DateFormat)}</li>
   * <li>{@link #registerCustomDateFormat(String)}</li>
   * </ul>
   * <p>
   * Beware that AssertJ will use the newly registered format for <b>all remaining Date assertions in the test suite</b>.
   * <p>
   * To revert to default formats only, call {@link #useDefaultDateFormatsOnly()} or
   * {@link org.assertj.core.api.AbstractDateAssert#withDefaultDateFormatsOnly()}.
   * <p>
   * Code examples:
   * <pre><code class='java'> Date date = ... // set to 2003 April the 26th
   * assertThat(date).isEqualTo("2003-04-26");
   *
   * try {
   *   // date with a custom format : failure since the default formats don't match.
   *   assertThat(date).isEqualTo("2003/04/26");
   * } catch (AssertionError e) {
   *   assertThat(e).hasMessage("Failed to parse 2003/04/26 with any of these date formats: " +
   *                            "[yyyy-MM-dd'T'HH:mm:ss.SSSX, yyyy-MM-dd'T'HH:mm:ss.SSS, " +
   *                            "yyyy-MM-dd'T'HH:mm:ssX, " +
   *                            "yyyy-MM-dd'T'HH:mm:ss, yyyy-MM-dd]");
   * }
   *
   * // registering a custom date format to make the assertion pass
   * registerCustomDateFormat("yyyy/MM/dd");
   * assertThat(date).isEqualTo("2003/04/26");
   *
   * // the default formats are still available and should work
   * assertThat(date).isEqualTo("2003-04-26");</code></pre>
   *
   * @param userCustomDateFormatPattern the new Date format pattern used for String based Date assertions.
   *
   * @since 3.20.0
   */
  public static void registerCustomDateFormat(String userCustomDateFormatPattern) {
    Assertions.registerCustomDateFormat(userCustomDateFormatPattern);
  }

  /**
   * Remove all registered custom date formats =&gt; use only the defaults date formats to parse string as date.
   * <p>
   * Beware that the default formats are expressed in the current local timezone.
   * <p>
   * Defaults date format are:
   * <ul>
   * <li><code>yyyy-MM-dd HH:mm:ss.SSSX</code></li>
   * <li><code>yyyy-MM-dd'T'HH:mm:ss.SSS</code></li>
   * <li><code>yyyy-MM-dd HH:mm:ss.SSS</code> (for {@link java.sql.Timestamp} String representation support)</li>
   * <li><code>yyyy-MM-dd'T'HH:mm:ssX</code></li>
   * <li><code>yyyy-MM-dd'T'HH:mm:ss</code></li>
   * <li><code>yyyy-MM-dd</code></li>
   * </ul>
   * <p>
   * Example of valid string date representations:
   * <ul>
   * <li><code>2003-04-26T03:01:02.999</code></li>
   * <li><code>2003-04-26 03:01:02.999</code></li>
   * <li><code>2003-04-26T13:01:02</code></li>
   * <li><code>2003-04-26</code></li>
   * </ul>
   *
   * @since 3.20.0
   */
  public static void useDefaultDateFormatsOnly() {
    Assertions.useDefaultDateFormatsOnly();
  }

  /**
   * Register a {@link Representation} that will be used in all following assertions.
   * <p>
   * {@link Representation} are used to format types in assertions error messages.
   * <p>
   * An alternative way of using a different representation is to register one as a service,
   * this approach is described in {@link Representation}, it requires more work than this method
   * but has the advantage of not having to do anything in your tests and it would be applied to all the tests globally
   * <p>
   * Example :
   * <pre><code class='java'> private class Example {}
   *
   * private class CustomRepresentation extends StandardRepresentation {
   *
   *   // override needed to hook specific formatting
   *   {@literal @}Override
   *   public String toStringOf(Object o) {
   *     if (o instanceof Example) return "Example";
   *     // fallback to default formatting.
   *     return super.toStringOf(o);
   *   }
   *
   *   // change String representation
   *   {@literal @}Override
   *   protected String toStringOf(String s) {
   *     return "$" + s + "$";
   *   }
   * }
   *
   * Assertions.useRepresentation(new CustomRepresentation());
   *
   * // this assertion fails ...
   * assertThat(new Example()).isNull();
   * // ... with error :
   * // "expected:&lt;[null]&gt; but was:&lt;[Example]&gt;"
   *
   * // this one fails ...
   * assertThat("foo").startsWith("bar");
   * // ... with error :
   * // Expecting:
   * //   &lt;$foo$&gt;
   * // to start with:
   * //   &lt;$bar$&gt;</code></pre>
   *
   * @param customRepresentation the {@link Representation} to use
   *
   * @since 3.20.0
   */
  public static void useRepresentation(Representation customRepresentation) {
    Assertions.useRepresentation(customRepresentation);
  }

  /**
   * Assertions error messages uses a {@link Representation} to format the different types involved, using this method
   * you can control the formatting of a given type by providing a specific formatter.
   *
   *
   * <p>
   * Registering a formatter makes it available for all AssertJ {@link Representation}:
   * <ul>
   * <li>{@link StandardRepresentation}</li>
   * <li>{@link UnicodeRepresentation}</li>
   * <li>{@link HexadecimalRepresentation}</li>
   * <li>{@link BinaryRepresentation}</li>
   * </ul>
   * <p>
   * Example :
   * <pre><code class='java'> // without specific formatter
   * assertThat(STANDARD_REPRESENTATION.toStringOf(123L)).isEqualTo("123L");
   *
   * // register a formatter for Long
   * Assertions.registerFormatterForType(Long.class, value -&gt; "$" + value + "$");
   *
   * // now Long will be formatted between in $$ in error message.
   * assertThat(STANDARD_REPRESENTATION.toStringOf(longNumber)).isEqualTo("$123$");
   *
   * // fails with error : expected:&lt;$456$&gt; but was:&lt;$123$&gt;
   * assertThat(123L).isEqualTo(456L);</code></pre>
   *
   * @param <T> the type of format.
   * @param type the class of the type to format
   * @param formatter the formatter {@link Function}
   *
   * @since 3.5.0
   */
  public static <T> void registerFormatterForType(Class<T> type, Function<T, String> formatter) {
    Assertions.registerFormatterForType(type, formatter);
  }

  /**
   * Fallback to use {@link StandardRepresentation} to revert the effect of calling {@link #useRepresentation(Representation)}.
   *
   * @since 3.20.0
   */
  public static void useDefaultRepresentation() {
    Assertions.useDefaultRepresentation();
  }
}
