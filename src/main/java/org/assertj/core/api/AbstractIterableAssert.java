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

import static org.assertj.core.extractor.Extractors.byName;
import static org.assertj.core.extractor.Extractors.resultOf;
import static org.assertj.core.util.Arrays.isArray;
import static org.assertj.core.util.Iterables.toArray;
import static org.assertj.core.util.Lists.newArrayList;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;

import org.assertj.core.api.iterable.Extractor;
import org.assertj.core.groups.FieldsOrPropertiesExtractor;
import org.assertj.core.groups.Tuple;
import org.assertj.core.internal.CommonErrors;
import org.assertj.core.internal.ComparatorBasedComparisonStrategy;
import org.assertj.core.internal.ComparisonStrategy;
import org.assertj.core.internal.FieldByFieldComparator;
import org.assertj.core.internal.IgnoringFieldsComparator;
import org.assertj.core.internal.IterableElementComparisonStrategy;
import org.assertj.core.internal.Iterables;
import org.assertj.core.internal.ObjectArrays;
import org.assertj.core.internal.Objects;
import org.assertj.core.internal.OnFieldsComparator;
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
 * @author Yvonne Wang
 * @author Alex Ruiz
 * @author Mathieu Baechler
 * @author Joel Costigliola
 * @author Maciej Jaskowski
 * @author Nicolas François
 * @author Mikhail Mazursky
 * @author Mateusz Haligowski
 */
public abstract class AbstractIterableAssert<S extends AbstractIterableAssert<S, A, T>, A extends Iterable<? extends T>, T>
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
  @Override
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
  public S contains(@SuppressWarnings("unchecked") T... values) {
    iterables.assertContains(info, actual, values);
    return myself;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public S containsOnly(@SuppressWarnings("unchecked") T... values) {
    iterables.assertContainsOnly(info, actual, values);
    return myself;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public S containsOnlyOnce(@SuppressWarnings("unchecked") T... values) {
    iterables.assertContainsOnlyOnce(info, actual, values);
    return myself;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public S containsExactly(@SuppressWarnings("unchecked") T... values) {
    iterables.assertContainsExactly(info, actual, values);
    return myself;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public S isSubsetOf(Iterable<? extends T> values) {
    iterables.assertIsSubsetOf(info, actual, values);
    return myself;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public S containsSequence(@SuppressWarnings("unchecked") T... sequence) {
    iterables.assertContainsSequence(info, actual, sequence);
    return myself;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public S containsSubsequence(@SuppressWarnings("unchecked") T... sequence) {
    iterables.assertContainsSubsequence(info, actual, sequence);
    return myself;
  }

  @Override
  public S doesNotContain(@SuppressWarnings("unchecked") T... values) {
    iterables.assertDoesNotContain(info, actual, values);
    return myself;
  }

  @Override
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
  public S startsWith(@SuppressWarnings("unchecked") T... sequence) {
    iterables.assertStartsWith(info, actual, sequence);
    return myself;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public S endsWith(@SuppressWarnings("unchecked") T... sequence) {
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
  public S areAtLeastOne(Condition<? super T> condition) {
    areAtLeast(1, condition);
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

  /** {@inheritDoc} */
  @Override
  public S haveAtLeastOne(Condition<? super T> condition) {
    return haveAtLeast(1, condition);
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
   * Verifies that at least one element in the actual {@code Iterable} belong to the specified type (matching includes
   * subclasses of the given type).
   * <p/>
   * Example:
   * 
   * <pre><code class='java'>
   * List&lt;Number&gt; numbers = new ArrayList&lt;Number&gt;();
   * numbers.add(1);
   * numbers.add(2L);
   * 
   * // successful assertion:
   * assertThat(numbers).hasAtLeastOneElementOfType(Long.class);
   * 
   * // assertion failure:
   * assertThat(numbers).hasAtLeastOneElementOfType(Float.class);
   * </code></pre>
   *
   * @param expectedType the expected type.
   * @return this assertion object.
   * @throws NullPointerException if the given type is {@code null}.
   * @throws AssertionError if the actual {@code Object} group does not have any elements of the given type.
   */
  @Override
  public S hasAtLeastOneElementOfType(Class<?> expectedType) {
    // reuse code from object arrays as the logic is the same
    // (ok since this assertion don't rely on comparison strategy)
    ObjectArrays.instance().assertHasAtLeastOneElementOfType(info, toArray(actual), expectedType);
    return myself;
  }

  /**
   * Verifies that all the elements in the actual {@code Iterable} belong to the specified type (matching includes
   * subclasses of the given type).
   * <p/>
   * Example:
   * 
   * <pre><code class='java'>
   * List&lt;Number&gt; numbers = new ArrayList&lt;Number&gt;();
   * numbers.add(1);
   * numbers.add(2);
   * numbers.add(3);
   * 
   * // successful assertion:
   * assertThat(numbers).hasOnlyElementsOfType(Number.class);
   * assertThat(numbers).hasOnlyElementsOfType(Integer.class);
   * 
   * // assertion failure:
   * assertThat(numbers).hasOnlyElementsOfType(Long.class);
   * </code></pre>
   *
   * @param expectedType the expected type.
   * @return this assertion object.
   * @throws NullPointerException if the given type is {@code null}.
   * @throws AssertionError if one element is not of the expected type.
   */
  @Override
  public S hasOnlyElementsOfType(Class<?> expectedType) {
    // reuse code from object arrays as the logic is the same
    // (ok since this assertion don't rely on comparison strategy)
    ObjectArrays.instance().assertHasOnlyElementsOfType(info, toArray(actual), expectedType);
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
  public S usingElementComparator(Comparator<? super T> elementComparator) {
    this.iterables = new Iterables(new ComparatorBasedComparisonStrategy(elementComparator));
    // to have the same semantics on base assertions like isEqualTo, we need to use an iterable comparator comparing
    // elements with elementComparator parameter
    objects = new Objects(new IterableElementComparisonStrategy<>(elementComparator));
    return myself;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public S usingDefaultElementComparator() {
    usingDefaultComparator();
    this.iterables = Iterables.instance();
    return myself;
  }

  /**
   * Extract the values of given field or property from the Iterable's elements under test into a new Iterable, this new
   * Iterable becoming the Iterable under test.
   * <p/>
   * It allows you to test a property/field of the the Iterable's elements instead of testing the elements themselves,
   * it can be sometimes much less work !
   * <p/>
   * Let's take an example to make things clearer :
   * 
   * <pre><code class='java'>
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
   *                                .contains(&quot;Hobbit&quot;, &quot;Elf&quot;)
   *                                .doesNotContain(&quot;Orc&quot;);
   * </code></pre>
   * <p/>
   * A property with the given name is looked for first, if it doesn't exist then a field with the given name is looked
   * for, if the field does not exist an {@link IntrospectionError} is thrown, by default private fields are read but
   * you can change this with {@link Assertions#setAllowComparingPrivateFields(boolean)}, trying to read a private field
   * when it's not allowed leads to an {@link IntrospectionError}.
   * <p/>
   * It only works if <b>all</b> objects have the field or all objects have the property with the given name, i.e. it
   * won't work if half of the objects have the field and the other the property.
   * <p/>
   * Note that the order of extracted property/field values is consistent with the iteration order of the Iterable under
   * test, for example if it's a {@link HashSet}, you won't be able to make any assumptions on the extracted values
   * order.
   * <hr>
   * <p/>
   * Extracting also support maps, that is, instead of extracting values from an Object, it extract maps values
   * corresponding to the given keys.
   * <p/>
   * Example:
   * 
   * <pre><code class='java'>
   * Employee yoda = new Employee(1L, new Name("Yoda"), 800);
   * Employee luke = new Employee(2L, new Name("Luke"), 22);
   * Employee han = new Employee(3L, new Name("Han"), 31);
   * 
   * // build two maps
   * Map&lt;String, Employee&gt; map1 = new HashMap&lt;&gt;();
   * map1.put("key1", yoda);
   * map1.put("key2", luke);
   * 
   * Map&lt;String, Employee&gt; map2 = new HashMap&lt;&gt;();
   * map2.put("key1", yoda);
   * map2.put("key2", han);
   * 
   * // instead of a list of objects, we have a list of maps 
   * List&lt;Map&lt;String, Employee&gt;&gt; maps = asList(map1, map2);
   * 
   * // extracting a property in that case = get values from maps using property as a key
   * assertThat(maps).extracting("key2").containsExactly(luke, han);
   * assertThat(maps).extracting("key1").containsExactly(yoda, yoda);
   * 
   * // type safe version
   * assertThat(maps).extracting(key2, Employee.class).containsExactly(luke, han); 
   * 
   * // it works with several keys, extracted values being wrapped in a Tuple
   * assertThat(maps).extracting("key1", "key2").containsExactly(tuple(yoda, luke), tuple(yoda, han));
   * 
   * // unknown keys leads to null (map behavior)
   * assertThat(maps).extracting("bad key").containsExactly(null, null);
   * </code></pre>
   *
   * @param propertyOrField the property/field to extract from the elements of the Iterable under test
   * @return a new assertion object whose object under test is the list of extracted property/field values.
   * @throws IntrospectionError if no field or property exists with the given name (or field exists but is not public)
   *           in one of the initial Iterable's element.
   */
  public ListAssert<Object> extracting(String propertyOrField) {
    List<Object> values = FieldsOrPropertiesExtractor.extract(actual, byName(propertyOrField));
    return new ListAssert<>(values);
  }

  /**
   * Extract the result of given method invocation on the Iterable's elements under test into a new Iterable, this new
   * Iterable becoming the Iterable under test.
   * <p/>
   * It allows you to test the method results of the Iterable's elements instead of testing the elements themselves, it
   * is especially usefull for classes that does not conform to Java Bean's getter specification (i.e. public String
   * toString() or public String status() instead of public String getStatus()).
   * <p/>
   * Let's take an example to make things clearer :
   * 
   * <pre><code class='java'>
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
   *                        .contains(&quot;Winter is Comming&quot;, &quot;We Do Not Sow&quot;, &quot;Hear Me Roar&quot;)
   *                        .doesNotContain(&quot;Lannisters always pay their debts&quot;);
   * </code></pre>
   * 
   * Following requirements have to be met to extract method results:
   * <ul>
   * <li>method has to be public,</li>
   * <li>method cannot accept any arguments,</li>
   * <li>method cannot return void.</li>
   * </ul>
   * <p/>
   * Note that the order of extracted results is consistent with the iteration order of the Iterable under test, for
   * example if it's a {@link HashSet}, you won't be able to make any assumptions on the extracted results order.
   *
   * @param method the name of the method which result is to be extracted from the array under test
   * @return a new assertion object whose object under test is the Iterable of extracted values.
   * @throws IllegalArgumentException if no method exists with the given name, or method is not public, or method does
   *           return void, or method accepts arguments.
   */
  public ListAssert<Object> extractingResultOf(String method) {
    List<Object> values = FieldsOrPropertiesExtractor.extract(actual, resultOf(method));
    return new ListAssert<>(values);
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
   * 
   * <pre><code class='java'>
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
   *                        .contains(&quot;Winter is Comming&quot;, &quot;We Do Not Sow&quot;, &quot;Hear Me Roar&quot;)
   *                        .doesNotContain(&quot;Lannisters always pay their debts&quot;);
   * </code></pre>
   * 
   * Following requirements have to be met to extract method results:
   * <ul>
   * <li>method has to be public,</li>
   * <li>method cannot accept any arguments,</li>
   * <li>method cannot return void.</li>
   * </ul>
   * <p/>
   * Note that the order of extracted property/field values is consistent with the iteration order of the Iterable under
   * test, for example if it's a {@link HashSet}, you won't be able to make any assumptions of the extracted values
   * order.
   *
   * @param method the name of the method which result is to be extracted from the array under test
   * @param extractedType type of element of the extracted List
   * @return a new assertion object whose object under test is the Iterable of extracted values.
   * @throws IllegalArgumentException if no method exists with the given name, or method is not public, or method does
   *           return void or method accepts arguments.
   */
  public <P> ListAssert<P> extractingResultOf(String method, Class<P> extractedType) {
    @SuppressWarnings("unchecked")
    List<P> values = (List<P>) FieldsOrPropertiesExtractor.extract(actual, resultOf(method));
    return new ListAssert<>(values);
  }

  /**
   * Extract the values of given field or property from the Iterable's elements under test into a new Iterable, this new
   * Iterable becoming the Iterable under test.
   * <p/>
   * It allows you to test a property/field of the the Iterable's elements instead of testing the elements themselves,
   * it can be sometimes much less work !
   * <p/>
   * Let's take an example to make things clearer :
   * 
   * <pre><code class='java'>
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
   *                                .contains(&quot;Hobbit&quot;, &quot;Elf&quot;)
   *                                .doesNotContain(&quot;Orc&quot;);
   * </code></pre>
   * 
   * A property with the given name is looked for first, if it doesn't exist then a field with the given name is looked
   * for, if the field does not exist an {@link IntrospectionError} is thrown, by default private fields are read but
   * you can change this with {@link Assertions#setAllowComparingPrivateFields(boolean)}, trying to read a private field
   * when it's not allowed leads to an {@link IntrospectionError}.
   * <p/>
   * It only works if <b>all</b> objects have the field or all objects have the property with the given name, i.e. it
   * won't work if half of the objects have the field and the other the property.
   * <p/>
   * Note that the order of extracted property/field values is consistent with the iteration order of the Iterable under
   * test, for example if it's a {@link HashSet}, you won't be able to make any assumptions on the extracted values
   * order.
   * <hr>
   * <p/>
   * Extracting also support maps, that is, instead of extracting values from an Object, it extract maps values
   * corresponding to the given keys.
   * <p/>
   * Example:
   * 
   * <pre><code class='java'>
   * Employee yoda = new Employee(1L, new Name("Yoda"), 800);
   * Employee luke = new Employee(2L, new Name("Luke"), 22);
   * Employee han = new Employee(3L, new Name("Han"), 31);
   * 
   * // build two maps
   * Map&lt;String, Employee&gt; map1 = new HashMap&lt;&gt;();
   * map1.put("key1", yoda);
   * map1.put("key2", luke);
   * 
   * Map&lt;String, Employee&gt; map2 = new HashMap&lt;&gt;();
   * map2.put("key1", yoda);
   * map2.put("key2", han);
   * 
   * // instead of a list of objects, we have a list of maps 
   * List&lt;Map&lt;String, Employee&gt;&gt; maps = asList(map1, map2);
   * 
   * // extracting a property in that case = get values from maps using property as a key
   * assertThat(maps).extracting(key2, Employee.class).containsExactly(luke, han); 
   * 
   * // non type safe version
   * assertThat(maps).extracting("key2").containsExactly(luke, han);
   * assertThat(maps).extracting("key1").containsExactly(yoda, yoda);
   * 
   * // it works with several keys, extracted values being wrapped in a Tuple
   * assertThat(maps).extracting("key1", "key2").containsExactly(tuple(yoda, luke), tuple(yoda, han));
   * 
   * // unknown keys leads to null (map behavior)
   * assertThat(maps).extracting("bad key").containsExactly(null, null);
   * </code></pre>
   *
   * @param propertyOrField the property/field to extract from the Iterable under test
   * @param extractingType type to return
   * @return a new assertion object whose object under test is the list of extracted property/field values.
   * @throws IntrospectionError if no field or property exists with the given name (or field exists but is not public)
   *           in one of the initial Iterable's element.
   */
  public <P> ListAssert<P> extracting(String propertyOrField, Class<P> extractingType) {
    @SuppressWarnings("unchecked")
    List<P> values = (List<P>) FieldsOrPropertiesExtractor.extract(actual, byName(propertyOrField));
    return new ListAssert<>(values);
  }

  /**
   * Extract the values of given fields/properties from the Iterable's elements under test into a new Iterable composed
   * of Tuple (a simple data structure), this new Iterable becoming the Iterable under test.
   * <p/>
   * It allows you to test fields/properties of the the Iterable's elements instead of testing the elements themselves,
   * it can be sometimes much less work!
   * <p/>
   * The Tuple data corresponds to the extracted values of the given fields/properties, for instance if you ask to
   * extract "id", "name" and "email" then each Tuple data will be composed of id, name and email extracted from the
   * element of the initial Iterable (the Tuple's data order is the same as the given fields/properties order).
   * <p/>
   * Let's take an example to make things clearer :
   * 
   * <pre><code class='java'>
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
   * assertThat(fellowshipOfTheRing).extracting(&quot;name&quot;, &quot;age&quot;)
   *                                .contains(tuple(&quot;Boromir&quot;, 37),
   *                                          tuple(&quot;Sam&quot;, 38),
   *                                          tuple(&quot;Legolas&quot;, 1000));
   * 
   * 
   * // extract 'name', 'age' and Race name values.
   * 
   * assertThat(fellowshipOfTheRing).extracting(&quot;name&quot;, &quot;age&quot;, &quot;race.name&quot;)
   *                                .contains(tuple(&quot;Boromir&quot;, 37, &quot;Man&quot;),
   *                                          tuple(&quot;Sam&quot;, 38, &quot;Hobbit&quot;),
   *                                          tuple(&quot;Legolas&quot;, 1000, &quot;Elf&quot;));
   * </code></pre>
   * 
   * A property with the given name is looked for first, if it doesn't exist then a field with the given name is looked
   * for, if the field does not exist an {@link IntrospectionError} is thrown, by default private fields are read but
   * you can change this with {@link Assertions#setAllowComparingPrivateFields(boolean)}, trying to read a private field
   * when it's not allowed leads to an {@link IntrospectionError}.
   * <p/>
   * It only works if <b>all</b> objects have the field or all objects have the property with the given name, i.e. it
   * won't work if half of the objects have the field and the other the property.
   * <p/>
   * Note that the order of extracted property/field values is consistent with the iteration order of the Iterable under
   * test, for example if it's a {@link HashSet}, you won't be able to make any assumptions on the extracted values
   * order.
   * <hr>
   * <p/>
   * Extracting also support maps, that is, instead of extracting values from an Object, it extract maps values
   * corresponding to the given keys.
   * <p/>
   * Example:
   * 
   * <pre><code class='java'>
   * Employee yoda = new Employee(1L, new Name("Yoda"), 800);
   * Employee luke = new Employee(2L, new Name("Luke"), 22);
   * Employee han = new Employee(3L, new Name("Han"), 31);
   * 
   * // build two maps
   * Map&lt;String, Employee&gt; map1 = new HashMap&lt;&gt;();
   * map1.put("key1", yoda);
   * map1.put("key2", luke);
   * 
   * Map&lt;String, Employee&gt; map2 = new HashMap&lt;&gt;();
   * map2.put("key1", yoda);
   * map2.put("key2", han);
   * 
   * // instead of a list of objects, we have a list of maps 
   * List&lt;Map&lt;String, Employee&gt;&gt; maps = asList(map1, map2);
   * 
   * // extracting a property in that case = get values from maps using property as a key
   * assertThat(maps).extracting("key2").containsExactly(luke, han);
   * assertThat(maps).extracting("key1").containsExactly(yoda, yoda);
   * 
   * // it works with several keys, extracted values being wrapped in a Tuple
   * assertThat(maps).extracting("key1", "key2").containsExactly(tuple(yoda, luke), tuple(yoda, han));
   * 
   * // unknown keys leads to null (map behavior)
   * assertThat(maps).extracting("bad key").containsExactly(null, null);
   * </code></pre>
   *
   * @param propertiesOrFields the properties/fields to extract from the elements of the Iterable under test
   * @return a new assertion object whose object under test is the list of Tuple with extracted properties/fields values
   *         as data.
   * @throws IntrospectionError if one of the given name does not match a field or property (or field exists but is not
   *           public) in one of the initial Iterable's element.
   */
  public ListAssert<Tuple> extracting(String... propertiesOrFields) {
    List<Tuple> values = FieldsOrPropertiesExtractor.extract(actual, byName(propertiesOrFields));
    return new ListAssert<>(values);
  }

  /**
   * Extract the values from Iterable's elements under test by applying an extracting function on them. The returned
   * iterable becomes a new object under test.
   * <p/>
   * It allows to test values from the elements in more safe way than by using {@link #extracting(String)}, as it
   * doesn't utilize introspection.
   * <p/>
   * Let's have a look at an example :
   * 
   * <pre><code class='java'>
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
   * // this extracts the race
   * Extractor&lt;TolkienCharacter, Race&gt; race = new Extractor&lt;TolkienCharacter, Race&gt;() {
   *    &commat;Override
   *    public Race extract(TolkienCharacter input) {
   *        return input.getRace();
   *    }
   * }
   * 
   * // fellowship has hobbitses, right, my presioussss?
   * assertThat(fellowshipOfTheRing).extracting(race).contains(HOBBIT);
   * </code></pre>
   * 
   * Note that the order of extracted property/field values is consistent with the iteration order of the Iterable under
   * test, for example if it's a {@link HashSet}, you won't be able to make any assumptions on the extracted values
   * order.
   * 
   * @param extractor the object transforming input object to desired one
   * @return a new assertion object whose object under test is the list of values extracted
   */
  public <V> ListAssert<V> extracting(Extractor<? super T, V> extractor) {
    List<V> values = FieldsOrPropertiesExtractor.extract(actual, extractor);
    return new ListAssert<>(values);
  }

  /**
   * Extract the Iterable values from Iterable's elements under test by applying an Iterable extracting function on them
   * and concatenating the result lists. The returned iterable becomes a new object under test.
   * <p/>
   * It allows testing the results of extracting values that are represented by Iterables.
   * <p/>
   * For example:
   * 
   * <pre><code class='java'>
   * CartoonCharacter bart = new CartoonCharacter("Bart Simpson");
   * CartoonCharacter lisa = new CartoonCharacter("Lisa Simpson");
   * CartoonCharacter maggie = new CartoonCharacter("Maggie Simpson");
   * CartoonCharacter homer = new CartoonCharacter("Homer Simpson");
   * homer.addChildren(bart, lisa, maggie);
   * 
   * CartoonCharacter pebbles = new CartoonCharacter("Pebbles Flintstone");
   * CartoonCharacter fred = new CartoonCharacter("Fred Flintstone");
   * fred.getChildren().add(pebbles);
   * 
   * Extractor&lt;CartoonCharacter, List&lt;CartoonCharacter&gt;&gt; childrenOf = new Extractor&lt;CartoonChildren, List&lt;CartoonChildren&gt;&gt;() {
   *    &commat;Override
   *    public List&lt;CartoonChildren&gt; extract(CartoonCharacter input) {
   *        return input.getChildren();
   *    }
   * }
   * 
   * List&lt;CartoonCharacter&gt; parents = newArrayList(homer, fred);
   * // check children
   * assertThat(parent).flatExtracting(childrenOf)
   *                   .containsOnly(bart, lisa, maggie, pebbles);
   * </code></pre>
   * 
   * The order of extracted values is consisted with both the order of the collection itself, as well as the extracted
   * collections.
   * 
   * @param extractor the object transforming input object to an Iterable of desired ones
   * @return a new assertion object whose object under test is the list of values extracted
   */
  public <V> ListAssert<V> flatExtracting(Extractor<? super T, ? extends Collection<V>> extractor) {
    List<V> result = newArrayList();
    final List<? extends Collection<V>> extractedValues = FieldsOrPropertiesExtractor.extract(actual, extractor);

    for (Collection<? extends V> iterable : extractedValues) {
      result.addAll(iterable);
    }

    return new ListAssert<>(result);
  }

  /**
   * Extract from Iterable's elements the Iterable/Array values corresponding to the given property/field name and
   * concatenate them into a single list becoming the new object under test.
   * <p/>
   * It allows testing the elements of extracting values that are represented by iterables or arrays.
   * <p/>
   * For example:
   *
   * <pre><code class='java'>
   * CartoonCharacter bart = new CartoonCharacter("Bart Simpson");
   * CartoonCharacter lisa = new CartoonCharacter("Lisa Simpson");
   * CartoonCharacter maggie = new CartoonCharacter("Maggie Simpson");
   * CartoonCharacter homer = new CartoonCharacter("Homer Simpson");
   * homer.addChildren(bart, lisa, maggie);
   *
   * CartoonCharacter pebbles = new CartoonCharacter("Pebbles Flintstone");
   * CartoonCharacter fred = new CartoonCharacter("Fred Flintstone");
   * fred.getChildren().add(pebbles);
   *
   * List&lt;CartoonCharacter&gt; parents = newArrayList(homer, fred);
   * // check children
   * assertThat(parents).flatExtracting("children")
   *                    .containsOnly(bart, lisa, maggie, pebbles);
   * </code></pre>
   *
   * The order of extracted values is consisted with both the order of the collection itself, as well as the extracted
   * collections.
   *
   * @param propertyName the object transforming input object to an Iterable of desired ones
   * @return a new assertion object whose object under test is the list of values extracted
   * @throws IllegalArgumentException if one of the extracted property value was not an array or an iterable.
   */
  public ListAssert<Object> flatExtracting(String propertyName) {
    List<Object> extractedValues = newArrayList();
    List<?> extractedGroups = FieldsOrPropertiesExtractor.extract(actual, byName(propertyName));
    for (Object group : extractedGroups) {
      // expecting group to be an iterable or an array
      if (isArray(group)) {
        int size = Array.getLength(group);
        for (int i = 0; i < size; i++) {
          extractedValues.add(Array.get(group, i));
        }
      } else if (group instanceof Iterable) {
        Iterable<?> iterable = (Iterable<?>) group;
        for (Object value : iterable) {
          extractedValues.add(value);
        }
      } else {
        CommonErrors.wrongElementTypeForFlatExtracting(group);
      }
    }
    return new ListAssert<>(extractedValues);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public S containsExactlyElementsOf(Iterable<? extends T> iterable) {
    return containsExactly(toArray(iterable));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public S containsOnlyElementsOf(Iterable<? extends T> iterable) {
    return containsOnly(toArray(iterable));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public S hasSameElementsAs(Iterable<? extends T> iterable) {
    return containsOnlyElementsOf(iterable);
  }

  /**
   * Use field by field comparison (including inherited fields) instead of relying on actual type A <code>equals</code>
   * method to compare group elements for incoming assertion checks.
   * <p/>
   * This can be handy if <code>equals</code> method of the objects to compare does not suit you.
   * <p/>
   * Note that only <b>accessible </b>fields values are compared, accessible fields include directly accessible fields
   * (e.g. public) or fields with an accessible getter.<br/>
   * Moreover comparison is <b>not</b> recursive, if one of the field is an Object, it will be compared to the other
   * field using its <code>equals</code> method.
   * </p>
   * Example:
   *
   * <pre><code class='java'>
   * TolkienCharacter frodo = new TolkienCharacter("Frodo", 33, HOBBIT);
   * TolkienCharacter frodoClone = new TolkienCharacter("Frodo", 33, HOBBIT);
   * 
   * // Fail if equals has not been overridden in TolkienCharacter as equals default implementation only compares references
   * assertThat(newArrayList(frodo)).contains(frodoClone);
   * 
   * // frodo and frodoClone are equals when doing a field by field comparison.
   * assertThat(newArrayList(frodo)).usingFieldByFieldElementComparator().contains(frodoClone);
   * </code></pre>
   *
   * @return {@code this} assertion object.
   */
  public S usingFieldByFieldElementComparator() {
    return usingElementComparator(new FieldByFieldComparator());
  }

  /**
   * Use field by field comparison on the <b>given fields only</b> (fields can be inherited fields) instead of relying
   * on actual type A <code>equals</code> method to compare group elements for incoming assertion checks.
   * <p/>
   * This can be handy if <code>equals</code> method of the objects to compare does not suit you.
   * <p/>
   * Note that only <b>accessible </b>fields values are compared, accessible fields include directly accessible fields
   * (e.g. public) or fields with an accessible getter.<br/>
   * Moreover comparison is <b>not</b> recursive, if one of the field is an Object, it will be compared to the other
   * field using its <code>equals</code> method.
   * </p>
   * Example:
   *
   * <pre><code class='java'>
   * TolkienCharacter frodo = new TolkienCharacter("Frodo", 33, HOBBIT);
   * TolkienCharacter sam = new TolkienCharacter("Sam", 38, HOBBIT);
   * 
   * // frodo and sam both are hobbits, so they are equals when comparing only race
   * assertThat(newArrayList(frodo)).usingElementComparatorOnFields("race").contains(sam); // OK
   * 
   * // ... but not when comparing both name and race
   * assertThat(newArrayList(frodo)).usingElementComparatorOnFields("name", "race").contains(sam); // FAIL
   * </code></pre>
   *
   * @return {@code this} assertion object.
   */
  public S usingElementComparatorOnFields(String... fields) {
    return usingElementComparator(new OnFieldsComparator(fields));
  }

  protected S usingComparisonStrategy(ComparisonStrategy comparisonStrategy) {
    iterables = new Iterables(comparisonStrategy);
    return myself;
  }

  /**
   * Use field by field comparison on all fields <b>except</b> the given ones (fields can be inherited fields) instead
   * of relying on actual type A <code>equals</code> method to compare group elements for incoming assertion checks.
   * <p/>
   * This can be handy if <code>equals</code> method of the objects to compare does not suit you.
   * <p/>
   * Note that only <b>accessible </b>fields values are compared, accessible fields include directly accessible fields
   * (e.g. public) or fields with an accessible getter.<br/>
   * Moreover comparison is <b>not</b> recursive, if one of the field is an Object, it will be compared to the other
   * field using its <code>equals</code> method.
   * </p>
   * Example:
   *
   * <pre><code class='java'>
   * TolkienCharacter frodo = new TolkienCharacter("Frodo", 33, HOBBIT);
   * TolkienCharacter sam = new TolkienCharacter("Sam", 38, HOBBIT);
   * 
   * // frodo and sam both are hobbits, so they are equals when comparing only race (i.e. ignoring all other fields)
   * assertThat(newArrayList(frodo)).usingElementComparatorIgnoringFields("name", "age").contains(sam); // OK
   * 
   * // ... but not when comparing both name and race
   * assertThat(newArrayList(frodo)).usingElementComparatorIgnoringFields("age").contains(sam); // FAIL
   * </code></pre>
   *
   * @return {@code this} assertion object.
   */
  public S usingElementComparatorIgnoringFields(String... fields) {
    return usingElementComparator(new IgnoringFieldsComparator(fields));
  }

  /**
   * Enable hexadecimal representation of Iterable elements instead of standard representation in error messages.
   * <p/>
   * It can be useful to better understand what the error was with a more meaningful error message.
   * <p/>
   * Example
   * 
   * <pre><code class='java'>
   * final List&lt;Byte&gt; bytes = newArrayList((byte) 0x10, (byte) 0x20);
   * </code></pre>
   *
   * With standard error message:
   * 
   * <pre><code class='java'>
   * assertThat(bytes).contains((byte)0x30);
   * 
   * Expecting:
   *  <[16, 32]>
   * to contain:
   *  <[48]>
   * but could not find:
   *  <[48]>
   * </code></pre>
   *
   * With Hexadecimal error message:
   * 
   * <pre><code class='java'>
   * assertThat(bytes).inHexadecimal().contains((byte)0x30);
   * 
   * Expecting:
   *  <[0x10, 0x20]>
   * to contain:
   *  <[0x30]>
   * but could not find:
   *  <[0x30]>
   * </code></pre>
   *
   * @return {@code this} assertion object.
   */
  @Override
  public S inHexadecimal() {
    return super.inHexadecimal();
  }

  /**
   * Enable binary representation of Iterable elements instead of standard representation in error messages.
   * <p/>
   * Example:
   * 
   * <pre><code class='java'>
   * final List&lt;Byte&gt; bytes = newArrayList((byte) 0x10, (byte) 0x20);
   * </code></pre>
   *
   * With standard error message:
   * 
   * <pre><code class='java'>
   * assertThat(bytes).contains((byte)0x30);
   * 
   * Expecting:
   *  <[16, 32]>
   * to contain:
   *  <[48]>
   * but could not find:
   *  <[48]>
   * </code></pre>
   *
   * With binary error message:
   * 
   * <pre><code class='java'>
   * assertThat(bytes).inBinary().contains((byte)0x30);
   * 
   * Expecting:
   *  <[0b00010000, 0b00100000]>
   * to contain:
   *  <[0b00110000]>
   * but could not find:
   *  <[0b00110000]>
   * </code></pre>
   *
   * @return {@code this} assertion object.
   */
  @Override
  public S inBinary() {
    return super.inBinary();
  }
}
