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

import java.nio.file.Path;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Spliterator;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.function.DoublePredicate;
import java.util.function.IntPredicate;
import java.util.function.LongPredicate;
import java.util.function.Predicate;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import java.util.stream.Stream;

import org.assertj.core.util.CheckReturnValue;

/**
 * Entry point for assertion methods for different data types. Each method in this class is a static factory for the
 * type-specific assertion objects. The purpose of this class is to make test code more readable.
 * <p>
 * For example:
 *
 * <pre><code class='java'> int removed = employees.removeFired();
 * {@link Assertions#assertThat(int) assertThat}(removed).{@link IntegerAssert#isZero isZero}();
 *
 * List&lt;Employee&gt; newEmployees = employees.hired(TODAY);
 * {@link Assertions#assertThat(Iterable) assertThat}(newEmployees).{@link IterableAssert#hasSize(int) hasSize}(6); </code></pre>
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
 * @author William Bakker
 * @author William Delanoue
 * @author Turbo87
 * @author dorzey
 */
@CheckReturnValue
public class AssertionsForInterfaceTypes extends AssertionsForClassTypes {

  /**
   * Delegates the creation of the {@link Assert} to the {@link AssertProvider#assertThat()} of the given component.
   *
   * <p>
   * Read the comments on {@link AssertProvider} for an example of its usage.
   * </p>
   *
   * @param <T> the AssertProvider wrapped type.
   * @param component the component that creates its own assert
   * @return the associated {@link Assert} of the given component
   */
  public static <T> T assertThat(final AssertProvider<T> component) {
    return component.assertThat();
  }

  /**
   * Creates a new <code>{@link Assertions}</code>.
   */
  protected AssertionsForInterfaceTypes() {}

  /**
   * Creates a new instance of <code>{@link CharSequenceAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   */
  public static AbstractCharSequenceAssert<?, ? extends CharSequence> assertThat(CharSequence actual) {
    return new CharSequenceAssert(actual);
  }

  /**
   * Creates a new instance of <code>{@link IterableAssert}</code>.
   *
   * @param <ELEMENT> the type of elements.
   * @param actual the actual value.
   * @return the created assertion object.
   */
  public static <ELEMENT> IterableAssert<ELEMENT> assertThat(Iterable<? extends ELEMENT> actual) {
    return new IterableAssert<>(actual);
  }

  /**
   * Creates a new instance of <code>{@link IteratorAssert}</code>.
   *
   * @param <ELEMENT> the type of elements.
   * @param actual the actual value.
   * @return the created assertion object.
   */
  public static <ELEMENT> IteratorAssert<ELEMENT> assertThat(Iterator<? extends ELEMENT> actual) {
    return new IteratorAssert<>(actual);
  }

  /**
   * Creates a new instance of <code>{@link ListAssert}</code>.
   *
   * @param <ELEMENT> the type of elements.
   * @param actual the actual value.
   * @return the created assertion object.
   */
  public static <ELEMENT> ListAssert<ELEMENT> assertThat(List<? extends ELEMENT> actual) {
    return new ListAssert<>(actual);
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
   * assertThat(Stream.of(1, 2, 3)).contains(1)
   *                               .doesNotContain(42);</code></pre>
   * <p>
   * The following assertion fails as the Stream under test is converted to a List before being compared to the expected Stream:
   * <pre><code class='java'> // FAIL: the Stream under test is converted to a List and compared to a Stream but a List is not a Stream.
   * assertThat(Stream.of(1, 2, 3)).isEqualTo(Stream.of(1, 2, 3));</code></pre>
   * <p>
   * These assertions succeed as {@code isEqualTo} and {@code isSameAs} checks references which does not require to convert the Stream to a List.
   * <pre><code class='java'> // The following assertions succeed as it only performs reference checking which does not require to convert the Stream to a List
   * Stream&lt;Integer&gt; stream = Stream.of(1, 2, 3);
   * assertThat(stream).isEqualTo(stream)
   *                   .isSameAs(stream);</code></pre>
   *
   * @param <ELEMENT> the type of elements.
   * @param actual the actual {@link Stream} value.
   * @return the created assertion object.
   */
  public static <ELEMENT> ListAssert<ELEMENT> assertThat(Stream<? extends ELEMENT> actual) {
    return new ListAssert<>(actual);
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
   * assertThat(DoubleStream.of(1.0, 2.0, 3.0)).contains(1.0)
   *                                           .doesNotContain(42.0);</code></pre>
   * <p>
   * The following assertion fails as the DoubleStream under test is converted to a List before being compared to the expected DoubleStream:
   * <pre><code class='java'> // FAIL: the DoubleStream under test is converted to a List and compared to a DoubleStream but a List is not a DoubleStream.
   * assertThat(DoubleStream.of(1.0, 2.0, 3.0)).isEqualTo(DoubleStream.of(1.0, 2.0, 3.0));</code></pre>
   * <p>
   * These assertions succeed as {@code isEqualTo} and {@code isSameAs} checks references which does not require to convert the DoubleStream to a List.
   * <pre><code class='java'> // The following assertions succeed as it only performs reference checking which does not require to convert the DoubleStream to a List
   * DoubleStream stream = DoubleStream.of(1.0, 2.0, 3.0);
   * assertThat(stream).isEqualTo(stream)
   *                   .isSameAs(stream);</code></pre>
   *
   * @param actual the actual {@link DoubleStream} value.
   * @return the created assertion object.
   */
  public static ListAssert<Double> assertThat(DoubleStream actual) {
    return new ListAssert<>(actual);
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
   * assertThat(LongStream.of(1, 2, 3)).contains(1)
   *                                   .doesNotContain(42);</code></pre>
   * <p>
   * The following assertion fails as the LongStream under test is converted to a List before being compared to the expected LongStream:
   * <pre><code class='java'> // FAIL: the LongStream under test is converted to a List and compared to a LongStream but a List is not a LongStream.
   * assertThat(LongStream.of(1, 2, 3)).isEqualTo(LongStream.of(1, 2, 3));</code></pre>
   * <p>
   * These assertions succeed as {@code isEqualTo} and {@code isSameAs} checks references which does not require to convert the LongStream to a List.
   * <pre><code class='java'> // The following assertions succeed as it only performs reference checking which does not require to convert the LongStream to a List
   * LongStream stream = LongStream.of(1, 2, 3);
   * assertThat(stream).isEqualTo(stream)
   *                   .isSameAs(stream);</code></pre>
   *
   * @param actual the actual {@link LongStream} value.
   * @return the created assertion object.
   */
  public static ListAssert<Long> assertThat(LongStream actual) {
    return new ListAssert<>(actual);
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
   * assertThat(IntStream.of(1, 2, 3)).contains(1)
   *                                  .doesNotContain(42);</code></pre>
   * <p>
   * The following assertion fails as the IntStream under test is converted to a List before being compared to the expected IntStream:
   * <pre><code class='java'> // FAIL: the IntStream under test is converted to a List and compared to a IntStream but a List is not a IntStream.
   * assertThat(IntStream.of(1, 2, 3)).isEqualTo(IntStream.of(1, 2, 3));</code></pre>
   * <p>
   * These assertions succeed as {@code isEqualTo} and {@code isSameAs} checks references which does not require to convert the IntStream to a List.
   * <pre><code class='java'> // The following assertions succeed as it only performs reference checking which does not require to convert the IntStream to a List
   * IntStream stream = IntStream.of(1, 2, 3);
   * assertThat(stream).isEqualTo(stream)
   *                   .isSameAs(stream);</code></pre>
   *
   * @param actual the actual {@link IntStream} value.
   * @return the created assertion object.
   */
  public static ListAssert<Integer> assertThat(IntStream actual) {
    return new ListAssert<>(actual);
  }

  /**
   * Creates a new instance of <code>{@link IterableAssert}</code>.
   *
   * @param <ACTUAL> The actual type
   * @param <ELEMENT> The actual elements type
   * @param <ELEMENT_ASSERT> The actual elements AbstractAssert type
   * @param actual the actual value.
   * @param assertFactory the factory used to create the elements assert instance.
   * @return the created assertion object.
   */
//@format:off
  @SuppressWarnings({ "unchecked", "rawtypes" })
  public static <ACTUAL extends Iterable<? extends ELEMENT>, ELEMENT, ELEMENT_ASSERT extends AbstractAssert<ELEMENT_ASSERT, ELEMENT>>
         FactoryBasedNavigableIterableAssert<?, ACTUAL, ELEMENT, ELEMENT_ASSERT> assertThat(Iterable<? extends ELEMENT> actual,
                                                                                 AssertFactory<ELEMENT, ELEMENT_ASSERT> assertFactory) {
    return new FactoryBasedNavigableIterableAssert(actual, FactoryBasedNavigableIterableAssert.class, assertFactory);
  }

  @SuppressWarnings({ "rawtypes", "unchecked" })
  public static <ACTUAL extends Iterable<? extends ELEMENT>, ELEMENT, ELEMENT_ASSERT extends AbstractAssert<ELEMENT_ASSERT, ELEMENT>>
         ClassBasedNavigableIterableAssert<?, ACTUAL, ELEMENT, ELEMENT_ASSERT> assertThat(ACTUAL actual,
                                                                                          Class<ELEMENT_ASSERT> assertClass) {
    return new ClassBasedNavigableIterableAssert(actual, ClassBasedNavigableIterableAssert.class, assertClass);
  }

  @SuppressWarnings({ "unchecked", "rawtypes" })
  public static <ACTUAL extends List<? extends ELEMENT>, ELEMENT, ELEMENT_ASSERT extends AbstractAssert<ELEMENT_ASSERT, ELEMENT>>
         FactoryBasedNavigableListAssert<?, ACTUAL, ELEMENT, ELEMENT_ASSERT> assertThat(List<? extends ELEMENT> actual,
                                                                                        AssertFactory<ELEMENT, ELEMENT_ASSERT> assertFactory) {
    return new FactoryBasedNavigableListAssert(actual, FactoryBasedNavigableListAssert.class, assertFactory);
  }

  @SuppressWarnings({ "unchecked", "rawtypes" })
  public static <ELEMENT, ACTUAL extends List<? extends ELEMENT>, ELEMENT_ASSERT extends AbstractAssert<ELEMENT_ASSERT, ELEMENT>>
         ClassBasedNavigableListAssert<?, ACTUAL, ELEMENT, ELEMENT_ASSERT> assertThat(List<? extends ELEMENT> actual,
                                                                                      Class<ELEMENT_ASSERT> assertClass) {
    return new ClassBasedNavigableListAssert(actual, assertClass);
  }
//@format:on

  /**
   * Creates a new instance of {@link PathAssert}
   *
   * @param actual the path to test
   * @return the created assertion object
   */
  public static AbstractPathAssert<?> assertThat(Path actual) {
    return new PathAssert(actual);
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
  public static <K, V> MapAssert<K, V> assertThat(Map<K, V> actual) {
    return new MapAssert<>(actual);
  }

  /**
   * Creates a new instance of <code>{@link GenericComparableAssert}</code> with
   * standard comparison semantics.
   *
   * @param <T> the type of actual.
   * @param actual the actual value.
   * @return the created assertion object.
   */
  public static <T extends Comparable<? super T>> AbstractComparableAssert<?, T> assertThat(T actual) {
    return new GenericComparableAssert<>(actual);
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
  public static <T extends AssertDelegateTarget> T assertThat(T assertion) {
    return assertion;
  }

  /**
   * Create assertion for {@link Predicate}.
   *
   * @param actual the actual value.
   * @param <T> the type of the value contained in the {@link Predicate}.
   * @return the created assertion object.
   * @since 3.5.0
   */
  public static <T> PredicateAssert<T> assertThat(Predicate<T> actual) {
    return new PredicateAssert<>(actual);
  }

  /**
   * Create assertion for {@link IntPredicate}.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   * @since 3.5.0
   */
  public static IntPredicateAssert assertThat(IntPredicate actual) {
    return new IntPredicateAssert(actual);
  }

  /**
   * Create assertion for {@link LongPredicate}.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   * @since 3.5.0
   */
  public static LongPredicateAssert assertThat(LongPredicate actual) {
    return new LongPredicateAssert(actual);
  }

  /**
   * Create assertion for {@link DoublePredicate}.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   * @since 3.5.0
   */
  public static DoublePredicateAssert assertThat(DoublePredicate actual) {
    return new DoublePredicateAssert(actual);
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
  public static <RESULT> CompletableFutureAssert<RESULT> assertThat(CompletionStage<RESULT> actual) {
    return new CompletableFutureAssert<>(actual);
  }

  /**
   * Create assertion for {@link SpliteratorAssert}.
   *
   * @param <ELEMENT> the type of elements
   * @param actual the actual value.
   *
   * @return the created assertion object.
   */
  public static <ELEMENT> SpliteratorAssert<ELEMENT> assertThat(Spliterator<ELEMENT> actual) {
    return new SpliteratorAssert<>(actual);
  }
}
