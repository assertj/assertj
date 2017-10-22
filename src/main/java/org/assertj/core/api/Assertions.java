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

import static org.assertj.core.configuration.ConfigurationProvider.CONFIGURATION_PROVIDER;
import static org.assertj.core.data.Percentage.withPercentage;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URI;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.OffsetTime;
import java.time.ZonedDateTime;
import java.time.temporal.TemporalUnit;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.OptionalDouble;
import java.util.OptionalInt;
import java.util.OptionalLong;
import java.util.concurrent.CompletableFuture;
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
import java.util.function.DoublePredicate;
import java.util.function.Function;
import java.util.function.IntPredicate;
import java.util.function.LongPredicate;
import java.util.function.Predicate;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import java.util.stream.Stream;

import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.assertj.core.api.exception.RuntimeIOException;
import org.assertj.core.api.filter.FilterOperator;
import org.assertj.core.api.filter.Filters;
import org.assertj.core.api.filter.InFilter;
import org.assertj.core.api.filter.NotFilter;
import org.assertj.core.api.filter.NotInFilter;
import org.assertj.core.condition.AllOf;
import org.assertj.core.condition.AnyOf;
import org.assertj.core.condition.DoesNotHave;
import org.assertj.core.condition.Not;
import org.assertj.core.data.Index;
import org.assertj.core.data.MapEntry;
import org.assertj.core.data.Offset;
import org.assertj.core.data.Percentage;
import org.assertj.core.data.TemporalUnitLessThanOffset;
import org.assertj.core.data.TemporalUnitOffset;
import org.assertj.core.data.TemporalUnitWithinOffset;
import org.assertj.core.groups.Properties;
import org.assertj.core.groups.Tuple;
import org.assertj.core.presentation.BinaryRepresentation;
import org.assertj.core.presentation.HexadecimalRepresentation;
import org.assertj.core.presentation.Representation;
import org.assertj.core.presentation.StandardRepresentation;
import org.assertj.core.presentation.UnicodeRepresentation;
import org.assertj.core.util.CheckReturnValue;
import org.assertj.core.util.Files;
import org.assertj.core.util.URLs;
import org.assertj.core.util.introspection.FieldSupport;

/**
 * Entry point for assertion methods for different types. Each method in this class is a static factory for a
 * type-specific assertion object.
 * <p>
 * For example:
 *
 * <pre><code class='java'> int removed = employees.removeFired();
 * {@link Assertions#assertThat(int) assertThat}(removed).{@link IntegerAssert#isZero isZero}();
 *
 * List&lt;Employee&gt; newEmployees = employees.hired(TODAY);
 * {@link Assertions#assertThat(Iterable) assertThat}(newEmployees).{@link IterableAssert#hasSize(int) hasSize}(6);</code></pre>
 * <p>
 * This class only contains all <code>assertThat</code> methods, if you have ambiguous method compilation error, use either {@link AssertionsForClassTypes} or {@link AssertionsForInterfaceTypes}
 * and if you need both, fully qualify you assertThat method.
 * <p>
 * Java 8 is picky when choosing the right <code>assertThat</code> method if the object under test is generic and bounded,
 * for example if foo is instance of T that extends Exception, java 8  will complain that it can't resolve
 * the proper <code>assertThat</code> method (normally <code>assertThat(Throwable)</code> as foo might implement an interface like List,
 * if that occurred <code>assertThat(List)</code> would also be a possible choice - thus confusing java 8.
 * <p>
 * This why {@link Assertions} have been split in {@link AssertionsForClassTypes} and {@link AssertionsForInterfaceTypes}
 * (see http://stackoverflow.com/questions/29499847/ambiguous-method-in-java-8-why).
 *
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
 */
@CheckReturnValue
public class Assertions {

  /**
   * Create assertion for {@link Predicate}.
   *
   * @param actual the actual value.
   * @param <T> the type of the value contained in the {@link Predicate}.
   * @return the created assertion object.
   *
   * @since 3.5.0
   */
  @CheckReturnValue
  public static <T> PredicateAssert<T> assertThat(Predicate<T> actual) {
    return AssertionsForInterfaceTypes.assertThat(actual);
  }

  /**
   * Create assertion for {@link IntPredicate}.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   *
   * @since 3.5.0
   */
  @CheckReturnValue
  public static IntPredicateAssert assertThat(IntPredicate actual) {
    return AssertionsForInterfaceTypes.assertThat(actual);
  }

  /**
   * Create assertion for {@link LongPredicate}.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   *
   * @since 3.5.0
   */
  @CheckReturnValue
  public static LongPredicateAssert assertThat(LongPredicate actual) {
    return AssertionsForInterfaceTypes.assertThat(actual);
  }

  /**
   * Create assertion for {@link DoublePredicate}.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   *
   * @since 3.5.0
   */
  @CheckReturnValue
  public static DoublePredicateAssert assertThat(DoublePredicate actual) {
    return AssertionsForInterfaceTypes.assertThat(actual);
  }

  /**
   * Create assertion for {@link java.util.concurrent.CompletableFuture}.
   *
   * @param actual the actual value.
   * @param <RESULT> the type of the value contained in the {@link java.util.concurrent.CompletableFuture}.
   *
   * @return the created assertion object.
   */
  @CheckReturnValue
  public static <RESULT> CompletableFutureAssert<RESULT> assertThat(CompletableFuture<RESULT> actual) {
    return AssertionsForClassTypes.assertThat(actual);
  }

  /**
   * Create assertion for {@link java.util.Optional}.
   *
   * @param actual the actual value.
   * @param <VALUE> the type of the value contained in the {@link java.util.Optional}.
   *
   * @return the created assertion object.
   */
  @CheckReturnValue
  public static <VALUE> OptionalAssert<VALUE> assertThat(Optional<VALUE> actual) {
    return AssertionsForClassTypes.assertThat(actual);
  }

  /**
   * Create assertion for {@link java.util.OptionalDouble}.
   *
   * @param actual the actual value.
   *
   * @return the created assertion object.
   */
  @CheckReturnValue
  public static OptionalDoubleAssert assertThat(OptionalDouble actual) {
    return AssertionsForClassTypes.assertThat(actual);
  }

  /**
   * Create assertion for {@link java.util.OptionalInt}.
   *
   * @param actual the actual value.
   *
   * @return the created assertion object.
   */
  @CheckReturnValue
  public static OptionalIntAssert assertThat(OptionalInt actual) {
    return AssertionsForClassTypes.assertThat(actual);
  }

  /**
   * Create assertion for {@link java.util.OptionalInt}.
   *
   * @param actual the actual value.
   *
   * @return the created assertion object.
   */
  @CheckReturnValue
  public static OptionalLongAssert assertThat(OptionalLong actual) {
    return AssertionsForClassTypes.assertThat(actual);
  }

  /**
   * Creates a new instance of <code>{@link BigDecimalAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   */
  @CheckReturnValue
  public static AbstractBigDecimalAssert<?> assertThat(BigDecimal actual) {
    return AssertionsForClassTypes.assertThat(actual);
  }

  /**
   * Creates a new instance of <code>{@link BigIntegerAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   * @since 2.7.0 / 3.7.0
   */
  @CheckReturnValue
  public static AbstractBigIntegerAssert<?> assertThat(BigInteger actual) {
    return new BigIntegerAssert(actual);
  }

  /**
   * Creates a new instance of <code>{@link UriAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   */
  @CheckReturnValue
  public static AbstractUriAssert<?> assertThat(URI actual) {
    return AssertionsForClassTypes.assertThat(actual);
  }

  /**
   * Creates a new instance of <code>{@link UrlAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   */
  @CheckReturnValue
  public static AbstractUrlAssert<?> assertThat(URL actual) {
    return AssertionsForClassTypes.assertThat(actual);
  }

  /**
   * Creates a new instance of <code>{@link BooleanAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   */
  @CheckReturnValue
  public static AbstractBooleanAssert<?> assertThat(boolean actual) {
    return AssertionsForClassTypes.assertThat(actual);
  }

  /**
   * Creates a new instance of <code>{@link BooleanAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   */
  @CheckReturnValue
  public static AbstractBooleanAssert<?> assertThat(Boolean actual) {
    return AssertionsForClassTypes.assertThat(actual);
  }

  /**
   * Creates a new instance of <code>{@link BooleanArrayAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   */
  @CheckReturnValue
  public static AbstractBooleanArrayAssert<?> assertThat(boolean[] actual) {
    return AssertionsForClassTypes.assertThat(actual);
  }

  /**
   * Creates a new instance of <code>{@link ByteAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   */
  @CheckReturnValue
  public static AbstractByteAssert<?> assertThat(byte actual) {
    return AssertionsForClassTypes.assertThat(actual);
  }

  /**
   * Creates a new instance of <code>{@link ByteAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   */
  @CheckReturnValue
  public static AbstractByteAssert<?> assertThat(Byte actual) {
    return AssertionsForClassTypes.assertThat(actual);
  }

  /**
   * Creates a new instance of <code>{@link ByteArrayAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   */
  @CheckReturnValue
  public static AbstractByteArrayAssert<?> assertThat(byte[] actual) {
    return AssertionsForClassTypes.assertThat(actual);
  }

  /**
   * Creates a new instance of <code>{@link CharacterAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   */
  @CheckReturnValue
  public static AbstractCharacterAssert<?> assertThat(char actual) {
    return AssertionsForClassTypes.assertThat(actual);
  }

  /**
   * Creates a new instance of <code>{@link CharArrayAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   */
  @CheckReturnValue
  public static AbstractCharArrayAssert<?> assertThat(char[] actual) {
    return AssertionsForClassTypes.assertThat(actual);
  }

  /**
   * Creates a new instance of <code>{@link CharacterAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   */
  @CheckReturnValue
  public static AbstractCharacterAssert<?> assertThat(Character actual) {
    return AssertionsForClassTypes.assertThat(actual);
  }

  /**
   * Creates a new instance of <code>{@link ClassAssert}</code>
   *
   * @param actual the actual value.
   * @return the created assertion object.
   */
  @CheckReturnValue
  public static AbstractClassAssert<?> assertThat(Class<?> actual) {
    return AssertionsForClassTypes.assertThat(actual);
  }

  /**
   * Creates a new instance of <code>{@link DoubleAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   */
  @CheckReturnValue
  public static AbstractDoubleAssert<?> assertThat(double actual) {
    return AssertionsForClassTypes.assertThat(actual);
  }

  /**
   * Creates a new instance of <code>{@link DoubleAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   */
  @CheckReturnValue
  public static AbstractDoubleAssert<?> assertThat(Double actual) {
    return AssertionsForClassTypes.assertThat(actual);
  }

  /**
   * Creates a new instance of <code>{@link DoubleArrayAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   */
  @CheckReturnValue
  public static AbstractDoubleArrayAssert<?> assertThat(double[] actual) {
    return AssertionsForClassTypes.assertThat(actual);
  }

  /**
   * Creates a new instance of <code>{@link FileAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   */
  @CheckReturnValue
  public static AbstractFileAssert<?> assertThat(File actual) {
    return AssertionsForClassTypes.assertThat(actual);
  }

  /**
   * Create assertion for {@link java.util.concurrent.Future}.
   *
   * @param actual the actual value.
   * @param <RESULT> the type of the value contained in the {@link java.util.concurrent.Future}.
   *
   * @return the created assertion object.
   * @since 2.7.0 / 3.7.0
   */
  @CheckReturnValue
  public static <RESULT> FutureAssert<RESULT> assertThat(Future<RESULT> actual) {
    return new FutureAssert<>(actual);
  }

  /**
   * Creates a new instance of <code>{@link InputStreamAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   */
  @CheckReturnValue
  public static AbstractInputStreamAssert<?, ? extends InputStream> assertThat(InputStream actual) {
    return AssertionsForClassTypes.assertThat(actual);
  }

  /**
   * Creates a new instance of <code>{@link FloatAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   */
  @CheckReturnValue
  public static AbstractFloatAssert<?> assertThat(float actual) {
    return AssertionsForClassTypes.assertThat(actual);
  }

  /**
   * Creates a new instance of <code>{@link FloatAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   */
  @CheckReturnValue
  public static AbstractFloatAssert<?> assertThat(Float actual) {
    return AssertionsForClassTypes.assertThat(actual);
  }

  /**
   * Creates a new instance of <code>{@link FloatArrayAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   */
  @CheckReturnValue
  public static AbstractFloatArrayAssert<?> assertThat(float[] actual) {
    return AssertionsForClassTypes.assertThat(actual);
  }

  /**
   * Creates a new instance of <code>{@link IntegerAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   */
  @CheckReturnValue
  public static AbstractIntegerAssert<?> assertThat(int actual) {
    return AssertionsForClassTypes.assertThat(actual);
  }

  /**
   * Creates a new instance of <code>{@link IntArrayAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   */
  @CheckReturnValue
  public static AbstractIntArrayAssert<?> assertThat(int[] actual) {
    return AssertionsForClassTypes.assertThat(actual);
  }

  /**
   * Creates a new instance of <code>{@link IntegerAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   */
  @CheckReturnValue
  public static AbstractIntegerAssert<?> assertThat(Integer actual) {
    return AssertionsForClassTypes.assertThat(actual);
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
   * assertThat(hobbits, stringAssertFactory).first()
   *                                         .startsWith("fro")
   *                                         .endsWith("do");</code></pre>
   *
   * @param <ACTUAL> The actual type
   * @param <ELEMENT> The actual elements type
   * @param <ELEMENT_ASSERT> The actual elements AbstractAssert type
   * @param actual the actual value.
   * @param assertFactory the factory used to create the elements assert instance.
   * @return the created assertion object.
   * @since 2.5.0 / 3.5.0
   */
  //@format:off
  @CheckReturnValue
  public static <ACTUAL extends Iterable<? extends ELEMENT>, ELEMENT, ELEMENT_ASSERT extends AbstractAssert<ELEMENT_ASSERT, ELEMENT>>
         FactoryBasedNavigableIterableAssert<?, ACTUAL, ELEMENT, ELEMENT_ASSERT> assertThat(Iterable<? extends ELEMENT> actual,
                                                                                 AssertFactory<ELEMENT, ELEMENT_ASSERT> assertFactory) {
    return AssertionsForInterfaceTypes.assertThat(actual, assertFactory);
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
   * assertThat(hobbits, StringAssert.class).first()
   *                                        .startsWith("fro")
   *                                        .endsWith("do");</code></pre>
   *
   * @param <ACTUAL> The actual type
   * @param <ELEMENT> The actual elements type
   * @param <ELEMENT_ASSERT> The actual elements AbstractAssert type
   * @param actual the actual value.
   * @param assertClass the class used to create the elements assert instance.
   * @return the created assertion object.
   * @since 2.5.0 / 3.5.0
   */
  @CheckReturnValue
  public static <ACTUAL extends Iterable<? extends ELEMENT>, ELEMENT, ELEMENT_ASSERT extends AbstractAssert<ELEMENT_ASSERT, ELEMENT>>
         ClassBasedNavigableIterableAssert<?, ACTUAL, ELEMENT, ELEMENT_ASSERT> assertThat(ACTUAL actual,
                                                                                          Class<ELEMENT_ASSERT> assertClass) {
           return AssertionsForInterfaceTypes.assertThat(actual, assertClass);
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
   * assertThat(hobbits, stringAssertFactory).first()
   *                                         .startsWith("fro")
   *                                         .endsWith("do");</code></pre>
   *
   * @param <ACTUAL> The actual type
   * @param <ELEMENT> The actual elements type
   * @param <ELEMENT_ASSERT> The actual elements AbstractAssert type
   * @param actual the actual value.
   * @param assertFactory the factory used to create the elements assert instance.
   * @return the created assertion object.
   * @since 2.5.0 / 3.5.0
   */
  @CheckReturnValue
  public static <ACTUAL extends List<? extends ELEMENT>, ELEMENT, ELEMENT_ASSERT extends AbstractAssert<ELEMENT_ASSERT, ELEMENT>>
         FactoryBasedNavigableListAssert<?, ACTUAL, ELEMENT, ELEMENT_ASSERT> assertThat(List<? extends ELEMENT> actual,
                                                                                        AssertFactory<ELEMENT, ELEMENT_ASSERT> assertFactory) {
    return AssertionsForInterfaceTypes.assertThat(actual, assertFactory);
  }

  /**
   * Creates a new instance of <code>{@link ClassBasedNavigableListAssert}</code> tallowing to navigate to any {@code List} element
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
   * assertThat(hobbits, StringAssert.class).first()
   *                                        .startsWith("fro")
   *                                        .endsWith("do");</code></pre>
   *
   * @param <ACTUAL> The actual type
   * @param <ELEMENT> The actual elements type
   * @param <ELEMENT_ASSERT> The actual elements AbstractAssert type
   * @param actual the actual value.
   * @param assertClass the class used to create the elements assert instance.
   * @return the created assertion object.
   * @since 2.5.0 / 3.5.0
   */
  @CheckReturnValue
  public static <ELEMENT, ACTUAL extends List<? extends ELEMENT>, ELEMENT_ASSERT extends AbstractAssert<ELEMENT_ASSERT, ELEMENT>>
         ClassBasedNavigableListAssert<?, ACTUAL, ELEMENT, ELEMENT_ASSERT> assertThat(List<? extends ELEMENT> actual,
                                                                                      Class<ELEMENT_ASSERT> assertClass) {
    return AssertionsForInterfaceTypes.assertThat(actual, assertClass);
  }

//@format:on

  /**
   * Creates a new instance of <code>{@link LongAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   */
  @CheckReturnValue
  public static AbstractLongAssert<?> assertThat(long actual) {
    return AssertionsForClassTypes.assertThat(actual);
  }

  /**
   * Creates a new instance of <code>{@link LongAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   */
  @CheckReturnValue
  public static AbstractLongAssert<?> assertThat(Long actual) {
    return AssertionsForClassTypes.assertThat(actual);
  }

  /**
   * Creates a new instance of <code>{@link LongArrayAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   */
  @CheckReturnValue
  public static AbstractLongArrayAssert<?> assertThat(long[] actual) {
    return AssertionsForClassTypes.assertThat(actual);
  }

  /**
   * Creates a new instance of <code>{@link ObjectAssert}</code>.
   *
   * @param <T> the type of the actual value.
   * @param actual the actual value.
   * @return the created assertion object.
   */
  @CheckReturnValue
  public static <T> AbstractObjectAssert<?, T> assertThat(T actual) {
    return AssertionsForClassTypes.assertThat(actual);
  }

  /**
   * Creates a new instance of <code>{@link ObjectArrayAssert}</code>.
   *
   * @param <T> the actual's elements type.
   * @param actual the actual value.
   * @return the created assertion object.
   */
  @CheckReturnValue
  public static <T> AbstractObjectArrayAssert<?, T> assertThat(T[] actual) {
    return AssertionsForClassTypes.assertThat(actual);
  }

  /**
   * Creates a new instance of <code>{@link ShortAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   */
  @CheckReturnValue
  public static AbstractShortAssert<?> assertThat(short actual) {
    return AssertionsForClassTypes.assertThat(actual);
  }

  /**
   * Creates a new instance of <code>{@link ShortAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   */
  @CheckReturnValue
  public static AbstractShortAssert<?> assertThat(Short actual) {
    return AssertionsForClassTypes.assertThat(actual);
  }

  /**
   * Creates a new instance of <code>{@link ShortArrayAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   */
  @CheckReturnValue
  public static AbstractShortArrayAssert<?> assertThat(short[] actual) {
    return AssertionsForClassTypes.assertThat(actual);
  }

  /**
   * Creates a new instance of <code>{@link DateAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   */
  @CheckReturnValue
  public static AbstractDateAssert<?> assertThat(Date actual) {
    return AssertionsForClassTypes.assertThat(actual);
  }

  /**
   * Creates a new instance of <code>{@link ZonedDateTimeAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   */
  @CheckReturnValue
  public static AbstractZonedDateTimeAssert<?> assertThat(ZonedDateTime actual) {
    return AssertionsForClassTypes.assertThat(actual);
  }

  /**
   * Creates a new instance of <code>{@link LocalDateTimeAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   */
  @CheckReturnValue
  public static AbstractLocalDateTimeAssert<?> assertThat(LocalDateTime actual) {
    return AssertionsForClassTypes.assertThat(actual);
  }

  /**
   * Creates a new instance of <code>{@link java.time.OffsetDateTime}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   */
  @CheckReturnValue
  public static AbstractOffsetDateTimeAssert<?> assertThat(OffsetDateTime actual) {
    return AssertionsForClassTypes.assertThat(actual);
  }

  /**
   * Create assertion for {@link java.time.OffsetTime}.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   */
  @CheckReturnValue
  public static AbstractOffsetTimeAssert<?> assertThat(OffsetTime actual) {
    return AssertionsForClassTypes.assertThat(actual);
  }

  /**
   * Creates a new instance of <code>{@link LocalTimeAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   */
  @CheckReturnValue
  public static AbstractLocalTimeAssert<?> assertThat(LocalTime actual) {
    return AssertionsForClassTypes.assertThat(actual);
  }

  /**
   * Creates a new instance of <code>{@link LocalDateAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   */
  @CheckReturnValue
  public static AbstractLocalDateAssert<?> assertThat(LocalDate actual) {
    return AssertionsForClassTypes.assertThat(actual);
  }

  /**
   * Creates a new instance of <code>{@link InstantAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   * @since 3.7.0
   */
  @CheckReturnValue
  public static AbstractInstantAssert<?> assertThat(Instant actual) {
    return AssertionsForClassTypes.assertThat(actual);
  }

  /**
   * Create assertion for {@link AtomicBoolean}.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   * @since 2.7.0 / 3.7.0
   */
  @CheckReturnValue
  public static AtomicBooleanAssert assertThat(AtomicBoolean actual) {
    return new AtomicBooleanAssert(actual);
  }

  /**
   * Create assertion for {@link AtomicInteger}.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   * @since 2.7.0 / 3.7.0
   */
  @CheckReturnValue
  public static AtomicIntegerAssert assertThat(AtomicInteger actual) {
    return new AtomicIntegerAssert(actual);
  }

  /**
   * Create int[] assertion for {@link AtomicIntegerArray}.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   * @since 2.7.0 / 3.7.0
   */
  @CheckReturnValue
  public static AtomicIntegerArrayAssert assertThat(AtomicIntegerArray actual) {
    return new AtomicIntegerArrayAssert(actual);
  }

  /**
   * Create assertion for {@link AtomicIntegerFieldUpdater}.
   *
   * @param actual the actual value.
   * @param <OBJECT> the type of the object holding the updatable field.
   * @return the created assertion object.
   * @since 2.7.0 / 3.7.0
   */
  @CheckReturnValue
  public static <OBJECT> AtomicIntegerFieldUpdaterAssert<OBJECT> assertThat(AtomicIntegerFieldUpdater<OBJECT> actual) {
    return new AtomicIntegerFieldUpdaterAssert<>(actual);
  }

  /**
   * Create assertion for {@link AtomicLong}.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   * @since 2.7.0 / 3.7.0
   */
  @CheckReturnValue
  public static AtomicLongAssert assertThat(AtomicLong actual) {
    return new AtomicLongAssert(actual);
  }

  /**
   * Create assertion for {@link AtomicLongArray}.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   * @since 2.7.0 / 3.7.0
   */
  @CheckReturnValue
  public static AtomicLongArrayAssert assertThat(AtomicLongArray actual) {
    return new AtomicLongArrayAssert(actual);
  }

  /**
   * Create assertion for {@link AtomicLongFieldUpdater}.
   *
   * @param actual the actual value.
   * @param <OBJECT> the type of the object holding the updatable field.
   * @return the created assertion object.
   * @since 2.7.0 / 3.7.0
   */
  @CheckReturnValue
  public static <OBJECT> AtomicLongFieldUpdaterAssert<OBJECT> assertThat(AtomicLongFieldUpdater<OBJECT> actual) {
    return new AtomicLongFieldUpdaterAssert<>(actual);
  }

  /**
   * Create assertion for {@link AtomicReference}.
   *
   * @param actual the actual value.
   * @param <VALUE> the type of the value contained in the {@link AtomicReference}.
   * @return the created assertion object.
   * @since 2.7.0 / 3.7.0
   */
  @CheckReturnValue
  public static <VALUE> AtomicReferenceAssert<VALUE> assertThat(AtomicReference<VALUE> actual) {
    return new AtomicReferenceAssert<>(actual);
  }

  /**
   * Create assertion for {@link AtomicReferenceArray}.
   *
   * @param actual the actual value.
   * @param <ELEMENT> the type of the value contained in the {@link AtomicReferenceArray}.
   * @return the created assertion object.
   * @since 2.7.0 / 3.7.0
   */
  @CheckReturnValue
  public static <ELEMENT> AtomicReferenceArrayAssert<ELEMENT> assertThat(AtomicReferenceArray<ELEMENT> actual) {
    return new AtomicReferenceArrayAssert<>(actual);
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
  @CheckReturnValue
  public static <FIELD, OBJECT> AtomicReferenceFieldUpdaterAssert<FIELD, OBJECT> assertThat(AtomicReferenceFieldUpdater<OBJECT, FIELD> actual) {
    return new AtomicReferenceFieldUpdaterAssert<>(actual);
  }

  /**
   * Create assertion for {@link AtomicMarkableReference}.
   *
   * @param actual the actual value.
   * @param <VALUE> the type of the value contained in the {@link AtomicMarkableReference}.
   * @return the created assertion object.
   * @since 2.7.0 / 3.7.0
   */
  @CheckReturnValue
  public static <VALUE> AtomicMarkableReferenceAssert<VALUE> assertThat(AtomicMarkableReference<VALUE> actual) {
    return new AtomicMarkableReferenceAssert<>(actual);
  }

  /**
   * Create assertion for {@link AtomicStampedReference}.
   *
   * @param actual the actual value.
   * @param <VALUE> the type of the value contained in the {@link AtomicStampedReference}.
   * @return the created assertion object.
   * @since 2.7.0 / 3.7.0
   */
  @CheckReturnValue
  public static <VALUE> AtomicStampedReferenceAssert<VALUE> assertThat(AtomicStampedReference<VALUE> actual) {
    return new AtomicStampedReferenceAssert<>(actual);
  }

  /**
   * Creates a new instance of <code>{@link ThrowableAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created {@link ThrowableAssert}.
   */
  @CheckReturnValue
  public static AbstractThrowableAssert<?, ? extends Throwable> assertThat(Throwable actual) {
    return AssertionsForClassTypes.assertThat(actual);
  }

  /**
   * Allows to capture and then assert on a {@link Throwable} (easier done with lambdas).
   * <p>
   * Java 8 example :
   * <pre><code class='java'> {@literal @}Test
   *  public void testException() {
   *    assertThatThrownBy(() -&gt; { throw new Exception("boom!") }).isInstanceOf(Exception.class)
   *                                                              .hasMessageContaining("boom");
   * }</code></pre>
   *
   * If the provided {@link ThrowingCallable} does not raise an exception, an error is immediately thrown,
   * in that case the test description provided with {@link AbstractAssert#as(String, Object...) as(String, Object...)} is not honored.<br>
   * To use a test description, use {@link #catchThrowable(ThrowableAssert.ThrowingCallable)} as shown below:
   * <pre><code class='java'> // assertion will fail but "display me" won't appear in the error
   * assertThatThrownBy(() -&gt; {}).as("display me")
   *                             .isInstanceOf(Exception.class);
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
  @CheckReturnValue
  public static AbstractThrowableAssert<?, ? extends Throwable> assertThatThrownBy(ThrowingCallable shouldRaiseThrowable) {
    return assertThat(catchThrowable(shouldRaiseThrowable)).hasBeenThrown();
  }

  /**
   * Allows to capture and then assert on a {@link Throwable} (easier done with lambdas).
   * <p>
   * The main difference with {@code assertThatThrownBy(ThrowingCallable)} is that 
   * this method does not fail if no exception was thrown.
   * <p>
   * Example :
   * <pre><code class='java'> ThrowingCallable boomCode = () -&gt; {
   *   throw new Exception("boom!");
   * };
   * ThrowingCallable doNothing = () -&gt; {}; 
   * 
   * // assertions succeed
   * assertThatCode(doNothing).doesNotThrowAnyException();
   * assertThatCode(boomCode).isInstanceOf(Exception.class)
   *                         .hasMessageContaining("boom");
   *                                                      
   * // assertion fails
   * assertThatCode(boomCode).doesNotThrowAnyException();</code></pre>
   *
   * Contrary to <code>assertThatThrownBy(ThrowingCallable)</code> the test description provided with 
   * {@link AbstractAssert#as(String, Object...) as(String, Object...)} is always honored as shown below.
   * 
   * <pre><code class='java'> ThrowingCallable doNothing = () -&gt; {}; 
   * 
   * // assertion fails and "display me" appears in the assertion error
   * assertThatCode(doNothing).as("display me")
   *                          .isInstanceOf(Exception.class);</code></pre>
   * <p>
   * This method was not named {@code assertThat} because the java compiler reported it ambiguous when used directly with a lambda :(  
   *
   * @param shouldRaiseOrNotThrowable The {@link ThrowingCallable} or lambda with the code that should raise the throwable.
   * @return the created {@link ThrowableAssert}.
   * @since 3.7.0
   */
  @CheckReturnValue
  public static AbstractThrowableAssert<?, ? extends Throwable> assertThatCode(ThrowingCallable shouldRaiseOrNotThrowable) {
    return AssertionsForClassTypes.assertThatCode(shouldRaiseOrNotThrowable);
  }

  /**
   * Allows to catch an {@link Throwable} more easily when used with Java 8 lambdas.
   *
   * <p>
   * This caught {@link Throwable} can then be asserted.
   * </p>
   *
   * <p>
   * Example:
   * </p>
   *
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
   * @param shouldRaiseThrowable The lambda with the code that should raise the exception.
   * @return The captured exception or <code>null</code> if none was raised by the callable.
   */
  public static Throwable catchThrowable(ThrowingCallable shouldRaiseThrowable) {
    return AssertionsForClassTypes.catchThrowable(shouldRaiseThrowable);
  }

  /**
   * Entry point to check that an exception of type T is thrown by a given {@code throwingCallable}
   * which allows to chain assertions on the thrown exception.
   * <p>
   * Example:
   * <pre><code class='java'> assertThatExceptionOfType(IOException.class)
   *           .isThrownBy(() -&gt; { throw new IOException("boom!"); })
   *           .withMessage("boom!"); </code></pre>
   *
   * This method is more or less the same of {@link #assertThatThrownBy(ThrowableAssert.ThrowingCallable)} but in a more natural way.
   * @param <T> the the exception type.
   * @param exceptionType the exception type class.
   * @return the created {@link ThrowableTypeAssert}.
   */
  @CheckReturnValue
  public static <T extends Throwable> ThrowableTypeAssert<T> assertThatExceptionOfType(final Class<? extends T> exceptionType) {
    return AssertionsForClassTypes.assertThatExceptionOfType(exceptionType);
  }

  /**
   * Alias for {@link #assertThatExceptionOfType(Class)} for {@link NullPointerException}.
   * 
   * @return the created {@link ThrowableTypeAssert}.
   * 
   * @since 3.7.0
   */
  @CheckReturnValue
  public static ThrowableTypeAssert<NullPointerException> assertThatNullPointerException() {
    return assertThatExceptionOfType(NullPointerException.class);
  }

  /**
   * Alias for {@link #assertThatExceptionOfType(Class)} for {@link IllegalArgumentException}.
   * 
   * @return the created {@link ThrowableTypeAssert}.
   * 
   * @since 3.7.0
   */
  @CheckReturnValue
  public static ThrowableTypeAssert<IllegalArgumentException> assertThatIllegalArgumentException() {
    return assertThatExceptionOfType(IllegalArgumentException.class);
  }

  /**
   * Alias for {@link #assertThatExceptionOfType(Class)} for {@link IOException}.
   * 
   * @return the created {@link ThrowableTypeAssert}.
   * 
   * @since 3.7.0
   */
  @CheckReturnValue
  public static ThrowableTypeAssert<IOException> assertThatIOException() {
    return assertThatExceptionOfType(IOException.class);
  }

  /**
   * Alias for {@link #assertThatExceptionOfType(Class)} for {@link IllegalStateException}.
   * 
   * @return the created {@link ThrowableTypeAssert}.
   * 
   * @since 3.7.0
   */
  @CheckReturnValue
  public static ThrowableTypeAssert<IllegalStateException> assertThatIllegalStateException() {
    return assertThatExceptionOfType(IllegalStateException.class);
  }

  // -------------------------------------------------------------------------------------------------
  // fail methods : not assertions but here to have a single entry point to all AssertJ features.
  // -------------------------------------------------------------------------------------------------

  /**
   * Sets whether we remove elements related to AssertJ from assertion error stack trace.
   *
   * @param removeAssertJRelatedElementsFromStackTrace flag.
   */
  public static void setRemoveAssertJRelatedElementsFromStackTrace(boolean removeAssertJRelatedElementsFromStackTrace) {
    Fail.setRemoveAssertJRelatedElementsFromStackTrace(removeAssertJRelatedElementsFromStackTrace);
  }

  /**
   * Throws an {@link AssertionError} with the given message.
   *
   * @param failureMessage error message.
   * @throws AssertionError with the given message.
   */
  public static void fail(String failureMessage) {
    Fail.fail(failureMessage);
  }

  /**
   * Throws an {@link AssertionError} with the given message built as {@link String#format(String, Object...)}.
   *
   * @param failureMessage error message.
   * @param args Arguments referenced by the format specifiers in the format string.
   * @throws AssertionError with the given built message.
   */
  public static void fail(String failureMessage, Object... args) {
    Fail.fail(failureMessage, args);
  }

  /**
   * Throws an {@link AssertionError} with the given message and with the {@link Throwable} that caused the failure.
   * @param failureMessage the description of the failed assertion. It can be {@code null}.
   * @param realCause cause of the error.
   * @throws AssertionError with the given message and with the {@link Throwable} that caused the failure.
   */
  public static void fail(String failureMessage, Throwable realCause) {
    Fail.fail(failureMessage, realCause);
  }

  /**
   * Throws an {@link AssertionError} with a message explaining that a {@link Throwable} of given class was expected to be thrown
   * but had not been.
   * <p>
   * {@link Assertions#shouldHaveThrown(Class)} can be used as a replacement.
   * <p>
   * @param throwableClass the Throwable class that was expected to be thrown.
   * @throws AssertionError with a message explaining that a {@link Throwable} of given class was expected to be thrown but had
   *           not been.
   *
   */
  public static void failBecauseExceptionWasNotThrown(Class<? extends Throwable> throwableClass) {
    Fail.shouldHaveThrown(throwableClass);
  }

  /**
   * Throws an {@link AssertionError} with a message explaining that a {@link Throwable} of given class was expected to be thrown
   * but had not been.
   * @param throwableClass the Throwable class that was expected to be thrown.
   * @throws AssertionError with a message explaining that a {@link Throwable} of given class was expected to be thrown but had
   *           not been.
   */
  public static void shouldHaveThrown(Class<? extends Throwable> throwableClass) {
    Fail.shouldHaveThrown(throwableClass);
  }

  /**
   * In error messages, sets the threshold when iterable/array formatting will on one line (if their String description
   * is less than this parameter) or it will be formatted with one element per line.
   * <p>
   * The following array will be formatted on one line as its length &lt; 80:
   * <pre><code class='java'> String[] greatBooks = array("A Game of Thrones", "The Lord of the Rings", "Assassin's Apprentice");
   *
   * // formatted as:
   *
   * ["A Game of Thrones", "The Lord of the Rings", "Assassin's Apprentice"]</code></pre>
   * whereas this array is formatted on multiple lines (one element per line)
   *
   * <pre><code class='java'> String[] greatBooks = array("A Game of Thrones", "The Lord of the Rings", "Assassin's Apprentice", "Guards! Guards! (Discworld)");
   *
   * // formatted as:
   *
   * ["A Game of Thrones",
   *  "The Lord of the Rings",
   *  "Assassin's Apprentice",
   *  "Guards! Guards! (Discworld)"]</code></pre>
   *
   * @param maxLengthForSingleLineDescription the maximum length for an iterable/array to be displayed on one line
   */
  public static void setMaxLengthForSingleLineDescription(int maxLengthForSingleLineDescription) {
    StandardRepresentation.setMaxLengthForSingleLineDescription(maxLengthForSingleLineDescription);
  }

  /**
   * In error messages, sets the threshold for how many elements from one iterable/array/map will be included in the
   * in the description.
   *
   * E.q. When this method is called with a value of {@code 3}.
   * <p>
   * The following array will be formatted entirely as it's length is &lt;= 3:
   * <pre><code class='java'> String[] greatBooks = array("A Game of Thrones", "The Lord of the Rings", "Assassin's Apprentice");
   *
   * // formatted as:
   *
   * ["A Game of Thrones", "The Lord of the Rings", "Assassin's Apprentice"]</code></pre>
   *
   * whereas this array is formatted only with it's first 3 elements, followed by {@code ...}:
   * <pre><code class='java'> String[] greatBooks = array("A Game of Thrones", "The Lord of the Rings", "Assassin's Apprentice", "Guards! Guards!");
   *
   * // formatted as:
   *
   * ["A Game of Thrones", "The Lord of the Rings", "Assassin's Apprentice", ...]</code></pre>
   *
   * @param maxElementsForPrinting the maximum elements that would be printed from one iterable/array/map
   * @since 2.6.0 / 3.6.0
   */
  public static void setMaxElementsForPrinting(int maxElementsForPrinting) {
    StandardRepresentation.setMaxElementsForPrinting(maxElementsForPrinting);
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
   */
  public static <T> Properties<T> extractProperty(String propertyName, Class<T> propertyType) {
    return Properties.extractProperty(propertyName, propertyType);
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
   * @throws NullPointerException if the given property name is {@code null}.
   * @throws IllegalArgumentException if the given property name is empty.
   * @return the created {@code Properties}.
   */
  public static Properties<Object> extractProperty(String propertyName) {
    return Properties.extractProperty(propertyName);
  }

  /**
   * Utility method to build nicely a {@link Tuple} when working with {@link IterableAssert#extracting(String...)} or
   * {@link ObjectArrayAssert#extracting(String...)}
   *
   * @param values the values stored in the {@link Tuple}
   * @return the built {@link Tuple}
   */
  public static Tuple tuple(Object... values) {
    return Tuple.tuple(values);
  }

  /**
   * Globally sets whether
   * <code>{@link org.assertj.core.api.AbstractIterableAssert#extracting(String) IterableAssert#extracting(String)}</code>
   * and
   * <code>{@link org.assertj.core.api.AbstractObjectArrayAssert#extracting(String) ObjectArrayAssert#extracting(String)}</code>
   * should be allowed to extract private fields, if not and they try it fails with exception.
   *
   * @param allowExtractingPrivateFields allow private fields extraction. Default {@code true}.
   */
  public static void setAllowExtractingPrivateFields(boolean allowExtractingPrivateFields) {
    FieldSupport.extraction().setAllowUsingPrivateFields(allowExtractingPrivateFields);
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
   * @param allowComparingPrivateFields allow private fields comparison. Default {@code true}.
   */
  public static void setAllowComparingPrivateFields(boolean allowComparingPrivateFields) {
    FieldSupport.comparison().setAllowUsingPrivateFields(allowComparingPrivateFields);
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
   */
  public static <K, V> MapEntry<K, V> entry(K key, V value) {
    return MapEntry.entry(key, value);
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
   */
  public static Index atIndex(int index) {
    return Index.atIndex(index);
  }

  /**
   * Assertions entry point for double {@link Offset}.
   * <p>
   * Typical usage :
   * <pre><code class='java'> assertThat(8.1).isEqualTo(8.0, offset(0.1));</code></pre>
   * 
   * @param value the allowed offset
   * @return the created {@code Offset}.
   * @throws NullPointerException if the given value is {@code null}.
   * @throws IllegalArgumentException if the given value is negative.
   */
  public static Offset<Double> offset(Double value) {
    return Offset.offset(value);
  }

  /**
   * Assertions entry point for float {@link Offset}.
   * <p>
   * Typical usage :
   * <pre><code class='java'> assertThat(8.2f).isCloseTo(8.0f, offset(0.2f));</code></pre>
   * 
   * @param value the allowed offset
   * @return the created {@code Offset}.
   * @throws NullPointerException if the given value is {@code null}.
   * @throws IllegalArgumentException if the given value is negative.
   */
  public static Offset<Float> offset(Float value) {
    return Offset.offset(value);
  }

  /**
   * Alias for {@link #offset(Double)} to use with isCloseTo assertions.
   * <p>
   * Typical usage :
   * <pre><code class='java'> assertThat(8.1).isCloseTo(8.0, within(0.1));</code></pre>
   * 
   * @param value the value of the offset.
   * @return the created {@code Offset}.
   * @throws NullPointerException if the given value is {@code null}.
   * @throws IllegalArgumentException if the given value is negative.
   */
  public static Offset<Double> within(Double value) {
    return Offset.offset(value);
  }

  /**
   * Alias for {@link #offset(Double)} to use with real number assertions.
   * <p>
   * Typical usage :
   * <pre><code class='java'> assertThat(8.1).isEqualTo(8.0, withPrecision(0.1));</code></pre>
   * 
   * @param value the required precision
   * @return the created {@code Offset}.
   * @throws NullPointerException if the given value is {@code null}.
   * @throws IllegalArgumentException if the given value is negative.
   */
  public static Offset<Double> withPrecision(Double value) {
    return Offset.offset(value);
  }

  /**
   * Alias for {@link #offset(Float)} to use with isCloseTo assertions.
   * <p>
   * Typical usage :
   * <pre><code class='java'> assertThat(8.2f).isCloseTo(8.0f, within(0.2f));</code></pre>
   * 
   * @param value the value of the offset.
   * @return the created {@code Offset}.
   * @throws NullPointerException if the given value is {@code null}.
   * @throws IllegalArgumentException if the given value is negative.
   */
  public static Offset<Float> within(Float value) {
    return Offset.offset(value);
  }

  /**
   * Alias for {@link #offset(Float)} to use with real number assertions.
   * <p>
   * Typical usage :
   * <pre><code class='java'> assertThat(8.2f).isEqualTo(8.0f, withPrecision(0.2f));</code></pre>
   * 
   * @param value the required precision
   * @return the created {@code Offset}.
   * @throws NullPointerException if the given value is {@code null}.
   * @throws IllegalArgumentException if the given value is negative.
   */
  public static Offset<Float> withPrecision(Float value) {
    return Offset.offset(value);
  }

  /**
   * Assertions entry point for BigDecimal {@link Offset} to use with isCloseTo assertions.
   * <p>
   * Typical usage :
   * <pre><code class='java'> assertThat(BigDecimal.TEN).isCloseTo(new BigDecimal("10.5"), within(BigDecimal.ONE));</code></pre>
   * 
   * @param value the value of the offset.
   * @return the created {@code Offset}.
   * @throws NullPointerException if the given value is {@code null}.
   * @throws IllegalArgumentException if the given value is negative.
   */
  public static Offset<BigDecimal> within(BigDecimal value) {
    return Offset.offset(value);
  }

  /**
   * Assertions entry point for BigInteger {@link Offset} to use with isCloseTo assertions.
   * <p>
   * Typical usage :
   * <pre><code class='java'> assertThat(BigInteger.TEN).isCloseTo(new BigInteger("11"), within(new BigInteger("2")));</code></pre>
   * 
   * @param value the value of the offset.
   * @return the created {@code Offset}.
   * @throws NullPointerException if the given value is {@code null}.
   * @throws IllegalArgumentException if the given value is negative.
   * @since 2.7.0 / 3.7.0
   */
  public static Offset<BigInteger> within(BigInteger value) {
    return Offset.offset(value);
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
   */
  public static Offset<Byte> within(Byte value) {
    return Offset.offset(value);
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
   */
  public static Offset<Integer> within(Integer value) {
    return Offset.offset(value);
  }

  /**
   * Assertions entry point for Short {@link Offset} to use with isCloseTo assertions.
   * <p>
   * Typical usage :
   * <pre><code class='java'> assertThat(10).isCloseTo(11, within(1));</code></pre>
   * 
   * @param value the value of the offset.
   * @return the created {@code Offset}.
   * @throws NullPointerException if the given value is {@code null}.
   * @throws IllegalArgumentException if the given value is negative.
   */
  public static Offset<Short> within(Short value) {
    return Offset.offset(value);
  }

  /**
   * Assertions entry point for Long {@link Offset} to use with isCloseTo assertions.
   * <p>
   * Typical usage :
   * <pre><code class='java'> assertThat(5l).isCloseTo(7l, within(2l));</code></pre>
   * 
   * @param value the value of the offset.
   * @return the created {@code Offset}.
   * @throws NullPointerException if the given value is {@code null}.
   * @throws IllegalArgumentException if the given value is negative.
   */
  public static Offset<Long> within(Long value) {
    return Offset.offset(value);
  }

  /**
   * Assertions entry point for {@link TemporalUnitOffset} with  with less than or equal condition
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
   * @since 3.7.0
   */
  public static TemporalUnitOffset within(long value, TemporalUnit unit) {
    return new TemporalUnitWithinOffset(value, unit);
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
   */
  public static Percentage withinPercentage(Double value) {
    return withPercentage(value);
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
   */
  public static Percentage withinPercentage(Integer value) {
    return withPercentage(value);
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
   */
  public static Percentage withinPercentage(Long value) {
    return withPercentage(value);
  }

  /**
   * Alias for {@link #offset(Double)} to use with isCloseTo assertions.
   * <p>
   * Typical usage :
   * <pre><code class='java'> assertThat(8.1).isCloseTo(8.0, byLessThan(0.1));</code></pre>
   *
   * @param value the value of the offset.
   * @return the created {@code Offset}.
   * @throws NullPointerException if the given value is {@code null}.
   * @throws IllegalArgumentException if the given value is negative.
   */
  public static Offset<Double> byLessThan(Double value) {
    return Offset.offset(value);
  }

  /**
   * Alias for {@link #offset(Float)} to use with isCloseTo assertions.
   * <p>
   * Typical usage :
   * <pre><code class='java'> assertThat(8.2f).isCloseTo(8.0f, byLessThan(0.2f));</code></pre>
   *
   * @param value the value of the offset.
   * @return the created {@code Offset}.
   * @throws NullPointerException if the given value is {@code null}.
   * @throws IllegalArgumentException if the given value is negative.
   */
  public static Offset<Float> byLessThan(Float value) {
    return Offset.offset(value);
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
   */
  public static Offset<BigDecimal> byLessThan(BigDecimal value) {
    return Offset.offset(value);
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
   * @since 2.7.0 / 3.7.0
   */
  public static Offset<BigInteger> byLessThan(BigInteger value) {
    return Offset.offset(value);
  }

  /**
   * Assertions entry point for Byte {@link Offset} to use with isCloseTo assertions.
   * <p>
   * Typical usage :
   * <pre><code class='java'> assertThat((byte) 10).isCloseTo((byte) 11, byLessThan((byte) 1));</code></pre>
   *
   * @param value the value of the offset.
   * @return the created {@code Offset}.
   * @throws NullPointerException if the given value is {@code null}.
   * @throws IllegalArgumentException if the given value is negative.
   */
  public static Offset<Byte> byLessThan(Byte value) {
    return Offset.offset(value);
  }

  /**
   * Assertions entry point for Integer {@link Offset} to use with isCloseTo assertions.
   * <p>
   * Typical usage :
   * <pre><code class='java'> assertThat(10).isCloseTo(11, byLessThan(1));</code></pre>
   *
   * @param value the value of the offset.
   * @return the created {@code Offset}.
   * @throws NullPointerException if the given value is {@code null}.
   * @throws IllegalArgumentException if the given value is negative.
   */
  public static Offset<Integer> byLessThan(Integer value) {
    return Offset.offset(value);
  }

  /**
   * Assertions entry point for Short {@link Offset} to use with isCloseTo assertions.
   * <p>
   * Typical usage :
   * <pre><code class='java'> assertThat(10).isCloseTo(11, byLessThan(1));</code></pre>
   *
   * @param value the value of the offset.
   * @return the created {@code Offset}.
   * @throws NullPointerException if the given value is {@code null}.
   * @throws IllegalArgumentException if the given value is negative.
   */
  public static Offset<Short> byLessThan(Short value) {
    return Offset.offset(value);
  }

  /**
   * Assertions entry point for Long {@link Offset} to use with isCloseTo assertions.
   * <p>
   * Typical usage :
   * <pre><code class='java'> assertThat(5l).isCloseTo(7l, byLessThan(2l));</code></pre>
   *
   * @param value the value of the offset.
   * @return the created {@code Offset}.
   * @throws NullPointerException if the given value is {@code null}.
   * @throws IllegalArgumentException if the given value is negative.
   */
  public static Offset<Long> byLessThan(Long value) {
    return Offset.offset(value);
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
   * @since 3.7.0
   */
  public static TemporalUnitOffset byLessThan(long value, TemporalUnit unit) {
    return new TemporalUnitLessThanOffset(value, unit);
  }

  /**
   * A syntax sugar to write fluent assertion using {@link ObjectAssert#returns(Object, Function)}.
   * <p>
   * Example:
   * <pre><code class="java"> Jedi yoda = new Jedi("Yoda", "Green");
   * assertThat(yoda).returns("Yoda", from(Jedi::getName))
   *                 .returns(2.4, from(Jedi::getHeight))
   *                 .returns(150, from(Jedi::getWeight)); </code></pre>
   *
   * @param extractor A function to extract test subject's property
   * @param <F> Type of test subject
   * @param <T> Type of the property under the assertion
   * @return same instance of {@code extractor}
   */
  public static <F, T> Function<F, T> from(Function<F, T> extractor) {
    return extractor;
  }

  // ------------------------------------------------------------------------------------------------------
  // Condition methods : not assertions but here to have a single entry point to all AssertJ features.
  // ------------------------------------------------------------------------------------------------------

  /**
   * Creates a new <code>{@link AllOf}</code>
   *
   * @param <T> the type of object the given condition accept.
   * @param conditions the conditions to evaluate.
   * @return the created {@code AnyOf}.
   * @throws NullPointerException if the given array is {@code null}.
   * @throws NullPointerException if any of the elements in the given array is {@code null}.
   */
  @SafeVarargs
  public static <T> Condition<T> allOf(Condition<? super T>... conditions) {
    return AllOf.allOf(conditions);
  }

  /**
   * Creates a new <code>{@link AllOf}</code>
   *
   * @param <T> the type of object the given condition accept.
   * @param conditions the conditions to evaluate.
   * @return the created {@code AnyOf}.
   * @throws NullPointerException if the given iterable is {@code null}.
   * @throws NullPointerException if any of the elements in the given iterable is {@code null}.
   */
  public static <T> Condition<T> allOf(Iterable<? extends Condition<? super T>> conditions) {
    return AllOf.allOf(conditions);
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
   */
  @SafeVarargs
  public static <T> Condition<T> anyOf(Condition<? super T>... conditions) {
    return AnyOf.anyOf(conditions);
  }

  /**
   * Creates a new <code>{@link AnyOf}</code>
   *
   * @param <T> the type of object the given condition accept.
   * @param conditions the conditions to evaluate.
   * @return the created {@code AnyOf}.
   * @throws NullPointerException if the given iterable is {@code null}.
   * @throws NullPointerException if any of the elements in the given iterable is {@code null}.
   */
  public static <T> Condition<T> anyOf(Iterable<? extends Condition<? super T>> conditions) {
    return AnyOf.anyOf(conditions);
  }

  /**
   * Creates a new <code>{@link DoesNotHave}</code>.
   *
   * @param <T> the type of object the given condition accept.
   * @param condition the condition to inverse.
   * @return The DoesNotHave condition created.
   */
  public static <T> DoesNotHave<T> doesNotHave(Condition<? super T> condition) {
    return DoesNotHave.doesNotHave(condition);
  }

  /**
   * Creates a new <code>{@link Not}</code>.
   *
   * @param <T> the type of object the given condition accept.
   * @param condition the condition to inverse.
   * @return The Not condition created.
   */
  public static <T> Not<T> not(Condition<? super T> condition) {
    return Not.not(condition);
  }

  // --------------------------------------------------------------------------------------------------
  // Filter methods : not assertions but here to have a single entry point to all AssertJ features.
  // --------------------------------------------------------------------------------------------------

  /**
   * Only delegate to {@link Filters#filter(Object[])} so that Assertions offers a full feature entry point to all
   * AssertJ features (but you can use {@link Filters} if you prefer).
   * <p>
   * Note that the given array is not modified, the filters are performed on an {@link Iterable} copy of the array.
   * <p>
   * Typical usage with {@link Condition} :
   *
   * <pre><code class='java'> assertThat(filter(players).being(potentialMVP).get()).containsOnly(james, rose);</code></pre>
   * <p>
   * and with filter language based on java bean property :
   *
   * <pre><code class='java'> assertThat(filter(players).with(&quot;pointsPerGame&quot;).greaterThan(20).and(&quot;assistsPerGame&quot;).greaterThan(7).get())
   *           .containsOnly(james, rose);</code></pre>
   *
   * @param <E> the array elements type.
   * @param array the array to filter.
   * @return the created <code>{@link Filters}</code>.
   */
  public static <E> Filters<E> filter(E[] array) {
    return Filters.filter(array);
  }

  /**
   * Only delegate to {@link Filters#filter(Object[])} so that Assertions offers a full feature entry point to all
   * AssertJ features (but you can use {@link Filters} if you prefer).
   * <p>
   * Note that the given {@link Iterable} is not modified, the filters are performed on a copy.
   * <p>
   * Typical usage with {@link Condition} :
   *
   * <pre><code class='java'> assertThat(filter(players).being(potentialMVP).get()).containsOnly(james, rose);</code></pre>
   * <p>
   * and with filter language based on java bean property :
   *
   * <pre><code class='java'> assertThat(filter(players).with(&quot;pointsPerGame&quot;).greaterThan(20)
   *                           .and(&quot;assistsPerGame&quot;).greaterThan(7).get())
   *           .containsOnly(james, rose);</code></pre>
   *
   * @param <E> the {@link Iterable} elements type.
   * @param iterableToFilter the {@code Iterable} to filter.
   * @return the created <code>{@link Filters}</code>.
   */
  public static <E> Filters<E> filter(Iterable<E> iterableToFilter) {
    return Filters.filter(iterableToFilter);
  }

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
   */
  public static InFilter in(Object... values) {
    return InFilter.in(values);
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
   */
  public static NotInFilter notIn(Object... valuesNotToMatch) {
    return NotInFilter.notIn(valuesNotToMatch);
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
   */
  public static NotFilter not(Object valueNotToMatch) {
    return NotFilter.not(valueNotToMatch);
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
   * @throws RuntimeIOException if an I/O exception occurs.
   */
  public static String contentOf(File file, Charset charset) {
    return Files.contentOf(file, charset);
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
   * @throws RuntimeIOException if an I/O exception occurs.
   */
  public static String contentOf(File file, String charsetName) {
    return Files.contentOf(file, charsetName);
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
   * @throws RuntimeIOException if an I/O exception occurs.
   */
  public static String contentOf(File file) {
    return Files.contentOf(file, Charset.defaultCharset());
  }

  /**
   * Loads the text content of a file into a list of strings with the default charset, each string corresponding to a
   * line.
   * The line endings are either \n, \r or \r\n.
   *
   * @param file the file.
   * @return the content of the file.
   * @throws NullPointerException if the given charset is {@code null}.
   * @throws RuntimeIOException if an I/O exception occurs.
   */
  public static List<String> linesOf(File file) {
    return Files.linesOf(file, Charset.defaultCharset());
  }

  /**
   * Loads the text content of a file into a list of strings, each string corresponding to a line.
   * The line endings are either \n, \r or \r\n.
   *
   * @param file the file.
   * @param charset the character set to use.
   * @return the content of the file.
   * @throws NullPointerException if the given charset is {@code null}.
   * @throws RuntimeIOException if an I/O exception occurs.
   */
  public static List<String> linesOf(File file, Charset charset) {
    return Files.linesOf(file, charset);
  }

  /**
   * Loads the text content of a file into a list of strings, each string corresponding to a line. The line endings are
   * either \n, \r or \r\n.
   *
   * @param file the file.
   * @param charsetName the name of the character set to use.
   * @return the content of the file.
   * @throws NullPointerException if the given charset is {@code null}.
   * @throws RuntimeIOException if an I/O exception occurs.
   */
  public static List<String> linesOf(File file, String charsetName) {
    return Files.linesOf(file, charsetName);
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
   * @throws RuntimeIOException if an I/O exception occurs.
   */
  public static String contentOf(URL url, Charset charset) {
    return URLs.contentOf(url, charset);
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
   * @throws RuntimeIOException if an I/O exception occurs.
   */
  public static String contentOf(URL url, String charsetName) {
    return URLs.contentOf(url, charsetName);
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
   * @throws RuntimeIOException if an I/O exception occurs.
   */
  public static String contentOf(URL url) {
    return URLs.contentOf(url, Charset.defaultCharset());
  }

  /**
   * Loads the text content of a URL into a list of strings with the default charset, each string corresponding to a
   * line.
   * The line endings are either \n, \r or \r\n.
   *
   * @param url the URL.
   * @return the content of the file.
   * @throws NullPointerException if the given charset is {@code null}.
   * @throws RuntimeIOException if an I/O exception occurs.
   */
  public static List<String> linesOf(URL url) {
    return URLs.linesOf(url, Charset.defaultCharset());
  }

  /**
   * Loads the text content of a URL into a list of strings, each string corresponding to a line.
   * The line endings are either \n, \r or \r\n.
   *
   * @param url the URL.
   * @param charset the character set to use.
   * @return the content of the file.
   * @throws NullPointerException if the given charset is {@code null}.
   * @throws RuntimeIOException if an I/O exception occurs.
   */
  public static List<String> linesOf(URL url, Charset charset) {
    return URLs.linesOf(url, charset);
  }

  /**
   * Loads the text content of a URL into a list of strings, each string corresponding to a line. The line endings are
   * either \n, \r or \r\n.
   *
   * @param url the URL.
   * @param charsetName the name of the character set to use.
   * @return the content of the file.
   * @throws NullPointerException if the given charset is {@code null}.
   * @throws RuntimeIOException if an I/O exception occurs.
   */
  public static List<String> linesOf(URL url, String charsetName) {
    return URLs.linesOf(url, charsetName);
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
   */
  public static void setLenientDateParsing(boolean value) {
    AbstractDateAssert.setLenientDateParsing(value);
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
   *                            "[yyyy-MM-dd'T'HH:mm:ss.SSS, yyyy-MM-dd'T'HH:mm:ss, yyyy-MM-dd]");
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
   */
  public static void registerCustomDateFormat(DateFormat userCustomDateFormat) {
    AbstractDateAssert.registerCustomDateFormat(userCustomDateFormat);
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
   *                            "[yyyy-MM-dd'T'HH:mm:ss.SSS, yyyy-MM-dd'T'HH:mm:ss, yyyy-MM-dd]");
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
   */
  public static void registerCustomDateFormat(String userCustomDateFormatPattern) {
    AbstractDateAssert.registerCustomDateFormat(userCustomDateFormatPattern);
  }

  /**
   * Remove all registered custom date formats =&gt; use only the defaults date formats to parse string as date.
   * <p>
   * Beware that the default formats are expressed in the current local timezone.
   * <p>
   * Defaults date format are:
   * <ul>
   * <li><code>yyyy-MM-dd'T'HH:mm:ss.SSS</code></li>
   * <li><code>yyyy-MM-dd HH:mm:ss.SSS</code> (for {@link Timestamp} String representation support)</li>
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
   */
  public static void useDefaultDateFormatsOnly() {
    AbstractDateAssert.useDefaultDateFormatsOnly();
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
  public static <T> T assertThat(final AssertProvider<T> component) {
    return AssertionsForInterfaceTypes.assertThat(component);
  }

  /**
   * Creates a new instance of <code>{@link CharSequenceAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   */
  @CheckReturnValue
  public static AbstractCharSequenceAssert<?, ? extends CharSequence> assertThat(CharSequence actual) {
    return AssertionsForInterfaceTypes.assertThat(actual);
  }

  /**
   * Creates a new instance of <code>{@link CharSequenceAssert}from a {@link String}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   */
  @CheckReturnValue
  public static AbstractCharSequenceAssert<?, String> assertThat(String actual) {
    return AssertionsForClassTypes.assertThat(actual);
  }

  /**
   * Creates a new instance of <code>{@link IterableAssert}</code>.
   *
   * @param <ELEMENT> the type of elements.
   * @param actual the actual value.
   * @return the created assertion object.
   */
  @CheckReturnValue
  public static <ELEMENT> IterableAssert<ELEMENT> assertThat(Iterable<? extends ELEMENT> actual) {
    return new IterableAssert<>(actual);
  }

  /**
   * Creates a new instance of <code>{@link IterableAssert}</code>.
   * <p>
   * <b>Be aware that calls to most methods on returned IterableAssert will consume Iterator so it won't be possible to
   * iterate over it again.</b> Calling multiple methods on returned IterableAssert is safe as Iterator's elements are
   * cached by IterableAssert first time Iterator is consumed.
   *
   * @param <ELEMENT> the type of elements.
   * @param actual the actual value.
   * @return the created assertion object.
   */
  @CheckReturnValue
  public static <ELEMENT> IterableAssert<ELEMENT> assertThat(Iterator<? extends ELEMENT> actual) {
    return AssertionsForInterfaceTypes.assertThat(actual);
  }

  /**
   * Creates a new instance of <code>{@link ListAssert}</code>.
   *
   * @param <ELEMENT> the type of elements.
   * @param actual the actual value.
   * @return the created assertion object.
   */
  @CheckReturnValue
  public static <ELEMENT> ListAssert<ELEMENT> assertThat(List<? extends ELEMENT> actual) {
    return AssertionsForInterfaceTypes.assertThat(actual);
  }

  /**
   * Creates a new instance of <code>{@link ListAssert}</code> from the given {@link Stream}.
   * <p>
   * <b>Be aware that to create the returned {@link ListAssert} the given the {@link Stream} is consumed so it won't be
   * possible to use it again.</b> Calling multiple methods on the returned {@link ListAssert} is safe as it only
   * interacts with the {@link List} built from the {@link Stream}.
   *
   * @param <ELEMENT> the type of elements.
   * @param actual the actual {@link Stream} value.
   * @return the created assertion object.
   */
  @CheckReturnValue
  public static <ELEMENT> AbstractListAssert<?, List<? extends ELEMENT>, ELEMENT, ObjectAssert<ELEMENT>> assertThat(Stream<? extends ELEMENT> actual) {
    return AssertionsForInterfaceTypes.assertThat(actual);
  }

  /**
   * Creates a new instance of <code>{@link ListAssert}</code> from the given {@link DoubleStream}.
   * <p>
   * <b>Be aware that to create the returned {@link ListAssert} the given the {@link DoubleStream} is consumed so it won't be
   * possible to use it again.</b> Calling multiple methods on the returned {@link ListAssert} is safe as it only
   * interacts with the {@link List} built from the {@link DoubleStream}.
   *
   * @param actual the actual {@link DoubleStream} value.
   * @return the created assertion object.
   */
  @CheckReturnValue
  public static AbstractListAssert<?, List<? extends Double>, Double, ObjectAssert<Double>> assertThat(DoubleStream actual) {
    // TODO remove ? extends Double
    return AssertionsForInterfaceTypes.assertThat(actual);
  }

  /**
   * Creates a new instance of <code>{@link ListAssert}</code> from the given {@link LongStream}.
   * <p>
   * <b>Be aware that to create the returned {@link ListAssert} the given the {@link LongStream} is consumed so it won't be
   * possible to use it again.</b> Calling multiple methods on the returned {@link ListAssert} is safe as it only
   * interacts with the {@link List} built from the {@link LongStream}.
   *
   * @param actual the actual {@link LongStream} value.
   * @return the created assertion object.
   */
  @CheckReturnValue
  public static AbstractListAssert<?, List<? extends Long>, Long, ObjectAssert<Long>> assertThat(LongStream actual) {
    return AssertionsForInterfaceTypes.assertThat(actual);
  }

  /**
   * Creates a new instance of <code>{@link ListAssert}</code> from the given {@link IntStream}.
   * <p>
   * <b>Be aware that to create the returned {@link ListAssert} the given the {@link IntStream} is consumed so it won't be
   * possible to use it again.</b> Calling multiple methods on the returned {@link ListAssert} is safe as it only
   * interacts with the {@link List} built from the {@link IntStream}.
   *
   * @param actual the actual {@link IntStream} value.
   * @return the created assertion object.
   */
  @CheckReturnValue
  public static AbstractListAssert<?, List<? extends Integer>, Integer, ObjectAssert<Integer>> assertThat(IntStream actual) {
    return AssertionsForInterfaceTypes.assertThat(actual);
  }

  /**
   * Creates a new instance of {@link PathAssert}
   *
   * @param actual the path to test
   * @return the created assertion object
   */
  @CheckReturnValue
  public static AbstractPathAssert<?> assertThat(Path actual) {
    return AssertionsForInterfaceTypes.assertThat(actual);
  }

  /**
   * Creates a new instance of <code>{@link MapAssert}</code>.
   * <p>
   * Returned type is {@link MapAssert} as it overrides method to annotate them with {@link SafeVarargs} avoiding
   * annoying warnings.
   *
   * @param <K> the type of keys in the map.
   * @param <V> the type of values in the map.
   * @param actual the actual value.
   * @return the created assertion object.
   */
  @CheckReturnValue
  public static <K, V> MapAssert<K, V> assertThat(Map<K, V> actual) {
    return AssertionsForInterfaceTypes.assertThat(actual);
  }

  /**
   * Creates a new instance of <code>{@link GenericComparableAssert}</code> with
   * standard comparison semantics.
   *
   * @param <T> the type of actual.
   * @param actual the actual value.
   * @return the created assertion object.
   */
  @CheckReturnValue
  public static <T extends Comparable<? super T>> AbstractComparableAssert<?, T> assertThat(T actual) {
    return AssertionsForInterfaceTypes.assertThat(actual);
  }

  /**
   * Returns the given assertion. This method improves code readability by surrounding the given assertion with
   * <code>assertThat</code>.
   * <p>
   * Consider for example the following MyButton and MyButtonAssert classes:
   * <pre><code class='java'> public class MyButton extends JButton {
   *
   *   private boolean blinking;
   *
   *   public boolean isBlinking() { return this.blinking; }
   *
   *   public void setBlinking(boolean blink) { this.blinking = blink; }
   *
   * }
   *
   * private static class MyButtonAssert implements AssertDelegateTarget {
   *
   *   private MyButton button;
   *   MyButtonAssert(MyButton button) { this.button = button; }
   *
   *   void isBlinking() {
   *     // standard assertion from core Assertions.assertThat
   *     assertThat(button.isBlinking()).isTrue();
   *   }
   *
   *   void isNotBlinking() {
   *     // standard assertion from core Assertions.assertThat
   *     assertThat(button.isBlinking()).isFalse();
   *   }
   * }</code></pre>
   *
   * As MyButtonAssert implements AssertDelegateTarget, you can use <code>assertThat(buttonAssert).isBlinking();</code>
   * instead of <code>buttonAssert.isBlinking();</code> to have easier to read assertions:
   * <pre><code class='java'> {@literal @}Test
   * public void AssertDelegateTarget_example() {
   *
   *   MyButton button = new MyButton();
   *   MyButtonAssert buttonAssert = new MyButtonAssert(button);
   *
   *   // you can encapsulate MyButtonAssert assertions methods within assertThat
   *   assertThat(buttonAssert).isNotBlinking(); // same as : buttonAssert.isNotBlinking();
   *
   *   button.setBlinking(true);
   *
   *   assertThat(buttonAssert).isBlinking(); // same as : buttonAssert.isBlinking();
   * }</code></pre>
   *
   * @param <T> the generic type of the user-defined assert.
   * @param assertion the assertion to return.
   * @return the given assertion.
   */
  @CheckReturnValue
  public static <T extends AssertDelegateTarget> T assertThat(T assertion) {
    return assertion;
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
   * @since 2.5.0 / 3.5.0
   */
  public static void useRepresentation(Representation customRepresentation) {
    AbstractAssert.setCustomRepresentation(customRepresentation);
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
    StandardRepresentation.registerFormatterForType(type, formatter);
  }

  /**
   * Fallback to use {@link StandardRepresentation} to revert the effect of calling {@link #useRepresentation(Representation)}.
   *
   * @since 2.5.0 / 3.5.0
   */
  public static void useDefaultRepresentation() {
    AbstractAssert.setCustomRepresentation(CONFIGURATION_PROVIDER.representation());
  }

  /**
   * Creates a new <code>{@link Assertions}</code>.
   */
  protected Assertions() {}

}
