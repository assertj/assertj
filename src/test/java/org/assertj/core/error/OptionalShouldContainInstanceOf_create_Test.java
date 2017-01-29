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
package org.assertj.core.error;

import org.junit.Test;

import java.util.Optional;

import static java.lang.String.format;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.error.OptionalShouldContainInstanceOf.shouldContainInstanceOf;

public class OptionalShouldContainInstanceOf_create_Test {

  @Test
  public void should_create_error_message_with_empty() {
    String errorMessage = shouldContainInstanceOf(Optional.empty(), Object.class).create();
    assertThat(errorMessage).isEqualTo(format("%nExpecting:%n <Optional>%n" +
      "to contain a value that is an instance of:%n <java.lang.Object>%n" +
      "but was empty"));
  }

  @Test
  public void should_create_error_message_with_expected_type() {
    String errorMessage = shouldContainInstanceOf(Optional.of(Integer.MIN_VALUE), String.class).create();
    assertThat(errorMessage).isEqualTo(format("%nExpecting:%n <Optional>%n" +
      "to contain a value that is an instance of:%n <java.lang.String>%n" +
      "but did contain an instance of:%n <java.lang.Integer>"));
  }
}
