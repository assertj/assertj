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
 * @author Mikhail Mazursky
 */
public class ObjectAssert<T> extends AbstractAssert<ObjectAssert<T>, T> {

  protected ObjectAssert(T actual) {
    super(actual, ObjectAssert.class);
  }

  /**
   * Assert that the actual object is lenient equals to given one by comparing only actual and <b>not null</b> other
   * fields (including inherited fields).
   * <p>
   * It means that if an actual field is not null and the corresponding field in other is null, field will be ignored by
   * lenient comparison, but the inverse will make assertion fail (null field in actual, not null in other).
   * 
   * <pre>
   * Example: 
   * 
   * TolkienCharacter frodo = new TolkienCharacter("Frodo", 33, HOBBIT); 
   * TolkienCharacter mysteriousHobbit = new TolkienCharacter(null, 33, HOBBIT); 
   * 
   * // Null fields in other/expected object are ignored, the mysteriousHobbit has null name thus name is ignored
   * assertThat(frodo).isLenientEqualsToByIgnoringNullFields(mysteriousHobbit); //=> OK
   * 
   * // ... but the lenient equality is not reversible !
   * assertThat(mysteriousHobbit).isLenientEqualsToByIgnoringNullFields(frodo); //=> FAIL
   * 
   * </pre>
   * 
   * @param other the object to compare {@code actual} to.
   * @throws NullPointerException if the actual type is {@code null}.
   * @throws NullPointerException if the other type is {@code null}.
   * @throws AssertionError if the actual and the given object are not lenient equals.
   * @throws AssertionError if the other object is not an instance of the actual type.
   */
  public ObjectAssert<T> isLenientEqualsToByIgnoringNullFields(T other) {
    objects.assertIsLenientEqualsToByIgnoringNullFields(info, actual, other);
    return this;
  }

  /**
   * Assert that the actual object is lenient equals to given one by only comparing actual and other on the given
   * "accepted" fields only ("accepted" fields can be inherited fields).
   * <p>
   * Example:
   * 
   * <pre>
   * TolkienCharacter frodo = new TolkienCharacter(&quot;Frodo&quot;, 33, HOBBIT);
   * TolkienCharacter sam = new TolkienCharacter(&quot;Sam&quot;, 38, HOBBIT);
   * 
   * // frodo and sam both are hobbits, so they are lenient equals on race
   * assertThat(frodo).isLenientEqualsToByAcceptingFields(sam, &quot;race&quot;); // =&gt; OK
   * 
   * // ... but not when accepting name and race
   * assertThat(frodo).isLenientEqualsToByAcceptingFields(sam, &quot;name&quot;, &quot;race&quot;); // =&gt; FAIL
   * 
   * </pre>
   * 
   * @param other the object to compare {@code actual} to.
   * @param fields accepted fields for lenient equality.
   * @throws NullPointerException if the actual type is {@code null}.
   * @throws NullPointerException if the other type is {@code null}.
   * @throws AssertionError if the actual and the given object are not lenient equals.
   * @throws AssertionError if the other object is not an instance of the actual type.
   * @throws IntrospectionError if a field does not exist in actual.
   */
  public ObjectAssert<T> isLenientEqualsToByAcceptingFields(T other, String... fields) {
    objects.assertIsLenientEqualsToByAcceptingFields(info, actual, other, fields);
    return this;
  }

  /**
   * Assert that the actual object is lenient equals to given one by comparing actual and other fields (including
   * inherited fields) except the given "ignored" fields.
   * 
   * <pre>
   * Example: 
   * 
   * TolkienCharacter frodo = new TolkienCharacter("Frodo", 33, HOBBIT); 
   * TolkienCharacter sam = new TolkienCharacter("Sam", 38, HOBBIT); 
   * 
   * // frodo and sam both are lenient equals ignoring name and age since only remaining property is race and frodo and sam both are HOBBIT
   * assertThat(frodo).isLenientEqualsToByIgnoringFields(sam, "name", "age"); //=> OK
   * 
   * // ... but they are not lenient equals if only age is ignored because their names differ.
   * assertThat(frodo).isLenientEqualsToByIgnoringFields(sam, "age"); //=> FAIL
   * 
   * </pre>
   * 
   * @param other the object to compare {@code actual} to.
   * @param fields ignored fields for lenient equality.
   * @throws NullPointerException if the actual type is {@code null}.
   * @throws NullPointerException if the other type is {@code null}.
   * @throws AssertionError if the actual and the given object are not lenient equals.
   * @throws AssertionError if the other object is not an instance of the actual type.
   */
  public ObjectAssert<T> isLenientEqualsToByIgnoringFields(T other, String... fields) {
    objects.assertIsLenientEqualsToByIgnoringFields(info, actual, other, fields);
    return this;
  }

  /**
   * Assert that the actual object is equals fields by fields to another object, inherited fields are taken into
   * account.
   * <p>
   * This can be handy if <code>equals</code> implementation of objects to compare does not suit you.
   * 
   * <pre>
   * Example: 
   * 
   * TolkienCharacter frodo = new TolkienCharacter("Frodo", 33, HOBBIT); 
   * TolkienCharacter frodoClone = new TolkienCharacter("Frodo", 33, HOBBIT); 
   * 
   * // Fail if equals has not been overriden in TolkienCharacter as equals default implementation only compares references 
   * assertThat(frodo).isEqualsTo(frodoClone); // Fail if equals has not been overriden in TolkienCharacter 
   * 
   * // frodo and frodoClone are equals by comparing fields
   * assertThat(frodo).isEqualsToByComparingFields(frodoClone); // OK
   * 
   * </pre>
   * 
   * @param other the object to compare {@code actual} to.
   * @throws NullPointerException if the actual type is {@code null}.
   * @throws NullPointerException if the other type is {@code null}.
   * @throws AssertionError if the actual and the given object are not equals fields by fields.
   * @throws AssertionError if the other object is not an instance of the actual type.
   */
  public ObjectAssert<T> isEqualsToByComparingFields(T other) {
    objects.assertIsLenientEqualsToByIgnoringFields(info, actual, other);
    return this;
  }

}
