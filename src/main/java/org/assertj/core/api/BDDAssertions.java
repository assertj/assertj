/*
 * Created on Nov 16, 2013
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
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * BDD style entry point for assertion methods for different data types. Each method in this class is a static factory
 * for the type-specific assertion objects. The purpose of this class is to make test code more readable.
 * <p>
 * The difference with Assertions class is that entry point methods are named <code>then</code> instead of
 * <code>assertThat</code>.
 * <p>
 * For example:
 * <pre>
 * {@literal @}Test
 * public void bdd_assertions_examples() {
 *
 *   //given
 *   List&lt;BasketBallPlayer&gt; bulls = new ArrayList&lt;BasketBallPlayer&gt;();
 *
 *   //when
 *   bulls.add(rose);
 *   bulls.add(noah);
 *
 *   then(bulls).contains(rose, noah).doesNotContain(james);
 * }
 * </pre>
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
 * @author Mariusz Smykula
 */
public class BDDAssertions extends Assertions {

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
  public static AbstractClassAssert<?> then(Class<?> actual) {
    return assertThat(actual);
  }

  /**
   * Creates a new instance of <code>{@link org.assertj.core.api.IterableAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   */
  public static <T> AbstractIterableAssert<?, Iterable<T>, T> then(Iterable<T> actual) {
    return assertThat(actual);
  }

  /**
   * Creates a new instance of <code>{@link org.assertj.core.api.IterableAssert}</code>. The <code>{@link
   * java.util.Iterator}</code> is first
   * converted
   * into an <code>{@link Iterable}</code>
   *
   * @param actual the actual value.
   * @return the created assertion object.
   */
  public static <T> AbstractIterableAssert<?, Iterable<T>, T> then(Iterator<T> actual) {
    return assertThat(actual);
  }

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
   * Creates a new instance of <code>{@link org.assertj.core.api.FileAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   */
  public static AbstractFileAssert<?> then(File actual) {
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
   * @param actual the actual value.
   * @return the created assertion object.
   */
  public static <T> AbstractListAssert<?, List<T>, T> then(List<T> actual) {
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
   * Creates a new instance of <code>{@link org.assertj.core.api.ObjectAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   */
  public static <T> AbstractObjectAssert<?, T> then(T actual) {
    return assertThat(actual);
  }

  /**
   * Creates a new instance of <code>{@link org.assertj.core.api.ObjectArrayAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   */
  public static <T> AbstractObjectArrayAssert<?, T> then(T[] actual) {
    return assertThat(actual);
  }

  /**
   * Creates a new instance of <code>{@link org.assertj.core.api.MapAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   */
  public static <K, V> AbstractMapAssert<?, Map<K, V>, K, V> then(Map<K, V> actual) {
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
   * Creates a new instance of <code>{@link org.assertj.core.api.CharSequenceAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   */
  public static AbstractCharSequenceAssert<?, ?> then(CharSequence actual) {
    return assertThat(actual);
  }

  /**
   * Creates a new instance of <code>{@link org.assertj.core.api.StringAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   */
  public static AbstractCharSequenceAssert<?, ?> then(String actual) {
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
   * Creates a new instance of <code>{@link org.assertj.core.api.ThrowableAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion Throwable.
   */
  public static AbstractThrowableAssert<?, ?> then(Throwable actual) {
    return assertThat(actual);
  }

  /**
   * Creates a new </code>{@link org.assertj.core.api.BDDAssertions}</code>.
   */
  protected BDDAssertions() {
  }
}
