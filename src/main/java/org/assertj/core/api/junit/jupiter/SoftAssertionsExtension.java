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
 * Copyright 2012-2020 the original author or authors.
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
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ConcurrentMap;

import org.assertj.core.api.AbstractSoftAssertions;
import org.assertj.core.api.AssertionErrorCollector;
import org.assertj.core.api.AssertionErrorCollectorImpl;
import org.assertj.core.api.BDDSoftAssertions;
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
 * Extension for JUnit Jupiter that provides support for injecting a concrete
 * implementation of {@link SoftAssertionsProvider} into test methods. Two examples that
 * come packaged with AssertJ are {@link SoftAssertions} and
 * {@link BDDSoftAssertions}, but custom implementations are also supported as
 * long as they have a default constructor.
 *
 * <h2>Applicability</h2>
 *
 * <p>
 * In this context, the term "test method" refers to any method annotated with
 * {@code @Test}, {@code @RepeatedTest}, {@code @ParameterizedTest},
 * {@code @TestFactory}, or {@code @TestTemplate}.<br>
 * This extension does not inject {@code SoftAssertionsProvider} arguments into test
 * constructors or lifecycle methods.
 *
 * <h2>Scope</h2>
 *
 * <p>
 * The scope of the {@code SoftAssertionsProvider} instance managed by this extension
 * begins when a parameter of type {@code SoftAssertionsProvider} is resolved for a test
 * method.<br>
 * The scope of the instance ends after the test method has been executed, this
 * is when {@code assertAll()} will be invoked on the instance to verify that no
 * soft assertions failed.
 *
 * <h3>Example with {@code SoftAssertions}</h3>
 *
 * <pre>
 * <code class='java'> {@literal @}ExtendWith(SoftAssertionsExtension.class)
 * class ExampleTestCase {
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
 * <h3>Example with {@code BDDSoftAssertions}</h3>
 *
 * <pre>
 * <code class='java'> {@literal @}ExtendWith(SoftAssertionsExtension.class)
 * class ExampleTestCase {
 *
 *    {@literal @}Test
 *    void multipleFailures(BDDSoftAssertions softly) {
 *       softly.then(2 * 3).isEqualTo(0);
 *       softly.then(Arrays.asList(1, 2)).containsOnly(1);
 *       softly.then(1 + 1).isEqualTo(2);
 *    }
 * }</code>
 * </pre>
 *
 * @author Sam Brannen
 * @since 3.13
 */
public class SoftAssertionsExtension
    implements TestInstancePostProcessor, BeforeEachCallback, ParameterResolver, AfterTestExecutionCallback {

  private static final Namespace SOFT_ASSERTIONS_EXTENSION_NAMESPACE = Namespace.create(SoftAssertionsExtension.class);

  static class ThreadLocalErrorCollector implements AssertionErrorCollector {

    InheritableThreadLocal<AssertionErrorCollector> tl = new InheritableThreadLocal<>();

    @Override
    public Optional<AssertionErrorCollector> getDelegate() {
      return Optional.of(tl.get());
    }

    @Override
    public void setDelegate(AssertionErrorCollector aec) {
      tl.set(aec);
    }

    public void reset() {
      tl.remove();
    }

    @Override
    public void collectAssertionError(AssertionError error) {
      tl.get().collectAssertionError(error);
    }

    @Override
    public List<AssertionError> assertionErrorsCollected() {
      return tl.get().assertionErrorsCollected();
    }

    @Override
    public void succeeded() {
      tl.get().succeeded();
    }

    @Override
    public boolean wasSuccess() {
      return tl.get().wasSuccess();
    }
  }

  static boolean isPerClass(ExtensionContext context) {
    return context.getTestInstanceLifecycle().map(x -> x == Lifecycle.PER_CLASS).orElse(false);
  }

  static boolean isAnnotatedConcurrent(ExtensionContext context) {
    return findAnnotation(context.getRequiredTestClass(), Execution.class).map(Execution::value)
                                                                          .map(x -> x == ExecutionMode.CONCURRENT).orElse(false);
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

//    final List<SoftAssertionsProvider> providers = new ArrayList<>(softAssertionsFields.size());
    for (Field field : softAssertionsFields) {

      final int fieldModifiers = field.getModifiers();
      if (Modifier.isStatic(fieldModifiers) || Modifier.isFinal(fieldModifiers)) {
        throw new ExtensionConfigurationException(format("[%s] SoftAssertionsProvider field must not be static or final.",
                                                         field.getName()));
      }

      Class<?> providerClass = (Class<?>) field.getType();
      if (!SoftAssertionsProvider.class.isAssignableFrom(providerClass)) {
        throw new ExtensionConfigurationException(format("[%s] field is not a SoftAssertionsProvider (%s).",
                                                         field.getName(), providerClass.getTypeName()));
      }
      @SuppressWarnings("unchecked") // Guaranteed because of the above sanity check
      final Class<? extends SoftAssertionsProvider> softAssertionsProvider = (Class<? extends SoftAssertionsProvider>) providerClass;

      if (Modifier.isAbstract(providerClass.getModifiers())) {
        throw new ExtensionConfigurationException(format("[%s] SoftAssertionsProvider [%s] is abstract and cannot be instantiated.",
                                                         field.getName(), providerClass));
      }
      try {
        providerClass.getDeclaredConstructor();
      } catch (Exception e) {
        throw new ExtensionConfigurationException(format("[%s] SoftAssertionsProvider [%s] does not have a default constructor",
                                                         field.getName(), providerClass.getName()));
      }

      field.setAccessible(true);
      SoftAssertionsProvider softAssertions = getSoftAssertionsProvider(context, softAssertionsProvider);
      try {
        field.set(testInstance, softAssertions);
      } catch (IllegalAccessException e) {
        throw new ExtensionConfigurationException(format("[%s] Could not gain access to field", field.getName()), e);
      }
    }
  }

  @Override
  public void beforeEach(ExtensionContext context) throws Exception {
    AssertionErrorCollector collector = getAssertionErrorCollector(context);

    if (isPerClassConcurrent(context)) {
      ThreadLocalErrorCollector tlec = getThreadLocalCollector(context);
      tlec.setDelegate(collector);
    } else {
      while (initialiseDelegate(context, collector)) {
        context = context.getParent().get();
      }
    }
  }

  private static boolean initialiseDelegate(ExtensionContext context, AssertionErrorCollector collector) {
    Collection<SoftAssertionsProvider> providers = getSoftAssertionsProviders(context);
    if (providers == null) {
      return false;
    }
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
    } catch (Exception e) {
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
      collector = tlec.getDelegate().get();
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
   * Returns the AssertionErrorCollector for the given extension context. If none exists for the current
   * context, then one is created and stored for future retrieval. This way all clients 
   * 
   * @param context the {@code ExtensionContext} whose error collector we are
   * attempting to retrieve.
   * @return The {@code AssertionErrorCollector} for the given context.
   */
  public static AssertionErrorCollector getAssertionErrorCollector(ExtensionContext context) {
    return getStore(context).getOrComputeIfAbsent(AssertionErrorCollector.class, unused -> new AssertionErrorCollectorImpl(),
                                                  AssertionErrorCollector.class);
  }

  @SuppressWarnings("unchecked")
  private static Collection<SoftAssertionsProvider> getSoftAssertionsProviders(ExtensionContext context) {
    return getStore(context).getOrComputeIfAbsent(Collection.class, unused -> new ConcurrentLinkedQueue<>(), Collection.class);
  }

  private static <T extends SoftAssertionsProvider> T instantiateProvider(ExtensionContext context, Class<T> providerType) {
    T softAssertions = ReflectionSupport.newInstance(providerType);
    if (isPerClassConcurrent(context)) {
      softAssertions.setDelegate(getThreadLocalCollector(context));
    } else if (context.getTestMethod().isPresent()) {
      // If we're already in a method, then set our delegate as the beforeEach() which sets it may have already run.
      softAssertions.setDelegate(getAssertionErrorCollector(context));
    }
    getSoftAssertionsProviders(context).add(softAssertions);
    return softAssertions;
  }
  
  public static <T extends SoftAssertionsProvider> T getSoftAssertionsProvider(ExtensionContext context,
                                                                               Class<T> concreteSoftAssertionsProviderType) {
    return getStore(context).getOrComputeIfAbsent(concreteSoftAssertionsProviderType,
                                                  unused -> instantiateProvider(context, concreteSoftAssertionsProviderType),
                                                  concreteSoftAssertionsProviderType);
  }
}
