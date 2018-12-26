package org.assertj.core.api.recursive.comparison;

import static java.lang.String.format;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.presentation.StandardRepresentation.STANDARD_REPRESENTATION;
import static org.assertj.core.util.Lists.list;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class RecursiveComparisonConfiguration_multiLineDescription_Test {

  private RecursiveComparisonConfiguration recursiveComparisonConfiguration;

  @BeforeEach
  public void setup() {
    recursiveComparisonConfiguration = new RecursiveComparisonConfiguration();
  }

  @Test
  public void should_show_that_null_fields_are_ignored_in_the_multiline_description() {
    // WHEN
    recursiveComparisonConfiguration.setIgnoreAllActualNullFields(true);
    String multiLineDescription = recursiveComparisonConfiguration.multiLineDescription(STANDARD_REPRESENTATION);
    // THEN
    assertThat(multiLineDescription).isEqualTo(format("- all actual null fields were ignored in the comparison%n"));
  }

  @Test
  public void should_show_that_some_given_fields_are_ignored_in_the_multiline_description() {
    // WHEN
    recursiveComparisonConfiguration.ignoreFields("foo", "bar", "foo.bar");
    String multiLineDescription = recursiveComparisonConfiguration.multiLineDescription(STANDARD_REPRESENTATION);
    // THEN
    assertThat(multiLineDescription).isEqualTo(format("- the following fields were ignored in the comparison: foo, bar, foo.bar%n"));
  }

  @Test
  public void should_show_a_complete_multiline_description() {
    // WHEN
    recursiveComparisonConfiguration.setIgnoreAllActualNullFields(true);
    recursiveComparisonConfiguration.ignoreFields("foo", "bar", "foo.bar");
    String multiLineDescription = recursiveComparisonConfiguration.multiLineDescription(STANDARD_REPRESENTATION);
    // THEN
    assertThat(multiLineDescription).isEqualTo(format("- all actual null fields were ignored in the comparison%n" +
                                                      "- the following fields were ignored in the comparison: foo, bar, foo.bar%n"));
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
