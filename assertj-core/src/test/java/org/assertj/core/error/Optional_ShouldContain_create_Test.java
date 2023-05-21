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
 * Copyright 2012-2023 the original author or authors.
 */
package org.assertj.core.error;

import static java.lang.String.format;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.error.OptionalShouldContain.shouldContain;
import static org.assertj.core.error.OptionalShouldContain.shouldContainSame;

import java.util.Optional;
import java.util.OptionalDouble;
import java.util.OptionalInt;
import java.util.OptionalLong;

import org.junit.jupiter.api.Test;

class Optional_ShouldContain_create_Test {

  @Test
  void should_create_error_message_when_value_not_present() {
    // WHEN
    String errorMessage = shouldContain(10).create();
    // THEN
    then(errorMessage).isEqualTo(format("%nExpecting Optional to contain:%n" +
                                        "  10%n" +
                                        "but was empty."));
  }

  @Test
  void should_create_error_message() {
    // WHEN
    String errorMessage = shouldContain(Optional.of(20), 10).create();
    // THEN
    then(errorMessage).isEqualTo(format("%nExpecting actual:%n" +
                                        "  Optional[20]%n" +
                                        "to contain:%n" +
                                        "  10%n" +
                                        "but did not."));
  }

  @Test
  void should_create_error_message_when_optional_empty() {
    // WHEN
    String errorMessage = shouldContain(Optional.empty(), 10).create();
    // THEN
    then(errorMessage).isEqualTo(format("%nExpecting Optional to contain:%n" +
                                        "  10%n" +
                                        "but was empty."));
  }

  @Test
  void should_create_error_message_with_optionaldouble() {
    // WHEN
    String errorMessage = shouldContain(OptionalDouble.of(20.0), 10.0).create();
    // THEN
    then(errorMessage).isEqualTo(format("%nExpecting actual:%n" +
                                        "  OptionalDouble[20.0]%n" +
                                        "to contain:%n" +
                                        "  10.0%n" +
                                        "but did not."));
  }

  @Test
  void should_create_error_message_with_empty_optionaldouble() {
    // WHEN
    String errorMessage = shouldContain(OptionalDouble.empty(), 10.0).create();
    // THEN
    then(errorMessage).isEqualTo(format("%nExpecting Optional to contain:%n" +
                                        "  10.0%n" +
                                        "but was empty."));
  }

  @Test
  void should_create_error_message_with_optionalint() {
    // WHEN
    String errorMessage = shouldContain(OptionalInt.of(20), 10).create();
    // THEN
    then(errorMessage).isEqualTo(format("%nExpecting actual:%n" +
                                        "  OptionalInt[20]%n" +
                                        "to contain:%n" +
                                        "  10%n" +
                                        "but did not."));
  }

  @Test
  void should_create_error_message_with_empty_optionalint() {
    // WHEN
    String errorMessage = shouldContain(OptionalInt.empty(), 10).create();
    // THEN
    then(errorMessage).isEqualTo(format("%nExpecting Optional to contain:%n" +
                                        "  10%n" +
                                        "but was empty."));
  }

  @Test
  void should_create_error_message_with_optionallong() {
    // WHEN
    String errorMessage = shouldContain(OptionalLong.of(20L), 10L).create();
    // THEN
    then(errorMessage).isEqualTo(format("%nExpecting actual:%n" +
                                        "  OptionalLong[20]%n" +
                                        "to contain:%n" +
                                        "  10L%n" +
                                        "but did not."));
  }

  @Test
  void should_create_error_message_with_empty_optionallong() {
    // WHEN
    String errorMessage = shouldContain(OptionalLong.empty(), 10L).create();
    // THEN
    then(errorMessage).isEqualTo(format("%nExpecting Optional to contain:%n" +
                                        "  10L%n" +
                                        "but was empty."));
  }

  @Test
  void should_create_error_message_for_different_instances() {
    // WHEN
    String errorMessage = shouldContainSame(Optional.of(10), 10).create();
    // THEN
    then(errorMessage).isEqualTo(format("%nExpecting actual:%n" +
                                        "  Optional[10]%n" +
                                        "to contain the instance (i.e. compared with ==):%n" +
                                        "  10%n" +
                                        "but did not."));
  }
}
