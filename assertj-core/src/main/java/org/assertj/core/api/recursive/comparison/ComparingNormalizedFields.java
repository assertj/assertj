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

import static java.lang.String.format;
import static java.util.stream.Collectors.toSet;
import static org.assertj.core.util.introspection.PropertyOrFieldSupport.COMPARISON;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.assertj.core.internal.Objects;
import org.assertj.core.util.introspection.IntrospectionError;

/**
 * A {@link RecursiveComparisonIntrospectionStrategy} that introspects fields provided their normalized name.
 * <p>
 * Subclass this and simply provide the normalization to apply by implementing {@link #normalizeFieldName(String)}.
 * <p>
 * It is recommended to override {@link #getDescription()} to get more informative error messages.
 *
 * @since 3.24.0
 */
public abstract class ComparingNormalizedFields implements RecursiveComparisonIntrospectionStrategy {

  private static final String NO_FIELD_FOUND = "Unable to find field in %s, fields tried: %s and %s";

  // original field name <-> normalized field name by type
  private final Map<Class<?>, Map<String, String>> originalFieldNameByNormalizedFieldNameByType = new ConcurrentHashMap<>();

  // use ConcurrentHashMap in case this strategy instance is used in a multi-thread context
  private final Map<Class<?>, Set<String>> fieldNamesPerClass = new ConcurrentHashMap<>();

  /**
   * Returns the <b>normalized</b> names of the children nodes of the given object that will be used in the recursive comparison.
   * <p>
   * The names are normalized according to {@link #normalizeFieldName(String)}.
   *
   * @param node the object to get the child nodes from
   * @return the normalized names of the children nodes of the given object
   */
  @Override
  public Set<String> getChildrenNodeNamesOf(Object node) {
    if (node == null) return new HashSet<>();
    Class<?> nodeClass = node.getClass();
    Set<String> fieldsNames = Objects.getFieldsNames(nodeClass);
    // we normalize fields so that we can compare actual and expected, for example if actual has a firstName field and expected
    // a first_name field, we won't find firstName in expected unless we normalize it
    // Note that normalize has side effects as it keeps track of the normalized name -> original name mapping
    return fieldNamesPerClass.computeIfAbsent(nodeClass,
                                              unused -> fieldsNames.stream()
                                                                   .map(fieldsName -> normalize(nodeClass, fieldsName))
                                                                   .collect(toSet()));
  }

  /**
   * Returns the normalized version of the given field name to allow actual and expected fields to be matched.
   * <p>
   * For example, let's assume {@code actual} is a {@code Person} with camel case fields like {@code firstName} and
   * {@code expected} is a {@code PersonDto} with snake case field like {@code first_name}.
   * <p>
   * The default recursive comparison gathers all {@code actual} and {@code expected} fields to compare them but fails as it can't
   * know that {@code actual.firstName} must be compared to {@code expected.first_name}.<br/> By normalizing fields names first,
   * the recursive comparison can now operate on fields that can be matched.
   * <p>
   * In our example, we can either normalize fields to be camel case or snake case (camel case would be more natural though).
   * <p>
   * Note that {@link #getChildNodeValue(String, Object)} receives the normalized field name, it tries to get its value first and
   * if failing to do so, it tries the original field name.<br/>
   * In our example, if we normalize to camel case, getting {@code firstName} works fine for {@code actual} but not for
   * {@code expected}, we have to get the original field name {@code first_name} to get the value ({@code ComparingNormalizedFields}
   * implementation tracks which original field names resulted in a specific normalized field name).
   *
   * @param fieldName the field name to normalize
   * @return the normalized field name
   */
  protected abstract String normalizeFieldName(String fieldName);

  /**
   * Normalize the field name and keep track of the normalized name -> original name
   * @param fieldName the field name to normalize
   * @return the normalized field name
   */
  private String normalize(Class<?> nodeClass, String fieldName) {
    if (!originalFieldNameByNormalizedFieldNameByType.containsKey(nodeClass)) {
      originalFieldNameByNormalizedFieldNameByType.put(nodeClass, new HashMap<>());
    }
    String normalizedFieldName = normalizeFieldName(fieldName);
    originalFieldNameByNormalizedFieldNameByType.get(nodeClass).put(normalizedFieldName, fieldName);
    return normalizedFieldName;
  }

  /**
   * Returns the value of the given object field identified by the fieldName parameter.
   * <p>
   * Note that this method receives the normalized field name with (see {@link #normalizeFieldName(String)}), it tries to get
   * its value first and if failing to do so, it tries the original field name ({@code ComparingNormalizedFields} implementation
   * tracks which original field names resulted in a specific normalized field name).
   * <p>
   * For example, let's assume {@code actual} is a {@code Person} with camel case fields like {@code firstName} and {@code expected} is a
   * {@code PersonDto} with snake case field like {@code first_name} and we normalize all fields names to be camel case. In this
   * case, getting {@code firstName} works fine for {@code actual} but not for {@code expected}, for the latter it succeeds with
   * the original field name {@code first_name}.
   *
   * @param fieldName the field name
   * @param instance the object to read the field from
   * @return the object field value
   */
  @Override
  public Object getChildNodeValue(String fieldName, Object instance) {
    // fieldName was normalized usually matching actual or expected field naming convention but not both, we first try
    // to get the value corresponding to fieldName but if that does not work it means the instance object class fields were
    // changed when normalized, to get the field value we then need to try the original field name.
    // This process is not super efficient since we might try two field names instead of one, improving this requires to change
    // the recursive comparison quite a bit to:
    // - introspect actual and expected differently
    // - keep a mapping of which actual field should be matched to which expected field
    // This is not a straightforward change and might quite a bit more complexity to an already rather complex feature.
    try {
      return COMPARISON.getSimpleValue(fieldName, instance);
    } catch (Exception e) {
      String originalFieldName = getOriginalFieldName(fieldName, instance);
      try {
        return COMPARISON.getSimpleValue(originalFieldName, instance);
      } catch (Exception ex) {
        throw new IntrospectionError(format(NO_FIELD_FOUND, instance, fieldName, originalFieldName), ex);
      }
    }
  }

  private String getOriginalFieldName(String fieldName, Object instance) {
    // call getChildrenNodeNamesOf to populate originalFieldNamesByNormalizedFieldNameByNode, the recursive comparison
    // should already do this if this is used outside then getChildNodeValue would fail
    Class<?> instanceClass = instance.getClass();
    if (!originalFieldNameByNormalizedFieldNameByType.containsKey(instanceClass)) getChildrenNodeNamesOf(instance);
    return originalFieldNameByNormalizedFieldNameByType.get(instanceClass).get(fieldName);
  }

  @Override
  public String getDescription() {
    return "comparing normalized fields";
  }

}
