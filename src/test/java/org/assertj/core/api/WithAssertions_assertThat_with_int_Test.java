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
 * Tests for <code>{@link org.assertj.core8.api.WithAssertions} int assertion Java 8 extension</code>.
 *
 * @author arothkopf
 *
 */
public class WithAssertions_assertThat_with_int_Test implements WithAssertions {

  /**
   * @throws java.lang.Exception
   */
  @Before
  public void setUp() throws Exception {
  }

  /**
   * Test method for {@link org.assertj.core8.api.WithAssertions#assertThat(int)}.
   */
  @Test
  public void should_pass_actual_with_lambda() {
	this.assertThat(17).is((int x) -> x % 2 != 0);
  }

}
