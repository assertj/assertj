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

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.api.Assertions;
import org.assertj.core.internal.Objects;
import org.assertj.core.internal.ObjectsBaseTest;
import org.assertj.core.internal.TypeComparators;
import org.assertj.core.internal.objects.Objects_assertIsEqualToIgnoringNullFields_Test.OuterClass.InnerClass;
import org.assertj.core.test.CartoonCharacter;
import org.assertj.core.test.Employee;
import org.assertj.core.test.Jedi;
import org.assertj.core.test.Person;
import org.assertj.core.test.TestClassWithRandomId;
import org.assertj.core.util.introspection.FieldSupport;
import org.junit.Test;

import java.util.Map;

/**
 * Tests for <code>{@link Objects#assertIsEqualToIgnoringNullFields(AssertionInfo, Object, Object, Map, TypeComparators)} </code>.
 *
 * @author Nicolas François
 * @author Joel Costigliola
 */
public class Objects_assertIsEqualToIgnoringNullFields_Test extends ObjectsBaseTest {

  @Test
  public void should_pass_when_fields_are_equal() {
    Jedi actual = new Jedi("Yoda", "Green");
    Jedi other = new Jedi("Yoda", "Green");
    objects.assertIsEqualToIgnoringNullFields(someInfo(), actual, other, noFieldComparators(), defaultTypeComparators());
  }

  @Test
  public void should_pass_when_some_other_field_is_null_but_not_actual() {
    Jedi actual = new Jedi("Yoda", "Green");
    Jedi other = new Jedi("Yoda", null);
    objects.assertIsEqualToIgnoringNullFields(someInfo(), actual, other, noFieldComparators(), defaultTypeComparators());
  }

  @Test
  public void should_pass_when_fields_are_equal_even_if_objects_types_differ() {
    Person actual = new Person("Homer Simpson");
    CartoonCharacter other = new CartoonCharacter("Homer Simpson");
    objects.assertIsEqualToIgnoringNullFields(someInfo(), actual, other, noFieldComparators(), defaultTypeComparators());
  }

  @Test
  public void should_pass_when_private_fields_differ_but_are_not_compared() {
    boolean allowedToUsePrivateFields = FieldSupport.comparison().isAllowedToUsePrivateFields();
    Assertions.setAllowComparingPrivateFields(false);
    TestClassWithRandomId actual = new TestClassWithRandomId("1", 1);
    TestClassWithRandomId other = new TestClassWithRandomId(null, 1);
    // s field is ignored because null in other, and id also because it is private without public getter
    objects.assertIsEqualToIgnoringNullFields(someInfo(), actual, other, noFieldComparators(), defaultTypeComparators());
    // reset
    Assertions.setAllowComparingPrivateFields(allowedToUsePrivateFields);
  }

  @Test
  public void should_fail_if_actual_is_null() {
    thrown.expectAssertionError(actualIsNull());
    Jedi other = new Jedi("Yoda", "Green");
    objects.assertIsEqualToIgnoringNullFields(someInfo(), null, other, noFieldComparators(), defaultTypeComparators());
  }

  @Test
  public void should_fail_when_some_actual_field_is_null_but_not_other() {
    AssertionInfo info = someInfo();
    Jedi actual = new Jedi("Yoda", null);
    Jedi other = new Jedi("Yoda", "Green");
    try {
      objects.assertIsEqualToIgnoringNullFields(info, actual, other, noFieldComparators(), defaultTypeComparators());
    } catch (AssertionError err) {
      verify(failures).failure(info, shouldBeEqualToIgnoringGivenFields(actual,
                                                                        newArrayList("lightSaberColor"),
                                                                        newArrayList((Object) null),
                                                                        newArrayList((Object) "Green"),
                                                                        newArrayList("strangeNotReadablePrivateField")));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }

  @Test
  public void should_fail_when_a_field_differ() {
    AssertionInfo info = someInfo();
    Jedi actual = new Jedi("Yoda", "Green");
    Jedi other = new Jedi("Soda", "Green");
    try {
      objects.assertIsEqualToIgnoringNullFields(info, actual, other, noFieldComparators(), defaultTypeComparators());
    } catch (AssertionError err) {
      verify(failures).failure(info, shouldBeEqualToIgnoringGivenFields(actual,
                                                                        newArrayList("name"),
                                                                        newArrayList((Object) "Yoda"),
                                                                        newArrayList((Object) "Soda"),
                                                                        newArrayList("strangeNotReadablePrivateField")));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }

  @Test
  public void should_fail_when_one_of_actual_field_to_compare_can_not_be_found_in_the_other_object() {
    thrown.expectIntrospectionErrorWithMessageContaining("Can't find any field or property with name 'lightSaberColor'");
    Jedi actual = new Jedi("Yoda", "Green");
    Employee other = new Employee();
    objects.assertIsEqualToIgnoringNullFields(someInfo(), actual, other, noFieldComparators(), defaultTypeComparators());
  }

  @Test
  public void should_pass_when_class_has_synthetic_field() {
    InnerClass actual = new OuterClass().createInnerClass();
    InnerClass other = new OuterClass().createInnerClass();

    // ensure that the compiler has generated at one synthetic field for the comparison
    assertThat(InnerClass.class.getDeclaredFields()).extracting("synthetic").contains(Boolean.TRUE);

    objects.assertIsEqualToIgnoringNullFields(someInfo(), actual, other, noFieldComparators(), defaultTypeComparators());
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
