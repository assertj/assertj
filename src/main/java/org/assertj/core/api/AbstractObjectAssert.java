/**
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 *
 * Copyright 2012-2015 the original author or authors.
 */
package org.assertj.core.api;

import static org.assertj.core.api.StrictAssertions.assertThat;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;

import org.assertj.core.util.introspection.IntrospectionError;

/**
 * Base class for all implementations of assertions for {@link Object}s.
 * 
 * @param <S> the "self" type of this assertion class. Please read &quot;<a href="http://bit.ly/1IZIRcY"
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
   * <p/>
   * It means that if an actual field is not null and the corresponding field in other is null, this field will be
   * ignored in comparison, but the opposite will make assertion fail (null field in actual, not null in other).
   * <p/>
   * Note that comparison is <b>not</b> recursive, if one of the field is an Object, it will be compared to the other
   * field using its <code>equals</code> method.
   * <p/>
   * Private fields are used in comparison but this can be disabled using
   * {@link Assertions#setAllowComparingPrivateFields(boolean)}, if disabled only <b>accessible </b>fields values are
   * compared, accessible fields include directly accessible fields (e.g. public) or fields with an accessible getter.
   * <p/>
   * 
   * Example:
   * <pre><code class='java'> TolkienCharacter frodo = new TolkienCharacter("Frodo", 33, HOBBIT);
   * TolkienCharacter mysteriousHobbit = new TolkienCharacter(null, 33, HOBBIT);
   * 
   * // Null fields in other/expected object are ignored, the mysteriousHobbit has null name thus name is ignored
   * assertThat(frodo).isEqualToIgnoringNullFields(mysteriousHobbit); // OK
   * 
   * // ... but the lenient equality is not reversible !
   * assertThat(mysteriousHobbit).isEqualToIgnoringNullFields(frodo); // FAIL</code></pre>
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
   * Assert that the actual object is equal to given one using a field by field comparison on the given fields only
   * (fields can be inherited fields or nested fields). This can be handy if <code>equals</code> implementation of
   * objects to compare
   * does not suit you.
   * <p/>
   * Note that comparison is <b>not</b> recursive, if one of the field is an Object, it will be compared to the other
   * field using its <code>equals</code> method.
   * <p/>
   * Private fields are used in comparison but this can be disabled using
   * {@link Assertions#setAllowComparingPrivateFields(boolean)}, if disabled only <b>accessible </b>fields values are
   * compared, accessible fields include directly accessible fields (e.g. public) or fields with an accessible getter.
   * <p/>
   * 
   * Example:
   * <pre><code class='java'> TolkienCharacter frodo = new TolkienCharacter(&quot;Frodo&quot;, 33, HOBBIT);
   * TolkienCharacter sam = new TolkienCharacter(&quot;Sam&quot;, 38, HOBBIT);
   * 
   * // frodo and sam both are hobbits, so they are equals when comparing only race
   * assertThat(frodo).isEqualToComparingOnlyGivenFields(sam, &quot;race&quot;); // OK
   * 
   * // they are also equals when comparing only race name (nested field).
   * assertThat(frodo).isEqualToComparingOnlyGivenFields(sam, &quot;race.name&quot;); // OK
   * 
   * // ... but not when comparing both name and race
   * assertThat(frodo).isEqualToComparingOnlyGivenFields(sam, &quot;name&quot;, &quot;race&quot;); // FAIL</code></pre>
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
   * Assert that the actual object is equal to the given one by comparing their fields except for the given ones
   * (inherited fields are taken into account). This can be handy if <code>equals</code> implementation of objects to
   * compare does not suit you.
   * <p/>
   * Note that comparison is <b>not</b> recursive, if one of the field is an Object, it will be compared to the other
   * field using its <code>equals</code> method.
   * <p/>
   * Private fields are used in comparison but this can be disabled using
   * {@link Assertions#setAllowComparingPrivateFields(boolean)}, if disabled only <b>accessible </b>fields values are
   * compared, accessible fields include directly accessible fields (e.g. public) or fields with an accessible getter.
   * <p/>
   * 
   * Example:
   * <pre><code class='java'> TolkienCharacter frodo = new TolkienCharacter("Frodo", 33, HOBBIT);
   * TolkienCharacter sam = new TolkienCharacter("Sam", 38, HOBBIT);
   * 
   * // frodo and sam are equals when ignoring name and age since the only remaining field is race which they share as HOBBIT.
   * assertThat(frodo).isEqualToIgnoringGivenFields(sam, "name", "age"); // OK
   * 
   * // ... but they are not equals if only age is ignored as their names differ.
   * assertThat(frodo).isEqualToIgnoringGivenFields(sam, "age"); // FAIL</code></pre>
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
   * Assert that the actual object is equal to the given object based on a field by field comparison (including
   * inherited fields). This can be handy if <code>equals</code> implementation of objects to compare does not suit you.
   * <p/>
   * Note that comparison is <b>not</b> recursive, if one of the field is an Object, it will be compared to the other
   * field using its <code>equals</code> method.
   * <p/>
   * Private fields are used in comparison but this can be disabled using
   * {@link Assertions#setAllowComparingPrivateFields(boolean)}, if disabled only <b>accessible </b>fields values are
   * compared, accessible fields include directly accessible fields (e.g. public) or fields with an accessible getter.
   * <p/>
   * 
   * Example:
   * <pre><code class='java'> TolkienCharacter frodo = new TolkienCharacter("Frodo", 33, HOBBIT);
   * TolkienCharacter frodoClone = new TolkienCharacter("Frodo", 33, HOBBIT);
   * 
   * // Fail if equals has not been overridden in TolkienCharacter as equals default implementation only compares references
   * assertThat(frodo).isEqualsTo(frodoClone);
   * 
   * // frodo and frodoClone are equals when doing a field by field comparison.
   * assertThat(frodo).isEqualToComparingFieldByField(frodoClone);</code></pre>
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
   * This method will check that {@link Object#equals(Object)} and {@link #hashCode()} follows the
   * rules described in javadoc of {@link Object#equals(Object)} and {@link Object#hashCode()}.
   *
   * @param supplierOfTThatAreEqualsTo provide a set of object that should be equals to the actual
   *          value. The actual value is always checked!
   * @param supplierOfTThatAreNotEqualsTo a set of object that are not equals to the actual value.
   * @return this object for chaining purpose.
   */
  public final S hasValidEqualsAndHashCode(final Supplier<List<? extends A>> supplierOfTThatAreEqualsTo,
                                           final Supplier<List<?>> supplierOfTThatAreNotEqualsTo) {
    // @formatter:off
    this.isNotNull()
        .isEqualTo(actual)
        .isNotEqualTo(null);
    // @formatter:on

    final List<A> equalToList = new ArrayList<>();
    equalToList.add(actual);
    equalToList.addAll(supplierOfTThatAreEqualsTo.get());
    final List<Object> notEqualToList = new ArrayList<>();
    notEqualToList.add(new Object());
    notEqualToList.addAll(supplierOfTThatAreNotEqualsTo.get());

    // the object should not have changed its value, the hashCode should remain the
    // same.
    final AbstractIntegerAssert<?> assertThatActualHashCode = assertThat(actual.hashCode()).as("%s hashCode()", this.info.descriptionText());

    equalToList.forEach(other -> {
      this.isEqualTo(other);
      assertThatActualHashCode.isEqualTo(other.hashCode());
    });

    notEqualToList.forEach(other -> {
      this.isNotEqualTo(other);
    });

    return myself;
  }

  /**
   * This method will check that {@link Object#equals(Object)} and {@link #hashCode()} follows the
   * rules described in javadoc of {@link Object#equals(Object)} and {@link Object#hashCode()}.
   *
   * @param supplierOfTThatAreNotEqualsTo a set of object that are not equals to the actual value.
   * @return this object for chaining purpose.
   * @see #hasValidEqualsAndHashCode(Supplier, Supplier)
   */
  public final S hasValidEqualsAndHashCode(final Supplier<List<?>> supplierOfTThatAreNotEqualsTo) {
    return hasValidEqualsAndHashCode(Collections::emptyList, supplierOfTThatAreNotEqualsTo);
  }

  /**
   * This method will check that {@link Object#equals(Object)} and {@link #hashCode()} follows the
   * rules described in javadoc of {@link Object#equals(Object)} and {@link Object#hashCode()}.
   *
   * @return this object for chaining purpose.
   * @see #hasValidEqualsAndHashCode(Supplier, Supplier)
   */
  public final S hasValidEqualsAndHashCode() {
    return hasValidEqualsAndHashCode(Collections::emptyList, Collections::emptyList);
  }  
}
