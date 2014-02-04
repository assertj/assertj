package org.assertj.core.error;

import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.error.ShouldBeInSameHourWindow.shouldBeInSameHourWindow;
import static org.assertj.core.util.Dates.parseDatetimeWithMs;

import java.text.ParseException;

import org.assertj.core.description.Description;
import org.assertj.core.presentation.StandardRepresentation;
import org.junit.Test;

import org.assertj.core.description.TextDescription;


/**
 * Tests for <code>{@link ShouldBeInSameHourWindow#create(Description, org.assertj.core.presentation.Representation)}</code>.
 *
 * @author Joel Costigliola
 * @author Mikhail Mazursky
 */
public class ShouldBeInSameHourWindow_create_Test {

  @Test
  public void should_create_error_message() throws ParseException {
    ErrorMessageFactory factory = shouldBeInSameHourWindow(parseDatetimeWithMs("2011-01-01T05:00:00.000"),
                                                           parseDatetimeWithMs("2011-01-01T06:05:17.003"));

    String message = factory.create(new TextDescription("Test"), new StandardRepresentation());
    assertThat(message).isEqualTo("[Test] \nExpecting:\n  <2011-01-01T05:00:00>\nto be close to:\n  " +
                                    "<2011-01-01T06:05:17>\n" +
                                    "by less than one hour (strictly) but difference was: 1h 5m 17s and 3ms");
  }

}
