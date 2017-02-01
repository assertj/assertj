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

import static org.mockito.Mockito.verify;

import java.util.Date;

import org.assertj.core.api.DateAssert;
import org.junit.Before;

/**
 * Tests for {@link DateAssert#isCloseTo(Date, long)} and {@link DateAssert#isCloseTo(String, long)}.
 * 
 * @author Joel Costigliola
 */
public class DateAssert_isCloseTo_Test extends AbstractDateAssertWithDateArg_Test {

  private long delta;

  @Override
  @Before
  public void setUp() {
    super.setUp();
    delta = 100;
  }

  @Override
  protected DateAssert assertionInvocationWithDateArg() {
    return assertions.isCloseTo(otherDate, delta);
  }

  @Override
  protected DateAssert assertionInvocationWithStringArg(String dateAsString) {
    return assertions.isCloseTo(dateAsString, delta);
  }

  @Override
  protected void verifyAssertionInvocation(Date date) {
    verify(dates).assertIsCloseTo(getInfo(assertions), getActual(assertions), date, delta);
  }

}
