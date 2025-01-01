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
package org.assertj.core.api.recursive.assertion;

import static java.lang.String.format;
import static java.util.Arrays.stream;
import static java.util.stream.Collectors.toList;
import static org.assertj.core.internal.Objects.getDeclaredFieldsIncludingInherited;
import static org.assertj.core.util.introspection.PropertyOrFieldSupport.EXTRACTION;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Optional;

public class DefaultRecursiveAssertionIntrospectionStrategy implements RecursiveAssertionIntrospectionStrategy {

  @Override
  public List<RecursiveAssertionNode> getChildNodesOf(Object node) {
    return getDeclaredFieldsIncludingInherited(node.getClass()).stream()
                                                               .map(field -> toNode(field, node))
                                                               .collect(toList());
  }

  @Override
  public String getDescription() {
    return "DefaultRecursiveAssertionIntrospectionStrategy which introspects all fields (including inherited ones)";
  }

  private static RecursiveAssertionNode toNode(Field field, Object node) {
    String fieldName = field.getName();
    Object fieldValue = EXTRACTION.getSimpleValue(fieldName, node);
    Class<?> fieldType = getFieldType(fieldValue, fieldName, node);
    return new RecursiveAssertionNode(fieldValue, fieldName, fieldType);
  }

  private static Class<?> getFieldType(Object fieldValue, String fieldName, Object targetObject) {
    return fieldValue != null ? fieldValue.getClass() : getFieldType(fieldName, targetObject.getClass());
  }

  private static Class<?> getFieldType(String fieldName, Class<?> objectClass) {
    try {
      Optional<Field> potentialField = stream(objectClass.getDeclaredFields()).filter(field -> fieldName.equals(field.getName()))
                                                                              .findAny();
      if (potentialField.isPresent()) return potentialField.get().getType();
      Class<?> superclass = objectClass.getSuperclass();
      if (superclass != null) return getFieldType(fieldName, superclass);
      throw new NoSuchFieldException();
    } catch (NoSuchFieldException | SecurityException e) {
      throw new IllegalStateException(format("Could not find field %s on class %s, even though its name was retrieved from the class earlier",
                                             fieldName, objectClass.getCanonicalName()),
                                      e);
    }
  }

}
