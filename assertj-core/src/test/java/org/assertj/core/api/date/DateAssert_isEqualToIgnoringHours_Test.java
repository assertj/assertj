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
 * Copyright 2012-2023 the original author or authors.
 */
package org.assertj.core.api.date;

import static org.mockito.Mockito.verify;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import org.assertj.core.api.DateAssert;

/**
 * Tests for <code>{@link org.assertj.core.api.DateAssert#isEqualToIgnoringHours(java.util.Date)}</code>.
 *
 * @author William Delanoue
 */
class DateAssert_isEqualToIgnoringHours_Test extends AbstractDateAssertWithDateArg_Test {

  @Override
  protected DateAssert assertionInvocationWithDateArg() {
    return assertions.isEqualToIgnoringHours(otherDate);
  }

  @Override
  protected DateAssert assertionInvocationWithStringArg(String date) {
    return assertions.isEqualToIgnoringHours(date);
  }

  @Override
  protected DateAssert assertionInvocationWithInstantArg() {
    return assertions.isEqualToIgnoringHours(otherDate.toInstant());
  }

  @Override
  protected void verifyAssertionInvocation(Date date) {
    verify(dates).assertIsEqualWithPrecision(getInfo(assertions), getActual(assertions), date, TimeUnit.HOURS);
  }

}
