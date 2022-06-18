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
import java.lang.management.ClassLoadingMXBean;
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
   * {@link ClassLoadingMXBean class-loading management bean}, by ensuring that it implements
   * {@link PrivateClassLoader}.
   *
   * <p>This assertion would pass:
   *
   * <pre><code>  // Given
   * class PrivateClassLoader extends ClassLoader
   *        implements javax.management.loading.PrivateClassLoader { }
   * PrivateClassLoader privateClassLoader = new PrivateClassLoader();
   *
   * // Then
   * assertThat(privateClassLoader).isPrivate();
   * </code></pre>
   *
   * <p>This assertion would fail:
   *
   * <pre><code>  // Given
   * class PublicClassLoader extends ClassLoader { }
   * PublicClassLoader publicClassLoader = new PublicClassLoader();
   *
   * // Then
   * assertThat(privateClassLoader).isPrivate();
   * </code></pre>
   *
   * @return {@code this} assertion object.
   * @throws AssertionError if {@code actual} is null.
   * @throws AssertionError if the {@code actual} class loader does not implement the
   *                        {@link PrivateClassLoader required marker interface}.
   * @see #isNotPrivate()
   * @see PrivateClassLoader
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
   * {@link ClassLoadingMXBean class-loading management bean}, by ensuring that it implements
   * {@link PrivateClassLoader}.
   *
   * <p>This assertion would pass:
   *
   * <pre><code>  // Given
   * class PublicClassLoader extends ClassLoader { }
   * PublicClassLoader publicClassLoader = new PublicClassLoader();
   *
   * // Then
   * assertThat(privateClassLoader).isNotPrivate();
   * </code></pre>
   *
   * <p>This assertion would fail:
   *
   * <pre><code>  // Given
   * class PrivateClassLoader extends ClassLoader
   *        implements javax.management.loading.PrivateClassLoader { }
   * PrivateClassLoader privateClassLoader = new PrivateClassLoader();
   *
   * // Then
   * assertThat(privateClassLoader).isNotPrivate();
   * </code></pre>
   *
   * @return {@code this} assertion object.
   * @throws AssertionError if {@code actual} is null.
   * @throws AssertionError if the {@code actual} class loader implements the
   *                        {@link PrivateClassLoader required marker interface}.
   * @see #isNotPrivate()
   * @see PrivateClassLoader
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
   * <p>This assertion would pass:
   *
   * <pre><code>  // Given
   * ClassLoader systemClassLoader = java.lang.Object.class.getClassLoader();
   *
   * // Then
   * assertThat(systemClassLoader).isSystemClassLoader();
   * </code></pre>
   *
   * <p>This assertion would fail:
   *
   * <pre><code>  // Given
   * URL url = new URL("file:///home/user/code/classes");
   * ClassLoader customClassLoader = new URLClassLoader(new URL[]{ url });
   *
   * // Then
   * assertThat(systemClassLoader).isSystemClassLoader();
   * </code></pre>
   *
   * <p><strong>Note:</strong> this is NOT the same as checking for the
   * <em>Platform Class Loader</em> (which is also known as the <em>Bootstrap Class Loader</em>)
   * on JDK 9 and above.
   *
   * @return {@code this} assertion object.
   * @throws AssertionError if {@code actual} is null.
   * @throws AssertionError if the {@code actual} class loader is not the same object as the system
   *                        class loader.
   * @see #isNotSystemClassLoader()
   * @see ClassLoader#getSystemClassLoader()
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
   * <p>This assertion would pass:
   *
   * <pre><code>  // Given
   * URL url = new URL("file:///home/user/code/classes");
   * ClassLoader customClassLoader = new URLClassLoader(new URL[]{ url });
   *
   * // Then
   * assertThat(systemClassLoader).isNotSystemClassLoader();
   * </code></pre>
   *
   * <p>This assertion would fail:
   *
   * <pre><code>  // Given
   * ClassLoader systemClassLoader = java.lang.Object.class.getClassLoader();
   *
   * // Then
   * assertThat(systemClassLoader).isNotSystemClassLoader();
   * </code></pre>
   *
   * <p><strong>Note:</strong> this is NOT the same as checking for the
   * <em>Platform Class Loader</em> (which is also known as the <em>Bootstrap Class Loader</em>)
   * on JDK 9 and above.
   *
   * @return {@code this} assertion object.
   * @throws AssertionError if {@code actual} is null.
   * @throws AssertionError if the {@code actual} class loader is the same object as the system
   *                        class loader.
   * @see #isSystemClassLoader()
   * @see ClassLoader#getSystemClassLoader()
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
   * <p>This assertion would pass:
   *
   * <pre><code>  // Given
   * ClassLoader parent = new ClassLoader();
   * ClassLoader child = new ClassLoader(parent);
   *
   * // Then
   * assertThat(child).hasParent();
   * </code></pre>
   *
   * <p>This assertion would fail:
   *
   * <pre><code>  // Given
   * ClassLoader classLoader = new ClassLoader();
   *
   * // Then
   * assertThat(classLoader).hasParent();
   * </code></pre>
   *
   * @return {@code this} assertion object.
   * @throws AssertionError if {@code actual} is {@code null}.
   * @throws AssertionError if the actual {@code ClassLoader} has no parent.
   * @see #hasNoParent()
   * @see #parent()
   * @see ClassLoader#getParent()
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
   * <p>This assertion would pass:
   *
   * <pre><code>  // Given
   * ClassLoader classLoader = new ClassLoader();
   *
   * // Then
   * assertThat(classLoader).hasParent();
   * </code></pre>
   *
   * <p>This assertion would fail:
   *
   * <pre><code>  // Given
   * ClassLoader parent = new ClassLoader();
   * ClassLoader child = new ClassLoader(parent);
   *
   * // Then
   * assertThat(child).hasNoParent();
   * </code></pre>
   *
   * @return {@code this} assertion object.
   * @throws AssertionError if {@code actual} is {@code null}.
   * @throws AssertionError if the actual {@code ClassLoader} has a non-{@code null} parent.
   * @see #hasParent()
   * @see #parent()
   * @see ClassLoader#getParent()
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
   * <p>The parent class loader will be considered to be {@code null} if no parent class
   * loader is provided.
   *
   * <p>This assertion would pass:
   *
   * <pre><code>  // Given
   * ClassLoader classLoader = new ClassLoader();
   *
   * // Then
   * assertThat(classLoader).parent().isNull();
   * </code></pre>
   *
   * <p>This assertion would also pass:
   *
   * <pre><code>  // Given
   * ClassLoader parent = new ClassLoader();
   * ClassLoader child = new ClassLoader(parent);
   *
   * // Then
   * assertThat(child).parent().isSameAs(parent);
   * </code></pre>
   *
   * <p>This assertion would fail:
   *
   * <pre><code>  // Given
   * ClassLoader parent = new ClassLoader();
   * ClassLoader child = new ClassLoader(parent);
   *
   * // Then
   * assertThat(child).parent().isNull();
   * </code></pre>
   *
   * @return the assertions to perform on the parent class loader.
   * @throws AssertionError if the actual {@code ClassLoader} is {@code null}.
   * @see #hasParent()
   * @see #hasNoParent()
   * @see ClassLoader#getParent()
   */
  @CheckReturnValue
  public AbstractClassLoaderAssert<?> parent() {
    objects.assertNotNull(info, actual);
    return toAssert(actual.getParent());
  }

  /**
   * Assert that at least one resource with the given path exists in the class loader.
   *
   * <p>This assertion would pass:
   *
   * <pre><code>  // Given
   * ClassLoader classLoader = Assertions.class.getClassLoader();
   *
   * // Then
   * assertThat(classLoader).hasResource("org/assertj/core/api/Assertions.class");
   * </code></pre>
   *
   * <p>This assertion would fail:
   *
   * <pre><code>  // Given
   * ClassLoader classLoader = Assertions.class.getClassLoader();
   *
   * // Then
   * assertThat(classLoader).hasResource("org/assertj/core/api/SomeRandomClassThatDoesNotExist.class");
   * </code></pre>
   *
   * <p><strong>Note: </strong>this calls {@link ClassLoader#getResources(String)} and checks the
   * first result. This means that an exception will be thrown if a resource cannot be read from the
   * classloader due to an {@link IOException}. This contrasts with the behaviour of
   * {@link ClassLoader#getResource(String)}, which cannot throw an {@link IOException} and must
   * instead return {@code null}. This is an intentional design decision to provide assertion errors
   * with more meaningful error messages.
   *
   * @param path the path of the resource to look up.
   * @return {@code this} assertion object.
   * @throws NullPointerException if the {@code path} is null.
   * @throws AssertionError       if {@code actual} is null.
   * @throws AssertionError       if no resource is found matching the given {@code path}.
   * @throws UncheckedIOException if the call to {@link ClassLoader#getResources(String)} throws an
   *                              {@link IOException}.
   * @see #hasNoResource(String)
   * @see #resourceContent(String)
   * @see #resourceContents(String)
   * @see #resourceUrl(String)
   * @see #resourceUrls(String)
   * @see ClassLoader#getResources(String)
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
   * <p>This assertion would pass:
   *
   * <pre><code>  // Given
   * ClassLoader classLoader = Assertions.class.getClassLoader();
   *
   * // Then
   * assertThat(classLoader).hasNoResource("org/assertj/core/api/SomeRandomClassThatDoesNotExist.class");
   * </code></pre>
   *
   * <p>This assertion would fail:
   *
   * <pre><code>  // Given
   * ClassLoader classLoader = Assertions.class.getClassLoader();
   *
   * // Then
   * assertThat(classLoader).hasNoResource("org/assertj/core/api/Assertions.class");
   * </code></pre>
   *
   * <p><strong>Note: </strong>this calls {@link ClassLoader#getResources(String)} and checks the
   * first result. This means that an exception will be thrown if a resource cannot be read from the
   * classloader due to an {@link IOException}. This contrasts with the behaviour of
   * {@link ClassLoader#getResource(String)}, which cannot throw an {@link IOException} and must
   * instead return {@code null}. This is an intentional design decision to provide assertion errors
   * with more meaningful error messages.
   *
   * @param path the path of the resource to look up.
   * @return {@code this} assertion object.
   * @throws NullPointerException if the {@code path} is null.
   * @throws AssertionError       if {@code actual} is null.
   * @throws AssertionError       if a resource is found matching the given {@code path}.
   * @throws UncheckedIOException if the call to {@link ClassLoader#getResources(String)} throws an
   *                              {@link IOException}.
   * @see #hasResource(String)
   * @see #resourceContent(String)
   * @see #resourceContents(String)
   * @see #resourceUrl(String)
   * @see #resourceUrls(String)
   * @see ClassLoader#getResources(String)
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
   * Perform assertions on the byte content of a resource in the given class loader, if present.
   *
   * <p>If the resource is not present, then a {@code null} resource will be used for the returned
   * assertion object.
   *
   * <p>For example, suppose you were to add a file to your class path at
   * {@code /foo/bar/Greeting.txt} with the following content (without any trailing space or
   * newlines):
   *
   * <pre><code>Hello, World!</code></pre>
   *
   * <p>In this case, you can expect the following assertion to pass:
   *
   * <pre><code>  // Then
   * assertThat(classLoader)
   *     .resourceContent("foo/bar/Greeting.txt")
   *     .asString()
   *     .hasContent("Hello, World!");
   * </code></pre>
   *
   * <p>...assuming {@code classLoader} refers to the class loader of your package.
   *
   * <p>If no file exists with the given path, you can expect the following assertion to pass:
   *
   * <pre><code>  // Then
   * assertThat(classLoader)
   *     .resourceContent("some-file-that-does-not-exist.json")
   *     .isNull();
   * </code></pre>
   *
   * <p><strong>Note: </strong>this calls {@link ClassLoader#getResources(String)} and checks the
   * first result. This means that an exception will be thrown if a resource cannot be read from the
   * classloader due to an {@link IOException}. This contrasts with the behaviour of
   * {@link ClassLoader#getResource(String)}, which cannot throw an {@link IOException} and must
   * instead return {@code null}. This is an intentional design decision to provide assertion errors
   * with more meaningful error messages.
   *
   * @param path the path of the resource to look up.
   * @return the assertion to perform on the resource content. If not found, the resource may be
   * null.
   * @throws NullPointerException if the {@code path} is null.
   * @throws AssertionError       if {@code actual} is null.
   * @throws UncheckedIOException if the call to {@link ClassLoader#getResources(String)} throws an
   *                              {@link IOException}.
   * @see #hasResource(String)
   * @see #hasNoResource(String)
   * @see #resourceContents(String)
   * @see #resourceUrl(String)
   * @see #resourceUrls(String)
   * @see ClassLoader#getResources(String)
   */
  @CheckReturnValue
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
   * the iterable of byte array contents of those resources.
   *
   * <p>This is functionally the same as {@link #resourceContent(String)}, but will check for
   * all matches for the given path. This scenario can occur when multiple root paths are handled by
   * the same class loader. A common occurrence of this is with {@link java.net.URLClassLoader},
   * which can handle multiple packages, modules, JAR, and WAR files.
   *
   * <p>For example, suppose you have two JAR files on your class path, and both contain a file
   * called {@code foo/bar/baz.txt}. This method would return an iterable containing assertions for
   * both of those files, in which ever order the {@code actual} class loader returns them in.
   *
   * @param path the path of the resource to look up.
   * @return the assertion to perform on the iterable of resource contents.
   * @throws NullPointerException if the {@code path} is null.
   * @throws AssertionError       if {@code actual} is null.
   * @throws UncheckedIOException if the call to {@link ClassLoader#getResources(String)} throws an
   *                              {@link IOException}.
   * @see #hasResource(String)
   * @see #hasNoResource(String)
   * @see #resourceContent(String)
   * @see #resourceUrl(String)
   * @see #resourceUrls(String)
   * @see ClassLoader#getResources(String)
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
   * Perform assertions on a resource URL that exists in the class loader, if present.
   *
   * <p>If the resource is not present, then a {@code null} resource will be used for the returned
   * assertion object.
   *
   * <p>For example, suppose you were to add a file to your class path at
   * {@code /foo/bar/Greeting.txt}, then you can expect the following assertion to pass:
   *
   * <pre><code>  // Then
   * assertThat(classLoader)
   *     .resourceUrl("foo/bar/Greeting.txt")
   *     .hasPath("/foo/bar/Greeting.txt");
   * </code></pre>
   *
   * <p>...assuming {@code classLoader} refers to the class loader of your package.
   *
   * <p>If no file exists with the given path, you can expect the following assertion to pass:
   *
   * <pre><code>  // Then
   * assertThat(classLoader)
   *     .resourceUrl("some-file-that-does-not-exist.json")
   *     .isNull();
   * </code></pre>
   *
   * <p><strong>Note: </strong>this calls {@link ClassLoader#getResources(String)} and checks the
   * first result. This means that an exception will be thrown if a resource cannot be read from the
   * classloader due to an {@link IOException}. This contrasts with the behaviour of
   * {@link ClassLoader#getResource(String)}, which cannot throw an {@link IOException} and must
   * instead return {@code null}. This is an intentional design decision to provide assertion errors
   * with more meaningful error messages.
   *
   * @param path the path of the resource to look up.
   * @return the assertion to perform on the resource. If not found, the resource may be null.
   * @throws NullPointerException if the {@code path} is null.
   * @throws AssertionError       if {@code actual} is null.
   * @throws UncheckedIOException if the call to {@link ClassLoader#getResources(String)} throws an
   *                              {@link IOException}.
   * @see #hasResource(String)
   * @see #hasNoResource(String)
   * @see #resourceContent(String)
   * @see #resourceContents(String)
   * @see #resourceUrls(String)
   * @see ClassLoader#getResources(String)
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
   * Discover all resources with the given path using the class loader, and perform assertions on
   * the iterable of URL results.
   *
   * <p>This is functionally the same as {@link #resourceUrl(String)}, but will check for
   * all matches for the given path. This scenario can occur when multiple root paths are handled by
   * the same class loader. A common occurrence of this is with {@link java.net.URLClassLoader},
   * which can handle multiple packages, modules, JAR, and WAR files.
   *
   * <p>For example, suppose you have two JAR files on your class path, and both contain a file
   * called {@code foo/bar/baz.txt}. This method would return an iterable containing assertions for
   * both of those files, in which ever order the {@code actual} class loader returns them in.
   *
   * @param path the path of the resource to look up.
   * @return the assertion to perform on the resources.
   * @throws NullPointerException if the {@code path} is null.
   * @throws AssertionError       if {@code actual} is null.
   * @throws UncheckedIOException if the call to {@link ClassLoader#getResources(String)} throws an
   *                              {@link IOException}.
   * @see #hasResource(String)
   * @see #hasNoResource(String)
   * @see #resourceContent(String)
   * @see #resourceContents(String)
   * @see #resourceUrl(String)
   * @see ClassLoader#getResources(String)
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
   * Assert that the class loader can load a class for the given binary name successfully.
   *
   * <p>This assertion would pass:
   *
   * <pre><code>  // Given
   * ClassLoader classLoader = Assertions.class.getClassLoader();
   *
   * // Then
   * assertThat(classLoader)
   *    .canLoadClass("org.assertj.core.api.BDDAssertions")
   *    .isPublic();
   * </code></pre>
   *
   * <p>This assertion would fail (since the class being loaded does not exist):
   *
   * <pre><code>  // Given
   * ClassLoader classLoader = Assertions.class.getClassLoader();
   *
   * // Then
   * assertThat(classLoader)
   *    .canLoadClass("org.assertj.core.api.GherkinAssertions")
   *    .isPublic();
   * </code></pre>
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
   * @throws AssertionError       if a {@link LinkageError} is thrown while loading the class with
   *                              the given {@code binaryName}.
   * @throws AssertionError       if a {@link RuntimeException} is thrown while loading the class
   *                              with the given {@code binaryName}.
   * @see #canNotLoadClass(String)
   * @see ClassLoader#loadClass(String)
   */
  public AbstractClassAssert<?> canLoadClass(String binaryName) {
    requireNonNullBinaryName(binaryName);
    objects.assertNotNull(info, actual);

    try {
      Class<?> clazz = actual.loadClass(binaryName);
      return toAssert(clazz);

    } catch (ClassNotFoundException | LinkageError | RuntimeException ex) {
      // We only handle specific types of exception. These are:
      //
      // - ClassNotFoundException - checked Exception that can be thrown per the method signature.
      // - LinkageError - specific type of Error that the JVM may throw if class linkage is invalid.
      // - RuntimeException - any exceptions that are unchecked that we can throw without altering
      //     the signature of ClassLoader#loadClass.
      //
      // We do not handle other Error types because this may hide other bugs in the code or unit
      // test runners (such as ThreadDeath, other nested AssertionErrors, OutOfMemoryError, etc).
      //
      // We also do not handle other checked exceptions.
      //
      // Some may argue that this should not actually be able to occur with the contract of how the
      // Java compiler works handling checked exceptions. The reality is that this is only enforced
      // at compile time, and other languages like Kotlin, Scala, Groovy, Clojure, and Concurnas
      // discard this concept entirely. Furthermore, if the user is using Lombok, they can hide
      // checked exceptions using @SneakyThrows. We choose to not handle these exceptions because
      // it would violate the API contract for the class loader according to the specification for
      // the Java Standard Library -- we don't want to encourage accepting behaviour that cannot be
      // handled in Java code directly (it can be a compile error to try and catch a checked
      // exception that is not explicitly thrown in some cases).

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
   * <p>This assertion would pass (since the class being loaded does not exist):
   *
   * <pre><code>  // Given
   * ClassLoader classLoader = Assertions.class.getClassLoader();
   *
   * // Then
   * assertThat(classLoader)
   *    .canNotLoadClass("org.assertj.core.api.GherkinAssumptions");
   * </code></pre>
   *
   * <p>This assertion would fail (since the class would be able to be loaded):
   *
   * <pre><code>  // Given
   * ClassLoader classLoader = Assertions.class.getClassLoader();
   *
   * // Then
   * assertThat(classLoader)
   *    .canNotLoadClass("org.assertj.core.api.BDDAssumptions")
   *    .isInstanceOf(ClassNotFoundException.class);
   * </code></pre>
   *
   * <p>This assertion would also fail (since the exception being thrown would be expected to be an
   * instance of {@link ClassNotFoundException} instead):
   *
   * <pre><code>  // Given
   * ClassLoader classLoader = Assertions.class.getClassLoader();
   *
   * // Then
   * assertThat(classLoader)
   *    .canNotLoadClass("org.assertj.core.api.GherkinAssumptions")
   *    .isInstanceOf(NullPointerException.class);
   * </code></pre>
   *
   * <p>You should always consider checking the result of this call to
   * ensure the expected exception was thrown. Failing to do this may result in missing bugs in code
   * that is being tested.
   *
   * <p><strong>Note:</strong> This will call {@link ClassLoader#getResources(String)} internally,
   * which may change the class loader state depending on the implementation.
   *
   * @param binaryName the binary name of the class to attempt to load.
   * @return assertions to perform on the loaded class that is returned.
   * @throws NullPointerException if {@code binaryName} is null.
   * @throws AssertionError       if {@code actual} is null.
   * @throws AssertionError       if no {@link ClassNotFoundException}, {@link RuntimeException}, or
   *                              {@link LinkageError} is thrown while loading the class with the
   *                              given {@code binaryName}.
   * @see #canLoadClass(String)
   * @see ClassLoader#loadClass(String)
   */
  @CheckReturnValue
  public AbstractThrowableAssert<?, ? extends Throwable> canNotLoadClass(
    String binaryName
  ) {
    requireNonNullBinaryName(binaryName);
    objects.assertNotNull(info, actual);

    try {
      actual.loadClass(binaryName);
      throw failures.failure(info, shouldNotLoadClassSuccessfully(actual, binaryName));
    } catch (ClassNotFoundException | LinkageError | RuntimeException ex) {
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
   * Create a new assertion object for the given {@link ClassNotFoundException} or
   * {@link LinkageError}.
   *
   * @param ex the exception or error that was thrown while loading a class.
   * @return the assertion.
   */
  @CheckReturnValue
  protected abstract AbstractThrowableAssert<?, ? extends Throwable> toAssert(
    Throwable ex
  );

  /**
   * Create a new assertion object for the given iterable of URLs.
   *
   * @param urls the enumeration of URLs.
   * @return the assertion.
   */
  @CheckReturnValue
  protected abstract AbstractIterableAssert<?, ? extends Iterable<URL>, URL, ? extends AbstractUrlAssert<?>> toUrlIterableAssert(
    Iterable<URL> urls
  );

  /**
   * Create a new assertion object for the given iterable of byte arrays.
   *
   * @param byteArrays the iterable of byte arrays.
   * @return the assertion.
   */
  @CheckReturnValue
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
      try (InputStream inputStream = url.openStream()) {
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
