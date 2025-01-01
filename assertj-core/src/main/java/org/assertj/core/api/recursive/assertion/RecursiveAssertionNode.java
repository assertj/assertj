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

import java.util.Objects;

/**
 * Immutable class representing a node in the recursive assertion.
 */
public final class RecursiveAssertionNode {
  public final Object value;
  public final String name;
  public final Class<?> type;

  public RecursiveAssertionNode(Object value, String name, Class<?> type) {
    this.value = value;
    this.name = name;
    this.type = type;
  }

  @Override
  public String toString() {
    return String.format("RecursiveAssertionNode[value=%s, name=%s, type=%s]", this.value, this.name, this.type);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    RecursiveAssertionNode that = (RecursiveAssertionNode) o;
    return Objects.equals(value, that.value) && Objects.equals(name, that.name) && Objects.equals(type, that.type);
  }

  @Override
  public int hashCode() {
    return Objects.hash(value, name, type);
  }
}
