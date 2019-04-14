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
 * Copyright 2012-2019 the original author or authors.
 */
package org.assertj.core.internal;

import java.time.chrono.ChronoZonedDateTime;


public class ChronoZonedDateTimeByInstantComparator extends DescribableComparator<ChronoZonedDateTime<?>> {

  private static final ChronoZonedDateTimeByInstantComparator INSTANCE = new ChronoZonedDateTimeByInstantComparator();

  public static ChronoZonedDateTimeByInstantComparator getInstance() {
    return INSTANCE;
  }

  private ChronoZonedDateTimeByInstantComparator() {
  }

  @Override
  public String description() {
    return ChronoZonedDateTimeByInstantComparator.class.getSimpleName();
  }

  @Override
  public int compare(ChronoZonedDateTime<?> o1, ChronoZonedDateTime<?> o2) {
    return ChronoZonedDateTime.timeLineOrder().compare(o1, o2);
  }
}
