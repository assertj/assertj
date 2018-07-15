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
package org.assertj.core.api.assumptions;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assumptions.assumeThat;

import org.junit.AssumptionViolatedException;
import org.junit.jupiter.api.Test;

public class Assumptions_assumeThat_Object_Test {

  private static final Object STRING_OBJECT = "test";

  @Test
  public void should_run_test_when_assumption_passes() {
    assertThatCode(() -> assumeThat(STRING_OBJECT).isNotNull().isEqualTo("test")).doesNotThrowAnyException();
  }

  @Test
  public void should_run_test_when_assumption_for_internally_created_string_passes() {
    assertThatCode(() -> assumeThat(STRING_OBJECT).isNotNull().asString().startsWith("te")).doesNotThrowAnyException();
  }

  @Test
  public void should_run_test_when_assumption_for_internally_created_list_passes() {
    Object listObject = asList(1, 2, 3);
    assertThatCode(() -> assumeThat(listObject).isNotNull().asList().hasSize(3)).doesNotThrowAnyException();
  }

  @Test
  public void should_ignore_test_when_assumption_fails() {
    assertThatExceptionOfType(AssumptionViolatedException.class).isThrownBy(() -> assumeThat(STRING_OBJECT).isNotNull()
                                                                                                           .isEqualTo("other"));
  }

  @Test
  public void should_ignore_test_when_assumption_for_internally_created_string_assertion_fails() {
    assertThatExceptionOfType(AssumptionViolatedException.class).isThrownBy(() -> assumeThat(STRING_OBJECT).isNotNull()
                                                                                                           .asString()
                                                                                                           .isEqualTo("other"));
  }

  @Test
  public void should_ignore_test_when_assumption_for_internally_created_list_assertion_fails() {
    Object listObject = asList(1, 2, 3);
    assertThatExceptionOfType(AssumptionViolatedException.class).isThrownBy(() -> assumeThat(listObject).isNotNull()
                                                                                                        .asList()
                                                                                                        .contains(4,
                                                                                                                  5));
  }
}
