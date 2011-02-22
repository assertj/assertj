/*
 * Created on Jun 26, 2010
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 *
 * Copyright @2010-2011 the original author or authors.
 */
package org.fest.assertions.internal;

import static java.util.Collections.*;
import static org.fest.util.Collections.*;
import static org.fest.util.Introspection.descriptorForProperty;

import java.beans.PropertyDescriptor;
import java.util.*;

import org.fest.util.*;

/**
 * Utility methods for properties access.
 *
 * @author Joel Costigliola
 * @author Alex Ruiz
 */
public class PropertySupport {

  private static final String SEPARATOR = ".";

  private static final PropertySupport INSTANCE = new PropertySupport();

  /**
   * Returns the singleton instance of this class.
   * @return the singleton instance of this class.
   */
  public static PropertySupport instance() {
    return INSTANCE;
  }

  @VisibleForTesting JavaBeanDescriptor javaBeanDescriptor = new JavaBeanDescriptor();

  @VisibleForTesting PropertySupport() {}

  /**
   * Returns a <code>{@link List}</code> containing the values of the given property name, from the elements of the
   * given <code>{@link Collection}</code>. If the given {@code Collection} is empty or {@code null}, this method will
   * return an empty {@code List}. This method supports nested properties (e.g. "address.street.number").
   * @param propertyName the name of the property. It may be a nested property.
   * @param target the given {@code Collection}.
   * @return a {@code List} containing the values of the given property name, from the elements of the given
   * {@code Collection}.
   * @throws NullPointerException if the given property name is {@code null}.
   * @throws NullPointerException if the given property name is empty.
   * @throws IntrospectionError if an element in the given {@code Collection} does not have a property with a matching
   * name.
   */
  public List<Object> propertyValues(String propertyName, Collection<?> target) {
    verifyIsNotNullOrEmpty(propertyName);
    // ignore null elements as we can't extract a property from a null object
    Collection<?> cleanedUp = nonNullElements(target);
    if (isEmpty(cleanedUp)) return emptyList();
    if (isNestedProperty(propertyName)) {
      String firstPropertyName = popPropertyNameFrom(propertyName);
      List<Object> propertyValues = propertyValues(firstPropertyName, cleanedUp);
      // extract next sub-property values until reaching the last sub-property
      return propertyValues(nextPropertyNameFrom(propertyName), propertyValues);
    }
    return simplePropertyValues(propertyName, cleanedUp);
  }

  private void verifyIsNotNullOrEmpty(String propertyName) {
    if (propertyName == null) throw new NullPointerException("The name of the property to read should not be null");
    if (propertyName.length() == 0)
      throw new IllegalArgumentException("The name of the property to read should not be empty");
  }

  private List<Object> simplePropertyValues(String propertyName, Collection<?> target) {
    List<Object> propertyValues = new ArrayList<Object>();
    for (Object e : target)
      propertyValues.add(propertyValue(propertyName, e));
    return unmodifiableList(propertyValues);
  }

  private String popPropertyNameFrom(String propertyNameChain) {
    if (!isNestedProperty(propertyNameChain)) return propertyNameChain;
    return propertyNameChain.substring(0, propertyNameChain.indexOf(SEPARATOR));
  }

  private String nextPropertyNameFrom(String propertyNameChain) {
    if (!isNestedProperty(propertyNameChain)) return "";
    return propertyNameChain.substring(propertyNameChain.indexOf(SEPARATOR) + 1);
  }

  /*
   * isNestedProperty("address.street");       // true
   * isNestedProperty("address.street.name");  // true
   * isNestedProperty("person");               // false
   * isNestedProperty(".name");                // false
   * isNestedProperty("person.");              // false
   * isNestedProperty("person.name.");         // false
   * isNestedProperty(".person.name");         // false
   * isNestedProperty(".");                    // false
   * isNestedProperty("");                     // false
   */
  private boolean isNestedProperty(String propertyName) {
    return propertyName.contains(SEPARATOR) && !propertyName.startsWith(SEPARATOR) && !propertyName.endsWith(SEPARATOR);
  }

  private Object propertyValue(String propertyName, Object target) {
    PropertyDescriptor descriptor = descriptorForProperty(propertyName, target);
    try {
      return javaBeanDescriptor.invokeReadMethod(descriptor, target);
    } catch (Throwable unexpected) {
      String msg = String.format("Unable to obtain the value of the property <'%s'> from <%s>", propertyName, target);
      throw new IntrospectionError(msg, unexpected);
    }
  }
}
