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
package org.assertj.core.api.junit.jupiter;

import static org.assertj.core.api.junit.jupiter.TestKitUtils.assertThatTest;

import org.assertj.core.api.AbstractSoftAssertions;
import org.assertj.core.api.SoftAssertions;
import org.assertj.core.api.SoftAssertionsProvider;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.ExtensionConfigurationException;

@DisplayName("SoftAssertionsExtension injection sanity checking test")
class SoftAssertionsExtension_InjectionSanityChecking_Test {

  @ExtendWith(SoftAssertionsExtension.class)
  static abstract class TestBase {
    @Test
    void myTest() {}
  }

  class NoDefaultSoftAssertionsProvider extends AbstractSoftAssertions {
    public NoDefaultSoftAssertionsProvider(@SuppressWarnings("unused") String param) {}
  }

  @Disabled("Run by the testkit")
  static class NoDefaultConstructorTest extends TestBase {
    @InjectSoftAssertions
    NoDefaultSoftAssertionsProvider usp;
  }

  @Test
  void field_with_no_default_constructor_throws_exception() {
    assertThatTest(NoDefaultConstructorTest.class).isInstanceOf(ExtensionConfigurationException.class)
                                                  .hasMessage("[usp] SoftAssertionsProvider [%s] does not have a default constructor",
                                                              NoDefaultSoftAssertionsProvider.class.getName());
  }

  static abstract class AbstractProvider implements SoftAssertionsProvider {
  }

  @Disabled("Run by the testkit")
  static class AbstractProviderTest extends TestBase {
    @InjectSoftAssertions
    AbstractProvider usp;
  }

  @Test
  void field_with_abstract_provider_throws_exception() {
    assertThatTest(AbstractProviderTest.class).isInstanceOf(ExtensionConfigurationException.class)
                                              .hasMessage("[usp] SoftAssertionsProvider [%s] is abstract and cannot be instantiated.",
                                                          AbstractProvider.class);
  }

  @Disabled("Run by the testkit")
  static class FinalField extends TestBase {
    @InjectSoftAssertions
    final SoftAssertions usp = null;

    @Override
    @Test
    void myTest() {}
  }

  @Test
  void final_field__throws_exception() {
    assertThatTest(FinalField.class).isInstanceOf(ExtensionConfigurationException.class)
                                    .hasMessage("[usp] SoftAssertionsProvider field must not be static or final.");
  }

  @Disabled("Run by the testkit")
  static class StaticField extends TestBase {
    @InjectSoftAssertions
    static SoftAssertions usp = null;

    @Override
    @Test
    void myTest() {}
  }

  @Test
  void static_field_throws_exception() {
    assertThatTest(StaticField.class).isInstanceOf(ExtensionConfigurationException.class)
                                     .hasMessage("[usp] SoftAssertionsProvider field must not be static or final.");
  }

  @Disabled("Run by the testkit")
  static class WrongType extends TestBase {
    @InjectSoftAssertions
    String usp;

    @Override
    @Test
    void myTest() {}
  }

  @Test
  void wrong_type_throws_exception() {
    assertThatTest(WrongType.class).isInstanceOf(ExtensionConfigurationException.class)
                                   .hasMessage("[usp] field is not a SoftAssertionsProvider (java.lang.String).");
  }
}
