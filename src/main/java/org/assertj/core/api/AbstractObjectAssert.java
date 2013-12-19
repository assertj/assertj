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
package org.assertj.core.api;

import org.assertj.core.util.introspection.IntrospectionError;

/**
 * Base class for all implementations of assertions for {@link Object}s.
 * 
 * @param <S> the "self" type of this assertion class. Please read &quot;<a href="http://bit.ly/anMa4g"
 *          target="_blank">Emulating 'self types' using Java Generics to simplify fluent API implementation</a>&quot;
 *          for more details.
 * @param <A> the type of the "actual" value.
 * 
 * @author Yvonne Wang
 * @author Alex Ruiz
 * @author Nicolas François
 * @author Mikhail Mazursky
 * @author Joel Costigliola
 */
public abstract class AbstractObjectAssert<S extends AbstractObjectAssert<S, A>, A> extends AbstractAssert<S, A> {

  protected AbstractObjectAssert(A actual, Class<?> selfType) {
    super(actual, selfType);
  }

  /**
   * Assert that the actual object is equal to the given one by comparing actual's fields with <b>not null</b> other's
   * fields only (including inherited fields).
   * <p>
   * It means that if an actual field is not null and the corresponding field in other is null, this field will be
   * ignored in comparison, but the opposite will make assertion fail (null field in actual, not null in other).
   * 
   * <pre>
   * Example:
   * 
   * TolkienCharacter frodo = new TolkienCharacter("Frodo", 33, HOBBIT);
   * TolkienCharacter mysteriousHobbit = new TolkienCharacter(null, 33, HOBBIT);
   * 
   * // Null fields in other/expected object are ignored, the mysteriousHobbit has null name thus name is ignored
   * assertThat(frodo).isEqualToIgnoringNullFields(mysteriousHobbit); // OK
   * 
   * // ... but the lenient equality is not reversible !
   * assertThat(mysteriousHobbit).isEqualToIgnoringNullFields(frodo); // FAIL
   * 
   * </pre>
   * 
   * @param other the object to compare {@code actual} to.
   * @throws NullPointerException if the actual or other object is {@code null}.
   * @throws AssertionError if the actual and the given object are not lenient equals.
   * @throws AssertionError if the other object is not an instance of the actual type.
   */
  public S isEqualToIgnoringNullFields(A other) {
    objects.assertIsLenientEqualsToIgnoringNullFields(info, actual, other);
    return myself;
  }

  /**
   * @deprecated : use {@link #isEqualToIgnoringNullFields(Object)} instead.
   */
  public S isLenientEqualsToByIgnoringNullFields(A other) {
    objects.assertIsLenientEqualsToIgnoringNullFields(info, actual, other);
    return myself;
  }

  /**
   * Assert that the actual object is equal to given one when doing a field by field comparison on the given fields only
   * (fields can be inherited fields).
   * 
   * <p>
   * Note that only <b>accessible </b>fields values are compared, accessible fields include directly accessible fields
   * (e.g. public) or fields with an accessible getter.
   * </p>
   * 
   * Example:
   * 
   * <pre>
   * TolkienCharacter frodo = new TolkienCharacter(&quot;Frodo&quot;, 33, HOBBIT);
   * TolkienCharacter sam = new TolkienCharacter(&quot;Sam&quot;, 38, HOBBIT);
   * 
   * // frodo and sam both are hobbits, so they are equals when comparing only race
   * assertThat(frodo).isEqualToComparingOnlyGivenFields(sam, &quot;race&quot;); // OK
   * 
   * // ... but not when comparing both name and race
   * assertThat(frodo).isEqualToComparingOnlyGivenFields(sam, &quot;name&quot;, &quot;race&quot;); // FAIL
   * 
   * </pre>
   * 
   * @param other the object to compare {@code actual} to.
   * @param fieldsUsedInComparison accepted fieldsUsedInComparison for lenient equality.
   * @throws NullPointerException if the actual or other is {@code null}.
   * @throws AssertionError if the actual and the given object are not lenient equals.
   * @throws AssertionError if the other object is not an instance of the actual type.
   * @throws IntrospectionError if a field does not exist in actual.
   */
  public S isEqualToComparingOnlyGivenFields(A other, String... fieldsUsedInComparison) {
    objects.assertIsEqualToComparingOnlyGivenFields(info, actual, other, fieldsUsedInComparison);
    return myself;
  }

  /**
   * @deprecated : use {@link #isEqualToComparingOnlyGivenFields(Object, String...)} instead.
   */
  public S isLenientEqualsToByAcceptingFields(A other, String... fields) {
    objects.assertIsEqualToComparingOnlyGivenFields(info, actual, other, fields);
    return myself;
  }

  /**
   * Assert that the actual object is equal to the given one by comparing their fields except for the given ones
   * (inherited fields are taken into account).
   * 
   * <p>
   * Note that only <b>accessible </b>fields values are compared, accessible fields include directly accessible fields
   * (e.g. public) or fields with an accessible getter.
   * </p>
   * 
   * <pre>
   * Example:
   * 
   * TolkienCharacter frodo = new TolkienCharacter("Frodo", 33, HOBBIT);
   * TolkienCharacter sam = new TolkienCharacter("Sam", 38, HOBBIT);
   * 
   * // frodo and sam are equals when ignoring name and age since the only remaining field is race which they share as HOBBIT.
   * assertThat(frodo).isEqualToIgnoringGivenFields(sam, "name", "age"); // OK
   * 
   * // ... but they are not equals if only age is ignored as their names differ.
   * assertThat(frodo).isEqualToIgnoringGivenFields(sam, "age"); // FAIL
   * 
   * </pre>
   * 
   * @param other the object to compare {@code actual} to.
   * @param fieldsToIgnore ignored fieldsToIgnore for lenient equality.
   * @throws NullPointerException if the actual or given object is {@code null}.
   * @throws AssertionError if the actual and the given object are not lenient equals.
   * @throws AssertionError if the other object is not an instance of the actual type.
   */
  public S isEqualToIgnoringGivenFields(A other, String... fieldsToIgnore) {
    objects.assertIsEqualToIgnoringGivenFields(info, actual, other, fieldsToIgnore);
    return myself;
  }

  /**
   * @deprecated : use {@link #isEqualToIgnoringGivenFields(Object, String...)} instead.
   */
  public S isLenientEqualsToByIgnoringFields(A other, String... fields) {
    objects.assertIsEqualToIgnoringGivenFields(info, actual, other, fields);
    return myself;
  }

  /**
   * Assert that the actual object is equal to the given object based on a field by field comparison (including
   * inherited fields).
   * <p>
   * This can be handy if <code>equals</code> implementation of objects to compare does not suit you.
   * </p>
   * <p>
   * Note that only <b>accessible </b>fields values are compared, accessible fields include directly accessible fields
   * (e.g. public) or fields with an accessible getter.
   * </p>
   * 
   * <pre>
   * Example:
   * 
   * TolkienCharacter frodo = new TolkienCharacter("Frodo", 33, HOBBIT);
   * TolkienCharacter frodoClone = new TolkienCharacter("Frodo", 33, HOBBIT);
   * 
   * // Fail if equals has not been overriden in TolkienCharacter as equals default implementation only compares references
   * assertThat(frodo).isEqualsTo(frodoClone);
   * 
   * // frodo and frodoClone are equals when doing a field by field comparison.
   * assertThat(frodo).isEqualToComparingFieldByField(frodoClone);
   * 
   * </pre>
   * 
   * @param other the object to compare {@code actual} to.
   * @throws NullPointerException if the actual or given object is {@code null}.
   * @throws AssertionError if the actual and the given object are not equals field by field.
   * @throws AssertionError if the other object is not an instance of the actual type.
   */
  public S isEqualToComparingFieldByField(A other) {
    objects.assertIsEqualToIgnoringGivenFields(info, actual, other);
    return myself;
  }

  /**
   * @deprecated : use {@link #isEqualToComparingFieldByField(Object)} instead.
   */
  public S isEqualsToByComparingFields(A other) {
    objects.assertIsEqualToIgnoringGivenFields(info, actual, other);
    return myself;
  }
}
