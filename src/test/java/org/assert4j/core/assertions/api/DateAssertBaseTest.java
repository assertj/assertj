package org.assert4j.core.assertions.api;

import static org.mockito.Mockito.mock;

import java.text.DateFormat;
import java.util.Date;

import org.assert4j.core.assertions.api.DateAssert;
import org.assert4j.core.assertions.core.AssertionInfo;
import org.assert4j.core.assertions.internal.Dates;
import org.assert4j.core.assertions.internal.Objects;
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
  
  protected DateFormat dateFormat() {
    return DateAssert.dateFormat;
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
