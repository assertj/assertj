/*
 * Created on Nov 18, 2010
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

import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;

import org.assertj.core.groups.FieldsOrPropertiesExtractor;
import org.assertj.core.groups.Tuple;
import org.assertj.core.internal.ComparatorBasedComparisonStrategy;
import org.assertj.core.internal.Iterables;
import org.assertj.core.util.VisibleForTesting;
import org.assertj.core.util.introspection.IntrospectionError;

/**
 * Base class for implementations of <code>{@link ObjectEnumerableAssert}</code> whose actual value type is
 * <code>{@link Collection}</code>.
 * 
 * @param <S> the "self" type of this assertion class. Please read &quot;<a href="http://bit.ly/anMa4g"
 *          target="_blank">Emulating 'self types' using Java Generics to simplify fluent API implementation</a>&quot;
 *          for more details.
 * @param <A> the type of the "actual" value.
 * @param <T> the type of elements of the "actual" value.
 * 
 * @author Yvonne Wang
 * @author Alex Ruiz
 * @author Mathieu Baechler
 * @author Joel Costigliola
 * @author Maciej Jaskowski
 * @author Nicolas François
 * @author Mikhail Mazursky
 */
public abstract class AbstractIterableAssert<S extends AbstractIterableAssert<S, A, T>, A extends Iterable<T>, T>
    extends AbstractAssert<S, A> implements ObjectEnumerableAssert<S, T> {

  @VisibleForTesting
  Iterables iterables = Iterables.instance();

  protected AbstractIterableAssert(A actual, Class<?> selfType) {
    super(actual, selfType);
  }

  /** {@inheritDoc} */
  public void isNullOrEmpty() {
    iterables.assertNullOrEmpty(info, actual);
  }

  /** {@inheritDoc} */
  public void isEmpty() {
    iterables.assertEmpty(info, actual);
  }

  /** {@inheritDoc} */
  public S isNotEmpty() {
    iterables.assertNotEmpty(info, actual);
    return myself;
  }

  /** {@inheritDoc} */
  public S hasSize(int expected) {
    iterables.assertHasSize(info, actual, expected);
    return myself;
  }

  /** {@inheritDoc} */
  public S hasSameSizeAs(Object[] other) {
    iterables.assertHasSameSizeAs(info, actual, other);
    return myself;
  }

  /** {@inheritDoc} */
  public S hasSameSizeAs(Iterable<?> other) {
    iterables.assertHasSameSizeAs(info, actual, other);
    return myself;
  }

  /** {@inheritDoc} */
  public S contains(T... values) {
    iterables.assertContains(info, actual, values);
    return myself;
  }

  /** {@inheritDoc} */
  public S containsOnly(T... values) {
    iterables.assertContainsOnly(info, actual, values);
    return myself;
  }

  /** {@inheritDoc} */
  public S containsExactly(T... values) {
    iterables.assertContainsExactly(info, actual, values);
    return myself;
  }

  /**
   * Verifies that all the elements of the actual {@code Iterable} are present in the given {@code Iterable}.
   * 
   * @param values the {@code Iterable} that should contain all actual elements.
   * @return this assertion object.
   * @throws AssertionError if the actual {@code Iterable} is {@code null}.
   * @throws NullPointerException if the given {@code Iterable} is {@code null}.
   * @throws AssertionError if the actual {@code Iterable} is not subset of set {@code Iterable}.
   */
  public S isSubsetOf(Iterable<? extends T> values) {
    iterables.assertIsSubsetOf(info, actual, values);
    return myself;
  }

  /** {@inheritDoc} */
  public S containsSequence(T... sequence) {
    iterables.assertContainsSequence(info, actual, sequence);
    return myself;
  }

  public S doesNotContain(T... values) {
    iterables.assertDoesNotContain(info, actual, values);
    return myself;
  }

  /**
   * Verifies that the actual group does not contain any elements of the given {@link Iterable} (i.e. none).
   * 
   * <pre>
   * // this assertion succeeds :
   * List&lt;String&gt; actual = newArrayList(&quot;GIT&quot;, &quot;CVS&quot;, &quot;SOURCESAFE&quot;);
   * List&lt;String&gt; values = newArrayList(&quot;git&quot;, &quot;cvs&quot;, &quot;subversion&quot;);
   * assertThat(actual).doesNotContainAnyElementsOf(values);
   * 
   * // This one fails : 
   * List&lt;String&gt; actual = newArrayList(&quot;GIT&quot;, &quot;cvs&quot;, &quot;SOURCESAFE&quot;);
   * List&lt;String&gt; values = newArrayList(&quot;git&quot;, &quot;cvs&quot;, &quot;subversion&quot;);
   * assertThat(actual).doesNotContainAnyElementsOf(values);
   * </pre>
   * 
   * @param iterable the given {@link Iterable}
   * @return {@code this} assertion object.
   * @throws NullPointerException if the given argument is {@code null}.
   * @throws IllegalArgumentException if the given argument is an empty iterable.
   * @throws AssertionError if the actual group is {@code null}.
   * @throws AssertionError if the actual group contains some elements of the given {@link Iterable}.
   */
  public S doesNotContainAnyElementsOf(Iterable<? extends T> iterable) {
    iterables.assertDoesNotContainAnyElementsOf(info, actual, iterable);
    return myself;
  }

  /** {@inheritDoc} */
  public S doesNotHaveDuplicates() {
    iterables.assertDoesNotHaveDuplicates(info, actual);
    return myself;
  }

  /** {@inheritDoc} */
  public S startsWith(T... sequence) {
    iterables.assertStartsWith(info, actual, sequence);
    return myself;
  }

  /** {@inheritDoc} */
  public S endsWith(T... sequence) {
    iterables.assertEndsWith(info, actual, sequence);
    return myself;
  }

  /** {@inheritDoc} */
  public S containsNull() {
    iterables.assertContainsNull(info, actual);
    return myself;
  }

  /** {@inheritDoc} */
  public S doesNotContainNull() {
    iterables.assertDoesNotContainNull(info, actual);
    return myself;
  }

  /** {@inheritDoc} */
  public S are(Condition<? super T> condition) {
    iterables.assertAre(info, actual, condition);
    return myself;
  }

  /** {@inheritDoc} */
  public S areNot(Condition<? super T> condition) {
    iterables.assertAreNot(info, actual, condition);
    return myself;
  }

  /** {@inheritDoc} */
  public S have(Condition<? super T> condition) {
    iterables.assertHave(info, actual, condition);
    return myself;
  }

  /** {@inheritDoc} */
  public S doNotHave(Condition<? super T> condition) {
    iterables.assertDoNotHave(info, actual, condition);
    return myself;
  }

  /** {@inheritDoc} */
  public S areAtLeast(int times, Condition<? super T> condition) {
    iterables.assertAreAtLeast(info, actual, times, condition);
    return myself;
  }

  /** {@inheritDoc} */
  public S areNotAtLeast(int times, Condition<? super T> condition) {
    iterables.assertAreNotAtLeast(info, actual, times, condition);
    return myself;
  }

  /** {@inheritDoc} */
  public S areAtMost(int times, Condition<? super T> condition) {
    iterables.assertAreAtMost(info, actual, times, condition);
    return myself;
  }

  /** {@inheritDoc} */
  public S areNotAtMost(int times, Condition<? super T> condition) {
    iterables.assertAreNotAtMost(info, actual, times, condition);
    return myself;
  }

  /** {@inheritDoc} */
  public S areExactly(int times, Condition<? super T> condition) {
    iterables.assertAreExactly(info, actual, times, condition);
    return myself;
  }

  /** {@inheritDoc} */
  public S areNotExactly(int times, Condition<? super T> condition) {
    iterables.assertAreNotExactly(info, actual, times, condition);
    return myself;
  }

  /** {@inheritDoc} */
  public S haveAtLeast(int times, Condition<? super T> condition) {
    iterables.assertHaveAtLeast(info, actual, times, condition);
    return myself;
  }

  /** {@inheritDoc} */
  public S doNotHaveAtLeast(int times, Condition<? super T> condition) {
    iterables.assertDoNotHaveAtLeast(info, actual, times, condition);
    return myself;
  }

  /** {@inheritDoc} */
  public S haveAtMost(int times, Condition<? super T> condition) {
    iterables.assertHaveAtMost(info, actual, times, condition);
    return myself;
  }

  /** {@inheritDoc} */
  public S doNotHaveAtMost(int times, Condition<? super T> condition) {
    iterables.assertDoNotHaveAtMost(info, actual, times, condition);
    return myself;
  }

  /** {@inheritDoc} */
  public S haveExactly(int times, Condition<? super T> condition) {
    iterables.assertHaveExactly(info, actual, times, condition);
    return myself;
  }

  /** {@inheritDoc} */
  public S doNotHaveExactly(int times, Condition<? super T> condition) {
    iterables.assertDoNotHaveExactly(info, actual, times, condition);
    return myself;
  }

  /** {@inheritDoc} */
  public S containsAll(Iterable<? extends T> iterable) {
    iterables.assertContainsAll(info, actual, iterable);
    return myself;
  }

  /** {@inheritDoc} */
  public S usingElementComparator(Comparator<? super T> customComparator) {
    this.iterables = new Iterables(new ComparatorBasedComparisonStrategy(customComparator));
    return myself;
  }

  /** {@inheritDoc} */
  public S usingDefaultElementComparator() {
    this.iterables = Iterables.instance();
    return myself;
  }

  /**
   * Extract the values of given field or property from the Iterable's elements under test into a new Iterable, this new
   * Iterable becoming the Iterable under test.
   * <p>
   * It allows you to test a property/field of the the Iterable's elements instead of testing the elements themselves,
   * it can be sometimes much less work !
   * <p>
   * Let's take an example to make things clearer :
   * 
   * <pre>
   * // Build a list of TolkienCharacter, a TolkienCharacter has a name, and age and a Race (a specific class)
   * // they can be public field or properties, both can be extracted.
   * List&lt;TolkienCharacter&gt; fellowshipOfTheRing = new ArrayList&lt;TolkienCharacter&gt;();
   * 
   * fellowshipOfTheRing.add(new TolkienCharacter(&quot;Frodo&quot;, 33, HOBBIT));
   * fellowshipOfTheRing.add(new TolkienCharacter(&quot;Sam&quot;, 38, HOBBIT));
   * fellowshipOfTheRing.add(new TolkienCharacter(&quot;Gandalf&quot;, 2020, MAIA));
   * fellowshipOfTheRing.add(new TolkienCharacter(&quot;Legolas&quot;, 1000, ELF));
   * fellowshipOfTheRing.add(new TolkienCharacter(&quot;Pippin&quot;, 28, HOBBIT));
   * fellowshipOfTheRing.add(new TolkienCharacter(&quot;Gimli&quot;, 139, DWARF));
   * fellowshipOfTheRing.add(new TolkienCharacter(&quot;Aragorn&quot;, 87, MAN);
   * fellowshipOfTheRing.add(new TolkienCharacter(&quot;Boromir&quot;, 37, MAN));
   * 
   * // let's verify the names of TolkienCharacter in fellowshipOfTheRing :
   * 
   * assertThat(fellowshipOfTheRing).extracting(&quot;name&quot;)
   *           .contains(&quot;Boromir&quot;, &quot;Gandalf&quot;, &quot;Frodo&quot;)
   *           .doesNotContain(&quot;Sauron&quot;, &quot;Elrond&quot;);
   *         
   * // you can extract nested property/field like the name of Race :
   * 
   * assertThat(fellowshipOfTheRing).extracting(&quot;race.name&quot;)
   *           .contains(&quot;Hobbit&quot;, &quot;Elf&quot;)
   *           .doesNotContain(&quot;Orc&quot;);
   * </pre>
   * 
   * A property with the given name is looked for first, if it does'nt exist then a field with the given name is looked
   * for, if no field accessible (ie. does not exist or is not public) an IntrospectionError is thrown.
   * <p>
   * It only works if <b>all</b> objects have the field or all objects have the property with the given name, i.e. it
   * won't work if half of the objects have the field and the other the property.
   * <p>
   * Note that the order of extracted property/field values is consistent with the iteration order of the Iterable under
   * test, for example if it's a {@link HashSet}, you won't be able to make any assumptions of the extracted values
   * order.
   * 
   * @param propertyOrField the property/field to extract from the Iterable under test
   * @return a new assertion object whose object under test is the list of extracted property/field values.
   * @throws IntrospectionError if no field or property exists with the given name (or field exists but is not public)
   *           in one of the initial Iterable's element.
   */
  public ListAssert<Object> extracting(String propertyOrField) {
    List<Object> values = FieldsOrPropertiesExtractor.extract(propertyOrField, actual);
    return new ListAssert<Object>(values);
  }

  /**
   * Extract the values of given fields/properties from the Iterable's elements under test into a new Iterable composed
   * of Tuple (a simple data structure), this new Iterable becoming the Iterable under test.
   * <p>
   * It allows you to test fields/properties of the the Iterable's elements instead of testing the elements themselves,
   * it can be sometimes much less work !
   * <p>
   * The Tuple data corresponds to the extracted values of the given fields/properties, for instance if you ask to
   * extract "id", "name" and "email" then each Tuple data will be composed of id, name and email extracted from the
   * element of the initial Iterable (the Tuple's data order is the same as the given fields/properties order).
   * <p>
   * Let's take an example to make things clearer :
   * 
   * <pre>
   * // Build a list of TolkienCharacter, a TolkienCharacter has a name, and age and a Race (a specific class)
   * // they can be public field or properties, both can be extracted.
   * List&lt;TolkienCharacter&gt; fellowshipOfTheRing = new ArrayList&lt;TolkienCharacter&gt;();
   * 
   * fellowshipOfTheRing.add(new TolkienCharacter(&quot;Frodo&quot;, 33, HOBBIT));
   * fellowshipOfTheRing.add(new TolkienCharacter(&quot;Sam&quot;, 38, HOBBIT));
   * fellowshipOfTheRing.add(new TolkienCharacter(&quot;Gandalf&quot;, 2020, MAIA));
   * fellowshipOfTheRing.add(new TolkienCharacter(&quot;Legolas&quot;, 1000, ELF));
   * fellowshipOfTheRing.add(new TolkienCharacter(&quot;Pippin&quot;, 28, HOBBIT));
   * fellowshipOfTheRing.add(new TolkienCharacter(&quot;Gimli&quot;, 139, DWARF));
   * fellowshipOfTheRing.add(new TolkienCharacter(&quot;Aragorn&quot;, 87, MAN);
   * fellowshipOfTheRing.add(new TolkienCharacter(&quot;Boromir&quot;, 37, MAN));
   * 
   * // let's verify 'name' and 'age' of some TolkienCharacter in fellowshipOfTheRing :
   * 
   * assertThat(fellowshipOfTheRing).extracting("name", "age")
   *                                .contains(tuple("Boromir", 37),
   *                                          tuple("Sam", 38),
   *                                          tuple("Legolas", 1000));
   * 
   * 
   * // extract 'name', 'age' and Race name values.
   * 
   * assertThat(fellowshipOfTheRing).extracting("name", "age", "race.name")
   *                                .contains(tuple("Boromir", 37, "Man"),
   *                                          tuple("Sam", 38, "Hobbit"),
   *                                          tuple("Legolas", 1000, "Elf"));
   * </pre>
   * 
   * A property with the given name is looked for first, if it does'nt exist then a field with the given name is looked
   * for, if no field accessible (ie. does not exist or is not public) an IntrospectionError is thrown.
   * <p>
   * It only works if <b>all</b> objects have the field or all objects have the property with the given name, i.e. it
   * won't work if half of the objects have the field and the other the property.
   * <p>
   * Note that the order of extracted property/field values is consistent with the iteration order of the Iterable under
   * test, for example if it's a {@link HashSet}, you won't be able to make any assumptions of the extracted values
   * order.
   * 
   * @param propertiesOrFields the properties/fields to extract from the initial Iterable under test
   * @return a new assertion object whose object under test is the list of Tuple with extracted properties/fields values
   *         as data.
   * @throws IntrospectionError if one of the given name does not match a field or property (or field exists but is not
   *           public) in one of the initial Iterable's element.
   */
  public ListAssert<Tuple> extracting(String... propertiesOrFields) {
    List<Tuple> values = FieldsOrPropertiesExtractor.extract(actual, propertiesOrFields);
    return new ListAssert<Tuple>(values);
  }

}
