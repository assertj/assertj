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
import static org.assertj.core.api.recursive.comparison.ComparingFields.COMPARING_FIELDS;

import java.util.Set;

import org.assertj.core.api.recursive.comparison.ComparingFields;
import org.junit.jupiter.api.Test;

@SuppressWarnings("unused")
class ComparingFields_getChildrenNodeNamesOf_Test {

  @Test
  void getChildrenNodeNamesOf_return_all_instance_fields_names() {
    // GIVEN
    Fields node = new Fields();
    // WHEN
    Set<String> childrenNodeNames = COMPARING_FIELDS.getChildrenNodeNamesOf(node);
    // THEN
    then(childrenNodeNames).containsExactlyInAnyOrder("publicField", "protectedField", "packagePrivateField", "privateField",
                                                      "privateBoolean", "privateByte", "privateShort", "privateInt",
                                                      "privateLong", "privateFloat", "privateDouble", "privateChar");
  }

  @Test
  void getChildrenNodeNamesOf_caches_instance_fields_names() {
    // GIVEN
    Fields node = new Fields();
    // Create new instance to ensure that cache is empty before first getChildrenNodeNamesOf call
    ComparingFields comparingFields = new ComparingFields();
    // WHEN
    Set<String> childrenNodeNames = comparingFields.getChildrenNodeNamesOf(node);
    Set<String> cachedChildrenNodeNames = comparingFields.getChildrenNodeNamesOf(node);
    // THEN
    then(cachedChildrenNodeNames).isSameAs(childrenNodeNames);
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

  @Test
  void getChildrenNodeNamesOf_ignores_synthetic_fields() {
    // GIVEN
    Outer.Inner inner = new Outer().new Inner();
    // WHEN
    Set<String> childrenNodeNames = COMPARING_FIELDS.getChildrenNodeNamesOf(inner);
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
    Set<String> childrenNodeNames = COMPARING_FIELDS.getChildrenNodeNamesOf(actual);
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
