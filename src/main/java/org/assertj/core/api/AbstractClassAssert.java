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
public abstract class AbstractClassAssert<SELF extends AbstractClassAssert<SELF>> extends AbstractAssert<SELF, Class<?>> {

  Classes classes = Classes.instance();

  public AbstractClassAssert(Class<?> actual, Class<?> selfType) {
    super(actual, selfType);
  }

  /**
   * Verifies that the actual {@code Class} is assignable from others {@code Class}
   * 
   * <pre><code class='java'> class Jedi {}
   * class HumanJedi extends Jedi {}
   * 
   * // Should pass if :
   * assertThat(Jedi.class).isAssignableFrom(HumanJedi.class);
   * 
   * // Should fail if :
   * assertThat(HumanJedi.class).isAssignableFrom(Jedi.class);</code></pre>
   * 
   * @see Class#isAssignableFrom(Class)
   * @param others {@code Class} who can be assignable from.
   * @return this assertion object.
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
   * 
   * <pre><code class='java'> interface Jedi {}
   * class HumanJedi implements Jedi {}
   * 
   * // Should pass if :
   * assertThat(HumanJedi.class).isNotInterface();
   * 
   * // Should fail if :
   * assertThat(Jedi.class).isNotInterface();</code></pre>
   * 
   * @throws AssertionError if {@code actual} is {@code null}.
   * @throws AssertionError if the actual {@code Class} is not an interface.
   */
  public SELF isNotInterface() {
    classes.assertIsNotInterface(info, actual);
    return myself;
  }

  /**
   * Verifies that the actual {@code Class} is an interface.
   * 
   * <pre><code class='java'> interface Jedi {}
   * class HumanJedi implements Jedi {}
   * 
   * // Should pass if :
   * assertThat(Jedi.class).isInterface();
   * 
   * // Should fail if :
   * assertThat(HumanJedi.class).isInterface();</code></pre>
   * 
   * @throws AssertionError if {@code actual} is {@code null}.
   * @throws AssertionError if the actual {@code Class} is not an interface.
   */
  public SELF isInterface() {
    classes.assertIsInterface(info, actual);
    return myself;
  }

  /**
   * Verifies that the actual {@code Class} is an annotation.
   * 
   * <pre><code class='java'> public @interface Jedi {}
   * 
   * // Should pass if :
   * assertThat(Jedi.class).isAnnotation();
   * assertThat(Override.class).isAnnotation();
   * assertThat(Deprecated.class).isAnnotation();
   * 
   * // Should fail if :
   * assertThat(String.class).isAnnotation();</code></pre>
   * 
   * @throws AssertionError if {@code actual} is {@code null}.
   * @throws AssertionError if the actual {@code Class} is not an annotation.
   */
  public SELF isAnnotation() {
    classes.assertIsAnnotation(info, actual);
    return myself;
  }

  /**
   * Verifies that the actual {@code Class} is not an annotation.
   * 
   * <pre><code class='java'> public @interface Jedi {}
   * 
   * // Should pass if :
   * assertThat(String.class).isNotAnnotation();
   * 
   * // Should fail if :
   * assertThat(Jedi.class).isNotAnnotation();
   * assertThat(Override.class).isNotAnnotation();
   * assertThat(Deprecated.class).isNotAnnotation();</code></pre>
   * 
   * @throws AssertionError if {@code actual} is {@code null}.
   * @throws AssertionError if the actual {@code Class} is an annotation.
   */
  public SELF isNotAnnotation() {
    classes.assertIsNotAnnotation(info, actual);
    return myself;
  }

  /**
   * Verifies that the actual {@code Class} is final (has {@code final} modifier).
   *
   * <pre><code class='java'> // These assertions succeed:
   * assertThat(String.class).isFinal();
   * assertThat(Math.class).isFinal();
   *
   * // These assertions fail:
   * assertThat(Object.class).isFinal();
   * assertThat(Throwable.class).isFinal();</code></pre>
   *
   * @throws AssertionError if {@code actual} is {@code null}.
   * @throws AssertionError if the actual {@code Class} is not final.
   */
  public SELF isFinal() {
    classes.assertIsFinal(info, actual);
    return myself;
  }

  /**
   * Verifies that the actual {@code Class} is not final (does not have {@code final} modifier).
   *
   * <pre><code class='java'> // These assertions succeed:
   * assertThat(Object.class).isNotFinal();
   * assertThat(Throwable.class).isNotFinal();
   *
   * // These assertions fail:
   * assertThat(String.class).isNotFinal();
   * assertThat(Math.class).isNotFinal();</code></pre>
   *
   * @throws AssertionError if {@code actual} is {@code null}.
   * @throws AssertionError if the actual {@code Class} is final.
   */
  public SELF isNotFinal() {
    classes.assertIsNotFinal(info, actual);
    return myself;
  }

  /**
   * Verifies that the actual {@code Class} is public (has {@code public} modifier).
   *
   * <pre><code class='java'> protected class MyClass { }
   *
   * // These assertions succeed:
   * assertThat(String.class).isPublic();
   * assertThat(Math.class).isPublic();
   *
   * // This assertion fails:
   * assertThat(MyClass.class).isPublic();</code></pre>
   *
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
   *
   * <pre><code class='java'> public class MyClass { }
   *
   * // This assertion succeeds:
   * assertThat(MyClass.class).isProtected();
   *
   * // These assertions fail:
   * assertThat(String.class).isProtected();
   * assertThat(Math.class).isProtected();</code></pre>
   *
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
   * 
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
   * // Should pass:
   * assertThat(Jedi.class).containsAnnotations(Force.class, Hero.class);
   * 
   * // Should fail:
   * assertThat(Jedi.class).containsAnnotations(Force.class, DarkSide.class);</code></pre>
   * 
   * @param annotations annotations who must be attached to the class
   * @throws AssertionError if {@code actual} is {@code null}.
   * @throws AssertionError if the actual {@code Class} doesn't contains all of these annotations.
   */
  public SELF hasAnnotations(@SuppressWarnings("unchecked") Class<? extends Annotation>... annotations) {
    classes.assertContainsAnnotations(info, actual, annotations);
    return myself;
  }

  /**
   * Verifies that the actual {@code Class} has the given {@code Annotation}.
   * 
   * <pre><code class='java'> &#64;Target(ElementType.TYPE)
   * &#64;Retention(RetentionPolicy.RUNTIME)
   * private static @interface Force { }
   * &#64;Force
   * class Jedi implements Jedi {}
   * 
   * // Should pass if :
   * assertThat(Jedi.class).containsAnnotation(Force.class);
   * 
   * // Should fail if :
   * assertThat(Jedi.class).containsAnnotation(DarkSide.class);</code></pre>
   * 
   * @param annotation annotations who must be attached to the class
   * @throws AssertionError if {@code actual} is {@code null}.
   * @throws AssertionError if the actual {@code Class} doesn't contains all of these annotations.
   */
  @SuppressWarnings("unchecked")
  public SELF hasAnnotation(Class<? extends Annotation> annotation) {
    classes.assertContainsAnnotations(info, actual, annotation);
    return myself;
  }

  /**
   * Verifies that the actual {@code Class} has the {@code fields}.
   * 
   * <pre><code class='java'> class MyClass {
   *     public String fieldOne;
   *     private String fieldTwo;
   * }
   * 
   * // This one should pass :
   * assertThat(MyClass.class).hasFields("fieldOne");
   * 
   * // This one should fail :
   * assertThat(MyClass.class).hasFields("fieldTwo");
   * assertThat(MyClass.class).hasDeclaredFields("fieldThree");</code></pre>
   * 
   * @see Class#getField(String)
   * @param fields the fields who must be in the class.
   * @throws AssertionError if {@code actual} is {@code null}.
   * @throws AssertionError if the actual {@code Class} doesn't contains all of the field.
   */
  public SELF hasFields(String... fields) {
    classes.assertHasFields(info, actual, fields);
    return myself;
  }

  /**
   * Verifies that the actual {@code Class} has the declared {@code fields}.
   * 
   * <pre><code class='java'> class MyClass {
   *     public String fieldOne;
   *     private String fieldTwo;
   * }
   * 
   * // This one should pass :
   * assertThat(MyClass.class).hasDeclaredFields("fieldOne", "fieldTwo");
   * 
   * // This one should fail :
   * assertThat(MyClass.class).hasDeclaredFields("fieldThree");</code></pre>
   * 
   * @see Class#getDeclaredField(String)
   * @param fields the fields who must be declared in the class.
   * @throws AssertionError if {@code actual} is {@code null}.
   * @throws AssertionError if the actual {@code Class} doesn't contains all of the field.
   */
  public SELF hasDeclaredFields(String... fields) {
    classes.assertHasDeclaredFields(info, actual, fields);
    return myself;
  }

  /**
   * Verifies that the actual {@code Class} has the given methods.
   *
   * <pre><code class='java'> class MyClass {
   *     public void methodOne() {}
   *     private void methodTwo() {}
   * }
   *
   * // This assertion succeeds:
   * assertThat(MyClass.class).hasMethods("methodOne");
   *
   * // These assertions fail:
   * assertThat(MyClass.class).hasMethods("methodTwo");
   * assertThat(MyClass.class).hasMethods("methodThree");</code></pre>
   *
   * @param methodNames the method names which must be in the class.
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
   *
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
   * // These assertions fail:
   * assertThat(MyClass.class).hasDeclaredMethods("superMethod");
   * assertThat(MyClass.class).hasDeclaredMethods("methodThree");</code></pre>
   *
   * @param methodNames the method names which must be declared in the class.
   * @throws AssertionError if {@code actual} is {@code null}.
   * @throws AssertionError if the actual {@code Class} doesn't contains all of the method names.
   *
   * @since 2.7.0 / 3.7.0
   */
  public SELF hasDeclaredMethods(String... methodNames) {
    classes.assertHasDeclaredMethods(info, actual, methodNames);
    return myself;
  }

  /**
   * Verifies that the actual {@code Class} has the given public methods.
   *
   * <pre><code class='java'> class MyClass {
   *     public void methodOne() {}
   *     public void methodTwo() {}
   *     protected void methodThree() {}
   * }
   *
   * // These assertions succeed:
   * assertThat(MyClass.class).hasPublicMethods("methodOne");
   * assertThat(MyClass.class).hasPublicMethods("methodOne", "methodTwo");
   *
   * // These assertions fail:
   * assertThat(MyClass.class).hasPublicMethods("methodOne", "methodThree");
   * assertThat(MyClass.class).hasPublicMethods("methodThree");</code></pre>
   *
   * @param methodNames the public method names which must be in the class.
   * @throws AssertionError if {@code actual} is {@code null}.
   * @throws AssertionError if the actual {@code Class} doesn't contains all of the public method names.
   *
   * @since 2.7.0 / 3.7.0
   */
  public SELF hasPublicMethods(String... methodNames) {
    classes.assertHasPublicMethods(info, actual, methodNames);
    return myself;
  }

  /**
   * Verifies that the actual {@code Class} has the given declared public methods.
   *
   * <pre><code class='java'> class MySuperClass {
   *     public void superMethod() {}
   * }
   *
   * class MyClass extends MySuperClass{
   *     public void methodOne() {}
   *     public void methodTwo() {}
   *     protected void methodThree() {}
   * }
   *
   * // These assertions succeed:
   * assertThat(MyClass.class).hasDeclaredPublicMethods("methodOne");
   * assertThat(MyClass.class).hasDeclaredPublicMethods("methodOne", "methodTwo");
   *
   * // These assertions fail:
   * assertThat(MyClass.class).hasDeclaredPublicMethods("superMethod");
   * assertThat(MyClass.class).hasDeclaredPublicMethods("methodOne", "methodThree");
   * assertThat(MyClass.class).hasDeclaredPublicMethods("methodThree");</code></pre>
   *
   * @param methodNames the public method names which must be declared in the class.
   * @throws AssertionError if {@code actual} is {@code null}.
   * @throws AssertionError if the actual {@code Class} doesn't contains all of the public method names.
   *
   * @since 2.7.0 / 3.7.0
   */
  public SELF hasDeclaredPublicMethods(String... methodNames) {
    classes.assertHasDeclaredPublicMethods(info, actual, methodNames);
    return myself;
  }
}
