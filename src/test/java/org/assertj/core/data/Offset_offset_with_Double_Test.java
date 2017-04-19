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
package org.assertj.core.data;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.internal.ErrorMessages.offsetValueIsNotPositive;
import static org.assertj.core.test.ExpectedException.none;


import org.assertj.core.data.Offset;
import org.assertj.core.test.ExpectedException;
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
    thrown.expectNullPointerException();
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
    assertThat(offset.value).isSameAs(value);
  }
}
