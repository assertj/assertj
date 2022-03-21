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
package org.assertj.core.groups;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;
import static org.assertj.core.groups.Properties.extractProperty;

import org.junit.jupiter.api.Test;

/**
 * Tests for <code>{@link Properties#extractProperty(String, Class)}</code>.
 * 
 * @author Yvonne Wang
 * @author Mikhail Mazursky
 */
class Properties_extractProperty_Test {

  @Test
  void should_create_a_new_Properties() {
    Properties<Object> properties = Properties.extractProperty("id", Object.class);
    assertThat(properties.propertyName).isEqualTo("id");
  }

  @Test
  void should_throw_error_if_property_name_is_null() {
    assertThatNullPointerException().isThrownBy(() -> Properties.extractProperty(null, Object.class))
                                    .withMessage("The name of the property to read should not be null");
  }

  @Test
  void should_throw_error_if_property_name_is_empty() {
    assertThatIllegalArgumentException().isThrownBy(() -> Properties.extractProperty("", Object.class))
                                        .withMessage("The name of the property to read should not be empty");
  }

  @Test
  void extractProperty_string_Test() {
    TestItem[] ITEMS = { new TestItem("n1", "v1"), new TestItem("n2", "v2") };

    assertThat(extractProperty("name").from(ITEMS).contains("n1")).isTrue();
    assertThat(extractProperty("name", String.class).from(ITEMS).contains("n1")).isTrue();
  }

  private static final class TestItem {
    private final String name;
    private final String value;

    public TestItem(final String name, final String value) {
      this.name = name;
      this.value = value;
    }

    @SuppressWarnings("unused")
    public String getName() {
      return name;
    }

    @SuppressWarnings("unused")
    public String getValue() {
      return value;
    }

  }

}
