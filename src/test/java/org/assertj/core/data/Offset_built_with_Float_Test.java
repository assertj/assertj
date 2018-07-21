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
 * Copyright 2012-2018 the original author or authors.
 */
package org.assertj.core.data;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;
import static org.assertj.core.data.Offset.offset;
import static org.assertj.core.data.Offset.strictOffset;
import static org.assertj.core.internal.ErrorMessages.offsetValueIsNotPositive;
import static org.assertj.core.internal.ErrorMessages.strictOffsetValueIsNotStrictlyPositive;

import org.junit.jupiter.api.Test;

/**
 * Tests for {@link Offset#offset(Float)}.
 *
 * @author Alex Ruiz
 */
public class Offset_built_with_Float_Test {

  @Test
  public void should_throw_error_if_value_is_null() {
    assertThatNullPointerException().isThrownBy(() -> {
      Float value = null;
      offset(value);
    });
  }

  @Test
  public void should_throw_error_if_value_is_negative() {
    assertThatIllegalArgumentException().isThrownBy(() -> offset(-1f)).withMessage(offsetValueIsNotPositive());
  }

  @Test
  public void should_throw_error_if_value_is_zero_strict_offset() {
    assertThatIllegalArgumentException().isThrownBy(() -> strictOffset(0f))
                                        .withMessage(strictOffsetValueIsNotStrictlyPositive());
  }

  @Test
  public void should_create_Offset() {
    Float value = 0.8f;
    Offset<Float> offset = offset(value);
    assertThat(offset.value).isSameAs(value);
  }
}
