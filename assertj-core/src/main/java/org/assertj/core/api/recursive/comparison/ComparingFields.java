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
package org.assertj.core.api.recursive.comparison;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.assertj.core.internal.Objects;
import org.assertj.core.util.introspection.FieldSupport;

/**
 * A {@link RecursiveComparisonIntrospectionStrategy} that introspects fields including inherited ones but ignores static and
 * synthetic fields.
 */
public class ComparingFields implements RecursiveComparisonIntrospectionStrategy {

  public static final ComparingFields COMPARING_FIELDS = new ComparingFields();

  // use ConcurrentHashMap in case this strategy instance is used in a multi-thread context
  private final Map<Class<?>, Set<String>> fieldNamesPerClass = new ConcurrentHashMap<>();

  @Override
  public Set<String> getChildrenNodeNamesOf(Object node) {
    if (node == null) return new HashSet<>();
    return fieldNamesPerClass.computeIfAbsent(node.getClass(), Objects::getFieldsNames);
  }

  @Override
  public Object getChildNodeValue(String childNodeName, Object instance) {
    return FieldSupport.comparison().fieldValue(childNodeName, Object.class, instance);
  }

  @Override
  public String getDescription() {
    return "comparing fields";
  }

}
