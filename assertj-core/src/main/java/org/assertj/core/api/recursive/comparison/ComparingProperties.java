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

import static java.util.Arrays.stream;
import static java.util.stream.Collectors.toCollection;
import static java.util.stream.Collectors.toSet;
import static org.assertj.core.util.introspection.ClassUtils.isInJavaLangPackage;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.assertj.core.util.introspection.PropertySupport;

/**
 * A {@link RecursiveComparisonIntrospectionStrategy} that introspects properties by looking at public getters like
 * {@code getName()} or {@code isActive()}/{@code getActive()} for boolean properties.
 */
public class ComparingProperties implements RecursiveComparisonIntrospectionStrategy {

  public static final ComparingProperties COMPARING_PROPERTIES = new ComparingProperties();

  private static final String GET_PREFIX = "get";
  private static final String IS_PREFIX = "is";

  // use ConcurrentHashMap in case this strategy instance is used in a multi-thread context
  private final Map<Class<?>, Set<String>> propertiesNamesPerClass = new ConcurrentHashMap<>();

  @Override
  public Set<String> getChildrenNodeNamesOf(Object node) {
    if (node == null) return new HashSet<>();
    return propertiesNamesPerClass.computeIfAbsent(node.getClass(), ComparingProperties::getPropertiesNamesOf);
  }

  @Override
  public Object getChildNodeValue(String childNodeName, Object instance) {
    return PropertySupport.instance().propertyValueOf(childNodeName, Object.class, instance);
  }

  @Override
  public String getDescription() {
    return "comparing properties";
  }

  static Set<String> getPropertiesNamesOf(Class<?> clazz) {
    return gettersIncludingInheritedOf(clazz).stream()
                                             .map(Method::getName)
                                             .map(ComparingProperties::toPropertyName)
                                             .collect(toSet());
  }

  private static String toPropertyName(String methodName) {
    String propertyWithCapitalLetter = methodName.startsWith(GET_PREFIX)
        ? methodName.substring(GET_PREFIX.length())
        : methodName.substring(IS_PREFIX.length());
    return propertyWithCapitalLetter.toLowerCase().charAt(0) + propertyWithCapitalLetter.substring(1);
  }

  public static Set<Method> gettersIncludingInheritedOf(Class<?> clazz) {
    return gettersOf(clazz);
  }

  private static Set<Method> gettersOf(Class<?> clazz) {
    return stream(clazz.getMethods()).filter(method -> !isInJavaLangPackage(method.getDeclaringClass()))
                                     .filter(method -> !isStatic(method))
                                     .filter(ComparingProperties::isGetter)
                                     .collect(toCollection(LinkedHashSet::new));
  }

  private static boolean isStatic(Method method) {
    return Modifier.isStatic(method.getModifiers());
  }

  private static boolean isGetter(Method method) {
    if (hasParameters(method)) return false;
    return isRegularGetter(method) || isBooleanProperty(method);
  }

  private static boolean isRegularGetter(Method method) {
    return method.getName().startsWith(GET_PREFIX);
  }

  private static boolean hasParameters(Method method) {
    return method.getParameters().length > 0;
  }

  private static boolean isBooleanProperty(Method method) {
    Class<?> returnType = method.getReturnType();
    return method.getName().startsWith(IS_PREFIX) && (returnType.equals(boolean.class) || returnType.equals(Boolean.class));
  }

}
