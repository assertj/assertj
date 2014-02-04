package org.assertj.core.internal;

import static org.mockito.Mockito.spy;

import org.assertj.core.api.Condition;
import org.assertj.core.api.TestCondition;
import org.assertj.core.condition.JediCondition;
import org.assertj.core.condition.JediPowerCondition;
import org.assertj.core.internal.Conditions;
import org.assertj.core.internal.Iterables;
import org.junit.Before;


/**
 * Base class for testing <code>{@link Iterables}</code> with {@link Conditions}.
 * <p>
 * Is in <code>org.assertj.core.internal</code> package to be able to set {@link Iterables#conditions} appropriately.
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