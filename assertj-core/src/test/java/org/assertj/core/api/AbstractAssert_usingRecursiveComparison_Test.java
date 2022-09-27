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
package org.assertj.core.api;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.BDDAssertions.*;
import static org.assertj.core.presentation.UnicodeRepresentation.UNICODE_REPRESENTATION;
import static org.assertj.core.util.AssertionsUtil.expectAssertionError;

import org.assertj.core.api.recursive.comparison.RecursiveComparisonConfiguration;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

class AbstractAssert_usingRecursiveComparison_Test {

  @Test
  void should_honor_test_description() {
    // GIVEN
    AbstractAssert<?, ?> assertion = assertThat("foo");
    // WHEN
    AssertionError error = expectAssertionError(() -> assertion.as("test description")
                                                               .usingRecursiveComparison()
                                                               .isEqualTo("bar"));
    // THEN
    then(error).hasMessageContaining("[test description]");
  }

  @Test
  void should_honor_representation() {
    // GIVEN
    AbstractAssert<?, ?> assertion = assertThat("foo");
    // WHEN
    RecursiveComparisonAssert<?> recursiveAssertion = assertion.withRepresentation(UNICODE_REPRESENTATION)
                                                               .usingRecursiveComparison();
    // THEN
    then(recursiveAssertion.info.representation()).isEqualTo(UNICODE_REPRESENTATION);
  }

  @Test
  void should_honor_overridden_error_message() {
    // GIVEN
    AbstractAssert<?, ?> assertion = assertThat("foo");
    String errorMessage = "boom";
    // WHEN
    RecursiveComparisonAssert<?> recursiveAssertion = assertion.overridingErrorMessage(errorMessage)
                                                               .usingRecursiveComparison();
    // THEN
    then(recursiveAssertion.info.overridingErrorMessage()).isEqualTo(errorMessage);
  }

  @Test
  void should_honor_ignored_fields() {
    // GIVEN
    Data actual = new Data(new Data.InnerData("match", "nonMatch"), null);
    Data expected = new Data(new Data.InnerData("match", "hctaMnon"), null);
    RecursiveComparisonConfiguration conf = new RecursiveComparisonConfiguration();
    conf.ignoreFields("field1.field12");
    AbstractAssert<?, ?> assertion = assertThat(actual);
    // WHEN
    RecursiveComparisonAssert<?> recursiveAssertion = assertion.usingRecursiveComparison(conf);
    // THEN
    thenCode(() -> recursiveAssertion.isEqualTo(expected)).doesNotThrowAnyException();
  }

  @Test
  void should_honor_ignored_fields_regex_in_inner_list() {
    // GIVEN
    Data actual = new Data(null, Collections.singletonList(new Data.InnerData("match", "nonMatch")));
    Data expected = new Data(null, Collections.singletonList(new Data.InnerData("match", "hctaMnon")));
    RecursiveComparisonConfiguration conf = new RecursiveComparisonConfiguration();
    conf.ignoreFieldsMatchingRegexes(".*field12");
    AbstractAssert<?, ?> assertion = assertThat(actual);
    // WHEN
    RecursiveComparisonAssert<?> recursiveAssertion = assertion.usingRecursiveComparison(conf);
    // THEN
    thenCode(() -> recursiveAssertion.isEqualTo(expected)).doesNotThrowAnyException();
  }

  @Test
  void should_honor_ignored_fields_regex() {
    // GIVEN
    Data actual = new Data(new Data.InnerData("match", "nonMatch"), null);
    Data expected = new Data(new Data.InnerData("match", "hctaMnon"), null);
    RecursiveComparisonConfiguration conf = new RecursiveComparisonConfiguration();
    conf.ignoreFieldsMatchingRegexes(".*field12");
    AbstractAssert<?, ?> assertion = assertThat(actual);
    // WHEN
    RecursiveComparisonAssert<?> recursiveAssertion = assertion.usingRecursiveComparison(conf);
    // THEN
    thenCode(() -> recursiveAssertion.isEqualTo(expected)).doesNotThrowAnyException();
  }

  static class Data {
    private InnerData field1;

    public Data(InnerData field1, List<InnerData> field2) {
      this.field1 = field1;
    }

    public InnerData getField1() {
      return field1;
    }

    public void setField1(InnerData field1) {
      this.field1 = field1;
    }

    static class InnerData {
      private String field11;
      private String field12;

      public InnerData(String field11, String field12) {
        this.field11 = field11;
        this.field12 = field12;
      }

      public String getField12() {
        return field12;
      }

      public void setField12(String field12) {
        this.field12 = field12;
      }

      public String getField11() {
        return field11;
      }

      public void setField11(String field11) {
        this.field11 = field11;
      }
    }
  }
}
