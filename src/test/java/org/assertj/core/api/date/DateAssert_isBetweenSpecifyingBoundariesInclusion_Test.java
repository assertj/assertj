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
 * Tests for {@link DateAssert#isBetween(Date, Date, boolean, boolean)} and
 * {@link DateAssert#isBetween(String, String, boolean, boolean)}.
 * 
 * @author Joel Costigliola
 */
public class DateAssert_isBetweenSpecifyingBoundariesInclusion_Test extends AbstractDateAssertWithDateArg_Test {

  private boolean inclusiveStart;
  private boolean inclusiveEnd;

  @Override
  @Before
  public void setUp() {
    super.setUp();
    inclusiveStart = false;
    inclusiveEnd = true;
  }

  @Override
  protected DateAssert assertionInvocationWithDateArg() {
    return assertions.isBetween(otherDate, otherDate, inclusiveStart, inclusiveEnd);
  }

  @Override
  protected DateAssert assertionInvocationWithStringArg(String dateAsString) {
    return assertions.isBetween(dateAsString, dateAsString, inclusiveStart, inclusiveEnd);
  }

  @Override
  protected void verifyAssertionInvocation(Date date) {
    verify(dates).assertIsBetween(getInfo(assertions), getActual(assertions), date, date, inclusiveStart, inclusiveEnd);
  }

}
