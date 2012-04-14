/*
 * Created on Dec 26, 2010
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
package org.fest.assertions.api;

import org.fest.util.IntrospectionError;

/**
 * Assertion methods for {@code Object}s.
 * <p>
 * To create a new instance of this class, invoke <code>{@link Assertions#assertThat(Object)}</code>.
 * </p>
 *
 * @author Yvonne Wang
 * @author Alex Ruiz
 * @author Nicolas François
 */
public class ObjectAssert extends AbstractAssert<ObjectAssert, Object> {

  protected ObjectAssert(Object actual) {
    super(actual, ObjectAssert.class);
  }

  /**
   * Verifies that the actual {@code Object} is an instance of the given type.
   * @param type the type to check the actual {@code Object} against.
   * @return this assertion object.
   * @throws NullPointerException if the given type is {@code null}.
   * @throws AssertionError if the actual {@code Object} is {@code null}.
   * @throws AssertionError if the actual {@code Object} is not an instance of the given type.
   */
  public ObjectAssert isInstanceOf(Class<?> type) {
    objects.assertIsInstanceOf(info, actual, type);
    return this;
  }

  /**
   * Verifies that the actual {@code Object} is an instance of any of the given types.
   * @param types the types to check the actual {@code Object} against.
   * @return this assertion object.
   * @throws AssertionError if the actual {@code Object} is {@code null}.
   * @throws AssertionError if the actual {@code Object} is not an instance of any of the given types.
   * @throws NullPointerException if the given array of types is {@code null}.
   * @throws NullPointerException if the given array of types contains {@code null}s.
   */
  public ObjectAssert isInstanceOfAny(Class<?>...types) {
    objects.assertIsInstanceOfAny(info, actual, types);
    return this;
  }
  
  /**
   * Assert that the given object is lenient equals by ignoring null fields value on other object.
   * @param other the object to compare {@code actual} to.
   * @throws NullPointerException if the actual type is {@code null}.
   * @throws NullPointerException if the other type is {@code null}.
   * @throws AssertionError if the actual and the given object are not lenient equals.
   * @throws AssertionError if the other object is not an instance of the actual type.
   */  
  public ObjectAssert isLenientEqualsToByIgnoringNullFields(Object other){
	  objects.assertIsLenientEqualsToByIgnoringNullFields(info, actual, other);
	  return this;
  }
  
  /**
   * Assert that the given object is lenient equals by accepting fields.
   * @param other the object to compare {@code actual} to.
   * @param fields accepted fields for lenient equality.
   * @throws NullPointerException if the actual type is {@code null}.
   * @throws NullPointerException if the other type is {@code null}.
   * @throws AssertionError if the actual and the given object are not lenient equals.
   * @throws AssertionError if the other object is not an instance of the actual type.
   * @throws IntrospectionError if a field does not exist in actual.
   */  
  public ObjectAssert isLenientEqualsToByAcceptingFields(Object other, String... fields){
	  objects.assertIsLenientEqualsToByAcceptingFields(info, actual, other, fields);
	  return this;
  }
  
  /**
   * Assert that the given object is lenient equals by ignoring fields.
   * @param other the object to compare {@code actual} to.
   * @param fields ignored fields for lenient equality.
   * @throws NullPointerException if the actual type is {@code null}.
   * @throws NullPointerException if the other type is {@code null}.
   * @throws AssertionError if the actual and the given object are not lenient equals.
   * @throws AssertionError if the other object is not an instance of the actual type.
   */  
  public ObjectAssert isLenientEqualsToByIgnoringFields(Object other, String... fields){
	  objects.assertIsLenientEqualsToByIgnoringFields(info, actual, other, fields);
	  return this;
  }  
  
}
