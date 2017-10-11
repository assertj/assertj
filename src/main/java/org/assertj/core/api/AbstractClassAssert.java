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

import java.lang.annotation.Annotation;

import org.assertj.core.internal.Classes;

/**
 * Base class for all implementations of assertions for {@link Class}es.
 * 
 * @param <SELF> the "self" type of this assertion class. Please read &quot;<a href="http://bit.ly/1IZIRcY"
 *          target="_blank">Emulating 'self types' using Java Generics to simplify fluent API implementation</a>&quot;
 *          for more details.
 * 
 * @author William Delanoue
 * @author Mikhail Mazursky
 */
public abstract class AbstractClassAssert<SELF extends AbstractClassAssert<SELF>>
    extends AbstractAssert<SELF, Class<?>> {

  Classes classes = Classes.instance();

  public AbstractClassAssert(Class<?> actual, Class<?> selfType) {
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
   * @see Class#isAssignableFrom(Class)
   * @param others {@code Class} who can be assignable from.
   * @return {@code this} assertions object
   * @throws AssertionError if the actual {@code Class} is {@code null}.
   * @throws IllegalArgumentException if no {@code others} classes have been specified.
   * @throws AssertionError if the actual {@code Class} is not assignable from all of the {@code others} classes.
   */
  public SELF isAssignableFrom(Class<?>... others) {
    classes.assertIsAssignableFrom(info, actual, others);
    return myself;
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
    classes.assertIsNotInterface(info, actual);
    return myself;
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
    classes.assertIsInterface(info, actual);
    return myself;
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
    classes.assertIsAnnotation(info, actual);
    return myself;
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
    classes.assertIsNotAnnotation(info, actual);
    return myself;
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
    classes.assertIsFinal(info, actual);
    return myself;
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
    classes.assertIsNotFinal(info, actual);
    return myself;
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
    classes.assertIsPublic(info, actual);
    return myself;
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
    classes.assertIsProtected(info, actual);
    return myself;
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
  public SELF hasAnnotations(@SuppressWarnings("unchecked") Class<? extends Annotation>... annotations) {
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
  @SuppressWarnings("unchecked")
  public SELF hasAnnotation(Class<? extends Annotation> annotation) {
    classes.assertContainsAnnotations(info, actual, annotation);
    return myself;
  }

  /**
   * @deprecated use {@link #hasPublicFields(String...)} instead.
   * @param fields the fields who must be in the class.
   * @return {@code this} assertions object
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
   * @see Class#getField(String)
   * @param fields the fields who must be in the class.
   * @return {@code this} assertions object
   * @throws AssertionError if {@code actual} is {@code null}.
   * @throws AssertionError if the actual {@code Class} doesn't contain all of the fields.
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
   * @see Class#getField(String)
   * @param fields all the fields that are expected to be in the class.
   * @return {@code this} assertions object
   * @throws AssertionError if {@code actual} is {@code null}.
   * @throws AssertionError if fields are not all the actual {@code Class}'s accessible public fields.
   *
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
   * @see Class#getDeclaredField(String)
   * @param fields the fields who must be declared in the class.
   * @return {@code this} assertions object
   * @throws AssertionError if {@code actual} is {@code null}.
   * @throws AssertionError if the actual {@code Class} doesn't contains all of the field.
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
   * @see Class#getField(String)
   * @param fields all the fields that are expected to be in the class.
   * @return {@code this} assertions object
   * @throws AssertionError if {@code actual} is {@code null}.
   * @throws AssertionError if fields are not all the declared fields of the actual {@code Class}.
   *
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
}
