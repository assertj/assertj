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
import static java.util.Objects.requireNonNull;
import static java.util.stream.Collectors.toCollection;
import static java.util.stream.Collectors.toSet;
import static org.assertj.core.util.introspection.ClassUtils.isInJavaLangPackage;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.function.Predicate;

public abstract class AbstractRecursiveComparisonIntrospectionStrategy implements RecursiveComparisonIntrospectionStrategy {

  private boolean ignoreTransientFields = false;

  public void ignoreTransientFields() {
    ignoreTransientFields = true;
  }

  @Override
  public boolean shouldIgnoreTransientFields() {
    return ignoreTransientFields;
  }

  private Set<Field> getDeclaredFieldsIncludingInherited(Class<?> clazz) {
    requireNonNull(clazz, "expecting Class parameter not to be null");
    Set<Field> declaredFields = getDeclaredFields(clazz);
    // get fields declared in superClass
    Class<?> superClass = clazz.getSuperclass();
    while (!isInJavaLangPackage(superClass)) {
      declaredFields.addAll(getDeclaredFields(superClass));
      superClass = superClass.getSuperclass();
    }
    return declaredFields;
  }

  protected Set<String> getFieldsNames(Class<?> clazz) {
    return getDeclaredFieldsIncludingInherited(clazz).stream()
                                                     .map(Field::getName)
                                                     .collect(toSet());
  }

  private Set<Field> getDeclaredFields(Class<?> clazz) {
    Field[] declaredFields = clazz.getDeclaredFields();
    Predicate<Field> fieldPredicate = field -> !(field.isSynthetic() || Modifier.isStatic(field.getModifiers()));
    if (ignoreTransientFields) {
      fieldPredicate = fieldPredicate.and(field -> !Modifier.isTransient(field.getModifiers()));
    }
    return stream(declaredFields).filter(fieldPredicate)
                                 .collect(toCollection(LinkedHashSet::new));
  }

}
