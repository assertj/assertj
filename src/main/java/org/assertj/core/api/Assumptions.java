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

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.assertj.core.util.CheckReturnValue;

import java.io.File;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URI;
import java.net.URL;
import java.nio.file.Path;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
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

import static org.assertj.core.api.Assertions.catchThrowable;

/**
 * Entry point for assumption methods for different types, which allow to skip test execution on failed assumptions.
 * @since 2.9.0
 */
public class Assumptions {

  /**
   * Creates a new instance of <code>{@link ObjectAssert}</code> assumption.
   *
   * @param actual the actual value.
   * @param <T> the type of the actual value.
   * @return the created assumption for assertion object.
   */
  @CheckReturnValue
  @SuppressWarnings("unchecked")
  public static <T> AbstractObjectAssert<?, T> assumeThat(T actual) {
    return enhanceAsAssumption(ObjectAssert.class, Object.class, actual);
  }

  /**
   * Creates a new instance of <code>{@link StringAssert}</code> assumption.
   *
   * @param actual the actual value.
   * @return the created assumption for assertion object.
   */
  @CheckReturnValue
  public static AbstractCharSequenceAssert<?, String> assumeThat(String actual) {
    return enhanceAsAssumption(StringAssert.class, String.class, actual);
  }

  /**
   * Creates a new instance of <code>{@link BigDecimalAssert}</code> assumption.
   *
   * @param actual the actual value.
   * @return the created assumption for assertion object.
   */
  @CheckReturnValue
  public static AbstractBigDecimalAssert<?> assumeThat(BigDecimal actual) {
    return enhanceAsAssumption(BigDecimalAssert.class, BigDecimal.class, actual);
  }

  /**
   * Creates a new instance of <code>{@link BigIntegerAssert}</code> assumption.
   *
   * @param actual the actual value.
   * @return the created assumption for assertion object.
   */
  @CheckReturnValue
  public static AbstractBigIntegerAssert<?> assumeThat(BigInteger actual) {
    return enhanceAsAssumption(BigIntegerAssert.class, BigInteger.class, actual);
  }

  /**
   * Creates a new instance of <code>{@link UriAssert}</code> assumption.
   *
   * @param actual the actual value.
   * @return the created assumption for assertion object.
   */
  @CheckReturnValue
  public static AbstractUriAssert<?> assumeThat(URI actual) {
    return enhanceAsAssumption(UriAssert.class, URI.class, actual);
  }

  /**
   * Creates a new instance of <code>{@link UrlAssert}</code> assumption.
   *
   * @param actual the actual value.
   * @return the created assumption for assertion object.
   */
  @CheckReturnValue
  public static AbstractUrlAssert<?> assumeThat(URL actual) {
    return enhanceAsAssumption(UrlAssert.class, URL.class, actual);
  }

  /**
   * Creates a new instance of <code>{@link BooleanAssert}</code> assumption.
   *
   * @param actual the actual value.
   * @return the created assumption for assertion object.
   */
  @CheckReturnValue
  public static AbstractBooleanAssert<?> assumeThat(boolean actual) {
    return enhanceAsAssumption(BooleanAssert.class, Boolean.class, actual);
  }

  /**
   * Creates a new instance of <code>{@link BooleanAssert}</code> assumption.
   *
   * @param actual the actual value.
   * @return the created assumption for assertion object.
   */
  @CheckReturnValue
  public static AbstractBooleanAssert<?> assumeThat(Boolean actual) {
    return enhanceAsAssumption(BooleanAssert.class, Boolean.class, actual);
  }

  /**
   * Creates a new instance of <code>{@link BooleanArrayAssert}</code> assumption.
   *
   * @param actual the actual value.
   * @return the created assumption for assertion object.
   */
  @CheckReturnValue
  public static AbstractBooleanArrayAssert<?> assumeThat(boolean[] actual) {
    return enhanceAsAssumption(BooleanArrayAssert.class, boolean[].class, actual);
  }

  /**
   * Creates a new instance of <code>{@link ByteAssert}</code> assumption.
   *
   * @param actual the actual value.
   * @return the created assumption for assertion object.
   */
  @CheckReturnValue
  public static AbstractByteAssert<?> assumeThat(byte actual) {
    return enhanceAsAssumption(ByteAssert.class, Byte.class, actual);
  }

  /**
   * Creates a new instance of <code>{@link ByteAssert}</code> assumption.
   *
   * @param actual the actual value.
   * @return the created assumption for assertion object.
   */
  @CheckReturnValue
  public static AbstractByteAssert<?> assumeThat(Byte actual) {
    return enhanceAsAssumption(ByteAssert.class, Byte.class, actual);
  }

  /**
   * Creates a new instance of <code>{@link ByteArrayAssert}</code> assumption.
   *
   * @param actual the actual value.
   * @return the created assumption for assertion object.
   */
  @CheckReturnValue
  public static AbstractByteArrayAssert<?> assumeThat(byte[] actual) {
    return enhanceAsAssumption(ByteArrayAssert.class, byte[].class, actual);
  }

  /**
   * Creates a new instance of <code>{@link CharacterAssert}</code> assumption.
   *
   * @param actual the actual value.
   * @return the created assumption for assertion object.
   */
  @CheckReturnValue
  public static AbstractCharacterAssert<?> assumeThat(char actual) {
    return enhanceAsAssumption(CharacterAssert.class, Character.class, actual);
  }

  /**
   * Creates a new instance of <code>{@link CharacterAssert}</code> assumption.
   *
   * @param actual the actual value.
   * @return the created assumption for assertion object.
   */
  @CheckReturnValue
  public static AbstractCharacterAssert<?> assumeThat(Character actual) {
    return enhanceAsAssumption(CharacterAssert.class, Character.class, actual);
  }

  /**
   * Creates a new instance of <code>{@link CharArrayAssert}</code> assumption.
   *
   * @param actual the actual value.
   * @return the created assumption for assertion object.
   */
  @CheckReturnValue
  public static AbstractCharArrayAssert<?> assumeThat(char[] actual) {
    return enhanceAsAssumption(CharArrayAssert.class, char[].class, actual);
  }

  /**
   * Creates a new instance of <code>{@link CharSequenceAssert}</code> assumption.
   *
   * @param actual the actual value.
   * @return the created assumption for assertion object.
   */
  @CheckReturnValue
  public static AbstractCharSequenceAssert<?, ? extends CharSequence> assumeThat(CharSequence actual) {
    return enhanceAsAssumption(CharSequenceAssert.class, CharSequence.class, actual);
  }

  /**
   * Creates a new instance of <code>{@link ShortAssert}</code> assumption.
   *
   * @param actual the actual value.
   * @return the created assumption for assertion object.
   */
  @CheckReturnValue
  public static AbstractShortAssert<?> assumeThat(short actual) {
    return enhanceAsAssumption(ShortAssert.class, Short.class, actual);
  }

  /**
   * Creates a new instance of <code>{@link ShortAssert}</code> assumption.
   *
   * @param actual the actual value.
   * @return the created assumption for assertion object.
   */
  @CheckReturnValue
  public static AbstractShortAssert<?> assumeThat(Short actual) {
    return enhanceAsAssumption(ShortAssert.class, Short.class, actual);
  }

  /**
   * Creates a new instance of <code>{@link ShortArrayAssert}</code> assumption.
   *
   * @param actual the actual value.
   * @return the created assumption for assertion object.
   */
  @CheckReturnValue
  public static AbstractShortArrayAssert<?> assumeThat(short[] actual) {
    return enhanceAsAssumption(ShortArrayAssert.class, short[].class, actual);
  }

  /**
   * Creates a new instance of <code>{@link IntegerAssert}</code> assumption.
   *
   * @param actual the actual value.
   * @return the created assumption for assertion object.
   */
  @CheckReturnValue
  public static AbstractIntegerAssert<?> assumeThat(int actual) {
    return enhanceAsAssumption(IntegerAssert.class, Integer.class, actual);
  }

  /**
   * Creates a new instance of <code>{@link IntegerAssert}</code> assumption.
   *
   * @param actual the actual value.
   * @return the created assumption for assertion object.
   */
  @CheckReturnValue
  public static AbstractIntegerAssert<?> assumeThat(Integer actual) {
    return enhanceAsAssumption(IntegerAssert.class, Integer.class, actual);
  }

  /**
   * Creates a new instance of <code>{@link IntArrayAssert}</code> assumption.
   *
   * @param actual the actual value.
   * @return the created assumption for assertion object.
   */
  @CheckReturnValue
  public static AbstractIntArrayAssert<?> assumeThat(int[] actual) {
    return enhanceAsAssumption(IntArrayAssert.class, int[].class, actual);
  }

  /**
   * Creates a new instance of <code>{@link LongAssert}</code> assumption.
   *
   * @param actual the actual value.
   * @return the created assumption for assertion object.
   */
  @CheckReturnValue
  public static AbstractLongAssert<?> assumeThat(long actual) {
    return enhanceAsAssumption(LongAssert.class, Long.class, actual);
  }

  /**
   * Creates a new instance of <code>{@link LongAssert}</code> assumption.
   *
   * @param actual the actual value.
   * @return the created assumption for assertion object.
   */
  @CheckReturnValue
  public static AbstractLongAssert<?> assumeThat(Long actual) {
    return enhanceAsAssumption(LongAssert.class, Long.class, actual);
  }

  /**
   * Creates a new instance of <code>{@link LongArrayAssert}</code> assumption.
   *
   * @param actual the actual value.
   * @return the created assumption for assertion object.
   */
  @CheckReturnValue
  public static AbstractLongArrayAssert<?> assumeThat(long[] actual) {
    return enhanceAsAssumption(LongArrayAssert.class, long[].class, actual);
  }

  /**
   * Creates a new instance of <code>{@link FloatAssert}</code> assumption.
   *
   * @param actual the actual value.
   * @return the created assumption for assertion object.
   */
  @CheckReturnValue
  public static AbstractFloatAssert<?> assumeThat(float actual) {
    return enhanceAsAssumption(FloatAssert.class, Float.class, actual);
  }

  /**
   * Creates a new instance of <code>{@link FloatAssert}</code> assumption.
   *
   * @param actual the actual value.
   * @return the created assumption for assertion object.
   */
  @CheckReturnValue
  public static AbstractFloatAssert<?> assumeThat(Float actual) {
    return enhanceAsAssumption(FloatAssert.class, Float.class, actual);
  }

  /**
   * Creates a new instance of <code>{@link FloatArrayAssert}</code> assumption.
   *
   * @param actual the actual value.
   * @return the created assumption for assertion object.
   */
  @CheckReturnValue
  public static AbstractFloatArrayAssert<?> assumeThat(float[] actual) {
    return enhanceAsAssumption(FloatArrayAssert.class, float[].class, actual);
  }

  /**
   * Creates a new instance of <code>{@link DoubleAssert}</code> assumption.
   *
   * @param actual the actual value.
   * @return the created assumption for assertion object.
   */
  @CheckReturnValue
  public static AbstractDoubleAssert<?> assumeThat(double actual) {
    return enhanceAsAssumption(DoubleAssert.class, Double.class, actual);
  }

  /**
   * Creates a new instance of <code>{@link DoubleAssert}</code> assumption.
   *
   * @param actual the actual value.
   * @return the created assumption for assertion object.
   */
  @CheckReturnValue
  public static AbstractDoubleAssert<?> assumeThat(Double actual) {
    return enhanceAsAssumption(DoubleAssert.class, Double.class, actual);
  }

  /**
   * Creates a new instance of <code>{@link DoubleArrayAssert}</code> assumption.
   *
   * @param actual the actual value.
   * @return the created assumption for assertion object.
   */
  @CheckReturnValue
  public static AbstractDoubleArrayAssert<?> assumeThat(double[] actual) {
    return enhanceAsAssumption(DoubleArrayAssert.class, double[].class, actual);
  }

  /**
   * Creates assumption for {@link AtomicBoolean}.
   *
   * @param actual the actual value.
   * @return the created assumption for assertion object.
   */
  @CheckReturnValue
  public static AtomicBooleanAssert assumeThat(AtomicBoolean actual) {
    return enhanceAsAssumption(AtomicBooleanAssert.class, AtomicBoolean.class, actual);
  }

  /**
   * Creates assumption for {@link AtomicInteger}.
   *
   * @param actual the actual value.
   * @return the created assumption for assertion object.
   */
  @CheckReturnValue
  public static AtomicIntegerAssert assumeThat(AtomicInteger actual) {
    return enhanceAsAssumption(AtomicIntegerAssert.class, AtomicInteger.class, actual);
  }

  /**
   * Creates int[] assumption for {@link AtomicIntegerArray}.
   *
   * @param actual the actual value.
   * @return the created assumption for assertion object.
   */
  @CheckReturnValue
  public static AtomicIntegerArrayAssert assumeThat(AtomicIntegerArray actual) {
    return enhanceAsAssumption(AtomicIntegerArrayAssert.class, AtomicIntegerArray.class, actual);
  }

  /**
   * Creates assumption for {@link AtomicIntegerFieldUpdater}.
   *
   * @param actual the actual value.
   * @param <OBJECT> the type of the object holding the updatable field.
   * @return the created assumption for assertion object.
   */
  @CheckReturnValue
  @SuppressWarnings("unchecked")
  public static <OBJECT> AtomicIntegerFieldUpdaterAssert<OBJECT> assumeThat(AtomicIntegerFieldUpdater<OBJECT> actual) {
    return enhanceAsAssumption(AtomicIntegerFieldUpdaterAssert.class, AtomicIntegerFieldUpdater.class, actual);
  }

  /**
   * Creates assumption for {@link AtomicLong}.
   *
   * @param actual the actual value.
   * @return the created assumption for assertion object.
   */
  @CheckReturnValue
  public static AtomicLongAssert assumeThat(AtomicLong actual) {
    return enhanceAsAssumption(AtomicLongAssert.class, AtomicLong.class, actual);
  }

  /**
   * Creates assumption for {@link AtomicLongArray}.
   *
   * @param actual the actual value.
   * @return the created assumption for assertion object.
   */
  @CheckReturnValue
  public static AtomicLongArrayAssert assumeThat(AtomicLongArray actual) {
    return enhanceAsAssumption(AtomicLongArrayAssert.class, AtomicLongArray.class, actual);
  }

  /**
   * Creates assumption for {@link AtomicLongFieldUpdater}.
   *
   * @param actual the actual value.
   * @param <OBJECT> the type of the object holding the updatable field.
   * @return the created assumption for assertion object.
   */
  @CheckReturnValue
  @SuppressWarnings("unchecked")
  public static <OBJECT> AtomicLongFieldUpdaterAssert<OBJECT> assumeThat(AtomicLongFieldUpdater<OBJECT> actual) {
    return enhanceAsAssumption(AtomicLongFieldUpdaterAssert.class, AtomicLongFieldUpdater.class, actual);
  }

  /**
   * Creates assumption for {@link AtomicReference}.
   *
   * @param actual the actual value.
   * @param <VALUE> the type of the value contained in the {@link AtomicReference}.
   * @return the created assumption for assertion object.
   */
  @CheckReturnValue
  @SuppressWarnings("unchecked")
  public static <VALUE> AtomicReferenceAssert<VALUE> assumeThat(AtomicReference<VALUE> actual) {
    return enhanceAsAssumption(AtomicReferenceAssert.class, AtomicReference.class, actual);
  }

  /**
   * Creates assumption for {@link AtomicReferenceArray}.
   *
   * @param actual the actual value.
   * @param <ELEMENT> the type of the value contained in the {@link AtomicReferenceArray}.
   * @return the created assumption for assertion object.
   */
  @CheckReturnValue
  @SuppressWarnings("unchecked")
  public static <ELEMENT> AtomicReferenceArrayAssert<ELEMENT> assumeThat(AtomicReferenceArray<ELEMENT> actual) {
    return enhanceAsAssumption(AtomicReferenceArrayAssert.class, AtomicReferenceArray.class, actual);
  }

  /**
   * Creates assumption for {@link AtomicReferenceFieldUpdater}.
   *
   * @param actual the actual value.
   * @param <FIELD> the type of the field which gets updated by the {@link AtomicReferenceFieldUpdater}.
   * @param <OBJECT> the type of the object holding the updatable field.
   * @return the created assumption for assertion object.
   */
  @CheckReturnValue
  @SuppressWarnings("unchecked")
  public static <FIELD, OBJECT> AtomicReferenceFieldUpdaterAssert<FIELD, OBJECT> assumeThat(AtomicReferenceFieldUpdater<OBJECT, FIELD> actual) {
    return enhanceAsAssumption(AtomicReferenceFieldUpdaterAssert.class, AtomicReferenceFieldUpdater.class, actual);
  }

  /**
   * Creates assumption for {@link AtomicMarkableReference}.
   *
   * @param actual the actual value.
   * @param <VALUE> the type of the value contained in the {@link AtomicMarkableReference}.
   * @return the created assumption for assertion object.
   */
  @CheckReturnValue
  @SuppressWarnings("unchecked")
  public static <VALUE> AtomicMarkableReferenceAssert<VALUE> assumeThat(AtomicMarkableReference<VALUE> actual) {
    return enhanceAsAssumption(AtomicMarkableReferenceAssert.class, AtomicMarkableReference.class, actual);
  }

  /**
   * Creates assumption for {@link AtomicStampedReference}.
   *
   * @param actual the actual value.
   * @param <VALUE> the type of the value contained in the {@link AtomicStampedReference}.
   * @return the created assumption for assertion object.
   */
  @CheckReturnValue
  @SuppressWarnings("unchecked")
  public static <VALUE> AtomicStampedReferenceAssert<VALUE> assumeThat(AtomicStampedReference<VALUE> actual) {
    return enhanceAsAssumption(AtomicStampedReferenceAssert.class, AtomicStampedReference.class, actual);
  }

  /**
   * Creates a new instance of <code>{@link ClassAssert}</code> assumption.
   *
   * @param actual the actual value.
   * @return the created assumption for assertion object.
   */
  @CheckReturnValue
  public static AbstractClassAssert<?> assumeThat(Class<?> actual) {
    return enhanceAsAssumption(ClassAssert.class, Class.class, actual);
  }

  /**
   * Creates a new instance of <code>{@link DateAssert}</code> assumption.
   *
   * @param actual the actual value.
   * @return the created assumption for assertion object.
   */
  @CheckReturnValue
  public static AbstractDateAssert<?> assumeThat(Date actual) {
    return enhanceAsAssumption(DateAssert.class, Date.class, actual);
  }

  /**
   * Creates a new instance of <code>{@link FileAssert}</code> assumption.
   *
   * @param actual the actual value.
   * @return the created assumption for assertion object.
   */
  @CheckReturnValue
  public static AbstractFileAssert<?> assumeThat(File actual) {
    return enhanceAsAssumption(FileAssert.class, File.class, actual);
  }

  /**
   * Creates a new instance of {@link PathAssert} assumption.
   *
   * @param actual the path to test
   * @return the created assumption for assertion object.
   */
  @CheckReturnValue
  public static AbstractPathAssert<?> assumeThat(Path actual) {
    return enhanceAsAssumption(PathAssert.class, Path.class, actual);
  }

  /**
   * Creates a new instance of <code>{@link InputStreamAssert}</code> assumption.
   *
   * @param actual the actual value.
   * @return the created assumption for assertion object.
   */
  @CheckReturnValue
  public static AbstractInputStreamAssert<?, ? extends InputStream> assumeThat(InputStream actual) {
    return enhanceAsAssumption(InputStreamAssert.class, InputStream.class, actual);
  }

  /**
   * Create assertion for {@link java.util.concurrent.Future} assumption.
   *
   * @param actual the actual value.
   * @param <RESULT> the type of the value contained in the {@link java.util.concurrent.Future}.
   *
   * @return the created assumption for assertion object.
   */
  @CheckReturnValue
  @SuppressWarnings("unchecked")
  public static <RESULT> AbstractFutureAssert<?, ? extends Future<? extends RESULT>, RESULT> assumeThat(Future<RESULT> actual) {
    return (FutureAssert<RESULT>) enhanceAsAssumption(FutureAssert.class, Future.class, actual);
  }

  /**
   * Creates a new instance of <code>{@link IterableAssert}</code> assumption.
   *
   * @param actual the actual value.
   * @return the created assumption for assertion object.
   */
  @CheckReturnValue
  @SuppressWarnings("unchecked")
  public static <ELEMENT> FactoryBasedNavigableIterableAssert<IterableAssert<ELEMENT>, Iterable<? extends ELEMENT>, ELEMENT, ObjectAssert<ELEMENT>> assumeThat(Iterable<? extends ELEMENT> actual) {
    return enhanceAsAssumption(IterableAssert.class, Iterable.class, actual);
  }

  /**
   * Creates a new instance of <code>{@link IterableAssert}</code> assumption.
   *
   * @param actual the actual value.
   * @return the created assumption for assertion object.
   */
  @CheckReturnValue
  @SuppressWarnings("unchecked")
  public static <ELEMENT> FactoryBasedNavigableIterableAssert<IterableAssert<ELEMENT>, Iterable<? extends ELEMENT>, ELEMENT, ObjectAssert<ELEMENT>> assumeThat(Iterator<? extends ELEMENT> actual) {
    return enhanceAsAssumption(IterableAssert.class, Iterator.class, actual);
  }

  /**
   * Creates a new instance of <code>{@link ListAssert}</code> assumption.
   *
   * @param actual the actual value.
   * @return the created assumption for assertion object.
   */
  @CheckReturnValue
  @SuppressWarnings("unchecked")
  public static <ELEMENT> FactoryBasedNavigableListAssert<ListAssert<ELEMENT>, List<? extends ELEMENT>, ELEMENT, ObjectAssert<ELEMENT>> assumeThat(List<? extends ELEMENT> actual) {
    return enhanceAsAssumption(ListAssert.class, List.class, actual);
  }

  /**
   * Creates a new instance of <code>{@link ObjectArrayAssert}</code> assumption.
   *
   * @param actual the actual value.
   * @return the created assumption for assertion object.
   */
  @CheckReturnValue
  @SuppressWarnings("unchecked")
  public static <T> AbstractObjectArrayAssert<?, T> assumeThat(T[] actual) {
    return enhanceAsAssumption(ObjectArrayAssert.class, Object[].class, actual);
  }

  /**
   * Creates a new instance of <code>{@link MapAssert}</code> assumption.
   *
   * @param <K> the type of keys in the map.
   * @param <V> the type of values in the map.
   * @param actual the actual value.
   * @return the created assumption for assertion object.
   */
  @CheckReturnValue
  @SuppressWarnings("unchecked")
  public static <K, V> AbstractMapAssert<?, ?, K, V> assumeThat(Map<K, V> actual) {
    return enhanceAsAssumption(MapAssert.class, Map.class, actual);
  }

  /**
   * Creates a new instance of <code>{@link ThrowableAssert}</code> assumption.
   *
   * @param actual the actual value.
   * @return the created assumption for assertion object.
   */
  @CheckReturnValue
  public static AbstractThrowableAssert<?, ? extends Throwable> assumeThat(Throwable actual) {
    return enhanceAsAssumption(ThrowableAssert.class, Throwable.class, actual);
  }

  /**
   * Allows to capture and then assume on a {@link Throwable} (done easier when used with Java 8 lambdas).
   *
   * @param shouldRaiseThrowable The {@link ThrowingCallable} or lambda with the code that should raise the throwable.
   * @return the created assumption for assertion object.
   */
  @CheckReturnValue
  public static AbstractThrowableAssert<?, ? extends Throwable> assumeThatThrownBy(ThrowingCallable shouldRaiseThrowable) {
    return enhanceAsAssumption(ThrowableAssert.class, Throwable.class, catchThrowable(shouldRaiseThrowable));
  }

  @SuppressWarnings("unchecked")
  private static <ASSERTION, ACTUAL> ASSERTION enhanceAsAssumption(Class<ASSERTION> assertionType,
                                                                   Class<ACTUAL> actualType,
                                                                   Object actual) {
    Enhancer enhancer = new Enhancer();
    enhancer.setSuperclass(assertionType);
    enhancer.setCallback(new MethodInterceptor() {
      @Override
      public Object intercept(Object o, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {
        try {
          Object result = methodProxy.invokeSuper(o, args);
          if (result != o && result instanceof AbstractAssert) {
            return enhanceAsAssumption(result);
          }
          return result;
        } catch (AssertionError e) {
          throw createAssumptionViolatedException(e);
        }
      }
    });

    return (ASSERTION) enhancer.create(new Class[] { actualType }, new Object[] { actual });
  }

  private static RuntimeException createAssumptionViolatedException(AssertionError e) throws ReflectiveOperationException {
    try {
      return createAssumptionViolatedException(Class.forName("org.junit.AssumptionViolatedException"), e);
    } catch (ClassNotFoundException junitClassNotFound) {
      try {
        return createAssumptionViolatedException(Class.forName("org.testng.SkipException"), e);
      } catch (ClassNotFoundException testngClassNotFound) {
        throw new IllegalStateException("Assumptions require JUnit or TestNG in the classpath");
      }
    }
  }

  private static RuntimeException createAssumptionViolatedException(Class<?> exceptionClass,
                                                                    AssertionError e) throws ReflectiveOperationException {
    return (RuntimeException) exceptionClass.getConstructor(String.class, Throwable.class)
                                            .newInstance("assumption failed due to assertion error", e);
  }

  private static Object enhanceAsAssumption(Object assertion) {
    if (assertion instanceof StringAssert) {
      return enhanceAsAssumption(StringAssert.class, String.class, ((StringAssert) assertion).actual);
    }
    if (assertion instanceof ListAssert) {
      return enhanceAsAssumption(ListAssert.class, List.class, ((ListAssert) assertion).actual);
    }
    if (assertion instanceof ObjectArrayAssert) {
      return enhanceAsAssumption(ObjectArrayAssert.class, Object[].class, ((ObjectArrayAssert) assertion).actual);
    }
    if (assertion instanceof ObjectAssert) {
      return enhanceAsAssumption(ObjectAssert.class, Object.class,
        ((ObjectAssert)assertion).actual).as(((ObjectAssert) assertion).descriptionText());
    }

    throw new IllegalArgumentException("Unsupported assumption creation for " + assertion.getClass());
  }
}
