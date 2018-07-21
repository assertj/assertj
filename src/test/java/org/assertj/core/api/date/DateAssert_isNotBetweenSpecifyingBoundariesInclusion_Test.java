/*
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 *
 * Copyright 2012-2018 the original author or authors.
 */
package org.assertj.core.api.date;

import static org.mockito.Mockito.verify;

import java.util.Date;

import org.assertj.core.api.DateAssert;
import org.junit.jupiter.api.BeforeEach;

/**
 * Tests for {@link DateAssert#isNotBetween(Date, Date, boolean, boolean)} and
 * {@link DateAssert#isNotBetween(String, String, boolean, boolean)}.
 * 
 * @author Joel Costigliola
 */
public class DateAssert_isNotBetweenSpecifyingBoundariesInclusion_Test extends AbstractDateAssertWithDateArg_Test {

  private boolean inclusiveStart;
  private boolean inclusiveEnd;

  @Override
  @BeforeEach
  public void setUp() {
    super.setUp();
    inclusiveStart = false;
    inclusiveEnd = true;
  }

  @Override
  protected DateAssert assertionInvocationWithDateArg() {
    return assertions.isNotBetween(otherDate, otherDate, inclusiveStart, inclusiveEnd);
  }

  @Override
  protected DateAssert assertionInvocationWithStringArg(String dateAsString) {
    return assertions.isNotBetween(dateAsString, dateAsString, inclusiveStart, inclusiveEnd);
  }

  @Override
  protected void verifyAssertionInvocation(Date date) {
    verify(dates).assertIsNotBetween(getInfo(assertions), getActual(assertions), date, date, inclusiveStart, inclusiveEnd);
  }

}
