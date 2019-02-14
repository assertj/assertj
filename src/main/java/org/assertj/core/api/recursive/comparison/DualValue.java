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
 * Copyright 2012-2019 the original author or authors.
 */
package org.assertj.core.api.recursive.comparison;

import static java.lang.String.format;
import static java.util.Collections.unmodifiableList;
import static org.assertj.core.util.Strings.join;

import java.util.List;

// logically immutable
final class DualValue {

  final List<String> path;
  final String concatenatedPath;
  final Object actual;
  final Object expected;
  private final int hashCode;

  DualValue(List<String> path, Object actual, Object expected) {
    this.path = path;
    this.concatenatedPath = join(path).with(".");
    this.actual = actual;
    this.expected = expected;
    int h1 = actual != null ? actual.hashCode() : 0;
    int h2 = expected != null ? expected.hashCode() : 0;
    hashCode = h1 + h2;
  }

  @Override
  public boolean equals(Object other) {
    if (!(other instanceof DualValue)) return false;
    DualValue that = (DualValue) other;
    return actual == that.actual && expected == that.expected;
  }

  @Override
  public int hashCode() {
    return hashCode;
  }

  @Override
  public String toString() {
    return format("DualValue [path=%s, actual=%s, expected=%s]", concatenatedPath, actual, expected);
  }

  public List<String> getPath() {
    return unmodifiableList(path);
  }

  public String getConcatenatedPath() {
    return concatenatedPath;
  }

  public boolean isJavaType() {
    if (actual == null) return false;
    return actual.getClass().getName().startsWith("java.");
  }
}