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

public class GetterIntrospectionStrategy implements IntrospectionStrategy {

  private static final GetterIntrospectionStrategy INSTANCE = new GetterIntrospectionStrategy();
  private static final PropertySupport propertySupport = PropertySupport.instance();

  public static GetterIntrospectionStrategy instance() {
    return INSTANCE;
  }

  @Override
  public Object getValue(String fieldName, Object target) {
    return propertySupport.propertyValueOf(fieldName, Object.class, target);
  }
}
