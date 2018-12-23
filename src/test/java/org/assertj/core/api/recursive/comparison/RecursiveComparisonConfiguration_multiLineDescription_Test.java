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
  public void should_build_a_readable_multiline_description() {
    // WHEN
    recursiveComparisonConfiguration.setIgnoreAllActualNullFields(true);
    String multiLineDescription = recursiveComparisonConfiguration.multiLineDescription(STANDARD_REPRESENTATION);
    // THEN
    assertThat(multiLineDescription).isEqualTo("- all actual null fields were ignored in the comparison");
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
