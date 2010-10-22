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

import java.util.Collection;

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
   * Creates a new instance of <code>{@link CollectionAssert}</code>.
   * @param actual the actual value.
   * @return the created assertion object.
   */
  public static CollectionAssert assertThat(Collection<?> actual) {
    return new CollectionAssert(actual);
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
   * Creates a new instance of <code>{@link IntegerAssert}</code>.
   * @param actual the actual value.
   * @return the created assertion object.
   */
  public static IntegerAssert assertThat(Integer actual) {
    return new IntegerAssert(actual);
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
