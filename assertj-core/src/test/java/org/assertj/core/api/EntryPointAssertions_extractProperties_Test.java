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
package org.assertj.core.api;

import static org.assertj.core.api.BDDAssertions.then;

import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Stream;

import org.assertj.core.groups.Properties;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

@DisplayName("EntryPoint assertions extractProperty method")
class EntryPointAssertions_extractProperties_Test extends EntryPointAssertionsBaseTest {

  @ParameterizedTest
  @MethodSource("extractPropertiesFunctions")
  void should_create_Properties(Function<String, Properties<Object>> extractPropertiesFunction) {
    // GIVEN
    String property = "name";
    // WHEN
    Properties<Object> properties = extractPropertiesFunction.apply(property);
    // THEN
    then(properties).extracting("propertyName")
                    .isEqualTo(property);
  }

  private static Stream<Function<String, Properties<Object>>> extractPropertiesFunctions() {
    return Stream.of(Assertions::extractProperty, BDDAssertions::extractProperty, withAssertions::extractProperty);
  }

  @ParameterizedTest
  @MethodSource("extractTypedPropertiesFunctions")
  void should_create_strongly_typed_Properties(BiFunction<String, Class<String>, Properties<String>> extractTypedPropertiesFunction) {
    // GIVEN
    String property = "name";
    // WHEN
    Properties<String> properties = extractTypedPropertiesFunction.apply(property, String.class);
    // THEN
    then(properties).extracting("propertyName")
                    .isEqualTo(property);
  }

  private static Stream<BiFunction<String, Class<String>, Properties<String>>> extractTypedPropertiesFunctions() {
    return Stream.of(Assertions::extractProperty, BDDAssertions::extractProperty, withAssertions::extractProperty);
  }

}
