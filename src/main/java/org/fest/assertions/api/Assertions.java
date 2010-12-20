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
 * Copyright @2010 the original author or authors.
 */
package org.fest.assertions.api;

import java.awt.image.BufferedImage;
import java.util.Collection;
import java.util.List;

/**
 * Entry point for assertion methods for different data types. Each method in this class is a static
 * factory for the type-specific assertion objects. The purpose of this class is to make test code more readable.
 * <p>
 * For example:
 * <pre>
 * int removed = employees.removeFired();
 * {@link Assertions#assertThat(int) assertThat}(removed).{@link IntegerAssert#isZero isZero}();
 *
 * List&lt;Employee&gt; newEmployees = employees.hired(TODAY);
 * {@link Assertions#assertThat(Collection) assertThat}(newEmployees).{@link CollectionAssert#hasSize(int) hasSize}(6);
 * </pre>
 * </p>
 *
 * @author Alex Ruiz
 * @author Yvonne Wang
 * @author David DIDIER
 * @author Ted Young
 */
public class Assertions {

  /**
   * Creates a new instance of <code>{@link BooleanAssert}</code>.
   * @param actual the actual value.
   * @return the created assertion object.
   */
  public static BooleanAssert assertThat(boolean actual) {
    return new BooleanAssert(actual);
  }

  /**
   * Creates a new instance of <code>{@link BooleanAssert}</code>.
   * @param actual the actual value.
   * @return the created assertion object.
   */
  public static BooleanAssert assertThat(Boolean actual) {
    return new BooleanAssert(actual);
  }

  /**
   * Creates a new instance of <code>{@link BooleanArrayAssert}</code>.
   * @param actual the actual value.
   * @return the created assertion object.
   */
  public static BooleanArrayAssert assertThat(boolean[] actual) {
    return new BooleanArrayAssert(actual);
  }

  /**
   * Creates a new instance of <code>{@link ImageAssert}</code>.
   * @param actual the actual value.
   * @return the created assertion object.
   */
  public static ImageAssert assertThat(BufferedImage actual) {
    return new ImageAssert(actual);
  }

  /**
   * Creates a new instance of <code>{@link ByteAssert}</code>.
   * @param actual the actual value.
   * @return the created assertion object.
   */
  public static ByteAssert assertThat(byte actual) {
    return new ByteAssert(actual);
  }

  /**
   * Creates a new instance of <code>{@link ByteAssert}</code>.
   * @param actual the actual value.
   * @return the created assertion object.
   */
  public static ByteAssert assertThat(Byte actual) {
    return new ByteAssert(actual);
  }

  /**
   * Creates a new instance of <code>{@link ByteArrayAssert}</code>.
   * @param actual the actual value.
   * @return the created assertion object.
   */
  public static ByteArrayAssert assertThat(byte[] actual) {
    return new ByteArrayAssert(actual);
  }

  /**
   * Creates a new instance of <code>{@link CharacterAssert}</code>.
   * @param actual the actual value.
   * @return the created assertion object.
   */
  public static CharacterAssert assertThat(char actual) {
    return new CharacterAssert(actual);
  }

  /**
   * Creates a new instance of <code>{@link CharArrayAssert}</code>.
   * @param actual the actual value.
   * @return the created assertion object.
   */
  public static CharArrayAssert assertThat(char[] actual) {
    return new CharArrayAssert(actual);
  }

  /**
   * Creates a new instance of <code>{@link CharacterAssert}</code>.
   * @param actual the actual value.
   * @return the created assertion object.
   */
  public static CharacterAssert assertThat(Character actual) {
    return new CharacterAssert(actual);
  }

  /**
   * Creates a new instance of <code>{@link CollectionAssert}</code>.
   * @param actual the actual value.
   * @return the created assertion object.
   */
  public static CollectionAssert assertThat(Collection<?> actual) {
    return new CollectionAssert(actual);
  }

  /**
   * Creates a new instance of <code>{@link DoubleAssert}</code>.
   * @param actual the actual value.
   * @return the created assertion object.
   */
  public static DoubleAssert assertThat(double actual) {
    return new DoubleAssert(actual);
  }

  /**
   * Creates a new instance of <code>{@link DoubleAssert}</code>.
   * @param actual the actual value.
   * @return the created assertion object.
   */
  public static DoubleAssert assertThat(Double actual) {
    return new DoubleAssert(actual);
  }

  /**
   * Creates a new instance of <code>{@link FloatAssert}</code>.
   * @param actual the actual value.
   * @return the created assertion object.
   */
  public static FloatAssert assertThat(float actual) {
    return new FloatAssert(actual);
  }

  /**
   * Creates a new instance of <code>{@link FloatAssert}</code>.
   * @param actual the actual value.
   * @return the created assertion object.
   */
  public static FloatAssert assertThat(Float actual) {
    return new FloatAssert(actual);
  }

  /**
   * Creates a new instance of <code>{@link IntegerAssert}</code>.
   * @param actual the actual value.
   * @return the created assertion object.
   */
  public static IntegerAssert assertThat(int actual) {
    return new IntegerAssert(actual);
  }

  /**
   * Creates a new instance of <code>{@link IntArrayAssert}</code>.
   * @param actual the actual value.
   * @return the created assertion object.
   */
  public static IntArrayAssert assertThat(int[] actual) {
    return new IntArrayAssert(actual);
  }

  /**
   * Creates a new instance of <code>{@link IntegerAssert}</code>.
   * @param actual the actual value.
   * @return the created assertion object.
   */
  public static IntegerAssert assertThat(Integer actual) {
    return new IntegerAssert(actual);
  }

  /**
   * Creates a new instance of <code>{@link ListAssert}</code>.
   * @param actual the actual value.
   * @return the created assertion object.
   */
  public static ListAssert assertThat(List<?> actual) {
    return new ListAssert(actual);
  }

  /**
   * Creates a new instance of <code>{@link LongAssert}</code>.
   * @param actual the actual value.
   * @return the created assertion object.
   */
  public static LongAssert assertThat(long actual) {
    return new LongAssert(actual);
  }

  /**
   * Creates a new instance of <code>{@link LongAssert}</code>.
   * @param actual the actual value.
   * @return the created assertion object.
   */
  public static LongAssert assertThat(Long actual) {
    return new LongAssert(actual);
  }

  /**
   * Creates a new instance of <code>{@link ObjectArrayAssert}</code>.
   * @param actual the actual value.
   * @return the created assertion object.
   */
  public static ObjectArrayAssert assertThat(Object[] actual) {
    return new ObjectArrayAssert(actual);
  }

  /**
   * Creates a new instance of <code>{@link ShortAssert}</code>.
   * @param actual the actual value.
   * @return the created assertion object.
   */
  public static ShortAssert assertThat(short actual) {
    return new ShortAssert(actual);
  }

  /**
   * Creates a new instance of <code>{@link ShortAssert}</code>.
   * @param actual the actual value.
   * @return the created assertion object.
   */
  public static ShortAssert assertThat(Short actual) {
    return new ShortAssert(actual);
  }

  /** Creates a new </code>{@link Assertions}</code>. */
  protected Assertions() {}
}
