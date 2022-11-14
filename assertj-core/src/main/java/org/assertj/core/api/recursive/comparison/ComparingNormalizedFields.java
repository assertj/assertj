package org.assertj.core.api.recursive.comparison;

import static java.util.stream.Collectors.toSet;
import static org.assertj.core.util.introspection.PropertyOrFieldSupport.COMPARISON_NORMALIZED;

import java.util.HashSet;
import java.util.Set;

import org.assertj.core.internal.Objects;

/**
 * A {@link RecursiveComparisonIntrospectionStrategy} that introspects fields provided their normalized name.
 */
abstract class ComparingNormalizedFields implements RecursiveComparisonIntrospectionStrategy {

  @Override
  public Set<String> getChildrenNodeNamesOf(Object node) {
    if (node == null) return new HashSet<>();
    Set<String> fieldsNames = Objects.getFieldsNames(node.getClass());
    return fieldsNames.stream().map(this::normalizeFieldName).collect(toSet());
  }

  protected abstract String normalizeFieldName(String fieldName);

  @Override
  public Object getChildNodeValue(String childNodeName, Object instance) {
    return COMPARISON_NORMALIZED.getSimpleValue(childNodeName, instance);
  }

  @Override
  public String getDescription() {
    return "comparing normalized fields";
  }

}
