package org.assertj.core.internal.objects;

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.internal.ObjectsBaseTest;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.error.ShouldHaveNoNullFields.shouldHaveNoNullFieldsExcept;
import static org.assertj.core.test.TestData.someInfo;
import static org.assertj.core.util.AssertionsUtil.expectAssertionError;
import static org.assertj.core.util.FailureMessages.actualIsNull;

public class Objects_assertHasNoNullFieldsOrPropertiesExcept_Test extends ObjectsBaseTest {
  protected static final AssertionInfo INFO = someInfo();

  @Test
  void should_pass_if_actual_has_no_null_fields_except_given() {
    // GIVEN
    Object actual = new Objects_assertHasNoNullFieldsOrPropertiesExcept_Test.Data();
    // WHEN/THEN
    objects.assertHasNoNullFieldsOrPropertiesExcept (INFO, actual, "field2", "field3");
  }

  @Test
  void should_fail_if_actual_is_null() {
    // GIVEN
    Object actual = null;
    String fieldName = "field1";
    // WHEN
    AssertionError error = expectAssertionError(() -> objects.assertHasNoNullFieldsOrPropertiesExcept(INFO, actual, fieldName));
    // THEN
    assertThat(error).hasMessage(actualIsNull());
  }

  @Test
  void should_fail_if_ignored_fields_are_null() {
    // GIVEN
    Object actual = new Objects_assertHasNoNullFieldsOrPropertiesExcept_Test.Data();
    String fieldName = "field3";
    String illegalNullField = "field2";
    // WHEN
    AssertionError error = expectAssertionError(() -> objects.assertHasNoNullFieldsOrPropertiesExcept(INFO, actual, fieldName));
    // THEN
    assertThat(error).hasMessage(shouldHaveNoNullFieldsExcept(actual, List.of(illegalNullField), List.of(fieldName)).create());
  }

  @SuppressWarnings("unused")
  private static class Data {

    private Object field1 = "foo";
    private Object field2 = null;
    private Object field3 = null;
    private static Object staticField;

    @Override
    public String toString() {
      return "data";
    }

    public Object getField3() {
      return "bar";
    }

  }
}
