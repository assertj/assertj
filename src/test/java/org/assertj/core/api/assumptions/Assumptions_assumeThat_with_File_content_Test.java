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
 * Copyright 2012-2022 the original author or authors.
 */
package org.assertj.core.api.assumptions;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.assertj.core.api.Assumptions.assumeThat;
import static org.assertj.core.api.BDDAssertions.thenCode;
import static org.assertj.core.util.AssertionsUtil.expectAssumptionNotMetException;

import java.io.File;

import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.Test;

class Assumptions_assumeThat_with_File_content_Test {

  private static final File FILE = new File("src/test/resources/actual_file.txt");

  @Test
  void should_run_test_when_assumption_using_file_content_succeeds() {
    // WHEN
    ThrowingCallable assumptionCode = () -> assumeThat(FILE).content().contains("actual");
    // THEN
    thenCode(assumptionCode).doesNotThrowAnyException();
  }

  @Test
  void should_ignore_test_when_assumption_using_file_content_fails() {
    // WHEN
    ThrowingCallable assumptionCode = () -> assumeThat(FILE).content().contains("foo");
    // THEN
    expectAssumptionNotMetException(assumptionCode);
  }

  @Test
  void should_run_test_when_assumption_using_file_content_with_charset_succeeds() {
    // WHEN
    ThrowingCallable assumptionCode = () -> assumeThat(FILE).content(UTF_8).contains("actual");
    // THEN
    thenCode(assumptionCode).doesNotThrowAnyException();
  }

  @Test
  void should_ignore_test_when_assumption_using_file_content_with_charset_fails() {
    // WHEN
    ThrowingCallable assumptionCode = () -> assumeThat(FILE).content(UTF_8).contains("foo");
    // THEN
    expectAssumptionNotMetException(assumptionCode);
  }
}
