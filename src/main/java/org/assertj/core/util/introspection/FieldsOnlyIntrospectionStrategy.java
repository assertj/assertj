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

import org.assertj.core.internal.Objects;

import java.lang.reflect.Field;
import java.util.Set;
import java.util.stream.Collectors;

public class FieldsOnlyIntrospectionStrategy implements IntrospectionStrategy {

  private static final FieldsOnlyIntrospectionStrategy INSTANCE = new FieldsOnlyIntrospectionStrategy();
  private static final FieldSupport fieldSupport = FieldSupport.COMPARISON;

  public static FieldsOnlyIntrospectionStrategy instance() {
    return INSTANCE;
  }

  @Override
  public Object getMemberValue(String memberName, Object target) {
    return fieldSupport.fieldValue(memberName, Object.class, target);
  }


  @Override
  public Set<String> getMemberNamesAsFields(Class<?> clazz) {
    return getMemberNames(clazz);
  }

  @Override
  public Set<String> getMemberNames(Class<?> clazz) {
    return Objects.getFieldsNames(clazz);
  }
}
