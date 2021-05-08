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
package org.assertj.core.util.introspection;

import static java.util.Arrays.stream;
import static java.util.Locale.ENGLISH;
import static java.util.Objects.requireNonNull;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Set;
import java.util.stream.Collectors;

public class GetterIntrospectionStrategy implements IntrospectionStrategy {

  private static final GetterIntrospectionStrategy INSTANCE = new GetterIntrospectionStrategy();
  private static final PropertySupport propertySupport = PropertySupport.instance();

  private static final String GETTER_REGEX = "^(is|get)\\p{Upper}\\w*";

  public static GetterIntrospectionStrategy instance() {
    return INSTANCE;
  }

  @Override
  public Set<String> getMemberNames(Class<?> clazz) {
    requireNonNull(clazz, "expecting Class parameter not to be null");
    Set<String> declaredFields = getDeclaredMethodsIgnoringSyntheticAndStatic(clazz);

    Class<?> superclazz = clazz.getSuperclass();
    while (superclazz != null && !superclazz.getName().startsWith("java.lang")) {
      declaredFields.addAll(getDeclaredMethodsIgnoringSyntheticAndStatic(superclazz));
      superclazz = superclazz.getSuperclass();
    }

    return declaredFields;
  }

  @Override
  public Set<String> getMemberNamesAsFields(Class<?> clazz) {
    return getMemberNames(clazz)
                                .stream()
                                .map(s -> s.startsWith("is") ? s.substring(2) : s.substring(3))
                                .map(s -> s.substring(0, 1).toLowerCase(ENGLISH) + s.substring(1))
                                .collect(Collectors.toSet());
  }

  private Set<String> getDeclaredMethodsIgnoringSyntheticAndStatic(Class<?> clazz) {
    return stream(clazz.getDeclaredMethods())
                                             .filter(member -> !(member.isSynthetic()))
                                             .filter(member -> !Modifier.isStatic(member.getModifiers()))
                                             .map(Method::getName)
                                             .filter(member -> member.matches(GETTER_REGEX))
                                             .collect(Collectors.toSet());
  }

  @Override
  public Object getValue(String fieldName, Object target) {
    return propertySupport.propertyValueOf(fieldName, Object.class, target);
  }
}
