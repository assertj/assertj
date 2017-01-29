/**
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 *
 * Copyright 2012-2017 the original author or authors.
 */
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

  @Before
  public void setUp() {
    failures = spy(new Failures());
    condition = new TestCondition<>();
    conditions = new Conditions();
    conditions.failures = failures;
  }

}