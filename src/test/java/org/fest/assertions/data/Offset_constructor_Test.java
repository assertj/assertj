/*
 * Created on Oct 23, 2010
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 *
 * Copyright @2010 the original author or authors.
 */
package org.fest.assertions.data;

import static junit.framework.Assert.assertSame;
import static org.fest.assertions.test.ExpectedException.none;

import org.fest.assertions.test.ExpectedException;
import org.junit.Rule;
import org.junit.Test;

/**
 * Tests for <code>{@link Offset#Offset(Number)}</code>.
 *
 * @author Alex Ruiz
 */
public class Offset_constructor_Test {

  @Rule public ExpectedException thrown = none();

  @Test public void should_create_new_Offset() {
    Integer value = 6;
    Offset<Integer> offset = new Offset<Integer>(value);
    assertSame(value, offset.value());
  }

  @Test public void should_throw_error_if_value_is_null() {
    thrown.expectNullPointerException("The value of the offset to create should not be null");
    new Offset<Integer>(null);
  }
}
