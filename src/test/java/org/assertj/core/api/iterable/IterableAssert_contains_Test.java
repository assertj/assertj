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
 * Copyright 2012-2016 the original author or authors.
 */
package org.assertj.core.api.iterable;

import org.assertj.core.api.AbstractIterableAssert;
import org.assertj.core.api.ConcreteIterableAssert;
import org.assertj.core.api.IterableAssertBaseTest;
import org.assertj.core.util.BigDecimalComparator;
import org.junit.Test;

import java.math.BigDecimal;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;


/**
 * Tests for <code>{@link AbstractIterableAssert#contains(Object...)}</code>.
 * 
 * @author Alex Ruiz
 * @author Joel Costigliola
 */
public class IterableAssert_contains_Test extends IterableAssertBaseTest {

  private Object[] values = { "Yoda", "Luke" };

  @Override
  protected ConcreteIterableAssert<Object> invoke_api_method() {
    return assertions.contains(values);
  }

  @Override
  protected void verify_internal_effects() {
    verify(iterables).assertContains(getInfo(assertions), getActual(assertions), values);
  }

  @Test
  public void should_allow_to_specify_element_type_comparator() {
    assertThat(asList("some", new BigDecimal("4.2")))
      .usingComparatorForElementFieldsWithType(new BigDecimalComparator(), BigDecimal.class)
      .contains(new BigDecimal("4.20"));
  }
}
