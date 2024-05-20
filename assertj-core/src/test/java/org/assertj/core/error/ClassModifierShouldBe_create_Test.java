/*
 * Copyright © 2024 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.assertj.core.error;

import static java.lang.String.format;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.error.ClassModifierShouldBe.shouldBeFinal;
import static org.assertj.core.error.ClassModifierShouldBe.shouldBePrivate;
import static org.assertj.core.error.ClassModifierShouldBe.shouldBePublic;
import static org.assertj.core.error.ClassModifierShouldBe.shouldNotBeFinal;

import org.assertj.core.internal.TestDescription;
import org.junit.jupiter.api.Test;

class ClassModifierShouldBe_create_Test {

  @Test
  void should_create_error_message_for_shouldBeFinal() {
    // GIVEN
    Class<?> nonFinalClass = Object.class;
    // WHEN
    String error = shouldBeFinal(nonFinalClass).create(new TestDescription("TEST"));
    // THEN
    then(error).isEqualTo(format("[TEST] %n" +
                                 "Expecting actual:%n" +
                                 "  java.lang.Object%n" +
                                 "to be a \"final\" class but was \"public\"."));
  }

  @Test
  void should_create_error_message_for_shouldNotBeFinal() {
    // GIVEN
    Class<?> finalClass = String.class;
    // WHEN
    String error = shouldNotBeFinal(finalClass).create(new TestDescription("TEST"));
    // THEN
    then(error).isEqualTo(format("[TEST] %n" +
                                 "Expecting actual:%n" +
                                 "  java.lang.String%n" +
                                 "not to be a \"final\" class but was \"public final\"."));
  }

  @Test
  void should_create_error_message_for_shouldBePublic() {
    // GIVEN
    Class<?> packagePrivateClass = PackagePrivateClass.class;
    // WHEN
    String error = shouldBePublic(packagePrivateClass).create(new TestDescription("TEST"));
    // THEN
    then(error).isEqualTo(format("[TEST] %n" +
                                 "Expecting actual:%n" +
                                 "  org.assertj.core.error.ClassModifierShouldBe_create_Test.PackagePrivateClass%n" +
                                 "to be a \"public\" class but was \"package-private\"."));
  }

  @Test
  void should_create_error_message_for_shouldBePrivate() {
    // GIVEN
    Class<?> packagePrivateClass = PackagePrivateClass.class;
    // WHEN
    String error = shouldBePrivate(packagePrivateClass).create(new TestDescription("TEST"));
    // THEN
    then(error).isEqualTo(format("[TEST] %n" +
                                 "Expecting actual:%n" +
                                 "  org.assertj.core.error.ClassModifierShouldBe_create_Test.PackagePrivateClass%n" +
                                 "to be a \"private\" class but was \"package-private\"."));
  }

  class PackagePrivateClass {
  }

}
