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
package org.assertj.core.api.date;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.MockitoAnnotations.initMocks;

import java.util.Comparator;
import java.util.Date;


import org.assertj.core.api.DateAssert;
import org.assertj.core.api.DateAssertBaseTest;
import org.assertj.core.internal.Dates;
import org.assertj.core.internal.Objects;
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
    assertThat(Objects.instance()).isSameAs(getObjects(assertions));
    assertThat(Dates.instance()).isSameAs(getDates(assertions));
  }

  @Test
  public void using_custom_comparator_test() {
    // in that, we don't care of the comparator, the point to check is that we switch correctly of comparator
    assertions.usingComparator(comparator);
    assertThat(comparator).isSameAs(getObjects(assertions).getComparator());
    assertThat(comparator).isSameAs(getDates(assertions).getComparator());
  }
}
