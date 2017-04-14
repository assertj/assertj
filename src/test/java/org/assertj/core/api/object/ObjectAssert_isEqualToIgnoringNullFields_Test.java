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
package org.assertj.core.api.object;

import static java.util.Collections.EMPTY_MAP;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.internal.ObjectsBaseTest.defaultTypeComparators;
import static org.assertj.core.test.AlwaysEqualComparator.ALWAY_EQUALS_STRING;
import static org.mockito.Mockito.verify;

import java.util.Comparator;

import org.assertj.core.api.ObjectAssert;
import org.assertj.core.api.ObjectAssertBaseTest;
import org.assertj.core.test.Jedi;
import org.junit.Test;


/**
 * Tests for <code>{@link ObjectAssert#isEqualToIgnoringNullFields(Object)}</code>.
 *
 * @author Nicolas François
 * @author Mikhail Mazursky
 */
public class ObjectAssert_isEqualToIgnoringNullFields_Test extends ObjectAssertBaseTest {

  private Jedi other = new Jedi("Yoda", "Green");

  @Override
  protected ObjectAssert<Jedi> invoke_api_method() {
    return assertions.isEqualToIgnoringNullFields(other);
  }

  @Override
  @SuppressWarnings("unchecked")
  protected void verify_internal_effects() {
    verify(objects).assertIsEqualToIgnoringNullFields(getInfo(assertions), getActual(assertions), other, EMPTY_MAP, defaultTypeComparators());
  }

  @Test
  public void should_be_able_to_use_a_comparator_for_specified_fields() {
    Jedi actual = new Jedi("Yoda", null);
    Jedi other = new Jedi("Luke", null);

    assertThat(actual).usingComparatorForFields(ALWAY_EQUALS_STRING, "name").isEqualToIgnoringNullFields(other);
  }

  @Test
  public void comparators_for_fields_should_have_precedence_over_comparators_for_types() {
    Comparator<String> comparator = new Comparator<String>() {
      public int compare(String o1, String o2) {
        return o1.compareTo(o2);
      }
    };
    Jedi actual = new Jedi("Yoda", null);
    Jedi other = new Jedi("Luke", null);

    assertThat(actual).usingComparatorForFields(ALWAY_EQUALS_STRING, "name")
      .usingComparatorForType(comparator, String.class).isEqualToIgnoringNullFields(other);
  }

  @Test
  public void should_be_able_to_use_a_comparator_for_specified_type() {
    Jedi actual = new Jedi("Yoda", null);
    Jedi other = new Jedi("Luke", null);

    assertThat(actual).usingComparatorForType(ALWAY_EQUALS_STRING, String.class).isEqualToIgnoringNullFields(other);
  }

}
