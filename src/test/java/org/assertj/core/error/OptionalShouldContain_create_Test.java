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
import static org.assertj.core.error.OptionalShouldContain.shouldContain;
import static org.assertj.core.error.OptionalShouldContain.shouldContainSame;

import java.util.Optional;
import java.util.OptionalDouble;
import java.util.OptionalInt;
import java.util.OptionalLong;

import org.junit.Test;

public class OptionalShouldContain_create_Test {

  @Test
  public void should_create_error_message_when_value_not_present() {
    String errorMessage = shouldContain(10).create();
    assertThat(errorMessage).isEqualTo(format("%nExpecting Optional to contain:%n" +
                                              "  <10>%n" +
                                              "but was empty."));
  }

  @Test
  public void should_create_error_message() {
    String errorMessage = shouldContain(Optional.of(20), 10).create();
    assertThat(errorMessage).isEqualTo(format("%nExpecting:%n" +
                                              "  <Optional[20]>%n" +
                                              "to contain:%n" +
                                              "  <10>%n" +
                                              "but did not."));
  }

  @Test
  public void should_create_error_message_when_optional_empty() {
    String errorMessage = shouldContain(Optional.empty(), 10).create();
    assertThat(errorMessage).isEqualTo(format("%nExpecting Optional to contain:%n" +
                                              "  <10>%n" +
                                              "but was empty."));
  }

  @Test
  public void should_create_error_message_with_optionaldouble() {
    String errorMessage = shouldContain(OptionalDouble.of(20.0), 10.0).create();
    assertThat(errorMessage).isEqualTo(format("%nExpecting:%n" +
                                              "  <OptionalDouble[20.0]>%n" +
                                              "to contain:%n" +
                                              "  <10.0>%n" +
                                              "but did not."));
  }

  @Test
  public void should_create_error_message_with_empty_optionaldouble() {
    String errorMessage = shouldContain(OptionalDouble.empty(), 10.0).create();
    assertThat(errorMessage).isEqualTo(format("%nExpecting Optional to contain:%n" +
                                              "  <10.0>%n" +
                                              "but was empty."));
  }

  @Test
  public void should_create_error_message_with_optionalint() {
    String errorMessage = shouldContain(OptionalInt.of(20), 10).create();
    assertThat(errorMessage).isEqualTo(format("%nExpecting:%n" +
                                              "  <OptionalInt[20]>%n" +
                                              "to contain:%n" +
                                              "  <10>%n" +
                                              "but did not."));
  }

  @Test
  public void should_create_error_message_with_empty_optionalint() {
    String errorMessage = shouldContain(OptionalInt.empty(), 10).create();
    assertThat(errorMessage).isEqualTo(format("%nExpecting Optional to contain:%n" +
                                              "  <10>%n" +
                                              "but was empty."));
  }

  @Test
  public void should_create_error_message_with_optionallong() {
    String errorMessage = shouldContain(OptionalLong.of(20L), 10L).create();
    assertThat(errorMessage).isEqualTo(format("%nExpecting:%n" +
                                              "  <OptionalLong[20]>%n" +
                                              "to contain:%n" +
                                              "  <10L>%n" +
                                              "but did not."));
  }

  @Test
  public void should_create_error_message_with_empty_optionallong() {
    String errorMessage = shouldContain(OptionalLong.empty(), 10L).create();
    assertThat(errorMessage).isEqualTo(format("%nExpecting Optional to contain:%n" +
                                              "  <10L>%n" +
                                              "but was empty."));
  }

  @Test
  public void should_create_error_message_for_different_instances() {
    String errorMessage = shouldContainSame(Optional.of(10), 10).create();
    assertThat(errorMessage).isEqualTo(format("%nExpecting:%n" +
                                       "  <Optional[10]>%n" +
                                       "to contain the instance (i.e. compared with ==):%n" +
                                       "  <10>%n" +
                                       "but did not."));
  }
}
