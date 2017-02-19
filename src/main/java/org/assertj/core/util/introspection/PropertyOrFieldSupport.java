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
package org.assertj.core.util.introspection;

import static java.lang.String.format;
import static org.assertj.core.util.Preconditions.checkArgument;

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

  public Object getSimpleValue(String propertyOrFieldName, Object input) {
    // first try to get given property values from objects, then try fields
    try {
      return propertySupport.propertyValueOf(propertyOrFieldName, Object.class, input);
    } catch (IntrospectionError propertyIntrospectionError) {
      // no luck with properties, let's try fields
      try {
        return fieldSupport.fieldValue(propertyOrFieldName, Object.class, input);
      } catch (IntrospectionError fieldIntrospectionError) {
        // no field nor property found with given name, it is considered as an error
        String message = format("%nCan't find any field or property with name '%s'.%n" +
                                "Error when introspecting properties was :%n" +
                                "- %s %n" +
                                "Error when introspecting fields was :%n" +
                                "- %s",
                                propertyOrFieldName, propertyIntrospectionError.getMessage(),
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
