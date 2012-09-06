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
 * Copyright @2010-2012 the original author or authors.
 */
package org.fest.assertions.data;

import static junit.framework.Assert.assertSame;
import static org.fest.assertions.test.ErrorMessages.offsetValueIsNotPositive;
import static org.fest.assertions.test.ExpectedException.none;

import org.fest.assertions.test.ExpectedException;
import org.junit.*;

/**
 * Tests for {@link Offset#offset(Double)}.
 *
 * @author Alex Ruiz
 */
public class Offset_offset_with_Double_Test {
  @Rule
  public ExpectedException thrown = none();

  @Test
  public void should_throw_error_if_value_is_null() {
    thrown.expect(NullPointerException.class);
    Double value = null;
    Offset.offset(value);
  }

  @Test
  public void should_throw_error_if_value_is_negative() {
    thrown.expectIllegalArgumentException(offsetValueIsNotPositive());
    Offset.offset(-1d);
  }

  @Test
  public void should_create_Offset() {
    Double value = 0.8d;
    Offset<Double> offset = Offset.offset(value);
    assertSame(value, offset.value);
  }
}
