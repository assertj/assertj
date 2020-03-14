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

import static java.util.stream.Collectors.toList;
import static org.assertj.core.util.Preconditions.checkArgument;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

import org.assertj.core.groups.Tuple;

class ByNameMultipleExtractor implements Function<Object, Tuple> {

  private final String[] fieldsOrProperties;

  ByNameMultipleExtractor(String... fieldsOrProperties) {
    this.fieldsOrProperties = fieldsOrProperties;
  }

  @Override
  public Tuple apply(Object input) {
    checkArgument(fieldsOrProperties != null, "The names of the fields/properties to read should not be null");
    checkArgument(fieldsOrProperties.length > 0, "The names of the fields/properties to read should not be empty");
    checkArgument(input != null, "The object to extract fields/properties from should not be null");

    List<Function<Object, Object>> extractors = buildExtractors();
    List<Object> values = extractValues(input, extractors);

    return new Tuple(values.toArray());
  }

  private List<Function<Object, Object>> buildExtractors() {
    return Arrays.stream(fieldsOrProperties).map(ByNameSingleExtractor::new).collect(toList());
  }

  private List<Object> extractValues(Object input, List<Function<Object, Object>> singleExtractors) {
    return singleExtractors.stream().map(extractor -> extractor.apply(input)).collect(toList());
  }

}
