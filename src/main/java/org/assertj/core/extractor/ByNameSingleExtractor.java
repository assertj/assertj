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
 * Copyright 2012-2020 the original author or authors.
 */
package org.assertj.core.extractor;

import java.util.function.Function;

import org.assertj.core.util.introspection.PropertyOrFieldSupport;

class ByNameSingleExtractor implements Function<Object, Object> {

  private final String propertyOrFieldName;

  ByNameSingleExtractor(String propertyOrFieldName) {
    this.propertyOrFieldName = propertyOrFieldName;
  }

  @Override
  public Object apply(Object input) {
    return PropertyOrFieldSupport.EXTRACTION.getValueOf(propertyOrFieldName, input);
  }

}
