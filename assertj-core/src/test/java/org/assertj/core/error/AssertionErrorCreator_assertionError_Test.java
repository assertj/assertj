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
import static org.assertj.core.presentation.StandardRepresentation.STANDARD_REPRESENTATION;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

import java.util.List;

import org.assertj.core.internal.FileContent;
import org.assertj.core.presentation.Representation;
import org.assertj.core.presentation.StandardRepresentation;
import org.junit.jupiter.api.Test;
import org.opentest4j.AssertionFailedError;
import org.opentest4j.FileInfo;

class AssertionErrorCreator_assertionError_Test {

  private final AssertionErrorCreator assertionErrorCreator = new AssertionErrorCreator();

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
    then(assertionFailedError.getActual().getValue()).isSameAs(actual);
    then(assertionFailedError.getExpected().getValue()).isSameAs(expected);
  }

  @Test
  void should_create_AssertionError_when_an_AssertionFailedError_could_not_be_created() throws Exception {
    // GIVEN
    String message = "error message";
    ConstructorInvoker constructorInvoker = mock(ConstructorInvoker.class);
    Representation representation = mock(Representation.class);
    given(constructorInvoker.newInstance(anyString(), any(Class[].class), any())).willThrow(Exception.class);
    assertionErrorCreator.constructorInvoker = constructorInvoker;
    // WHEN
    AssertionError assertionError = assertionErrorCreator.assertionError(message, "actual", "expected", representation);
    // THEN
    then(assertionError).isNotInstanceOf(AssertionFailedError.class)
                        .hasMessage(message);
  }

  @Test
  void should_use_FileInfo_as_actual_and_expected_for_FileContent_values() {
    // GIVEN
    FileContent actual = new FileContent("/tmp/actual.txt", "actual".getBytes());
    FileContent expected = new FileContent("/tmp/expected.txt", "expected".getBytes());
    // WHEN
    var assertionError = assertionErrorCreator.assertionError("boom", actual, expected, STANDARD_REPRESENTATION);
    // THEN
    then(assertionError).isInstanceOf(AssertionFailedError.class);
    var assertionFailedError = (AssertionFailedError) assertionError;
    then(assertionFailedError.getActual().getValue()).isInstanceOfSatisfying(FileInfo.class, fileInfo -> {
      then(fileInfo.getPath()).isEqualTo("/tmp/actual.txt");
      then(fileInfo.getContents()).isEqualTo("actual".getBytes());
    });
    then(assertionFailedError.getExpected().getValue()).isInstanceOfSatisfying(FileInfo.class, fileInfo -> {
      then(fileInfo.getPath()).isEqualTo("/tmp/expected.txt");
      then(fileInfo.getContents()).isEqualTo("expected".getBytes());
    });
  }

  @Test
  public void should_honor_representation_in_AssertionFailedError_actual_and_expected_values() {
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
  public void should_honor_custom_representation_in_AssertionFailedError_actual_and_expected_values() {
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
