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

import java.lang.reflect.Method;
import java.util.Set;

/**
 * A {@link RecursiveComparisonIntrospectionStrategy} that introspects properties by looking at public getters like
 * {@code getName()} or {@code isActive()}/{@code getActive()} for regular objects and record components for records.
 */
public class ComparingProperties extends AbstractRecursiveComparisonIntrospectionStrategy {

  /** Shared property introspection strategy instance. */
  public static final ComparingProperties COMPARING_PROPERTIES = new ComparingProperties();

  private final ComparingRegularProperties regularProperties = new ComparingRegularProperties();
  private final ComparingRecordProperties recordProperties = new ComparingRecordProperties();

  /** Creates a new property introspection strategy. */
  public ComparingProperties() {}

  @Override
  public Set<String> getChildrenNodeNamesOf(Object node) {
    return strategyFor(node).getChildrenNodeNamesOf(node);
  }

  @Override
  public Object getChildNodeValue(String childNodeName, Object instance) {
    return strategyFor(instance).getChildNodeValue(childNodeName, instance);
  }

  @Override
  public String getDescription() {
    return "comparing properties";
  }

  @Override
  public void ignoreTransientFields() {
    throw new IllegalArgumentException("ignoringTransientFields is not supported since we are comparing properties");
  }

  /**
   * Returns public getters declared by the given class or inherited from its superclasses, or record component
   * accessors when the given class is a record.
   *
   * @param clazz the class to inspect
   * @return the getter methods
   */
  public static Set<Method> gettersIncludingInheritedOf(Class<?> clazz) {
    return clazz.isRecord() ? ComparingRecordProperties.gettersIncludingInheritedOf(clazz)
        : ComparingRegularProperties.gettersIncludingInheritedOf(clazz);
  }

  private RecursiveComparisonIntrospectionStrategy strategyFor(Object node) {
    return node != null && node.getClass().isRecord() ? recordProperties : regularProperties;
  }

}
