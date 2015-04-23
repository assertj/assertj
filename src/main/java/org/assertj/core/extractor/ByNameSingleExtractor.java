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

import static java.lang.String.format;

import java.util.Map;

import org.assertj.core.api.iterable.Extractor;
import org.assertj.core.internal.PropertySupport;
import org.assertj.core.util.introspection.FieldSupport;
import org.assertj.core.util.introspection.IntrospectionError;

class ByNameSingleExtractor<T> implements Extractor<T, Object> {
  private static final String SEPARATOR = ".";

  private final String propertyOrFieldName;

  ByNameSingleExtractor(String propertyOrFieldName) {
    this.propertyOrFieldName = propertyOrFieldName;
  }

  @Override
  public Object extract(T input) {
    if (propertyOrFieldName == null)
      throw new IllegalArgumentException("The name of the field/property to read should not be null");
    if (propertyOrFieldName.length() == 0)
      throw new IllegalArgumentException("The name of the field/property to read should not be empty");
    if (input == null)
      throw new IllegalArgumentException("The object to extract field/property from should not be null");

    // if input is a map, use propertyOrFieldName as a key
    if (input instanceof Map) {
      Map<?, ?> map = (Map<?, ?>) input;
      return map.get(propertyOrFieldName);
    }

    return extract(propertyOrFieldName, input);
  }

  private Object extract(String propertyOrFieldName, Object input) {
    if (isNested(propertyOrFieldName)) {
      String firstPropertyName = popNameFrom(propertyOrFieldName);
      Object propertyOrFieldValue = extractValue(firstPropertyName, input);
      // extract next sub-property/field value until reaching the last sub-property/field
      return extract(nextNameFrom(propertyOrFieldName), propertyOrFieldValue);
    }
    return extractValue(propertyOrFieldName, input);
  }

  private Object extractValue(String propertyOrFieldName, Object input) {
    // first try to get given property values from objects, then try fields
    try {
      return PropertySupport.instance().propertyValueOf(propertyOrFieldName, Object.class, input);
    } catch (IntrospectionError fieldIntrospectionError) {
      // no luck with properties, let's try fields
      try {
        return FieldSupport.extraction().fieldValue(propertyOrFieldName, Object.class, input);
      } catch (IntrospectionError propertyIntrospectionError) {
        // no field nor property found with given name, it is considered as an error
        String message = format("%nCan't find any field or property with name '%s'.%nError when introspecting fields was :%n- %s %nError when introspecting properties was :%n- %s",
                                propertyOrFieldName, fieldIntrospectionError.getMessage(),
                                propertyIntrospectionError.getMessage());
        throw new IntrospectionError(message);
      }
    }
  }

  private String popNameFrom(String propertyOrFieldNameChain) {
    if (!isNested(propertyOrFieldNameChain)) return propertyOrFieldNameChain;
    return propertyOrFieldNameChain.substring(0, propertyOrFieldNameChain.indexOf(SEPARATOR));
  }

  private String nextNameFrom(String propertyOrFieldNameChain) {
    if (!isNested(propertyOrFieldNameChain)) return "";
    return propertyOrFieldNameChain.substring(propertyOrFieldNameChain.indexOf(SEPARATOR) + 1);
  }

  private boolean isNested(String propertyOrFieldName) {
    return propertyOrFieldName.contains(SEPARATOR)
           && !propertyOrFieldName.startsWith(SEPARATOR)
           && !propertyOrFieldName.endsWith(SEPARATOR);
  }
}
