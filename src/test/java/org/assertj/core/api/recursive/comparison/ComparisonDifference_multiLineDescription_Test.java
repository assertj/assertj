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
 * Copyright 2012-2018 the original author or authors.
 */
package org.assertj.core.api.recursive.comparison;

import static java.lang.String.format;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.util.Lists.list;

import org.junit.jupiter.api.Test;

public class ComparisonDifference_multiLineDescription_Test {

  @Test
  public void should_build_comparison_difference_multiline_description() {
    // GIVEN
    ComparisonDifference com = new ComparisonDifference(list("a", "b"), "foo", "bar");

    // THEN
    assertThat(com.multiLineDescription()).isEqualTo(format("field/property 'a.b' differ:%n" +
                                                            "- actual value   : \"foo\"%n" +
                                                            "- expected value : \"bar\""));
  }

  @Test
  public void should_build_comparison_difference_multiline_description_with_additional_information() {
    // GIVEN
    ComparisonDifference com = new ComparisonDifference(list("a", "b"), "foo", "bar", "additional information");

    // THEN
    assertThat(com.multiLineDescription()).isEqualTo(format("field/property 'a.b' differ:%n" +
                                                            "- actual value   : \"foo\"%n" +
                                                            "- expected value : \"bar\"%n" +
                                                            "additional information"));
  }

  @Test
  public void should_build_multiline_description_containing_percent() {
    // GIVEN
    ComparisonDifference com = new ComparisonDifference(list("a", "b"), "foo%", "%bar%%", "%additional %information%");

    // THEN
    assertThat(com.multiLineDescription()).isEqualTo(format("field/property 'a.b' differ:%n" +
                                                            "- actual value   : \"foo%%\"%n" +
                                                            "- expected value : \"%%bar%%%%\"%n" +
                                                            "%%additional %%information%%"));
  }

}
