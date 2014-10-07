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
package org.assertj.core.util.introspection;

import static java.lang.String.format;
import static java.util.Collections.emptyList;
import static java.util.Collections.unmodifiableList;
import static org.assertj.core.util.ArrayWrapperList.wrap;
import static org.assertj.core.util.Iterables.isNullOrEmpty;

import java.util.ArrayList;
import java.util.List;

/**
 * Utility methods for fields access.
 * 
 * @author Joel Costigliola
 */
public class FieldSupport {

  private static final String SEPARATOR = ".";

  private static final FieldSupport INSTANCE = new FieldSupport(true);

  private boolean allowExtractingPrivateFields;

  /**
   * Returns the singleton instance of this class.
   * 
   * @return the singleton instance of this class.
   */
  public static FieldSupport instance() {
	return INSTANCE;
  }

  /**
   * Build a new {@link FieldSupport}
   * 
   * @param allowExtractingPrivateFields wether to read private fields or not.
   */
  public FieldSupport(boolean allowExtractingPrivateFields) {
	this.allowExtractingPrivateFields = allowExtractingPrivateFields;
  }

  /**
   * Globally set whether
   * <code>{@link org.assertj.core.api.AbstractIterableAssert#extracting(String) IterableAssert#extracting(String)}</code>
   * and
   * <code>{@link org.assertj.core.api.AbstractObjectArrayAssert#extracting(String) ObjectArrayAssert#extracting(String)}</code>
   * should be allowed to extract private fields, if not and they try it fails with exception.
   *
   * @param allowExtractingPrivateFields allow private fields extraction. Default {@code true}.
   */
  public static void setAllowExtractingPrivateFields(boolean allowExtractingPrivateFields) {
	FieldSupport.INSTANCE.allowExtractingPrivateFields = allowExtractingPrivateFields;
  }

  /**
   * Returns a <code>{@link List}</code> containing the values of the given field name, from the elements of the given
   * <code>{@link Iterable}</code>. If the given {@code Iterable} is empty or {@code null}, this method will return an
   * empty {@code List}. This method supports nested fields (e.g. "address.street.number").
   * 
   * @param fieldName the name of the field. It may be a nested field. It is left to the clients to validate for
   *          {@code null} or empty.
   * @param fieldClass the expected type of the given field.
   * @param target the given {@code Iterable}.
   * @return an {@code Iterable} containing the values of the given field name, from the elements of the given
   *         {@code Iterable}.
   * @throws IntrospectionError if an element in the given {@code Iterable} does not have a field with a matching name.
   */
  public <T> List<T> fieldValues(String fieldName, Class<T> fieldClass, Iterable<?> target) {
	if (isNullOrEmpty(target)) return emptyList();

	if (isNestedField(fieldName)) {
	  String firstFieldName = popFieldNameFrom(fieldName);
	  Iterable<Object> fieldValues = fieldValues(firstFieldName, Object.class, target);
	  // extract next sub-field values until reaching the last sub-field
	  return fieldValues(nextFieldNameFrom(fieldName), fieldClass, fieldValues);
	}
	return simpleFieldValues(fieldName, fieldClass, target);
  }

  public List<Object> fieldValues(String fieldName, Iterable<?> target) {
	return fieldValues(fieldName, Object.class, target);
  }

  /**
   * Returns a <code>{@link List}</code> containing the values of the given field name, from the elements of the given
   * <code>{@link Iterable}</code>. If the given {@code Iterable} is empty or {@code null}, this method will return an
   * empty {@code List}. This method supports nested fields (e.g. "address.street.number").
   * 
   * @param fieldName the name of the field. It may be a nested field. It is left to the clients to validate for
   *          {@code null} or empty.
   * @param fieldClass the expected type of the given field.
   * @param target the given {@code Iterable}.
   * @return an {@code Iterable} containing the values of the given field name, from the elements of the given
   *         {@code Iterable}.
   * @throws IntrospectionError if an element in the given {@code Iterable} does not have a field with a matching name.
   */
  public <T> List<T> fieldValues(String fieldName, Class<T> fieldClass, Object[] target) {
	return fieldValues(fieldName, fieldClass, wrap(target));
  }

  private <T> List<T> simpleFieldValues(String fieldName, Class<T> clazz, Iterable<?> target) {
	List<T> fieldValues = new ArrayList<T>();
	for (Object e : target) {
	  fieldValues.add(e == null ? null : fieldValue(fieldName, clazz, e));
	}
	return unmodifiableList(fieldValues);
  }

  private String popFieldNameFrom(String fieldNameChain) {
	if (!isNestedField(fieldNameChain)) {
	  return fieldNameChain;
	}
	return fieldNameChain.substring(0, fieldNameChain.indexOf(SEPARATOR));
  }

  private String nextFieldNameFrom(String fieldNameChain) {
	if (!isNestedField(fieldNameChain)) {
	  return "";
	}
	return fieldNameChain.substring(fieldNameChain.indexOf(SEPARATOR) + 1);
  }

  /**
   * <pre>
   * isNestedField(&quot;address.street&quot;); // true
   * isNestedField(&quot;address.street.name&quot;); // true
   * isNestedField(&quot;person&quot;); // false
   * isNestedField(&quot;.name&quot;); // false
   * isNestedField(&quot;person.&quot;); // false
   * isNestedField(&quot;person.name.&quot;); // false
   * isNestedField(&quot;.person.name&quot;); // false
   * isNestedField(&quot;.&quot;); // false
   * isNestedField(&quot;&quot;); // false
   * </pre>
   */
  private boolean isNestedField(String fieldName) {
	return fieldName.contains(SEPARATOR) && !fieldName.startsWith(SEPARATOR) && !fieldName.endsWith(SEPARATOR);
  }

  /**
   * Return the value of field from a target object.
   * <p>
   * Return null if field is nested and one of the nested value is null, ex :
   * 
   * <pre>
   * fieldValue(race.name, String.class, frodo) will return null if frodo.race is null
   * </pre>
   * 
   * @param fieldName the name of the field. It may be a nested field. It is left to the clients to validate for
   *          {@code null} or empty.
   * @param target the given object
   * @param clazz type of field
   * @return a the values of the given field name
   * @throws IntrospectionError if the given target does not have a field with a matching name.
   */
  public <T> T fieldValue(String fieldName, Class<T> fieldClass, Object target) {
	if (target == null) return null;

	if (isNestedField(fieldName)) {
	  String outerFieldName = popFieldNameFrom(fieldName);
	  Object outerFieldValue = readSimpleField(outerFieldName, Object.class, target);
	  // extract next sub-field values until reaching the last sub-field
	  return fieldValue(nextFieldNameFrom(fieldName), fieldClass, outerFieldValue);
	}
	return readSimpleField(fieldName, fieldClass, target);
  }

  private <T> T readSimpleField(String fieldName, Class<T> clazz, Object target) {
	try {
	  Object readField = FieldUtils.readField(target, fieldName, allowExtractingPrivateFields);
	  return clazz.cast(readField);
	} catch (ClassCastException e) {
	  String msg = format("Unable to obtain the value of the field <'%s'> from <%s> - wrong field type specified <%s>",
		                  fieldName, target, clazz);
	  throw new IntrospectionError(msg, e);
	} catch (IllegalAccessException iae) {
	  String msg = format("Unable to obtain the value of the field <'%s'> from <%s>, check that field is public.",
		                  fieldName, target);
	  throw new IntrospectionError(msg, iae);
	} catch (Throwable unexpected) {
	  String msg = format("Unable to obtain the value of the field <'%s'> from <%s>", fieldName, target);
	  throw new IntrospectionError(msg, unexpected);
	}
  }

}
