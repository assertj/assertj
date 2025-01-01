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
package org.assertj.core.api.recursive.assertion;

import static java.lang.String.format;
import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.recursive.assertion.RecursiveAssertionConfiguration.CollectionAssertionPolicy.COLLECTION_OBJECT_ONLY;
import static org.assertj.core.api.recursive.assertion.RecursiveAssertionConfiguration.MapAssertionPolicy.MAP_OBJECT_AND_ENTRIES;
import static org.assertj.core.api.recursive.assertion.RecursiveAssertionConfiguration.MapAssertionPolicy.MAP_OBJECT_ONLY;
import static org.assertj.core.api.recursive.assertion.RecursiveAssertionConfiguration.OptionalAssertionPolicy.OPTIONAL_OBJECT_ONLY;
import static org.assertj.core.api.recursive.comparison.FieldLocation.rootFieldLocation;
import static org.assertj.core.util.Lists.list;
import static org.assertj.core.util.Sets.newHashSet;
import static org.assertj.core.util.introspection.ClassUtils.isOptionalOrPrimitiveOptional;
import static org.assertj.core.util.introspection.ClassUtils.isPrimitiveOrWrapper;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.OptionalDouble;
import java.util.OptionalInt;
import java.util.OptionalLong;
import java.util.Set;
import java.util.function.Predicate;

import org.assertj.core.api.recursive.comparison.FieldLocation;
import org.assertj.core.util.Arrays;

public class RecursiveAssertionDriver {

  private static final String NULL = "null";
  private static final String INDEX_FORMAT = "[%d]";
  private static final String KEY_FORMAT = "KEY[%s]";
  private static final String VALUE_FORMAT = "VAL[%s]";

  private final Set<String> visitedNodeIds = newHashSet();
  private final List<FieldLocation> fieldsFailingTheAssertion = list();
  private final RecursiveAssertionConfiguration configuration;

  public RecursiveAssertionDriver(RecursiveAssertionConfiguration configuration) {
    this.configuration = configuration;
  }

  public List<FieldLocation> assertOverObjectGraph(Predicate<Object> predicate, Object graphNode) {
    assertRecursively(predicate, graphNode, graphNode.getClass(), rootFieldLocation());
    return fieldsFailingTheAssertion.stream().sorted().collect(toList());
  }

  public void reset() {
    visitedNodeIds.clear();
    fieldsFailingTheAssertion.clear();
  }

  private void assertRecursively(Predicate<Object> predicate, Object node, Class<?> nodeType, FieldLocation fieldLocation) {
    if (nodeMustBeIgnored(node, nodeType, fieldLocation)) return;

    boolean nodeAlreadyVisited = markNodeAsVisited(node);
    if (nodeAlreadyVisited) return;

    if (!isRootObject(fieldLocation) && shouldEvaluateAssertion(nodeType)) {
      evaluateAssertion(predicate, node, fieldLocation);
    }
    recurseIntoFieldsOfCurrentNode(predicate, node, nodeType, fieldLocation);
  }

  private boolean nodeMustBeIgnored(Object node, Class<?> nodeType, FieldLocation fieldLocation) {
    return isNullWhichAreIgnored(node)
           || isPrimitiveWhichAreIgnored(nodeType)
           || configuration.matchesAnIgnoredField(fieldLocation)
           || configuration.matchesAnIgnoredFieldRegex(fieldLocation)
           || configuration.getIgnoredTypes().contains(nodeType);
  }

  private boolean isRootObject(FieldLocation fieldLocation) {
    return fieldLocation.equals(rootFieldLocation());
  }

  private boolean isNullWhichAreIgnored(Object node) {
    return node == null && configuration.shouldIgnoreAllNullFields();
  }

  private boolean isPrimitiveWhichAreIgnored(Class<?> nodeType) {
    return configuration.shouldIgnorePrimitiveFields() && isPrimitiveOrWrapper(nodeType);
  }

  private void evaluateAssertion(Predicate<Object> predicate, Object node, FieldLocation fieldLocation) {
    if (assertionFails(predicate, node)) {
      fieldsFailingTheAssertion.add(fieldLocation);
    }
  }

  private boolean assertionFails(Predicate<Object> predicate, Object node) {
    return !predicate.test(node);
  }

  private boolean shouldEvaluateAssertion(Class<?> nodeType) {
    boolean ignoreContainerAssertion = configuration.shouldIgnoreContainer() && isContainer(nodeType);
    boolean ignoreMapAssertion = configuration.shouldIgnoreMap() && isMap(nodeType);
    boolean ignoreOptionalAssertion = configuration.shouldIgnoreOptional() && isOptionalOrPrimitiveOptional(nodeType);
    return !(ignoreContainerAssertion || ignoreMapAssertion || ignoreOptionalAssertion);
  }

  private boolean isContainer(Class<?> nodeType) {
    return isCollection(nodeType) || isArray(nodeType);
  }

  private void recurseIntoFieldsOfCurrentNode(Predicate<Object> predicate, Object node, Class<?> nodeType,
                                              FieldLocation fieldLocation) {
    if (isTypeRequiringSpecificHandling(nodeType)) {
      if (shouldRecurseOverSpecialTypes(nodeType)) {
        doRecursionForSpecialTypes(predicate, node, nodeType, fieldLocation);
      }
    } else if (shouldRecurseIntoNode(node)) {
      evaluateFieldsOfCurrentNodeRecursively(predicate, node, fieldLocation);
    }
  }

  private boolean isTypeRequiringSpecificHandling(Class<?> nodeType) {
    return isCollection(nodeType) || isMap(nodeType) || isArray(nodeType) || isOptionalOrPrimitiveOptional(nodeType);
  }

  private boolean shouldRecurseOverSpecialTypes(Class<?> nodeType) {
    boolean recurseOverContainer = isContainer(nodeType)
                                   && configuration.getCollectionAssertionPolicy() != COLLECTION_OBJECT_ONLY;
    boolean recurseOverMap = isMap(nodeType) && configuration.getMapAssertionPolicy() != MAP_OBJECT_ONLY;
    boolean recurseOverOptional = isOptionalOrPrimitiveOptional(nodeType)
                                  && configuration.getOptionalAssertionPolicy() != OPTIONAL_OBJECT_ONLY;
    return recurseOverContainer || recurseOverMap || recurseOverOptional;
  }

  private void doRecursionForSpecialTypes(Predicate<Object> predicate, Object node, Class<?> nodeType,
                                          FieldLocation fieldLocation) {
    if (isCollection(nodeType)) {
      recurseIntoCollection(predicate, (Collection<?>) node, fieldLocation);
    } else if (isArray(nodeType)) {
      recurseIntoArray(predicate, node, nodeType, fieldLocation);
    } else if (isMap(nodeType)) {
      recurseIntoMap(predicate, (Map<?, ?>) node, fieldLocation);
    } else if (isOptionalOrPrimitiveOptional(nodeType)) {
      recurseIntoOptional(predicate, node, fieldLocation);
    }
  }

  private void recurseIntoCollection(Predicate<Object> predicate, Collection<?> collection, FieldLocation fieldLocation) {
    // TODO handle collection if needed by policy
    if (collection == null) {
      return; // no way to recursive into the collection, anyway the collection node has already been visited
    }
    int index = 0;
    for (Object element : collection) {
      assertRecursively(predicate, element, safeGetClass(element), fieldLocation.field(format(INDEX_FORMAT, index)));
      index++;
    }
  }

  private void recurseIntoArray(Predicate<Object> predicate, Object node, Class<?> nodeType, FieldLocation fieldLocation) {
    if (node == null) {
      return; // no way to recursive into the array, anyway the array node has already been visited
    }
    Class<?> arrayType = nodeType.getComponentType();
    Object[] array = Arrays.asObjectArray(node);
    for (int i = 0; i < array.length; i++) {
      assertRecursively(predicate, array[i], arrayType, fieldLocation.field(format(INDEX_FORMAT, i)));
    }
  }

  private void recurseIntoOptional(Predicate<Object> predicate, Object node, FieldLocation fieldLocation) {
    // If we are here, we know the node is an optional or a primitive optional
    if (node instanceof Optional) {
      Optional<?> optionalNode = (Optional<?>) node;
      if (optionalNode.isPresent()) {
        Class<?> nextNodeType = safeGetClass(optionalNode.get());
        assertRecursively(predicate, optionalNode.get(), nextNodeType, fieldLocation.field("value"));
      }
    } else if (node instanceof OptionalInt) {
      OptionalInt optionalIntNode = (OptionalInt) node;
      if (optionalIntNode.isPresent()) {
        evaluateAssertion(predicate, optionalIntNode.getAsInt(), fieldLocation.field("value"));
      }
    } else if (node instanceof OptionalLong) {
      OptionalLong optionalLongNode = (OptionalLong) node;
      if (optionalLongNode.isPresent()) {
        evaluateAssertion(predicate, optionalLongNode.getAsLong(), fieldLocation.field("value"));
      }
    } else if (node instanceof OptionalDouble) {
      OptionalDouble optionalDoubleNode = (OptionalDouble) node;
      if (optionalDoubleNode.isPresent()) {
        evaluateAssertion(predicate, optionalDoubleNode.getAsDouble(), fieldLocation.field("value"));
      }
    }
  }

  private void recurseIntoMap(Predicate<Object> predicate, Map<?, ?> node, FieldLocation fieldLocation) {
    // If we are here, we can assume the policy is not MAP_OBJECT_ONLY
    // For both policies VALUES_ONLY and MAP_OBJECT_AND_ENTRIES we have to recurse over the values.
    if (node == null) {
      return; // no way to recursive into the map, anyway the map node has already been visited
    }
    recurseIntoMapValues(predicate, node, fieldLocation);
    if (configuration.getMapAssertionPolicy() == MAP_OBJECT_AND_ENTRIES) {
      recurseIntoMapKeys(predicate, node, fieldLocation);
    }
  }

  private void recurseIntoMapValues(Predicate<Object> predicate, Map<?, ?> currentNode, FieldLocation fieldLocation) {
    currentNode.values().forEach(nextNode -> recurseIntoMapElement(predicate, fieldLocation, nextNode, VALUE_FORMAT));
  }

  private void recurseIntoMapKeys(Predicate<Object> predicate, Map<?, ?> currentNode, FieldLocation fieldLocation) {
    currentNode.keySet().forEach(nextNode -> recurseIntoMapElement(predicate, fieldLocation, nextNode, KEY_FORMAT));
  }

  private void recurseIntoMapElement(Predicate<Object> predicate, FieldLocation fieldLocation, Object nextNode,
                                     String msgFormat) {
    Class<?> nextNodeType = safeGetClass(nextNode);
    String nextNodeFieldName = nextNode != null ? nextNode.toString() : NULL;
    assertRecursively(predicate, nextNode, nextNodeType, fieldLocation.field(format(msgFormat, nextNodeFieldName)));
  }

  private static Class<?> safeGetClass(Object object) {
    return object != null ? object.getClass() : Object.class;
  }

  private boolean shouldRecurseIntoNode(Object node) {
    return node != null && !nodeIsJavaTypeToIgnore(node);
  }

  private boolean nodeIsJavaTypeToIgnore(Object node) {
    String name = node.getClass().getCanonicalName();
    // best effort if canonical name is null
    if (name == null) name = node.getClass().getName();
    boolean isJCLType = name.startsWith("java.") || name.startsWith("javax.");
    return isJCLType && configuration.shouldSkipJavaLibraryTypeObjects();
  }

  private void evaluateFieldsOfCurrentNodeRecursively(Predicate<Object> predicate, Object node, FieldLocation fieldLocation) {
    configuration.getIntrospectionStrategy().getChildNodesOf(node)
                 .forEach(field -> assertRecursively(predicate, field.value, field.type, fieldLocation.field(field.name)));
  }

  private boolean markNodeAsVisited(Object node) {
    // Cannot mark null nodes, so just lie and say marking succeeded...
    if (node == null) return false;

    String objectId = identityToString(node);
    return !visitedNodeIds.add(objectId);
  }

  /*
   * This is taken verbatim from org.apache.commons.lang3.ObjectUtils .
   */
  private static String identityToString(final Object object) {
    if (object == null) {
      return null;
    }
    final String name = object.getClass().getName();
    final String hexString = Integer.toHexString(System.identityHashCode(object));
    return name + '@' + hexString;
  }

  private boolean isCollection(Class<?> nodeType) {
    return Collection.class.isAssignableFrom(nodeType);
  }

  private boolean isArray(Class<?> nodeType) {
    return nodeType.isArray();
  }

  private boolean isMap(Class<?> nodeType) {
    return Map.class.isAssignableFrom(nodeType);
  }

  // try to get the runtime type if possible or the declared one if not
}
