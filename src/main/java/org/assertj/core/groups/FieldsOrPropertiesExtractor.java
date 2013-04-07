package org.assertj.core.groups;

import static java.lang.String.format;

import java.util.ArrayList;
import java.util.List;

import org.assertj.core.api.AbstractIterableAssert;
import org.assertj.core.internal.PropertySupport;
import org.assertj.core.util.Lists;
import org.assertj.core.util.introspection.FieldSupport;
import org.assertj.core.util.introspection.IntrospectionError;

/**
 * 
 * Understands how to retrieve fields or properties values from a collection/array of objects.
 * <p>
 * You just have to give the field/property name, a collection/array of objects and it will extract the list of
 * field/property values from the given objects.
 * 
 * @author Joel Costigliola
 * 
 */
public class FieldsOrPropertiesExtractor {

  /**
   * Call {@link #extract(String, Iterable)} after converting objects to an iterable.
   * <p>
   * Behavior is described in javadoc {@link AbstractIterableAssert#extracting(String)}
   */
  public static Object[] extract(String fieldOrPropertyName, Object[] objects) {
    ArrayList<Object> newArrayList = Lists.newArrayList(objects);
    List<Object> extractedValues = extract(fieldOrPropertyName, newArrayList);
    return extractedValues.toArray();
  }
  
  /**
   * Behavior is described in {@link AbstractIterableAssert#extracting(String)}
   */
  public static List<Object> extract(String fieldOrPropertyName, Iterable<?> objects) {
    if (fieldOrPropertyName == null)
      throw new IllegalArgumentException("The name of the field/property to read should not be null");
    if (fieldOrPropertyName.length() == 0)
      throw new IllegalArgumentException("The name of the field/property to read should not be empty");
    if (objects == null)
      throw new IllegalArgumentException("The objects to extract field/property from should not be null");

    // first try to get given field values from objects, then try properties
    try {
      return FieldSupport.instance().fieldValues(fieldOrPropertyName, objects);
    } catch (IntrospectionError fieldIntrospectionError) {
      // no luck with fields, let's try properties
      try {
        return PropertySupport.instance().propertyValues(fieldOrPropertyName, objects);
      } catch (IntrospectionError propertyIntrospectionError) {
        // no field nor property found with given name, it is considered as an error
        String message = format("\nCan't find any field or property with name '%s'.\nError when introspecting fields was :\n- %s \nError when introspecting properties was :\n- %s",
                                fieldOrPropertyName, fieldIntrospectionError.getMessage(),
                                propertyIntrospectionError.getMessage());
        throw new IntrospectionError(message);
      }
    }
  }

}
