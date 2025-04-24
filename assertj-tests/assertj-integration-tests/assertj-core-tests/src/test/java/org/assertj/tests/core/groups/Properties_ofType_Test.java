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
 * Copyright 2012-2025 the original author or authors.
 */
package org.assertj.tests.core.groups;

import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.groups.Properties.extractProperty;

import org.assertj.core.groups.Properties;
import org.assertj.core.util.introspection.FieldSupport;
import org.junit.jupiter.api.Test;

/**
 * Tests for <code>{@link Properties#ofType(Class)}</code>.
 *
 * @author Olivier Michallat
 */
class Properties_ofType_Test {

  @Test
  void should_create_a_new_Properties() {
    // WHEN
    Properties<String> properties = extractProperty("id").ofType(String.class);
    // THEN
    then(FieldSupport.EXTRACTION.fieldValue("propertyName", String.class, properties)).isEqualTo("id");
    then(FieldSupport.EXTRACTION.fieldValue("propertyType", Class.class, properties)).isEqualTo(String.class);
  }
}
