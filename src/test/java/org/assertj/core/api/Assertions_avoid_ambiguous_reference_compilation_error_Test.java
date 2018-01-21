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
package org.assertj.core.api;

import java.util.Date;

import org.junit.Test;

public class Assertions_avoid_ambiguous_reference_compilation_error_Test {

  @Test
  public void should_not_report_ambiguous_reference_compilation_error() {
    // does not compile, explanation: https://stackoverflow.com/questions/29499847/ambiguous-method-in-java-8-why
    // Assertions.assertThat(getDate()).isEqualTo(getDate());
    
    // compiles since AssertionsForClassTypes does not provide assertThat for interfaces   
    AssertionsForClassTypes.assertThat(getDate()).isEqualTo(getDate());
  }

  @SuppressWarnings("unchecked")
  protected static <T extends Date> T getDate() {
    return (T) new Date(123);
  }

}
