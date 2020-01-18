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
package org.assertj.core.internal;

import java.time.chrono.ChronoLocalDateTime;

public class ChronoLocalDateTimeComparator extends DescribableComparator<ChronoLocalDateTime<?>> {

  private static final ChronoLocalDateTimeComparator INSTANCE = new ChronoLocalDateTimeComparator();

  public static ChronoLocalDateTimeComparator getInstance() {
    return INSTANCE;
  }

  private ChronoLocalDateTimeComparator() {
  }

  @Override
  public String description() {
    return "ChronoLocalDateTime.timeLineOrder()";
  }

  @Override
  public int compare(ChronoLocalDateTime<?> date1, ChronoLocalDateTime<?> date2) {
    return ChronoLocalDateTime.timeLineOrder().compare(date1, date2);
  }
}
