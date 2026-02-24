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

import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for <code>{@link Assertions#assertThat(Constructor)}</code>.
 * 
 * @author William Bakker
 */
class Assertions_assertThat_with_Constructor_Test {
  static class ConstructorContainer {
    ConstructorContainer() {}
  }

  @Test
  void should_create_Assert() throws NoSuchMethodException {
    Constructor<ConstructorContainer> constructor = Assertions_assertThat_with_Constructor_Test.ConstructorContainer.class.getDeclaredConstructor();
    AbstractConstructorAssert<?, ConstructorContainer> assertions = Assertions.assertThat(constructor);
    assertThat(assertions).isNotNull();
  }

  @Test
  void should_pass_actual() throws NoSuchMethodException {
    Constructor<ConstructorContainer> constructor = Assertions_assertThat_with_Constructor_Test.ConstructorContainer.class.getDeclaredConstructor();
    AbstractConstructorAssert<?, ConstructorContainer> assertions = Assertions.assertThat(constructor);
    assertThat(assertions.actual).isSameAs(constructor);
  }
}
