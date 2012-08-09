package org.fest.assertions.internal;

import static org.fest.assertions.test.ExpectedException.none;

import static org.mockito.Mockito.spy;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;

import org.fest.assertions.core.TestCondition;
import org.fest.assertions.test.ExpectedException;

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