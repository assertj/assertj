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
 * Copyright 2012-2025 the original author or authors.
 */
package org.assertj.core.util.introspection;

import static java.lang.String.format;
import static org.assertj.core.util.Preconditions.checkArgument;

import java.util.Map;
import java.util.Optional;

import org.assertj.core.util.VisibleForTesting;

public class PropertyOrFieldSupport {
  private static final String SEPARATOR = ".";
  private PropertySupport propertySupport;
  private FieldSupport fieldSupport;

  public static final PropertyOrFieldSupport EXTRACTION = new PropertyOrFieldSupport();
  public static final PropertyOrFieldSupport COMPARISON = new PropertyOrFieldSupport(PropertySupport.instance(),
                                                                                     FieldSupport.COMPARISON);

  PropertyOrFieldSupport() {
    this.propertySupport = PropertySupport.instance();
    this.fieldSupport = FieldSupport.extraction();
  }

  @VisibleForTesting
  PropertyOrFieldSupport(PropertySupport propertySupport, FieldSupport fieldSupport) {
    this.propertySupport = propertySupport;
    this.fieldSupport = fieldSupport;
  }

  public void setAllowUsingPrivateFields(boolean allowUsingPrivateFields) {
    fieldSupport.setAllowUsingPrivateFields(allowUsingPrivateFields);
  }

  public Object getValueOf(String propertyOrFieldName, Object input) {
    checkArgument(propertyOrFieldName != null, "The name of the property/field to read should not be null");
    checkArgument(!propertyOrFieldName.isEmpty(), "The name of the property/field to read should not be empty");
    checkArgument(input != null, "The object to extract property/field from should not be null");

    if (isNested(propertyOrFieldName)) {
      String firstPropertyName = popNameFrom(propertyOrFieldName);
      Object propertyOrFieldValue = getSimpleValue(firstPropertyName, input);
      // when one of the intermediate nested property/field value is null, return null
      if (propertyOrFieldValue == null) return null;
      // extract next sub-property/field value until reaching the last sub-property/field
      return getValueOf(nextNameFrom(propertyOrFieldName), propertyOrFieldValue);
    }

    return getSimpleValue(propertyOrFieldName, input);
  }

  @SuppressWarnings({ "unchecked", "rawtypes" })
  public Object getSimpleValue(String name, Object input) {
    // if input is an optional and name is "value", let's get the optional value directly
    if (input instanceof Optional && name.equals("value")) return ((Optional) input).orElse(null);

    try {
      // try to get name as a property
      return propertySupport.propertyValueOf(name, Object.class, input);
    } catch (IntrospectionError propertyIntrospectionError) {
      // try to get name as a field
      try {
        return fieldSupport.fieldValue(name, Object.class, input);
      } catch (IntrospectionError fieldIntrospectionError) {
        // if input is a map, try to use the name value as a map key
        if (input instanceof Map) {
          Map<?, ?> map = (Map<?, ?>) input;
          if (map.containsKey(name)) return map.get(name);
        }

        // if the getter invocation throws exception and there's no field present,
        // we'll propagate the IntrospectionError containing the original exception
        if (propertyIntrospectionError.getterInvocationException().isPresent()) {
          throw propertyIntrospectionError;
        }

        // no value found with given name, it is considered as an error
        String message = format("%nCan't find any field or property with name '%s'.%n" +
                                "Error when introspecting properties was :%n" +
                                "- %s %n" +
                                "Error when introspecting fields was :%n" +
                                "- %s",
                                name, propertyIntrospectionError.getMessage(),
                                fieldIntrospectionError.getMessage());
        throw new IntrospectionError(message, fieldIntrospectionError);
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
