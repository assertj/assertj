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
package org.assertj.core.api.date;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;


import org.assertj.core.api.DateAssert;
import org.assertj.core.api.DateAssertBaseTest;
import org.junit.Test;

/**
 * Tests for <code>{@link DateAssert#isInTheFuture()}</code>.
 * 
 * @author Joel Costigliola
 */
public class DateAssert_isInTheFuture_Test extends DateAssertBaseTest {

  @Test
  public void should_verify_that_actual_is_in_the_past() {
    assertions.isInTheFuture();
    verify(dates).assertIsInTheFuture(getInfo(assertions), getActual(assertions));
  }

  @Test
  public void should_return_this() {
    DateAssert returned = assertions.isInTheFuture();
    assertThat(returned).isSameAs(assertions);
  }

}
