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
package org.assertj.core.api.recursive.comparison;

import static org.assertj.core.api.BDDAssertions.then;

import java.util.Set;

import org.junit.jupiter.api.Test;

@SuppressWarnings("unused")
class DefaultRecursiveComparisonIntrospectionStrategy_getChildrenNodeNamesOf_Test {

  private static final DefaultRecursiveComparisonIntrospectionStrategy DEFAULT_RECURSIVE_COMPARISON_INTROSPECTION_STRATEGY = new DefaultRecursiveComparisonIntrospectionStrategy();

  @Test
  void getChildrenNodeNamesOf_return_all_fields_names() {
    // GIVEN
    FieldsAndProperties node = new FieldsAndProperties();
    // WHEN
    Set<String> nodePropertiesNames = DEFAULT_RECURSIVE_COMPARISON_INTROSPECTION_STRATEGY.getChildrenNodeNamesOf(node);
    // THEN
    then(nodePropertiesNames).containsExactlyInAnyOrder("publicField", "protectedField", "packagePrivateField", "privateField",
                                                        "privateBoolean", "privateByte", "privateShort", "privateInt",
                                                        "privateLong", "privateFloat", "privateDouble", "privateChar")

                             .doesNotContain("object", "boolean", "booleanWrapper", "booleanVariation",
                                             "booleanVariationWrapper", "byte", "short", "int", "long", "float",
                                             "double", "char");
  }

  @Test
  void getChildrenNodeNamesOf_caches_all_fields_names() {
    // GIVEN
    FieldsAndProperties node = new FieldsAndProperties();
    // Create new instance to ensure that cache is empty before first getChildrenNodeNamesOf call
    ComparingProperties comparingProperties = new ComparingProperties();
    // WHEN
    Set<String> nodePropertiesNames = comparingProperties.getChildrenNodeNamesOf(node);
    Set<String> cachedNodePropertiesNames = comparingProperties.getChildrenNodeNamesOf(node);
    // THEN
    then(cachedNodePropertiesNames).isSameAs(nodePropertiesNames);
  }

  static class FieldsAndProperties {

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

  @Test
  void getChildrenNodeNamesOf_ignores_synthetic_fields() {
    // GIVEN
    Outer.Inner inner = new Outer().new Inner();
    // WHEN
    Set<String> childrenNodeNames = DEFAULT_RECURSIVE_COMPARISON_INTROSPECTION_STRATEGY.getChildrenNodeNamesOf(inner);
    // THEN
    then(childrenNodeNames).containsOnly("innerField");
  }

  static class Outer {
    class Inner {
      // compiler generates a synthetic field to represent the appropriate instance of the Outer class.
      private final String innerField = "innerField value";
    }
  }

  @Test
  void getChildrenNodeNamesOf_returns_inherited_fields() {
    // GIVEN
    SubClass actual = new SubClass();
    // WHEN
    Set<String> childrenNodeNames = DEFAULT_RECURSIVE_COMPARISON_INTROSPECTION_STRATEGY.getChildrenNodeNamesOf(actual);
    // THEN
    then(childrenNodeNames).containsExactlyInAnyOrder("superClassField1", "superClassField2", "subClassField");
  }

  static class SuperClass {
    private final String superClassField1 = "superClassField1 value";
    private final String superClassField2 = "superClassField2 value";

  }

  static class SubClass extends SuperClass {
    private final String subClassField = "subClassField value";
  }
}
