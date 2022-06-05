/*
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 *
 * Copyright 2012-2022 the original author or authors.
 */
package org.assertj.core.api;

import static java.util.Objects.requireNonNull;
import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toList;
import static org.assertj.core.error.classloader.ShouldBePrivate.shouldBePrivate;
import static org.assertj.core.error.classloader.ShouldBeSystemClassLoader.shouldBeSystemClassLoader;
import static org.assertj.core.error.classloader.ShouldHaveParent.shouldHaveParent;
import static org.assertj.core.error.classloader.ShouldHaveResource.shouldHaveResource;
import static org.assertj.core.error.classloader.ShouldLoadClassSuccessfully.shouldLoadClassSuccessfully;
import static org.assertj.core.error.classloader.ShouldNotBePrivate.shouldNotBePrivate;
import static org.assertj.core.error.classloader.ShouldNotBeSystemClassLoader.shouldNotBeSystemClassLoader;
import static org.assertj.core.error.classloader.ShouldNotHaveParent.shouldNotHaveParent;
import static org.assertj.core.error.classloader.ShouldNotHaveResource.shouldNotHaveResource;
import static org.assertj.core.error.classloader.ShouldNotLoadClassSuccessfully.shouldNotLoadClassSuccessfully;
import static org.assertj.core.util.Strings.quote;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UncheckedIOException;
import java.net.URL;
import java.util.stream.Stream;
import javax.management.loading.PrivateClassLoader;
import org.assertj.core.internal.Failures;
import org.assertj.core.internal.Objects;
import org.assertj.core.util.CheckReturnValue;
import org.assertj.core.util.Streams;

/**
 * Base class for all implementations of assertions for {@link ClassLoader class loaders}.
 *
 * @param <SELF> the "self" type of this assertion class.
 * @author Ashley Scopes
 * @see <a
 * href="http://web.archive.org/web/20130721224442/http:/passion.forco.de/content/emulating-self-types-using-java-generics-simplify-fluent-api-implementation">
 * Emulating 'self types' using Java Generics to simplify fluent API implementation</a>
 */
public abstract class AbstractClassLoaderAssert<SELF extends AbstractClassLoaderAssert<SELF>>
    extends AbstractAssert<SELF, ClassLoader> {

  private static final Objects objects = Objects.instance();
  private static final Failures failures = Failures.instance();

  /**
   * Initialize this class loader assert.
   *
   * @param classLoader the class loader to assert on.
   * @param selfType    the type of the assertion implementation.
   */
  protected AbstractClassLoaderAssert(ClassLoader classLoader, Class<?> selfType) {
    super(classLoader, selfType);
  }

  /**
   * Assert that the class loader is "private" to the
   * {@link java.lang.management.ClassLoadingMXBean class-loading management bean}, by ensuring that
   * it implements {@link PrivateClassLoader}.
   *
   * @return {@code this} assertion object.
   * @throws AssertionError if {@code actual} is null.
   * @throws AssertionError if the {@code actual} class loader does not implement the
   *                        {@link PrivateClassLoader required marker interface}.
   */
  public SELF isPrivate() {
    objects.assertNotNull(info, actual);

    if (!classLoaderIsPrivate()) {
      throw failures.failure(info, shouldBePrivate(actual));
    }

    return myself;
  }

  /**
   * Assert that the class loader is not "private" to the
   * {@link java.lang.management.ClassLoadingMXBean class-loading management bean}, by ensuring that
   * it implements {@link PrivateClassLoader}.
   *
   * @return {@code this} assertion object.
   * @throws AssertionError if {@code actual} is null.
   * @throws AssertionError if the {@code actual} class loader implements the
   *                        {@link PrivateClassLoader required marker interface}.
   */
  public SELF isNotPrivate() {
    objects.assertNotNull(info, actual);

    if (classLoaderIsPrivate()) {
      throw failures.failure(info, shouldNotBePrivate(actual));
    }

    return myself;
  }

  /**
   * Assert that the class loader is the same object as the
   * {@link ClassLoader#getSystemClassLoader() system class loader}.
   *
   * @return {@code this} assertion object.
   * @throws AssertionError if {@code actual} is null.
   * @throws AssertionError if the {@code actual} class loader is not the same object as the system
   *                        class loader.
   */
  public SELF isSystemClassLoader() {
    objects.assertNotNull(info, actual);

    if (!classLoaderIsSystemClassLoader()) {
      throw failures.failure(
        info,
        shouldBeSystemClassLoader(actual),
        actual,
        ClassLoader.getSystemClassLoader()
      );
    }

    return myself;
  }

  /**
   * Assert that the class loader is not the same object as the
   * {@link ClassLoader#getSystemClassLoader() system class loader}.
   *
   * @return {@code this} assertion object.
   * @throws AssertionError if {@code actual} is null.
   * @throws AssertionError if the {@code actual} class loader is the same object as the system
   *                        class loader.
   */
  public SELF isNotSystemClassLoader() {
    objects.assertNotNull(info, actual);

    if (classLoaderIsSystemClassLoader()) {
      throw failures.failure(
        info,
        shouldNotBeSystemClassLoader(actual),
        actual,
        ClassLoader.getSystemClassLoader()
      );
    }

    return myself;
  }

  /**
   * Assert that the class loader has a non-{@code null} {@link ClassLoader#getParent() parent}.
   *
   * <p>The parent class loader may be {@code null} if no parent class loader exists. The parent
   * may also be null for some JVM implementations that return {@code null} for the bootstrap class
   * loader (also known as the <em>platform</em> class loader in some places).
   *
   * <p>Example:
   *
   * <pre><code class='java'>// Success scenario - a parent is present.
   * ClassLoader parent = new ClassLoader() {};
   * ClassLoader child = new ClassLoader() {};
   * assertThat(child).hasParent();
   *
   * // Failure scenario - a parent is not present.
   * ClassLoader child = new ClassLoader("child", null) {};
   * assertThat(child).hasParent();</code></pre>
   *
   * @return {@code this} assertions object.
   * @throws AssertionError if {@code actual} is {@code null}.
   * @throws AssertionError if the actual {@code ClassLoader} has no parent.
   */
  public SELF hasParent() {
    objects.assertNotNull(info, actual);

    if (actual.getParent() == null) {
      throw failures.failure(info, shouldHaveParent(actual));
    }

    return myself;
  }

  /**
   * Assert that the class loader has a null {@link ClassLoader#getParent() parent}.
   *
   * <p>The parent class loader may be {@code null} if no parent class loader exists. The parent
   * may also be null for some JVM implementations that return {@code null} for the bootstrap class
   * loader (also known as the <em>platform</em> class loader in some places).
   *
   * <p>Example:
   *
   * <pre><code class='java'>// Success scenario - a parent is present.
   * ClassLoader child = new ClassLoader(null) {};
   * assertThat(child).hasNoParent();
   *
   * // Failure scenario - a parent is present.
   * ClassLoader parent = new ClassLoader() {};
   * ClassLoader child = new ClassLoader() {};
   * assertThat(child).hasNoParent();</code></pre>
   *
   * @return {@code this} assertions object.
   * @throws AssertionError if {@code actual} is {@code null}.
   * @throws AssertionError if the actual {@code ClassLoader} has a non-{@code null} parent.
   */
  public SELF hasNoParent() {
    objects.assertNotNull(info, actual);

    ClassLoader parent = actual.getParent();
    if (parent != null) {
      throw failures.failure(info, shouldNotHaveParent(actual, parent));
    }

    return myself;
  }

  /**
   * Returns an assertion object to perform on the {@link ClassLoader#getParent() parent} class
   * loader.
   *
   * <p>The parent class loader may be {@code null} if no parent class loader exists. The parent
   * may also be null for some JVM implementations that return {@code null} for the bootstrap class
   * loader (also known as the <em>platform</em> class loader in some places).
   *
   * <pre><code class='java'>assertThat(classLoader).parent().isNotNull();</code></pre>
   *
   * @return the assertions to perform on the parent class loader.
   * @throws AssertionError if the actual {@code ClassLoader} is {@code null}.
   */
  @CheckReturnValue
  public AbstractClassLoaderAssert<?> parent() {
    objects.assertNotNull(info, actual);
    return toAssert(actual.getParent());
  }

  /**
   * Assert that at least one resource with the given path exists in the class loader.
   *
   * <p><strong>Note: </strong>this calls {@link ClassLoader#getResources(String)} and checks the
   * first result. This means that an exception will be thrown if a resource cannot be read from the
   * classloader due to an {@link IOException}. This contrasts with the behaviour of
   * {@link ClassLoader#getResource(String)}, which cannot throw an {@link IOException} and must
   * instead return {@code null}.
   *
   * @param path the path of the resource to look up.
   * @return {@code this} assertions object.
   * @throws NullPointerException if the {@code path} is null.
   * @throws AssertionError       if {@code actual} is null.
   * @throws AssertionError       if no resource is found matching the given {@code path}.
   * @throws UncheckedIOException if the call to {@link ClassLoader#getResources(String)} throws an
   *                              {@link IOException}.
   */
  public SELF hasResource(String path) {
    findResourcesForPath(path)
      .findFirst()
      .orElseThrow(() -> failures.failure(info, shouldHaveResource(actual, path)));

    return myself;
  }

  /**
   * Assert that no resource with the given path exists in the class loader.
   *
   * <p><strong>Note: </strong>this calls {@link ClassLoader#getResources(String)} and checks the
   * first result. This means that an exception will be thrown if a resource cannot be read from the
   * classloader due to an {@link IOException}. This contrasts with the behaviour of
   * {@link ClassLoader#getResource(String)}, which cannot throw an {@link IOException} and must
   * instead return {@code null}. This call may change the state of the classloader depending on the
   * implementation.
   *
   * @param path the path of the resource to look up.
   * @return {@code this} assertions object.
   * @throws NullPointerException if the {@code path} is null.
   * @throws AssertionError       if {@code actual} is null.
   * @throws AssertionError       if a resource is found matching the given {@code path}.
   * @throws UncheckedIOException if the call to {@link ClassLoader#getResources(String)} throws an
   *                              {@link IOException}.
   */
  public SELF hasNoResource(String path) {
    findResourcesForPath(path)
      .findFirst()
      .ifPresent(url -> {
        throw failures.failure(info, shouldNotHaveResource(actual, path, url));
      });

    return myself;
  }

  /**
   * Perform assertions on a resource URL that exists in the class loader, if present.
   *
   * <p>If the resource is not present, then a {@code null} resource will be used for the returned
   * assertion object.
   *
   * <p><strong>Note: </strong>this calls {@link ClassLoader#getResources(String)} and checks the
   * first result. This means that an exception will be thrown if a resource cannot be read from the
   * classloader due to an {@link IOException}. This contrasts with the behaviour of
   * {@link ClassLoader#getResource(String)}, which cannot throw an {@link IOException} and must
   * instead return {@code null}. This call may change the state of the classloader depending on the
   * implementation.
   *
   * @param path the path of the resource to look up.
   * @return the assertion to perform on the resource. If not found, the resource may be null.
   * @throws NullPointerException if the {@code path} is null.
   * @throws AssertionError       if {@code actual} is null.
   * @throws UncheckedIOException if the call to {@link ClassLoader#getResources(String)} throws an
   *                              {@link IOException}.
   */
  @CheckReturnValue
  public AbstractUrlAssert<?> resourceUrl(String path) {
    return toAssert(
      findResourcesForPath(path)
        .findFirst()
        .orElse(null)
    );
  }

  /**
   * Perform assertions on the byte content of a resource in the given class loader, if present.
   *
   * <p>If the resource is not present, then a {@code null} resource will be used for the returned
   * assertion object.
   *
   * <p><strong>Note: </strong>this calls {@link ClassLoader#getResources(String)} and checks the
   * first result. This means that an exception will be thrown if a resource cannot be read from the
   * classloader due to an {@link IOException}. This contrasts with the behaviour of
   * {@link ClassLoader#getResource(String)}, which cannot throw an {@link IOException} and must
   * instead return {@code null}. This call may change the state of the classloader depending on the
   * implementation.
   *
   * @param path the path of the resource to look up.
   * @return the assertion to perform on the resource content. If not found, the resource may be
   * null.
   * @throws NullPointerException if the {@code path} is null.
   * @throws AssertionError       if {@code actual} is null.
   * @throws UncheckedIOException if the call to {@link ClassLoader#getResources(String)} throws an
   *                              {@link IOException}.
   */
  public AbstractByteArrayAssert<?> resourceContent(String path) {
    return toAssert(
      findResourcesForPath(path)
        .map(this::readResourceFromUrl)
        .findFirst()
        .orElse(null)
    );
  }

  /**
   * Discover all resources with the given path using the class loader, and perform assertions on
   * the iterable of URL results.
   *
   * <p><strong>Note:</strong> This will call {@link ClassLoader#getResources(String)} internally,
   * which may change the class loader state depending on the implementation.
   *
   * @param path the path of the resource to look up.
   * @return the assertion to perform on the resources.
   * @throws NullPointerException if the {@code path} is null.
   * @throws AssertionError       if {@code actual} is null.
   * @throws UncheckedIOException if the call to {@link ClassLoader#getResources(String)} throws an
   *                              {@link IOException}.
   */
  @CheckReturnValue
  public AbstractIterableAssert<?, ? extends Iterable<URL>, URL, ? extends AbstractUrlAssert<?>> resourceUrls(
    String path
  ) {
    return findResourcesForPath(path)
      // No point using parallel for this, as we do not load anything else once we get this far.
      .sequential()
      .collect(collectingAndThen(toList(), this::toUrlIterableAssert));
  }

  /**
   * Discover all resources with the given path using the class loader, and perform assertions on
   * the iterable of byte array contents of those resources.
   *
   * <p><strong>Note:</strong> This will call {@link ClassLoader#getResources(String)} internally,
   * which may change the class loader state depending on the implementation.
   *
   * @param path the path of the resource to look up.
   * @return the assertion to perform on the iterable of resource contents.
   * @throws NullPointerException if the {@code path} is null.
   * @throws AssertionError       if {@code actual} is null.
   * @throws UncheckedIOException if the call to {@link ClassLoader#getResources(String)} throws an
   *                              {@link IOException}.
   */
  @CheckReturnValue
  public AbstractIterableAssert<?, ? extends Iterable<byte[]>, byte[], ? extends AbstractByteArrayAssert<?>> resourceContents(
    String path
  ) {
    return findResourcesForPath(path)
      // Parallel will give a slight performance benefit for slow filesystem-based assertions.
      .parallel()
      .map(this::readResourceFromUrl)
      .collect(collectingAndThen(toList(), this::toByteArrayIterableAssert));
  }

  /**
   * Assert that the class loader can load a class for the given binary name successfully.
   *
   * <p><strong>Note:</strong> This will call {@link ClassLoader#getResources(String)} internally,
   * which may change the class loader state depending on the implementation.
   *
   * @param binaryName the binary name of the class to attempt to load.
   * @return assertions to perform on the loaded class that is returned by the class loader.
   * @throws NullPointerException if {@code binaryName} is null.
   * @throws AssertionError       if {@code actual} is null.
   * @throws AssertionError       if a {@link ClassNotFoundException} is thrown while loading the
   *                              class with the given {@code binaryName}.
   */
  public AbstractClassAssert<?> canLoadClass(String binaryName) {
    try {
      return toAssert(tryLoadClass(binaryName));
    } catch (ClassNotFoundException ex) {
      AssertionError error = failures.failure(
        info,
        shouldLoadClassSuccessfully(actual, binaryName, ex)
      );
      error.initCause(ex);
      throw error;
    }
  }

  /**
   * Assert that the class loader can load a class for the given binary name successfully.
   *
   * <p><strong>Note:</strong> This will call {@link ClassLoader#getResources(String)} internally,
   * which may change the class loader state depending on the implementation.
   *
   * @param binaryName the binary name of the class to attempt to load.
   * @return assertions to perform on the loaded class that is returned.
   * @throws NullPointerException if {@code binaryName} is null.
   * @throws AssertionError       if {@code actual} is null.
   * @throws AssertionError       if no {@link ClassNotFoundException} is thrown while loading the
   *                              class with the given {@code binaryName}.
   */
  public AbstractThrowableAssert<?, ? extends ClassNotFoundException> canNotLoadClass(
    String binaryName
  ) {
    try {
      tryLoadClass(binaryName);
      throw failures.failure(info, shouldNotLoadClassSuccessfully(actual, binaryName));
    } catch (ClassNotFoundException ex) {
      return toAssert(ex);
    }
  }

  /**
   * Create a new assertion object for the given class loader.
   *
   * @param classLoader the class loader.
   * @return the assertion.
   */
  @CheckReturnValue
  protected abstract AbstractClassLoaderAssert<?> toAssert(ClassLoader classLoader);

  /**
   * Create a new assertion object for the given class.
   *
   * @param aClass the class.
   * @return the assertion.
   */
  @CheckReturnValue
  protected abstract AbstractClassAssert<?> toAssert(Class<?> aClass);

  /**
   * Create a new assertion object for the given URL.
   *
   * @param url the URL.
   * @return the assertion.
   */
  @CheckReturnValue
  protected abstract AbstractUrlAssert<?> toAssert(URL url);

  /**
   * Create a new assertion object for the given byte array.
   *
   * @param array the byte array.
   * @return the assertion.
   */
  @CheckReturnValue
  protected abstract AbstractByteArrayAssert<?> toAssert(byte[] array);

  /**
   * Create a new assertion object for the given {@link ClassNotFoundException}.
   *
   * @param ex the exception that was thrown while loading a class.
   * @return the assertion.
   */
  @CheckReturnValue
  protected abstract AbstractThrowableAssert<?, ? extends ClassNotFoundException> toAssert(
    ClassNotFoundException ex
  );

  /**
   * Create a new assertion object for the given iterable of URLs.
   *
   * @param urls the enumeration of URLs.
   * @return the assertion.
   */
  protected abstract AbstractIterableAssert<?, ? extends Iterable<URL>, URL, ? extends AbstractUrlAssert<?>> toUrlIterableAssert(
    Iterable<URL> urls
  );

  /**
   * Create a new assertion object for the given iterable of byte arrays.
   *
   * @param byteArrays the iterable of byte arrays.
   * @return the assertion.
   */
  protected abstract AbstractIterableAssert<?, ? extends Iterable<byte[]>, byte[], ? extends AbstractByteArrayAssert<?>> toByteArrayIterableAssert(
    Iterable<byte[]> byteArrays
  );

  // invariant as URL is a final class.
  @CheckReturnValue
  private Stream<URL> findResourcesForPath(String path) {
    requireNonNullResourcePath(path);
    objects.assertNotNull(info, actual);

    try {
      return Streams.stream(actual.getResources(path));
    } catch (IOException ex) {
      throw new UncheckedIOException(
        "Failed to discover resources for path " + quote(path),
        ex
      );
    }
  }

  // invariant as Class is a final class.
  private Class<?> tryLoadClass(String binaryName) throws ClassNotFoundException {
    requireNonNullBinaryName(binaryName);
    objects.assertNotNull(info, actual);
    return actual.loadClass(binaryName);
  }

  @CheckReturnValue
  private boolean classLoaderIsSystemClassLoader() {
    objects.assertNotNull(info, actual);
    return actual == ClassLoader.getSystemClassLoader();
  }

  @CheckReturnValue
  private boolean classLoaderIsPrivate() {
    return actual instanceof PrivateClassLoader;
  }

  @CheckReturnValue
  private byte[] readResourceFromUrl(URL url) {
    try {
      // Buffered to prevent contention reading classpath resources, especially on slow block
      // device such as those used in CI systems.
      try (InputStream inputStream = new BufferedInputStream(url.openStream())) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        transfer(inputStream, outputStream);
        return outputStream.toByteArray();
      }
    } catch (IOException ex) {
      throw new UncheckedIOException(
        "Failed to read content of resource for URL " + quote(url),
        ex
      );
    }
  }

  private void requireNonNullResourcePath(String path) {
    requireNonNull(
      path,
      "the path to discover resources for should not be null"
    );
  }

  private void requireNonNullBinaryName(String binaryName) {
    requireNonNull(
      binaryName,
      "the binary name of the class to attempt to find should not be null"
    );
  }

  // In Java 9, replace this with `inputStream.transferTo(outputStream)`
  private void transfer(InputStream inputStream, OutputStream outputStream) throws IOException {
    int count;
    byte[] buff = new byte[8192];
    while ((count = inputStream.read(buff)) != -1) {
      outputStream.write(buff, 0, count);
    }
  }

}
