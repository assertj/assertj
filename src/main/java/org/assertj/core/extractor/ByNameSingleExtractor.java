/**
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 *
 * Copyright 2012-2017 the original author or authors.
 */
package org.assertj.core.extractor;

import static org.assertj.core.util.Preconditions.checkArgument;

import java.util.Map;

import org.assertj.core.api.iterable.Extractor;
import org.assertj.core.util.VisibleForTesting;
import org.assertj.core.util.introspection.PropertyOrFieldSupport;

class ByNameSingleExtractor<T> implements Extractor<T, Object> {

  private final String propertyOrFieldName;

  @VisibleForTesting
  ByNameSingleExtractor(String propertyOrFieldName) {
    this.propertyOrFieldName = propertyOrFieldName;
  }

  @Override
  public Object extract(T input) {
    checkArgument(propertyOrFieldName != null, "The name of the field/property to read should not be null");
    checkArgument(propertyOrFieldName.length() > 0, "The name of the field/property to read should not be empty");
    checkArgument(input != null, "The object to extract field/property from should not be null");

    // if input is a map, use propertyOrFieldName as a key
    if (input instanceof Map) {
      Map<?, ?> map = (Map<?, ?>) input;
      return map.get(propertyOrFieldName);
    }

    return PropertyOrFieldSupport.EXTRACTION.getValueOf(propertyOrFieldName, input);
  }

}
