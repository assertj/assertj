/*
 * Copyright © 2024 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.assertj.core.api.junit.jupiter;

import static java.lang.String.format;
import static java.lang.reflect.Modifier.isAbstract;
import static org.junit.platform.commons.support.AnnotationSupport.findAnnotation;
import static org.junit.platform.commons.support.AnnotationSupport.isAnnotated;
import static org.junit.platform.commons.support.ReflectionSupport.findFields;

import java.lang.reflect.Executable;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Parameter;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentLinkedQueue;
import org.assertj.core.annotations.Beta;
import org.assertj.core.api.AbstractSoftAssertions;
import org.assertj.core.api.AssertionErrorCollector;
import org.assertj.core.api.BDDSoftAssertions;
import org.assertj.core.api.DefaultAssertionErrorCollector;
import org.assertj.core.api.SoftAssertions;
import org.assertj.core.api.SoftAssertionsProvider;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.extension.AfterTestExecutionCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionConfigurationException;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ExtensionContext.Namespace;
import org.junit.jupiter.api.extension.ExtensionContext.Store;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;
import org.junit.jupiter.api.extension.TestInstancePostProcessor;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import org.junit.platform.commons.annotation.Testable;
import org.junit.platform.commons.support.HierarchyTraversalMode;
import org.junit.platform.commons.support.ReflectionSupport;

/**
 * Extension for JUnit Jupiter that provides support for injecting a concrete implementation of {@link SoftAssertionsProvider}
 * into test methods and (since 3.18.0) into test fields annotated with {@code @InjectSoftAssertions}.
 * <p>
 * Two examples of {@code SoftAssertionsProvider}s that come packaged with AssertJ are {@link SoftAssertions} and
 * {@link BDDSoftAssertions}, but custom implementations are also supported as long as they are non-abstract and have a default
 * constructor.
 *
 * <h2>Applicability</h2>
 *
 * <p>
 * In this context, the term "test method" refers to any method annotated with {@code @Test}, {@code @RepeatedTest},
 * {@code @ParameterizedTest}, {@code @TestFactory}, or {@code @TestTemplate}.<br>
 * This extension does not inject {@code SoftAssertionsProvider} arguments into test constructors or lifecycle methods.
 *
 * <h2>Scope</h2>
 *
 * Annotated {@code SoftAssertionsProvider} fields become valid from the `@BeforeEach` lifecycle phase.
 * For parameters, they become valid when the parameter is resolved.<br>
 * In the {@code afterTestExecution} phase (immediately after the test has returned, but before the {@code AfterEach} phase) all
 * collected errors (if any) will be wrapped in a single multiple-failures error.<br>
 * All {@code SoftAssertionsProvider} instances (fields &amp; parameters) created within the scope of the same test method
 * (including its {@code BeforeEach} phase) will share the same state object to collect the failed assertions, so that all
 * assertion failures from all {@link SoftAssertionsProvider}s will be reported in the order that they failed.
 *
 * <h2>Integration with third-party extensions</h2>
 *
 * Sometimes a third-party extension may wish to softly assert something as part of the main test. Or sometimes a third-party
 * extension may be a wrapper around another assertion library (eg, Mockito) and it would be nice for that library's soft
 * assertions to mix well with AssertJ's. This can be achieved through the use of the {@code SoftAssertionExtension}'s API.
 * Calling {@link #getAssertionErrorCollector(ExtensionContext)} will return a handle to the error collector used for the current
 * context into which a third-party extension can directly store its assertion failures. Alternatively, calling
 * {@link #getSoftAssertionsProvider(ExtensionContext, Class) getSoftAssertionsProvider()} will instantiate a
 * {@link SoftAssertionsProvider} for the given context that can then be used to make assertions.
 *
 * <h2>Examples</h2>
 *
 * <h3>Example parameter injection</h3>
 *
 * <pre>
 * <code class='java'> {@literal @}ExtendWith(SoftAssertionsExtension.class)
 * class ExampleTestCase {
 *
 *    {@literal @}InjectSoftAssertions
 *    BDDSoftAssertions bdd;
 *
 *    {@literal @}Test
 *    void multipleFailures(SoftAssertions softly) {
 *       softly.assertThat(2 * 3).isEqualTo(0);
 *       softly.assertThat(Arrays.asList(1, 2)).containsOnly(1);
 *       softly.assertThat(1 + 1).isEqualTo(2);
 *    }
 * }</code>
 * </pre>
 *
 * <h3>Example field injection</h3>
 * <pre><code> {@literal @}ExtendWith(SoftlyExtension.class)
 * public class SoftlyExtensionExample {
 *
 *   // initialized by the SoftlyExtension extension
 *   {@literal @}InjectSoftAssertions
 *   private SoftAssertions soft;
 *
 *   {@literal @}Test
 *   public void chained_soft_assertions_example() {
 *     String name = "Michael Jordan - Bulls";
 *     soft.assertThat(name)
 *         .startsWith("Mi")
 *         .contains("Bulls");
 *     // no need to call softly.assertAll(), this is done by the extension
 *   }
 *
 *   // nested classes test work too
 *   {@literal @}Nested
 *   class NestedExample {
 *
 *     {@literal @}Test
 *     public void football_assertions_example() {
 *       String kylian = "Kylian Mbappé";
 *       soft.assertThat(kylian)
 *           .startsWith("Ky")
 *           .contains("bap");
 *       // no need to call softly.assertAll(), this is done by the extension
 *     }
 *   }
 * } </code></pre>
 *
 * <h3>Example using a mix of field and parameter injection</h3>
 *
 * <pre>
 * <code class='java'> {@literal @}ExtendWith(SoftAssertionsExtension.class)
 * class ExampleTestCase {
 *
 *    {@literal @}InjectSoftAssertions
 *    SoftAssertions softly
 *
 *    {@literal @}Test
 *    void multipleFailures(BDDSoftAssertions bdd) {
 *       bdd.then(2 * 3).isEqualTo(0);
 *       softly.assertThat(Arrays.asList(1, 2)).containsOnly(1);
 *       bdd.then(1 + 1).isEqualTo(2);
 *       // When SoftAssertionsExtension calls assertAll(), the three
 *       // above failures above will be reported in-order.
 *    }
 * }</code>
 * </pre>
 *
 * <h3>Example third-party extension using {@code SoftAssertionsExtension}</h3>
 *
 * <pre>
 * <code class='java'>
 * class ExampleTestCase implements BeforeEachCallback {
 *
 *    {@literal @}Override
 *    public void beforeEach(ExtensionContext context) {
 *      SoftAssertions softly = SoftAssertionsExtension
 *        .getSoftAssertionsProvider(context, SoftAssertions.class);
 *      softly.assertThat(false).isTrue();
 *      // When SoftAssertionsExtension calls assertAll(), the
 *      // above failure will be included in the list of reported failures.
 *    }
 * }</code>
 * </pre>
 *
 * @author Sam Brannen
 * @author Arthur Mita (author of {@link SoftlyExtension})
 * @author Fr Jeremy Krieg
 * @since 3.13
 */
public class SoftAssertionsExtension
    implements TestInstancePostProcessor, BeforeEachCallback, ParameterResolver, AfterTestExecutionCallback {

  private static final Namespace SOFT_ASSERTIONS_EXTENSION_NAMESPACE = Namespace.create(SoftAssertionsExtension.class);

  static class ThreadLocalErrorCollector implements AssertionErrorCollector {

    InheritableThreadLocal<AssertionErrorCollector> threadLocal = new InheritableThreadLocal<>();

    @Override
    public Optional<AssertionErrorCollector> getDelegate() {
      return Optional.of(threadLocal.get());
    }

    @Override
    public void setDelegate(AssertionErrorCollector assertionErrorCollector) {
      threadLocal.set(assertionErrorCollector);
    }

    public void reset() {
      threadLocal.remove();
    }

    @Override
    public void collectAssertionError(AssertionError assertionError) {
      threadLocal.get().collectAssertionError(assertionError);
    }

    @Override
    public List<AssertionError> assertionErrorsCollected() {
      return threadLocal.get().assertionErrorsCollected();
    }

    @Override
    public void succeeded() {
      threadLocal.get().succeeded();
    }

    @Override
    public boolean wasSuccess() {
      return threadLocal.get().wasSuccess();
    }
  }

  static boolean isPerClass(ExtensionContext context) {
    return context.getTestInstanceLifecycle().map(x -> x == Lifecycle.PER_CLASS).orElse(false);
  }

  static boolean isAnnotatedConcurrent(ExtensionContext context) {
    return findAnnotation(context.getRequiredTestClass(), Execution.class).map(Execution::value)
                                                                          .map(x -> x == ExecutionMode.CONCURRENT)
                                                                          .orElse(false);
  }

  static boolean isPerClassConcurrent(ExtensionContext context) {
    return isPerClass(context) && isAnnotatedConcurrent(context);
  }

  @Override
  public void postProcessTestInstance(Object testInstance, ExtensionContext context) throws Exception {
    // find SoftAssertions fields in the test class hierarchy
    Collection<Field> softAssertionsFields = findFields(testInstance.getClass(),
                                                        field -> isAnnotated(field, InjectSoftAssertions.class),
                                                        HierarchyTraversalMode.BOTTOM_UP);
    for (Field softAssertionsField : softAssertionsFields) {
      checkIsNotStaticOrFinal(softAssertionsField);
      Class<? extends SoftAssertionsProvider> softAssertionsProviderClass = asSoftAssertionsProviderClass(softAssertionsField,
                                                                                                          softAssertionsField.getType());
      checkIsNotAbstract(softAssertionsField, softAssertionsProviderClass);
      checkHasDefaultConstructor(softAssertionsField, softAssertionsProviderClass);
      SoftAssertionsProvider softAssertions = getSoftAssertionsProvider(context, softAssertionsProviderClass);
      setTestInstanceSoftAssertionsField(testInstance, softAssertionsField, softAssertions);
    }
  }

  @Override
  public void beforeEach(ExtensionContext context) throws Exception {
    AssertionErrorCollector collector = getAssertionErrorCollector(context);

    if (isPerClassConcurrent(context)) {
      // If the current context is "per class+concurrent", then getSoftAssertionsProvider() will have already set the delegate
      // for all the soft assertions provider to the thread-local error collector, so all we need to do is set the tlec's value
      // for the current thread.
      ThreadLocalErrorCollector tlec = getThreadLocalCollector(context);
      tlec.setDelegate(collector);
    } else {
      // Make sure that all of the soft assertion provider instances have their delegate initialised to the assertion error
      // collector for the current context. Also check enclosing contexts (in the case of nested tests).
      while (initialiseDelegate(context, collector) && context.getParent().isPresent()) {
        context = context.getParent().get();
      }
    }
  }

  private static boolean initialiseDelegate(ExtensionContext context, AssertionErrorCollector collector) {
    Collection<SoftAssertionsProvider> providers = getSoftAssertionsProviders(context);
    if (providers == null) return false;
    providers.forEach(x -> x.setDelegate(collector));
    return context.getParent().isPresent();
  }

  @Override
  public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) {
    // Abort if parameter type is unsupported.
    if (isUnsupportedParameterType(parameterContext.getParameter())) return false;

    Executable executable = parameterContext.getDeclaringExecutable();
    // @Testable is used as a meta-annotation on @Test, @TestFactory, @TestTemplate, etc.
    boolean isTestableMethod = executable instanceof Method && isAnnotated(executable, Testable.class);
    if (!isTestableMethod) {
      throw new ParameterResolutionException(format("Configuration error: cannot resolve SoftAssertionsProvider instances for [%s]. Only test methods are supported.",
                                                    executable));
    }
    Class<?> parameterType = parameterContext.getParameter().getType();
    if (isAbstract(parameterType.getModifiers())) {
      throw new ParameterResolutionException(format("Configuration error: the resolved SoftAssertionsProvider implementation [%s] is abstract and cannot be instantiated.",
                                                    executable));
    }
    try {
      parameterType.getDeclaredConstructor();
    } catch (@SuppressWarnings("unused") Exception e) {
      throw new ParameterResolutionException(format("Configuration error: the resolved SoftAssertionsProvider implementation [%s] has no default constructor and cannot be instantiated.",
                                                    executable));
    }
    return true;
  }

  @Override
  public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) {
    // The parameter type is guaranteed to be an instance of SoftAssertionsProvider
    @SuppressWarnings("unchecked")
    Class<? extends SoftAssertionsProvider> concreteSoftAssertionsProviderType = (Class<? extends SoftAssertionsProvider>) parameterContext.getParameter()
                                                                                                                                           .getType();
    SoftAssertionsProvider provider = ReflectionSupport.newInstance(concreteSoftAssertionsProviderType);
    provider.setDelegate(getAssertionErrorCollector(extensionContext));
    return provider;
  }

  @Override
  public void afterTestExecution(ExtensionContext extensionContext) {
    AssertionErrorCollector collector;
    if (isPerClassConcurrent(extensionContext)) {
      ThreadLocalErrorCollector tlec = getThreadLocalCollector(extensionContext);
      collector = tlec.getDelegate()
                      .orElseThrow(() -> new IllegalStateException("Expecting delegate to be present for current context"));
      tlec.reset();
    } else {
      collector = getAssertionErrorCollector(extensionContext);
    }
    AbstractSoftAssertions.assertAll(collector);
  }

  private static boolean isUnsupportedParameterType(Parameter parameter) {
    Class<?> type = parameter.getType();
    return !SoftAssertionsProvider.class.isAssignableFrom(type);
  }

  private static Store getStore(ExtensionContext extensionContext) {
    return extensionContext.getStore(SOFT_ASSERTIONS_EXTENSION_NAMESPACE);
  }

  private static ThreadLocalErrorCollector getThreadLocalCollector(ExtensionContext context) {
    return getStore(context).getOrComputeIfAbsent(ThreadLocalErrorCollector.class, unused -> new ThreadLocalErrorCollector(),
                                                  ThreadLocalErrorCollector.class);
  }

  /**
   * Returns the {@link AssertionErrorCollector} for the given extension context, if none exists for the current context then
   * one is created.
   * <p>
   * This method is thread safe - all extensions attempting to access the {@code AssertionErrorCollector} for a given context
   * through this method will get a reference to the same {@code AssertionErrorCollector} instance, regardless of the order
   * in which they are called.
   * <p>
   * Third-party extensions that wish to provide soft-asserting behavior can use this method to obtain the current
   * {@code AssertionErrorCollector} instance and record their assertion failures into it by calling
   * {@link AssertionErrorCollector#collectAssertionError(AssertionError) collectAssertionError(AssertionError)}.<br>
   * In this way their soft assertions will integrate with the existing AssertJ soft assertions and the assertion failures (both
   * AssertJ's and the third-party extension's) will be reported in the order that they occurred.
   *
   * @param context the {@code ExtensionContext} whose error collector we are attempting to retrieve.
   * @return The {@code AssertionErrorCollector} for the given context.
   */
  @Beta
  public static AssertionErrorCollector getAssertionErrorCollector(ExtensionContext context) {
    return getStore(context).getOrComputeIfAbsent(AssertionErrorCollector.class, unused -> new DefaultAssertionErrorCollector(),
                                                  AssertionErrorCollector.class);
  }

  @SuppressWarnings("unchecked")
  private static Collection<SoftAssertionsProvider> getSoftAssertionsProviders(ExtensionContext context) {
    return getStore(context).getOrComputeIfAbsent(Collection.class, unused -> new ConcurrentLinkedQueue<>(), Collection.class);
  }

  private static <T extends SoftAssertionsProvider> T instantiateProvider(ExtensionContext context, Class<T> providerType) {
    T softAssertions = ReflectionSupport.newInstance(providerType);
    // If we are running single-threaded, we won't have any concurrency issues. Likewise,
    // if we are running "per-method", then every test gets its own instance and again there
    // won't be any concurrency issues. But we need to special-case the situation where
    // we are running *both* per class and concurrent - use a thread-local so that each thread
    // gets its own copy. The beforeEach() callback above will take care of setting the
    // ThreadLocal collector's value for the thread in which it is executing.
    if (isPerClassConcurrent(context)) {
      softAssertions.setDelegate(getThreadLocalCollector(context));
    } else if (context.getTestMethod().isPresent()) {
      // If we're already in a method, then set our delegate as the beforeEach() which sets it may have already run.
      softAssertions.setDelegate(getAssertionErrorCollector(context));
    }
    getSoftAssertionsProviders(context).add(softAssertions);
    return softAssertions;
  }

  /**
   * Returns a {@link SoftAssertionsProvider} instance of the given type for the given extension context.
   * If no instance of the given type exists for the supplied context, then one is created.<br>
   * Note that the given type must be a concrete type with an accessible no-arg constructor for this method to work.
   * <p>
   * This method is thread safe - all extensions attempting to access the {@code SoftAssertionsProvider} for a
   * given context through this method will receive end up getting a reference to the same
   * {@code SoftAssertionsProvider} instance of that same type, regardless of the order in which they are called.
   * <p>
   * Third party extensions that wish to use soft assertions in their own implementation can use this
   * to get a {@code SoftAssertionsProvider} instance that interoperates with other soft-asserting
   * extensions (including {@code SoftAssertionsExtension}).
   * <p>
   * The {@code SoftAssertionExtension} will take care of initialising this provider instance's delegate
   * at the appropriate time, so that collected soft assertions are routed to the {@link AssertionErrorCollector}
   * instance for the current context.
   *
   * <pre><code class='java'> public class CustomExtension implements BeforeEachCallback {
   *
   *   {@literal @}Override
   *   public void beforeEach(ExtensionContext context) {
   *     CustomSoftAssertions softly = SoftAssertionsExtension.getSoftAssertionsProvider(context, CustomSoftAssertions.class);
   *     softly.assertThat(1).isOne();
   *   }
   * }</code>
   * </pre>
   *
   * @param <T> the type of {@link SoftAssertionsProvider} to instantiate.
   * @param context the {@code ExtensionContext} whose error collector we are attempting to retrieve.
   * @param concreteSoftAssertionsProviderType the class instance for the type of soft assertions
   * @return The {@code AssertionErrorCollector} for the given context.
   */
  @Beta
  public static <T extends SoftAssertionsProvider> T getSoftAssertionsProvider(ExtensionContext context,
                                                                               Class<T> concreteSoftAssertionsProviderType) {
    return getStore(context).getOrComputeIfAbsent(concreteSoftAssertionsProviderType,
                                                  unused -> instantiateProvider(context, concreteSoftAssertionsProviderType),
                                                  concreteSoftAssertionsProviderType);
  }

  private static void setTestInstanceSoftAssertionsField(Object testInstance, Field softAssertionsField,
                                                         SoftAssertionsProvider softAssertions) {
    softAssertionsField.setAccessible(true);
    try {
      softAssertionsField.set(testInstance, softAssertions);
    } catch (IllegalAccessException e) {
      throw new ExtensionConfigurationException(format("[%s] Could not gain access to field", softAssertionsField.getName()), e);
    }
  }

  private static void checkHasDefaultConstructor(Field softAssertionsField,
                                                 Class<? extends SoftAssertionsProvider> softAssertionsProviderClass) {
    try {
      softAssertionsProviderClass.getDeclaredConstructor();
    } catch (@SuppressWarnings("unused") Exception e) {
      throw new ExtensionConfigurationException(format("[%s] SoftAssertionsProvider [%s] does not have a default constructor",
                                                       softAssertionsField.getName(), softAssertionsProviderClass.getName()));
    }
  }

  private static void checkIsNotAbstract(Field softAssertionsField,
                                         Class<? extends SoftAssertionsProvider> softAssertionsProviderClass) {
    if (Modifier.isAbstract(softAssertionsProviderClass.getModifiers())) {
      throw new ExtensionConfigurationException(format("[%s] SoftAssertionsProvider [%s] is abstract and cannot be instantiated.",
                                                       softAssertionsField.getName(), softAssertionsProviderClass));
    }
  }

  @SuppressWarnings("unchecked")
  private static Class<? extends SoftAssertionsProvider> asSoftAssertionsProviderClass(Field softAssertionsField,
                                                                                       Class<?> providerClass) {
    if (!SoftAssertionsProvider.class.isAssignableFrom(providerClass)) {
      throw new ExtensionConfigurationException(format("[%s] field is not a SoftAssertionsProvider (%s).",
                                                       softAssertionsField.getName(), providerClass.getTypeName()));
    }
    // Guaranteed because of the sanity check
    return (Class<? extends SoftAssertionsProvider>) providerClass;
  }

  private static void checkIsNotStaticOrFinal(Field softAssertionsField) {
    int fieldModifiers = softAssertionsField.getModifiers();
    if (Modifier.isStatic(fieldModifiers) || Modifier.isFinal(fieldModifiers)) {
      throw new ExtensionConfigurationException(format("[%s] SoftAssertionsProvider field must not be static or final.",
                                                       softAssertionsField.getName()));
    }
  }

}
