/*
 * Created on Sep 30, 2010
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 * 
 * Copyright @2010-2011 the original author or authors.
 */
package org.assertj.core.api;

import java.io.File;
import java.io.InputStream;
import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.text.DateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

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
import org.assertj.core.util.Dates;
import org.assertj.core.util.Files;
import org.assertj.core.util.FilesException;
import org.assertj.core.util.introspection.FieldSupport;

/**
 * Entry point for assertion methods for different data types. Each method in this class is a static factory for the
 * type-specific assertion objects. The purpose of this class is to make test code more readable. <p> For example: <p/>
 * <pre>
 * int removed = employees.removeFired();
 * {@link Assertions#assertThat(int) assertThat}(removed).{@link IntegerAssert#isZero isZero}();
 *
 * List&lt;Employee&gt; newEmployees = employees.hired(TODAY);
 * {@link Assertions#assertThat(Iterable) assertThat}(newEmployees).{@link IterableAssert#hasSize(int) hasSize}(6);
 * </pre>
 * <p/> </p>
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
  public static BigDecimalAssert assertThat(BigDecimal actual) {
    return new BigDecimalAssert(actual);
  }

  /**
   * Creates a new instance of <code>{@link BooleanAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   */
  public static BooleanAssert assertThat(boolean actual) {
    return new BooleanAssert(actual);
  }

  /**
   * Creates a new instance of <code>{@link BooleanAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   */
  public static BooleanAssert assertThat(Boolean actual) {
    return new BooleanAssert(actual);
  }

  /**
   * Creates a new instance of <code>{@link BooleanArrayAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   */
  public static BooleanArrayAssert assertThat(boolean[] actual) {
    return new BooleanArrayAssert(actual);
  }

  /**
   * Creates a new instance of <code>{@link ByteAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   */
  public static ByteAssert assertThat(byte actual) {
    return new ByteAssert(actual);
  }

  /**
   * Creates a new instance of <code>{@link ByteAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   */
  public static ByteAssert assertThat(Byte actual) {
    return new ByteAssert(actual);
  }

  /**
   * Creates a new instance of <code>{@link ByteArrayAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   */
  public static ByteArrayAssert assertThat(byte[] actual) {
    return new ByteArrayAssert(actual);
  }

  /**
   * Creates a new instance of <code>{@link CharacterAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   */
  public static CharacterAssert assertThat(char actual) {
    return new CharacterAssert(actual);
  }

  /**
   * Creates a new instance of <code>{@link CharArrayAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   */
  public static CharArrayAssert assertThat(char[] actual) {
    return new CharArrayAssert(actual);
  }

  /**
   * Creates a new instance of <code>{@link CharacterAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   */
  public static CharacterAssert assertThat(Character actual) {
    return new CharacterAssert(actual);
  }

  /**
   * Creates a new instance of <code>{@link ClassAssert}</code>
   *
   * @param actual the actual value.
   * @return the created assertion object.
   */
  public static ClassAssert assertThat(Class<?> actual) {
    return new ClassAssert(actual);
  }

  /**
   * Creates a new instance of <code>{@link IterableAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   */
  public static <T> IterableAssert<T> assertThat(Iterable<T> actual) {
    return new IterableAssert<T>(actual);
  }

  /**
   * Creates a new instance of <code>{@link IterableAssert}</code> by consuming the given <code>{@link Iterator}</code>
   * to build an <code>{@link Iterable}</code>
   * <p/>
   * <b>Be aware thar the given Iterator can't be use anymore since it has been consumed !</b>
   *
   * @param actual the actual value.
   * @return the created assertion object.
   */
  public static <T> IterableAssert<T> assertThat(Iterator<T> actual) {
    return new IterableAssert<T>(actual);
  }

  /**
   * Creates a new instance of <code>{@link DoubleAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   */
  public static DoubleAssert assertThat(double actual) {
    return new DoubleAssert(actual);
  }

  /**
   * Creates a new instance of <code>{@link DoubleAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   */
  public static DoubleAssert assertThat(Double actual) {
    return new DoubleAssert(actual);
  }

  /**
   * Creates a new instance of <code>{@link DoubleArrayAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   */
  public static DoubleArrayAssert assertThat(double[] actual) {
    return new DoubleArrayAssert(actual);
  }

  /**
   * Creates a new instance of <code>{@link FileAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   */
  public static FileAssert assertThat(File actual) {
    return new FileAssert(actual);
  }

  /**
   * Creates a new instance of <code>{@link InputStreamAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   */
  public static InputStreamAssert assertThat(InputStream actual) {
    return new InputStreamAssert(actual);
  }

  /**
   * Creates a new instance of <code>{@link FloatAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   */
  public static FloatAssert assertThat(float actual) {
    return new FloatAssert(actual);
  }

  /**
   * Creates a new instance of <code>{@link FloatAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   */
  public static FloatAssert assertThat(Float actual) {
    return new FloatAssert(actual);
  }

  /**
   * Creates a new instance of <code>{@link FloatArrayAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   */
  public static FloatArrayAssert assertThat(float[] actual) {
    return new FloatArrayAssert(actual);
  }

  /**
   * Creates a new instance of <code>{@link IntegerAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   */
  public static IntegerAssert assertThat(int actual) {
    return new IntegerAssert(actual);
  }

  /**
   * Creates a new instance of <code>{@link IntArrayAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   */
  public static IntArrayAssert assertThat(int[] actual) {
    return new IntArrayAssert(actual);
  }

  /**
   * Creates a new instance of <code>{@link IntegerAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   */
  public static IntegerAssert assertThat(Integer actual) {
    return new IntegerAssert(actual);
  }

  /**
   * Creates a new instance of <code>{@link ListAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   */
  public static <T> ListAssert<T> assertThat(List<T> actual) {
    return new ListAssert<T>(actual);
  }

  /**
   * Creates a new instance of <code>{@link LongAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   */
  public static LongAssert assertThat(long actual) {
    return new LongAssert(actual);
  }

  /**
   * Creates a new instance of <code>{@link LongAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   */
  public static LongAssert assertThat(Long actual) {
    return new LongAssert(actual);
  }

  /**
   * Creates a new instance of <code>{@link LongArrayAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   */
  public static LongArrayAssert assertThat(long[] actual) {
    return new LongArrayAssert(actual);
  }

  /**
   * Creates a new instance of <code>{@link ObjectAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   */
  public static <T> ObjectAssert<T> assertThat(T actual) {
    return new ObjectAssert<T>(actual);
  }

  /**
   * Creates a new instance of <code>{@link ObjectArrayAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   */
  public static <T> ObjectArrayAssert<T> assertThat(T[] actual) {
    return new ObjectArrayAssert<T>(actual);
  }

  /**
   * Creates a new instance of <code>{@link MapAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   */
  public static <K, V> MapAssert<K, V> assertThat(Map<K, V> actual) {
    return new MapAssert<K, V>(actual);
  }

  /**
   * Creates a new instance of <code>{@link ShortAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   */
  public static ShortAssert assertThat(short actual) {
    return new ShortAssert(actual);
  }

  /**
   * Creates a new instance of <code>{@link ShortAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   */
  public static ShortAssert assertThat(Short actual) {
    return new ShortAssert(actual);
  }

  /**
   * Creates a new instance of <code>{@link ShortArrayAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   */
  public static ShortArrayAssert assertThat(short[] actual) {
    return new ShortArrayAssert(actual);
  }

  /**
   * Creates a new instance of <code>{@link CharSequenceAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   */
  public static CharSequenceAssert assertThat(CharSequence actual) {
    return new CharSequenceAssert(actual);
  }

  /**
   * Creates a new instance of <code>{@link StringAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   */
  public static StringAssert assertThat(String actual) {
    return new StringAssert(actual);
  }

  /**
   * Creates a new instance of <code>{@link DateAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   */
  public static DateAssert assertThat(Date actual) {
    return new DateAssert(actual);
  }

  /**
   * Creates a new instance of <code>{@link ThrowableAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion Throwable.
   */
  public static ThrowableAssert assertThat(Throwable actual) {
    return new ThrowableAssert(actual);
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
   */
  public static void failBecauseExceptionWasNotThrown(Class<? extends Throwable> exceptionClass) {
    Fail.failBecauseExceptionWasNotThrown(exceptionClass);
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
   * <pre>
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
   * </pre>
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
   * <pre>
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
   * </pre>
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
   * Globally set whether <code>{@link org.assertj.core.api.AbstractIterableAssert#extracting(String) IterableAssert#extracting(String)}</code> and
   * <code>{@link org.assertj.core.api.AbstractObjectArrayAssert#extracting(String) ObjectArrayAssert#extracting(String)}</code>
   * should be allowed to extract private fields, if not and they try it fails with exception.
   *
   * @param allowExtractingPrivateFields allow private fields extraction. Default {@code true}.
   */
  public static void setAllowExtractingPrivateFields(boolean allowExtractingPrivateFields) {
    FieldSupport.setAllowExtractingPrivateFields(allowExtractingPrivateFields);
  }

  // ------------------------------------------------------------------------------------------------------
  // Data utility methods : not assertions but here to have a single entry point to all AssertJ features.
  // ------------------------------------------------------------------------------------------------------

  /**
   * Only delegate to {@link MapEntry#entry(Object, Object)} so that Assertions offers a full feature entry point to
   * all
   * AssertJ features (but you can use {@link MapEntry} if you prefer).
   * <p/>
   * Typical usage is to call <code>entry</code> in MapAssert <code>contains</code> assertion, see examples below :
   * <p/>
   * <pre>
   * assertThat(ringBearers).contains(entry(oneRing, frodo), entry(nenya, galadriel));
   * </pre>
   */
  public static MapEntry entry(Object key, Object value) {
    return MapEntry.entry(key, value);
  }

  /**
   * Only delegate to {@link Index#atIndex(int)} so that Assertions offers a full feature entry point to all AssertJ
   * features (but you can use {@link Index} if you prefer).
   * <p/>
   * Typical usage :
   * <p/>
   * <pre>
   * List&lt;Ring&gt; elvesRings = newArrayList(vilya, nenya, narya);
   * assertThat(elvesRings).contains(vilya, atIndex(0)).contains(nenya, atIndex(1)).contains(narya, atIndex(2));
   * </pre>
   */
  public static Index atIndex(int index) {
    return Index.atIndex(index);
  }

  /**
   * Only delegate to {@link Offset#offset(Double)} so that Assertions offers a full feature entry point to all AssertJ
   * features (but you can use {@link Offset} if you prefer).
   * <p/>
   * Typical usage :
   * <p/>
   * <pre>
   * assertThat(8.1).isEqualTo(8.0, offset(0.1));
   * </pre>
   */
  public static Offset<Double> offset(Double value) {
    return Offset.offset(value);
  }

  /**
   * Only delegate to {@link Offset#offset(Float)} so that Assertions offers a full feature entry point to all AssertJ
   * features (but you can use {@link Offset} if you prefer).
   * <p/>
   * Typical usage :
   * <p/>
   * <pre>
   * assertThat(8.2f).isEqualTo(8.0f, offset(0.2f));
   * </pre>
   */
  public static Offset<Float> offset(Float value) {
    return Offset.offset(value);
  }

  // ------------------------------------------------------------------------------------------------------
  // Condition methods : not assertions but here to have a single entry point to all AssertJ features.
  // ------------------------------------------------------------------------------------------------------

  /**
   * Creates a new <code>{@link AllOf}</code>
   *
   * @param <T>        the type of object the given condition accept.
   * @param conditions the conditions to evaluate.
   * @return the created {@code AnyOf}.
   * @throws NullPointerException if the given array is {@code null}.
   * @throws NullPointerException if any of the elements in the given array is {@code null}.
   */
  public static <T> Condition<T> allOf(Condition<? super T>... conditions) {
    return AllOf.allOf(conditions);
  }

  /**
   * Creates a new <code>{@link AllOf}</code>
   *
   * @param <T>        the type of object the given condition accept.
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
   * <pre>
   * assertThat(&quot;Vader&quot;).is(anyOf(jedi, sith));
   * </pre>
   */
  public static <T> Condition<T> anyOf(Condition<? super T>... conditions) {
    return AnyOf.anyOf(conditions);
  }

  /**
   * Creates a new <code>{@link AnyOf}</code>
   *
   * @param <T>        the type of object the given condition accept.
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
   * <pre>
   * assertThat(filter(players).being(potentialMVP).get()).containsOnly(james, rose);
   * </pre>
   * <p/>
   * and with filter language based on java bean property :
   * <p/>
   * <pre>
   * assertThat(filter(players).with(&quot;pointsPerGame&quot;).greaterThan(20).and(&quot;assistsPerGame&quot;)
   * .greaterThan(7).get())
   *     .containsOnly(james, rose);
   * </pre>
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
   * <pre>
   * assertThat(filter(players).being(potentialMVP).get()).containsOnly(james, rose);
   * </pre>
   * <p/>
   * and with filter language based on java bean property :
   * <p/>
   * <pre>
   * assertThat(filter(players).with(&quot;pointsPerGame&quot;).greaterThan(20).and(&quot;assistsPerGame&quot;)
   * .greaterThan(7).get())
   *     .containsOnly(james, rose);
   * </pre>
   */
  public static <E> Filters<E> filter(Iterable<E> iterableToFilter) {
    return Filters.filter(iterableToFilter);
  }

  // --------------------------------------------------------------------------------------------------
  // File methods : not assertions but here to have a single entry point to all AssertJ features.
  // --------------------------------------------------------------------------------------------------

  /**
   * Loads the text content of a file, so that it can be passed to {@link #assertThat(String)}. <p> Note that this will
   * load the entire file in memory; for larger files, there might be a more efficient alternative with {@link
   * #assertThat(File)}. </p>
   *
   * @param file    the file.
   * @param charset the character set to use.
   * @return the content of the file.
   * @throws NullPointerException if the given charset is {@code null}.
   * @throws FilesException       if an I/O exception occurs.
   */
  public static String contentOf(File file, Charset charset) {
    return Files.contentOf(file, charset);
  }

  /**
   * Loads the text content of a file, so that it can be passed to {@link #assertThat(String)}. <p> Note that this will
   * load the entire file in memory; for larger files, there might be a more efficient alternative with {@link
   * #assertThat(File)}. </p>
   *
   * @param file        the file.
   * @param charsetName the name of the character set to use.
   * @return the content of the file.
   * @throws IllegalArgumentException if the given character set is not supported on this platform.
   * @throws FilesException           if an I/O exception occurs.
   */
  public static String contentOf(File file, String charsetName) {
    return Files.contentOf(file, charsetName);
  }

  /**
   * Loads the text content of a file with the default character set, so that it can be passed to {@link
   * #assertThat(String)}. <p> Note that this will load the entire file in memory; for larger files, there might be a
   * more efficient alternative with {@link #assertThat(File)}. </p>
   *
   * @param file the file.
   * @return the content of the file.
   * @throws FilesException if an I/O exception occurs.
   */
  public static String contentOf(File file) {
    return Files.contentOf(file, Charset.defaultCharset());
  }

  /**
   * For String based Date assertions like {@link AbstractDateAssert#isBefore(String)}, given String is expected to
   * follow one of the default Date format:
   * <ul>
   * <li><code>yyyy-MM-dd'T'HH:mm:ss.SSS</code></li>
   * <li><code>yyyy-MM-dd'T'HH:mm:ss</code></li>
   * <li><code>yyyy-MM-dd</code></li>
   * </ul>
   * With this method, user can specify its own date format, replacing the current date format for all future Date
   * assertions in the test suite (i.e. not only the current assertions) since custom DateFormat is stored in a static
   * field.
   * <p/>
   * To revert to default format simply call {@link #useDefaultDateFormats()} (static method) or {@link
   * org.assertj.core.api.AbstractDateAssert#withDefaultDateFormats()}.
   *
   * @param userCustomDateFormat the new Date format used for String based Date assertions.
   */
  public static void useDateFormat(final DateFormat userCustomDateFormat) {
    AbstractDateAssert.useDateFormat(userCustomDateFormat);
  }

  /**
   * For String based Date assertions like {@link AbstractDateAssert#isBefore(String)}, given String is expected to
   * follow one of the default Date format:
   * <ul>
   * <li><code>yyyy-MM-dd'T'HH:mm:ss.SSS</code></li>
   * <li><code>yyyy-MM-dd'T'HH:mm:ss</code></li>
   * <li><code>yyyy-MM-dd</code></li>
   * </ul>
   * With this method, user can specify its own date format, replacing the current date format for all future Date
   * assertions in the test suite (i.e. not only the current assertions) since custom DateFormat is stored in a static
   * field.
   * <p/>
   * To revert to default format simply call {@link #useDefaultDateFormats()} (static method) or {@link
   * org.assertj.core.api.AbstractDateAssert#withDefaultDateFormats()}.
   *
   * @param userCustomDateFormatPattern the new Date format pattern used for String based Date assertions.
   */
  public static void useDateFormat(final String userCustomDateFormatPattern) {
    AbstractDateAssert.useDateFormat(userCustomDateFormatPattern);
  }

  /**
   * Use ISO 8601 date format ("yyyy-MM-dd") for String based Date assertions.
   */
  public static void useIsoDateFormat() {
    AbstractDateAssert.useIsoDateFormat();
  }

  /**
   * Use ISO 8601 date-time format (yyyy-MM-dd'T'HH:mm:ss), example : <code>2003-04-26T13:01:02</code>
   */
  public static void useIsoDateTimeFormat() {
    AbstractDateAssert.useDateFormat(Dates.newIsoDateTimeFormat());
  }

  /**
   * Use ISO 8601 date-time format (yyyy-MM-dd'T'HH:mm:ss), example : <code>2003-04-26T03:01:02.999</code>
   */
  public static void useIsoDateTimeWithMsFormat() {
    AbstractDateAssert.useDateFormat(Dates.newIsoDateTimeWithMsFormat());
  }

  /**
   * Use the defaults date formats to parse string as date.
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
  public static void useDefaultDateFormats() {
    AbstractDateAssert.useDefaultDateFormats();
  }

  /**
   * Creates a new </code>{@link Assertions}</code>.
   */
  protected Assertions() {
  }
}
