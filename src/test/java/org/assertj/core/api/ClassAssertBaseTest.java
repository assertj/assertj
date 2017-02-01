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

import static org.mockito.Mockito.mock;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.assertj.core.internal.Classes;
import org.assertj.core.internal.Objects;

/**
 * 
 * Abstract base of all ClassAssert tests.
 * 
 * @author William Delanoue
 * 
 */
public abstract class ClassAssertBaseTest extends BaseTestTemplate<ClassAssert, Class<?>> {

  protected Classes classes;
  protected Objects objects;

  @Target(ElementType.TYPE)
  @Retention(RetentionPolicy.RUNTIME)
  protected static @interface MyAnnotation {
  }

  @Target(ElementType.TYPE)
  @Retention(RetentionPolicy.RUNTIME)
  protected static @interface AnotherAnnotation {
  }
  
  @MyAnnotation @AnotherAnnotation
  protected static class AnnotatedClass {
  }

  @Override
  protected ClassAssert create_assertions() {
    return new ClassAssert(AnnotatedClass.class);
  }

  @Override
  protected void inject_internal_objects() {
    super.inject_internal_objects();
    classes = mock(Classes.class);
    objects = mock(Objects.class);
    assertions = new ClassAssert(AnnotatedClass.class);
    assertions.objects = objects;
    assertions.classes = classes;
  }

  @Override
  protected AssertionInfo getInfo(ClassAssert someAssertions) {
    return someAssertions.info;
  }

  @Override
  protected Class<?> getActual(ClassAssert someAssertions) {
    return someAssertions.actual;
  }

  @Override
  protected Objects getObjects(ClassAssert someAssertions) {
    return someAssertions.objects;
  }

  protected Classes getClasses(ClassAssert someAssertions) {
    return someAssertions.classes;
  }
}
