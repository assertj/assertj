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
 * Copyright 2012-2020 the original author or authors.
 */
package org.assertj.core.api;

import java.time.Period;

/**
 * Assertion methods for {@link java.time.Period}
 *
 * @author Hayden Meloche
 * @since 3.17.0
 */
public class PeriodAssert extends AbstractPeriodAssert<PeriodAssert> {

  /**
   * Creates a new <code>{@link PeriodAssert}</code>
   *
   * @param period   the actual value to verify
   */
  public PeriodAssert(Period period) {
    super(period, PeriodAssert.class);
  }
}
