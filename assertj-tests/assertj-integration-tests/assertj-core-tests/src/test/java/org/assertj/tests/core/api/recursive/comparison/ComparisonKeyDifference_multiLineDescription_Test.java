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
package org.assertj.tests.core.api.recursive.comparison;

import static java.lang.String.format;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.util.Lists.list;

import org.assertj.core.api.recursive.comparison.ComparisonDifference;
import org.assertj.core.api.recursive.comparison.ComparisonKeyDifference;
import org.assertj.core.api.recursive.comparison.DualValue;
import org.junit.jupiter.api.Test;

class ComparisonKeyDifference_multiLineDescription_Test {

  @Test
  void should_build_a_multiline_description() {
    // GIVEN
    DualValue dualValue = new DualValue(list("a", "b"), "foo", "bar");
    ComparisonDifference comparisonDifference = new ComparisonKeyDifference(dualValue, "k1", "k2");
    // WHEN
    String multiLineDescription = comparisonDifference.multiLineDescription();
    // THEN
    assertThat(multiLineDescription).isEqualTo(format("field/property 'a.b' differ:%n" +
                                                      "- actual value  : \"foo\"%n" +
                                                      "- expected value: \"bar\"%n" +
                                                      "map key difference:%n" +
                                                      "- actual key  : \"k1\"%n" +
                                                      "- expected key: \"k2\""));
  }

  @Test
  void multiline_description_should_indicate_top_level_objects_difference() {
    // GIVEN
    ComparisonDifference comparisonDifference = new ComparisonKeyDifference(new DualValue(list(), "foo", "bar"), "k1", "k2");
    // WHEN
    String multiLineDescription = comparisonDifference.multiLineDescription();
    // THEN
    assertThat(multiLineDescription).isEqualTo(format("Top level actual and expected objects differ:%n" +
                                                      "- actual value  : \"foo\"%n" +
                                                      "- expected value: \"bar\"%n" +
                                                      "map key difference:%n" +
                                                      "- actual key  : \"k1\"%n" +
                                                      "- expected key: \"k2\""));
  }

  @Test
  void should_build_multiline_description_containing_percent() {
    // GIVEN
    DualValue dualValue = new DualValue(list("a", "b"), "foo%", "%bar%%");
    ComparisonDifference com = new ComparisonKeyDifference(dualValue, "%k1", "%k2%%");
    // THEN
    assertThat(com.multiLineDescription()).isEqualTo(format("field/property 'a.b' differ:%n" +
                                                            "- actual value  : \"foo%%\"%n" +
                                                            "- expected value: \"%%bar%%%%\"%n" +
                                                            "map key difference:%n" +
                                                            "- actual key  : \"%%k1\"%n" +
                                                            "- expected key: \"%%k2%%%%\""));
  }
}
