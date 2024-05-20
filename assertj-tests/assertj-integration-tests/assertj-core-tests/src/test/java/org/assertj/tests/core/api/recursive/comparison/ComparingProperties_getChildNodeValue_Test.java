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
import static org.assertj.core.api.recursive.comparison.ComparingProperties.COMPARING_PROPERTIES;

import org.assertj.core.util.introspection.IntrospectionError;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class ComparingProperties_getChildNodeValue_Test {

  @ParameterizedTest
  @CsvSource({
      "publicStaticValue,No getter for property 'publicStaticValue'",
      "protectedValue,No public getter for property 'protectedValue'",
      "packagePrivateValue,No public getter for property 'packagePrivateValue'",
      "privateField,No getter for property 'privateField'"
  })
  void getChildNodeValue_should_fail_to_read_non_public_properties(String propertyName, String messagePart) {
    // GIVEN
    Properties node = new Properties();
    // WHEN
    Throwable throwable = catchThrowable(() -> COMPARING_PROPERTIES.getChildNodeValue(propertyName, node));
    // THEN
    then(throwable).isInstanceOf(IntrospectionError.class)
                   .hasMessageContaining(messagePart);
  }

  @Test
  void getChildNodeValue_should_read_public_properties() {
    // GIVEN
    Properties node = new Properties();
    // WHEN / THEN
    then(COMPARING_PROPERTIES.getChildNodeValue("Object", node)).isEqualTo("object value");
    then(COMPARING_PROPERTIES.getChildNodeValue("boolean", node)).isEqualTo(true);
    then(COMPARING_PROPERTIES.getChildNodeValue("boolean", node)).isEqualTo(true);
    then(COMPARING_PROPERTIES.getChildNodeValue("booleanWrapper", node)).isEqualTo(true);
    then(COMPARING_PROPERTIES.getChildNodeValue("booleanVariation", node)).isEqualTo(true);
    then(COMPARING_PROPERTIES.getChildNodeValue("booleanVariationWrapper", node)).isEqualTo(true);
    then(COMPARING_PROPERTIES.getChildNodeValue("byte", node)).isEqualTo((byte) 1);
    then(COMPARING_PROPERTIES.getChildNodeValue("short", node)).isEqualTo((short) 2);
    then(COMPARING_PROPERTIES.getChildNodeValue("int", node)).isEqualTo(3);
    then(COMPARING_PROPERTIES.getChildNodeValue("long", node)).isEqualTo((long) 4);
    then(COMPARING_PROPERTIES.getChildNodeValue("float", node)).isEqualTo(1.1f);
    then(COMPARING_PROPERTIES.getChildNodeValue("double", node)).isEqualTo(2.2);
    then(COMPARING_PROPERTIES.getChildNodeValue("char", node)).isEqualTo('x');
  }

  @SuppressWarnings("unused")
  static class Properties {

    // non readable

    public static Object getPublicStaticValue() {
      return "public Static value";
    };

    protected Object getProtectedValue() {
      return "protectedValue value";
    }

    Object getPackagePrivateValue() {
      return "packagePrivateValue value";
    }

    private Object getPrivateValue() {
      return "privateValue value";
    }

    // readable

    public Object getObject() {
      return "object value";
    }

    public boolean isBoolean() {
      return true;
    }

    public Boolean isBooleanWrapper() {
      return true;
    }

    public boolean getBooleanVariation() {
      return true;
    }

    public Boolean getBooleanVariationWrapper() {
      return true;
    }

    public byte getByte() {
      return (byte) 1;
    }

    public short getShort() {
      return (short) 2;
    }

    public int getInt() {
      return 3;
    }

    public long getLong() {
      return 4;
    }

    public float getFloat() {
      return 1.1f;
    }

    public double getDouble() {
      return 2.2;
    }

    public char getChar() {
      return 'x';
    }
  }
}
