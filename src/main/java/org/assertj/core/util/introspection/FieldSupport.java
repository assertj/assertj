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
 * Copyright 2012-2019 the original author or authors.
 */
package org.assertj.core.util.introspection;

import static java.lang.String.format;
import static java.lang.reflect.Modifier.isPublic;
import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toList;
import static org.assertj.core.util.ArrayWrapperList.wrap;
import static org.assertj.core.util.IterableUtil.isNullOrEmpty;
import static org.assertj.core.util.Streams.stream;
import static org.assertj.core.util.introspection.FieldUtils.readField;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.List;

import org.assertj.core.util.VisibleForTesting;

/**
 * Utility methods for fields access.
 *
 * @author Joel Costigliola
 */
public enum FieldSupport {

  EXTRACTION(true), EXTRACTION_OF_PUBLIC_FIELD_ONLY(false), COMPARISON(true);

  private static final String CHAR = "char";
  private static final String BOOLEAN = "boolean";
  private static final String DOUBLE = "double";
  private static final String FLOAT = "float";
  private static final String LONG = "long";
  private static final String INT = "int";
  private static final String SHORT = "short";
  private static final String BYTE = "byte";

  private static final String SEPARATOR = ".";

  private boolean allowUsingPrivateFields;

  /**
   * Returns the instance dedicated to extraction of fields.
   *
   * @return the instance dedicated to extraction of fields.
   */
  public static FieldSupport extraction() {
    return EXTRACTION;
  }

  /**
   * Returns the instance dedicated to comparison of fields.
   *
   * @return the instance dedicated to comparison of fields.
   */
  public static FieldSupport comparison() {
    return COMPARISON;
  }

  /**
   * Build a new {@link FieldSupport}
   *
   * @param allowUsingPrivateFields whether to read private fields or not.
   */
  FieldSupport(boolean allowUsingPrivateFields) {
    this.allowUsingPrivateFields = allowUsingPrivateFields;
  }

  @VisibleForTesting
  public boolean isAllowedToUsePrivateFields() {
    return allowUsingPrivateFields;
  }

  /**
   * Sets whether the use of private fields is allowed.
   * If a method tries to extract/compare private fields and is not allowed to, it will fail with an exception.
   *
   * @param allowUsingPrivateFields allow private fields extraction and comparison. Default {@code true}.
   */
  public void setAllowUsingPrivateFields(boolean allowUsingPrivateFields) {
    this.allowUsingPrivateFields = allowUsingPrivateFields;
  }

  /**
   * Returns a <code>{@link List}</code> containing the values of the given field name, from the elements of the given
   * <code>{@link Iterable}</code>. If the given {@code Iterable} is empty or {@code null}, this method will return an
   * empty {@code List}. This method supports nested fields (e.g. "address.street.number").
   *
   * @param <T> the type of the extracted elements.
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
   * @param <T> the type of the extracted elements.
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
    return stream(target).map(e -> e == null ? null : fieldValue(fieldName, clazz, e))
                         .collect(collectingAndThen(toList(), Collections::unmodifiableList));
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
   * <pre><code class='java'> isNestedField(&quot;address.street&quot;); // true
   * isNestedField(&quot;address.street.name&quot;); // true
   * isNestedField(&quot;person&quot;); // false
   * isNestedField(&quot;.name&quot;); // false
   * isNestedField(&quot;person.&quot;); // false
   * isNestedField(&quot;person.name.&quot;); // false
   * isNestedField(&quot;.person.name&quot;); // false
   * isNestedField(&quot;.&quot;); // false
   * isNestedField(&quot;&quot;); // false</code></pre>
   */
  private boolean isNestedField(String fieldName) {
    return fieldName.contains(SEPARATOR) && !fieldName.startsWith(SEPARATOR) && !fieldName.endsWith(SEPARATOR);
  }

  /**
   * Return the value of field from a target object.
   * <p>
   * Return null if field is nested and one of the nested value is null, ex :
   * <pre><code class='java'> fieldValue(race.name, String.class, frodo); // will return null if frodo.race is null</code></pre>
   *
   * @param <T> the type of the extracted value.
   * @param fieldName the name of the field. It may be a nested field. It is left to the clients to validate for
   *          {@code null} or empty.
   * @param target the given object
   * @param fieldClass type of field
   * @return the value of the given field name
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

  @SuppressWarnings("unchecked")
  private <T> T readSimpleField(String fieldName, Class<T> clazz, Object target) {
    try {
      Object fieldValue = readField(target, fieldName, allowUsingPrivateFields);
      if (clazz.isPrimitive()) {
        switch (clazz.getSimpleName()) {
        case BYTE:
          Byte byteValue = (byte) fieldValue;
          return (T) byteValue;
        case SHORT:
          Short shortValue = (short) fieldValue;
          return (T) shortValue;
        case INT:
          Integer intValue = (int) fieldValue;
          return (T) intValue;
        case LONG:
          Long longValue = (long) fieldValue;
          return (T) longValue;
        case FLOAT:
          Float floatValue = (float) fieldValue;
          return (T) floatValue;
        case DOUBLE:
          Double doubleValue = (double) fieldValue;
          return (T) doubleValue;
        case BOOLEAN:
          Boolean booleanValue = (boolean) fieldValue;
          return (T) booleanValue;
        case CHAR:
          Character charValue = (char) fieldValue;
          return (T) charValue;
        }
      }
      return clazz.cast(fieldValue);
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

  public boolean isAllowedToRead(Field field) {
    if (allowUsingPrivateFields) return true;
    // only read public field
    return isPublic(field.getModifiers());
  }

}
