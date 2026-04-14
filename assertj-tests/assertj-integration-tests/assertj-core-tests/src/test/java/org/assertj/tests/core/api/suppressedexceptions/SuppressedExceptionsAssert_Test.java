/*
 * Copyright 2012-2026 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.assertj.tests.core.api.suppressedexceptions;

import static java.lang.reflect.Modifier.isProtected;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.BDDAssertions.then;

import org.assertj.core.api.AbstractThrowableAssert;
import org.assertj.core.api.SuppressedExceptionsAssert;
import org.junit.jupiter.api.Test;

class SuppressedExceptionsAssert_Test {

  @Test
  void class_should_be_abstract() {
    // WHEN/THEN
    assertThat(SuppressedExceptionsAssert.class).isAbstract();
  }

  @Test
  void constructor_should_be_protected() throws NoSuchMethodException {
    // WHEN
    var constructor = SuppressedExceptionsAssert.class.getDeclaredConstructor(AbstractThrowableAssert.class, Throwable[].class);
    // THEN
    then(isProtected(constructor.getModifiers())).isTrue(); // FIXME gh-1693
  }

  @Test
  void returnToThrowable_should_return_origin() {
    // GIVEN
    AbstractThrowableAssert<?, Throwable> origin = assertThat(new Throwable());
    SuppressedExceptionsAssert<?, Throwable> underTest = origin.suppressedExceptions();
    // WHEN
    AbstractThrowableAssert<?, Throwable> result = underTest.returnToThrowable();
    // THEN
    then(result).isSameAs(origin);
  }

}
