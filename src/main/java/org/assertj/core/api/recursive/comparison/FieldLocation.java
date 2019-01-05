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

import static java.util.stream.Collectors.toList;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

// TODO should understand Map keys as field
public class FieldLocation implements Comparable<FieldLocation> {

  // TODO test
  private static final Comparator<FieldLocation> COMPARATOR = Comparator.comparing(FieldLocation::getFieldPath);

  private String fieldPath;
  // private boolean whereverInGraph = false;
  // private boolean matches(Field field, Field parent); ?

  @Override
  public int compareTo(final FieldLocation other) {
    return COMPARATOR.compare(this, other);
  }

  @Override
  public boolean equals(final Object other) {
    if (!(other instanceof FieldLocation)) return false;
    FieldLocation castOther = (FieldLocation) other;
    return Objects.equals(fieldPath, castOther.fieldPath);
  }

  @Override
  public int hashCode() {
    return Objects.hash(fieldPath);
  }

  public FieldLocation(String fieldPath) {
    Objects.requireNonNull(fieldPath, "a field path can't be null");
    this.fieldPath = fieldPath;
  }

  public String getFieldPath() {
    return fieldPath;
  }

  @Override
  public String toString() {
    return String.format("FieldLocation [fieldPath=%s]", fieldPath);
  }

  public boolean matches(String concatenatedPath) {
    return fieldPath.equals(concatenatedPath);
  }

  static List<FieldLocation> from(String... fieldPaths) {
    return Stream.of(fieldPaths).map(FieldLocation::new).collect(toList());
  }

}
