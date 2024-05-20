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
package org.assertj.tests.core.api.recursive.comparison;

import static org.assertj.core.api.Assertions.catchThrowable;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.api.recursive.comparison.ComparingFields.COMPARING_FIELDS;

import org.assertj.core.util.introspection.IntrospectionError;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class ComparingFields_getChildNodeValue_Test {

  @ParameterizedTest
  @CsvSource({
      "publicField, publicField value",
      "protectedField, protectedField value",
      "packagePrivateField, packagePrivateField value",
      "privateField, privateField value"
  })
  void getChildNodeValue_should_read_fields_whatever_their_visibility_is(String fieldName, String expectedValue) {
    // GIVEN
    Fields node = new Fields();
    // WHEN
    Object value = COMPARING_FIELDS.getChildNodeValue(fieldName, node);
    // THEN
    then(value).isEqualTo(expectedValue);
  }

  @Test
  void getChildNodeValue_should_read_primitive_fields() {
    // GIVEN
    Fields node = new Fields();
    // WHEN / THEN
    then(COMPARING_FIELDS.getChildNodeValue("privateBoolean", node)).isEqualTo(true);
    then(COMPARING_FIELDS.getChildNodeValue("privateByte", node)).isEqualTo((byte) 1);
    then(COMPARING_FIELDS.getChildNodeValue("privateShort", node)).isEqualTo((short) 2);
    then(COMPARING_FIELDS.getChildNodeValue("privateInt", node)).isEqualTo(3);
    then(COMPARING_FIELDS.getChildNodeValue("privateLong", node)).isEqualTo((long) 4);
    then(COMPARING_FIELDS.getChildNodeValue("privateFloat", node)).isEqualTo(1.1f);
    then(COMPARING_FIELDS.getChildNodeValue("privateDouble", node)).isEqualTo(2.2);
    then(COMPARING_FIELDS.getChildNodeValue("privateChar", node)).isEqualTo('x');
  }

  @Test
  void getChildNodeValue_does_not_read_static_fields() {
    // GIVEN
    Fields node = new Fields();
    // WHEN
    Throwable throwable = catchThrowable(() -> COMPARING_FIELDS.getChildNodeValue("publicStaticField", node));
    // THEN
    then(throwable).isInstanceOf(IntrospectionError.class)
                   .hasMessageContaining("Unable to obtain the value of the field <'publicStaticField'>");
  }

  @SuppressWarnings("unused")
  static class Fields {
    public static final Object publicStaticField = "publicStaticField value";
    public final Object publicField = "publicField value";
    protected final Object protectedField = "protectedField value";
    final Object packagePrivateField = "packagePrivateField value";
    private final Object privateField = "privateField value";
    private final boolean privateBoolean = true;
    private final byte privateByte = 1;
    private final short privateShort = 2;
    private final int privateInt = 3;
    private final long privateLong = 4;
    private final float privateFloat = 1.1f;
    private final double privateDouble = 2.2;
    private final char privateChar = 'x';
  }
}
