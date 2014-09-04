package org.assertj.core.extractor;

import static java.lang.String.*;

import org.assertj.core.api.iterable.Extractor;
import org.assertj.core.internal.PropertySupport;
import org.assertj.core.util.introspection.FieldSupport;
import org.assertj.core.util.introspection.IntrospectionError;

class ByNameSingleExtractor<T> implements Extractor<T, Object> {
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

    // first try to get given property values from objects, then try properties
    try {
      return PropertySupport.instance().propertyValueOf(propertyOrFieldName, Object.class, input);
    } catch (IntrospectionError fieldIntrospectionError) {
      // no luck with properties, let's try fields
      try {
        return FieldSupport.instance().fieldValue(propertyOrFieldName, Object.class, input);
      } catch (IntrospectionError propertyIntrospectionError) {
        // no field nor property found with given name, it is considered as an error
        String message = format(
            "\nCan't find any field or property with name '%s'.\nError when introspecting fields was :\n- %s \nError when introspecting properties was :\n- %s",
            propertyOrFieldName, fieldIntrospectionError.getMessage(), propertyIntrospectionError.getMessage());
        throw new IntrospectionError(message);
      }
    }
  }

}
