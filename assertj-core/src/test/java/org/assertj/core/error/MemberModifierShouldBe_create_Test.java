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
 * Copyright 2012-2024 the original author or authors.
 */
package org.assertj.core.error;

import static java.lang.String.format;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.error.MemberModifierShouldBe.shouldBeFinal;
import static org.assertj.core.error.MemberModifierShouldBe.shouldBePackagePrivate;
import static org.assertj.core.error.MemberModifierShouldBe.shouldBeProtected;
import static org.assertj.core.error.MemberModifierShouldBe.shouldBePublic;
import static org.assertj.core.error.MemberModifierShouldBe.shouldBeStatic;
import static org.assertj.core.error.MemberModifierShouldBe.shouldNotBeFinal;
import static org.assertj.core.error.MemberModifierShouldBe.shouldNotBeStatic;

import org.assertj.core.internal.TestDescription;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Member;

/**
 * Tests for
 * <code>{@link MemberModifierShouldBe}</code>
 *
 * @author William Bakker
 */
class MemberModifierShouldBe_create_Test {

  @Test
  void should_create_error_message_for_should_be_final() throws NoSuchMethodException {
    // GIVEN
    Member nonFinalMember = MemberModifierShouldBe_create_Test.class.getDeclaredMethod("privateMethod");
    // WHEN
    String error = shouldBeFinal(nonFinalMember).create(new TestDescription("TEST"));
    // THEN
    then(error).isEqualTo(format("[TEST] %n" +
                                 "Expecting actual:%n" +
                                 "  private void org.assertj.core.error.MemberModifierShouldBe_create_Test.privateMethod()%n" +
                                 "to be a \"final\" member but was \"private\"."));
  }

  @Test
  void should_create_error_message_for_should_not_be_final() throws NoSuchMethodException {
    // GIVEN
    Member finalMember = MemberModifierShouldBe_create_Test.class.getDeclaredMethod("finalMethod");
    // WHEN
    String error = shouldNotBeFinal(finalMember).create(new TestDescription("TEST"));
    // THEN
    then(error).isEqualTo(format("[TEST] %n" +
                                 "Expecting actual:%n" +
                                 "  final void org.assertj.core.error.MemberModifierShouldBe_create_Test.finalMethod()%n" +
                                 "not to be a \"final\" member but was \"package-private final\"."));
  }

  @Test
  void should_create_error_message_for_should_be_public() throws NoSuchMethodException {
    // GIVEN
    Member privateMember = MemberModifierShouldBe_create_Test.class.getDeclaredMethod("privateMethod");
    // WHEN
    String error = shouldBePublic(privateMember).create(new TestDescription("TEST"));
    // THEN
    then(error).isEqualTo(format("[TEST] %n" +
                                 "Expecting actual:%n" +
                                 "  private void org.assertj.core.error.MemberModifierShouldBe_create_Test.privateMethod()%n" +
                                 "to be a \"public\" member but was \"private\"."));
  }

  @Test
  void should_create_error_message_for_should_be_protected() throws NoSuchMethodException {
    // GIVEN
    Member privateMember = MemberModifierShouldBe_create_Test.class.getDeclaredMethod("privateMethod");
    // WHEN
    String error = shouldBeProtected(privateMember).create(new TestDescription("TEST"));
    // THEN
    then(error).isEqualTo(format("[TEST] %n" +
                                 "Expecting actual:%n" +
                                 "  private void org.assertj.core.error.MemberModifierShouldBe_create_Test.privateMethod()%n" +
                                 "to be a \"protected\" member but was \"private\"."));
  }

  @Test
  void should_create_error_message_for_should_be_package_private() throws NoSuchMethodException {
    // GIVEN
    Member privateMember = MemberModifierShouldBe_create_Test.class.getDeclaredMethod("privateMethod");
    // WHEN
    String error = shouldBePackagePrivate(privateMember).create(new TestDescription("TEST"));
    // THEN
    then(error).isEqualTo(format("[TEST] %n" +
                                 "Expecting actual:%n" +
                                 "  private void org.assertj.core.error.MemberModifierShouldBe_create_Test.privateMethod()%n" +
                                 "to be a \"package-private\" member but was \"private\"."));
  }

  @Test
  void should_create_error_message_for_should_be_static() throws NoSuchMethodException {
    // GIVEN
    Member notStaticMember = MemberModifierShouldBe_create_Test.class.getDeclaredMethod("privateMethod");
    // WHEN
    String error = shouldBeStatic(notStaticMember).create(new TestDescription("TEST"));
    // THEN
    then(error).isEqualTo(format("[TEST] %n" +
                                 "Expecting actual:%n" +
                                 "  private void org.assertj.core.error.MemberModifierShouldBe_create_Test.privateMethod()%n" +
                                 "to be a \"static\" member but was \"private\"."));
  }

  @Test
  void should_create_error_message_for_should_not_be_static() throws NoSuchMethodException {
    // GIVEN
    Member staticMember = MemberModifierShouldBe_create_Test.class.getDeclaredMethod("staticMethod");
    // WHEN
    String error = shouldNotBeStatic(staticMember).create(new TestDescription("TEST"));
    // THEN
    then(error).isEqualTo(format("[TEST] %n" +
                                 "Expecting actual:%n" +
                                 "  static void org.assertj.core.error.MemberModifierShouldBe_create_Test.staticMethod()%n" +
                                 "not to be a \"static\" member but was \"package-private static\"."));
  }

  private void privateMethod() {};

  void packagePrivateMethod() {};

  protected void protectedMethod() {};

  public void publicMethod() {};

  static void staticMethod() {};

  final void finalMethod() {};
}
