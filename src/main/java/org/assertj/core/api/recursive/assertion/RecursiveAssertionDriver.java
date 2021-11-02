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

import static org.assertj.core.api.Assertions.tuple;
import static org.assertj.core.util.Lists.list;
import static org.assertj.core.util.Sets.newHashSet;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;

import org.assertj.core.api.recursive.FieldLocation;
import org.assertj.core.internal.Objects;
import org.assertj.core.util.introspection.FieldSupport;
import org.assertj.core.util.introspection.PropertyOrFieldSupport;
import org.junit.platform.commons.logging.Logger;
import org.junit.platform.commons.logging.LoggerFactory;

public class RecursiveAssertionDriver {
  
  private static final Logger LOGGER = LoggerFactory.getLogger(RecursiveAssertionDriver.class);

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
    boolean nodeWasAlreadyBlack = markNodeAsBlack(node);
    if (nodeWasAlreadyBlack) return;
    if (node == null && configuration.getIgnoreAllActualNullFields()) return;
    if (nodeType.isPrimitive() && !configuration.getAssertOverPrimitiveFields()) return;

    // TODO 1: Check conditions that should cause us to ignore this field (ignore by name, type, null...)
    doTheActualAssertionAndRegisterInCaseOfFailure(predicate, node, fieldLocation);
    // TODO 3: Check recursive conditions
    // TODO 4: Check for map/collections/arrays/optionals
    // TODO 5: Make the recursive call for all applicable fields
    recurseIntoFieldsOfCurrentNode(predicate, node, fieldLocation);
  }

  private void recurseIntoFieldsOfCurrentNode(Predicate<Object> predicate, Object node, FieldLocation fieldLocation) {
    if (nodeShouldBeRecursedInto(node)) {      
      findFieldsOfCurrentNodeAndDoRecursiveCall(predicate, node, fieldLocation);
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

}
