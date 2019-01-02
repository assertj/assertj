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
  public void should_show_that_null_fields_are_ignored() {
    // WHEN
    recursiveComparisonConfiguration.setIgnoreAllActualNullFields(true);
    String multiLineDescription = recursiveComparisonConfiguration.multiLineDescription(STANDARD_REPRESENTATION);
    // THEN
    assertThat(multiLineDescription).contains(format("- all actual null fields were ignored in the comparison%n"));
  }

  @Test
  public void should_show_that_some_given_fields_are_ignored() {
    // WHEN
    recursiveComparisonConfiguration.ignoreFields("foo", "bar", "foo.bar");
    String multiLineDescription = recursiveComparisonConfiguration.multiLineDescription(STANDARD_REPRESENTATION);
    // THEN
    assertThat(multiLineDescription).contains(format("- the following fields were ignored in the comparison: foo, bar, foo.bar%n"));
  }

  @Test
  public void should_show_the_regexes_used_to_ignore_fields() {
    // WHEN
    recursiveComparisonConfiguration.ignoreFieldsByRegexes("foo", "bar", "foo.bar");
    String multiLineDescription = recursiveComparisonConfiguration.multiLineDescription(STANDARD_REPRESENTATION);
    // THEN
    assertThat(multiLineDescription).contains(format("- the fields matching the following regexes were ignored in the comparison: foo, bar, foo.bar%n"));
  }

  @Test
  public void should_show_the_ignored_overridden_equals_methods_regexes() {
    // WHEN
    recursiveComparisonConfiguration.ignoreCustomEqualsByRegexes("foo", "bar", "foo.bar");
    String multiLineDescription = recursiveComparisonConfiguration.multiLineDescription(STANDARD_REPRESENTATION);
    // THEN
    // @format:off
    assertThat(multiLineDescription).contains(format("- overridden equals methods were used in the comparison, except for:%n" +
                                                     "--- the types matching the following regexes: foo, bar, foo.bar%n"));
    // @format:on

  }

  @Test
  public void should_show_a_complete_multiline_description() {
    // WHEN
    recursiveComparisonConfiguration.setIgnoreAllActualNullFields(true);
    recursiveComparisonConfiguration.ignoreFields("foo", "bar", "foo.bar");
    recursiveComparisonConfiguration.ignoreFieldsByRegexes("f.*", ".ba.", "..b%sr..");
    recursiveComparisonConfiguration.ignoreCustomEqualsByRegexes(".*oo", ".ar", "oo.ba");
    String multiLineDescription = recursiveComparisonConfiguration.multiLineDescription(STANDARD_REPRESENTATION);
    // THEN
    // @format:off
    assertThat(multiLineDescription).isEqualTo(format("- all actual null fields were ignored in the comparison%n" +
                                                      "- the following fields were ignored in the comparison: foo, bar, foo.bar%n" +
                                                      "- the fields matching the following regexes were ignored in the comparison: f.*, .ba., ..b%%sr..%n"+
                                                      "- overridden equals methods were used in the comparison, except for:%n" +
                                                      "--- the types matching the following regexes: .*oo, .ar, oo.ba%n"));
    // @format:on
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
