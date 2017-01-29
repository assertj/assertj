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
package org.assertj.core.api.abstract_;

import static org.assertj.core.test.ExpectedException.none;

import org.assertj.core.api.AbstractAssert;
import org.assertj.core.api.ConcreteAssert;
import org.assertj.core.test.ExpectedException;
import org.junit.Rule;
import org.junit.Test;


/**
 * Tests for <code>{@link AbstractAssert#getWClass()}</code>.
 * 
 * @author Joel Costigliola
 */
public class AbstractAssert_get_writable_info_Test {

  @Rule
  public ExpectedException thrown = none();

  @Test
  public void should_keep_specific_error_message_and_description_set_by_user() {
    thrown.expectAssertionError("[user description] specific error message");
    new ConcreteAssert(6L).as("user description").checkNull();
  }

}
