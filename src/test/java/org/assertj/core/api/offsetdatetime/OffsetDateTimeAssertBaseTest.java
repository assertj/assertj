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
 * Copyright 2012-2022 the original author or authors.
 */
package org.assertj.core.api.offsetdatetime;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;

/**
 * Base test class for {@link org.assertj.core.api.AbstractOffsetDateTimeAssert} tests.
 *
 * @author Joel Costigliola
 * @author Marcin Zajączkowski
 */
public class OffsetDateTimeAssertBaseTest {

  public static final OffsetDateTime REFERENCE = OffsetDateTime.of(2000, 12, 14, 0, 0, 0, 0, ZoneOffset.UTC);
  public static final OffsetDateTime BEFORE = OffsetDateTime.of(2000, 12, 13, 23, 59, 59, 999, ZoneOffset.UTC);
  public static final OffsetDateTime AFTER = OffsetDateTime.of(2000, 12, 14, 0, 0, 0, 1, ZoneOffset.UTC);
  public static final OffsetDateTime REFERENCE_WITH_DIFFERENT_OFFSET = OffsetDateTime.of(2000, 12, 14, 3, 0, 0, 0,
                                                                                         ZoneOffset.ofHours(3));
  public static final OffsetDateTime BEFORE_WITH_DIFFERENT_OFFSET = OffsetDateTime.of(2000, 12, 14, 2, 59, 59, 999,
                                                                                      ZoneOffset.ofHours(3));
  public static final OffsetDateTime AFTER_WITH_DIFFERENT_OFFSET = OffsetDateTime.of(2000, 12, 14, 3, 0, 0, 1,
                                                                                     ZoneOffset.ofHours(3));
}
