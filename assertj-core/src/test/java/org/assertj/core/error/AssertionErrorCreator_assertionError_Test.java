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

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.presentation.StandardRepresentation.STANDARD_REPRESENTATION;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.List;

import org.assertj.core.api.WritableAssertionInfo;
import org.assertj.core.internal.FileContent;
import org.assertj.core.presentation.Representation;
import org.assertj.core.presentation.StandardRepresentation;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.opentest4j.AssertionFailedError;
import org.opentest4j.FileInfo;

class AssertionErrorCreator_assertionError_Test {

  private final AssertionErrorCreator assertionErrorCreator = AssertionErrorCreator.instance();

  @Test
  void should_create_AssertionFailedError_using_reflection() {
    // GIVEN
    String actual = "actual";
    String expected = "expected";
    String message = "error message";
    Representation representation = mock(Representation.class);
    // WHEN
    AssertionError assertionError = assertionErrorCreator.assertionError(message, actual, expected, representation);
    // THEN
    then(assertionError).isInstanceOf(AssertionFailedError.class)
                        .hasMessage(message);
    AssertionFailedError assertionFailedError = (AssertionFailedError) assertionError;
    then(assertionFailedError.getActual().getValue()).as("").isSameAs(actual);
    then(assertionFailedError.getExpected().getValue()).isSameAs(expected);
  }

  @Test
  void should_create_AssertionError_when_an_AssertionFailedError_could_not_be_created() throws Exception {
    // GIVEN
    ConstructorInvoker constructorInvoker = mock(ConstructorInvoker.class);
    AssertionErrorCreator underTest = new AssertionErrorCreator(constructorInvoker);
    given(constructorInvoker.newInstance(anyString(), any(Class[].class), any())).willThrow(Exception.class);
    String message = "error message";
    Representation representation = mock(Representation.class);
    // WHEN
    AssertionError assertionError = underTest.assertionError(message, "actual", "expected", representation);
    // THEN
    then(assertionError).isNotInstanceOf(AssertionFailedError.class)
                        .hasMessage(message);
  }

  @Test
  void should_honor_representation_in_AssertionFailedError_actual_and_expected_values() {
    // WHEN
    var assertionError = assertionErrorCreator.assertionError("boom", List.of("actual"), List.of("expected"),
                                                              STANDARD_REPRESENTATION);
    // THEN
    then(assertionError).isInstanceOf(AssertionFailedError.class);
    var assertionFailedError = (AssertionFailedError) assertionError;
    then(assertionFailedError.getActual().toString()).contains("[\"actual\"]");
    then(assertionFailedError.getExpected().toString()).contains("[\"expected\"]");
  }

  @Test
  void should_honor_custom_representation_in_AssertionFailedError_actual_and_expected_values() {
    // GIVEN
    Representation representation = new ItemRepresentation();
    // WHEN
    var assertionError = assertionErrorCreator.assertionError("boom", new Item("actual"), new Item("expected"), representation);
    // THEN
    then(assertionError).isInstanceOf(AssertionFailedError.class);
    AssertionFailedError assertionFailedError = (AssertionFailedError) assertionError;
    then(assertionFailedError.getActual().toString()).contains("actual");
    then(assertionFailedError.getExpected().toString()).contains("expected");
  }

  @Test
  void should_use_FileInfo_as_actual_and_expected_for_FileContent_values() {
    // GIVEN
    var actual = new FileContent("/tmp/actual.txt", "actual".getBytes());
    var expected = new FileContent("/tmp/expected.txt", "expected".getBytes());
    // WHEN
    var assertionError = assertionErrorCreator.assertionError("boom", actual, expected, STANDARD_REPRESENTATION);
    // THEN
    then(assertionError).isInstanceOf(AssertionFailedError.class);
    var assertionFailedError = (AssertionFailedError) assertionError;
    then(assertionFailedError.getActual().getValue()).isInstanceOfSatisfying(FileInfo.class, fileInfo -> {
      assertThat(fileInfo.getPath()).isEqualTo("/tmp/actual.txt");
      assertThat(fileInfo.getContents()).isEqualTo("actual".getBytes());
    });
    then(assertionFailedError.getExpected().getValue()).isInstanceOfSatisfying(FileInfo.class, fileInfo -> {
      assertThat(fileInfo.getPath()).isEqualTo("/tmp/expected.txt");
      assertThat(fileInfo.getContents()).isEqualTo("expected".getBytes());
    });
  }

  @Test
  void should_remove_AssertJ_elements_from_AssertionFailedError_stack_trace_when_configured() {
    // GIVEN
    var underTest = new AssertionErrorCreator(new ConstructorInvoker());
    underTest.setRemoveAssertJRelatedElementsFromStackTrace(true);
    // WHEN
    var assertionError = underTest.assertionError("boom", "actual", "expected", STANDARD_REPRESENTATION);
    // THEN
    then(assertionError).isInstanceOf(AssertionFailedError.class);
    then(assertionError.getStackTrace()).noneMatch(element -> element.getClassName().startsWith("org.assertj.core.error."));
  }

  @Nested
  class Thread_dump {

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
    void should_print_thread_dump_if_requested_by_assertion_info() {
      // GIVEN
      var info = new WritableAssertionInfo();
      info.enablePrintThreadDumpOnError();
      // WHEN
      assertionErrorCreator.assertionError(info, "boom");
      // THEN
      then(err.toString()).contains("java.lang.Thread.State", "\"main\"");
    }

    @Test
    void should_print_thread_dump_with_actual_and_expected_if_requested_by_assertion_info() {
      // GIVEN
      var info = new WritableAssertionInfo();
      info.enablePrintThreadDumpOnError();
      // WHEN
      assertionErrorCreator.assertionError(info, "boom", "actual", "expected");
      // THEN
      then(err.toString()).contains("java.lang.Thread.State", "\"main\"");
    }

    @Test
    void should_not_print_thread_dump_if_not_requested_by_assertion_info() {
      // GIVEN
      var info = new WritableAssertionInfo();
      // WHEN
      assertionErrorCreator.assertionError(info, "boom");
      assertionErrorCreator.assertionError(info, "boom", "actual", "expected");
      // THEN
      then(err.toString()).isEmpty();
    }
  }

  record Item(String name) {

    @Override
    public String toString() {
      return "useless toString";
    }
  }

  private static class ItemRepresentation extends StandardRepresentation {
    @Override
    protected String fallbackToStringOf(final Object object) {
      if (object instanceof Item) {
        return ((Item) object).name;
      }
      return super.fallbackToStringOf(object);
    }
  }
}
