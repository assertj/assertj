/*
 * Copyright © 2024 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.assertj.core.error;

import static java.lang.String.format;
import static org.assertj.core.api.Assertions.catchThrowableOfType;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.presentation.StandardRepresentation.STANDARD_REPRESENTATION;
import static org.assertj.core.testkit.AlwaysDifferentComparator.ALWAY_DIFFERENT;
import static org.assertj.core.util.Arrays.array;
import static org.assertj.core.util.AssertionsUtil.expectAssertionError;
import static org.assertj.core.util.Lists.list;

import java.util.Objects;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.assertj.core.testkit.CaseInsensitiveStringComparator;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.opentest4j.AssertionFailedError;

class ShouldBeEqual_Test {

  @Test
  void should_display_comparison_strategy_in_error_message() {
    // GIVEN
    String actual = "Luke";
    String expected = "Yoda";
    ThrowingCallable code = () -> then(actual).as("Jedi")
                                              .usingComparator(CaseInsensitiveStringComparator.INSTANCE)
                                              .isEqualTo(expected);
    // WHEN
    AssertionFailedError error = catchThrowableOfType(code, AssertionFailedError.class);
    // THEN
    then(error.getActual().getValue()).isEqualTo(STANDARD_REPRESENTATION.toStringOf(actual));
    then(error.getExpected().getValue()).isEqualTo(STANDARD_REPRESENTATION.toStringOf(expected));
    then(error).hasMessage(format("[Jedi] %n" +
                                  "expected: \"Yoda\"%n" +
                                  " but was: \"Luke\"%n" +
                                  "when comparing values using CaseInsensitiveStringComparator"));
  }

  @Test
  void should_use_actual_and_expected_representation_in_AssertionFailedError_actual_and_expected_fields() {
    // GIVEN
    byte[] actual = { 1, 2, 3 };
    byte[] expected = { 1, 2, 4 };
    ThrowingCallable code = () -> then(actual).as("numbers").isEqualTo(expected);
    // WHEN
    AssertionFailedError error = catchThrowableOfType(code, AssertionFailedError.class);
    // THEN
    then(error.getActual().getValue()).isEqualTo("[1, 2, 3]");
    then(error.getExpected().getValue()).isEqualTo("[1, 2, 4]");
    then(error).hasMessage(format("[numbers] %n" +
                                  "expected: [1, 2, 4]%n" +
                                  " but was: [1, 2, 3]"));
  }

  @Test
  void should_display_multiline_values_nicely() {
    // GIVEN
    Xml actual = new Xml("1");
    Xml expected = new Xml("2");
    // WHEN
    AssertionError error = expectAssertionError(() -> then(actual).isEqualTo(expected));
    // THEN
    then(error).hasMessage(format("%nexpected: %n" +
                                  "  <xml>%n" +
                                  "    <value>2</value>%n" +
                                  "  </xml>%n" +
                                  " but was: %n" +
                                  "  <xml>%n" +
                                  "    <value>1</value>%n" +
                                  "  </xml>"));
  }

  @Test
  void should_display_all_values_as_multiline_if_one_is() {
    // GIVEN
    String actual = "foo";
    Xml expected = new Xml("2");
    // WHEN
    AssertionError error = expectAssertionError(() -> then(actual).isEqualTo(expected));
    // THEN
    then(error).hasMessage(format("%nexpected: %n" +
                                  "  <xml>%n" +
                                  "    <value>2</value>%n" +
                                  "  </xml>%n" +
                                  " but was: %n" +
                                  "  \"foo\""));
  }

  @Test
  void should_display_multiline_values_nicely_with_null() {
    // GIVEN
    Xml actual = null;
    Xml expected = new Xml("2");
    // WHEN
    AssertionError error = expectAssertionError(() -> then(actual).isEqualTo(expected));
    // THEN
    then(error).hasMessage(format("%nexpected: %n" +
                                  "  <xml>%n" +
                                  "    <value>2</value>%n" +
                                  "  </xml>%n" +
                                  " but was: %n" +
                                  "  null"));
  }

  @Test
  void should_display_multiline_values_nicely_for_ambiguous_representation() {
    // GIVEN
    Xml actual = new Xml("1");
    XmlDuplicate expected = new XmlDuplicate("1");
    // WHEN
    AssertionError error = expectAssertionError(() -> then(actual).isEqualTo(expected));
    // THEN
    then(error).hasMessageContainingAll(format("%nexpected: %n" +
                                               "  <xml>%n" +
                                               "    <value>1</value>%n" +
                                               "  </xml> (XmlDuplicate@"),
                                        format("%n but was: %n" +
                                               "  <xml>%n" +
                                               "    <value>1</value>%n" +
                                               "  </xml> (Xml@"));
  }

  @Test
  void should_display_multiline_values_nicely_with_comparison_strategy() {
    // GIVEN
    Xml actual = new Xml("1");
    Xml expected = new Xml("2");
    // WHEN
    AssertionError error = expectAssertionError(() -> then(actual).usingComparator(ALWAY_DIFFERENT).isEqualTo(expected));
    // THEN
    then(error).hasMessage(format("%nexpected: %n" +
                                  "  <xml>%n" +
                                  "    <value>2</value>%n" +
                                  "  </xml>%n" +
                                  " but was: %n" +
                                  "  <xml>%n" +
                                  "    <value>1</value>%n" +
                                  "  </xml>%n" +
                                  "when comparing values using AlwaysDifferentComparator"));
  }

  @Test
  void should_display_multiline_values_nicely_for_ambiguous_representation_for_ambiguous_representation() {
    // GIVEN
    Xml actual = new Xml("1");
    XmlDuplicate expected = new XmlDuplicate("1");
    // WHEN
    AssertionError error = expectAssertionError(() -> then(actual).usingComparator(ALWAY_DIFFERENT).isEqualTo(expected));
    // THEN
    then(error).hasMessageContainingAll(format("%nexpected: %n" +
                                               "  <xml>%n" +
                                               "    <value>1</value>%n" +
                                               "  </xml> (XmlDuplicate@"),
                                        format("%n but was: %n" +
                                               "  <xml>%n" +
                                               "    <value>1</value>%n" +
                                               "  </xml> (Xml@"),
                                        "when comparing values using AlwaysDifferentComparator");
  }

  @Disabled("future improvement")
  @Test
  void should_format_iterable_with_one_element_per_line_when_single_line_description_is_too_long() {
    // GIVEN
    String aaa = "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa";
    String bbb = "bbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbb";
    // WHEN
    AssertionError error = expectAssertionError(() -> then(list(bbb, aaa)).isEqualTo(list(aaa, bbb)));
    // THEN
    then(error).hasMessage(format("%nexpected: %n"
                                  + "  [\"aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa\",%n"
                                  + "   \"bbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbb\"]%n"
                                  + " but was: %n"
                                  + "  [\"bbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbb\",%n"
                                  + "   \"aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa\"]"));
  }

  @Disabled("future improvement")
  @Test
  void should_format_array_with_one_element_per_line_when_single_line_description_is_too_long() {
    // GIVEN
    String aaa = "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa";
    String bbb = "bbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbb";
    // WHEN
    AssertionError error = expectAssertionError(() -> then(array(bbb, aaa)).isEqualTo(array(aaa, bbb)));
    // THEN
    then(error).hasMessage(format("%nexpected: %n"
                                  + "  [\"aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa\",%n"
                                  + "   \"bbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbb\"]%n"
                                  + " but was: %n"
                                  + "  [\"bbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbb\",%n"
                                  + "   \"aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa\"]"));
  }

  @Disabled("future improvement")
  @Test
  void should_display_iterable_with_multiline_element_values_nicely() {
    // GIVEN
    Xml xml1 = new Xml("1");
    Xml xml2 = new Xml("2");
    // WHEN
    AssertionError error = expectAssertionError(() -> then(list(xml2, xml1)).isEqualTo(list(xml1, xml2)));
    // THEN
    then(error).hasMessage(format("%nexpected: %n" +
                                  "  [<xml>%n" +
                                  "     <value>1</value>%n" +
                                  "   </xml>, %n" +
                                  "   <xml>%n" +
                                  "     <value>2</value>%n" +
                                  "   </xml>]%n" +
                                  " but was: [%n" +
                                  "  [<xml>%n" +
                                  "     <value>2</value>%n" +
                                  "   </xml>, %n" +
                                  "   <xml>%n" +
                                  "     <value>1</value>%n" +
                                  "   </xml>]"));
  }

  static class Xml {
    private final String value;

    public Xml(String value) {
      this.value = value;
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;
      Xml xml = (Xml) o;
      return Objects.equals(value, xml.value);
    }

    @Override
    public int hashCode() {
      return Objects.hash(value);
    }

    @Override
    public String toString() {
      return format("<xml>%n" +
                    "  <value>" + value + "</value>%n" +
                    "</xml>");
    }
  }

  // same representation for Xml2 as Xml
  static class XmlDuplicate extends Xml {
    public XmlDuplicate(String value) {
      super(value);
    }

    @Override
    public boolean equals(Object o) {
      // to test case where same toString but unequal values
      return false;
    }

  }

}
