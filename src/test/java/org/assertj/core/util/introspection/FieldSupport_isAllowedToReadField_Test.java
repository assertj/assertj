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
 * Copyright 2012-2022 the original author or authors.
 */
package org.assertj.core.util.introspection;

import static org.assertj.core.api.Assertions.assertThat;

import org.assertj.core.test.Jedi;
import org.junit.jupiter.api.Test;

class FieldSupport_isAllowedToReadField_Test {

  private FieldSupport fieldSupport = FieldSupport.comparison();

  @Test
  void fieldSupport_should_be_allowed_to_read_public_field_only() throws Exception {
    fieldSupport.setAllowUsingPrivateFields(false);
    try {
      assertThat(fieldSupport.isAllowedToRead(Jedi.class.getField("lightSaberColor"))).isTrue();
      assertThat(fieldSupport.isAllowedToRead(Jedi.class.getDeclaredField("strangeNotReadablePrivateField"))).isFalse();
    } finally {
      // reset
      fieldSupport.setAllowUsingPrivateFields(true);
    }
  }

  @Test
  void fieldSupport_should_be_allowed_to_read_whatever_field_when_allowedToUsePrivateFields_flag_is_true()
                                                                                                           throws Exception {
    fieldSupport.setAllowUsingPrivateFields(true);
    assertThat(fieldSupport.isAllowedToRead(Jedi.class.getField("lightSaberColor"))).isTrue();
    assertThat(fieldSupport.isAllowedToRead(Jedi.class.getDeclaredField("strangeNotReadablePrivateField"))).isTrue();
  }

}
