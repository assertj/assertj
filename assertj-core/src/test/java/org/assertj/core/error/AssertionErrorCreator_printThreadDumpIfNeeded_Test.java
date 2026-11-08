/*
 * Copyright 2012-2026 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.assertj.core.error;

import static org.assertj.core.api.BDDAssertions.then;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.assertj.core.api.WritableAssertionInfo;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AssertionErrorCreator_printThreadDumpIfNeeded_Test {

  private final PrintStream originalErr = System.err;
  private final ByteArrayOutputStream err = new ByteArrayOutputStream();
  private final AssertionErrorCreator underTest = AssertionErrorCreator.instance();

  @BeforeEach
  void setUp() {
    System.setErr(new PrintStream(err, true));
  }

  @AfterEach
  void tearDown() {
    System.setErr(originalErr);
  }

  @Test
  void should_print_thread_dump_if_requested_by_assertion_info() {
    // GIVEN
    var info = new WritableAssertionInfo();
    info.enablePrintThreadDumpOnError();
    // WHEN
    underTest.printThreadDumpIfNeeded(info);
    // THEN
    then(err.toString()).contains("java.lang.Thread.State", "\"main\"");
  }

  @Test
  void should_not_print_thread_dump_if_not_requested_by_assertion_info() {
    // GIVEN
    var info = new WritableAssertionInfo();
    // WHEN
    underTest.printThreadDumpIfNeeded(info);
    // THEN
    then(err.toString()).isEmpty();
  }

}
