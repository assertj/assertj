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

import java.util.List;

/**
 * Defines how objects are introspected in the recursive assertion.
 */
public interface RecursiveAssertionIntrospectionStrategy {

  /**
   * Returns the child nodes of the given object that will be asserted in the recursive assertion.
   * <p>
   * A typical implementation could look at the object fields or properties.
   *
   * @param node the object to get the child from
   * @return the child nodes of the given object
   */
  List<RecursiveAssertionNode> getChildNodesOf(Object node);

  /**
   * Returns a human-readable description of the strategy to be used in error messages.
   * <p>
   * Default implementation returns {@code this.getClass().getSimpleName()}.
   *
   * @return a description of the strategy
   */
  default String getDescription() {
    return this.getClass().getSimpleName();
  }
}
