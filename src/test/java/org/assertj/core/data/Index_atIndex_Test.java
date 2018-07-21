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

import org.junit.jupiter.api.Test;

/**
 * Tests for {@link Index#atIndex(int)}.
 *
 * @author Alex Ruiz
 */
public class Index_atIndex_Test {

  @Test
  public void should_throw_error_if_value_is_negative() {
    assertThatIllegalArgumentException().isThrownBy(() -> Index.atIndex(-1))
                                        .withMessage("The value of the index should not be negative");
  }

  @Test
  public void should_create_new_Index() {
    Index index = Index.atIndex(8);
    assertThat(index.value).isEqualTo(8);
  }
}
