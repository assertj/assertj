package org.assertj.core.api;

import static org.mockito.Mockito.mock;

import java.text.DateFormat;
import java.util.Date;

import org.assertj.core.internal.Dates;
import org.assertj.core.internal.Objects;
import org.junit.Before;

/**
 * 
 * Abstract base of all DateAssert tests.
 * 
 * @author Joel Costigliola
 * 
 */
public abstract class DateAssertBaseTest {

  protected DateAssert assertions;
  protected Dates dates;
  protected Objects objects;

  @Before
  public void setUp() {
    dates = mock(Dates.class);
    objects = mock(Objects.class);
    assertions = new DateAssert(new Date());
    assertions.dates = dates;
    assertions.objects = objects;
  }
  
  protected Date parse(String dateAsString) {
    return assertions.parse(dateAsString);
  }

  protected AssertionInfo getInfo(DateAssert someAssertions) {
    return someAssertions.info;
  }

  protected Date getActual(DateAssert someAssertions) {
    return someAssertions.actual;
  }
  
  protected Objects getObjects(DateAssert someAssertions) {
    return someAssertions.objects;
  }
  
  protected Dates getDates(DateAssert someAssertions) {
    return someAssertions.dates;
  }
}
