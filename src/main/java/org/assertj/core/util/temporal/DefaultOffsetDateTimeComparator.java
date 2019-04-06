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
import java.time.OffsetDateTime;
import java.util.Comparator;

public class DefaultOffsetDateTimeComparator extends AbstractByInstantComparator<OffsetDateTime> {

  private static final Comparator<OffsetDateTime> INSTANCE = new DefaultOffsetDateTimeComparator();

  public static Comparator<OffsetDateTime> getInstance() {
    return INSTANCE;
  }

  private DefaultOffsetDateTimeComparator() {
    super();
  }

  @Override
  protected Instant getInstant(OffsetDateTime temporal) {
    return temporal.toInstant();
  }

  @Override
  public String toString() {
    return "default OffsetDateTime comparison by instant";
  }
}
