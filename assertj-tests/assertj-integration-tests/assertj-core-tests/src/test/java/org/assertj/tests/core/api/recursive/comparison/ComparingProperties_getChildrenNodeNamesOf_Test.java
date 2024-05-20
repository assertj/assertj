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

import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.api.recursive.comparison.ComparingProperties.COMPARING_PROPERTIES;

import java.util.Set;

import org.assertj.core.api.recursive.comparison.ComparingProperties;
import org.junit.jupiter.api.Test;

@SuppressWarnings("unused")
class ComparingProperties_getChildrenNodeNamesOf_Test {

  @Test
  void getChildrenNodeNamesOf_return_all_properties_names() {
    // GIVEN
    Properties node = new Properties();
    // WHEN
    Set<String> nodePropertiesNames = COMPARING_PROPERTIES.getChildrenNodeNamesOf(node);
    // THEN
    then(nodePropertiesNames).containsExactlyInAnyOrder("object", "boolean", "booleanWrapper", "booleanVariation",
                                                        "booleanVariationWrapper", "byte", "short", "int", "long", "float",
                                                        "double", "char")
                             .doesNotContain("privateValue", "packagePrivateValue", "protectedValue", "publicStaticValue");
  }

  @Test
  void getChildrenNodeNamesOf_returns_inherited_default_properties_names() {
    // GIVEN
    PropertiesInterface actual = new PropertiesImpl();
    // WHEN
    Set<String> childrenNodeNames = COMPARING_PROPERTIES.getChildrenNodeNamesOf(actual);
    // THEN
    then(childrenNodeNames).containsExactlyInAnyOrder("string")
                           .doesNotContain("staticValue", "invalidValue");
  }

  @Test
  void getChildrenNodeNamesOf_caches_all_properties_names() {
    // GIVEN
    Properties node = new Properties();
    // Create new instance to ensure that cache is empty before first getChildrenNodeNamesOf call
    ComparingProperties comparingProperties = new ComparingProperties();
    // WHEN
    Set<String> nodePropertiesNames = comparingProperties.getChildrenNodeNamesOf(node);
    Set<String> cachedNodePropertiesNames = comparingProperties.getChildrenNodeNamesOf(node);
    // THEN
    then(cachedNodePropertiesNames).isSameAs(nodePropertiesNames);
  }

  static class Properties {

    // non readable

    public static Object getPublicStaticValue() {
      return "public Static value";
    }

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

  interface PropertiesInterface {

    // not readable

    static String getStaticValue() {
      return "static value";
    }

    default String invalidValue() {
      return "";
    }

    // readable

    default String getString() {
      return "string value";
    }
  }

  static class PropertiesImpl implements PropertiesInterface {
  }

}
