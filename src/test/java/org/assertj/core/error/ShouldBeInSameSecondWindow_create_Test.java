package org.assertj.core.error;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.error.ShouldBeInSameSecondWindow.shouldBeInSameSecondWindow;
import static org.assertj.core.util.Dates.parseDatetimeWithMs;

import java.text.ParseException;

import org.junit.Test;

import org.assertj.core.description.Description;
import org.assertj.core.description.TextDescription;


/**
 * Tests for <code>{@link ShouldBeInSameSecondWindow#create(Description)}</code>.
 *
 * @author Joel Costigliola
 * @author Mikhail Mazursky
 */
public class ShouldBeInSameSecondWindow_create_Test {

  @Test
  public void should_create_error_message() throws ParseException {
    ErrorMessageFactory factory = shouldBeInSameSecondWindow(parseDatetimeWithMs("2011-01-01T05:00:01.000"),
                                                             parseDatetimeWithMs("2011-01-01T05:00:02.001"));
    String message = factory.create(new TextDescription("Test"));
    assertThat(message).isEqualTo("[Test] \nExpecting:\n  <2011-01-01T05:00:01>\nto be close to:\n  " +
                                    "<2011-01-01T05:00:02>\nby less than one second (strictly) but difference was: 1s" +
                                    " and 1ms");
  }

}
