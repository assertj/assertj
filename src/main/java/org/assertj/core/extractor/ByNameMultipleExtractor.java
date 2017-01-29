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

import java.util.ArrayList;
import java.util.List;

import org.assertj.core.api.iterable.Extractor;
import org.assertj.core.groups.Tuple;

public class ByNameMultipleExtractor<T> implements Extractor<T, Tuple>{

  private final String[] fieldsOrProperties;

  public ByNameMultipleExtractor(String... fieldsOrProperties) {
    this.fieldsOrProperties = fieldsOrProperties;
  }

  @Override
  public Tuple extract(T input) {
    checkArgument(fieldsOrProperties != null, "The names of the fields/properties to read should not be null");
    checkArgument(fieldsOrProperties.length > 0, "The names of the fields/properties to read should not be empty");
    checkArgument(input != null, "The object to extract fields/properties from should not be null");

    List<Extractor<T, Object>> extractors = buildExtractors();
    List<Object> values = extractValues(input, extractors);
    
    return new Tuple(values.toArray());
  }

  private List<Object> extractValues(T input, List<Extractor<T, Object>> singleExtractors) {
    List<Object> values = new ArrayList<>();
    
    for (Extractor<T, Object> extractor : singleExtractors) {
      values.add(extractor.extract(input));
    }
    return values;
  }

  private List<Extractor<T, Object>> buildExtractors() {
    List<Extractor<T, Object>> result = new ArrayList<>();
    
    for (String name : fieldsOrProperties) {
      result.add(new ByNameSingleExtractor<T>(name));
    }
    
    return result;
  }

}
