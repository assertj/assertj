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
package org.assertj.core.api.long_;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.MockitoAnnotations.initMocks;

import java.util.Comparator;


import org.assertj.core.api.LongAssert;
import org.assertj.core.api.LongAssertBaseTest;
import org.junit.Before;
import org.mockito.Mock;

/**
 * Tests for <code>{@link LongAssert#usingComparator(java.util.Comparator)}</code>.
 * 
 * @author Joel Costigliola
 */
public class LongAssert_usingComparator_Test extends LongAssertBaseTest {

  @Mock
  private Comparator<Long> comparator;

  @Before
  public void before() {
    initMocks(this);
  }

  @Override
  protected LongAssert invoke_api_method() {
    // in that, we don't care of the comparator, the point to check is that we switch correctly of comparator
    return assertions.usingComparator(comparator);
  }

  @Override
  protected void verify_internal_effects() {
    assertThat(comparator).isSameAs(getObjects(assertions).getComparator());
    assertThat(comparator).isSameAs(getLongs(assertions).getComparator());
  }
}
