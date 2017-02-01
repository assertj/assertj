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
package org.assertj.core.api.atomic.reference;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.concurrent.atomic.AtomicReference;

import org.assertj.core.test.ExpectedException;
import org.junit.Rule;
import org.junit.Test;

public class AtomicReferenceAssert_overridingErrorMessage_Test {

  @Rule
  public ExpectedException thrown = ExpectedException.none();

  @Test
  public void should_honor_custom_error_message_set_with_withFailMessage() {
    String error = "ssss";
    thrown.expectAssertionErrorWithMessageContaining(error);
    AtomicReference<String> actual = new AtomicReference<>("foo");
    assertThat(actual).withFailMessage(error).hasValue("bar");
  }

  @Test
  public void should_honor_custom_error_message_set_with_overridingErrorMessage() {
    String error = "ssss";
    thrown.expectAssertionErrorWithMessageContaining(error);
    AtomicReference<String> actual = new AtomicReference<>("foo");
    assertThat(actual).withFailMessage(error).hasValue("bar");
  }
  
}
