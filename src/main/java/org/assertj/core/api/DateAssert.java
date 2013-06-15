package org.assertj.core.api;

import java.text.DateFormat;
import java.util.Date;


/**
 * Assertions for {@link Date}s.
 * <p>
 * To create a new instance of this class invoke <code>{@link Assertions#assertThat(Date)}</code>.
 * </p>
 * Note that assertions with date parameter comes with two flavor, one is obviously a {@link Date} and the other is a String
 * representing a Date.<br>
 * For the latter, the default format follows ISO 8901 : "yyyy-MM-dd", user can override it with a custom format by calling
 * {@link #withDateFormat(DateFormat)}.<br>
 * The user custom format will then be used for all next Date assertions (i.e not limited to the current assertion) in the test
 * suite.<br>
 * To turn back to default format, simply call {@link #withIsoDateFormat()}.
 * 
 * @author Tomasz Nurkiewicz (thanks for giving assertions idea)
 * @author Joel Costigliola
 * @author Mikhail Mazursky
 */
public class DateAssert extends AbstractDateAssert<DateAssert> {

  /**
   * Creates a new {@link DateAssert}.
   * @param actual the target to verify.
   */
  protected DateAssert(Date actual) {
    super(actual, DateAssert.class);
  }
}
