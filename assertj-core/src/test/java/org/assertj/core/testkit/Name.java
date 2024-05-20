/*
 * Copyright © 2024 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.assertj.core.testkit;

import static java.util.Comparator.comparing;

import java.util.Comparator;
import java.util.Objects;

/**
 * @author Yvonne Wang
 * @author Joel Costigliola
 */
public class Name implements Comparable<Name> {

  public static final Comparator<Name> lastNameComparator = comparing(Name::getLast);

  // intentionally public to test field retrieval
  // getter have been created to test property retrieval
  public String first;
  // keep private to test we are able to read property but not field
  private String last;

  public Name() {}

  public Name(String first) {
    setFirst(first);
  }

  public Name(String first, String last) {
    setFirst(first);
    setLast(last);
  }

  public static Name name(String first, String last) {
    return new Name(first, last);
  }

  public String getFirst() {
    return first;
  }

  public void setFirst(String first) {
    this.first = first;
  }

  public String getLast() {
    return last;
  }

  public void setLast(String last) {
    this.last = last;
  }

  // property without field in order to test field/property combinations
  public String getName() {
    return String.format("%s %s", getFirst(), getLast());
  }

  @Override
  public String toString() {
    return String.format("%s[first='%s', last='%s']", getClass().getSimpleName(), first, last);
  }

  @Override
  public int hashCode() {
    return Objects.hash(first, last);
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) return true;
    if (obj == null) return false;
    if (getClass() != obj.getClass()) return false;
    Name other = (Name) obj;
    return Objects.equals(first, other.first) && Objects.equals(last, other.last);
  }

  @Override
  public int compareTo(Name other) {
    return this.getName().compareToIgnoreCase(other.getName());
  }

}
