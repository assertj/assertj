/*
 * Copyright 2012-2026 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.assertj.core.api.recursive.comparison;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.util.AssertionsUtil.expectAssertionError;

import java.lang.reflect.Method;

import org.junit.jupiter.api.Test;

class ComparingPropertiesTest {

  public record Foo(Integer id,
      Integer num,
      String name) {

    public String Ignored() {
      return "ignored";
    }

    public String getName() {
      return "foo name";
    }
  }

  @Test
  void should_compare_record_components_when_using_comparing_properties() {
    // Given
    RecursiveComparisonIntrospectionStrategy strategy = new ComparingProperties();

    Foo ob1 = new Foo(1, 22, "name");
    Foo ob2 = new Foo(2, 22, "other");

    // When
    var assertionError = expectAssertionError(() -> assertThat(ob1)
                                                                   .usingRecursiveComparison()
                                                                   .withIntrospectionStrategy(strategy)
                                                                   .ignoringFields("id")
                                                                   .isEqualTo(ob2));

    // Then
    then(strategy.getChildrenNodeNamesOf(ob1)).containsExactlyInAnyOrder("id", "num", "name");
    then(assertionError).isInstanceOf(AssertionError.class)
                        .hasMessageContaining("name");
  }

  @Test
  void should_return_record_component_accessors_but_not_custom_getters() {
    // Given & When
    var getters = ComparingProperties.gettersIncludingInheritedOf(Foo.class);

    // Then
    then(getters)
                 .extracting(Method::getName)
                 .containsExactlyInAnyOrder("id", "num", "name");
  }

  @Test
  void should_use_record_component_accessors_but_not_custom_getters() {
    // Given
    Foo ob1 = new Foo(1, 22, "name");
    ComparingProperties comparingProperties = new ComparingProperties();
    // When
    var value = comparingProperties.getChildNodeValue("name", ob1);
    // Then
    then(value).isEqualTo(ob1.name());
  }
}
