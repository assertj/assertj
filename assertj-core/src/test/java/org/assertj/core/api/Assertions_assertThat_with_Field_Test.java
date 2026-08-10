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

import java.lang.reflect.Field;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for <code>{@link Assertions#assertThat(Field)}</code>.
 * 
 * @author William Bakker
 */
class Assertions_assertThat_with_Field_Test {
  private Object field = new Object();

  @Test
  void should_create_Assert() throws NoSuchFieldException {
    Field field = Assertions_assertThat_with_Field_Test.class.getDeclaredField("field");
    AbstractFieldAssert<?> assertions = Assertions.assertThat(field);
    assertThat(assertions).isNotNull();
  }

  @Test
  void should_pass_actual() throws NoSuchFieldException {
    Field field = Assertions_assertThat_with_Field_Test.class.getDeclaredField("field");
    AbstractFieldAssert<?> assertions = Assertions.assertThat(field);
    assertThat(assertions.actual).isSameAs(field);
  }
}
