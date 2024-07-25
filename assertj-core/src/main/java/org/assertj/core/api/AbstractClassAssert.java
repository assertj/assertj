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
 * Copyright 2012-2024 the original author or authors.
 */
package org.assertj.core.api;

import static java.util.Objects.requireNonNull;
import static org.assertj.core.error.ClassModifierShouldBe.shouldBeFinal;
import static org.assertj.core.error.ClassModifierShouldBe.shouldBePackagePrivate;
import static org.assertj.core.error.ClassModifierShouldBe.shouldBePrivate;
import static org.assertj.core.error.ClassModifierShouldBe.shouldBeProtected;
import static org.assertj.core.error.ClassModifierShouldBe.shouldBePublic;
import static org.assertj.core.error.ClassModifierShouldBe.shouldBeStatic;
import static org.assertj.core.error.ClassModifierShouldBe.shouldNotBeFinal;
import static org.assertj.core.error.ClassModifierShouldBe.shouldNotBeStatic;
import static org.assertj.core.error.ShouldBeAbstract.shouldBeAbstract;
import static org.assertj.core.error.ShouldBeAnnotation.shouldBeAnnotation;
import static org.assertj.core.error.ShouldBeAnnotation.shouldNotBeAnnotation;
import static org.assertj.core.error.ShouldBeAssignableTo.shouldBeAssignableTo;
import static org.assertj.core.error.ShouldBeInterface.shouldBeInterface;
import static org.assertj.core.error.ShouldBeInterface.shouldNotBeInterface;
import static org.assertj.core.error.ShouldBePrimitive.shouldBePrimitive;
import static org.assertj.core.error.ShouldBeRecord.shouldBeRecord;
import static org.assertj.core.error.ShouldBeRecord.shouldNotBeRecord;
import static org.assertj.core.error.ShouldBeSealed.shouldBeSealed;
import static org.assertj.core.error.ShouldBeSealed.shouldNotBeSealed;
import static org.assertj.core.error.ShouldHaveNoPackage.shouldHaveNoPackage;
import static org.assertj.core.error.ShouldHaveNoSuperclass.shouldHaveNoSuperclass;
import static org.assertj.core.error.ShouldHavePackage.shouldHavePackage;
import static org.assertj.core.error.ShouldHavePermittedSubclasses.shouldHavePermittedSubclasses;
import static org.assertj.core.error.ShouldHaveRecordComponents.shouldHaveRecordComponents;
import static org.assertj.core.error.ShouldHaveSuperclass.shouldHaveSuperclass;
import static org.assertj.core.error.ShouldNotBeNull.shouldNotBeNull;
import static org.assertj.core.error.ShouldNotBePrimitive.shouldNotBePrimitive;
import static org.assertj.core.util.Arrays.array;
import static org.assertj.core.util.Sets.newLinkedHashSet;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.assertj.core.internal.Classes;

/**
 * Base class for all implementations of assertions for {@link Class}es.
 *
 * @author William Delanoue
 * @author Mikhail Mazursky
 *
 * @param <SELF> the "self" type of this assertion class. Please read &quot;<a href="http://bit.ly/1IZIRcY"
 *          target="_blank">Emulating 'self types' using Java Generics to simplify fluent API implementation</a>&quot;
 *          for more details.
 */
public abstract class AbstractClassAssert<SELF extends AbstractClassAssert<SELF>>
    extends AbstractAssert<SELF, Class<?>> {

  Classes classes = Classes.instance();

  protected AbstractClassAssert(Class<?> actual, Class<?> selfType) {
    super(actual, selfType);
  }

  /**
   * Verifies that the actual {@code Class} is assignable from others {@code Class}
   * <p>
   * Example:
   * <pre><code class='java'> class Jedi {}
   * class HumanJedi extends Jedi {}
   *
   * // this assertion succeeds:
   * assertThat(Jedi.class).isAssignableFrom(HumanJedi.class);
   *
   * // this assertion fails:
   * assertThat(HumanJedi.class).isAssignableFrom(Jedi.class);</code></pre>
   *
   * @param others {@code Class} who can be assignable from.
   * @return {@code this} assertions object
   * @throws AssertionError if the actual {@code Class} is {@code null}.
   * @throws AssertionError if the actual {@code Class} is not assignable from all of the {@code others} classes.
   * @throws IllegalArgumentException if no {@code others} classes have been specified.
   *
   * @see Class#isAssignableFrom(Class)
   */
  public SELF isAssignableFrom(Class<?>... others) {
    classes.assertIsAssignableFrom(info, actual, others);
    return myself;
  }

  /**
   * Verifies that the {@code Class} under test is assignable to the given {@code Class}.
   * <p>
   * Example:
   * <pre><code class='java'> class Jedi {}
   * class HumanJedi extends Jedi {}
   *
   * // this assertion succeeds:
   * assertThat(HumanJedi.class).isAssignableTo(Jedi.class);
   *
   * // this assertion fails
   * assertThat(Jedi.class).isAssignableTo(HumanJedi.class);</code></pre>
   *
   * @param other {@code Class} who can be assignable to.
   * @return {@code this} assertions object
   * @throws AssertionError if the actual {@code Class} is {@code null}.
   * @throws AssertionError if the actual {@code Class} is not assignable to the {@code others} class.
   * @throws IllegalArgumentException if {@code other} is null.
   *
   * @see Class#isAssignableFrom(Class)
   * @since 3.24.0
   */
  public SELF isAssignableTo(Class<?> other) {
    isNotNull();
    assertIsAssignableTo(other);
    return myself;
  }

  private void assertIsAssignableTo(Class<?> other) {
    requireNonNull(other, shouldNotBeNull("other")::create);
    if (!other.isAssignableFrom(actual)) throw assertionError(shouldBeAssignableTo(actual, other));
  }

  /**
   * Verifies that the actual {@code Class} is not an interface.
   * <p>
   * Example:
   * <pre><code class='java'> interface Jedi {}
   * class HumanJedi implements Jedi {}
   *
   * // this assertion succeeds:
   * assertThat(HumanJedi.class).isNotInterface();
   *
   * // this assertion fails:
   * assertThat(Jedi.class).isNotInterface();</code></pre>
   *
   * @return {@code this} assertions object
   * @throws AssertionError if {@code actual} is {@code null}.
   * @throws AssertionError if the actual {@code Class} is not an interface.
   */
  public SELF isNotInterface() {
    isNotNull();
    assertIsNotInterface();
    return myself;
  }

  private void assertIsNotInterface() {
    if (actual.isInterface()) throw assertionError(shouldNotBeInterface(actual));
  }

  /**
   * Verifies that the actual {@code Class} is an interface.
   * <p>
   * Example:
   * <pre><code class='java'> interface Jedi {}
   * class HumanJedi implements Jedi {}
   *
   * // this assertion succeeds:
   * assertThat(Jedi.class).isInterface();
   *
   * // this assertion fails:
   * assertThat(HumanJedi.class).isInterface();</code></pre>
   *
   * @return {@code this} assertions object
   * @throws AssertionError if {@code actual} is {@code null}.
   * @throws AssertionError if the actual {@code Class} is not an interface.
   */
  public SELF isInterface() {
    isNotNull();
    assertIsInterface();
    return myself;
  }

  private void assertIsInterface() {
    if (!actual.isInterface()) throw assertionError(shouldBeInterface(actual));
  }

  /**
   * Verifies that the actual {@code Class} is abstract (has {@code abstract} modifier).
   * <p>
   * Example:
   * <pre><code class='java'> public abstract class MyClass { }
   *
   * // this assertion succeeds:
   * assertThat(MyClass.class).isAbstract();
   *
   * // this assertion fails:
   * assertThat(String.class).isAbstract();</code></pre>
   *
   * @return {@code this} assertions object
   * @throws AssertionError if {@code actual} is {@code null}.
   * @throws AssertionError if the actual {@code Class} is not abstract.
   *
   * @since 3.12.0
   */
  public SELF isAbstract() {
    isNotNull();
    assertIsAbstract();
    return myself;
  }

  private void assertIsAbstract() {
    if (!Modifier.isAbstract(actual.getModifiers())) throw assertionError(shouldBeAbstract(actual));
  }

  /**
   * Verifies that the actual {@code Class} is an annotation.
   * <p>
   * Example:
   * <pre><code class='java'> public @interface Jedi {}
   *
   * // these assertions succeed:
   * assertThat(Jedi.class).isAnnotation();
   * assertThat(Override.class).isAnnotation();
   * assertThat(Deprecated.class).isAnnotation();
   *
   * // this assertion fails:
   * assertThat(String.class).isAnnotation();</code></pre>
   *
   * @return {@code this} assertions object
   * @throws AssertionError if {@code actual} is {@code null}.
   * @throws AssertionError if the actual {@code Class} is not an annotation.
   */
  public SELF isAnnotation() {
    isNotNull();
    assertIsAnnotation();
    return myself;
  }

  private void assertIsAnnotation() {
    if (!actual.isAnnotation()) throw assertionError(shouldBeAnnotation(actual));
  }

  /**
   * Verifies that the actual {@code Class} is not an annotation.
   * <p>
   * Example:
   * <pre><code class='java'> public @interface Jedi {}
   *
   * // this assertion succeeds:
   * assertThat(String.class).isNotAnnotation();
   *
   * // these assertions fail:
   * assertThat(Jedi.class).isNotAnnotation();
   * assertThat(Override.class).isNotAnnotation();
   * assertThat(Deprecated.class).isNotAnnotation();</code></pre>
   *
   * @return {@code this} assertions object
   * @throws AssertionError if {@code actual} is {@code null}.
   * @throws AssertionError if the actual {@code Class} is an annotation.
   */
  public SELF isNotAnnotation() {
    isNotNull();
    assertIsNotAnnotation();
    return myself;
  }

  private void assertIsNotAnnotation() {
    if (actual.isAnnotation()) throw assertionError(shouldNotBeAnnotation(actual));
  }

  /**
   * Verifies that the actual {@code Class} is a record.
   * <p>
   * Example:
   * <pre><code class='java'> public record Jedi(String name) {}
   *
   * // this assertion succeeds:
   * assertThat(Jedi.class).isRecord();
   *
   * // this assertion fails:
   * assertThat(String.class).isRecord();</code></pre>
   *
   * @return {@code this} assertions object
   * @throws AssertionError if {@code actual} is {@code null}.
   * @throws AssertionError if the actual {@code Class} is not a record.
   *
   * @since 3.25.0
   */
  public SELF isRecord() {
    isNotNull();
    assertIsRecord();
    return myself;
  }

  private void assertIsRecord() {
    if (!isRecord(actual)) throw assertionError(shouldBeRecord(actual));
  }

  /**
   * Verifies that the actual {@code Class} is not a record.
   * <p>
   * Example:
   * <pre><code class='java'> public record Jedi(String name) {}
   *
   * // this assertion succeeds:
   * assertThat(String.class).isNotRecord();
   *
   * // this assertion fails:
   * assertThat(Jedi.class).isNotRecord();</code></pre>
   *
   * @return {@code this} assertions object
   * @throws AssertionError if {@code actual} is {@code null}.
   * @throws AssertionError if the actual {@code Class} is a record.
   *
   * @since 3.25.0
   */
  public SELF isNotRecord() {
    isNotNull();
    assertIsNotRecord();
    return myself;
  }

  private void assertIsNotRecord() {
    if (isRecord(actual)) throw assertionError(shouldNotBeRecord(actual));
  }

  // TODO https://github.com/assertj/assertj/issues/3079
  private static boolean isRecord(Class<?> actual) {
    try {
      Method isRecord = Class.class.getMethod("isRecord");
      return (boolean) isRecord.invoke(actual);
    } catch (NoSuchMethodException e) {
      return false;
    } catch (ReflectiveOperationException e) {
      throw new IllegalStateException(e);
    }
  }

  /**
   * Verifies that the actual {@code Class} has the given record components
   * <p>
   * Example:
   * <pre><code class='java'> public class NotARecord {}
   *
   * public record MyRecord(String componentOne, String componentTwo) {}
   *
   * // these assertions succeed:
   * assertThat(MyRecord.class).hasRecordComponents("componentOne");
   * assertThat(MyRecord.class).hasRecordComponents("componentOne", "componentTwo");
   *
   * // these assertions fail:
   * assertThat(NotARecord.class).hasRecordComponents("componentOne");
   * assertThat(MyRecord.class).hasRecordComponents("componentOne", "unknownComponent");</code></pre>
   *
   * @param first the first record component name which must be in this class
   * @param rest the remaining record component names which must be in this class
   * @return {@code this} assertions object
   * @throws AssertionError if {@code actual} is {@code null}.
   * @throws AssertionError if the actual {@code Class} is not a record.
   * @throws AssertionError if the actual {@code Class} doesn't contain all the record component names.
   *
   * @since 3.25.0
   */
  public SELF hasRecordComponents(String first, String... rest) {
    isRecord();
    assertHasRecordComponents(first, rest);
    return myself;
  }

  private void assertHasRecordComponents(String first, String[] rest) {
    Set<String> expectedRecordComponents = newLinkedHashSet();
    expectedRecordComponents.add(first);
    if (rest != null) {
      Collections.addAll(expectedRecordComponents, rest);
    }
    Set<String> missingRecordComponents = newLinkedHashSet();
    Set<String> actualRecordComponents = getRecordComponentNames(actual);

    for (String name : expectedRecordComponents) {
      if (!actualRecordComponents.contains(name)) {
        missingRecordComponents.add(name);
      }
    }
    if (!missingRecordComponents.isEmpty()) {
      throw assertionError(shouldHaveRecordComponents(actual, expectedRecordComponents, missingRecordComponents));
    }
  }

  private static Set<String> getRecordComponentNames(Class<?> actual) {
    try {
      Method getRecordComponents = Class.class.getMethod("getRecordComponents");
      Object[] recordComponents = (Object[]) getRecordComponents.invoke(actual);
      Set<String> recordComponentNames = newLinkedHashSet();
      for (Object recordComponent : recordComponents) {
        Method getName = recordComponent.getClass().getMethod("getName");
        recordComponentNames.add((String) getName.invoke(recordComponent));
      }
      return recordComponentNames;
    } catch (ReflectiveOperationException e) {
      throw new IllegalStateException(e);
    }
  }

  /**
   * Verifies that the actual {@code Class} is final (has {@code final} modifier).
   * <p>
   * Example:
   * <pre><code class='java'> // these assertions succeed:
   * assertThat(String.class).isFinal();
   * assertThat(Math.class).isFinal();
   *
   * // these assertions fail:
   * assertThat(Object.class).isFinal();
   * assertThat(Throwable.class).isFinal();</code></pre>
   *
   * @return {@code this} assertions object
   * @throws AssertionError if {@code actual} is {@code null}.
   * @throws AssertionError if the actual {@code Class} is not final.
   */
  public SELF isFinal() {
    isNotNull();
    assertIsFinal();
    return myself;
  }

  private void assertIsFinal() {
    if (!Modifier.isFinal(actual.getModifiers())) throw assertionError(shouldBeFinal(actual));
  }

  /**
   * Verifies that the actual {@code Class} is not final (does not have {@code final} modifier).
   * <p>
   * Example:
   * <pre><code class='java'> // these assertions succeed:
   * assertThat(Object.class).isNotFinal();
   * assertThat(Throwable.class).isNotFinal();
   *
   * // these assertions fail:
   * assertThat(String.class).isNotFinal();
   * assertThat(Math.class).isNotFinal();</code></pre>
   *
   * @return {@code this} assertions object
   * @throws AssertionError if {@code actual} is {@code null}.
   * @throws AssertionError if the actual {@code Class} is final.
   */
  public SELF isNotFinal() {
    isNotNull();
    assertIsNotFinal();
    return myself;
  }

  private void assertIsNotFinal() {
    if (Modifier.isFinal(actual.getModifiers())) throw assertionError(shouldNotBeFinal(actual));
  }

  /**
   * Verifies that the actual {@code Class} is public (has {@code public} modifier).
   * <p>
   * Example:
   * <pre><code class='java'> protected class MyClass { }
   *
   * // these assertions succeed:
   * assertThat(String.class).isPublic();
   * assertThat(Math.class).isPublic();
   *
   * // This assertion fails:
   * assertThat(MyClass.class).isPublic();</code></pre>
   *
   * @return {@code this} assertions object
   * @throws AssertionError if {@code actual} is {@code null}.
   * @throws AssertionError if the actual {@code Class} is not public.
   *
   * @since 2.7.0 / 3.7.0
   */
  public SELF isPublic() {
    isNotNull();
    assertIsPublic();
    return myself;
  }

  private void assertIsPublic() {
    if (!Modifier.isPublic(actual.getModifiers())) throw assertionError(shouldBePublic(actual));
  }

  /**
   * Verifies that the actual {@code Class} is protected (has {@code protected} modifier).
   * <p>
   * Example:
   * <pre><code class='java'> public class MyClass { }
   *
   * // this assertion succeeds:
   * assertThat(MyClass.class).isProtected();
   *
   * // these assertions fail:
   * assertThat(String.class).isProtected();
   * assertThat(Math.class).isProtected();</code></pre>
   *
   * @return {@code this} assertions object
   * @throws AssertionError if {@code actual} is {@code null}.
   * @throws AssertionError if the actual {@code Class} is not protected.
   *
   * @since 2.7.0 / 3.7.0
   */
  public SELF isProtected() {
    isNotNull();
    assertIsProtected();
    return myself;
  }

  private void assertIsProtected() {
    if (!Modifier.isProtected(actual.getModifiers())) throw assertionError(shouldBeProtected(actual));
  }

  /**
   * Verifies that the actual {@code Class} is package-private (i.e., has no explicit access level modifier).
   * <p>
   * Example:
   * <pre><code class='java'> class MyClass { }
   *
   * // this assertion succeeds:
   * assertThat(MyClass.class).isPackagePrivate();
   *
   * // these assertions fail:
   * assertThat(String.class).isPackagePrivate();
   * assertThat(Math.class).isPackagePrivate();</code></pre>
   *
   * @return {@code this} assertions object
   * @throws AssertionError if {@code actual} is {@code null}.
   * @throws AssertionError if the actual {@code Class} is not package-private.
   *
   * @since 3.15.0
   */
  public SELF isPackagePrivate() {
    isNotNull();
    assertIsPackagePrivate();
    return myself;
  }

  private void assertIsPackagePrivate() {
    final int modifiers = actual.getModifiers();
    if (Modifier.isPublic(modifiers) || Modifier.isProtected(modifiers) || Modifier.isPrivate(modifiers)) {
      throw assertionError(shouldBePackagePrivate(actual));
    }
  }

  /**
   * Verifies that the actual {@code Class} is private (has {@code private} modifier).
   * <p>
   * Example:
   * <pre><code class='java'> class EnclosingClass {
   *   private static class PrivateClass { }
   * }
   *
   * // these assertions succeed:
   * assertThat(PrivateClass.class).isPrivate();
   * assertThat(Class.forName(EnclosingClass.class.getName() + "$PrivateClass")).isPrivate();
   *
   * // This assertion fails:
   * assertThat(String.class).isPrivate();</code></pre>
   *
   * @return {@code this} assertions object
   * @throws AssertionError if {@code actual} is {@code null}.
   * @throws AssertionError if the actual {@code Class} is not private.
   *
   * @since 3.26.0
   */
  public SELF isPrivate() {
    isNotNull();
    assertIsPrivate();
    return myself;
  }

  private void assertIsPrivate() {
    if (!Modifier.isPrivate(actual.getModifiers())) throw assertionError(shouldBePrivate(actual));
  }

  /**
   * Verifies that the actual {@code Class} is static (has {@code static} modifier).
   * <p>
   * Example:
   * <pre><code class='java'> class OuterClass {
   *    static class StaticNestedClass { }
   * }
   * // this assertion succeeds:
   * assertThat(OuterClass.StaticNestedClass.class).isStatic();
   *
   * // these assertions fail:
   * assertThat(Object.class).isStatic();
   * assertThat(Throwable.class).isStatic();</code></pre>
   *
   * @return {@code this} assertions object
   * @throws AssertionError if {@code actual} is {@code null}.
   * @throws AssertionError if the actual {@code Class} is not static.
   * @since 3.23.0
   */
  public SELF isStatic() {
    isNotNull();
    assertIsStatic();
    return myself;
  }

  private void assertIsStatic() {
    if (!Modifier.isStatic(actual.getModifiers())) throw assertionError(shouldBeStatic(actual));
  }

  /**
   * Verifies that the actual {@code Class} is not static (does not have {@code static} modifier).
   * <p>
   * Example:
   * <pre><code class='java'> // these assertions succeed:
   * assertThat(Object.class).isNotStatic();
   * assertThat(Throwable.class).isNotStatic();
   *
   * class OuterClass {
   *    static class StaticNestedClass { }
   * }
   * // this assertion fails:
   * assertThat(OuterClass.StaticNestedClass.class).isNotStatic();</code></pre>
   *
   * @return {@code this} assertions object
   * @throws AssertionError if {@code actual} is {@code null}.
   * @throws AssertionError if the actual {@code Class} is static.
   * @since 3.23.0
   */
  public SELF isNotStatic() {
    isNotNull();
    assertIsNotStatic();
    return myself;
  }

  private void assertIsNotStatic() {
    if (Modifier.isStatic(actual.getModifiers())) throw assertionError(shouldNotBeStatic(actual));
  }

  /**
   * Verifies that the actual {@code Class} has the given {@code Annotation}s.
   * <p>
   * Example:
   * <pre><code class='java'> &#64;Target(ElementType.TYPE)
   * &#64;Retention(RetentionPolicy.RUNTIME)
   * private static @interface Force { }
   *
   * &#64;Target(ElementType.TYPE)
   * &#64;Retention(RetentionPolicy.RUNTIME)
   * private static @interface Hero { }
   *
   * &#64;Target(ElementType.TYPE)
   * &#64;Retention(RetentionPolicy.RUNTIME)
   * private static @interface DarkSide { }
   *
   * &#64;Hero &#64;Force
   * class Jedi implements Jedi {}
   *
   * // this assertion succeeds:
   * assertThat(Jedi.class).containsAnnotations(Force.class, Hero.class);
   *
   * // this assertion fails:
   * assertThat(Jedi.class).containsAnnotations(Force.class, DarkSide.class);</code></pre>
   *
   * @param annotations annotations who must be attached to the class
   * @return {@code this} assertions object
   * @throws AssertionError if {@code actual} is {@code null}.
   * @throws AssertionError if the actual {@code Class} doesn't contains all of these annotations.
   */
  @SafeVarargs
  public final SELF hasAnnotations(Class<? extends Annotation>... annotations) {
    return hasAnnotationsForProxy(annotations);
  }

  // This method is protected in order to be proxied for SoftAssertions / Assumptions.
  // The public method for it (the one not ending with "ForProxy") is marked as final and annotated with @SafeVarargs
  // in order to avoid compiler warning in user code
  protected SELF hasAnnotationsForProxy(Class<? extends Annotation>[] annotations) {
    classes.assertContainsAnnotations(info, actual, annotations);
    return myself;
  }

  /**
   * Verifies that the actual {@code Class} has the given {@code Annotation}.
   * <p>
   * Example:
   * <pre><code class='java'> &#64;Target(ElementType.TYPE)
   * &#64;Retention(RetentionPolicy.RUNTIME)
   * private static @interface Force { }
   * &#64;Force
   * class Jedi implements Jedi {}
   *
   * // this assertion succeeds:
   * assertThat(Jedi.class).containsAnnotation(Force.class);
   *
   * // this assertion fails:
   * assertThat(Jedi.class).containsAnnotation(DarkSide.class);</code></pre>
   *
   * @param annotation annotations who must be attached to the class
   * @return {@code this} assertions object
   * @throws AssertionError if {@code actual} is {@code null}.
   * @throws AssertionError if the actual {@code Class} doesn't contains all of these annotations.
   */
  public SELF hasAnnotation(Class<? extends Annotation> annotation) {
    classes.assertContainsAnnotations(info, actual, array(annotation));
    return myself;
  }

  /**
   * Verifies that the actual {@code Class} has the given class as direct superclass (as in {@link Class#getSuperclass()}).
   * <p>
   * The expected {@code superclass} should always be not {@code null}. To verify the absence of the superclass, use
   * {@link #hasNoSuperclass()}.
   * <p>
   * Example:
   * <pre><code class='java'> // this assertion succeeds:
   * assertThat(Integer.class).hasSuperclass(Number.class);
   *
   * // this assertion succeeds as superclass for array classes is Object:
   * assertThat(Integer[].class).hasSuperclass(Object.class);
   *
   * // this assertion fails:
   * assertThat(String.class).hasSuperclass(Number.class);
   *
   * // this assertion fails as only direct superclass matches:
   * assertThat(String.class).hasSuperclass(Object.class);
   *
   * // this assertion fails as interfaces are not superclasses:
   * assertThat(String.class).hasSuperclass(Comparable.class);</code></pre>
   *
   * @param superclass the class which must be the direct superclass of actual.
   * @return {@code this} assertions object
   * @throws AssertionError if {@code actual} is {@code null}.
   * @throws AssertionError if the actual {@code Class} doesn't have the given class as direct superclass.
   * @throws NullPointerException if {@code superclass} is {@code null}.
   * @see #hasNoSuperclass()
   * @since 3.15.0
   */
  public SELF hasSuperclass(Class<?> superclass) {
    isNotNull();
    assertHasSuperclass(superclass);
    return myself;
  }

  private void assertHasSuperclass(Class<?> superclass) {
    requireNonNull(superclass, shouldNotBeNull("superclass")::create);
    Class<?> actualSuperclass = actual.getSuperclass();
    if (actualSuperclass == null || !actualSuperclass.equals(superclass)) {
      throw assertionError(shouldHaveSuperclass(actual, superclass));
    }
  }

  /**
   * Verifies that the actual {@code Class} has no superclass (as in {@link Class#getSuperclass()}, when {@code null}
   * is returned).
   * <p>
   * Example:
   * <pre><code class='java'> // this assertion succeeds as Object has no superclass:
   * assertThat(Object.class).hasNoSuperclass();
   *
   * // this assertion succeeds as interfaces have no superclass:
   * assertThat(Cloneable.class).hasNoSuperclass();
   *
   * // this assertion succeeds as primitive types have no superclass:
   * assertThat(Integer.TYPE).hasNoSuperclass();
   *
   * // this assertion succeeds as void type has no superclass:
   * assertThat(Void.TYPE).hasNoSuperclass();
   *
   * // this assertion fails as Integer has Number as superclass:
   * assertThat(Integer.class).hasNoSuperclass();</code></pre>
   *
   * @return {@code this} assertions object
   * @throws AssertionError if {@code actual} is {@code null}.
   * @throws AssertionError if the actual {@code Class} has a superclass.
   * @see #hasSuperclass(Class)
   * @since 3.15.0
   */
  public SELF hasNoSuperclass() {
    isNotNull();
    assertHasNoSuperclass();
    return myself;
  }

  private void assertHasNoSuperclass() {
    if (actual.getSuperclass() != null) throw assertionError(shouldHaveNoSuperclass(actual));
  }

  /**
   * @param fields the fields who must be in the class.
   * @return {@code this} assertions object
   * @deprecated use {@link #hasPublicFields(String...)} instead.
   */
  @Deprecated
  public SELF hasFields(String... fields) {
    return hasPublicFields(fields);
  }

  /**
   * Verifies that the actual {@code Class} has the given accessible public fields (as in {@link Class#getFields()}).
   * <p>
   * Example:
   * <pre><code class='java'> class MyClass {
   *     public String fieldOne;
   *     protected String fieldTwo;
   *     String fieldThree;
   *     private String fieldFour;
   * }
   *
   * // this assertion succeeds:
   * assertThat(MyClass.class).hasPublicFields("fieldOne");
   *
   * // these assertions fail:
   * assertThat(MyClass.class).hasPublicFields("fieldTwo");
   * assertThat(MyClass.class).hasPublicFields("fieldThree");
   * assertThat(MyClass.class).hasPublicFields("fieldFour");
   * assertThat(MyClass.class).hasPublicFields("unknownField");</code></pre>
   * <p>
   * The assertion succeeds if no given fields are passed and the actual {@code Class} has no accessible public fields.
   *
   * @param fields the fields who must be in the class.
   * @return {@code this} assertions object
   * @throws AssertionError if {@code actual} is {@code null}.
   * @throws AssertionError if the actual {@code Class} doesn't contain all of the fields.
   *
   * @see Class#getField(String)
   */
  public SELF hasPublicFields(String... fields) {
    classes.assertHasPublicFields(info, actual, fields);
    return myself;
  }

  /**
   * Verifies that the actual {@code Class} <b>only</b> has the given accessible public
   * fields (as in {@link Class#getFields()}) and nothing more <b>in any order</b>.
   * <p>
   * Example:
   * <pre><code class='java'> class MyClass {
   *     public String fieldOne;
   *     public String fieldTwo;
   *     private String fieldThree;
   * }
   *
   * // these assertions succeed:
   * assertThat(MyClass.class).hasOnlyPublicFields("fieldOne", "fieldTwo");
   * assertThat(MyClass.class).hasOnlyPublicFields("fieldTwo", "fieldOne");
   *
   * // this assertion fails:
   * assertThat(MyClass.class).hasOnlyPublicFields("fieldOne");</code></pre>
   * <p>
   * The assertion succeeds if no given fields are passed and the actual {@code Class} has no accessible public fields.
   *
   * @param fields all the fields that are expected to be in the class.
   * @return {@code this} assertions object
   * @throws AssertionError if {@code actual} is {@code null}.
   * @throws AssertionError if fields are not all the actual {@code Class}'s accessible public fields.
   *
   * @see Class#getField(String)
   * @since 2.7.0 / 3.7.0
   */
  public SELF hasOnlyPublicFields(String... fields) {
    classes.assertHasOnlyPublicFields(info, actual, fields);
    return myself;
  }

  /**
   * Verifies that the actual {@code Class} has the given declared fields (as in {@link Class#getDeclaredFields()}).
   * <p>
   * Example:
   * <pre><code class='java'> class MyClass {
   *     public String fieldOne;
   *     private String fieldTwo;
   * }
   *
   * // this assertion succeeds:
   * assertThat(MyClass.class).hasDeclaredFields("fieldOne", "fieldTwo");
   *
   * // this assertion fails:
   * assertThat(MyClass.class).hasDeclaredFields("fieldThree");</code></pre>
   * <p>
   * The assertion succeeds if no given fields are passed and the actual {@code Class} has no declared fields.
   *
   * @param fields the fields who must be declared in the class.
   * @return {@code this} assertions object
   * @throws AssertionError if {@code actual} is {@code null}.
   * @throws AssertionError if the actual {@code Class} doesn't contains all of the field.
   *
   * @see Class#getDeclaredField(String)
   */
  public SELF hasDeclaredFields(String... fields) {
    classes.assertHasDeclaredFields(info, actual, fields);
    return myself;
  }

  /**
   * Verifies that the actual {@code Class} <b>only</b> has the given declared {@code fields} and nothing more <b>in any order</b>
   * (as in {@link Class#getDeclaredFields()}).
   * <p>
   * Example:
   * <pre><code class='java'> class MyClass {
   *     public String fieldOne;
   *     public String fieldTwo;
   *     private String fieldThree;
   *     private String fieldFour;
   * }
   *
   * // this assertion succeeds:
   * assertThat(MyClass.class).hasOnlyDeclaredFields("fieldOne", "fieldTwo", "fieldThree", "fieldFour");
   *
   * // this assertion fails:
   * assertThat(MyClass.class).hasOnlyDeclaredFields("fieldOne", "fieldThree");</code></pre>
   * <p>
   * The assertion succeeds if no given fields are passed and the actual {@code Class} has no declared fields.
   *
   * @param fields all the fields that are expected to be in the class.
   * @return {@code this} assertions object
   * @throws AssertionError if {@code actual} is {@code null}.
   * @throws AssertionError if fields are not all the declared fields of the actual {@code Class}.
   *
   * @see Class#getField(String)
   * @since 2.7.0 / 3.7.0
   */
  public SELF hasOnlyDeclaredFields(String... fields) {
    classes.assertHasOnlyDeclaredFields(info, actual, fields);
    return myself;
  }

  /**
   * Verifies that the actual {@code Class} has the given methods (including inherited) whatever their visibility are.
   * <p>
   * Example:
   * <pre><code class='java'> class MySuperClass {
   *     public void superMethod() {}
   *     private void privateSuperMethod() {}
   * }
   *
   * class MyClass extends MySuperClass {
   *     public void methodOne() {}
   *     private void methodTwo() {}
   * }
   *
   * // this assertion succeeds:
   * assertThat(MyClass.class).hasMethods("methodOne", "methodTwo", "superMethod", "privateSuperMethod");
   *
   * // this assertion fails:
   * assertThat(MyClass.class).hasMethods("methodThree");</code></pre>
   *
   * @param methodNames the method names which must be in the class.
   * @return {@code this} assertions object
   * @throws AssertionError if {@code actual} is {@code null}.
   * @throws AssertionError if the actual {@code Class} doesn't contains all of the method names.
   *
   * @since 2.7.0 / 3.7.0
   */
  public SELF hasMethods(String... methodNames) {
    classes.assertHasMethods(info, actual, methodNames);
    return myself;
  }

  /**
   * Verifies that the actual {@code Class} has the given declared methods.
   * <p>
   * Example:
   * <pre><code class='java'> class MySuperClass {
   *     public void superMethod() {}
   * }
   *
   * class MyClass extends MySuperClass {
   *     public void methodOne() {}
   *     private void methodTwo() {}
   * }
   *
   * // This assertion succeeds:
   * assertThat(MyClass.class).hasDeclaredMethods("methodOne", "methodTwo");
   *
   * // these assertions fail:
   * assertThat(MyClass.class).hasDeclaredMethods("superMethod");
   * assertThat(MyClass.class).hasDeclaredMethods("methodThree");</code></pre>
   * <p>
   * The assertion succeeds if no given methods are passed and the actual {@code Class} has no declared methods.
   *
   * @param methodNames the method names which must be declared in the class.
   * @return {@code this} assertions object
   * @throws AssertionError if {@code actual} is {@code null}.
   * @throws AssertionError if the actual {@code Class} doesn't contains all of the given methods.
   *
   * @since 2.7.0 / 3.7.0
   */
  public SELF hasDeclaredMethods(String... methodNames) {
    classes.assertHasDeclaredMethods(info, actual, methodNames);
    return myself;
  }

  /**
   * Verifies that the actual {@code Class} has the given public methods.
   * <p>
   * Example:
   * <pre><code class='java'> class MyClass {
   *     public void methodOne() {}
   *     public void methodTwo() {}
   *     protected void methodThree() {}
   * }
   *
   * // these assertions succeed:
   * assertThat(MyClass.class).hasPublicMethods("methodOne");
   * assertThat(MyClass.class).hasPublicMethods("methodOne", "methodTwo");
   *
   * // these assertions fail:
   * assertThat(MyClass.class).hasPublicMethods("methodOne", "methodThree");
   * assertThat(MyClass.class).hasPublicMethods("methodThree");</code></pre>
   *
   * @param methodNames the public method names which must be in the class.
   * @return {@code this} assertions object
   * @throws AssertionError if {@code actual} is {@code null}.
   * @throws AssertionError if the actual {@code Class} doesn't contains all of the given public methods.
   *
   * @since 2.7.0 / 3.7.0
   */
  public SELF hasPublicMethods(String... methodNames) {
    classes.assertHasPublicMethods(info, actual, methodNames);
    return myself;
  }

  /**
   * Verifies that the actual {@code Class} has the given package name (as in {@link Class#getPackage()}).
   * <p>
   * The expected package name should always be not {@code null}. To verify the absence of the package, use
   * {@link #hasNoPackage()}. 
   * <p>
   * Example:
   * <pre><code class='java'> package one.two;
   *
   * class MyClass {}
   *
   * // this assertions succeeds:
   * assertThat(MyClass.class).hasPackage("one.two");
   *
   * // these assertions fail:
   * assertThat(MyClass.class).hasPackage("one");
   * assertThat(MyClass.class).hasPackage("");
   * assertThat(MyClass.class).hasPackage("java.lang");</code></pre>
   *
   * @param expected the package name the class should have
   * @return {@code this} assertions object
   * @throws AssertionError if {@code actual} is {@code null}.
   * @throws AssertionError if the actual {@code Class} does not have the given package.
   *
   * @since 3.18.0
   */
  public SELF hasPackage(String expected) {
    isNotNull();
    assertHasPackage(expected);
    return myself;
  }

  private void assertHasPackage(String packageName) {
    requireNonNull(packageName, shouldNotBeNull("expected")::create);
    Package actualPackage = actual.getPackage();
    if (actualPackage == null || !actualPackage.getName().equals(packageName)) {
      throw assertionError(shouldHavePackage(actual, packageName));
    }
  }

  /**
   * Verifies that the actual {@code Class} has the given package (as in {@link Class#getPackage()}).
   * <p>
   * The expected package should always be not {@code null}. To verify the absence of the package, use
   * {@link #hasNoPackage()}. 
   * <p>
   * Example:
   * <pre><code class='java'> package one.two;
   *
   * class MyClass {}
   **
   * // these assertions succeed:
   * assertThat(MyClass.class).hasPackage(Package.getPackage("one.two"));
   * assertThat(MyClass.class).hasPackage(MyClass.class.getPackage());
   *
   * // these assertions fail:
   * assertThat(MyClass.class).hasPackage(Package.getPackage("one"));
   * assertThat(MyClass.class).hasPackage(Package.getPackage(""));
   * assertThat(MyClass.class).hasPackage(Object.class.getPackage());</code></pre>
   *
   * @param expected the package the class should have
   * @return {@code this} assertions object
   * @throws AssertionError if {@code actual} is {@code null}.
   * @throws AssertionError if the actual {@code Class} does not have the given package.
   *
   * @see #hasPackage(String)
   * @see #hasNoPackage()
   * @since 3.18.0
   */
  public SELF hasPackage(Package expected) {
    isNotNull();
    assertHasPackage(expected);
    return myself;
  }

  private void assertHasPackage(Package expected) {
    requireNonNull(expected, shouldNotBeNull("expected")::create);
    if (!expected.equals(actual.getPackage())) throw assertionError(shouldHavePackage(actual, expected));
  }

  /**
   * Verifies that the actual {@code Class} has no package (as in {@link Class#getPackage()}, when {@code null}
   * is returned).
   * <p>
   * Example:
   * <pre><code class='java'> // this assertion succeeds as arrays have no package:
   * assertThat(int[].class).hasNoPackage();
   *
   * // this assertion succeeds as primitive types have no package:
   * assertThat(Integer.TYPE).hasNoPackage();
   *
   * // this assertion succeeds as void type has no package:
   * assertThat(Void.TYPE).hasNoPackage();
   *
   * // this assertion fails as Object has java.lang as package:
   * assertThat(Object.class).hasNoPackage();</code></pre>
   *
   * @return {@code this} assertions object
   * @throws AssertionError if {@code actual} is {@code null}.
   * @throws AssertionError if the actual {@code Class} has a package.
   *
   * @see #hasPackage(Package)
   * @see #hasPackage(String)
   * @since 3.25.0
   */
  public SELF hasNoPackage() {
    isNotNull();
    assertHasNoPackage();
    return myself;
  }

  private void assertHasNoPackage() {
    if (actual.getPackage() != null) throw assertionError(shouldHaveNoPackage(actual));
  }

  /**
   * Verifies that the actual {@code Class} is sealed.
   * <p>
   * Example:
   * <pre><code class='java'> sealed class SealedClass permits NonSealedClass {}
   *
   * non-sealed class NonSealedClass extends SealedClass {}
   *
   * // this assertion succeeds:
   * assertThat(SealedClass.class).isSealed();
   *
   * // this assertion fails:
   * assertThat(NonSealedClass.class).isSealed();</code></pre>
   *
   * @return {@code this} assertions object
   * @throws AssertionError if {@code actual} is {@code null}.
   * @throws AssertionError if the actual {@code Class} is not sealed.
   *
   * @since 3.25.0
   */
  public SELF isSealed() {
    isNotNull();
    assertIsSealed();
    return myself;
  }

  private void assertIsSealed() {
    if (!isSealed(actual)) throw assertionError(shouldBeSealed(actual));
  }

  /**
   * Verifies that the actual {@code Class} is not sealed.
   * <p>
   * Example:
   * <pre><code class='java'> sealed class SealedClass permits NonSealedClass {}
   *
   * non-sealed class NonSealedClass extends SealedClass {}
   *
   * // this assertion succeeds:
   * assertThat(NonSealedClass.class).isNotSealed();
   *
   * // this assertion fails:
   * assertThat(SealedClass.class).isNotSealed();</code></pre>
   *
   * @return {@code this} assertions object
   * @throws AssertionError if {@code actual} is {@code null}.
   * @throws AssertionError if the actual {@code Class} is sealed.
   *
   * @since 3.25.0
   */
  public SELF isNotSealed() {
    isNotNull();
    assertIsNotSealed();
    return myself;
  }

  private void assertIsNotSealed() {
    if (isSealed(actual)) throw assertionError(shouldNotBeSealed(actual));
  }

  // TODO https://github.com/assertj/assertj/issues/3081
  private static boolean isSealed(Class<?> actual) {
    try {
      Method isSealed = Class.class.getMethod("isSealed");
      return (boolean) isSealed.invoke(actual);
    } catch (NoSuchMethodException e) {
      return false;
    } catch (ReflectiveOperationException e) {
      throw new IllegalStateException(e);
    }
  }

  /**
   * Verifies that the actual {@code Class} permitted subclasses contains the given classes.
   * <p>
   * Example:
   * <pre><code class='java'> sealed class SealedClass permits NonSealedClass, FinalClass {}
   * non-sealed class NonSealedClass extends SealedClass {}
   * final class FinalClass extends SealedClass {}
   *
   * // these assertions succeed:
   * assertThat(SealedClass.class).hasPermittedSubclasses(NonSealedClass.class)
   *                              .hasPermittedSubclasses(FinalClass.class)
   *                              .hasPermittedSubclasses(NonSealedClass.class, FinalClass.class)
   *                              .hasPermittedSubclasses();
   *
   * // these assertions fail:
   * assertThat(SealedClass.class).hasPermittedSubclasses(String.class);
   * assertThat(SealedClass.class).hasPermittedSubclasses(FinalClass.class, String.class);</code></pre>
   *
   * @param permittedSubclasses classes that must be permitted subclasses of the given class
   * @return {@code this} assertions object
   * @throws AssertionError if {@code actual} is {@code null}.
   * @throws AssertionError if the actual {@code Class} does not have all of given permitted subclasses
   */
  public SELF hasPermittedSubclasses(Class<?>... permittedSubclasses) {
    isNotNull();
    assertHasPermittedSubclasses(permittedSubclasses);
    return myself;
  }

  private void assertHasPermittedSubclasses(Class<?>[] expectedPermittedSubclasses) {
    for (Class<?> expectedPermittedSubclass : expectedPermittedSubclasses) {
      classes.classParameterIsNotNull(expectedPermittedSubclass);
    }
    Set<Class<?>> actualPermittedSubclasses = newLinkedHashSet(getPermittedSubclasses(actual));
    Set<Class<?>> missingPermittedSubclasses = Stream.of(expectedPermittedSubclasses)
                                                     .filter(expectedPermittedSubclass -> !actualPermittedSubclasses.contains(expectedPermittedSubclass))
                                                     .collect(Collectors.toSet());
    if (!missingPermittedSubclasses.isEmpty())
      throw assertionError(shouldHavePermittedSubclasses(actual, expectedPermittedSubclasses, missingPermittedSubclasses));
  }

  private static Class<?>[] getPermittedSubclasses(Class<?> actual) {
    try {
      Method getPermittedSubclasses = Class.class.getMethod("getPermittedSubclasses");
      Class<?>[] permittedSubclasses = (Class<?>[]) getPermittedSubclasses.invoke(actual);
      return permittedSubclasses == null ? array() : permittedSubclasses;
    } catch (NoSuchMethodException e) {
      return new Class<?>[0];
    } catch (ReflectiveOperationException e) {
      throw new IllegalStateException(e);
    }
  }

  /**
   * Verifies that the actual {@code Class} is a primitive type.
   * <p>
   * Example:
   * <pre><code class='java'> // these assertions succeed:
   * assertThat(byte.class).isPrimitive();
   * assertThat(short.class).isPrimitive();
   * assertThat(int.class).isPrimitive();
   * assertThat(long.class).isPrimitive();
   * assertThat(float.class).isPrimitive();
   * assertThat(double.class).isPrimitive();
   * assertThat(boolean.class).isPrimitive();
   * assertThat(char.class).isPrimitive();
   *
   * // this assertion fails as Object is not a primitive type:
   * assertThat(Object.class).isPrimitive(); </code></pre>
   *
   * @return {@code this} assertions object
   * @throws AssertionError if {@code actual} is {@code null}.
   * @throws AssertionError if the actual {@code Class} is not a primitive type.
   * @see Class#isPrimitive()
   */
  public SELF isPrimitive() {
    isNotNull();
    if (!actual.isPrimitive()) throw assertionError(shouldBePrimitive(actual));
    return myself;
  }

  /**
   * Verifies that the actual {@code Class} is not a primitive type.
   * <p>
   * Example:
   * <pre><code class='java'> // this assertion succeeds as Object is not a primitive type:
   * assertThat(Object.class).isNotPrimitive();
   *
   * // these assertions fail:
   * assertThat(byte.class).isNotPrimitive();
   * assertThat(short.class).isNotPrimitive();
   * assertThat(int.class).isNotPrimitive();
   * assertThat(long.class).isNotPrimitive();
   * assertThat(float.class).isNotPrimitive();
   * assertThat(double.class).isNotPrimitive();
   * assertThat(boolean.class).isNotPrimitive();
   * assertThat(char.class).isNotPrimitive(); </code></pre>
   *
   * @return {@code this} assertions object
   * @throws AssertionError if {@code actual} is {@code null}.
   * @throws AssertionError if the actual {@code Class} is a primitive type.
   * @see Class#isPrimitive()
   */
  public SELF isNotPrimitive() {
    isNotNull();
    if (actual.isPrimitive()) throw assertionError(shouldNotBePrimitive(actual));
    return myself;
  }
}
