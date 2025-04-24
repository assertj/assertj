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

import java.util.Set;

import org.assertj.core.annotations.Beta;

/**
 * Defines how objects are introspected in the recursive comparison, the main operations it specifies are:
 * <ul>
 * <li>how to traverse the graph of nodes to compare</li>
 * <li>how to get a child node value</li>
 * </ul>
 */
@Beta
public interface RecursiveComparisonIntrospectionStrategy {

  /**
   * Returns the names of the children nodes of the given object that will be used in the recursive comparison.
   * <p>
   * A typical implementation could look at the object fields or properties.
   *
   * @param node the object to get the child nodes from
   * @return the names of the children nodes of the given object
   */
  Set<String> getChildrenNodeNamesOf(Object node);

  /**
   * Returns the value of the given object child node, the child node being identified by the childNodeName parameter.
   * <p>
   * It's the implementor choice how to resolve the child node value, a typical implementation consists of considering
   * childNodeName to be a field name and then use introspection to read the field value, but if the object is a Map
   * the implementation could consider the child node name to be a key of the map.
   *
   * @param childNodeName the child node identifier
   * @param object the object to read the child node from
   * @return the object child node value
   */
  Object getChildNodeValue(String childNodeName, Object object);

  /**
   * Returns a human-readable description of the strategy to be used in error messages.
   * <p>
   * The default implementation returns {@code this.getClass().getSimpleName()}.
   *
   * @return a description of the strategy
   */
  default String getDescription() {
    return this.getClass().getSimpleName();
  }
}
