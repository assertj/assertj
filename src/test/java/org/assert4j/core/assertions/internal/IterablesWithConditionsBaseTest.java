package org.assert4j.core.assertions.internal;

import static org.mockito.Mockito.spy;

import org.assert4j.core.assertions.condition.JediCondition;
import org.assert4j.core.assertions.condition.JediPowerCondition;
import org.assert4j.core.assertions.core.Condition;
import org.assert4j.core.assertions.core.TestCondition;
import org.assert4j.core.assertions.internal.Conditions;
import org.assert4j.core.assertions.internal.Iterables;
import org.junit.Before;


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