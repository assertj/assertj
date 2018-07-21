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
package org.assertj.core.api.atomic.referencearray;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.assertj.core.util.Arrays.array;

import java.util.concurrent.atomic.AtomicReferenceArray;

import org.assertj.core.test.FluentJedi;
import org.assertj.core.test.Name;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class AtomicReferenceArrayAssert_extractingResultOf_Test {

  private static FluentJedi yoda;
  private static FluentJedi vader;
  private static AtomicReferenceArray<FluentJedi> jedis;

  @BeforeAll
  public static void setUpOnce() {
    yoda = new FluentJedi(new Name("Yoda"), 800, false);
    vader = new FluentJedi(new Name("Darth Vader"), 50 ,true);
    jedis = new AtomicReferenceArray<>(array(yoda, vader));
  }

  @Test
  public void should_allow_assertions_on_method_invocation_result_extracted_from_given_iterable() {
    // extract method result
    assertThat(jedis).extractingResultOf("age").containsOnly(800, 50);
    // extract if method result is primitive
    assertThat(jedis).extractingResultOf("darkSide").containsOnly(false, true);
    // extract if method result is also a property
    assertThat(jedis).extractingResultOf("name").containsOnly(new Name("Yoda"), new Name("Darth Vader"));
    // extract toString method result
    assertThat(jedis).extractingResultOf("toString").containsOnly("Yoda", "Darth Vader");
  }

  @Test
  public void should_allow_assertions_on_method_invocation_result_extracted_from_given_iterable_with_enforcing_return_type() {
    assertThat(jedis).extractingResultOf("name", Name.class).containsOnly(new Name("Yoda"), new Name("Darth Vader"));
  }

  @Test
  public void should_throw_error_if_no_method_with_given_name_can_be_extracted() {
    assertThatIllegalArgumentException().isThrownBy(() -> assertThat(jedis).extractingResultOf("unknown"))
                                        .withMessage("Can't find method 'unknown' in class FluentJedi.class. Make sure public method exists and accepts no arguments!");
  }

  @Test
  public void should_use_method_name_as_description_when_extracting_result_of_method_list() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> assertThat(jedis).extractingResultOf("age")
                                                                                      .isEmpty())
                                                   .withMessageContaining("[Extracted: result of age()]");
  }

  @Test
  public void should_use_method_name_as_description_when_extracting_typed_result_of_method_list() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> assertThat(jedis).extractingResultOf("age",
                                                                                                          Integer.class)
                                                                                      .isEmpty())
                                                   .withMessageContaining("[Extracted: result of age()]");
  }
}

