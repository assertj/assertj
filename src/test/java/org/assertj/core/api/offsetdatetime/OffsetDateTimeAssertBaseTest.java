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
package org.assertj.core.api.offsetdatetime;

import static org.junit.Assume.assumeTrue;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;

import org.assertj.core.api.BaseTest;
import org.junit.experimental.theories.DataPoint;

/**
 * Base test class for {@link org.assertj.core.api.AbstractOffsetDateTimeAssert} tests.
 *
 * @author Joel Costigliola
 * @author Marcin Zajączkowski
 */
public class OffsetDateTimeAssertBaseTest extends BaseTest {

  @DataPoint
  public static OffsetDateTime offsetDateTime1 = OffsetDateTime.of(2000, 12, 14, 0, 0, 0, 0, ZoneOffset.UTC);
  @DataPoint
  public static OffsetDateTime offsetDateTime2 = OffsetDateTime.of(2000, 12, 13, 23, 59, 59, 999, ZoneOffset.UTC);
  @DataPoint
  public static OffsetDateTime offsetDateTime3 = OffsetDateTime.of(2000, 12, 14, 0, 0, 0, 1, ZoneOffset.UTC);
  @DataPoint
  public static OffsetDateTime offsetDateTime4 = OffsetDateTime.of(2000, 12, 14, 22, 15, 15, 875, ZoneOffset.UTC);
  @DataPoint
  public static OffsetDateTime offsetDateTime5 = OffsetDateTime.of(2000, 12, 14, 22, 15, 15, 874, ZoneOffset.UTC);
  @DataPoint
  public static OffsetDateTime offsetDateTime6 = OffsetDateTime.of(2000, 12, 14, 22, 15, 15, 876, ZoneOffset.UTC);

  protected static void testAssumptions(OffsetDateTime reference, OffsetDateTime before, OffsetDateTime equal,
                                        OffsetDateTime after) {
    assumeTrue(before.isBefore(reference));
    assumeTrue(equal.isEqual(reference));
    assumeTrue(after.isAfter(reference));
  }

}