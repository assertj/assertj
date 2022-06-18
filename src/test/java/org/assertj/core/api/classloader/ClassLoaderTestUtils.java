package org.assertj.core.api.classloader;

import static java.util.Objects.requireNonNull;
import static org.assertj.core.api.Assumptions.assumeThat;
import static org.mockito.Answers.RETURNS_SMART_NULLS;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.withSettings;
import static org.mockito.quality.Strictness.STRICT_STUBS;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Vector;
import javax.management.loading.PrivateClassLoader;
import net.bytebuddy.dynamic.DynamicType;
import net.bytebuddy.dynamic.DynamicType.Unloaded;
import net.bytebuddy.dynamic.TypeResolutionStrategy;
import net.bytebuddy.dynamic.TypeResolutionStrategy.Active;
import net.bytebuddy.pool.TypePool;
import net.bytebuddy.pool.TypePool.Empty;
import org.mockito.stubbing.Answer;

/**
 * A group of helper methods for testing class loader assertions with.
 *
 * @author Ashley Scopes
 */
@SuppressWarnings("ConstantConditions")
final class ClassLoaderTestUtils {

  private ClassLoaderTestUtils() {
    throw new UnsupportedOperationException("static-only class");
  }

  /**
   * Just a plain old class loader that does nothing special.
   */
  static final class SimpleClassLoader extends ClassLoader {

    SimpleClassLoader() {
      super(null);
    }

    SimpleClassLoader(ClassLoader parent) {
      super(requireNonNull(parent));
    }
  }

  /**
   * A stub class loader for tests which is marked as private in the management API.
   *
   * @author Ashley Scopes
   */
  static final class PrivateJmxClassLoader extends ClassLoader implements PrivateClassLoader {

    PrivateJmxClassLoader() {
      if (!PrivateClassLoader.class.isAssignableFrom(getClass())) {
        throw new IllegalStateException("Expected this class loader to be private");
      }
    }
  }

  /**
   * A stub class loader for tests which is marked as public in the management API.
   *
   * @author Ashley Scopes
   */
  static final class NonPrivateJmxClassLoader extends ClassLoader {

    NonPrivateJmxClassLoader() {
      if (PrivateClassLoader.class.isAssignableFrom(getClass())) {
        throw new IllegalStateException("Expected this class loader to not be private");
      }
    }

  }

  /**
   * A simple class loader that holds a map of byte arrays that represent valid classes. These can
   * be loaded into memory as needed. These are registered via ByteBuddy {@link DynamicType.Builder}
   * instances or by {@code byte[]} arrays.
   *
   * @author Ashley Scopes
   */
  static final class ByteClassLoader extends ClassLoader {

    private static final TypeResolutionStrategy RESOLUTION_STRATEGY = new Active();
    private static final TypePool TYPE_POOL = Empty.INSTANCE;
    private final Map<String, byte[]> rawClasses;

    ByteClassLoader() {
      rawClasses = new LinkedHashMap<>();
    }

    ByteClassLoader with(DynamicType.Builder<?> type) {
      requireNonNull(type);
      Unloaded<?> unloaded = type.make(RESOLUTION_STRATEGY, TYPE_POOL);
      String name = unloaded.getTypeDescription().getName();
      return with(name, unloaded.getBytes());
    }

    ByteClassLoader with(String name, byte[] data) {
      requireNonNull(name);
      requireNonNull(data);

      if (rawClasses.containsKey(name)) {
        // Sanity check. We don't want to allow tests to change this as it could give confusing
        // behaviour if classes are already loaded.
        throw new IllegalStateException("class " + name + " already defined");
      }

      rawClasses.put(name, data);
      return this;
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
      byte[] classData = Optional
        .ofNullable(name)
        .map(rawClasses::get)
        .orElseThrow(() -> new ClassNotFoundException(name));
      // (String name, byte[] data, int offset, int length)
      return defineClass(null, classData, 0, classData.length);
    }
  }

  /**
   * Get the system class loader and perform some last-minute sanity checks.
   *
   * @return the system class loader.
   */
  static ClassLoader systemClassLoader() {
    ClassLoader classLoader = ClassLoader.getSystemClassLoader();

    // There is one edge case where this may be null, and that is during initialization. This is
    // just a sanity check.
    assumeThat(classLoader)
      .withFailMessage("expected a valid system class loader to already be initialized")
      .isNotNull();

    return classLoader;
  }

  /**
   * Create a mock class loader with strict rules that can be stubbed and spied.
   *
   * @return the class loader.
   */
  static ClassLoader mockClassLoader() {
    return mockType(ClassLoader.class);
  }

  /**
   * Create a mocked URL.
   *
   * @return the mocked URL.
   */
  static URL mockUrl() {
    return mockType(URL.class);
  }

  /**
   * Create a mocked URL that when opened as a stream, will return the given stream.
   *
   * @param inputStream the input stream to return.
   * @return the mocked URL.
   */
  static URL mockUrlWithInputStream(InputStream inputStream) throws IOException {
    URL url = mockUrl();
    when(url.openStream()).thenReturn(inputStream);
    return url;
  }

  /**
   * Create a mocked URL that when opened as a stream, will allow reading in the given byte array.
   *
   * @param content the content to provide.
   * @return the mocked URL.
   */
  static URL mockUrlWithContent(byte[] content) throws IOException {
    URL url = mockUrl();
    when(url.openStream()).then(ctx -> new ByteArrayInputStream(content));
    return url;
  }

  /**
   * Convert the given vararg items to an enumerable and return them.
   *
   * @param items the items to enumerate.
   * @param <T>   the element type.
   * @return the enumerable.
   */
  @SafeVarargs
  static <T> Answer<Enumeration<T>> withEnumerationOf(T... items) {
    Vector<T> vec = new Vector<>(Arrays.asList(items));
    return unused -> vec.elements();
  }

  private static <T> T mockType(Class<T> type) {
    return mock(
      type,
      withSettings()
        .defaultAnswer(RETURNS_SMART_NULLS)
        .strictness(STRICT_STUBS)
    );
  }
}
