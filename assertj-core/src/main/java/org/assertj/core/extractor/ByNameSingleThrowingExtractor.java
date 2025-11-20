/*
 * Copyright 2012-2025 the original author or authors.
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
package org.assertj.core.extractor;

import org.assertj.core.api.iterable.ThrowingExtractor;
import org.assertj.core.util.introspection.PropertyOrFieldSupport;

public class ByNameSingleThrowingExtractor implements ThrowingExtractor<Object, Object> {

  private final String propertyOrFieldName;

  ByNameSingleThrowingExtractor(String propertyOrFieldName) {
    this.propertyOrFieldName = propertyOrFieldName;
  }

  @Override
  public Object extractThrows(Object input) {
    return PropertyOrFieldSupport.EXTRACTION.getValueOf(propertyOrFieldName, input);
  }

}
