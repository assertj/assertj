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
package org.assertj.tests.core.api.recursive.comparison;

import static java.lang.String.format;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.util.Lists.list;
import static org.assertj.tests.core.api.recursive.data.DualValueUtil.rootDualValue;

import org.assertj.core.api.recursive.comparison.ComparisonKeyDifference;
import org.assertj.core.api.recursive.comparison.DualValue;
import org.assertj.core.api.recursive.comparison.FieldLocation;
import org.junit.jupiter.api.Test;

class ComparisonKeyDifference_multiLineDescription_Test {

  @Test
  void should_build_a_multiline_description() {
    // GIVEN
    var dualValue = new DualValue(new FieldLocation(list("a", "b")), "foo", "bar", null);
    var comparisonKeyDifference = new ComparisonKeyDifference(dualValue, "k1", "k2");
    // WHEN
    String multiLineDescription = comparisonKeyDifference.multiLineDescription();
    // THEN
    then(multiLineDescription).isEqualTo(format("field/property 'a.b' differ:%n" +
                                                "- actual value  : \"foo\"%n" +
                                                "- expected value: \"bar\"%n" +
                                                "map key difference:%n" +
                                                "- actual key  : \"k1\"%n" +
                                                "- expected key: \"k2\""));
  }

  @Test
  void multiline_description_should_indicate_top_level_objects_difference() {
    // GIVEN
    var comparisonKeyDifference = new ComparisonKeyDifference(rootDualValue("foo", "bar"), "k1", "k2");
    // WHEN
    String multiLineDescription = comparisonKeyDifference.multiLineDescription();
    // THEN
    then(multiLineDescription).isEqualTo(format("Top level actual and expected objects differ:%n" +
                                                "- actual value  : \"foo\"%n" +
                                                "- expected value: \"bar\"%n" +
                                                "map key difference:%n" +
                                                "- actual key  : \"k1\"%n" +
                                                "- expected key: \"k2\""));
  }

  @Test
  void should_build_multiline_description_containing_percent() {
    // GIVEN
    var dualValue = new DualValue(new FieldLocation(list("a", "b")), "foo%", "%bar%%", null);
    var comparisonKeyDifference = new ComparisonKeyDifference(dualValue, "%k1", "%k2%%");
    // THEN
    then(comparisonKeyDifference.multiLineDescription()).isEqualTo(format("field/property 'a.b' differ:%n" +
                                                                          "- actual value  : \"foo%%\"%n" +
                                                                          "- expected value: \"%%bar%%%%\"%n" +
                                                                          "map key difference:%n" +
                                                                          "- actual key  : \"%%k1\"%n" +
                                                                          "- expected key: \"%%k2%%%%\""));
  }
}
