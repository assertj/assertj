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
package org.assertj.core.api.localdate;

import static org.junit.Assume.assumeTrue;

import java.time.LocalDate;

import org.assertj.core.api.AbstractLocalDateAssert;
import org.assertj.core.api.BaseTest;
import org.junit.experimental.theories.DataPoint;


/**
 * 
 * Base test class for {@link AbstractLocalDateAssert} tests.
 * 
 */
public class LocalDateAssertBaseTest extends BaseTest {

  @DataPoint
  public static LocalDate localDate1 = LocalDate.now().minusDays(10);
  @DataPoint
  public static LocalDate localDate2 = LocalDate.now();
  @DataPoint
  public static LocalDate localDate3 = LocalDate.now().plusDays(10);

  protected static void testAssumptions(LocalDate reference, LocalDate dateBefore, LocalDate dateAfter) {
    assumeTrue(dateBefore.isBefore(reference));
    assumeTrue(dateAfter.isAfter(reference));
  }

}