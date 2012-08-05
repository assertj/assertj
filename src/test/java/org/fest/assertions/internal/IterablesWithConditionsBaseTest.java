package org.fest.assertions.internal;

import static org.mockito.Mockito.spy;

import org.junit.Before;

import org.fest.assertions.condition.JediCondition;
import org.fest.assertions.condition.JediPowerCondition;
import org.fest.assertions.core.Condition;
import org.fest.assertions.core.TestCondition;

/**
 * Base class for testing <code>{@link Iterables}</code> with {@link Conditions}.
 * <p>
 * Is in <code>org.fest.assertions.internal</code> package to be able to set {@link Iterables#conditions} appropriately.
 * 
 * @author Joel Costigliola
 */
public class IterablesWithConditionsBaseTest extends IterablesBaseTest {

  protected Condition<String> jediPower;
  protected Condition<String> jedi;
  protected TestCondition<Object> testCondition;
  protected Conditions conditions;

  @Override
  @Before
  public void setUp() {
    super.setUp();
    testCondition = new TestCondition<Object>();
    conditions = spy(new Conditions());
    jedi = new JediCondition();
    jediPower = new JediPowerCondition();
    iterables.conditions = conditions;
  }

}