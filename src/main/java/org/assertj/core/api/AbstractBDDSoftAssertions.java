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

import org.assertj.core.api.ThrowableAssert.ThrowingCallable;

import java.io.File;
import java.io.InputStream;
import java.math.BigDecimal;
import java.nio.file.Path;
import java.time.*;
import java.util.*;

import static org.assertj.core.api.Assertions.catchThrowable;

public abstract class AbstractBDDSoftAssertions extends AbstractSoftAssertions {

  // then* methods duplicated from BDDAssertions
  
  /**
   * Creates a new instance of <code>{@link BigDecimalAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   */
  public BigDecimalAssert then(BigDecimal actual) {
	return proxy(BigDecimalAssert.class, BigDecimal.class, actual);
  }

  /**
   * Creates a new instance of <code>{@link BooleanAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   */
  public BooleanAssert then(boolean actual) {
	return proxy(BooleanAssert.class, Boolean.class, actual);
  }

  /**
   * Creates a new instance of <code>{@link BooleanAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   */
  public BooleanAssert then(Boolean actual) {
	return proxy(BooleanAssert.class, Boolean.class, actual);
  }

  /**
   * Creates a new instance of <code>{@link BooleanArrayAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   */
  public BooleanArrayAssert then(boolean[] actual) {
	return proxy(BooleanArrayAssert.class, boolean[].class, actual);
  }

  /**
   * Creates a new instance of <code>{@link ByteAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   */
  public ByteAssert then(byte actual) {
	return proxy(ByteAssert.class, Byte.class, actual);
  }

  /**
   * Creates a new instance of <code>{@link ByteAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   */
  public ByteAssert then(Byte actual) {
	return proxy(ByteAssert.class, Byte.class, actual);
  }

  /**
   * Creates a new instance of <code>{@link ByteArrayAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   */
  public ByteArrayAssert then(byte[] actual) {
	return proxy(ByteArrayAssert.class, byte[].class, actual);
  }

  /**
   * Creates a new instance of <code>{@link CharacterAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   */
  public CharacterAssert then(char actual) {
	return proxy(CharacterAssert.class, Character.class, actual);
  }

  /**
   * Creates a new instance of <code>{@link CharArrayAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   */
  public CharArrayAssert then(char[] actual) {
	return proxy(CharArrayAssert.class, char[].class, actual);
  }

  /**
   * Creates a new instance of <code>{@link CharacterAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   */
  public CharacterAssert then(Character actual) {
	return proxy(CharacterAssert.class, Character.class, actual);
  }

  /**
   * Creates a new instance of <code>{@link ClassAssert}</code>
   * </p> 
   * We don't return {@link ClassAssert} as it has overriden methods to annotated with {@link SafeVarargs}.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   */
  public SoftAssertionClassAssert then(Class<?> actual) {
	return proxy(SoftAssertionClassAssert.class, Class.class, actual);
  }

  /**
   * Creates a new instance of <code>{@link IterableAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   */
  public <T> IterableAssert<T> then(Iterable<? extends T> actual) {
	return proxy(IterableAssert.class, Iterable.class, actual);
  }

  /**
   * Creates a new instance of <code>{@link IterableAssert}</code>. The <code>{@link Iterator}</code> is first
   * converted
   * into an <code>{@link Iterable}</code>
   *
   * @param actual the actual value.
   * @return the created assertion object.
   */
  public <T> IterableAssert<T> then(Iterator<T> actual) {
	return proxy(IterableAssert.class, Iterator.class, actual);
  }

  /**
   * Creates a new instance of <code>{@link DoubleAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   */
  public DoubleAssert then(double actual) {
	return proxy(DoubleAssert.class, Double.class, actual);
  }

  /**
   * Creates a new instance of <code>{@link DoubleAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   */
  public DoubleAssert then(Double actual) {
	return proxy(DoubleAssert.class, Double.class, actual);
  }

  /**
   * Creates a new instance of <code>{@link DoubleArrayAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   */
  public DoubleArrayAssert then(double[] actual) {
	return proxy(DoubleArrayAssert.class, double[].class, actual);
  }

  /**
   * Creates a new instance of <code>{@link FileAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   */
  public FileAssert then(File actual) {
	return proxy(FileAssert.class, File.class, actual);
  }

  /**
   * Creates a new, proxied instance of a {@link PathAssert}
   *
   * @param actual the path
   * @return the created assertion object
   */
  public PathAssert then(Path actual) {
    return proxy(PathAssert.class, Path.class, actual);
  }

  /**
   * Creates a new instance of <code>{@link InputStreamAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   */
  public InputStreamAssert then(InputStream actual) {
	return proxy(InputStreamAssert.class, InputStream.class, actual);
  }

  /**
   * Creates a new instance of <code>{@link FloatAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   */
  public FloatAssert then(float actual) {
	return proxy(FloatAssert.class, Float.class, actual);
  }

  /**
   * Creates a new instance of <code>{@link FloatAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   */
  public FloatAssert then(Float actual) {
	return proxy(FloatAssert.class, Float.class, actual);
  }

  /**
   * Creates a new instance of <code>{@link FloatArrayAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   */
  public FloatArrayAssert then(float[] actual) {
	return proxy(FloatArrayAssert.class, float[].class, actual);
  }

  /**
   * Creates a new instance of <code>{@link IntegerAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   */
  public IntegerAssert then(int actual) {
	return proxy(IntegerAssert.class, Integer.class, actual);
  }

  /**
   * Creates a new instance of <code>{@link IntArrayAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   */
  public IntArrayAssert then(int[] actual) {
	return proxy(IntArrayAssert.class, int[].class, actual);
  }

  /**
   * Creates a new instance of <code>{@link IntegerAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   */
  public IntegerAssert then(Integer actual) {
	return proxy(IntegerAssert.class, Integer.class, actual);
  }

  /**
   * Creates a new instance of <code>{@link ListAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   */
  public <T> ListAssert<T> then(List<? extends T> actual) {
	return proxy(ListAssert.class, List.class, actual);
  }

  /**
   * Creates a new instance of <code>{@link LongAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   */
  public LongAssert then(long actual) {
	return proxy(LongAssert.class, Long.class, actual);
  }

  /**
   * Creates a new instance of <code>{@link LongAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   */
  public LongAssert then(Long actual) {
	return proxy(LongAssert.class, Long.class, actual);
  }

  /**
   * Creates a new instance of <code>{@link LongArrayAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   */
  public LongArrayAssert then(long[] actual) {
	return proxy(LongArrayAssert.class, long[].class, actual);
  }

  /**
   * Creates a new instance of <code>{@link ObjectAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   */
  public <T> ObjectAssert<T> then(T actual) {
	return proxy(ObjectAssert.class, Object.class, actual);
  }

  /**
   * Creates a new instance of <code>{@link ObjectArrayAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   */
  public <T> ObjectArrayAssert<T> then(T[] actual) {
	return proxy(ObjectArrayAssert.class, Object[].class, actual);
  }

  /**
   * Creates a new instance of <code>{@link MapAssert}</code>.
   * </p> 
   * We don't return {@link MapAssert} as it has overriden methods to annotated with {@link SafeVarargs}.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   */
  public <K, V> SoftAssertionMapAssert<K, V> then(Map<K, V> actual) {
	return proxy(SoftAssertionMapAssert.class, Map.class, actual);
  }

  /**
   * Creates a new instance of <code>{@link ShortAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   */
  public ShortAssert then(short actual) {
	return proxy(ShortAssert.class, Short.class, actual);
  }

  /**
   * Creates a new instance of <code>{@link ShortAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   */
  public ShortAssert then(Short actual) {
	return proxy(ShortAssert.class, Short.class, actual);
  }

  /**
   * Creates a new instance of <code>{@link ShortArrayAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   */
  public ShortArrayAssert then(short[] actual) {
	return proxy(ShortArrayAssert.class, short[].class, actual);
  }

  /**
   * Creates a new instance of <code>{@link CharSequenceAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   */
  public CharSequenceAssert then(CharSequence actual) {
	return proxy(CharSequenceAssert.class, CharSequence.class, actual);
  }

  /**
   * Creates a new instance of <code>{@link StringAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   */
  public StringAssert then(String actual) {
	return proxy(StringAssert.class, String.class, actual);
  }

  /**
   * Creates a new instance of <code>{@link DateAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   */
  public DateAssert then(Date actual) {
	return proxy(DateAssert.class, Date.class, actual);
  }

  /**
   * Creates a new instance of <code>{@link ThrowableAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion Throwable.
   */
  public ThrowableAssert then(Throwable actual) {
	return proxy(ThrowableAssert.class, Throwable.class, actual);
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
   *    BDDSoftAssertions softly = new BDDSoftAssertions();
   *    softly.thenThrownBy(() -> { throw new Exception("boom!") }).isInstanceOf(Exception.class)
   *                                                               .hasMessageContaining("boom");
   *  }
   * </code></pre>
   * 
   * <p>
   * Java 7 example :
   * </p>
   * 
   * <pre><code class='java'>
   * BDDSoftAssertions softly = new BDDSoftAssertions();
   * softly.thenThrownBy(new ThrowingCallable()
   * 
   *   {@literal @}Override
   *   public Void call() throws Exception {
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
  public AbstractThrowableAssert<?, ? extends Throwable> thenThrownBy(ThrowingCallable shouldRaiseThrowable) {
    return then(catchThrowable(shouldRaiseThrowable)).hasBeenThrown();
  }
  
  /**
   * Create assertion for {@link java.util.Optional}.
   *
   * @param actual the actual value.
   * @param <T> the type of the value contained in the {@link java.util.Optional}.
   *
   * @return the created assertion object.
   */
  public <T> OptionalAssert<T> then(Optional<T> actual) {
    return proxy(OptionalAssert.class, Optional.class, actual);
  }

  /**
   * Create assertion for {@link java.util.OptionalDouble}.
   *
   * @param actual the actual value.
   *
   * @return the created assertion object.
   */
  public OptionalDoubleAssert then(OptionalDouble actual) {
        return proxy(OptionalDoubleAssert.class, OptionalDouble.class, actual);
  }

  /**
   * Create assertion for {@link java.util.OptionalInt}.
   *
   * @param actual the actual value.
   *
   * @return the created assertion object.
   */
  public OptionalIntAssert then(OptionalInt actual) {
      return proxy(OptionalIntAssert.class, OptionalInt.class, actual);
  }

  /**
   * Create assertion for {@link java.util.OptionalLong}.
   *
   * @param actual the actual value.
   *
   * @return the created assertion object.
   */
  public OptionalLongAssert then(OptionalLong actual) {
      return proxy(OptionalLongAssert.class, OptionalLong.class, actual);
  }

    /**
   * Creates a new instance of <code>{@link LocalDateAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   */
  public LocalDateAssert then(LocalDate actual) {
    return proxy(LocalDateAssert.class, LocalDate.class, actual);
  }

  /**
   * Creates a new instance of <code>{@link LocalDateTimeAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   */
  public LocalDateTimeAssert then(LocalDateTime actual) {
    return proxy(LocalDateTimeAssert.class, LocalDateTime.class, actual);
  }
  
  /**
   * Creates a new instance of <code>{@link ZonedDateTimeAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   */
  public ZonedDateTimeAssert then(ZonedDateTime actual) {
    return proxy(ZonedDateTimeAssert.class, ZonedDateTime.class, actual);
  }
  
  /**
   * Creates a new instance of <code>{@link LocalTimeAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   */
  public LocalTimeAssert then(LocalTime actual) {
    return proxy(LocalTimeAssert.class, LocalTime.class, actual);
  }

  /**
   * Creates a new instance of <code>{@link OffsetTimeAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   */
  public OffsetTimeAssert then(OffsetTime actual) {
        return proxy(OffsetTimeAssert.class, OffsetTime.class, actual);
    }

  /**
   * Creates a new instance of <code>{@link OffsetTimeAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   */
  public OffsetDateTimeAssert then(OffsetDateTime actual) {
      return proxy(OffsetDateTimeAssert.class, OffsetDateTime.class, actual);
  }
}
