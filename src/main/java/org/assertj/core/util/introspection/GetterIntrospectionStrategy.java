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

import static java.util.Locale.ENGLISH;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Set;
import java.util.stream.Collectors;

public class GetterIntrospectionStrategy implements IntrospectionStrategy {

  private static final GetterIntrospectionStrategy INSTANCE = new GetterIntrospectionStrategy();

  private static final String GETTER_REGEX = "^(is|get)\\p{Upper}\\w*";

  public static GetterIntrospectionStrategy instance() {
    return INSTANCE;
  }

  @Override
  public Set<String> getMemberNames(Class<?> clazz) {
    return org.assertj.core.internal.Objects.getMethodNames(clazz)
                                            .stream()
                                            .filter(member -> member.matches(GETTER_REGEX))
                                            .collect(Collectors.toSet());
  }

  @Override
  public Set<String> getMemberNamesAsFields(Class<?> clazz) {
    return getMemberNames(clazz)
                                .stream()
                                .map(name -> name.startsWith("is") ? name.substring(2) : name.substring(3))
                                .map(name -> name.substring(0, 1).toLowerCase(ENGLISH) + name.substring(1))
                                .collect(Collectors.toSet());
  }

  @Override
  public Object getMemberValue(String memberName, Object target) {
    try {
      Method getter = target.getClass().getMethod(memberName);
      return getter.invoke(target);
    } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
      throw new IntrospectionError("Getter problem", e);
    }
  }
}
