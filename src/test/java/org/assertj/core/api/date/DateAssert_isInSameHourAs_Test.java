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


/**
 * Tests for <code>{@link org.assertj.core.api.DateAssert#isInSameHourAs(java.util.Date)}</code>.
 *
 * @author Joel Costigliola
 */
public class DateAssert_isInSameHourAs_Test extends AbstractDateAssertWithDateArg_Test {

  @Override
  protected DateAssert assertionInvocationWithDateArg() {
    return assertions.isInSameHourAs(otherDate);
  }

  @Override
  protected DateAssert assertionInvocationWithStringArg(String date) {
    return assertions.isInSameHourAs(date);
  }

  @Override
  protected void verifyAssertionInvocation(Date date) {
    verify(dates).assertIsInSameHourAs(getInfo(assertions), getActual(assertions), date);
  }

}
