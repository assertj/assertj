package org.assertj.core.internal;

import static org.assertj.core.test.ExpectedException.none;

import static org.mockito.Mockito.spy;

import org.assertj.core.api.TestCondition;
import org.assertj.core.internal.Conditions;
import org.assertj.core.internal.Failures;
import org.assertj.core.test.ExpectedException;
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