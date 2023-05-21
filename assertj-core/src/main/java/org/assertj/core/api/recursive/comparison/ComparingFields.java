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
 * Copyright 2012-2023 the original author or authors.
 */
package org.assertj.core.api.recursive.comparison;

import static org.assertj.core.internal.Objects.getFieldsNames;

import java.util.HashSet;
import java.util.Set;

import org.assertj.core.util.introspection.FieldSupport;

/**
 * A {@link RecursiveComparisonIntrospectionStrategy} that introspects fields including inherited ones but ignores static and
 * synthetic fields.
 */
public class ComparingFields implements RecursiveComparisonIntrospectionStrategy {

  public static final ComparingFields COMPARING_FIELDS = new ComparingFields();

  @Override
  public Set<String> getChildrenNodeNamesOf(Object node) {
    return node == null ? new HashSet<>() : getFieldsNames(node.getClass());
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
