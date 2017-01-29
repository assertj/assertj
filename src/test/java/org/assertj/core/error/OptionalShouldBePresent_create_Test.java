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

import static java.lang.String.format;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.error.OptionalShouldBePresent.shouldBePresent;

import java.util.Optional;
import java.util.OptionalDouble;
import java.util.OptionalInt;
import java.util.OptionalLong;

import org.junit.Test;

public class OptionalShouldBePresent_create_Test {

  @Test
  public void should_create_error_message_with_optional() throws Exception {
    String errorMessage = shouldBePresent(Optional.empty()).create();
    assertThat(errorMessage).isEqualTo(format("%nExpecting Optional to contain a value but was empty."));
  }

  @Test
  public void should_create_error_message_with_optionaldouble() throws Exception {
    String errorMessage = shouldBePresent(OptionalDouble.empty()).create();
    assertThat(errorMessage).isEqualTo(format("%nExpecting OptionalDouble to contain a value but was empty."));
  }

  @Test
  public void should_create_error_message_with_optionalint() throws Exception {
    String errorMessage = shouldBePresent(OptionalInt.empty()).create();
    assertThat(errorMessage).isEqualTo(format("%nExpecting OptionalInt to contain a value but was empty."));
  }

  @Test
  public void should_create_error_message_with_optionallong() throws Exception {
    String errorMessage = shouldBePresent(OptionalLong.empty()).create();
    assertThat(errorMessage).isEqualTo(format("%nExpecting OptionalLong to contain a value but was empty."));
  }
}
