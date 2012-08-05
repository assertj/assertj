package org.fest.assertions.api.date;

import static org.junit.Assert.assertSame;

import org.fest.assertions.api.DateAssertBaseTest;
import org.fest.assertions.api.DateAssert;
import org.fest.assertions.internal.Dates;
import org.junit.Before;
import org.junit.Test;

/**
 * 
 * Abstract class that factorize DateAssert tests with an int arg.
 * <p>
 * For the most part, date assertion tests are (whatever the concrete date assertion method invoked is) :
 * <ul>
 * <li>successfull assertion test with an int</li>
 * <li>checking that DateAssert instance used for assertions is returned to allow fluent assertions chaining</li>
 * </ul>
 * 
 * Subclasses are expected to define what is the invoked assertion method.
 * 
 * @author Joel Costigliola
 * 
 */
public abstract class AbstractDateAssertWithOneIntArg_Test extends DateAssertBaseTest {

  protected int intArg;

  @Override
  @Before
  public void setUp() {
    super.setUp();
    intArg = 5;
  }

  @Test
  public void should_verify_assertion_with_int_arg() {
    assertionInvocationWithOneIntArg();
    verifyAssertionInvocation();
  }

  @Test
  public void should_return_this() {
    DateAssert returned = assertionInvocationWithOneIntArg();
    assertSame(assertions, returned);
  }

  /**
   * Must be overriden to invoke the {@link DateAssert} assertion method under test with the {@link #intArg} attribute.
   * <p>
   * example with <code>isWithinMonth</code> date assertion:<br>
   * <code>assertions.isWithinMonth(5);</code>
   * 
   * @return the DateAssert instance called
   */
  protected abstract DateAssert assertionInvocationWithOneIntArg();

  /**
   * Must be overriden to verify that the {@link Dates} assertion method was invoked with the {@link #intArg} attribute.
   * <p>
   * example with <code>isWithinMonth</code> date assertion:<br>
   * <code>verify(dates).isWithinMonth(getInfo(assertions), getActual(assertions), intArg);</code>
   * 
   */
  protected abstract void verifyAssertionInvocation();
}
