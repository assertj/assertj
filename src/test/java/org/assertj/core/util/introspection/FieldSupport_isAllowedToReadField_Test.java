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
package org.assertj.core.util.introspection;

import static org.assertj.core.api.Assertions.assertThat;

import org.assertj.core.test.Jedi;
import org.junit.Test;

public class FieldSupport_isAllowedToReadField_Test {

  private FieldSupport fieldSupport = FieldSupport.comparison();

  @Test
  public void fieldSupport_should_be_allowed_to_read_public_field_only() throws Exception {
	boolean allowedToUsePrivateFields = fieldSupport.isAllowedToUsePrivateFields();
	fieldSupport.setAllowUsingPrivateFields(false);
	assertThat(fieldSupport.isAllowedToRead(Jedi.class.getField("lightSaberColor"))).isTrue();
	assertThat(fieldSupport.isAllowedToRead(Jedi.class.getDeclaredField("strangeNotReadablePrivateField"))).isFalse();
	// reset
	fieldSupport.setAllowUsingPrivateFields(allowedToUsePrivateFields);
  }

  @Test
  public void fieldSupport_should_be_allowed_to_read_whatever_field_when_allowedToUsePrivateFields_flag_is_true()
	  throws Exception {
	boolean allowedToUsePrivateFields = fieldSupport.isAllowedToUsePrivateFields();
	fieldSupport.setAllowUsingPrivateFields(true);
	assertThat(fieldSupport.isAllowedToRead(Jedi.class.getField("lightSaberColor"))).isTrue();
	assertThat(fieldSupport.isAllowedToRead(Jedi.class.getDeclaredField("strangeNotReadablePrivateField"))).isTrue();
	// reset
	fieldSupport.setAllowUsingPrivateFields(allowedToUsePrivateFields);
  }

}
