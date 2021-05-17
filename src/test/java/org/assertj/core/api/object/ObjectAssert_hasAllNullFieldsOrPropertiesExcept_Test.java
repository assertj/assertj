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
 * Copyright 2012-2021 the original author or authors.
 */
package org.assertj.core.api.object;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;

import org.assertj.core.api.ObjectAssert;
import org.assertj.core.api.ObjectAssertBaseTest;
import org.assertj.core.test.Jedi;
import org.junit.jupiter.api.Test;

/**
 * Tests for <code>{@link ObjectAssert#hasAllNullFieldsOrPropertiesExcept(String...)}</code>.
 *
 * @author Vladimir Chernikov
 */
class ObjectAssert_hasAllNullFieldsOrPropertiesExcept_Test extends ObjectAssertBaseTest {

  private static final String FIELD_NAME = "color";

  @Override
  protected ObjectAssert<Jedi> invoke_api_method() {
    return assertions.hasAllNullFieldsOrPropertiesExcept(FIELD_NAME);
  }

  @Override
  protected void verify_internal_effects() {
    verify(objects).assertHasAllNullFieldsOrPropertiesExcept(getInfo(assertions), getActual(assertions), FIELD_NAME);
  }
  class Book {
    String title;
    int pages;
  }

  @Test
  void testHasAllNullFieldsOrPropertiesExceptPrimitiveTypes_one() {
    Book book = new Book();
    assertThat(book).hasAllNullFieldsOrPropertiesExceptPrimitiveTypes();
  }
  class Apple {
    String color;
    int size;
  }
  @Test
  void testHasAllNullFieldsOrPropertiesExceptPrimitiveTypes_two() {
    Apple apple = new Apple();
    assertThat(apple).hasAllNullFieldsOrPropertiesExceptPrimitiveTypes();
  }
}
