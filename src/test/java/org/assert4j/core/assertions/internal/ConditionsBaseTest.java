package org.assert4j.core.assertions.internal;

import static org.assert4j.core.assertions.test.ExpectedException.none;

import static org.mockito.Mockito.spy;

import org.assert4j.core.assertions.core.TestCondition;
import org.assert4j.core.assertions.internal.Conditions;
import org.assert4j.core.assertions.internal.Failures;
import org.assert4j.core.assertions.test.ExpectedException;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;


public class ConditionsBaseTest {

  protected static Object actual;
  @Rule
  public ExpectedException thrown = none();

  @BeforeClass
  public static void setUpOnce() {
    actual = "Yoda";
  }

  protected Failures failures;
  protected TestCondition<Object> condition;
  protected Conditions conditions;

  public ConditionsBaseTest() {
    super();
  }

  @Before
  public void setUp() {
    failures = spy(new Failures());
    condition = new TestCondition<Object>();
    conditions = new Conditions();
    conditions.failures = failures;
  }

}