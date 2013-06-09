/*
 * Created on Jul 26, 2010
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

import java.util.Comparator;

import org.assertj.core.data.Index;
import org.assertj.core.groups.FieldsOrPropertiesExtractor;
import org.assertj.core.groups.Tuple;
import org.assertj.core.internal.ComparatorBasedComparisonStrategy;
import org.assertj.core.internal.ObjectArrays;
import org.assertj.core.util.VisibleForTesting;
import org.assertj.core.util.introspection.IntrospectionError;

/**
 * Assertion methods for arrays of objects.
 * <p>
 * To create an instance of this class, invoke <code>{@link Assertions#assertThat(T[])}</code>.
 * </p>
 * 
 * @param <T> the type of elements of the "actual" value.
 * 
 * @author Yvonne Wang
 * @author Alex Ruiz
 * @author Joel Costigliola
 * @author Nicolas François
 * @author Mikhail Mazursky
 */
public class ObjectArrayAssert<T> extends AbstractAssert<ObjectArrayAssert<T>, T[]> implements
    IndexedObjectEnumerableAssert<ObjectArrayAssert<T>, T>, ArraySortedAssert<ObjectArrayAssert<T>, T> {

  @VisibleForTesting
  ObjectArrays arrays = ObjectArrays.instance();

  protected ObjectArrayAssert(T[] actual) {
    super(actual, ObjectArrayAssert.class);
  }

  /** {@inheritDoc} */
  @Override
  public void isNullOrEmpty() {
    arrays.assertNullOrEmpty(info, actual);
  }

  /** {@inheritDoc} */
  @Override
  public void isEmpty() {
    arrays.assertEmpty(info, actual);
  }

  /** {@inheritDoc} */
  @Override
  public ObjectArrayAssert<T> isNotEmpty() {
    arrays.assertNotEmpty(info, actual);
    return this;
  }

  /** {@inheritDoc} */
  @Override
  public ObjectArrayAssert<T> hasSize(int expected) {
    arrays.assertHasSize(info, actual, expected);
    return this;
  }

  /** {@inheritDoc} */
  @Override
  public ObjectArrayAssert<T> hasSameSizeAs(Object[] other) {
    arrays.assertHasSameSizeAs(info, actual, other);
    return this;
  }

  /** {@inheritDoc} */
  @Override
  public ObjectArrayAssert<T> hasSameSizeAs(Iterable<?> other) {
    arrays.assertHasSameSizeAs(info, actual, other);
    return this;
  }

  /** {@inheritDoc} */
  @Override
  public ObjectArrayAssert<T> contains(T... values) {
    arrays.assertContains(info, actual, values);
    return this;
  }

  /** {@inheritDoc} */
  @Override
  public ObjectArrayAssert<T> containsOnly(T... values) {
    arrays.assertContainsOnly(info, actual, values);
    return this;
  }

  /** {@inheritDoc} */
  @Override
  public ObjectArrayAssert<T> containsOnlyOnce(T... values) {
    arrays.assertContainsOnlyOnce(info, actual, values);
    return this;
  }

  /** {@inheritDoc} */
  @Override
  public ObjectArrayAssert<T> containsExactly(T... values) {
    objects.assertEqual(info, actual, values);
    return this;
  };

  /** {@inheritDoc} */
  @Override
  public ObjectArrayAssert<T> containsSequence(T... sequence) {
    arrays.assertContainsSequence(info, actual, sequence);
    return this;
  }

  /** {@inheritDoc} */
  @Override
  public ObjectArrayAssert<T> contains(T value, Index index) {
    arrays.assertContains(info, actual, value, index);
    return this;
  }

  /** {@inheritDoc} */
  @Override
  public ObjectArrayAssert<T> doesNotContain(T value, Index index) {
    arrays.assertDoesNotContain(info, actual, value, index);
    return this;
  }

  /** {@inheritDoc} */
  @Override
  public ObjectArrayAssert<T> doesNotContain(T... values) {
    arrays.assertDoesNotContain(info, actual, values);
    return this;
  }

  /** {@inheritDoc} */
  @Override
  public ObjectArrayAssert<T> doesNotHaveDuplicates() {
    arrays.assertDoesNotHaveDuplicates(info, actual);
    return this;
  }

  /** {@inheritDoc} */
  @Override
  public ObjectArrayAssert<T> startsWith(T... sequence) {
    arrays.assertStartsWith(info, actual, sequence);
    return this;
  }

  /** {@inheritDoc} */
  @Override
  public ObjectArrayAssert<T> endsWith(T... sequence) {
    arrays.assertEndsWith(info, actual, sequence);
    return this;
  }

  /** {@inheritDoc} */
  @Override
  public ObjectArrayAssert<T> containsNull() {
    arrays.assertContainsNull(info, actual);
    return this;
  }

  /** {@inheritDoc} */
  @Override
  public ObjectArrayAssert<T> doesNotContainNull() {
    arrays.assertDoesNotContainNull(info, actual);
    return this;
  }

  /** {@inheritDoc} */
  @Override
  public ObjectArrayAssert<T> are(Condition<? super T> condition) {
    arrays.assertAre(info, actual, condition);
    return myself;
  }

  /** {@inheritDoc} */
  @Override
  public ObjectArrayAssert<T> areNot(Condition<? super T> condition) {
    arrays.assertAreNot(info, actual, condition);
    return myself;
  }

  /** {@inheritDoc} */
  @Override
  public ObjectArrayAssert<T> have(Condition<? super T> condition) {
    arrays.assertHave(info, actual, condition);
    return myself;
  }

  /** {@inheritDoc} */
  @Override
  public ObjectArrayAssert<T> doNotHave(Condition<? super T> condition) {
    arrays.assertDoNotHave(info, actual, condition);
    return myself;
  }

  /** {@inheritDoc} */
  @Override
  public ObjectArrayAssert<T> areAtLeast(int times, Condition<? super T> condition) {
    arrays.assertAreAtLeast(info, actual, times, condition);
    return myself;
  }

  /** {@inheritDoc} */
  @Override
  public ObjectArrayAssert<T> areNotAtLeast(int times, Condition<? super T> condition) {
    arrays.assertAreNotAtLeast(info, actual, times, condition);
    return myself;
  }

  /** {@inheritDoc} */
  @Override
  public ObjectArrayAssert<T> areAtMost(int times, Condition<? super T> condition) {
    arrays.assertAreAtMost(info, actual, times, condition);
    return myself;
  }

  /** {@inheritDoc} */
  @Override
  public ObjectArrayAssert<T> areNotAtMost(int times, Condition<? super T> condition) {
    arrays.assertAreNotAtMost(info, actual, times, condition);
    return myself;
  }

  /** {@inheritDoc} */
  @Override
  public ObjectArrayAssert<T> areExactly(int times, Condition<? super T> condition) {
    arrays.assertAreExactly(info, actual, times, condition);
    return myself;
  }

  /** {@inheritDoc} */
  @Override
  public ObjectArrayAssert<T> areNotExactly(int times, Condition<? super T> condition) {
    arrays.assertAreNotExactly(info, actual, times, condition);
    return myself;
  }

  /** {@inheritDoc} */
  @Override
  public ObjectArrayAssert<T> haveAtLeast(int times, Condition<? super T> condition) {
    arrays.assertHaveAtLeast(info, actual, times, condition);
    return myself;
  }

  /** {@inheritDoc} */
  @Override
  public ObjectArrayAssert<T> doNotHaveAtLeast(int times, Condition<? super T> condition) {
    arrays.assertDoNotHaveAtLeast(info, actual, times, condition);
    return myself;
  }

  /** {@inheritDoc} */
  @Override
  public ObjectArrayAssert<T> haveAtMost(int times, Condition<? super T> condition) {
    arrays.assertHaveAtMost(info, actual, times, condition);
    return myself;
  }

  /** {@inheritDoc} */
  @Override
  public ObjectArrayAssert<T> doNotHaveAtMost(int times, Condition<? super T> condition) {
    arrays.assertDoNotHaveAtMost(info, actual, times, condition);
    return myself;
  }

  /** {@inheritDoc} */
  @Override
  public ObjectArrayAssert<T> haveExactly(int times, Condition<? super T> condition) {
    arrays.assertHaveExactly(info, actual, times, condition);
    return myself;
  }

  /** {@inheritDoc} */
  @Override
  public ObjectArrayAssert<T> doNotHaveExactly(int times, Condition<? super T> condition) {
    arrays.assertDoNotHaveExactly(info, actual, times, condition);
    return myself;
  }

  /** {@inheritDoc} */
  @Override
  public ObjectArrayAssert<T> isSorted() {
    arrays.assertIsSorted(info, actual);
    return this;
  }

  /** {@inheritDoc} */
  @Override
  public ObjectArrayAssert<T> isSortedAccordingTo(Comparator<? super T> comparator) {
    arrays.assertIsSortedAccordingToComparator(info, actual, comparator);
    return this;
  }

  /** {@inheritDoc} */
  @Override
  public ObjectArrayAssert<T> containsAll(Iterable<? extends T> iterable) {
    arrays.assertContainsAll(info, actual, iterable);
    return this;
  }

  @Override
  public ObjectArrayAssert<T> usingElementComparator(Comparator<? super T> customComparator) {
    this.arrays = new ObjectArrays(new ComparatorBasedComparisonStrategy(customComparator));
    return myself;
  }

  @Override
  public ObjectArrayAssert<T> usingDefaultElementComparator() {
    this.arrays = ObjectArrays.instance();
    return myself;
  }

  /**
   * Extract the values of given field or property from the array's elements under test into a new array, this new array
   * becoming the array under test.
   * <p>
   * It allows you to test a field/property of the array's elements instead of testing the elements themselves, it can
   * be sometimes much less work !
   * <p>
   * Let's take an example to make things clearer :
   * 
   * <pre>
   * // Build a array of TolkienCharacter, a TolkienCharacter has a name (String) and a Race (a class)
   * // they can be public field or properties, both works when extracting their values.
   * TolkienCharacter[] fellowshipOfTheRing = new TolkienCharacter[] {
   *   new TolkienCharacter(&quot;Frodo&quot;, 33, HOBBIT),
   *   new TolkienCharacter(&quot;Sam&quot;, 38, HOBBIT),
   *   new TolkienCharacter(&quot;Gandalf&quot;, 2020, MAIA),
   *   new TolkienCharacter(&quot;Legolas&quot;, 1000, ELF),
   *   new TolkienCharacter(&quot;Pippin&quot;, 28, HOBBIT),
   *   new TolkienCharacter(&quot;Gimli&quot;, 139, DWARF),
   *   new TolkienCharacter(&quot;Aragorn&quot;, 87, MAN,
   *   new TolkienCharacter(&quot;Boromir&quot;, 37, MAN)
   * };
   * 
   * // let's verify the names of TolkienCharacter in fellowshipOfTheRing :
   * 
   * assertThat(fellowshipOfTheRing).extracting(&quot;name&quot;)
   *           .contains(&quot;Boromir&quot;, &quot;Gandalf&quot;, &quot;Frodo&quot;)
   *           .doesNotContain(&quot;Sauron&quot;, &quot;Elrond&quot;);
   *         
   * // you can also extract nested field/property like the name of Race :
   * 
   * assertThat(fellowshipOfTheRing).extracting(&quot;race.name&quot;)
   *           .contains(&quot;Hobbit&quot;, &quot;Elf&quot;)
   *           .doesNotContain(&quot;Orc&quot;);
   * </pre>
   * 
   * A field with the given name is looked for first, if it is not accessible (ie. does not exist or is not public) then
   * a property with the given name is looked for.
   * <p>
   * It works only if all objects have the field or all objects have the property with the given name, i.e. it won't
   * work if half of the objects have the field and the other the property.
   * <p>
   * Note that the order of extracted field/property values is consistent with the array order.
   * 
   * @param fieldOrProperty the field/property to extract from the array under test
   * @return a new assertion object whose object under test is the array of extracted field/property values.
   * @throws IntrospectionError if no field or property exists with the given name (or field exists but is not public)
   */
  public ObjectArrayAssert<Object> extracting(String fieldOrProperty) {
    Object[] values = FieldsOrPropertiesExtractor.extract(fieldOrProperty, actual);
    return new ObjectArrayAssert<Object>(values);
  }

  /**
   * Extract the values of given field or property from the array's elements under test into a new array, this new array
   * becoming the array under test with type.
   * <p>
   * It allows you to test a field/property of the array's elements instead of testing the elements themselves, it can
   * be sometimes much less work !
   * <p>
   * Let's take an example to make things clearer :
   * 
   * <pre>
   * // Build a array of TolkienCharacter, a TolkienCharacter has a name (String) and a Race (a class)
   * // they can be public field or properties, both works when extracting their values.
   * TolkienCharacter[] fellowshipOfTheRing = new TolkienCharacter[] {
   *   new TolkienCharacter(&quot;Frodo&quot;, 33, HOBBIT),
   *   new TolkienCharacter(&quot;Sam&quot;, 38, HOBBIT),
   *   new TolkienCharacter(&quot;Gandalf&quot;, 2020, MAIA),
   *   new TolkienCharacter(&quot;Legolas&quot;, 1000, ELF),
   *   new TolkienCharacter(&quot;Pippin&quot;, 28, HOBBIT),
   *   new TolkienCharacter(&quot;Gimli&quot;, 139, DWARF),
   *   new TolkienCharacter(&quot;Aragorn&quot;, 87, MAN,
   *   new TolkienCharacter(&quot;Boromir&quot;, 37, MAN)
   * };
   * 
   * // let's verify the names of TolkienCharacter in fellowshipOfTheRing :
   * 
   * assertThat(fellowshipOfTheRing).extracting(&quot;name&quot;, String.class)
   *           .contains(&quot;Boromir&quot;, &quot;Gandalf&quot;, &quot;Frodo&quot;)
   *           .doesNotContain(&quot;Sauron&quot;, &quot;Elrond&quot;);
   * 
   * // you can also extract nested field/property like the name of Race :
   * 
   * assertThat(fellowshipOfTheRing).extracting(&quot;race.name&quot;, String.class)
   *           .contains(&quot;Hobbit&quot;, &quot;Elf&quot;)
   *           .doesNotContain(&quot;Orc&quot;);
   * </pre>
   * 
   * A field with the given name is looked for first, if it is not accessible (ie. does not exist or is not public) then
   * a property with the given name is looked for.
   * <p>
   * It works only if all objects have the field or all objects have the property with the given name, i.e. it won't
   * work if half of the objects have the field and the other the property.
   * <p>
   * Note that the order of extracted field/property values is consistent with the order of the array under test.
   * 
   * @param fieldOrProperty the field/property to extract from the array under test
   * @param extractingType type to return
   * @return a new assertion object whose object under test is the array of extracted field/property values.
   * @throws IntrospectionError if no field or property exists with the given name (or field exists but is not public)
   */
  public <P> ObjectArrayAssert<P> extracting(String fieldOrProperty, Class<P> extractingType) {
    @SuppressWarnings("unchecked")
    P[] values = (P[]) FieldsOrPropertiesExtractor.extract(fieldOrProperty, actual);
    return new ObjectArrayAssert<P>(values);
  }

  // TODO : write javadoc !
  public ObjectArrayAssert<Tuple> extracting(String... fieldsOrProperties) {
    Tuple[] values = FieldsOrPropertiesExtractor.extract(actual, fieldsOrProperties);
    return new ObjectArrayAssert<Tuple>(values);
  }

}
