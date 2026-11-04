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
package org.assertj.core.api.abstract_;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.util.AssertionsUtil.expectAssertionError;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.assertj.core.api.AbstractStringAssert;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AbstractAssert_withThreadDumpOnError_Test {

  private final PrintStream originalErr = System.err;
  private final ByteArrayOutputStream err = new ByteArrayOutputStream();

  @BeforeEach
  void setUp() {
    System.setErr(new PrintStream(err, true));
  }

  @AfterEach
  void tearDown() {
    System.setErr(originalErr);
  }

  @Test
  void should_return_this() {
    // GIVEN
    AbstractStringAssert<?> underTest = assertThat("foo");
    // WHEN
    AbstractStringAssert<?> result = underTest.withThreadDumpOnError();
    // THEN
    then(result).isSameAs(underTest);
  }

  @Test
  void should_not_print_thread_dump_if_assertion_fails_without_withThreadDumpOnError() {
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat("foo").isEqualTo("bar"));
    // THEN
    then(assertionError).isNotNull();
    then(err.toString()).isEmpty();
  }

  @Test
  void should_print_thread_dump_if_assertion_fails() {
    // GIVEN
    AbstractStringAssert<?> actual = assertThat("foo").withThreadDumpOnError();
    // WHEN
    var assertionError = expectAssertionError(() -> actual.isEqualTo("bar"));
    // THEN
    then(assertionError).isNotNull();
    then(err.toString()).contains("java.lang.Thread.State", "\"main\"");
  }

  @Test
  void should_only_print_thread_dump_for_the_assertion_it_was_enabled_on() {
    // GIVEN
    assertThat("foo").withThreadDumpOnError();
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat("foo").isEqualTo("bar"));
    // THEN
    then(assertionError).isNotNull();
    then(err.toString()).isEmpty();
  }

  @Test
  void should_not_print_thread_dump_if_assertion_passes() {
    // WHEN
    assertThat("foo").withThreadDumpOnError().isNotNull();
    // THEN
    then(err.toString()).isEmpty();
  }

}
