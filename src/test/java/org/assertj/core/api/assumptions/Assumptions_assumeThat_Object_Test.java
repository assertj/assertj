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
package org.assertj.core.api.assumptions;

import org.junit.AfterClass;
import org.junit.Test;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assumptions.assumeThat;

public class Assumptions_assumeThat_Object_Test {

  private static final Object STRING_OBJECT = "test";
  private static int ranTests = 0;

  @Test
  public void should_ignore_test_when_assumption_fails() {
    assumeThat(STRING_OBJECT).isNotNull().isEqualTo("other");
    assertThat(true).isFalse();
  }

  @Test
  public void should_run_test_when_assumption_passes() {
    assumeThat(STRING_OBJECT).isNotNull().isEqualTo("test");
    ranTests++;
  }

  @Test
  public void should_ignore_test_when_assumption_for_internally_created_string_assertion_fails() {
    assumeThat(STRING_OBJECT).isNotNull().asString().isEqualTo("other");
    assertThat(true).isFalse();
  }

  @Test
  public void should_ignore_test_when_assumption_for_internally_created_list_assertion_fails() {
    Object listObject = asList(1, 2, 3);
    assumeThat(listObject).isNotNull().asList().hasSize(2);
    assertThat(true).isFalse();
  }

  @AfterClass
  public static void afterClass() {
    assertThat(ranTests).isEqualTo(1);
  }
}
