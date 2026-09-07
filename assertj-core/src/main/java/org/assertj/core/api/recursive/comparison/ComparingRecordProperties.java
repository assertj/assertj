/*
 * Copyright 2012-2026 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.assertj.core.api.recursive.comparison;

import static java.util.Arrays.stream;
import static java.util.stream.Collectors.toSet;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.RecordComponent;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.assertj.core.util.introspection.IntrospectionError;

/**
 * A {@link RecursiveComparisonIntrospectionStrategy} that introspects Java records through their record components.
 */
final class ComparingRecordProperties implements RecursiveComparisonIntrospectionStrategy {

  // use ConcurrentHashMap in case this strategy instance is used in a multi-thread context
  private final Map<Class<?>, Set<String>> recordComponentNamesPerClass = new ConcurrentHashMap<>();
  private final Map<GetterKey, Method> GETTER_CACHE = new ConcurrentHashMap<>();

  @Override
  public Set<String> getChildrenNodeNamesOf(Object node) {
    return node != null && node.getClass().isRecord()
        ? recordComponentNamesPerClass.computeIfAbsent(node.getClass(), ComparingRecordProperties::getPropertiesNamesOf)
        : new HashSet<>();
  }

  @Override
  public Object getChildNodeValue(String childNodeName, Object instance) {
    if (instance == null || !instance.getClass().isRecord()) {
      throw new IllegalArgumentException("given instance is not record " + (instance == null ? "null" : instance.getClass()));
    }

    Class<?> clazz = instance.getClass();
    GetterKey getterKey = new GetterKey(childNodeName, clazz);
    Method getter = GETTER_CACHE.computeIfAbsent(getterKey, ComparingRecordProperties::getterOf);
    try {
      getter.setAccessible(true);
      return getter.invoke(instance);
    } catch (InvocationTargetException e) {
      String message = "Unable to read record component <'%s'> from <%s>".formatted(childNodeName, clazz);
      throw new IntrospectionError(message, e, e.getTargetException());
    } catch (IllegalAccessException e) {
      String message = "Unable to read record component <'%s'> from <%s>".formatted(childNodeName, clazz);
      throw new IntrospectionError(message, e);
    }
  }

  private static Method getterOf(GetterKey getterKey) {
    return stream(getterKey.clazz.getRecordComponents()).filter(component -> component.getName().equals(getterKey.name))
                                                        .findFirst()
                                                        .map(RecordComponent::getAccessor)
                                                        .orElseThrow(() -> new IntrospectionError("No record component '%s' in %s".formatted(getterKey.name,
                                                                                                                                             getterKey.clazz)));
  }

  @Override
  public String getDescription() {
    return "comparing records";
  }

  private static Set<String> getPropertiesNamesOf(Class<?> recordType) {
    return stream(recordType.getRecordComponents()).map(RecordComponent::getName).collect(toSet());
  }

  private record GetterKey(String name, Class<?> clazz) {
  }

}
