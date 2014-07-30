/*
 * Created on Jan 28, 2011
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this Throwable except in compliance
 * with the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 * 
 * Copyright @2011 the original author or authors.
 */
package org.assertj.core.groups;

import static java.lang.String.*;
import static org.assertj.core.groups.Tuple.*;
import static org.assertj.core.util.Lists.*;

import java.util.List;

import org.assertj.core.api.AbstractIterableAssert;
import org.assertj.core.api.iterable.Extractor;
import org.assertj.core.internal.PropertySupport;
import org.assertj.core.util.Lists;
import org.assertj.core.util.introspection.FieldSupport;
import org.assertj.core.util.introspection.IntrospectionError;

/**
 * 
 * Understands how to retrieve fields or values from a collection/array of objects.
 * <p>
 * You just have to give the field/property name or an {@link Extractor} implementation, a collection/array of objects
 * and it will extract the list of field/values from the given objects.
 * 
 * @author Joel Costigliola
 * @author Mateusz Haligowski
 * 
 */
public class FieldsOrPropertiesExtractor {

  /**
   * Call {@link #extract(Iterable, String)} after converting objects to an iterable.
   * <p>
   * Behavior is described in javadoc {@link AbstractIterableAssert#extracting(String)}
   */
  public static Object[] extract(Object[] objects, String fieldOrPropertyName) {
    List<Object> newArrayList = Lists.newArrayList(objects);
    List<Object> extractedValues = extract(newArrayList, fieldOrPropertyName);
    return extractedValues.toArray();
  }

  /**
   * Call {@link #extract(Iterable, String...)} after converting objects to an iterable.
   * <p>
   * Behavior is described in javadoc {@link AbstractIterableAssert#extracting(String...)}
   */
  public static Tuple[] extract(Object[] objects, String... fieldsOrPropertiesNames) {
    List<Object> newArrayList = Lists.newArrayList(objects);
    List<Tuple> extractedValues = extract(newArrayList, fieldsOrPropertiesNames);
    return extractedValues.toArray(new Tuple[extractedValues.size()]);
  }
  
  @SuppressWarnings("unchecked")
  public static <F, T> T[] extract(F[] objects, Extractor<F, T> extractor) {
    List<F> objectsList = Lists.newArrayList(objects);
    
    List<T> extractedValues = extract(objectsList, extractor);
    
    return (T[]) extractedValues.toArray();
  }

  /**
   * Behavior is described in {@link AbstractIterableAssert#extracting(String)}
   */
  public static List<Object> extract(Iterable<?> objects, String propertyOrFieldName) {
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
    // convert objects to a list to ensure consistent iteration order in extracted fields/properties
    List<Object> objectsAsList = newArrayList(objects);
    List<Tuple> extractedTuples = buildTuples(objectsAsList.size());
    for (String fieldOrPropertyName : fieldsOrPropertiesNames) {
      List<Object> extractValues = extract(objectsAsList, fieldOrPropertyName);
      for (int i = 0; i < objectsAsList.size(); i++) {
        extractedTuples.get(i).addData(extractValues.get(i));
      }
    }
    return extractedTuples;
  }

  /**
   * Behavior is described in {@link AbstractIterableAssert#extracting(Extractor)} 
   */
  public static <F, T> List<T> extract(Iterable<F> objects, Extractor<F, T> extractor) {
    List<T> result = Lists.newArrayList();
    
    for (F object : objects) {
      final T newValue = extractor.extract(object);
      result.add(newValue);
    }
    
    return result;
  }

}
