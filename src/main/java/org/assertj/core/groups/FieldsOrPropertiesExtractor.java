package org.assertj.core.groups;

import static java.lang.String.format;
import static org.assertj.core.groups.Tuple.buildTuples;
import static org.assertj.core.util.Lists.newArrayList;

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
   * Call {@link #extract(Iterable, String)} after converting objects to an iterable.
   * <p>
   * Behavior is described in javadoc {@link AbstractIterableAssert#extracting(String...)}
   */
  public static Tuple[] extract(Object[] objects, String... fieldsOrPropertiesNames) {
    ArrayList<Object> newArrayList = Lists.newArrayList(objects);
    List<Tuple> extractedValues = extract(newArrayList, fieldsOrPropertiesNames);
    return extractedValues.toArray(new Tuple[0]);
  }

  /**
   * Behavior is described in {@link AbstractIterableAssert#extracting(String)}
   */
  public static List<Object> extract(String propertyOrFieldName, Iterable<?> objects) {
    if (propertyOrFieldName == null)
      throw new IllegalArgumentException("The name of the field/property to read should not be null");
    if (propertyOrFieldName.length() == 0)
      throw new IllegalArgumentException("The name of the field/property to read should not be empty");
    if (objects == null)
      throw new IllegalArgumentException("The objects to extract field/property from should not be null");

    // first try to get given property values from objects, then try properties
    try {
      return PropertySupport.instance().propertyValues(propertyOrFieldName, objects);
    } catch (IntrospectionError fieldIntrospectionError) {
      // no luck with properties, let's try fields
      try {
        return FieldSupport.instance().fieldValues(propertyOrFieldName, objects);
      } catch (IntrospectionError propertyIntrospectionError) {
        // no field nor property found with given name, it is considered as an error
        String message = format(
            "\nCan't find any field or property with name '%s'.\nError when introspecting fields was :\n- %s \nError when introspecting properties was :\n- %s",
            propertyOrFieldName, fieldIntrospectionError.getMessage(), propertyIntrospectionError.getMessage());
        throw new IntrospectionError(message);
      }
    }
  }

  /**
   * Behavior is described in {@link AbstractIterableAssert#extracting(String...)}
   */
  public static List<Tuple> extract(Iterable<?> objects, String... fieldsOrPropertiesNames) {
    if (fieldsOrPropertiesNames == null)
      throw new IllegalArgumentException("The names of the fields/properties to read should not be null");
    if (fieldsOrPropertiesNames.length == 0)
      throw new IllegalArgumentException("The names of the fields/properties to read should not be empty");
    if (objects == null)
      throw new IllegalArgumentException("The objects to extract fields/properties from should not be null");
    // convert objects to a list to ensure consistent iteration order in extracteds fields/properties
    List<Object> objectsAsList = newArrayList(objects);
    List<Tuple> extractedTuples = buildTuples(objectsAsList.size());
    for (String fieldOrPropertyName : fieldsOrPropertiesNames) {
      List<Object> extractValues = extract(fieldOrPropertyName, objectsAsList);
      for (int i = 0; i < objectsAsList.size(); i++) {
        extractedTuples.get(i).addData(extractValues.get(i));
      }
    }
    return extractedTuples;
  }
}
