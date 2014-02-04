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

import org.assertj.core.groups.FieldsOrPropertiesExtractor;
import org.assertj.core.groups.MethodInvocationResultExtractor;
import org.assertj.core.groups.Tuple;
import org.assertj.core.internal.*;
import org.assertj.core.util.VisibleForTesting;
import org.assertj.core.util.introspection.IntrospectionError;

import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;

import static org.assertj.core.util.Iterables.toArray;

/**
 * Base class for implementations of <code>{@link ObjectEnumerableAssert}</code> whose actual value type is
 * <code>{@link Collection}</code>.
 *
 * @param <S> the "self" type of this assertion class. Please read &quot;<a href="http://bit.ly/anMa4g"
 *            target="_blank">Emulating 'self types' using Java Generics to simplify fluent API
 *            implementation</a>&quot;
 *            for more details.
 * @param <A> the type of the "actual" value.
 * @param <T> the type of elements of the "actual" value.
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

  /**
   * {@inheritDoc}
   */
  @Override
  public void isNullOrEmpty() {
    iterables.assertNullOrEmpty(info, actual);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void isEmpty() {
    iterables.assertEmpty(info, actual);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public S isNotEmpty() {
    iterables.assertNotEmpty(info, actual);
    return myself;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public S hasSize(int expected) {
    iterables.assertHasSize(info, actual, expected);
    return myself;
  }

  /**
   * {@inheritDoc}
   */
  public S hasSameSizeAs(Object other) {
    iterables.assertHasSameSizeAs(info, actual, other);
    return myself;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public S hasSameSizeAs(Iterable<?> other) {
    iterables.assertHasSameSizeAs(info, actual, other);
    return myself;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public S contains(T... values) {
    iterables.assertContains(info, actual, values);
    return myself;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public S containsOnly(T... values) {
    iterables.assertContainsOnly(info, actual, values);
    return myself;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public S containsOnlyOnce(T... values) {
    iterables.assertContainsOnlyOnce(info, actual, values);
    return myself;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public S containsExactly(T... values) {
    iterables.assertContainsExactly(info, actual, values);
    return myself;
  }

  /**
   * Verifies that all the elements of the actual {@code Iterable} are present in the given {@code Iterable}.
   *
   * @param values the {@code Iterable} that should contain all actual elements.
   * @return this assertion object.
   * @throws AssertionError       if the actual {@code Iterable} is {@code null}.
   * @throws NullPointerException if the given {@code Iterable} is {@code null}.
   * @throws AssertionError       if the actual {@code Iterable} is not subset of set {@code Iterable}.
   */
  public S isSubsetOf(Iterable<? extends T> values) {
    iterables.assertIsSubsetOf(info, actual, values);
    return myself;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public S containsSequence(T... sequence) {
    iterables.assertContainsSequence(info, actual, sequence);
    return myself;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public S containsSubsequence(T... sequence) {
    iterables.assertContainsSubsequence(info, actual, sequence);
    return myself;
  }

  @Override
  public S doesNotContain(T... values) {
    iterables.assertDoesNotContain(info, actual, values);
    return myself;
  }

  /**
   * Verifies that the actual group does not contain any elements of the given {@link Iterable} (i.e. none).
   * <p/>
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
   * @throws NullPointerException     if the given argument is {@code null}.
   * @throws IllegalArgumentException if the given argument is an empty iterable.
   * @throws AssertionError           if the actual group is {@code null}.
   * @throws AssertionError           if the actual group contains some elements of the given {@link Iterable}.
   */
  public S doesNotContainAnyElementsOf(Iterable<? extends T> iterable) {
    iterables.assertDoesNotContainAnyElementsOf(info, actual, iterable);
    return myself;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public S doesNotHaveDuplicates() {
    iterables.assertDoesNotHaveDuplicates(info, actual);
    return myself;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public S startsWith(T... sequence) {
    iterables.assertStartsWith(info, actual, sequence);
    return myself;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public S endsWith(T... sequence) {
    iterables.assertEndsWith(info, actual, sequence);
    return myself;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public S containsNull() {
    iterables.assertContainsNull(info, actual);
    return myself;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public S doesNotContainNull() {
    iterables.assertDoesNotContainNull(info, actual);
    return myself;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public S are(Condition<? super T> condition) {
    iterables.assertAre(info, actual, condition);
    return myself;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public S areNot(Condition<? super T> condition) {
    iterables.assertAreNot(info, actual, condition);
    return myself;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public S have(Condition<? super T> condition) {
    iterables.assertHave(info, actual, condition);
    return myself;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public S doNotHave(Condition<? super T> condition) {
    iterables.assertDoNotHave(info, actual, condition);
    return myself;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public S areAtLeast(int times, Condition<? super T> condition) {
    iterables.assertAreAtLeast(info, actual, times, condition);
    return myself;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public S areAtMost(int times, Condition<? super T> condition) {
    iterables.assertAreAtMost(info, actual, times, condition);
    return myself;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public S areExactly(int times, Condition<? super T> condition) {
    iterables.assertAreExactly(info, actual, times, condition);
    return myself;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public S haveAtLeast(int times, Condition<? super T> condition) {
    iterables.assertHaveAtLeast(info, actual, times, condition);
    return myself;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public S haveAtMost(int times, Condition<? super T> condition) {
    iterables.assertHaveAtMost(info, actual, times, condition);
    return myself;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public S haveExactly(int times, Condition<? super T> condition) {
    iterables.assertHaveExactly(info, actual, times, condition);
    return myself;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public S containsAll(Iterable<? extends T> iterable) {
    iterables.assertContainsAll(info, actual, iterable);
    return myself;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public S usingElementComparator(Comparator<? super T> customComparator) {
    this.iterables = new Iterables(new ComparatorBasedComparisonStrategy(customComparator));
    return myself;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public S usingDefaultElementComparator() {
    this.iterables = Iterables.instance();
    return myself;
  }

  /**
   * Extract the values of given field or property from the Iterable's elements under test into a new Iterable, this
   * new
   * Iterable becoming the Iterable under test.
   * <p/>
   * It allows you to test a property/field of the the Iterable's elements instead of testing the elements themselves,
   * it can be sometimes much less work !
   * <p/>
   * Let's take an example to make things clearer :
   * <p/>
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
   * <p/>
   * A property with the given name is looked for first, if it doesn't exist then a field with the given name is looked
   * for, if no field accessible (ie. does not exist or is not public) an IntrospectionError is thrown.
   * <p/>
   * It only works if <b>all</b> objects have the field or all objects have the property with the given name, i.e. it
   * won't work if half of the objects have the field and the other the property.
   * <p/>
   * Note that the order of extracted property/field values is consistent with the iteration order of the Iterable
   * under
   * test, for example if it's a {@link HashSet}, you won't be able to make any assumptions on the extracted values
   * order.
   *
   * @param propertyOrField the property/field to extract from the Iterable under test
   * @return a new assertion object whose object under test is the list of extracted property/field values.
   * @throws IntrospectionError if no field or property exists with the given name (or field exists but is not public)
   *                            in one of the initial Iterable's element.
   */
  public ListAssert<Object> extracting(String propertyOrField) {
    List<Object> values = FieldsOrPropertiesExtractor.extract(propertyOrField, actual);
    return new ListAssert<Object>(values);
  }

  /**
   * Extract the result of given method invocation on the Iterable's elements under test into a new Iterable, this new
   * Iterable becoming the Iterable under test.
   * <p/>
   * It allows you to test the method results of the Iterable's elements instead of testing the elements themselves, it
   * is
   * especially usefull for classes that does not conform to Java Bean's getter specification (i.e. public String
   * toString() or public String status() instead of public String getStatus()).
   * <p/>
   * Let's take an example to make things clearer :
   * <pre>
   * // Build a array of WesterosHouse, a WesterosHouse has a method: public String sayTheWords()
   *
   * List&lt;WesterosHouse&gt; greatHouses = new ArrayList&lt;WesterosHouse&gt;();
   * greatHouses.add(new WesterosHouse(&quot;Stark&quot;, &quot;Winter is Comming&quot;));
   * greatHouses.add(new WesterosHouse(&quot;Lannister&quot;, &quot;Hear Me Roar!&quot;));
   * greatHouses.add(new WesterosHouse(&quot;Greyjoy&quot;, &quot;We Do Not Sow&quot;));
   * greatHouses.add(new WesterosHouse(&quot;Baratheon&quot;, &quot;Our is the Fury&quot;));
   * greatHouses.add(new WesterosHouse(&quot;Martell&quot;, &quot;Unbowed, Unbent, Unbroken&quot;));
   * greatHouses.add(new WesterosHouse(&quot;Tyrell&quot;, &quot;Growing Strong&quot;));
   *
   * // let's verify the words of great houses in Westeros:
   *
   * assertThat(greatHouses).extractingResultOf(&quot;sayTheWords&quot;)
   *           .contains(&quot;Winter is Comming&quot;, &quot;We Do Not Sow&quot;, &quot;Hear Me Roar&quot;)
   *           .doesNotContain(&quot;Lannisters always pay their debts&quot;);
   * </pre>
   * Following requirements have to be met to extract method results:
   * <ul>
   * <li>method has to be public,</li>
   * <li>method cannot accept any arguments,</li>
   * <li>method cannot return void.</li>
   * </ul>
   * <p/>
   * Note that the order of extracted results is consistent with the iteration order of the Iterable under
   * test, for example if it's a {@link HashSet}, you won't be able to make any assumptions on the extracted results
   * order.
   *
   * @param method the name of the method which result is to be extracted from the array under test
   * @return a new assertion object whose object under test is the Iterable of extracted values.
   * @throws IllegalArgumentException if no method exists with the given name, or method is not public,
   *                                  or method does return void, or method accepts arguments.
   */
  public ListAssert<Object> extractingResultOf(String method) {
    List<Object> values = MethodInvocationResultExtractor.extractResultOf(method, actual);
    return new ListAssert<Object>(values);
  }

  /**
   * Extract the result of given method invocation on the Iterable's elements under test into a new list of the given
   * class, this new List becoming the object under test.
   * <p/>
   * It allows you to test the method results of the Iterable's elements instead of testing the elements themselves, it
   * is especially usefull for classes that does not conform to Java Bean's getter specification (i.e. public String
   * toString() or public String status() instead of public String getStatus()).
   * <p/>
   * Let's take an example to make things clearer :
   * <pre>
   * // Build a array of WesterosHouse, a WesterosHouse has a method: public String sayTheWords()
   * List&lt;WesterosHouse&gt; greatHouses = new ArrayList&lt;WesterosHouse&gt;();
   * greatHouses.add(new WesterosHouse(&quot;Stark&quot;, &quot;Winter is Comming&quot;));
   * greatHouses.add(new WesterosHouse(&quot;Lannister&quot;, &quot;Hear Me Roar!&quot;));
   * greatHouses.add(new WesterosHouse(&quot;Greyjoy&quot;, &quot;We Do Not Sow&quot;));
   * greatHouses.add(new WesterosHouse(&quot;Baratheon&quot;, &quot;Our is the Fury&quot;));
   * greatHouses.add(new WesterosHouse(&quot;Martell&quot;, &quot;Unbowed, Unbent, Unbroken&quot;));
   * greatHouses.add(new WesterosHouse(&quot;Tyrell&quot;, &quot;Growing Strong&quot;));
   *
   * // let's verify the words of great houses in Westeros:
   *
   * assertThat(greatHouses).extractingResultOf(&quot;sayTheWords&quot;, String.class)
   *           .contains(&quot;Winter is Comming&quot;, &quot;We Do Not Sow&quot;, &quot;Hear Me Roar&quot;)
   *           .doesNotContain(&quot;Lannisters always pay their debts&quot;);
   * </pre>
   * Following requirements have to be met to extract method results:
   * <ul>
   * <li>method has to be public,</li>
   * <li>method cannot accept any arguments,</li>
   * <li>method cannot return void.</li>
   * </ul>
   * <p/>
   * Note that the order of extracted property/field values is consistent with the iteration order of the Iterable
   * under test, for example if it's a {@link HashSet}, you won't be able to make any assumptions of the extracted
   * values order.
   *
   * @param method        the name of the method which result is to be extracted from the array under test
   * @param extractedType type of element of the extracted List
   * @return a new assertion object whose object under test is the Iterable of extracted values.
   * @throws IllegalArgumentException if no method exists with the given name, or method is not public,
   *                                  or method does return void or method accepts arguments.
   */
  public <P> ListAssert<P> extractingResultOf(String method, Class<P> extractedType) {
    @SuppressWarnings("unchecked")
    List<P> values = (List<P>) MethodInvocationResultExtractor.extractResultOf(method, actual);
    return new ListAssert<P>(values);
  }

  /**
   * Extract the values of given field or property from the Iterable's elements under test into a new Iterable, this
   * new
   * Iterable becoming the Iterable under test.
   * <p/>
   * It allows you to test a property/field of the the Iterable's elements instead of testing the elements themselves,
   * it can be sometimes much less work !
   * <p/>
   * Let's take an example to make things clearer :
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
   * assertThat(fellowshipOfTheRing).extracting(&quot;name&quot;, String.class)
   *           .contains(&quot;Boromir&quot;, &quot;Gandalf&quot;, &quot;Frodo&quot;)
   *           .doesNotContain(&quot;Sauron&quot;, &quot;Elrond&quot;);
   *
   * // you can extract nested property/field like the name of Race :
   *
   * assertThat(fellowshipOfTheRing).extracting(&quot;race.name&quot;, String.class)
   *           .contains(&quot;Hobbit&quot;, &quot;Elf&quot;)
   *           .doesNotContain(&quot;Orc&quot;);
   * </pre>
   * A property with the given name is looked for first, if it doesn't exist then a field with the given name is looked
   * for, if no field accessible (ie. does not exist or is not public) an IntrospectionError is thrown.
   * <p/>
   * It only works if <b>all</b> objects have the field or all objects have the property with the given name, i.e. it
   * won't work if half of the objects have the field and the other the property.
   * <p/>
   * Note that the order of extracted property/field values is consistent with the iteration order of the Iterable
   * under test, for example if it's a {@link HashSet}, you won't be able to make any assumptions on the extracted
   * values order.
   *
   * @param propertyOrField the property/field to extract from the Iterable under test
   * @param extractingType  type to return
   * @return a new assertion object whose object under test is the list of extracted property/field values.
   * @throws IntrospectionError if no field or property exists with the given name (or field exists but is not public)
   *                            in one of the initial Iterable's element.
   */
  public <P> ListAssert<P> extracting(String propertyOrField, Class<P> extractingType) {
    @SuppressWarnings("unchecked")
    List<P> values = (List<P>) FieldsOrPropertiesExtractor.extract(propertyOrField, actual);
    return new ListAssert<P>(values);
  }

  /**
   * Extract the values of given fields/properties from the Iterable's elements under test into a new Iterable composed
   * of Tuple (a simple data structure), this new Iterable becoming the Iterable under test.
   * <p/>
   * It allows you to test fields/properties of the the Iterable's elements instead of testing the elements themselves,
   * it can be sometimes much less work !
   * <p/>
   * The Tuple data corresponds to the extracted values of the given fields/properties, for instance if you ask to
   * extract "id", "name" and "email" then each Tuple data will be composed of id, name and email extracted from the
   * element of the initial Iterable (the Tuple's data order is the same as the given fields/properties order).
   * <p/>
   * Let's take an example to make things clearer :
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
   * A property with the given name is looked for first, if it does'nt exist then a field with the given name is looked
   * for, if no field accessible (ie. does not exist or is not public) an IntrospectionError is thrown.
   * <p/>
   * It only works if <b>all</b> objects have the field or all objects have the property with the given name, i.e. it
   * won't work if half of the objects have the field and the other the property.
   * <p/>
   * Note that the order of extracted property/field values is consistent with the iteration order of the Iterable
   * under  test, for example if it's a {@link HashSet}, you won't be able to make any assumptions on the extracted
   * values order.
   *
   * @param propertiesOrFields the properties/fields to extract from the initial Iterable under test
   * @return a new assertion object whose object under test is the list of Tuple with extracted properties/fields
   *         values as data.
   * @throws IntrospectionError if one of the given name does not match a field or property (or field exists but is not
   *                            public) in one of the initial Iterable's element.
   */
  public ListAssert<Tuple> extracting(String... propertiesOrFields) {
    List<Tuple> values = FieldsOrPropertiesExtractor.extract(actual, propertiesOrFields);
    return new ListAssert<Tuple>(values);
  }

  /**
   * Same as {@link #containsExactly(Object[])} but handle the {@link Iterable} to array conversion.
   * <p/>
   * Example :
   * <pre>
   * Iterable&lt;Ring&gt; elvesRings = newArrayList(vilya, nenya, narya);
   *
   * // assertion will pass
   * assertThat(elvesRings).containsExactly(newLinkedList(vilya, nenya, narya));
   *
   * // assertion will fail as actual and expected orders differ.
   * assertThat(elvesRings).containsExactly(newLinkedList(nenya, vilya, narya));
   * </pre>
   *
   * @param iterable the given {@code Iterable} we will get elements from.
   */
  public S containsExactlyElementsOf(Iterable<? extends T> iterable) {
    return containsExactly(toArray(iterable));
  }

    /**
     * Use field by field comparison (including inherited fields) instead of relying
     * on actual type A <code>equals</code> method to compare group elements
     * for incoming assertion checks.
     *
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
     * assertThat(newArrayList(frodo)).contains(frodoClone);
     *
     * // frodo and frodoClone are equals when doing a field by field comparison.
     * assertThat(newArrayList(frodo)).usingFieldByFieldElementComparator().contains(frodoClone);
     *
     * </pre>
     *
     * @return {@code this} assertion object.
     */
  public S usingFieldByFieldElementComparator() {
    return usingComparisonStrategy(new FieldByFieldComparisonStrategy());
  }

    /**
     * Use field by field comparison on the given fields only (fields can be inherited fields)
     * instead of relying on actual type A <code>equals</code> method to compare group elements
     * for incoming assertion checks.
     *
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
     * TolkienCharacter sam = new TolkienCharacter("Sam", 38, HOBBIT);
     *
     * // frodo and sam both are hobbits, so they are equals when comparing only race
     * assertThat(newArrayList(frodo)).usingElementComparatorOnFields("race").contains(sam); // OK
     *
     * // ... but not when comparing both name and race
     * assertThat(newArrayList(frodo)).usingElementComparatorOnFields("name", "race").contains(sam); // FAIL
     *
     * </pre>
     *
     * @return {@code this} assertion object.
     */
  public S usingElementComparatorOnFields(String... fields) {
    return usingComparisonStrategy(new OnFieldsComparisonStrategy(fields));
  }

  protected S usingComparisonStrategy(ComparisonStrategy comparisonStrategy) {
    iterables = new Iterables(comparisonStrategy);
    return myself;
  }
    /**
     * Use field by field comparison on all fields except for the given ones
     * (inherited fields are taken into account)
     * instead of relying on actual type A <code>equals</code> method to compare group elements
     * for incoming assertion checks.
     *
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
     * TolkienCharacter sam = new TolkienCharacter("Sam", 38, HOBBIT);
     *
     * // frodo and sam both are hobbits, so they are equals when comparing only race (i.e. ignoring all other fields)
     * assertThat(newArrayList(frodo)).usingElementComparatorIgnoringFields("name", "age").contains(sam); // OK
     *
     * // ... but not when comparing both name and race
     * assertThat(newArrayList(frodo)).usingElementComparatorIgnoringFields("age").contains(sam); // FAIL
     *
     * </pre>
     *
     * @return {@code this} assertion object.
     */
  public S usingElementComparatorIgnoringFields(String... fields) {
    return usingComparisonStrategy(new IgnoringFieldsComparisonStrategy(fields));
  }

  /**
   * Enable hexadecimal representation of Iterable elements instead of standard representation in error messages.
   * <p/>
   * It can be useful to better understand what the error was with a more meaningful error message.
   * <p/>
   * Example
   * <pre>
   * final List<Byte> bytes = newArrayList((byte)0x10, (byte) 0x20);
   * </pre>
   *
   * With standard error message:
   * <pre>
   * assertThat(bytes).contains((byte)0x30);
   *
   * Expecting:
   *  <[16, 32]>
   * to contain:
   *  <[48]>
   * but could not find:
   *  <[48]>
   * </pre>
   *
   * With Hexadecimal error message:
   * <pre>
   * assertThat(bytes).inHexadecimal().contains((byte)0x30);
   *
   * Expecting:
   *  <[0x10, 0x20]>
   * to contain:
   *  <[0x30]>
   * but could not find:
   *  <[0x30]>
   * </pre>
   *
   * @return {@code this} assertion object.
   */
  @Override
  public S inHexadecimal() { // TODO rename to asHexadecimalElements() ?
    return super.inHexadecimal();
  }

  /**
   * Enable binary representation of Iterable elements instead of standard representation in error messages.
   * <p/>
   * Example:
   * <pre>
   * final List<Byte> bytes = newArrayList((byte)0x10, (byte) 0x20);
   * </pre>
   *
   * With standard error message:
   * <pre>
   * assertThat(bytes).contains((byte)0x30);
   *
   * Expecting:
   *  <[16, 32]>
   * to contain:
   *  <[48]>
   * but could not find:
   *  <[48]>
   * </pre>
   *
   * With binary error message:
   * <pre>
   * assertThat(bytes).inBinary().contains((byte)0x30);
   *
   * Expecting:
   *  <[0b00010000, 0b00100000]>
   * to contain:
   *  <[0b00110000]>
   * but could not find:
   *  <[0b00110000]>
   * </pre>
   *
   * @return {@code this} assertion object.
   */
  @Override
  public S inBinary() {
    return super.inBinary();
  }
}
