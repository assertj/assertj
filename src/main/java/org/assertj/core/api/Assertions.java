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
 * Copyright 2012-2015 the original author or authors.
 */
package org.assertj.core.api;

import java.io.File;
import java.io.InputStream;
import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.text.DateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.assertj.core.api.filter.Filters;
import org.assertj.core.condition.AllOf;
import org.assertj.core.condition.AnyOf;
import org.assertj.core.condition.DoesNotHave;
import org.assertj.core.condition.Not;
import org.assertj.core.data.Index;
import org.assertj.core.data.MapEntry;
import org.assertj.core.data.Offset;
import org.assertj.core.groups.Properties;
import org.assertj.core.groups.Tuple;
import org.assertj.core.util.Files;
import org.assertj.core.util.FilesException;
import org.assertj.core.util.introspection.FieldSupport;

/**
 * Entry point for assertion methods for different data types. Each method in this class is a static factory for the
 * type-specific assertion objects. The purpose of this class is to make test code more readable.
 * <p>
 * For example:
 * <p/>
 *
 * <pre><code class='java'>
 * int removed = employees.removeFired();
 * {@link Assertions#assertThat(int) assertThat}(removed).{@link IntegerAssert#isZero isZero}();
 *
 * List&lt;Employee&gt; newEmployees = employees.hired(TODAY);
 * {@link Assertions#assertThat(Iterable) assertThat}(newEmployees).{@link IterableAssert#hasSize(int) hasSize}(6);
 * </code></pre>
 * <p/>
 * </p>
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
public class Assertions {

  /**
   * Creates a new instance of <code>{@link BigDecimalAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   */
  public static AbstractBigDecimalAssert<?> assertThat(BigDecimal actual) {
    return new BigDecimalAssert(actual);
  }

  /**
   * Creates a new instance of <code>{@link BooleanAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   */
  public static AbstractBooleanAssert<?> assertThat(boolean actual) {
    return new BooleanAssert(actual);
  }

  /**
   * Creates a new instance of <code>{@link BooleanAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   */
  public static AbstractBooleanAssert<?> assertThat(Boolean actual) {
    return new BooleanAssert(actual);
  }

  /**
   * Creates a new instance of <code>{@link BooleanArrayAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   */
  public static AbstractBooleanArrayAssert<?> assertThat(boolean[] actual) {
    return new BooleanArrayAssert(actual);
  }

  /**
   * Creates a new instance of <code>{@link ByteAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   */
  public static AbstractByteAssert<?> assertThat(byte actual) {
    return new ByteAssert(actual);
  }

  /**
   * Creates a new instance of <code>{@link ByteAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   */
  public static AbstractByteAssert<?> assertThat(Byte actual) {
    return new ByteAssert(actual);
  }

  /**
   * Creates a new instance of <code>{@link ByteArrayAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   */
  public static AbstractByteArrayAssert<?> assertThat(byte[] actual) {
    return new ByteArrayAssert(actual);
  }

  /**
   * Creates a new instance of <code>{@link CharacterAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   */
  public static AbstractCharacterAssert<?> assertThat(char actual) {
    return new CharacterAssert(actual);
  }

  /**
   * Creates a new instance of <code>{@link CharArrayAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   */
  public static AbstractCharArrayAssert<?> assertThat(char[] actual) {
    return new CharArrayAssert(actual);
  }

  /**
   * Creates a new instance of <code>{@link CharacterAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   */
  public static AbstractCharacterAssert<?> assertThat(Character actual) {
    return new CharacterAssert(actual);
  }

  /**
   * Creates a new instance of <code>{@link ClassAssert}</code>
   *
   * @param actual the actual value.
   * @return the created assertion object.
   */
  public static AbstractClassAssert<?> assertThat(Class<?> actual) {
    return new ClassAssert(actual);
  }

  /**
   * Creates a new instance of <code>{@link GenericComparableAssert}</code> with
   * standard comparison semantics.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   */
  public static <T extends Comparable<? super T>> AbstractComparableAssert<?, T> assertThat(T actual) {
    return new GenericComparableAssert<>(actual);
  }

  /**
   * Creates a new instance of <code>{@link IterableAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   */
  public static <T> AbstractIterableAssert<?, ? extends Iterable<? extends T>, T> assertThat(Iterable<? extends T> actual) {
    return new IterableAssert<>(actual);
  }

  /**
   * Creates a new instance of <code>{@link IterableAssert}</code>.
   * <p/>
   * <b>Be aware that calls to most methods on returned IterableAssert will consume Iterator so it won't be possible to
   * iterate over it again.</b> Calling multiple methods on returned IterableAssert is safe as Iterator's elements are
   * cached by IterableAssert first time Iterator is consumed.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   */
  public static <T> AbstractIterableAssert<?, ? extends Iterable<? extends T>, T> assertThat(Iterator<? extends T> actual) {
    return new IterableAssert<>(actual);
  }

  /**
   * Creates a new instance of <code>{@link DoubleAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   */
  public static AbstractDoubleAssert<?> assertThat(double actual) {
    return new DoubleAssert(actual);
  }

  /**
   * Creates a new instance of <code>{@link DoubleAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   */
  public static AbstractDoubleAssert<?> assertThat(Double actual) {
    return new DoubleAssert(actual);
  }

  /**
   * Creates a new instance of <code>{@link DoubleArrayAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   */
  public static AbstractDoubleArrayAssert<?> assertThat(double[] actual) {
    return new DoubleArrayAssert(actual);
  }

  /**
   * Creates a new instance of <code>{@link FileAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   */
  public static AbstractFileAssert<?> assertThat(File actual) {
    return new FileAssert(actual);
  }

  /**
   * Creates a new instance of {@link PathAssert}
   *
   * @param actual the path to test
   * @return the created assertion object
   */
  public static AbstractPathAssert<?> assertThat(Path actual)
  {
    return new PathAssert(actual);
  }

  /**
   * Creates a new instance of <code>{@link InputStreamAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   */
  public static AbstractInputStreamAssert<?, ? extends InputStream> assertThat(InputStream actual) {
    return new InputStreamAssert(actual);
  }

  /**
   * Creates a new instance of <code>{@link FloatAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   */
  public static AbstractFloatAssert<?> assertThat(float actual) {
    return new FloatAssert(actual);
  }

  /**
   * Creates a new instance of <code>{@link FloatAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   */
  public static AbstractFloatAssert<?> assertThat(Float actual) {
    return new FloatAssert(actual);
  }

  /**
   * Creates a new instance of <code>{@link FloatArrayAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   */
  public static AbstractFloatArrayAssert<?> assertThat(float[] actual) {
    return new FloatArrayAssert(actual);
  }

  /**
   * Creates a new instance of <code>{@link IntegerAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   */
  public static AbstractIntegerAssert<?> assertThat(int actual) {
    return new IntegerAssert(actual);
  }

  /**
   * Creates a new instance of <code>{@link IntArrayAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   */
  public static AbstractIntArrayAssert<?> assertThat(int[] actual) {
    return new IntArrayAssert(actual);
  }

  /**
   * Creates a new instance of <code>{@link IntegerAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   */
  public static AbstractIntegerAssert<?> assertThat(Integer actual) {
    return new IntegerAssert(actual);
  }

  /**
   * Creates a new instance of <code>{@link ListAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   */
  public static <T> AbstractListAssert<?, ? extends List<? extends T>, T> assertThat(List<? extends T> actual) {
    return new ListAssert<>(actual);
  }

  /**
   * Creates a new instance of <code>{@link LongAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   */
  public static AbstractLongAssert<?> assertThat(long actual) {
    return new LongAssert(actual);
  }

  /**
   * Creates a new instance of <code>{@link LongAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   */
  public static AbstractLongAssert<?> assertThat(Long actual) {
    return new LongAssert(actual);
  }

  /**
   * Creates a new instance of <code>{@link LongArrayAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   */
  public static AbstractLongArrayAssert<?> assertThat(long[] actual) {
    return new LongArrayAssert(actual);
  }

  /**
   * Creates a new instance of <code>{@link ObjectAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   */
  public static <T> AbstractObjectAssert<?, T> assertThat(T actual) {
    return new ObjectAssert<>(actual);
  }

  /**
   * Returns the given assertion. This method improves code readability by surrounding the given assertion with
   * <code>assertThat</code>.
   * <p>
   * Consider for example the following MyButton and MyButtonAssert classes:
   *
   * <pre><code class='java'>
   * public class MyButton extends JButton {
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
   * }
   * </code></pre>
   *
   * As MyButtonAssert implements AssertDelegateTarget, you can use <code>assertThat(buttonAssert).isBlinking();</code>
   * instead of <code>buttonAssert.isBlinking();</code> to have easier to read assertions.
   *
   * <pre><code class='java'>
   * {@literal @}Test
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
   * }
   * </code></pre>
   * 
   * @param <T> the generic type of the user-defined assert.
   * @param assertion the assertion to return.
   * @return the given assertion.
   */
  public static <T extends AssertDelegateTarget> T assertThat(T assertion) {
    return assertion;
  }

  /**
   * Delegates the creation of the {@link Assert} to the {@link AssertProvider#assertThat()} of the given component.
   * 
   * <p>
   * Read the comments on {@link AssertProvider} for an example of its usage.
   * </p>
   * 
   * @param component
   *          the component that creates its own assert
   * @return the associated {@link Assert} of the given component
   */
  public static <T> T assertThat(final AssertProvider<T> component) {
    return component.assertThat();
  }

  /**
   * Creates a new instance of <code>{@link ObjectArrayAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   */
  public static <T> AbstractObjectArrayAssert<?, T> assertThat(T[] actual) {
    return new ObjectArrayAssert<>(actual);
  }

  /**
   * Creates a new instance of <code>{@link MapAssert}</code>.
   * <p>
   * Returned type is {@link MapAssert} as it overrides method to annotate them with {@link SafeVarargs} avoiding
   * annoying warnings.
   * 
   * @param actual the actual value.
   * @return the created assertion object.
   */
  public static <K, V> MapAssert<K, V> assertThat(Map<K, V> actual) {
    return new MapAssert<>(actual);
  }

  /**
   * Creates a new instance of <code>{@link ShortAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   */
  public static AbstractShortAssert<?> assertThat(short actual) {
    return new ShortAssert(actual);
  }

  /**
   * Creates a new instance of <code>{@link ShortAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   */
  public static AbstractShortAssert<?> assertThat(Short actual) {
    return new ShortAssert(actual);
  }

  /**
   * Creates a new instance of <code>{@link ShortArrayAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   */
  public static AbstractShortArrayAssert<?> assertThat(short[] actual) {
    return new ShortArrayAssert(actual);
  }

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
   * Creates a new instance of <code>{@link StringAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   */
  public static AbstractCharSequenceAssert<?, String> assertThat(String actual) {
    return new StringAssert(actual);
  }

  /**
   * Creates a new instance of <code>{@link DateAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   */
  public static AbstractDateAssert<?> assertThat(Date actual) {
    return new DateAssert(actual);
  }

  /**
   * Creates a new instance of <code>{@link ThrowableAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created {@link ThrowableAssert}.
   */
  public static AbstractThrowableAssert<?, ? extends Throwable> assertThat(Throwable actual) {
    return new ThrowableAssert(actual);
  }

  /**
   * Allows to capture and then assert on a {@link Throwable} more easily when used with Java 8 lambdas.
   * 
   * <p>
   * Java 8 example :
   * </p>
   * 
   * <pre><code class='java'>
   *  {@literal @}Test
   *  public void testException() {
   *    assertThatThrownBy(() -> { throw new Exception("boom!") }).isInstanceOf(Exception.class)
   *                                                              .hasMessageContaining("boom");
   *  }
   * </code></pre>
   * 
   * <p>
   * Java 7 example :
   * </p>
   * 
   * <pre><code class='java'>
   * assertThatThrownBy(new ThrowingCallable()
   * 
   *   {@literal @}Override
   *   public void call() throws Exception {
   *     throw new Exception("boom!");
   *   }
   *   
   * }).isInstanceOf(Exception.class)
   *   .hasMessageContaining("boom");
   * </code></pre>
   *
   * @param shouldRaiseThrowable The {@link ThrowingCallable} or lambda with the code that should raise the throwable.
   * @return The captured exception or <code>null</code> if none was raised by the callable.
   */
  public static AbstractThrowableAssert<?, ? extends Throwable> assertThatThrownBy(ThrowingCallable shouldRaiseThrowable) {
    return new ThrowableAssert(catchThrowable(shouldRaiseThrowable)).hasBeenThrown();
  }

  /**
   * Allows to catch an {@link Throwable} more easily when used with Java 8 lambdas.
   *
   * <p>
   * This caught {@link Throwable} can then be asserted.
   * </p>
   * 
   * <p>
   * Java 8 example:
   * </p>
   * 
   * <pre><code class='java'>
   *  {@literal @}Test
   *  public void testException() {
   *    // when
   *    Throwable thrown = catchThrowable(() -> { throw new Exception("boom!") });
   *
   *    // then
   *    assertThat(thrown).isInstanceOf(Exception.class)
   *                      .hasMessageContaining("boom");
   *  }
   * </code></pre>
   *
   * <p>
   * Java 7 example:
   * </p>
   * 
   * <pre><code class='java'>
   * {@literal @}Test
   * public void testException() {
   *   // when
   *   Throwable thrown = catchThrowable(new ThrowingCallable()
   *   
   *     {@literal @}Override
   *     public void call() throws Exception {
   *       throw new Exception("boom!");
   *     }
   *     
   *   });
   *   // then
   *   assertThat(thrown).isInstanceOf(Exception.class)
   *                     .hasMessageContaining("boom");
   * }
   * </code></pre>
   *
   * @param shouldRaiseThrowable The lambda with the code that should raise the exception.
   * @return The captured exception or <code>null</code> if none was raised by the callable.
   */
  public static Throwable catchThrowable(ThrowingCallable shouldRaiseThrowable) {
    return ThrowableAssert.catchThrowable(shouldRaiseThrowable);
  }

  // -------------------------------------------------------------------------------------------------
  // fail methods : not assertions but here to have a single entry point to all AssertJ features.
  // -------------------------------------------------------------------------------------------------

  /**
   * Only delegate to {@link Fail#setRemoveAssertJRelatedElementsFromStackTrace(boolean)} so that Assertions offers a
   * full feature entry point to all AssertJ Assert features (but you can use {@link Fail} if you prefer).
   */
  public static void setRemoveAssertJRelatedElementsFromStackTrace(boolean removeAssertJRelatedElementsFromStackTrace) {
    Fail.setRemoveAssertJRelatedElementsFromStackTrace(removeAssertJRelatedElementsFromStackTrace);
  }

  /**
   * Only delegate to {@link Fail#fail(String)} so that Assertions offers a full feature entry point to all Assertj
   * Assert features (but you can use Fail if you prefer).
   */
  public static void fail(String failureMessage) {
    Fail.fail(failureMessage);
  }

  /**
   * Only delegate to {@link Fail#fail(String, Throwable)} so that Assertions offers a full feature entry point to all
   * AssertJ features (but you can use Fail if you prefer).
   */
  public static void fail(String failureMessage, Throwable realCause) {
    Fail.fail(failureMessage, realCause);
  }

  /**
   * Only delegate to {@link Fail#failBecauseExceptionWasNotThrown(Class)} so that Assertions offers a full feature
   * entry point to all AssertJ features (but you can use Fail if you prefer).
   *
   * {@link Assertions#shouldHaveThrown(Class)} can be used as a replacement.
   */
  public static void failBecauseExceptionWasNotThrown(Class<? extends Throwable> exceptionClass) {
    Fail.shouldHaveThrown(exceptionClass);
  }

  /**
   * Only delegate to {@link Fail#shouldHaveThrown(Class)} so that Assertions offers a full feature
   * entry point to all AssertJ features (but you can use Fail if you prefer).
   */
  public static void shouldHaveThrown(Class<? extends Throwable> exceptionClass) {
    Fail.shouldHaveThrown(exceptionClass);
  }

  // ------------------------------------------------------------------------------------------------------
  // properties methods : not assertions but here to have a single entry point to all AssertJ features.
  // ------------------------------------------------------------------------------------------------------

  /**
   * Only delegate to {@link Properties#extractProperty(String)} so that Assertions offers a full feature entry point
   * to
   * all AssertJ features (but you can use {@link Properties} if you prefer).
   * <p/>
   * Typical usage is to chain <code>extractProperty</code> with <code>from</code> method, see examples below :
   * <p/>
   * 
   * <pre><code class='java'>
   * // extract simple property values having a java standard type (here String)
   * assertThat(extractProperty(&quot;name&quot;, String.class).from(fellowshipOfTheRing)).contains(&quot;
   * Boromir&quot;, &quot;Gandalf&quot;, &quot;Frodo&quot;,
   *     &quot;Legolas&quot;).doesNotContain(&quot;Sauron&quot;, &quot;Elrond&quot;);
   *
   * // extracting property works also with user's types (here Race)
   * assertThat(extractProperty(&quot;race&quot;, String.class).from(fellowshipOfTheRing)).contains(HOBBIT,
   * ELF).doesNotContain(ORC);
   *
   * // extract nested property on Race
   * assertThat(extractProperty(&quot;race.name&quot;, String.class).from(fellowshipOfTheRing)).contains(&quot;
   * Hobbit&quot;, &quot;Elf&quot;)
   *     .doesNotContain(&quot;Orc&quot;);
   * </code></pre>
   */
  public static <T> Properties<T> extractProperty(String propertyName, Class<T> propertyType) {
    return Properties.extractProperty(propertyName, propertyType);
  }

  /**
   * Only delegate to {@link Properties#extractProperty(String)} so that Assertions offers a full feature entry point
   * to
   * all AssertJ features (but you can use {@link Properties} if you prefer).
   * <p/>
   * Typical usage is to chain <code>extractProperty</code> with <code>from</code> method, see examples below :
   * <p/>
   * 
   * <pre><code class='java'>
   * // extract simple property values, as no type has been defined the extracted property will be considered as Object
   * // to define the real property type (here String) use extractProperty(&quot;name&quot;, String.class) instead.
   * assertThat(extractProperty(&quot;name&quot;).from(fellowshipOfTheRing)).contains(&quot;Boromir&quot;,
   * &quot;Gandalf&quot;, &quot;Frodo&quot;, &quot;Legolas&quot;)
   *     .doesNotContain(&quot;Sauron&quot;, &quot;Elrond&quot;);
   *
   * // extracting property works also with user's types (here Race), even though it will be considered as Object
   * // to define the real property type (here String) use extractProperty(&quot;name&quot;, Race.class) instead.
   * assertThat(extractProperty(&quot;race&quot;).from(fellowshipOfTheRing)).contains(HOBBIT, ELF).doesNotContain(ORC);
   *
   * // extract nested property on Race
   * assertThat(extractProperty(&quot;race.name&quot;).from(fellowshipOfTheRing)).contains(&quot;Hobbit&quot;,
   * &quot;Elf&quot;).doesNotContain(&quot;Orc&quot;);
   * </code></pre>
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
   * <code><code>{@link org.assertj.core.api.AbstractIterableAssert#usingElementComparatorOnFields(java.lang.String...)}</code>
   * </li>
   * <li><code>{@link org.assertj.core.api.AbstractObjectAssert#isEqualToComparingFieldByField(A)}</code></li>
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
   * Only delegate to {@link MapEntry#entry(K key, V value)} so that Assertions offers a full feature entry point to
   * all
   * AssertJ features (but you can use {@link MapEntry} if you prefer).
   * <p/>
   * Typical usage is to call <code>entry</code> in MapAssert <code>contains</code> assertion, see examples below :
   * <p/>
   * 
   * <pre><code class='java'>
   * Map<Ring, TolkienCharacter> ringBearers = ... // init omitted
   * 
   * assertThat(ringBearers).contains(entry(oneRing, frodo), entry(nenya, galadriel));
   * </code></pre>
   */
  public static <K, V> MapEntry<K, V> entry(K key, V value) {
    return MapEntry.entry(key, value);
  }

  /**
   * Only delegate to {@link Index#atIndex(int)} so that Assertions offers a full feature entry point to all AssertJ
   * features (but you can use {@link Index} if you prefer).
   * <p/>
   * Typical usage :
   * <p/>
   * 
   * <pre><code class='java'>
   * List&lt;Ring&gt; elvesRings = newArrayList(vilya, nenya, narya);
   * assertThat(elvesRings).contains(vilya, atIndex(0)).contains(nenya, atIndex(1)).contains(narya, atIndex(2));
   * </code></pre>
   */
  public static Index atIndex(int index) {
    return Index.atIndex(index);
  }

  /**
   * Assertions entry point for double {@link Offset}.
   * <p/>
   * Typical usage :
   * <p/>
   * 
   * <pre><code class='java'>
   * assertThat(8.1).isEqualTo(8.0, offset(0.1));
   * </code></pre>
   */
  public static Offset<Double> offset(Double value) {
    return Offset.offset(value);
  }

  /**
   * Assertions entry point for float {@link Offset}.
   * <p/>
   * Typical usage :
   * <p/>
   * 
   * <pre><code class='java'>
   * assertThat(8.2f).isCloseTo(8.0f, offset(0.2f));
   * </code></pre>
   */
  public static Offset<Float> offset(Float value) {
    return Offset.offset(value);
  }

  /**
   * Alias for {@link #offset(Double)} to use with isCloseTo assertions.
   * <p/>
   * Typical usage :
   * <p/>
   * 
   * <pre><code class='java'>
   * assertThat(8.1).isCloseTo(8.0, within(0.1));
   * </code></pre>
   */
  public static Offset<Double> within(Double value) {
    return Offset.offset(value);
  }

  /**
   * Alias for {@link #offset(Float)} to use with isCloseTo assertions.
   * <p/>
   * Typical usage :
   * <p/>
   * 
   * <pre><code class='java'>
   * assertThat(8.2f).isCloseTo(8.0f, within(0.2f));
   * </code></pre>
   */
  public static Offset<Float> within(Float value) {
    return Offset.offset(value);
  }

  /**
   * Assertions entry point for BigDecimal {@link Offset} to use with isCloseTo assertions.
   * <p/>
   * Typical usage :
   * <p/>
   * 
   * <pre><code class='java'>
   * assertThat(BigDecimal.TEN).isCloseTo(new BigDecimal("10.5"), within(BigDecimal.ONE));
   * </code></pre>
   */
  public static Offset<BigDecimal> within(BigDecimal value) {
    return Offset.offset(value);
  }

  /**
   * Assertions entry point for Byte {@link Offset} to use with isCloseTo assertions.
   * <p/>
   * Typical usage :
   * <p/>
   * 
   * <pre><code class='java'>
   * assertThat((byte)10).isCloseTo((byte)11, within((byte)1));
   * </code></pre>
   */
  public static Offset<Byte> within(Byte value) {
    return Offset.offset(value);
  }
  
  /**
   * Assertions entry point for Integer {@link Offset} to use with isCloseTo assertions.
   * <p/>
   * Typical usage :
   * <p/>
   * 
   * <pre><code class='java'>
   * assertThat(10).isCloseTo(11, within(1));
   * </code></pre>
   */
  public static Offset<Integer> within(Integer value) {
    return Offset.offset(value);
  }
  
  /**
   * Assertions entry point for Short {@link Offset} to use with isCloseTo assertions.
   * <p/>
   * Typical usage :
   * <p/>
   * 
   * <pre><code class='java'>
   * assertThat(10).isCloseTo(11, within(1));
   * </code></pre>
   */
  public static Offset<Short> within(Short value) {
    return Offset.offset(value);
  }
  
  /**
   * Assertions entry point for Long {@link Offset} to use with isCloseTo assertions.
   * <p/>
   * Typical usage :
   * <p/>
   * 
   * <pre><code class='java'>
   * assertThat(5l).isCloseTo(7l, within(2l));
   * </code></pre>
   */
  public static Offset<Long> within(Long value) {
    return Offset.offset(value);
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
   * <p/>
   * Typical usage (<code>jedi</code> and <code>sith</code> are {@link Condition}) :
   * <p/>
   * 
   * <pre><code class='java'>
   * assertThat(&quot;Vader&quot;).is(anyOf(jedi, sith));
   * </code></pre>
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
   * Creates a new </code>{@link DoesNotHave}</code>.
   *
   * @param condition the condition to inverse.
   * @return The Not condition created.
   */
  public static <T> DoesNotHave<T> doesNotHave(Condition<? super T> condition) {
    return DoesNotHave.doesNotHave(condition);
  }

  /**
   * Creates a new </code>{@link Not}</code>.
   *
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
   * <p/>
   * Note that the given array is not modified, the filters are performed on an {@link Iterable} copy of the array.
   * <p/>
   * Typical usage with {@link Condition} :
   * <p/>
   * 
   * <pre><code class='java'>
   * assertThat(filter(players).being(potentialMVP).get()).containsOnly(james, rose);
   * </code></pre>
   * <p/>
   * and with filter language based on java bean property :
   * <p/>
   * 
   * <pre><code class='java'>
   * assertThat(filter(players).with(&quot;pointsPerGame&quot;).greaterThan(20).and(&quot;assistsPerGame&quot;)
   * .greaterThan(7).get())
   *     .containsOnly(james, rose);
   * </code></pre>
   */
  public static <E> Filters<E> filter(E[] array) {
    return Filters.filter(array);
  }

  /**
   * Only delegate to {@link Filters#filter(Object[])} so that Assertions offers a full feature entry point to all
   * AssertJ features (but you can use {@link Filters} if you prefer).
   * <p/>
   * Note that the given {@link Iterable} is not modified, the filters are performed on a copy.
   * <p/>
   * Typical usage with {@link Condition} :
   * <p/>
   * 
   * <pre><code class='java'>
   * assertThat(filter(players).being(potentialMVP).get()).containsOnly(james, rose);
   * </code></pre>
   * <p/>
   * and with filter language based on java bean property :
   * <p/>
   * 
   * <pre><code class='java'>
   * assertThat(filter(players).with(&quot;pointsPerGame&quot;).greaterThan(20).and(&quot;assistsPerGame&quot;)
   * .greaterThan(7).get())
   *     .containsOnly(james, rose);
   * </code></pre>
   */
  public static <E> Filters<E> filter(Iterable<E> iterableToFilter) {
    return Filters.filter(iterableToFilter);
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
   * @throws FilesException if an I/O exception occurs.
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
   * @throws FilesException if an I/O exception occurs.
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
   * @throws FilesException if an I/O exception occurs.
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
   * @throws FilesException if an I/O exception occurs.
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
   * @throws FilesException if an I/O exception occurs.
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
   * @throws FilesException if an I/O exception occurs.
   */
  public static List<String> linesOf(File file, String charsetName) {
    return Files.linesOf(file, charsetName);
  }

  // --------------------------------------------------------------------------------------------------
  // Date formatting methods : not assertions but here to have a single entry point to all AssertJ features.
  // --------------------------------------------------------------------------------------------------

  /**
   * Add the given date format to the ones used to parse date String in String based Date assertions like
   * {@link org.assertj.core.api.AbstractDateAssert#isEqualTo(String)}.
   * <p/>
   * User date formats are used before default ones in the order they have been registered (first registered, first
   * used).
   * <p/>
   * AssertJ is gonna use any date formats registered with one of these methods :
   * <ul>
   * <li>{@link org.assertj.core.api.AbstractDateAssert#withDateFormat(String)}</li>
   * <li>{@link org.assertj.core.api.AbstractDateAssert#withDateFormat(java.text.DateFormat)}</li>
   * <li>{@link #registerCustomDateFormat(java.text.DateFormat)}</li>
   * <li>{@link #registerCustomDateFormat(String)}</li>
   * </ul>
   * <p/>
   * Beware that AssertJ will use the newly registered format for <b>all remaining Date assertions in the test suite</b>
   * <p/>
   * To revert to default formats only, call {@link #useDefaultDateFormatsOnly()} or
   * {@link org.assertj.core.api.AbstractDateAssert#withDefaultDateFormatsOnly()}.
   * <p/>
   * Code examples:
   * 
   * <pre><code class='java'>
   * Date date = ... // set to 2003 April the 26th
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
   * assertThat(date).isEqualTo("2003-04-26");
   * </code></pre>
   *
   * @param userCustomDateFormat the new Date format used for String based Date assertions.
   */
  public static void registerCustomDateFormat(DateFormat userCustomDateFormat) {
    AbstractDateAssert.registerCustomDateFormat(userCustomDateFormat);
  }

  /**
   * Add the given date format to the ones used to parse date String in String based Date assertions like
   * {@link org.assertj.core.api.AbstractDateAssert#isEqualTo(String)}.
   * <p/>
   * User date formats are used before default ones in the order they have been registered (first registered, first
   * used).
   * <p/>
   * AssertJ is gonna use any date formats registered with one of these methods :
   * <ul>
   * <li>{@link org.assertj.core.api.AbstractDateAssert#withDateFormat(String)}</li>
   * <li>{@link org.assertj.core.api.AbstractDateAssert#withDateFormat(java.text.DateFormat)}</li>
   * <li>{@link #registerCustomDateFormat(java.text.DateFormat)}</li>
   * <li>{@link #registerCustomDateFormat(String)}</li>
   * </ul>
   * <p/>
   * Beware that AssertJ will use the newly registered format for <b>all remaining Date assertions in the test suite</b>
   * <p/>
   * To revert to default formats only, call {@link #useDefaultDateFormatsOnly()} or
   * {@link org.assertj.core.api.AbstractDateAssert#withDefaultDateFormatsOnly()}.
   * <p/>
   * Code examples:
   * 
   * <pre><code class='java'>
   * Date date = ... // set to 2003 April the 26th
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
   * assertThat(date).isEqualTo("2003-04-26");
   * </code></pre>
   *
   * @param userCustomDateFormatPattern the new Date format pattern used for String based Date assertions.
   */
  public static void registerCustomDateFormat(String userCustomDateFormatPattern) {
    AbstractDateAssert.registerCustomDateFormat(userCustomDateFormatPattern);
  }

  /**
   * Remove all registered custom date formats => use only the defaults date formats to parse string as date.
   * <p/>
   * Beware that the default formats are expressed in the current local timezone.
   * <p/>
   * Defaults date format are:
   * <ul>
   * <li><code>yyyy-MM-dd'T'HH:mm:ss.SSS</code></li>
   * <li><code>yyyy-MM-dd'T'HH:mm:ss</code></li>
   * <li><code>yyyy-MM-dd</code></li>
   * </ul>
   * <p/>
   * Example of valid string date representations:
   * <ul>
   * <li><code>2003-04-26T03:01:02.999</code></li>
   * <li><code>2003-04-26T13:01:02</code></li>
   * <li><code>2003-04-26</code></li>
   * </ul>
   */
  public static void useDefaultDateFormatsOnly() {
    AbstractDateAssert.useDefaultDateFormatsOnly();
  }

  /**
   * Creates a new </code>{@link Assertions}</code>.
   */
  protected Assertions() {
  }
}
