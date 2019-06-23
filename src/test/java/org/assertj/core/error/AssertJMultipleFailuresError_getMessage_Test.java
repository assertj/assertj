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
 * Copyright 2012-2019 the original author or authors.
 */
package org.assertj.core.error;

import static java.lang.String.format;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.util.AssertionsUtil.expectAssertionError;
import static org.assertj.core.util.Lists.list;

import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Test;

public class AssertJMultipleFailuresError_getMessage_Test {

  @Test
  void should_honor_description() {
    // GIVEN
    String description = "desc";
    AssertJMultipleFailuresError error = new AssertJMultipleFailuresError(description, list(new AssertionError("boom")));
    // WHEN
    String message = error.getMessage();
    // THEN
    assertThat(message).startsWith(description);
  }

  @Test
  public void should_include_errors_count_and_clearly_separate_error_messages() {
    // GIVEN
    SoftAssertions softly = new SoftAssertions();
    softly.assertThat(list("")).isEmpty();
    softly.assertThat(list("a", "b", "c")).as("isEmpty list").isEmpty();
    softly.assertThat("abc").isEmpty();
    softly.assertThat("abc").as("isEmpty string").isEmpty();
    softly.assertThat("abc").isEqualTo("bcd");
    softly.assertThat("abc").as("isEqualTo").isEqualTo("bcd");
    softly.assertThat(list("a", "b", "c")).as("contains").contains("e").doesNotContain("a");
    softly.assertThat(list("a", "b", "c")).contains("e").doesNotContain("a");
    // WHEN
    AssertionError error = expectAssertionError(() -> softly.assertAll());
    // THEN
    assertThat(error).hasMessage(format("Multiple Failures (10 failures)%n" +
                                        "-- failure 1 --%n" +
                                        "Expecting empty but was:<[\"\"]>%n" +
                                        "-- failure 2 --%n" +
                                        "[isEmpty list] %n" +
                                        "Expecting empty but was:<[\"a\", \"b\", \"c\"]>%n" +
                                        "-- failure 3 --%n" +
                                        "Expecting empty but was:<\"abc\">%n" +
                                        "-- failure 4 --%n" +
                                        "[isEmpty string] %n" +
                                        "Expecting empty but was:<\"abc\">%n" +
                                        "-- failure 5 --%n" +
                                        "Expecting:%n" +
                                        " <\"abc\">%n" +
                                        "to be equal to:%n" +
                                        " <\"bcd\">%n" +
                                        "but was not.%n" +
                                        "-- failure 6 --%n" +
                                        "[isEqualTo] %n" +
                                        "Expecting:%n" +
                                        " <\"abc\">%n" +
                                        "to be equal to:%n" +
                                        " <\"bcd\">%n" +
                                        "but was not.%n" +
                                        "-- failure 7 --%n" +
                                        "[contains] %n" +
                                        "Expecting:%n" +
                                        " <[\"a\", \"b\", \"c\"]>%n" +
                                        "to contain:%n" +
                                        " <[\"e\"]>%n" +
                                        "but could not find:%n" +
                                        " <[\"e\"]>%n" +
                                        "%n" +
                                        "-- failure 8 --%n" +
                                        "[contains] %n" +
                                        "Expecting%n" +
                                        " <[\"a\", \"b\", \"c\"]>%n" +
                                        "not to contain%n" +
                                        " <[\"a\"]>%n" +
                                        "but found%n" +
                                        " <[\"a\"]>%n" +
                                        "%n" +
                                        "-- failure 9 --%n" +
                                        "Expecting:%n" +
                                        " <[\"a\", \"b\", \"c\"]>%n" +
                                        "to contain:%n" +
                                        " <[\"e\"]>%n" +
                                        "but could not find:%n" +
                                        " <[\"e\"]>%n" +
                                        "%n" +
                                        "-- failure 10 --%n" +
                                        "Expecting%n" +
                                        " <[\"a\", \"b\", \"c\"]>%n" +
                                        "not to contain%n" +
                                        " <[\"a\"]>%n" +
                                        "but found%n" +
                                        " <[\"a\"]>%n"));
  }

}
