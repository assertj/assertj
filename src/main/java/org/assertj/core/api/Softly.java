package org.assertj.core.api;

import java.io.File;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.junit.rules.ErrorCollector;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

/**
 * the same as {@link SoftAssertions}, but with the following differences:
 *
 * First, it's a junit rule, which can be used as follows (without the assertAll())
 *
 * <pre>
 *   public class SoftlyTest {
 *
 *     &#064;Rule
 *     public final Softly softly = new Softly();
 *
 *     &#064;Test
 *     public void testSoftly() throws Exception {
 *       softly.assertThat(1).isEqualTo(2);
 *       softly.assertThat(Lists.newArrayList(1, 2)).containsOnly(1, 2);
 *     }
 *  }
 * </pre>
 *
 * Second, the failures are recognized by IDE's (like IntelliJ IDEA) which open a comparison window.
 */
public class Softly extends ErrorCollector {

    /**
     * Collects error messages of all AssertionErrors thrown by the proxied method.
     */
    private class ErrorCollector implements MethodInterceptor {

        @Override
        public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {
            try {
                proxy.invokeSuper(obj, args);
            } catch (AssertionError e) {
                addError(e);
            }
            return obj;
        }
    }

    @SuppressWarnings("unchecked")
    protected <T, V> V proxy(Class<V> assertClass, Class<T> actualClass, T actual) {
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(assertClass);
        enhancer.setCallback(new ErrorCollector());
        return (V) enhancer.create(new Class[]{actualClass}, new Object[]{actual});
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

    //for guava assertions. Maybe have another Softly in guava?
//    @SuppressWarnings("unchecked")
//    public <K, V> MultimapAssert<K, V> assertThat(final Multimap<K, V> actual) {
//        return proxy(MultimapAssert.class, Multimap.class, actual);
//    }
//
//    @SuppressWarnings("unchecked")
//    public <T> OptionalAssert<T> assertThat(final Optional<T> actual) {
//        return proxy(OptionalAssert.class, Optional.class, actual);
//    }

}
