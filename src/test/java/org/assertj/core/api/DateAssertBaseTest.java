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
package org.assertj.core.api;

import static org.mockito.Mockito.mock;

import java.util.Date;

import org.assertj.core.internal.Dates;
import org.assertj.core.internal.Objects;
import org.junit.After;
import org.junit.Before;

/**
 * 
 * Abstract base of all DateAssert tests.
 * 
 * @author Joel Costigliola
 * 
 */
public abstract class DateAssertBaseTest {

  protected DateAssert assertions;
  protected Dates dates;
  protected Objects objects;

  @Before
  public void setUp() {
    dates = mock(Dates.class);
    objects = mock(Objects.class);
    assertions = new DateAssert(new Date());
    assertions.dates = dates;
    assertions.objects = objects;
  }

  @After
  public void tearDown() {
    AbstractDateAssert.useDefaultDateFormatsOnly();
  }

  protected Date parse(String dateAsString) {
    return assertions.parse(dateAsString);
  }

  protected AssertionInfo getInfo(DateAssert someAssertions) {
    return someAssertions.info;
  }

  protected Date getActual(DateAssert someAssertions) {
    return someAssertions.actual;
  }
  
  protected Objects getObjects(DateAssert someAssertions) {
    return someAssertions.objects;
  }
  
  protected Dates getDates(DateAssert someAssertions) {
    return someAssertions.dates;
  }
}
