/*
 * Created on Nov 29, 2010
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the
 * License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS"
 * BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 * 
 * Copyright @2010-2011 the original author or authors.
 */
package org.fest.assertions.api.date;

import static junit.framework.Assert.assertSame;
import static org.mockito.MockitoAnnotations.initMocks;

import java.util.Comparator;
import java.util.Date;

import org.fest.assertions.api.DateAssertBaseTest;
import org.fest.assertions.api.DateAssert;
import org.fest.assertions.internal.Dates;
import org.fest.assertions.internal.Objects;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

/**
 * Tests for <code>{@link DateAssert#usingComparator(java.util.Comparator)}</code> and
 * <code>{@link DateAssert#usingDefaultComparator()}</code>.
 * 
 * @author Joel Costigliola
 */
public class DateAssert_usingComparator_Test extends DateAssertBaseTest {

  @Mock
  private Comparator<Date> comparator;

  @Before
  public void before() {
    initMocks(this);
  }

  @Test
  public void using_default_comparator_test() {
    assertions.usingDefaultComparator();
    assertSame(getObjects(assertions), Objects.instance());
    assertSame(getDates(assertions), Dates.instance());
  }

  @Test
  public void using_custom_comparator_test() {
    // in that, we don't care of the comparator, the point to check is that we switch correctly of comparator
    assertions.usingComparator(comparator);
    assertSame(getObjects(assertions).getComparator(), comparator);
    assertSame(getDates(assertions).getComparator(), comparator);
  }
}
