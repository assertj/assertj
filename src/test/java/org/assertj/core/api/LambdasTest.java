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
 * Copyright 2012-2014 the original author or authors.
 */
package org.assertj.core.api;

import org.junit.Before;
import org.junit.Test;

/**
 * Tests for <code>{@link org.assertj.core8.api.Lambdas}</code>.
 *
 * @author arothkopf
 *
 */
public class LambdasTest implements WithAssertions {

  /**
   * @throws java.lang.Exception
   */
  @Before
  public void setUp() throws Exception {
  }

  /**
   * Test method for {@link org.assertj.core8.api.Lambdas#toCondition(java.util.function.Predicate)}.
   */
  @Test
  public void Lambdas_toCondition_Test() {
	Condition<String> condition = Lambdas.toCondition((final String str) -> str.startsWith("A"));
	assertThat(condition.matches("Aardvark")).isTrue();
	assertThat(condition.matches("Bee")).isFalse();

  }

}
