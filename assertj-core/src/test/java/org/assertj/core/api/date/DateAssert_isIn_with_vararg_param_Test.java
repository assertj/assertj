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

import java.time.Instant;
import java.util.Date;

import org.assertj.core.api.DateAssert;

/**
 * Tests for {@link DateAssert#isIn(Object...)} with vararg of Date or String.
 *
 * @author Joel Costigliola
 */
class DateAssert_isIn_with_vararg_param_Test extends AbstractDateAssertWithDateArg_Test {

  @Override
  protected DateAssert assertionInvocationWithDateArg() {
    return assertions.isIn(otherDate);
  }

  @Override
  protected DateAssert assertionInvocationWithStringArg(String dateAsString) {
    return assertions.isIn(dateAsString);
  }

  protected DateAssert assertionInvocationWithInstantArg(Instant instant) {
    return assertions.isIn(instant);
  }

  @Override
  protected void verifyAssertionInvocation(Date date) {
    verify(objects).assertIsIn(getInfo(assertions), getActual(assertions), new Object[] { date });
  }

  @Override
  protected DateAssert assertionInvocationWithInstantArg() {
    return assertions.isIn(otherDate.toInstant());
  }

}
