/*
 * Created on Sep 1, 2013
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
 * Copyright @2013 the original author or authors.
 */
package org.assertj.core.api;

import java.io.File;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.sf.cglib.proxy.Enhancer;

/**
 * <p>
 * Suppose we have a test case and in it we'd like to make numerous assertions. In this case, we're hosting a dinner
 * party and we want to ensure not only that all our guests survive but also that nothing in the mansion has been unduly
 * disturbed:
 * </p>
 * 
 * <pre>
 * &#064;Test
 * public void host_dinner_party_where_nobody_dies() {
 *   Mansion mansion = new Mansion();
 *   mansion.hostPotentiallyMurderousDinnerParty();
 *   assertThat(mansion.guests()).as(&quot;Living Guests&quot;).isEqualTo(7);
 *   assertThat(mansion.kitchen()).as(&quot;Kitchen&quot;).isEqualTo(&quot;clean&quot;);
 *   assertThat(mansion.library()).as(&quot;Library&quot;).isEqualTo(&quot;clean&quot;);
 *   assertThat(mansion.revolverAmmo()).as(&quot;Revolver Ammo&quot;).isEqualTo(6);
 *   assertThat(mansion.candlestick()).as(&quot;Candlestick&quot;).isEqualTo(&quot;pristine&quot;);
 *   assertThat(mansion.colonel()).as(&quot;Colonel&quot;).isEqualTo(&quot;well kempt&quot;);
 *   assertThat(mansion.professor()).as(&quot;Professor&quot;).isEqualTo(&quot;well kempt&quot;);
 * }
 * </pre>
 * 
 * <p>
 * After running the test, JUnit provides us with the following exception message:
 * </p>
 * 
 * <pre>
 * org.junit.ComparisonFailure: [Living Guests] expected:&lt;[7]&gt; but was:&lt;[6]&gt;
 * </pre>
 * 
 * <p>
 * Oh no! A guest has been murdered! But where, how, and by whom?
 * </p>
 * 
 * <p>
 * Unfortunately frameworks like JUnit halt the test upon the first failed assertion. Therefore, to collect more
 * evidence, we'll have to rerun the test (perhaps after attaching a debugger or modifying the test to skip past the
 * first assertion). Given that hosting dinner parties takes a long time, this seems rather inefficient.
 * </p>
 * 
 * <p>
 * Instead let's change the test so that at its completion we get the result of all assertions at once. We can do that
 * by using a SoftAssertions instance instead of the static methods on {@link Assertions} as follows:
 * </p>
 * 
 * <pre>
 * &#064;Test
 * public void host_dinner_party_where_nobody_dies() {
 *   Mansion mansion = new Mansion();
 *   mansion.hostPotentiallyMurderousDinnerParty();
 *   SoftAssertions softly = new SoftAssertions();
 *   softly.assertThat(mansion.guests()).as(&quot;Living Guests&quot;).isEqualTo(7);
 *   softly.assertThat(mansion.kitchen()).as(&quot;Kitchen&quot;).isEqualTo(&quot;clean&quot;);
 *   softly.assertThat(mansion.library()).as(&quot;Library&quot;).isEqualTo(&quot;clean&quot;);
 *   softly.assertThat(mansion.revolverAmmo()).as(&quot;Revolver Ammo&quot;).isEqualTo(6);
 *   softly.assertThat(mansion.candlestick()).as(&quot;Candlestick&quot;).isEqualTo(&quot;pristine&quot;);
 *   softly.assertThat(mansion.colonel()).as(&quot;Colonel&quot;).isEqualTo(&quot;well kempt&quot;);
 *   softly.assertThat(mansion.professor()).as(&quot;Professor&quot;).isEqualTo(&quot;well kempt&quot;);
 *   softly.assertAll();
 * }
 * </pre>
 * 
 * <p>
 * Now upon running the test our JUnit exception message is far more detailed:
 * </p>
 * 
 * <pre>
 * org.assertj.core.api.SoftAssertionError: The following 4 assertions failed:
 * 1) [Living Guests] expected:&lt;[7]&gt; but was:&lt;[6]&gt;
 * 2) [Library] expected:&lt;'[clean]'&gt; but was:&lt;'[messy]'&gt;
 * 3) [Candlestick] expected:&lt;'[pristine]'&gt; but was:&lt;'[bent]'&gt;
 * 4) [Professor] expected:&lt;'[well kempt]'&gt; but was:&lt;'[bloodied and disheveled]'&gt;
 * </pre>
 * 
 * <p>
 * Aha! It appears that perhaps the Professor used the candlestick to perform the nefarious deed in the library. We
 * should let the police take it from here.
 * </p>
 * 
 * <p>
 * SoftAssertions works by providing you with proxyies of the AssertJ assertion objects (those created by
 * {@link Assertions}#assertThat...) whose assertion failures are caught and stored. Only when you call
 * {@link SoftAssertions#assertAll()} will a {@link SoftAssertionError} be thrown containing the error messages of those
 * previously caught assertion failures.
 * </p>
 * 
 * <p>
 * Note that because SoftAssertions is stateful you should use a new instance of SoftAssertions per test method. Also,
 * if you forget to call assertAll() at the end of your test, the test <strong>will pass</strong> even if any assertion
 * objects threw exceptions (because they're proxied, remember?). So don't forget.
 * </p>
 * 
 * <p>
 * It is recommended to use {@link AbstractAssert#as(String, Object...)} so that the multiple failed assertions can be
 * easily distinguished from one another.
 * </p>
 * 
 * @author Brian Laframboise
 * 
 * @see http://beust.com/weblog/2012/07/29/reinventing-assertions/ for the inspiration
 */
public class SoftAssertions {

  protected final ErrorCollector collector = new ErrorCollector();

  /** Creates a new </code>{@link SoftAssertions}</code>. */
  public SoftAssertions() {
  }

  /**
   * Verifies that no proxied assertion methods have failed.
   * 
   * @throws SoftAssertionError if any proxied assertion objects threw
   */
  public void assertAll() {
    List<String> errors = collector.errors();
    if (!errors.isEmpty()) {
      throw new SoftAssertionError(errors);
    }
  }

  @SuppressWarnings("unchecked")
  protected <T, V> V proxy(Class<V> assertClass, Class<T> actualClass, T actual) {
    Enhancer enhancer = new Enhancer();
    enhancer.setSuperclass(assertClass);
    enhancer.setCallback(collector);
    return (V) enhancer.create(new Class[] { actualClass }, new Object[] { actual });
  }

  // assertThat* methods duplicated from Assertions

  /**
   * Creates a new instance of <code>{@link BigDecimalAssert}</code>.
   * 
   * @param actual the actual value.
   * @return the created assertion object.
   */
  public BigDecimalAssert assertThat(BigDecimal actual) {
    return proxy(BigDecimalAssert.class, BigDecimal.class, actual);
  }

  /**
   * Creates a new instance of <code>{@link BooleanAssert}</code>.
   * 
   * @param actual the actual value.
   * @return the created assertion object.
   */
  public BooleanAssert assertThat(boolean actual) {
    return proxy(BooleanAssert.class, Boolean.class, actual);
  }

  /**
   * Creates a new instance of <code>{@link BooleanAssert}</code>.
   * 
   * @param actual the actual value.
   * @return the created assertion object.
   */
  public BooleanAssert assertThat(Boolean actual) {
    return proxy(BooleanAssert.class, Boolean.class, actual);
  }

  /**
   * Creates a new instance of <code>{@link BooleanArrayAssert}</code>.
   * 
   * @param actual the actual value.
   * @return the created assertion object.
   */
  public BooleanArrayAssert assertThat(boolean[] actual) {
    return proxy(BooleanArrayAssert.class, boolean[].class, actual);
  }

  /**
   * Creates a new instance of <code>{@link ByteAssert}</code>.
   * 
   * @param actual the actual value.
   * @return the created assertion object.
   */
  public ByteAssert assertThat(byte actual) {
    return proxy(ByteAssert.class, Byte.class, actual);
  }

  /**
   * Creates a new instance of <code>{@link ByteAssert}</code>.
   * 
   * @param actual the actual value.
   * @return the created assertion object.
   */
  public ByteAssert assertThat(Byte actual) {
    return proxy(ByteAssert.class, Byte.class, actual);
  }

  /**
   * Creates a new instance of <code>{@link ByteArrayAssert}</code>.
   * 
   * @param actual the actual value.
   * @return the created assertion object.
   */
  public ByteArrayAssert assertThat(byte[] actual) {
    return proxy(ByteArrayAssert.class, byte[].class, actual);
  }

  /**
   * Creates a new instance of <code>{@link CharacterAssert}</code>.
   * 
   * @param actual the actual value.
   * @return the created assertion object.
   */
  public CharacterAssert assertThat(char actual) {
    return proxy(CharacterAssert.class, Character.class, actual);
  }

  /**
   * Creates a new instance of <code>{@link CharArrayAssert}</code>.
   * 
   * @param actual the actual value.
   * @return the created assertion object.
   */
  public CharArrayAssert assertThat(char[] actual) {
    return proxy(CharArrayAssert.class, char[].class, actual);
  }

  /**
   * Creates a new instance of <code>{@link CharacterAssert}</code>.
   * 
   * @param actual the actual value.
   * @return the created assertion object.
   */
  public CharacterAssert assertThat(Character actual) {
    return proxy(CharacterAssert.class, Character.class, actual);
  }

  /**
   * Creates a new instance of <code>{@link ClassAssert}</code>
   * 
   * @param actual the actual value.
   * @return the created assertion object.
   */
  public ClassAssert assertThat(Class<?> actual) {
    return proxy(ClassAssert.class, Class.class, actual);
  }

  /**
   * Creates a new instance of <code>{@link IterableAssert}</code>.
   * 
   * @param actual the actual value.
   * @return the created assertion object.
   */
  @SuppressWarnings("unchecked")
  public <T> IterableAssert<T> assertThat(Iterable<T> actual) {
    return proxy(IterableAssert.class, Iterable.class, actual);
  }

  /**
   * Creates a new instance of <code>{@link IterableAssert}</code>. The <code>{@link Iterator}</code> is first converted
   * into an <code>{@link Iterable}</code>
   * 
   * @param actual the actual value.
   * @return the created assertion object.
   */
  @SuppressWarnings("unchecked")
  public <T> IterableAssert<T> assertThat(Iterator<T> actual) {
    return proxy(IterableAssert.class, Iterator.class, actual);
  }

  /**
   * Creates a new instance of <code>{@link DoubleAssert}</code>.
   * 
   * @param actual the actual value.
   * @return the created assertion object.
   */
  public DoubleAssert assertThat(double actual) {
    return proxy(DoubleAssert.class, Double.class, actual);
  }

  /**
   * Creates a new instance of <code>{@link DoubleAssert}</code>.
   * 
   * @param actual the actual value.
   * @return the created assertion object.
   */
  public DoubleAssert assertThat(Double actual) {
    return proxy(DoubleAssert.class, Double.class, actual);
  }

  /**
   * Creates a new instance of <code>{@link DoubleArrayAssert}</code>.
   * 
   * @param actual the actual value.
   * @return the created assertion object.
   */
  public DoubleArrayAssert assertThat(double[] actual) {
    return proxy(DoubleArrayAssert.class, double[].class, actual);
  }

  /**
   * Creates a new instance of <code>{@link FileAssert}</code>.
   * 
   * @param actual the actual value.
   * @return the created assertion object.
   */
  public FileAssert assertThat(File actual) {
    return proxy(FileAssert.class, File.class, actual);
  }

  /**
   * Creates a new instance of <code>{@link InputStreamAssert}</code>.
   * 
   * @param actual the actual value.
   * @return the created assertion object.
   */
  public InputStreamAssert assertThat(InputStream actual) {
    return proxy(InputStreamAssert.class, InputStream.class, actual);
  }

  /**
   * Creates a new instance of <code>{@link FloatAssert}</code>.
   * 
   * @param actual the actual value.
   * @return the created assertion object.
   */
  public FloatAssert assertThat(float actual) {
    return proxy(FloatAssert.class, Float.class, actual);
  }

  /**
   * Creates a new instance of <code>{@link FloatAssert}</code>.
   * 
   * @param actual the actual value.
   * @return the created assertion object.
   */
  public FloatAssert assertThat(Float actual) {
    return proxy(FloatAssert.class, Float.class, actual);
  }

  /**
   * Creates a new instance of <code>{@link FloatArrayAssert}</code>.
   * 
   * @param actual the actual value.
   * @return the created assertion object.
   */
  public FloatArrayAssert assertThat(float[] actual) {
    return proxy(FloatArrayAssert.class, float[].class, actual);
  }

  /**
   * Creates a new instance of <code>{@link IntegerAssert}</code>.
   * 
   * @param actual the actual value.
   * @return the created assertion object.
   */
  public IntegerAssert assertThat(int actual) {
    return proxy(IntegerAssert.class, Integer.class, actual);
  }

  /**
   * Creates a new instance of <code>{@link IntArrayAssert}</code>.
   * 
   * @param actual the actual value.
   * @return the created assertion object.
   */
  public IntArrayAssert assertThat(int[] actual) {
    return proxy(IntArrayAssert.class, int[].class, actual);
  }

  /**
   * Creates a new instance of <code>{@link IntegerAssert}</code>.
   * 
   * @param actual the actual value.
   * @return the created assertion object.
   */
  public IntegerAssert assertThat(Integer actual) {
    return proxy(IntegerAssert.class, Integer.class, actual);
  }

  /**
   * Creates a new instance of <code>{@link ListAssert}</code>.
   * 
   * @param actual the actual value.
   * @return the created assertion object.
   */
  @SuppressWarnings("unchecked")
  public <T> ListAssert<T> assertThat(List<T> actual) {
    return proxy(ListAssert.class, List.class, actual);
  }

  /**
   * Creates a new instance of <code>{@link LongAssert}</code>.
   * 
   * @param actual the actual value.
   * @return the created assertion object.
   */
  public LongAssert assertThat(long actual) {
    return proxy(LongAssert.class, Long.class, actual);
  }

  /**
   * Creates a new instance of <code>{@link LongAssert}</code>.
   * 
   * @param actual the actual value.
   * @return the created assertion object.
   */
  public LongAssert assertThat(Long actual) {
    return proxy(LongAssert.class, Long.class, actual);
  }

  /**
   * Creates a new instance of <code>{@link LongArrayAssert}</code>.
   * 
   * @param actual the actual value.
   * @return the created assertion object.
   */
  public LongArrayAssert assertThat(long[] actual) {
    return proxy(LongArrayAssert.class, long[].class, actual);
  }

  /**
   * Creates a new instance of <code>{@link ObjectAssert}</code>.
   * 
   * @param actual the actual value.
   * @return the created assertion object.
   */
  @SuppressWarnings("unchecked")
  public <T> ObjectAssert<T> assertThat(T actual) {
    return proxy(ObjectAssert.class, Object.class, actual);
  }

  /**
   * Creates a new instance of <code>{@link ObjectArrayAssert}</code>.
   * 
   * @param actual the actual value.
   * @return the created assertion object.
   */
  @SuppressWarnings("unchecked")
  public <T> ObjectArrayAssert<T> assertThat(T[] actual) {
    return proxy(ObjectArrayAssert.class, Object[].class, actual);
  }

  /**
   * Creates a new instance of <code>{@link MapAssert}</code>.
   * 
   * @param actual the actual value.
   * @return the created assertion object.
   */
  @SuppressWarnings("unchecked")
  public <K, V> MapAssert<K, V> assertThat(Map<K, V> actual) {
    return proxy(MapAssert.class, Map.class, actual);
  }

  /**
   * Creates a new instance of <code>{@link ShortAssert}</code>.
   * 
   * @param actual the actual value.
   * @return the created assertion object.
   */
  public ShortAssert assertThat(short actual) {
    return proxy(ShortAssert.class, Short.class, actual);
  }

  /**
   * Creates a new instance of <code>{@link ShortAssert}</code>.
   * 
   * @param actual the actual value.
   * @return the created assertion object.
   */
  public ShortAssert assertThat(Short actual) {
    return proxy(ShortAssert.class, Short.class, actual);
  }

  /**
   * Creates a new instance of <code>{@link ShortArrayAssert}</code>.
   * 
   * @param actual the actual value.
   * @return the created assertion object.
   */
  public ShortArrayAssert assertThat(short[] actual) {
    return proxy(ShortArrayAssert.class, short[].class, actual);
  }

  /**
   * Creates a new instance of <code>{@link CharSequenceAssert}</code>.
   * 
   * @param actual the actual value.
   * @return the created assertion object.
   */
  public CharSequenceAssert assertThat(CharSequence actual) {
    return proxy(CharSequenceAssert.class, CharSequence.class, actual);
  }

  /**
   * Creates a new instance of <code>{@link StringAssert}</code>.
   * 
   * @param actual the actual value.
   * @return the created assertion object.
   */
  public StringAssert assertThat(String actual) {
    return proxy(StringAssert.class, String.class, actual);
  }

  /**
   * Creates a new instance of <code>{@link DateAssert}</code>.
   * 
   * @param actual the actual value.
   * @return the created assertion object.
   */
  public DateAssert assertThat(Date actual) {
    return proxy(DateAssert.class, Date.class, actual);
  }

  /**
   * Creates a new instance of <code>{@link ThrowableAssert}</code>.
   * 
   * @param actual the actual value.
   * @return the created assertion Throwable.
   */
  public ThrowableAssert assertThat(Throwable actual) {
    return proxy(ThrowableAssert.class, Throwable.class, actual);
  }

}
