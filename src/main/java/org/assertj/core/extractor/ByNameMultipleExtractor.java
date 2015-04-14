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
 * Copyright 2012-2015 the original author or authors.
 */
package org.assertj.core.extractor;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import org.assertj.core.groups.Tuple;

class ByNameMultipleExtractor<T> implements Function<T, Tuple> {

  private final String[] fieldsOrProperties;

  ByNameMultipleExtractor(String... fieldsOrProperties) {
    this.fieldsOrProperties = fieldsOrProperties;
  }

  @Override
  public Tuple apply(T input) {
    if (fieldsOrProperties == null)
      throw new IllegalArgumentException("The names of the fields/properties to read should not be null");
    if (fieldsOrProperties.length == 0)
      throw new IllegalArgumentException("The names of the fields/properties to read should not be empty");
    if (input == null)
      throw new IllegalArgumentException("The object to extract fields/properties from should not be null");

    List<Function<T, Object>> extractors = buildExtractors();
    List<Object> values = extractValues(input, extractors);
    
    return new Tuple(values.toArray());
  }

  private List<Object> extractValues(T input, List<Function<T, Object>> singleExtractors) {
    List<Object> values = new ArrayList<>();
    
    for (Function<T, Object> extractor : singleExtractors) {
      values.add(extractor.apply(input));
    }
    return values;
  }

  private List<Function<T, Object>> buildExtractors() {
    List<Function<T, Object>> result = new ArrayList<>();
    
    for (String name : fieldsOrProperties) {
      result.add(new ByNameSingleExtractor<>(name));
    }
    
    return result;
  }

}
