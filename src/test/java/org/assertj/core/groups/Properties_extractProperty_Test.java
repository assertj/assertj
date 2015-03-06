/**
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 *
 * Copyright 2012-2015 the original author or authors.
 */
package org.assertj.core.groups;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.test.ExpectedException.none;

import org.assertj.core.test.ExpectedException;
import org.junit.Rule;
import org.junit.Test;

/**
 * Tests for <code>{@link Properties#extractProperty(String, Class)}</code>.
 * 
 * @author Yvonne Wang
 * @author Mikhail Mazursky
 */
public class Properties_extractProperty_Test {

  @Rule
  public ExpectedException thrown = none();

  @Test
  public void should_create_a_new_Properties() {
    Properties<Object> properties = Properties.extractProperty("id", Object.class);
    assertThat(properties.propertyName).isEqualTo("id");
  }

  @Test
  public void should_throw_error_if_property_name_is_null() {
    thrown.expectNullPointerException("The name of the property to read should not be null");
    Properties.extractProperty(null, Object.class);
  }

  @Test
  public void should_throw_error_if_property_name_is_empty() {
    thrown.expectIllegalArgumentException("The name of the property to read should not be empty");
    Properties.extractProperty("", Object.class);
  }
}
