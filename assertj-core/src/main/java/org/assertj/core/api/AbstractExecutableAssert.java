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

import org.assertj.core.internal.Executables;

import java.lang.annotation.Annotation;
import java.lang.reflect.Executable;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import static org.assertj.core.error.MemberModifierShouldBe.shouldBePackagePrivate;
import static org.assertj.core.error.MemberModifierShouldBe.shouldBeProtected;
import static org.assertj.core.error.MemberModifierShouldBe.shouldBePublic;
import static org.assertj.core.util.Arrays.array;

/**
 * Base class for all implementations of assertions for {@link Method}s.
 *
 * @param <SELF> the "self" type of this assertion class. Please read &quot;<a href="http://bit.ly/1IZIRcY"
 *          target="_blank">Emulating 'self types' using Java Generics to simplify fluent API implementation</a>&quot;
 *          for more details.
 *
 * @author William Bakker
 */
public abstract class AbstractExecutableAssert<SELF extends AbstractExecutableAssert<SELF, ACTUAL>, ACTUAL extends Executable>
    extends AbstractAssert<SELF, ACTUAL> implements ExecutableAssert<SELF, ACTUAL> {

  Executables executables = Executables.instance();

  protected AbstractExecutableAssert(ACTUAL actual, Class<?> selfType) {
    super(actual, selfType);
  }

  /** {@inheritDoc} */
  @Override
  public SELF isPublic() {
    isNotNull();
    assertIsPublic();
    return myself;
  }

  private void assertIsPublic() {
    if (!Modifier.isPublic(actual.getModifiers())) throw assertionError(shouldBePublic(actual));
  }

  /** {@inheritDoc} */
  @Override
  public SELF isProtected() {
    isNotNull();
    assertIsProtected();
    return myself;
  }

  private void assertIsProtected() {
    if (!Modifier.isProtected(actual.getModifiers())) throw assertionError(shouldBeProtected(actual));
  }

  /** {@inheritDoc} */
  @Override
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

  /** {@inheritDoc} */
  @SafeVarargs
  @Override
  public final SELF hasAnnotations(Class<? extends Annotation>... annotations) {
    return hasAnnotationsForProxy(annotations);
  }

  // This method is protected in order to be proxied for SoftAssertions / Assumptions.
  // The public method for it (the one not ending with "ForProxy") is marked as final and annotated with @SafeVarargs
  // in order to avoid compiler warning in user code
  protected SELF hasAnnotationsForProxy(Class<? extends Annotation>[] annotations) {
    executables.assertContainsAnnotations(info, actual, annotations);
    return myself;
  }

  /** {@inheritDoc} */
  @Override
  public SELF hasAnnotation(Class<? extends Annotation> annotation) {
    executables.assertContainsAnnotations(info, actual, array(annotation));
    return myself;
  }
}
