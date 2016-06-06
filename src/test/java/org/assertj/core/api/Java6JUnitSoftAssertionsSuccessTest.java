package org.assertj.core.api;

import org.junit.Rule;
import org.junit.Test;

import static org.assertj.core.util.Lists.newArrayList;

public class Java6JUnitSoftAssertionsSuccessTest {
  @Rule
  public final Java6JUnitSoftAssertions softly = new Java6JUnitSoftAssertions();

  @Test
  public void all_assertions_should_pass() throws Throwable {
    softly.assertThat(1).isEqualTo(1);
    softly.assertThat(newArrayList(1, 2)).containsOnly(1, 2);
  }
}
