/*
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 *
 * Copyright 2012-2022 the original author or authors.
 */
package org.assertj.guava.testkit.junit.jupiter;

import static org.assertj.core.api.BDDAssertions.then;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class DefaultDisplayNameGeneratorTest {

  private final DefaultDisplayNameGenerator underTest = new DefaultDisplayNameGenerator();

  @ParameterizedTest
  @CsvSource({
      "org.assertj.guava.testkit.junit.jupiter.SomeAssert_someMethod_Test, SomeAssert someMethod",
      "org.assertj.guava.testkit.junit.jupiter.SomeAssert_someMethod_with_SomeType_Test, SomeAssert someMethod with SomeType"
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
