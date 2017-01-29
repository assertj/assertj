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

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.internal.ObjectsBaseTest.defaultTypeComparators;
import static org.assertj.core.test.AlwaysEqualStringComparator.ALWAY_EQUALS;
import static org.mockito.Mockito.verify;

import java.util.Collections;
import java.util.Comparator;

import org.assertj.core.api.ObjectAssert;
import org.assertj.core.api.ObjectAssertBaseTest;
import org.assertj.core.test.Jedi;
import org.assertj.core.test.Person;
import org.assertj.core.test.PersonCaseInsensitiveNameComparator;
import org.junit.Test;

/**
 * Tests for <code>{@link ObjectAssert#isEqualToComparingFieldByField(Object)}</code>.
 *
 * @author Nicolas François
 */
public class ObjectAssert_isEqualsToComparingFields_Test extends ObjectAssertBaseTest {

  private Jedi other = new Jedi("Yoda", "Blue");

  @Override
  protected ObjectAssert<Jedi> invoke_api_method() {
    return assertions.isEqualToComparingFieldByField(other);
  }

  @Override
  @SuppressWarnings("unchecked")
  protected void verify_internal_effects() {
    verify(objects).assertIsEqualToIgnoringGivenFields(getInfo(assertions), getActual(assertions), other,
                                                       Collections.EMPTY_MAP, defaultTypeComparators());
  }

  @Test
  public void should_be_able_to_use_a_comparator_for_specified_fields() {
    Jedi actual = new Jedi("Yoda", "green");
    Jedi other = new Jedi("Luke", "green");

    assertThat(actual).usingComparatorForFields(ALWAY_EQUALS, "name").isEqualToComparingFieldByField(other);
  }

  @Test
  public void comparators_for_fields_should_have_precedence_over_comparators_for_types() {
    Comparator<String> comparator = new Comparator<String>() {
      public int compare(String o1, String o2) {
        return o1.compareTo(o2);
      }
    };
    Jedi actual = new Jedi("Yoda", "green");
    Jedi other = new Jedi("Luke", "green");

    assertThat(actual).usingComparatorForFields(ALWAY_EQUALS, "name")
                      .usingComparatorForType(comparator, String.class).isEqualToComparingFieldByField(other);
  }

  @Test
  public void should_be_able_to_use_a_comparator_for_specified_type() {
    Jedi actual = new Jedi("Yoda", "green");
    Jedi other = new Jedi("Luke", "blue");

    assertThat(actual).usingComparatorForType(ALWAY_EQUALS, String.class).isEqualToComparingFieldByField(other);
  }

  @Test
  public void should_be_able_to_use_a_type_comparator_for_any_of_the_type_subclasses() {

    JediMaster yoda1 = new JediMaster("Yoda", new Jedi("luke", "Green"));
    JediMaster yoda2 = new JediMaster("Yoda", new Jedi("LUKE", null));

    // Jedi is a subclass of Person
    assertThat(yoda1).usingComparatorForType(new PersonCaseInsensitiveNameComparator(), Person.class)
                     .isEqualToComparingFieldByField(yoda2);
  }

  static class JediMaster {
    private Jedi padawan;
    private String name;

    JediMaster(String name, Jedi padawan) {
      this.name = name;
      this.padawan = padawan;
    }

    public Jedi getPadawan() {
      return padawan;
    }

    public String getName() {
      return name;
    }
  }

}
