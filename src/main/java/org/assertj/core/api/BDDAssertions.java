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

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URI;
import java.net.URL;
import java.nio.file.Path;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.OffsetTime;
import java.time.Period;
import java.time.ZonedDateTime;
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
import java.util.function.DoublePredicate;
import java.util.function.IntPredicate;
import java.util.function.LongPredicate;
import java.util.function.Predicate;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import java.util.stream.Stream;

import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
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
   * Creates a new instance of <code>{@link org.assertj.core.api.ClassAssert}</code>
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
   * Creates a new instance of <code>{@link FactoryBasedNavigableIterableAssert}</code> allowing to navigate to any {@code Iterable} element
   * in order to perform assertions on it.
   * <p>
   * Navigational methods provided:<ul>
   * <li>{@link AbstractIterableAssert#first() first()}</li>
   * <li>{@link AbstractIterableAssert#last() last()}</li>
   * <li>{@link AbstractIterableAssert#element(int) element(index)}</li>
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
   */
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
   */
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
   */
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
   */
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
   * @param actual the actual value.
   * @return the created assertion Throwable.
   */
  public static AbstractThrowableAssert<?, ? extends Throwable> then(Throwable actual) {
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
   * To use a test description, use {@link #catchThrowable(ThrowableAssert.ThrowingCallable)} as shown below:
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
   * To use a test description, use {@link #catchThrowable(ThrowableAssert.ThrowingCallable)} as shown below.
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
   * Creates a new instance of <code>{@link org.assertj.core.api.LocalDateAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   */
  public static AbstractLocalDateAssert<?> then(LocalDate actual) {
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
   *
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
   * Entry point to check that an exception of type T is thrown by a given {@code throwingCallable}
   * which allows to chain assertions on the thrown exception.
   * <p>
   * Example:
   * <pre><code class='java'> thenExceptionOfType(IOException.class)
   *           .isThrownBy(() -&gt; { throw new IOException("boom!"); })
   *           .withMessage("boom!"); </code></pre>
   *
   * This method is more or less the same of {@link #thenThrownBy(ThrowableAssert.ThrowingCallable)} but in a more natural way.
   * @param <T> the exception type.
   * @param exceptionType the exception type class.
   * @return the created {@link ThrowableTypeAssert}.
   */
  public static <T extends Throwable> ThrowableTypeAssert<T> thenExceptionOfType(final Class<? extends T> exceptionType) {
    return assertThatExceptionOfType(exceptionType);
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

}
