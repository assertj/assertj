package org.assertj.core.error;

import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.error.ShouldBeEqualWithTimePrecision.shouldBeEqual;
import static org.assertj.core.util.Dates.parseDatetimeWithMs;

import java.text.ParseException;
import java.util.concurrent.TimeUnit;

import org.assertj.core.presentation.StandardRepresentation;
import org.junit.Test;

import org.assertj.core.description.TextDescription;


/**
 * Tests for <code>{@link ShouldBeEqualWithTimePrecision#create(org.assertj.core.description.Description, org.assertj.core.presentation.Representation)}</code>.
 *
 * @author Joel Costigliola
 */
public class ShouldBeEqualWithTimePrecision_create_Test {

  @Test
  public void should_create_error_message_ignoring_millisseconds() throws ParseException {
    ErrorMessageFactory factory = shouldBeEqual(parseDatetimeWithMs("2011-01-01T05:00:00.000"),
                                                parseDatetimeWithMs("2011-01-01T06:05:17.003"), TimeUnit.MILLISECONDS);

    String message = factory.create(new TextDescription("Test"), new StandardRepresentation());
    assertThat(message).isEqualTo("[Test] \nExpecting:\n  <2011-01-01T05:00:00>\n" +
                                    "to have same year, month, day, hour, minute and second as:\n  " +
                                    "<2011-01-01T06:05:17>\n" +
                                    "but had not.");
  }

  @Test
  public void should_create_error_message_ignoring_seconds() throws ParseException {
    ErrorMessageFactory factory = shouldBeEqual(parseDatetimeWithMs("2011-01-01T05:00:00.000"),
                                                parseDatetimeWithMs("2011-01-01T06:05:17.003"), TimeUnit.SECONDS);

    String message = factory.create(new TextDescription("Test"), new StandardRepresentation());
    assertThat(message).isEqualTo("[Test] \nExpecting:\n  <2011-01-01T05:00:00>\n" +
                                    "to have same year, month, day, hour and minute as:\n  " +
                                    "<2011-01-01T06:05:17>\n" +
                                    "but had not.");
  }

  @Test
  public void should_create_error_message_ignoring_miinutes() throws ParseException {
    ErrorMessageFactory factory = shouldBeEqual(parseDatetimeWithMs("2011-01-01T05:00:00.000"),
                                                parseDatetimeWithMs("2011-01-01T06:05:17.003"), TimeUnit.MINUTES);

    String message = factory.create(new TextDescription("Test"), new StandardRepresentation());
    assertThat(message).isEqualTo("[Test] \nExpecting:\n  <2011-01-01T05:00:00>\n" +
                                    "to have same year, month, day and hour as:\n  " +
                                    "<2011-01-01T06:05:17>\n" +
                                    "but had not.");
  }

  @Test
  public void should_create_error_message_ignoring_hours() throws ParseException {
    ErrorMessageFactory factory = shouldBeEqual(parseDatetimeWithMs("2011-01-01T05:00:00.000"),
                                                parseDatetimeWithMs("2011-01-01T06:05:17.003"), TimeUnit.HOURS);

    String message = factory.create(new TextDescription("Test"), new StandardRepresentation());
    assertThat(message).isEqualTo("[Test] \nExpecting:\n  <2011-01-01T05:00:00>\n" +
                                    "to have same year, month and day as:\n  " +
                                    "<2011-01-01T06:05:17>\n" +
                                    "but had not.");
  }

}
