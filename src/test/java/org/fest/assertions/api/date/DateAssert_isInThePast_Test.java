/*
 * Created on Dec 21, 2010
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the
 * License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS"
 * BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 * 
 * Copyright @2010-2011 the original author or authors.
 */
package org.fest.assertions.api.date;

import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.verify;

import org.fest.assertions.api.DateAssertBaseTest;
import org.fest.assertions.api.DateAssert;
import org.junit.Test;

/**
 * Tests for <code>{@link DateAssert#isInThePast()}</code>.
 * 
 * @author Joel Costigliola
 */
public class DateAssert_isInThePast_Test extends DateAssertBaseTest {

  @Test
  public void should_verify_that_actual_is_in_the_past() {
    assertions.isInThePast();
    verify(dates).assertIsInThePast(getInfo(assertions), getActual(assertions));
  }

  @Test
  public void should_return_this() {
    DateAssert returned = assertions.isInThePast();
    assertSame(assertions, returned);
  }

}
