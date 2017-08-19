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

import static org.assertj.core.api.Assertions.catchThrowable;
import static org.assertj.core.util.Arrays.array;

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

import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.assertj.core.util.CheckReturnValue;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

/**
 * Entry point for assumption methods for different types, which allow to skip test execution on failed assumptions.
 * @since 2.9.0 / 3.9.0
 */
public class Assumptions {

  private static final class AssumptiomMethodInterceptor implements MethodInterceptor {
    @Override
    public Object intercept(Object assertion, Method method, Object[] args,
                            MethodProxy methodProxy) throws Throwable {
      try {
        Object result = methodProxy.invokeSuper(assertion, args);
        if (result != assertion && result instanceof AbstractAssert) {
          return asAssumption((AbstractAssert<?, ?>) result);
        }
        return result;
      } catch (AssertionError e) {
        throw assumptionNotMet(e);
      }
    }
  }

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
    return asAssumption(ObjectAssert.class, Object.class, actual);
  }

  /**
   * Creates a new instance of <code>{@link StringAssert}</code> assumption.
   *
   * @param actual the actual value.
   * @return the created assumption for assertion object.
   */
  @CheckReturnValue
  public static AbstractCharSequenceAssert<?, String> assumeThat(String actual) {
    return asAssumption(StringAssert.class, String.class, actual);
  }

  /**
   * Creates a new instance of <code>{@link BigDecimalAssert}</code> assumption.
   *
   * @param actual the actual value.
   * @return the created assumption for assertion object.
   */
  @CheckReturnValue
  public static AbstractBigDecimalAssert<?> assumeThat(BigDecimal actual) {
    return asAssumption(BigDecimalAssert.class, BigDecimal.class, actual);
  }

  /**
   * Creates a new instance of <code>{@link BigIntegerAssert}</code> assumption.
   *
   * @param actual the actual value.
   * @return the created assumption for assertion object.
   */
  @CheckReturnValue
  public static AbstractBigIntegerAssert<?> assumeThat(BigInteger actual) {
    return asAssumption(BigIntegerAssert.class, BigInteger.class, actual);
  }

  /**
   * Creates a new instance of <code>{@link UriAssert}</code> assumption.
   *
   * @param actual the actual value.
   * @return the created assumption for assertion object.
   */
  @CheckReturnValue
  public static AbstractUriAssert<?> assumeThat(URI actual) {
    return asAssumption(UriAssert.class, URI.class, actual);
  }

  /**
   * Creates a new instance of <code>{@link UrlAssert}</code> assumption.
   *
   * @param actual the actual value.
   * @return the created assumption for assertion object.
   */
  @CheckReturnValue
  public static AbstractUrlAssert<?> assumeThat(URL actual) {
    return asAssumption(UrlAssert.class, URL.class, actual);
  }

  /**
   * Creates a new instance of <code>{@link BooleanAssert}</code> assumption.
   *
   * @param actual the actual value.
   * @return the created assumption for assertion object.
   */
  @CheckReturnValue
  public static AbstractBooleanAssert<?> assumeThat(boolean actual) {
    return asAssumption(BooleanAssert.class, Boolean.class, actual);
  }

  /**
   * Creates a new instance of <code>{@link BooleanAssert}</code> assumption.
   *
   * @param actual the actual value.
   * @return the created assumption for assertion object.
   */
  @CheckReturnValue
  public static AbstractBooleanAssert<?> assumeThat(Boolean actual) {
    return asAssumption(BooleanAssert.class, Boolean.class, actual);
  }

  /**
   * Creates a new instance of <code>{@link BooleanArrayAssert}</code> assumption.
   *
   * @param actual the actual value.
   * @return the created assumption for assertion object.
   */
  @CheckReturnValue
  public static AbstractBooleanArrayAssert<?> assumeThat(boolean[] actual) {
    return asAssumption(BooleanArrayAssert.class, boolean[].class, actual);
  }

  /**
   * Creates a new instance of <code>{@link ByteAssert}</code> assumption.
   *
   * @param actual the actual value.
   * @return the created assumption for assertion object.
   */
  @CheckReturnValue
  public static AbstractByteAssert<?> assumeThat(byte actual) {
    return asAssumption(ByteAssert.class, Byte.class, actual);
  }

  /**
   * Creates a new instance of <code>{@link ByteAssert}</code> assumption.
   *
   * @param actual the actual value.
   * @return the created assumption for assertion object.
   */
  @CheckReturnValue
  public static AbstractByteAssert<?> assumeThat(Byte actual) {
    return asAssumption(ByteAssert.class, Byte.class, actual);
  }

  /**
   * Creates a new instance of <code>{@link ByteArrayAssert}</code> assumption.
   *
   * @param actual the actual value.
   * @return the created assumption for assertion object.
   */
  @CheckReturnValue
  public static AbstractByteArrayAssert<?> assumeThat(byte[] actual) {
    return asAssumption(ByteArrayAssert.class, byte[].class, actual);
  }

  /**
   * Creates a new instance of <code>{@link CharacterAssert}</code> assumption.
   *
   * @param actual the actual value.
   * @return the created assumption for assertion object.
   */
  @CheckReturnValue
  public static AbstractCharacterAssert<?> assumeThat(char actual) {
    return asAssumption(CharacterAssert.class, Character.class, actual);
  }

  /**
   * Creates a new instance of <code>{@link CharacterAssert}</code> assumption.
   *
   * @param actual the actual value.
   * @return the created assumption for assertion object.
   */
  @CheckReturnValue
  public static AbstractCharacterAssert<?> assumeThat(Character actual) {
    return asAssumption(CharacterAssert.class, Character.class, actual);
  }

  /**
   * Creates a new instance of <code>{@link CharArrayAssert}</code> assumption.
   *
   * @param actual the actual value.
   * @return the created assumption for assertion object.
   */
  @CheckReturnValue
  public static AbstractCharArrayAssert<?> assumeThat(char[] actual) {
    return asAssumption(CharArrayAssert.class, char[].class, actual);
  }

  /**
   * Creates a new instance of <code>{@link CharSequenceAssert}</code> assumption.
   *
   * @param actual the actual value.
   * @return the created assumption for assertion object.
   */
  @CheckReturnValue
  public static AbstractCharSequenceAssert<?, ? extends CharSequence> assumeThat(CharSequence actual) {
    return asAssumption(CharSequenceAssert.class, CharSequence.class, actual);
  }

  /**
   * Creates a new instance of <code>{@link ShortAssert}</code> assumption.
   *
   * @param actual the actual value.
   * @return the created assumption for assertion object.
   */
  @CheckReturnValue
  public static AbstractShortAssert<?> assumeThat(short actual) {
    return asAssumption(ShortAssert.class, Short.class, actual);
  }

  /**
   * Creates a new instance of <code>{@link ShortAssert}</code> assumption.
   *
   * @param actual the actual value.
   * @return the created assumption for assertion object.
   */
  @CheckReturnValue
  public static AbstractShortAssert<?> assumeThat(Short actual) {
    return asAssumption(ShortAssert.class, Short.class, actual);
  }

  /**
   * Creates a new instance of <code>{@link ShortArrayAssert}</code> assumption.
   *
   * @param actual the actual value.
   * @return the created assumption for assertion object.
   */
  @CheckReturnValue
  public static AbstractShortArrayAssert<?> assumeThat(short[] actual) {
    return asAssumption(ShortArrayAssert.class, short[].class, actual);
  }

  /**
   * Creates a new instance of <code>{@link IntegerAssert}</code> assumption.
   *
   * @param actual the actual value.
   * @return the created assumption for assertion object.
   */
  @CheckReturnValue
  public static AbstractIntegerAssert<?> assumeThat(int actual) {
    return asAssumption(IntegerAssert.class, Integer.class, actual);
  }

  /**
   * Creates a new instance of <code>{@link IntegerAssert}</code> assumption.
   *
   * @param actual the actual value.
   * @return the created assumption for assertion object.
   */
  @CheckReturnValue
  public static AbstractIntegerAssert<?> assumeThat(Integer actual) {
    return asAssumption(IntegerAssert.class, Integer.class, actual);
  }

  /**
   * Creates a new instance of <code>{@link IntArrayAssert}</code> assumption.
   *
   * @param actual the actual value.
   * @return the created assumption for assertion object.
   */
  @CheckReturnValue
  public static AbstractIntArrayAssert<?> assumeThat(int[] actual) {
    return asAssumption(IntArrayAssert.class, int[].class, actual);
  }

  /**
   * Creates a new instance of <code>{@link LongAssert}</code> assumption.
   *
   * @param actual the actual value.
   * @return the created assumption for assertion object.
   */
  @CheckReturnValue
  public static AbstractLongAssert<?> assumeThat(long actual) {
    return asAssumption(LongAssert.class, Long.class, actual);
  }

  /**
   * Creates a new instance of <code>{@link LongAssert}</code> assumption.
   *
   * @param actual the actual value.
   * @return the created assumption for assertion object.
   */
  @CheckReturnValue
  public static AbstractLongAssert<?> assumeThat(Long actual) {
    return asAssumption(LongAssert.class, Long.class, actual);
  }

  /**
   * Creates a new instance of <code>{@link LongArrayAssert}</code> assumption.
   *
   * @param actual the actual value.
   * @return the created assumption for assertion object.
   */
  @CheckReturnValue
  public static AbstractLongArrayAssert<?> assumeThat(long[] actual) {
    return asAssumption(LongArrayAssert.class, long[].class, actual);
  }

  /**
   * Creates a new instance of <code>{@link FloatAssert}</code> assumption.
   *
   * @param actual the actual value.
   * @return the created assumption for assertion object.
   */
  @CheckReturnValue
  public static AbstractFloatAssert<?> assumeThat(float actual) {
    return asAssumption(FloatAssert.class, Float.class, actual);
  }

  /**
   * Creates a new instance of <code>{@link FloatAssert}</code> assumption.
   *
   * @param actual the actual value.
   * @return the created assumption for assertion object.
   */
  @CheckReturnValue
  public static AbstractFloatAssert<?> assumeThat(Float actual) {
    return asAssumption(FloatAssert.class, Float.class, actual);
  }

  /**
   * Creates a new instance of <code>{@link FloatArrayAssert}</code> assumption.
   *
   * @param actual the actual value.
   * @return the created assumption for assertion object.
   */
  @CheckReturnValue
  public static AbstractFloatArrayAssert<?> assumeThat(float[] actual) {
    return asAssumption(FloatArrayAssert.class, float[].class, actual);
  }

  /**
   * Creates a new instance of <code>{@link DoubleAssert}</code> assumption.
   *
   * @param actual the actual value.
   * @return the created assumption for assertion object.
   */
  @CheckReturnValue
  public static AbstractDoubleAssert<?> assumeThat(double actual) {
    return asAssumption(DoubleAssert.class, Double.class, actual);
  }

  /**
   * Creates a new instance of <code>{@link DoubleAssert}</code> assumption.
   *
   * @param actual the actual value.
   * @return the created assumption for assertion object.
   */
  @CheckReturnValue
  public static AbstractDoubleAssert<?> assumeThat(Double actual) {
    return asAssumption(DoubleAssert.class, Double.class, actual);
  }

  /**
   * Creates a new instance of <code>{@link DoubleArrayAssert}</code> assumption.
   *
   * @param actual the actual value.
   * @return the created assumption for assertion object.
   */
  @CheckReturnValue
  public static AbstractDoubleArrayAssert<?> assumeThat(double[] actual) {
    return asAssumption(DoubleArrayAssert.class, double[].class, actual);
  }

  /**
   * Creates assumption for {@link AtomicBoolean}.
   *
   * @param actual the actual value.
   * @return the created assumption for assertion object.
   */
  @CheckReturnValue
  public static AtomicBooleanAssert assumeThat(AtomicBoolean actual) {
    return asAssumption(AtomicBooleanAssert.class, AtomicBoolean.class, actual);
  }

  /**
   * Creates assumption for {@link AtomicInteger}.
   *
   * @param actual the actual value.
   * @return the created assumption for assertion object.
   */
  @CheckReturnValue
  public static AtomicIntegerAssert assumeThat(AtomicInteger actual) {
    return asAssumption(AtomicIntegerAssert.class, AtomicInteger.class, actual);
  }

  /**
   * Creates int[] assumption for {@link AtomicIntegerArray}.
   *
   * @param actual the actual value.
   * @return the created assumption for assertion object.
   */
  @CheckReturnValue
  public static AtomicIntegerArrayAssert assumeThat(AtomicIntegerArray actual) {
    return asAssumption(AtomicIntegerArrayAssert.class, AtomicIntegerArray.class, actual);
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
    return asAssumption(AtomicIntegerFieldUpdaterAssert.class, AtomicIntegerFieldUpdater.class, actual);
  }

  /**
   * Creates assumption for {@link AtomicLong}.
   *
   * @param actual the actual value.
   * @return the created assumption for assertion object.
   */
  @CheckReturnValue
  public static AtomicLongAssert assumeThat(AtomicLong actual) {
    return asAssumption(AtomicLongAssert.class, AtomicLong.class, actual);
  }

  /**
   * Creates assumption for {@link AtomicLongArray}.
   *
   * @param actual the actual value.
   * @return the created assumption for assertion object.
   */
  @CheckReturnValue
  public static AtomicLongArrayAssert assumeThat(AtomicLongArray actual) {
    return asAssumption(AtomicLongArrayAssert.class, AtomicLongArray.class, actual);
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
    return asAssumption(AtomicLongFieldUpdaterAssert.class, AtomicLongFieldUpdater.class, actual);
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
    return asAssumption(AtomicReferenceAssert.class, AtomicReference.class, actual);
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
    return asAssumption(AtomicReferenceArrayAssert.class, AtomicReferenceArray.class, actual);
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
    return asAssumption(AtomicReferenceFieldUpdaterAssert.class, AtomicReferenceFieldUpdater.class, actual);
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
    return asAssumption(AtomicMarkableReferenceAssert.class, AtomicMarkableReference.class, actual);
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
    return asAssumption(AtomicStampedReferenceAssert.class, AtomicStampedReference.class, actual);
  }

  /**
   * Creates a new instance of <code>{@link ClassAssert}</code> assumption.
   *
   * @param actual the actual value.
   * @return the created assumption for assertion object.
   */
  @CheckReturnValue
  public static AbstractClassAssert<?> assumeThat(Class<?> actual) {
    return asAssumption(ClassAssert.class, Class.class, actual);
  }

  /**
   * Creates a new instance of <code>{@link DateAssert}</code> assumption.
   *
   * @param actual the actual value.
   * @return the created assumption for assertion object.
   */
  @CheckReturnValue
  public static AbstractDateAssert<?> assumeThat(Date actual) {
    return asAssumption(DateAssert.class, Date.class, actual);
  }

  /**
   * Creates a new instance of <code>{@link FileAssert}</code> assumption.
   *
   * @param actual the actual value.
   * @return the created assumption for assertion object.
   */
  @CheckReturnValue
  public static AbstractFileAssert<?> assumeThat(File actual) {
    return asAssumption(FileAssert.class, File.class, actual);
  }

  /**
   * Creates a new instance of {@link PathAssert} assumption.
   *
   * @param actual the path to test
   * @return the created assumption for assertion object.
   */
  @CheckReturnValue
  public static AbstractPathAssert<?> assumeThat(Path actual) {
    return asAssumption(PathAssert.class, Path.class, actual);
  }

  /**
   * Creates a new instance of <code>{@link InputStreamAssert}</code> assumption.
   *
   * @param actual the actual value.
   * @return the created assumption for assertion object.
   */
  @CheckReturnValue
  public static AbstractInputStreamAssert<?, ? extends InputStream> assumeThat(InputStream actual) {
    return asAssumption(InputStreamAssert.class, InputStream.class, actual);
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
    return asAssumption(FutureAssert.class, Future.class, actual);
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
    return asAssumption(IterableAssert.class, Iterable.class, actual);
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
    return asAssumption(IterableAssert.class, Iterator.class, actual);
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
    return asAssumption(ListAssert.class, List.class, actual);
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
    return asAssumption(ObjectArrayAssert.class, Object[].class, actual);
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
    return asAssumption(MapAssert.class, Map.class, actual);
  }

  /**
   * Creates a new instance of <code>{@link GenericComparableAssert}</code> assumption.
   *
   * @param actual the actual value.
   * @return the created assumption for assertion object.
   */
  @CheckReturnValue
  @SuppressWarnings("unchecked")
  public static <T extends Comparable<? super T>> AbstractComparableAssert<?, T> assumeThat(T actual) {
    return asAssumption(GenericComparableAssert.class, Comparable.class, actual);
  }

  /**
   * Creates a new instance of <code>{@link ThrowableAssert}</code> assumption.
   *
   * @param actual the actual value.
   * @return the created assumption for assertion object.
   */
  @CheckReturnValue
  public static AbstractThrowableAssert<?, ? extends Throwable> assumeThat(Throwable actual) {
    return asAssumption(ThrowableAssert.class, Throwable.class, actual);
  }

  /**
   * Allows to capture and then assume on a {@link Throwable} (done easier when used with Java 8 lambdas).
   *
   * @param shouldRaiseThrowable The {@link ThrowingCallable} or lambda with the code that should raise the throwable.
   * @return the created assumption for assertion object.
   */
  @CheckReturnValue
  public static AbstractThrowableAssert<?, ? extends Throwable> assumeThatThrownBy(ThrowingCallable shouldRaiseThrowable) {
    return asAssumption(ThrowableAssert.class, Throwable.class, catchThrowable(shouldRaiseThrowable));
  }

  private static <ASSERTION, ACTUAL> ASSERTION asAssumption(Class<ASSERTION> assertionType,
                                                            Class<ACTUAL> actualType,
                                                            Object actual) {
    return asAssumption(assertionType, array(actualType), array(actual));
  }

  @SuppressWarnings("unchecked")
  private static <ASSERTION, ACTUAL> ASSERTION asAssumption(Class<ASSERTION> assertionType,
                                                            Class<?>[] constructorTypes,
                                                            Object... constructorParams) {
    Enhancer enhancer = new Enhancer();
    enhancer.setSuperclass(assertionType);
    enhancer.setCallback(new AssumptiomMethodInterceptor());

    return (ASSERTION) enhancer.create(constructorTypes, constructorParams);
  }

  private static RuntimeException assumptionNotMet(AssertionError e) throws ReflectiveOperationException {
    try {
      return assumptionNotMet(Class.forName("org.junit.AssumptionViolatedException"), e);
    } catch (ClassNotFoundException junitClassNotFound) {
      try {
        return assumptionNotMet(Class.forName("org.testng.SkipException"), e);
      } catch (ClassNotFoundException testngClassNotFound) {
        throw new IllegalStateException("Assumptions require JUnit or TestNG in the classpath");
      }
    }
  }

  private static RuntimeException assumptionNotMet(Class<?> exceptionClass,
                                                   AssertionError e) throws ReflectiveOperationException {
    return (RuntimeException) exceptionClass.getConstructor(String.class, Throwable.class)
                                            .newInstance("assumption was not met due to: " + e.getMessage(), e);
  }

  private static Object asAssumption(AbstractAssert<?, ?> assertion) {
    Object actual = assertion.actual;
    if (assertion instanceof StringAssert) return asAssumption(StringAssert.class, String.class, actual);
    if (assertion instanceof ListAssert) return asAssumption(ListAssert.class, List.class, actual);
    if (assertion instanceof ObjectArrayAssert) return asAssumption(ObjectArrayAssert.class, Object[].class, actual);
    if (assertion instanceof IterableSizeAssert) {
      @SuppressWarnings("rawtypes")
      IterableSizeAssert iterableSizeAssert = (IterableSizeAssert) assertion;
      Class<?>[] constructorTypes = array(AbstractIterableAssert.class, Integer.class);
      return asAssumption(IterableSizeAssert.class, constructorTypes, iterableSizeAssert.returnToIterable(), actual);
    }
    if (assertion instanceof ObjectAssert)
      return asAssumption(ObjectAssert.class, Object.class, actual).as(assertion.descriptionText());

    throw new IllegalArgumentException("Unsupported assumption creation for " + assertion.getClass());
  }
}
