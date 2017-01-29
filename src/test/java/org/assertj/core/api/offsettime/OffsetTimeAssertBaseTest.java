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
package org.assertj.core.api.offsettime;

import static org.junit.Assume.assumeTrue;

import java.time.OffsetTime;
import java.time.ZoneOffset;

import org.assertj.core.api.BaseTest;
import org.junit.experimental.theories.DataPoint;

/**
 * Base test class for {@link org.assertj.core.api.AbstractOffsetTimeAssert} tests.
 */
public class OffsetTimeAssertBaseTest extends BaseTest {

  @DataPoint
  public static OffsetTime OffsetTime1 = OffsetTime.of(0, 0, 0, 0, ZoneOffset.UTC);
  @DataPoint
  public static OffsetTime OffsetTime2 = OffsetTime.of(23, 59, 59, 999, ZoneOffset.UTC);
  @DataPoint
  public static OffsetTime OffsetTime3 = OffsetTime.of(0, 0, 0, 1, ZoneOffset.UTC);
  @DataPoint
  public static OffsetTime OffsetTime4 = OffsetTime.of(22, 15, 15, 875, ZoneOffset.UTC);
  @DataPoint
  public static OffsetTime OffsetTime5 = OffsetTime.of(22, 15, 15, 874, ZoneOffset.UTC);
  @DataPoint
  public static OffsetTime OffsetTime6 = OffsetTime.of(22, 15, 15, 876, ZoneOffset.UTC);

  protected static void testAssumptions(OffsetTime reference, OffsetTime timeBefore, OffsetTime timeEqual,
                                        OffsetTime timeAfter) {
    assumeTrue(timeBefore.isBefore(reference));
    assumeTrue(timeEqual.isEqual(reference));
    assumeTrue(timeAfter.isAfter(reference));
  }

}