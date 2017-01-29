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
package org.assertj.core.api.localtime;

import static org.junit.Assume.assumeTrue;

import java.time.LocalTime;

import org.assertj.core.api.AbstractLocalTimeAssert;
import org.assertj.core.api.BaseTest;
import org.junit.experimental.theories.DataPoint;

/**
 * Base test class for {@link AbstractLocalTimeAssert} tests.
 */
public class LocalTimeAssertBaseTest extends BaseTest {

  @DataPoint
  public static LocalTime localTime1 = LocalTime.of(0, 0);
  @DataPoint
  public static LocalTime localTime2 = LocalTime.of(23, 59, 59, 999);
  @DataPoint
  public static LocalTime localTime3 = LocalTime.of(0, 0, 0, 1);
  @DataPoint
  public static LocalTime localTime4 = LocalTime.of(22, 15, 15, 875);
  @DataPoint
  public static LocalTime localTime5 = LocalTime.of(22, 15, 15, 874);
  @DataPoint
  public static LocalTime localTime6 = LocalTime.of(22, 15, 15, 876);

  protected static void testAssumptions(LocalTime reference, LocalTime timeBefore, LocalTime timeAfter) {
    assumeTrue(timeBefore.isBefore(reference));
    assumeTrue(timeAfter.isAfter(reference));
  }

}