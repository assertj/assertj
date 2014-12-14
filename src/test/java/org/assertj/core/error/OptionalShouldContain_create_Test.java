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
 * Copyright 2012-2014 the original author or authors.
 */
package org.assertj.core.error;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.error.OptionalShouldContain.shouldContain;

import java.util.Optional;

import org.junit.Test;

public class OptionalShouldContain_create_Test {

  @Test
  public void should_create_error_message_when_optional_is_empty() throws Exception {
	String errorMessage = shouldContain(10).create();
	assertThat(errorMessage).isEqualTo("\nExpecting an Optional with value:\n" +
	                                   "  <10>\n" +
	                                   "but was empty.");
  }

  @Test
  public void should_create_error_message() throws Exception {
	String errorMessage = shouldContain(Optional.of(20), 10).create();
	assertThat(errorMessage).isEqualTo("\nExpecting:\n" +
	                                   "  <Optional[20]>\n" +
	                                   "to contains:\n" +
	                                   "  <10>\n" +
	                                   "but was not.");
  }
}
