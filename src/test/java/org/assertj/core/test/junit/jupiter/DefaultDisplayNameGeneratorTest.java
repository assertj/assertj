package org.assertj.core.test.junit.jupiter;

import static org.assertj.core.api.BDDAssertions.then;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class DefaultDisplayNameGeneratorTest {

  private final DefaultDisplayNameGenerator underTest = new DefaultDisplayNameGenerator();

  @ParameterizedTest
  @CsvSource({
      "org.assertj.core.test.junit.jupiter.SomeAssert_someMethod_Test, SomeAssert someMethod",
      "org.assertj.core.test.junit.jupiter.SomeAssert_someMethod_with_SomeType_Test, SomeAssert someMethod with SomeType"
  })
  void generateDisplayNameForClass_should_remove_test_suffix(Class<?> testClass, String expected) {
    // WHEN
    String displayName = underTest.generateDisplayNameForClass(testClass);
    // THEN
    then(displayName).isEqualTo(expected);
  }

}

@SuppressWarnings("unused")
class SomeAssert_someMethod_Test {
}

@SuppressWarnings("unused")
class SomeAssert_someMethod_with_SomeType_Test {
}
