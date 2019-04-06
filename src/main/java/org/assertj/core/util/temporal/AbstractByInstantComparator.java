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
package org.assertj.core.util.temporal;

import java.time.Instant;
import java.time.temporal.Temporal;
import java.util.Comparator;

public abstract class AbstractByInstantComparator<TEMPORAL extends Temporal> implements Comparator<TEMPORAL> {

  private final Comparator<TEMPORAL> comparator;

  AbstractByInstantComparator() {
    comparator = Comparator.comparing(this::getInstant);
  }

  @Override
  public int compare(TEMPORAL o1, TEMPORAL o2) {
    return comparator.compare(o1, o2);
  }

  protected abstract Instant getInstant(TEMPORAL temporal);
}
