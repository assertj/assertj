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

import static java.util.Collections.emptyList;
import static java.util.Collections.unmodifiableList;
import static java.util.Collections.unmodifiableSet;
import static java.util.Objects.requireNonNull;
import static java.util.stream.Collectors.joining;
import static org.assertj.core.util.Lists.list;
import static org.assertj.core.util.Sets.newLinkedHashSet;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * Represents the path to a given field. Immutable
 */
// TODO should understand Map keys as field
// TODO rename to FieldPath?
public final class FieldLocation implements Comparable<FieldLocation> {

  public static final String FIELD_SEPARATOR = ".";
  private final String pathToUseInRules;
  private final List<String> decomposedPath;
  private final Set<String> pathsHierarchyToUseInRules;

  public FieldLocation(List<String> path) {
    decomposedPath = unmodifiableList(requireNonNull(path, "path cannot be null"));
    pathToUseInRules = pathToUseInRules(decomposedPath);
    pathsHierarchyToUseInRules = pathsHierarchyToUseInRules();
  }

  public FieldLocation(String s) {
    this(list(s.split("\\.")));
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
           && Objects.equals(decomposedPath, that.decomposedPath)
           && Objects.equals(pathsHierarchyToUseInRules, that.pathsHierarchyToUseInRules);
  }

  @Override
  public int hashCode() {
    int result = Objects.hashCode(pathToUseInRules);
    result = 31 * result + Objects.hashCode(decomposedPath);
    result = 31 * result + Objects.hashCode(pathsHierarchyToUseInRules);
    return result;
  }

  @Override
  public String toString() {
    return String.format("<%s>", pathToUseInRules);
  }

  public String shortDescription() {
    return pathToUseInRules;
  }

  private static String pathToUseInRules(List<String> path) {
    // remove the array sub-path, so person.children.[2].name -> person.children.name
    // rules for ignoring fields don't apply at the element level (ex: children.[2]) but at the group level (ex: children).
    return path.stream()
               .filter(subpath -> !subpath.startsWith("["))
               .collect(joining(FIELD_SEPARATOR));
  }

  public boolean exactlyMatches(FieldLocation field) {
    return exactlyMatches(field.pathToUseInRules);
  }

  public boolean exactlyMatches(String fieldPath) {
    return pathToUseInRules.equals(fieldPath);
  }

  /**
   * Reruns true if it exactly matches this field, false otherwise.
   *
   * @param fieldPath field path to check
   * @return true if it exactly matches this field, false otherwise
   * @deprecated use {@link #exactlyMatches(String)} instead.
   */
  @Deprecated
  public boolean matches(String fieldPath) {
    return exactlyMatches(fieldPath);
  }

  /**
   * Reruns true if it exactly matches this field, false otherwise.
   *
   * @param field field to check
   * @return true if it exactly matches this field, false otherwise
   * @deprecated use {@link #exactlyMatches(String)} instead.
   */
  @Deprecated
  public boolean matches(FieldLocation field) {
    return exactlyMatches(field);
  }

  /**
   * Checks whether this fieldLocation or any of its parents matches the given fieldPath.
   * <p>
   * Examples:
   * <pre><code class='java'>
   * | fieldLocation       | fieldPath    | matches?
   * -----------------------------------------------
   * | name.first          | "name"       | true
   * | name.first.nickname | "name"       | true
   * | name.first          | "name.first" | true
   * | name.first.nickname | "name.first" | true
   * | name                | "name"       | true
   * | name                | "name.first" | false
   * | person.name         | "name"       | false
   * | names               | "name"       | false
   * | nickname            | "name"       | false
   * | name                | "nickname"   | false
   * | first.nickname      | "name"       | false
   * </code></pre>
   *
   * @param fieldPath the field path to test
   * @return true if this fieldLocation is the given fieldPath or a child of it, false otherwise.
   */
  public boolean hierarchyMatches(String fieldPath) {
    return pathsHierarchyToUseInRules.contains(fieldPath);
  }

  /**
   * Checks whether this fieldLocation or any of its parents matches the given regex.
   * <p>
   * Examples:
   * <pre><code class='java'>
   * | fieldLocation       | regex        | matches?
   * -----------------------------------------------
   * | name.first          | "name"       | true
   * | name.first          | "..me"       | true
   * | name.first.nickname | "name"       | true
   * | name.first          | "name.first" | true
   * | name.first.nickname | "name.first" | true
   * | name                | "name"       | true
   * | name                | "name.first" | false
   * | person.name         | "name"       | false
   * | names               | "name"       | false
   * | nickname            | "name"       | false
   * | name                | "nickname"   | false
   * | first.nickname      | "name"       | false
   * </code></pre>
   *
   * @param regex the regex to test
   * @return true, this fieldLocation or any of its parent matches the given regex., false otherwise.
   */
  public boolean hierarchyMatchesRegex(Pattern regex) {
    return pathsHierarchyToUseInRules.stream().anyMatch(path -> regex.matcher(path).matches());
  }

  public List<String> getDecomposedPath() {
    return decomposedPath;
  }

  public String getPathToUseInRules() {
    return pathToUseInRules;
  }

  public String getPathToUseInRulesForChildField(String fieldName) {
    return pathToUseInRules.isEmpty() ? fieldName : pathToUseInRules + FIELD_SEPARATOR + fieldName;
  }

  public FieldLocation field(String field) {
    List<String> decomposedPathWithField = new ArrayList<>(decomposedPath);
    decomposedPathWithField.add(field);
    return new FieldLocation(decomposedPathWithField);
  }

  public String getPathToUseInErrorReport() {
    return String.join(FIELD_SEPARATOR, decomposedPath);
  }

  public String getFieldName() {
    if (decomposedPath.isEmpty()) return "";
    return decomposedPath.get(decomposedPath.size() - 1);
  }

  public boolean isRoot() {
    // Root is the top level object compared or in case of the top level is an iterable/array the elements are
    // considered as roots.
    // We don't do it for optional since it has a 'value' field (at least for now)
    return isRootPath(pathToUseInRules);
  }

  private boolean isRootPath(String pathToUseInRules) {
    return pathToUseInRules.isEmpty();
  }

  public boolean isTopLevelField() {
    return !isRoot() && !pathToUseInRules.contains(FIELD_SEPARATOR);
  }

  public static FieldLocation rootFieldLocation() {
    return new FieldLocation(emptyList());
  }

  /**
   * Returns true if this has the given parent (direct or indirect), false otherwise.
   * <p>
   * Examples:
   * <pre><code class='java'>
   * | field                 | parent       | hasParent?
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
    // FIELD_SEPARATOR guarantees that we compare path elements, this avoids making "name" a parent of "names"
    return pathToUseInRules.startsWith(parent.pathToUseInRules + FIELD_SEPARATOR);
  }

  /**
   * Returns true if this field has the given child (direct or indirect), false otherwise.
   * <p>
   * Examples:
   * <pre><code class='java'>
   * | field                 | child           | hasChild?
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

  private Set<String> pathsHierarchyToUseInRules() {
    // using LinkedHashSet to maintain leaf to root iteration order
    // so that hierarchyMatchesRegex can try matching from the longest to the shortest path
    Set<String> fieldAndParentFields = newLinkedHashSet();
    String currentPath = this.pathToUseInRules;
    while (!isRootPath(currentPath)) {
      fieldAndParentFields.add(currentPath);
      currentPath = parent(currentPath);
    }
    return unmodifiableSet(fieldAndParentFields);
  }

  private String parent(String currentPath) {
    int lastDot = currentPath.lastIndexOf(FIELD_SEPARATOR);
    if (lastDot < 0) {
      return "";
    }
    return currentPath.substring(0, lastDot);
  }
}
