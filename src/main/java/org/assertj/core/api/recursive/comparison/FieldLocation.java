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
 * Copyright 2012-2020 the original author or authors.
 */
package org.assertj.core.api.recursive.comparison;

import static java.util.stream.Collectors.toList;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

/**
 * @deprecated this class is meant to be internal and it is exposed only for backward compatibility together with
 *             {@link RecursiveComparisonConfiguration#registerComparatorForField(Comparator, FieldLocation)},
 *             it will be restricted to package-private visibility in the next major release.
 */
// TODO should understand Map keys as field
@Deprecated
public final class FieldLocation implements Comparable<FieldLocation> {

  private final String fieldPath;

  public FieldLocation(String fieldPath) {
    this.fieldPath = Objects.requireNonNull(fieldPath, "'fieldPath' cannot be null");
  }

  String getFieldPath() {
    return fieldPath;
  }

  @Override
  public int compareTo(final FieldLocation other) {
    return fieldPath.compareTo(other.fieldPath);
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) return true;
    if (!(obj instanceof FieldLocation)) return false;
    FieldLocation that = (FieldLocation) obj;
    return Objects.equals(fieldPath, that.fieldPath);
  }

  @Override
  public int hashCode() {
    return Objects.hash(fieldPath);
  }

  @Override
  public String toString() {
    return String.format("FieldLocation [fieldPath=%s]", fieldPath);
  }

  boolean matches(String concatenatedPath) {
    return fieldPath.equals(concatenatedPath);
  }

  static List<FieldLocation> from(String... fieldPaths) {
    return Stream.of(fieldPaths).map(FieldLocation::new).collect(toList());
  }

  /**
   * @deprecated use {@link #fieldLocation} instead
   *
   * @param fieldPath the field path.
   * @return the built field location.
   */
  @Deprecated
  public static FieldLocation fielLocation(String fieldPath) {
    return fieldLocation(fieldPath);
  }

  public static FieldLocation fieldLocation(String fieldPath) {
    return new FieldLocation(fieldPath);
  }

}
