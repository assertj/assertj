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
package org.assertj.core.internal.objects;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.error.ShouldBeEqualToIgnoringFields.shouldBeEqualToIgnoringGivenFields;
import static org.assertj.core.test.TestData.someInfo;
import static org.assertj.core.test.TestFailures.failBecauseExpectedAssertionErrorWasNotThrown;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.assertj.core.util.Lists.newArrayList;
import static org.mockito.Mockito.verify;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.api.Assertions;
import org.assertj.core.internal.Objects;
import org.assertj.core.internal.ObjectsBaseTest;
import org.assertj.core.internal.TypeComparators;
import org.assertj.core.internal.objects.Objects_assertIsEqualToIgnoringGivenFields_Test.OuterClass.InnerClass;
import org.assertj.core.test.CartoonCharacter;
import org.assertj.core.test.Employee;
import org.assertj.core.test.Jedi;
import org.assertj.core.test.Person;
import org.assertj.core.test.TestClassWithRandomId;
import org.assertj.core.util.introspection.FieldSupport;
import org.junit.Test;

/**
 * Tests for <code>{@link Objects#assertIsEqualToIgnoringGivenFields(AssertionInfo, Object, Object, Map, TypeComparators, String...)}</code>.
 *
 * @author Nicolas François
 * @author Joel Costigliola
 */
public class Objects_assertIsEqualToIgnoringGivenFields_Test extends ObjectsBaseTest {

  @Test
  public void should_pass_when_fields_are_equal() {
    Jedi actual = new Jedi("Yoda", "Green");
    Jedi other = new Jedi("Yoda", "Green");
    // strangeNotReadablePrivateField fields are compared and are null in both actual and other
    objects.assertIsEqualToIgnoringGivenFields(someInfo(), actual, other, noFieldComparators(), defaultTypeComparators());
  }

  @Test
  public void should_pass_when_not_ignored_fields_are_equal() {
    Jedi actual = new Jedi("Yoda", "Green");
    Jedi other = new Jedi("Yoda", "Blue");
    // strangeNotReadablePrivateField fields are compared and are null in both actual and other
    objects.assertIsEqualToIgnoringGivenFields(someInfo(), actual, other, noFieldComparators(), defaultTypeComparators(), "lightSaberColor");
  }

  @Test
  public void should_pass_when_not_ignored_inherited_fields_are_equal() {
    Jedi actual = new Jedi("Yoda", "Green");
    Jedi other = new Jedi("Luke", "Green");
    objects.assertIsEqualToIgnoringGivenFields(someInfo(), actual, other, noFieldComparators(), defaultTypeComparators(), "name");
  }

  @Test
  public void should_pass_when_not_ignored_fields_are_equal_even_if_one_ignored_field_is_not_defined() {
    Person actual = new Person("Yoda");
    Jedi other = new Jedi("Yoda", "Green");
    objects.assertIsEqualToIgnoringGivenFields(someInfo(), actual, other, noFieldComparators(), defaultTypeComparators(), "lightSaberColor");
  }

  @Test
  public void should_pass_when_field_values_are_null() {
    Jedi actual = new Jedi("Yoda", null);
    Jedi other = new Jedi("Yoda", null);
    objects.assertIsEqualToIgnoringGivenFields(someInfo(), actual, other, noFieldComparators(), defaultTypeComparators(), "name");
  }

  @Test
  public void should_pass_when_fields_are_equal_even_if_objects_types_differ() {
    CartoonCharacter actual = new CartoonCharacter("Homer Simpson");
    Person other = new Person("Homer Simpson");
    objects.assertIsEqualToIgnoringGivenFields(someInfo(), actual, other, noFieldComparators(), defaultTypeComparators(), "children");
  }

  @Test
  public void should_fail_if_actual_is_null() {
    thrown.expectAssertionError(actualIsNull());
    Jedi other = new Jedi("Yoda", "Green");
    objects.assertIsEqualToIgnoringGivenFields(someInfo(), null, other, noFieldComparators(), defaultTypeComparators(), "name");
  }

  @Test
  public void should_fail_when_some_field_values_differ() {
    AssertionInfo info = someInfo();
    Jedi actual = new Jedi("Yoda", "Green");
    Jedi other = new Jedi("Yoda", "Blue");
    try {
      objects.assertIsEqualToIgnoringGivenFields(info, actual, other, noFieldComparators(), defaultTypeComparators(), "name");
    } catch (AssertionError err) {
      verify(failures).failure(info, shouldBeEqualToIgnoringGivenFields(actual,
                                                                        newArrayList("lightSaberColor"),
                                                                        newArrayList((Object) "Green"),
                                                                        newArrayList((Object) "Blue"),
                                                                        newArrayList("name")));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }

  @Test
  public void should_fail_when_some_field_values_differ_and_no_fields_are_ignored() {
    AssertionInfo info = someInfo();
    Jedi actual = new Jedi("Yoda", "Green");
    Jedi other = new Jedi("Yoda", "Blue");
    try {
      objects.assertIsEqualToIgnoringGivenFields(info, actual, other, noFieldComparators(), defaultTypeComparators());
    } catch (AssertionError err) {
      verify(failures).failure(info, shouldBeEqualToIgnoringGivenFields(actual,
                                                                        newArrayList("lightSaberColor"),
                                                                        newArrayList((Object) "Green"),
                                                                        newArrayList((Object) "Blue"),
                                                                        new ArrayList<String>()));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }

  @Test
  public void should_fail_when_some_inherited_field_values_differ() {
    AssertionInfo info = someInfo();
    Jedi actual = new Jedi("Yoda", "Green");
    Jedi other = new Jedi("Luke", "Green");
    try {
      objects.assertIsEqualToIgnoringGivenFields(info, actual, other, noFieldComparators(), defaultTypeComparators(), "lightSaberColor");
    } catch (AssertionError err) {
      verify(failures).failure(info, shouldBeEqualToIgnoringGivenFields(actual,
                                                                        newArrayList("name"),
                                                                        newArrayList((Object) "Yoda"),
                                                                        newArrayList((Object) "Luke"),
                                                                        newArrayList("lightSaberColor")));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }

  @Test
  public void should_fail_when_one_of_actual_field_to_compare_can_not_be_found_in_the_other_object() {
    Jedi actual = new Jedi("Yoda", "Green");
    Employee other = new Employee();

    thrown.expectIntrospectionErrorWithMessageContaining("Can't find any field or property with name 'lightSaberColor'");

    objects.assertIsEqualToIgnoringGivenFields(someInfo(), actual, other, noFieldComparators(), defaultTypeComparators(), "name");
  }

  @Test
  public void should_fail_when_some_field_value_is_null_on_one_object_only() {
    AssertionInfo info = someInfo();
    Jedi actual = new Jedi("Yoda", null);
    Jedi other = new Jedi("Yoda", "Green");
    try {
      objects.assertIsEqualToIgnoringGivenFields(info, actual, other, noFieldComparators(), defaultTypeComparators(), "name");
    } catch (AssertionError err) {
      List<Object> expected = newArrayList((Object) "Green");
      verify(failures).failure(info, shouldBeEqualToIgnoringGivenFields(actual,
                                                                        newArrayList("lightSaberColor"),
                                                                        newArrayList((Object) null),
                                                                        expected,
                                                                        newArrayList("name")));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }

  @Test
  public void should_pass_when_private_fields_differ_but_are_not_compared_or_are_ignored() {
    boolean allowedToUsePrivateFields = FieldSupport.comparison().isAllowedToUsePrivateFields();
    Assertions.setAllowComparingPrivateFields(false);
    TestClassWithRandomId actual = new TestClassWithRandomId("1", 1);
    TestClassWithRandomId other = new TestClassWithRandomId("1", 2);
    //
    objects.assertIsEqualToIgnoringGivenFields(someInfo(), actual, other, noFieldComparators(), defaultTypeComparators(), "n");
    // reset
    Assertions.setAllowComparingPrivateFields(allowedToUsePrivateFields);
  }

  @Test
  public void should_be_able_to_compare_objects_of_different_types() {
    Dude person = new Dude("John", "Doe");
    DudeDAO personDAO = new DudeDAO("John", "Doe", 1L);

    assertThat(person).isEqualToComparingFieldByField(personDAO);
    assertThat(personDAO).isEqualToIgnoringGivenFields(person, "id");
  }

  @Test
  public void should_be_able_to_use_a_comparator_for_specified_fields() {
    Comparator<String> alwaysEqual = new Comparator<String>() {
      public int compare(String o1, String o2) {
        return 0;
      }
    };
    Jedi actual = new Jedi("Yoda", "Green");
    Jedi other = new Jedi("Luke", "Green");
    
    assertThat(actual).usingComparatorForFields(alwaysEqual, "name").isEqualToComparingFieldByField(other);
  }

  @Test
  public void should_pass_when_class_has_synthetic_field() {
    InnerClass actual = new OuterClass().createInnerClass();
    InnerClass other = new OuterClass().createInnerClass();

    // ensure that the compiler has generated at one synthetic field for the comparison
    assertThat(InnerClass.class.getDeclaredFields()).extracting("synthetic").contains(Boolean.TRUE);

    objects.assertIsEqualToIgnoringGivenFields(someInfo(), actual, other, noFieldComparators(), defaultTypeComparators());
  }

  private static class Dude {
    @SuppressWarnings("unused")
    String firstname, lastname;

    public Dude(String firstname, String lastname) {
      this.firstname = firstname;
      this.lastname = lastname;
    }
  }

  private static class DudeDAO {
    @SuppressWarnings("unused")
    String firstname, lastname;
    @SuppressWarnings("unused")
    Long id;

    public DudeDAO(String firstname, String lastname, Long id) {
      this.firstname = firstname;
      this.lastname = lastname;
      this.id = id;
    }
  }

  // example taken from http://stackoverflow.com/questions/8540768/when-is-the-jvm-bytecode-access-modifier-flag-0x1000-hex-synthetic-set
  class OuterClass {
    private String outerField;

    class InnerClass {
      // synthetic field this$1 generated in inner class to provider reference to outer
      private InnerClass() {
      }

      String getOuterField() {
        return outerField;
      }
    }

    InnerClass createInnerClass() {
      return new InnerClass();
    }
  }

}
