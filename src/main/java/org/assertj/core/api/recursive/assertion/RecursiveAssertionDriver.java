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
 * Copyright 2012-2021 the original author or authors.
 */
package org.assertj.core.api.recursive.assertion;

import org.assertj.core.api.recursive.FieldLocation;
import org.assertj.core.internal.Objects;
import org.assertj.core.util.Arrays;
import org.assertj.core.util.introspection.FieldSupport;
import org.assertj.core.util.introspection.PropertyOrFieldSupport;

import java.text.MessageFormat;
import java.util.*;
import java.util.function.Predicate;

import static org.assertj.core.api.Assertions.tuple;
import static org.assertj.core.util.Lists.list;
import static org.assertj.core.util.Sets.newHashSet;

public class RecursiveAssertionDriver {

  private static final MessageFormat INDEX_FORMAT = new MessageFormat("[{0, number}]");

  private final Set<String> markedBlackSet = newHashSet();
  private final List<FieldLocation> fieldsThatFailedTheAssertion = list();
  private final RecursiveAssertionConfiguration configuration;

  public RecursiveAssertionDriver(RecursiveAssertionConfiguration configuration) {
    this.configuration = configuration;
  }

  public List<FieldLocation> assertOverObjectGraph(Predicate<Object> predicate, Object graphNode) {
    assertRecursively(predicate, graphNode, graphNode.getClass(), FieldLocation.rootFieldLocation());
    return Collections.unmodifiableList(fieldsThatFailedTheAssertion);
  }

  private void assertRecursively(Predicate<Object> predicate, Object node, Class<?> nodeType, FieldLocation fieldLocation) {
    if (nodeMustBeIgnored(node, nodeType, fieldLocation)) return;

    boolean nodeWasAlreadyBlack = markNodeAsBlack(node);
    if (nodeWasAlreadyBlack) return;

    checkPoliciesAndAssert(predicate, node, nodeType, fieldLocation);
    // TODO 3: Check recursive conditions
    // TODO 4: Check for map/collections/arrays/optionals
    // TODO 5: Make the recursive call for all applicable fields
    recurseIntoFieldsOfCurrentNode(predicate, node, nodeType, fieldLocation);
  }

  private boolean nodeMustBeIgnored(Object node, Class<?> nodeType, FieldLocation fieldLocation) {
    return node_is_null_and_we_are_ignoring_those(node)
           || node_is_primitive_and_we_are_ignoring_those(nodeType)
           || node_is_empty_optional_and_we_are_ignoring_those(node, nodeType)
           || node_is_being_ignored_by_name_or_name_patten(fieldLocation)
           || node_is_being_ignored_by_type(nodeType);
  }

  private boolean node_is_null_and_we_are_ignoring_those(Object node) {
    return node == null && configuration.getIgnoreAllActualNullFields();
  }

  private boolean node_is_primitive_and_we_are_ignoring_those(Class<?> nodeType) {
    return nodeType.isPrimitive() && !configuration.getAssertOverPrimitiveFields();
  }

  private boolean node_is_empty_optional_and_we_are_ignoring_those(Object node, Class<?> nodeType) {
    return configuration.getIgnoreAllActualEmptyOptionalFields()
           && Optional.class.isAssignableFrom(nodeType)
           && !((Optional) node).isPresent();
  }

  private boolean node_is_being_ignored_by_name_or_name_patten(FieldLocation fieldLocation) {
    return configuration.matchesAnIgnoredField(fieldLocation)
           || configuration.matchesAnIgnoredFieldRegex(fieldLocation);
  }

  private boolean node_is_being_ignored_by_type(Class<?> nodeType) {
    return configuration.getIgnoredTypes().contains(nodeType);
  }

  private void checkPoliciesAndAssert(Predicate<Object> predicate, Object node, Class<?> nodeType, FieldLocation fieldLocation) {
    if (policyDoesNotForbidAssertingOverNode(nodeType)) {
      doTheActualAssertionAndRegisterInCaseOfFailure(predicate, node, fieldLocation);
    }
  }

  private boolean policyDoesNotForbidAssertingOverNode(Class<?> nodeType) {
    boolean policyForbidsAsserting = node_is_a_collection_and_policy_is_to_ignore_container(nodeType);
    return !policyForbidsAsserting;
  }

  private boolean node_is_a_collection_and_policy_is_to_ignore_container(Class<?> nodeType) {
    boolean nodeIsCollection = isCollection(nodeType) || isArray(nodeType);
    boolean policyIsIgnoreContainer =
      configuration.getCollectionAssertionPolicy() == RecursiveAssertionConfiguration.CollectionAssertionPolicy.ELEMENTS_ONLY;
    return policyIsIgnoreContainer && nodeIsCollection;
  }

  private void recurseIntoFieldsOfCurrentNode(Predicate<Object> predicate, Object node, Class<?> nodeType,
                                              FieldLocation fieldLocation) {
    if (node_is_a_special_type_which_requires_special_treatment(nodeType)) {
      if (policy_is_to_recurse_over_special_types(nodeType)) {
        doRecursionForSpecialTypes(predicate, node, nodeType, fieldLocation);
      }
    } else if (nodeShouldBeRecursedInto(node)) {
      findFieldsOfCurrentNodeAndDoRecursiveCall(predicate, node, fieldLocation);
    }
  }

  private boolean node_is_a_special_type_which_requires_special_treatment(Class<?> nodeType) {
    return isCollection(nodeType)
           || Map.class.isAssignableFrom(nodeType)
           || isArray(nodeType);
  }

  private boolean policy_is_to_recurse_over_special_types(Class<?> nodeType) {
    return (isCollection(nodeType)
            || isArray(nodeType))
           && (configuration.getCollectionAssertionPolicy()
        != RecursiveAssertionConfiguration.CollectionAssertionPolicy.COLLECTION_OBJECT_ONLY);
  }

  private void doRecursionForSpecialTypes(Predicate<Object> predicate, Object node, Class<?> nodeType,
                                          FieldLocation fieldLocation) {
    if (isCollection(nodeType)) {
      recurseIntoCollection(predicate, (Collection) node, fieldLocation);
    }
    if (isArray(nodeType)) {
      recurseIntoArray(predicate, node, nodeType, fieldLocation);
    }
  }

  private void recurseIntoCollection(Predicate<Object> predicate, Collection node, FieldLocation fieldLocation) {
    int idx = 0;
    for (Object o : node) {
      assertRecursively(predicate, o, o != null ? o.getClass() : Object.class,
                        fieldLocation.field(INDEX_FORMAT.format(new Integer[] { idx })));
      idx++;
    }
  }

  private void recurseIntoArray(Predicate<Object> predicate, Object node, Class<?> nodeType, FieldLocation fieldLocation) {
    Class<?> arrayType = nodeType.getComponentType();
    Object[] arr = Arrays.asObjectArray(node);
    for (int i = 0; i < arr.length; i++) {
      assertRecursively(predicate, arr[i], arrayType,
                        fieldLocation.field(INDEX_FORMAT.format(new Integer[] { i })));
    }
  }

  private boolean nodeShouldBeRecursedInto(Object node) {
    boolean nodeShouldBeRecursedInto = node != null;
    nodeShouldBeRecursedInto = nodeShouldBeRecursedInto && !node_is_a_jcl_type_and_we_skip_those(node);
    return nodeShouldBeRecursedInto;
  }

  private boolean node_is_a_jcl_type_and_we_skip_those(Object node) {
    boolean skipJCLTypes = configuration.isSkipJavaLibraryTypeObjects();
    boolean isJCLType = node.getClass().getCanonicalName().startsWith("java.")
                        || node.getClass().getCanonicalName().startsWith("javax.");
    return isJCLType && skipJCLTypes;
  }

  private void findFieldsOfCurrentNodeAndDoRecursiveCall(Predicate<Object> predicate, Object node, FieldLocation fieldLocation) {
    Set<String> namesOfFieldsInNode = Objects.getFieldsNames(node.getClass());
    namesOfFieldsInNode.stream()
                       .map(name -> tuple(name, PropertyOrFieldSupport.EXTRACTION.getSimpleValue(name, node),
                                          FieldSupport.getFieldType(name, node)))
                       .forEach(tuple -> {
                         String fieldName = tuple.getByIndexAndType(0, String.class);
                         Object nextNodeValue = tuple.getByIndexAndType(1, Object.class);
                         Class<?> nextNodeType = tuple.getByIndexAndType(2, Class.class);
                         assertRecursively(predicate, nextNodeValue, nextNodeType, fieldLocation.field(fieldName));
                       });
  }

  private void doTheActualAssertionAndRegisterInCaseOfFailure(Predicate<Object> predicate, Object node,
                                                              FieldLocation fieldLocation) {
    if (!predicate.test(node)) {
      fieldsThatFailedTheAssertion.add(fieldLocation);
    }
  }

  private boolean markNodeAsBlack(Object node) {
    // Cannot mark null nodes, so just lie and say marking succeeded...
    if (node == null) return false;

    String objectId = identityToString(node);
    return !markedBlackSet.add(objectId);
  }

  /*
   * This is taken verbatim from org.apache.commons.lang3.ObjectUtils .
   *
   * It would be much cleaner if we would be allowed to use ObjectUtils directly.
   */
  private String identityToString(final Object object) {
    if (object == null) {
      return null;
    }
    final String name = object.getClass().getName();
    final String hexString = Integer.toHexString(System.identityHashCode(object));
    final StringBuilder builder = new StringBuilder(name.length() + 1 + hexString.length());
    // @formatter:off
    builder.append(name)
           .append('@')
           .append(hexString);
    // @formatter:on
    return builder.toString();
  }

  private boolean isCollection(Class<?> nodeType) {
    return Collection.class.isAssignableFrom(nodeType);
  }

  private boolean isArray(Class<?> nodeType) {
    return nodeType.isArray();
  }

}
