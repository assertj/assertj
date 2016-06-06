package org.assertj.core.api;

import java.util.List;

import org.assertj.core.util.Lists;
import org.junit.Test;
import org.junit.runners.model.MultipleFailureException;

import static java.lang.String.format;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

public class Java6JUnitSoftAssertionsFailureTest {
  // we cannot make it a rule here, because we need to test the failure without this test failing!
  // @Rule
  public final Java6JUnitSoftAssertions softly = new Java6JUnitSoftAssertions();

  @Test
  public void should_report_all_errors() throws Throwable {
    try {
      softly.assertThat(1).isEqualTo(1);
      softly.assertThat(1).isEqualTo(2);
      softly.assertThat(Lists.newArrayList(1, 2)).containsOnly(1, 3);
      MultipleFailureException.assertEmpty(softly.errorsCollected());
      fail("Should not reach here");
    } catch (MultipleFailureException e) {
      List<Throwable> failures = e.getFailures();

      assertThat(failures).hasSize(2)
          .extracting("message")
          .contains("expected:<[2]> but was:<[1]>",
              format("%n" +
                  "Expecting:%n" +
                  "  <[1, 2]>%n" +
                  "to contain only:%n" +
                  "  <[1, 3]>%n" +
                  "elements not found:%n" +
                  "  <[3]>%n" +
                  "and elements not expected:%n" +
                  "  <[2]>%n"));
    }
  }
}
