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
 * Copyright 2012-2022 the original author or authors.
 */
package org.assertj.core.api.recursive;

import static java.util.Collections.emptyList;
import static java.util.Collections.unmodifiableList;
import static java.util.Objects.requireNonNull;
import static java.util.stream.Collectors.joining;
import static org.assertj.core.util.Lists.list;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Represents the path to a given field. Immutable
 */
// TODO should understand Map keys as field
// TODO rename to FieldPath?
public final class FieldLocation implements Comparable<FieldLocation> {

  private final String pathToUseInRules;
  private final List<String> decomposedPath; // TODO is it useful?

  public FieldLocation(List<String> path) {
    decomposedPath = unmodifiableList(requireNonNull(path, "path cannot be null"));
    pathToUseInRules = pathToUseInRules(decomposedPath);
  }

  public FieldLocation(String s) {
    this(list(s.split("\\.")));
  }

  public boolean matches(FieldLocation field) {
    return pathToUseInRules.equals(field.pathToUseInRules);
  }

  public boolean matches(String fieldPath) {
    return pathToUseInRules.equals(fieldPath);
  }

  public List<String> getDecomposedPath() {
    return decomposedPath;
  }

  public String getPathToUseInRules() {
    return pathToUseInRules;
  }

  public FieldLocation field(String field) {
    List<String> decomposedPathWithField = new ArrayList<>(decomposedPath);
    decomposedPathWithField.add(field);
    return new FieldLocation(decomposedPathWithField);
  }

  @Override
  public int compareTo(final FieldLocation other) {
    return pathToUseInRules.compareTo(other.pathToUseInRules);
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) return true;
    if (!(obj instanceof FieldLocation)) return false;
    FieldLocation that = (FieldLocation) obj;
    return Objects.equals(pathToUseInRules, that.pathToUseInRules)
           && Objects.equals(decomposedPath, that.decomposedPath);
  }

  @Override
  public int hashCode() {
    return Objects.hash(pathToUseInRules, decomposedPath);
  }

  @Override
  public String toString() {
    return String.format("FieldLocation [pathToUseInRules=%s, decomposedPath=%s]", pathToUseInRules, decomposedPath);
  }

  public String shortDescription() {
    return pathToUseInRules;
  }

  private static String pathToUseInRules(List<String> path) {
    // remove the array subpath, so person.children.[2].name -> person.children.name
    // rules for ignoring fields don't apply at the element level (ex: children.[2]) but at the group level (ex: children).
    return path.stream()
               .filter(subpath -> !subpath.startsWith("["))
               .collect(joining("."));
  }

  public String getPathToUseInErrorReport() {
    return String.join(".", decomposedPath);
  }

  public String getFieldName() {
    if (decomposedPath.isEmpty()) return "";
    return decomposedPath.get(decomposedPath.size() - 1);
  }

  public static FieldLocation rootFieldLocation() {
    return new FieldLocation(emptyList());
  }

  /**
   * Returns true if this has the given parent (direct or indirect), false otherwise.
   * <p>
   * Examples:
   * <pre><code class='java'> | field                 | parent       | hasParent? 
   * -----------------------------------------------  
   * | "name.first"          | "name"       | true       
   * | "name.first.nickname" | "name"       | true       
   * | "name.first.nickname" | "name.first" | true       
   * | "name"                | "name"       | false      
   * | "names"               | "name"       | false      
   * | "nickname"            | "name"       | false      
   * | "name"                | "nickname"   | false      
   * | "first.nickname"      | "name"       | false      
   * </code></pre>
   *  
   * @param parent the field to check for being a parent
   * @return true if this has the given parent (direct or indirect), false otherwise.
   */
  public boolean hasParent(FieldLocation parent) {
    // "." garantees that we compare path elements, this avoid making "name" a parent of "names"
    return pathToUseInRules.startsWith(parent.pathToUseInRules + ".");
  }

  /**
   * Returns true if this field has the given child (direct or indirect), false otherwise.
   * <p>
   * Examples:
   * <pre><code class='java'> | field                 | child           | hasChild? 
   * -----------------------------------------------  
   * | "name"                | "name.first"    | true       
   * | "name"                | "name.last"     | true       
   * | "one"                 | "one.two.three" | true
   * | "name.first"          | "name "         | false       
   * | "name"                | "name"          | false      
   * | "names"               | "name"          | false      
   * | "nickname"            | "name"          | false      
   * | "name"                | "nickname"      | false      
   * | "first.nickname"      | "name"          | false      
   * </code></pre>
   *  
   * @param child the field to check for being a child
   * @return true if this has the given child (direct or indirect), false otherwise.
   */
  public boolean hasChild(FieldLocation child) {
    return child.hasParent(this);
  }

}
