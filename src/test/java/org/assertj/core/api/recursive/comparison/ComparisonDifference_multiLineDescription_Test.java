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
