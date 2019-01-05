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
 * Copyright 2012-2018 the original author or authors.
 */
package org.assertj.core.api.recursive.comparison;

import static org.assertj.core.util.Strings.join;

import java.util.List;

final class DualKey { // TODO rename DualValue

  final List<String> path;
  final String concatenatedPath;
  final Object key1; // TODO rename to actual
  final Object key2; // TODO rename to expected

  DualKey(List<String> path, Object key1, Object key2) {
    this.path = path;
    this.concatenatedPath = join(path).with(".");
    this.key1 = key1;
    this.key2 = key2;
  }

  @Override
  public boolean equals(Object other) {
    if (!(other instanceof DualKey)) {
      return false;
    }

    DualKey that = (DualKey) other;
    return key1 == that.key1 && key2 == that.key2;
  }

  @Override
  public int hashCode() {
    int h1 = key1 != null ? key1.hashCode() : 0;
    int h2 = key2 != null ? key2.hashCode() : 0;
    return h1 + h2;
  }


  @Override
  public String toString() {
    return String.format("DualKey [path=%s, key1=%s, key2=%s]", concatenatedPath, key1, key2);
  }

  public List<String> getPath() {
    return path;
  }

  public String getConcatenatedPath() {
    return concatenatedPath;
  }
}