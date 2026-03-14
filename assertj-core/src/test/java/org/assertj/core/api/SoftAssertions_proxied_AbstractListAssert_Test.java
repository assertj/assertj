package org.assertj.core.api;

import static org.assertj.core.api.BDDAssertions.thenNoException;
import static org.assertj.core.util.Lists.list;

import java.util.List;

import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link SoftAssertions}, to verify that proxied custom assertions inheriting from {@link AbstractListAssert} do not throw {@link ClassCastException}.
 *
 * @author HuitaePark
 */
public class SoftAssertions_proxied_AbstractListAssert_Test {

  @Test
  void should_not_throw_class_cast_exception_when_calling_methods_with_array_args_on_proxied_custom_assertion() {
    // GIVEN
    List<String> actual = list("a", "b");
    List<String> expected = list("a", "b");

    // WHEN / THEN
    thenNoException().isThrownBy(() -> {
      SoftAssertions.assertSoftly(softly -> {
        softly.proxy(TestListAssert.class, List.class, actual)
              .containsExactlyInAnyOrderElementsOf(expected);
      });
    });
  }

  static class TestListAssert extends AbstractListAssert<TestListAssert, List<String>, String, ObjectAssert<String>> {

    public TestListAssert(List<String> actual) {
      super(actual, TestListAssert.class);
    }

    @Override
    protected TestListAssert newAbstractIterableAssert(Iterable<? extends String> iterable) {
      return new TestListAssert(Lists.newArrayList(iterable));
    }

    @Override
    protected ObjectAssert<String> toAssert(String value, String description) {
      return new ObjectAssert<>(value);
    }
  }
}
