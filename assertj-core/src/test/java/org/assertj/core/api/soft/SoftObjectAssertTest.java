package org.assertj.core.api.soft;

import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.api.soft.SoftAssertFactories.OPTIONAL;
import static org.assertj.core.api.soft.SoftAssertFactories.STRING;
import static org.assertj.core.api.soft.SoftAssertFactories.optional;
import static org.assertj.core.error.ShouldBeEmpty.shouldBeEmpty;
import static org.assertj.core.util.AssertionsUtil.expectMultipleAssertionsError;

import java.util.List;
import java.util.Optional;

import org.assertj.core.api.GeneratedSoftAssertions;
import org.assertj.core.testkit.Jedi;
import org.junit.jupiter.api.Test;

@SuppressWarnings("ResultOfMethodCallIgnored")
public final class SoftObjectAssertTest {
  private final GeneratedSoftAssertions softly = new GeneratedSoftAssertions();

  @Test
  void should_gather_all_assertion_errors() {
    // GIVEN
    Object actual = "foo";
    softly.assertThat(actual)
          .isEqualTo("foo")
          .isSameAs("bar")
          .hasToString("baz");
    // WHEN
    var multipleAssertionsError = expectMultipleAssertionsError(softly::assertAll);
    // THEN
    List<AssertionError> errors = multipleAssertionsError.getErrors();
    then(errors).hasSize(2);
    then(errors.get(0)).hasMessageContaining("bar");
    then(errors.get(1)).hasMessageContaining("baz");
  }

  @Test
  void should_support_and_delegate_to_non_assertion_methods() {
    // GIVEN
    Object actual = "foo";
    softly.assertThat(actual)
          .as("test")
          .isEqualTo("bar")
          .isEqualTo("baz");
    // WHEN
    var multipleAssertionsError = expectMultipleAssertionsError(softly::assertAll);
    // THEN
    then(multipleAssertionsError).hasMessageContaining("test");
    List<AssertionError> errors = multipleAssertionsError.getErrors();
    then(errors).hasSize(2);
    then(errors.get(0)).hasMessageContaining("bar");
    then(errors.get(1)).hasMessageContaining("baz");
  }

  @Test
  void should_support_asInstanceOf_navigation_methods() {
    // GIVEN
    Object actual = "foo";
    softly.assertThat(actual).asInstanceOf(STRING).isEmpty();
    // WHEN
    var multipleAssertionsError = expectMultipleAssertionsError(softly::assertAll);
    // THEN
    List<AssertionError> errors = multipleAssertionsError.getErrors();
    then(errors).hasSize(1);
    then(errors.get(0)).hasMessage(shouldBeEmpty("foo").create());
  }

  @Test
  void should_support_asInstanceOf_optional_navigation_methods() {
    // GIVEN
    Object actual = Optional.of("foo");
    softly.assertThat(actual)
          .asInstanceOf(OPTIONAL)
          .isPresent()
          .contains(123)
          .contains("baz");
    // WHEN
    var multipleAssertionsError = expectMultipleAssertionsError(softly::assertAll);
    // THEN
    List<AssertionError> errors = multipleAssertionsError.getErrors();
    then(errors).hasSize(2);
    then(errors.get(0)).hasMessageContainingAll("to contain", "123");
    then(errors.get(1)).hasMessageContainingAll("to contain", "baz");
  }

  @Test
  void should_support_generic_typed_asInstanceOf_optional_navigation_methods() {
    // GIVEN
    Object actual = Optional.of("foo");
    softly.assertThat(actual)
          .asInstanceOf(optional(String.class))
          .isPresent()
          .contains("bar")
          .contains("baz");
    // WHEN
    var multipleAssertionsError = expectMultipleAssertionsError(softly::assertAll);
    // THEN
    List<AssertionError> errors = multipleAssertionsError.getErrors();
    then(errors).hasSize(2);
    then(errors.get(0)).hasMessageContainingAll("to contain", "bar");
    then(errors.get(1)).hasMessageContainingAll("to contain", "baz");
  }

  @Test
  void should_support_extracting_with_function_navigation_methods() {
    // GIVEN
    var yoda = new Jedi("Yoda", "Green");
    softly.assertThat(yoda)
          .extracting(value -> value.lightSaberColor)
          .isEqualTo("Green")
          .isEqualTo("Blue")
          .isEqualTo("Red");
    // WHEN
    var multipleAssertionsError = expectMultipleAssertionsError(softly::assertAll);
    // THEN
    List<AssertionError> errors = multipleAssertionsError.getErrors();
    then(errors).hasSize(2);
    then(errors.get(0)).hasMessageContaining("Blue");
    then(errors.get(1)).hasMessageContaining("Red");
  }

  @Test
  void should_support_strongly_typed_extracting_with_function_navigation_methods() {
    // GIVEN
    var yoda = new Jedi("Yoda", "Green");
    softly.assertThat(yoda)
          .extracting(value -> value.lightSaberColor, STRING)
          .containsIgnoringCase("green")
          .containsIgnoringCase("Red")
          .isEmpty();
    // WHEN
    var multipleAssertionsError = expectMultipleAssertionsError(softly::assertAll);
    // THEN
    List<AssertionError> errors = multipleAssertionsError.getErrors();
    then(errors).hasSize(2);
    then(errors.get(0)).hasMessageContaining("Red");
    then(errors.get(1)).hasMessage(shouldBeEmpty("Green").create());
  }

  @Test
  void should_support_extracting_with_single_string_navigation_methods() {
    // GIVEN
    var yoda = new Jedi("Yoda", "Green");
    softly.assertThat(yoda)
          .extracting("lightSaberColor")
          .isEqualTo("Green")
          .isEqualTo("Blue")
          .isEqualTo("Red");
    // WHEN
    var multipleAssertionsError = expectMultipleAssertionsError(softly::assertAll);
    // THEN
    List<AssertionError> errors = multipleAssertionsError.getErrors();
    then(errors).hasSize(2);
    then(errors.get(0)).hasMessageContaining("Blue");
    then(errors.get(1)).hasMessageContaining("Red");
  }

  @Test
  void should_support_strongly_typed_extracting_with_string_navigation_methods() {
    // GIVEN
    var yoda = new Jedi("Yoda", "Green");
    softly.assertThat(yoda)
          .extracting("lightSaberColor", STRING)
          .containsIgnoringCase("green")
          .containsIgnoringCase("Red")
          .isEmpty();
    // WHEN
    var multipleAssertionsError = expectMultipleAssertionsError(softly::assertAll);
    // THEN
    List<AssertionError> errors = multipleAssertionsError.getErrors();
    then(errors).hasSize(2);
    then(errors.get(0)).hasMessageContaining("Red");
    then(errors.get(1)).hasMessage(shouldBeEmpty("Green").create());
  }


}
