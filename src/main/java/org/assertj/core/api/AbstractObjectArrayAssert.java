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

import static org.assertj.core.extractor.Extractors.*;
import static org.assertj.core.util.Lists.*;

import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;

import org.assertj.core.api.iterable.Extractor;
import org.assertj.core.data.Index;
import org.assertj.core.groups.FieldsOrPropertiesExtractor;
import org.assertj.core.groups.Tuple;
import org.assertj.core.internal.ComparatorBasedComparisonStrategy;
import org.assertj.core.internal.ObjectArrays;
import org.assertj.core.util.Iterables;
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
 * @author Mateusz Haligowski
 */
public abstract class AbstractObjectArrayAssert<S extends AbstractObjectArrayAssert<S, T>, T> extends
    AbstractAssert<S, T[]> implements IndexedObjectEnumerableAssert<AbstractObjectArrayAssert<S, T>, T>,
    ArraySortedAssert<AbstractObjectArrayAssert<S, T>, T> {

  @VisibleForTesting
  ObjectArrays arrays = ObjectArrays.instance();

  protected AbstractObjectArrayAssert(T[] actual, Class<?> selfType) {
	super(actual, selfType);
  }

  /**
   * {@inheritDoc}
   * 
   * @throws AssertionError {@inheritDoc}
   */
  @Override
  public AbstractObjectArrayAssert<S, T> isNullOrEmpty() {
    arrays.assertNullOrEmpty(info, actual);
    return myself;
  }

  /**
   * {@inheritDoc}
   * 
   * @throws AssertionError {@inheritDoc}
   */
  @Override
  public AbstractObjectArrayAssert<S, T> isEmpty() {
    arrays.assertEmpty(info, actual);
    return myself;
  }

  /**
   * {@inheritDoc}
   * 
   * @throws AssertionError {@inheritDoc}
   */
  @Override
  public S isNotEmpty() {
	arrays.assertNotEmpty(info, actual);
	return myself;
  }

  /**
   * {@inheritDoc}
   * 
   * @throws AssertionError {@inheritDoc}
   */
  @Override
  public S hasSize(int expected) {
	arrays.assertHasSize(info, actual, expected);
	return myself;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public S hasSameSizeAs(Object other) {
	// TODO same implementation as in AbstractArrayAssert, but can't inherit from it due to generics problem ...
	arrays.assertHasSameSizeAs(info, actual, other);
	return myself;
  }

  /** {@inheritDoc} */
  @Override
  public S hasSameSizeAs(Iterable<?> other) {
	arrays.assertHasSameSizeAs(info, actual, other);
	return myself;
  }

  /** {@inheritDoc} */
  @Override
  public S contains(T... values) {
	arrays.assertContains(info, actual, values);
	return myself;
  }

  /** {@inheritDoc} */
  @Override
  public S containsOnly(T... values) {
	arrays.assertContainsOnly(info, actual, values);
	return myself;
  }

  /** {@inheritDoc} */
  @Override
  public S containsOnlyOnce(T... values) {
	arrays.assertContainsOnlyOnce(info, actual, values);
	return myself;
  }

  /** {@inheritDoc} */
  @Override
  public S containsExactly(T... values) {
	objects.assertEqual(info, actual, values);
	return myself;
  }

  /** {@inheritDoc} */
  @Override
  public S containsSequence(T... sequence) {
	arrays.assertContainsSequence(info, actual, sequence);
	return myself;
  }

  /** {@inheritDoc} */
  @Override
  public S containsSubsequence(T... subsequence) {
	arrays.assertContainsSubsequence(info, actual, subsequence);
	return myself;
  }

  /** {@inheritDoc} */
  @Override
  public S contains(T value, Index index) {
	arrays.assertContains(info, actual, value, index);
	return myself;
  }

  /** {@inheritDoc} */
  @Override
  public S doesNotContain(T value, Index index) {
	arrays.assertDoesNotContain(info, actual, value, index);
	return myself;
  }

  /** {@inheritDoc} */
  @Override
  public S doesNotContain(T... values) {
	arrays.assertDoesNotContain(info, actual, values);
	return myself;
  }

  /** {@inheritDoc} */
  @Override
  public S doesNotHaveDuplicates() {
	arrays.assertDoesNotHaveDuplicates(info, actual);
	return myself;
  }

  /** {@inheritDoc} */
  @Override
  public S startsWith(T... sequence) {
	arrays.assertStartsWith(info, actual, sequence);
	return myself;
  }

  /** {@inheritDoc} */
  @Override
  public S endsWith(T... sequence) {
	arrays.assertEndsWith(info, actual, sequence);
	return myself;
  }

  /** {@inheritDoc} */
  @Override
  public S containsNull() {
	arrays.assertContainsNull(info, actual);
	return myself;
  }

  /** {@inheritDoc} */
  @Override
  public S doesNotContainNull() {
	arrays.assertDoesNotContainNull(info, actual);
	return myself;
  }

  /** {@inheritDoc} */
  @Override
  public S are(Condition<? super T> condition) {
	arrays.assertAre(info, actual, condition);
	return myself;
  }

  /** {@inheritDoc} */
  @Override
  public S areNot(Condition<? super T> condition) {
	arrays.assertAreNot(info, actual, condition);
	return myself;
  }

  /** {@inheritDoc} */
  @Override
  public S have(Condition<? super T> condition) {
	arrays.assertHave(info, actual, condition);
	return myself;
  }

  /** {@inheritDoc} */
  @Override
  public S doNotHave(Condition<? super T> condition) {
	arrays.assertDoNotHave(info, actual, condition);
	return myself;
  }

  /** {@inheritDoc} */
  @Override
  public S areAtLeast(int times, Condition<? super T> condition) {
	arrays.assertAreAtLeast(info, actual, times, condition);
	return myself;
  }

  /** {@inheritDoc} */
  @Override
  public S areAtLeastOne(Condition<? super T> condition) {
	areAtLeast(1, condition);
	return myself;
  }
  
  /** {@inheritDoc} */
  @Override
  public S areAtMost(int times, Condition<? super T> condition) {
	arrays.assertAreAtMost(info, actual, times, condition);
	return myself;
  }

  /** {@inheritDoc} */
  @Override
  public S areExactly(int times, Condition<? super T> condition) {
	arrays.assertAreExactly(info, actual, times, condition);
	return myself;
  }

  /** {@inheritDoc} */
  @Override
  public S haveAtLeastOne(Condition<? super T> condition) {
	return haveAtLeast(1, condition);
  }

  /** {@inheritDoc} */
  @Override
  public S haveAtLeast(int times, Condition<? super T> condition) {
	arrays.assertHaveAtLeast(info, actual, times, condition);
	return myself;
  }

  /** {@inheritDoc} */
  @Override
  public S haveAtMost(int times, Condition<? super T> condition) {
	arrays.assertHaveAtMost(info, actual, times, condition);
	return myself;
  }

  /** {@inheritDoc} */
  @Override
  public S haveExactly(int times, Condition<? super T> condition) {
	arrays.assertHaveExactly(info, actual, times, condition);
	return myself;
  }

  /** {@inheritDoc} */
  @Override
  public S hasAtLeastOneElementOfType(Class<?> type) {
	arrays.assertHasAtLeastOneElementOfType(info, actual, type);
	return myself;
  }

  /** {@inheritDoc} */
  @Override
  public S hasOnlyElementsOfType(Class<?> type) {
	arrays.assertHasOnlyElementsOfType(info, actual, type);
	return myself;
  }
  
  /** {@inheritDoc} */
  @Override
  public S isSorted() {
	arrays.assertIsSorted(info, actual);
	return myself;
  }

  /** {@inheritDoc} */
  @Override
  public S isSortedAccordingTo(Comparator<? super T> comparator) {
	arrays.assertIsSortedAccordingToComparator(info, actual, comparator);
	return myself;
  }

  /** {@inheritDoc} */
  @Override
  public S containsAll(Iterable<? extends T> iterable) {
	arrays.assertContainsAll(info, actual, iterable);
	return myself;
  }

  @Override
  public S usingElementComparator(Comparator<? super T> customComparator) {
	this.arrays = new ObjectArrays(new ComparatorBasedComparisonStrategy(customComparator));
	return myself;
  }

  @Override
  public S usingDefaultElementComparator() {
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
   * <pre><code class='java'>
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
   * </code></pre>
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
	Object[] values = FieldsOrPropertiesExtractor.extract(actual, byName(fieldOrProperty));
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
   * <pre><code class='java'>
   * // Build an array of TolkienCharacter, a TolkienCharacter has a name (String) and a Race (a class)
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
   * </code></pre>
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
	P[] values = (P[]) FieldsOrPropertiesExtractor.extract(actual, byName(fieldOrProperty));
	return new ObjectArrayAssert<P>(values);
  }

  /**
   * Extract the values of given fields/properties from the array's elements under test into a new array composed of
   * Tuple (a simple data structure), this new array becoming the array under test.
   * <p/>
   * It allows you to test fields/properties of the the array's elements instead of testing the elements themselves, it
   * can be sometimes much less work !
   * <p/>
   * The Tuple data corresponds to the extracted values of the given fields/properties, for instance if you ask to
   * extract "id", "name" and "email" then each Tuple data will be composed of id, name and email extracted from the
   * element of the initial array (the Tuple's data order is the same as the given fields/properties order).
   * <p/>
   * Let's take an example to make things clearer :
   * 
   * <pre><code class='java'>
   * // Build an array of TolkienCharacter, a TolkienCharacter has a name (String) and a Race (a class)
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
   * for, if no field accessible (ie. does not exist or is not public) an IntrospectionError is thrown.
   * <p/>
   * It only works if <b>all</b> objects have the field or all objects have the property with the given name, i.e. it
   * won't work if half of the objects have the field and the other the property.
   * <p/>
   * Note that the order of extracted property/field values is consistent with the iteration order of the array under
   * test.
   *
   * @param propertiesOrFields the properties/fields to extract from the initial array under test
   * @return a new assertion object whose object under test is the list of Tuple with extracted properties/fields values
   *         as data.
   * @throws IntrospectionError if one of the given name does not match a field or property (or field exists but is not
   *           public) in one of the initial Iterable's element.
   */
  public ObjectArrayAssert<Tuple> extracting(String... propertiesOrFields) {
	Object[] values = FieldsOrPropertiesExtractor.extract(actual, byName(propertiesOrFields));
	Tuple[] result = Arrays.copyOf(values, values.length, Tuple[].class);

	return new ObjectArrayAssert<Tuple>(result);
  }

  /**
   * Extract the values from Iterable's elements after test by applying an extracting function on them. The returned
   * iterable becomes a new object under test.
   * <p/>
   * It allows to test values from the elements in more safe way than by using {@link #extracting(String)}, as it
   * doesn't utilize introspection.
   * <p/>
   * Let's take a look an example
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
  public <U> ObjectArrayAssert<U> extracting(Extractor<? super T, U> extractor) {
	U[] extracted = FieldsOrPropertiesExtractor.extract(actual, extractor);

	return new ObjectArrayAssert<U>(extracted);
  }

  public <U, C extends Collection<U>> ObjectArrayAssert<U> flatExtracting(Extractor<? super T, C> extractor) {
	final List<C> extractedValues = FieldsOrPropertiesExtractor.extract(Arrays.asList(actual), extractor);

	final List<U> result = newArrayList();
	for (C e : extractedValues) {
	  result.addAll(e);
	}

	return new ObjectArrayAssert<U>(Iterables.toArray(result));
  }

  /**
   * Extract the result of given method invocation from the array's elements under test into a new array, this new array
   * becoming the array under test.
   * <p>
   * It allows you to test a method reslts of the array's elements instead of testing the elements themselves, it can be
   * sometimes much less work!
   * <p>
   * It is especially usefull for classes that does not conform to Java Bean's getter specification (i.e. public String
   * toString() or public String status() instead of public String getStatus()).
   * <p>
   * Let's take an example to make things clearer :
   * 
   * <pre><code class='java'>
   * // Build a array of WesterosHouse, a WesterosHouse has a method: public String sayTheWords()
   * WesterosHouse[] greatHousesOfWesteros = new WesterosHouse[] { new WesterosHouse(&quot;Stark&quot;, &quot;Winter is Comming&quot;),
   *     new WesterosHouse(&quot;Lannister&quot;, &quot;Hear Me Roar!&quot;), new WesterosHouse(&quot;Greyjoy&quot;, &quot;We Do Not Sow&quot;),
   *     new WesterosHouse(&quot;Baratheon&quot;, &quot;Our is the Fury&quot;), new WesterosHouse(&quot;Martell&quot;, &quot;Unbowed, Unbent, Unbroken&quot;),
   *     new WesterosHouse(&quot;Tyrell&quot;, &quot;Growing Strong&quot;) };
   * 
   * // let's verify the words of great houses in Westeros:
   * 
   * assertThat(greatHousesOfWesteros).extractingResultOf(&quot;sayTheWords&quot;)
   *                                  .contains(&quot;Winter is Comming&quot;, &quot;We Do Not Sow&quot;, &quot;Hear Me Roar&quot;)
   *                                  .doesNotContain(&quot;Lannisters always pay their debts&quot;);
   * </code></pre>
   * 
   * <p>
   * Following requirements have to be met to extract method results:
   * <ul>
   * <li>method has to be public,</li>
   * <li>method cannot accept any arguments,</li>
   * <li>method cannot return void.</li>
   * </ul>
   * <p>
   * Note that the order of extracted values is consistent with the order of the array under test.
   * 
   * @param method the name of the method which result is to be extracted from the array under test
   * @return a new assertion object whose object under test is the array of extracted values.
   * @throws IllegalArgumentException if no method exists with the given name, or method is not public, or method does
   *           return void, or method accepts arguments.
   */
  public ObjectArrayAssert<Object> extractingResultOf(String method) {
	Object[] values = FieldsOrPropertiesExtractor.extract(actual, resultOf(method));
	return new ObjectArrayAssert<Object>(values);
  }

  /**
   * Extract the result of given method invocation from the array's elements under test into a new array, this new array
   * becoming the array under test.
   * <p>
   * It allows you to test a method reslts of the array's elements instead of testing the elements themselves, it can be
   * sometimes much less work!
   * <p>
   * It is especially usefull for classes that does not conform to Java Bean's getter specification (i.e. public String
   * toString() or public String status() instead of public String getStatus()).
   * <p>
   * Let's take an example to make things clearer :
   * 
   * <pre><code class='java'>
   * // Build a array of WesterosHouse, a WesterosHouse has a method: public String sayTheWords()
   * WesterosHouse[] greatHousesOfWesteros = new WesterosHouse[] { new WesterosHouse(&quot;Stark&quot;, &quot;Winter is Comming&quot;),
   *     new WesterosHouse(&quot;Lannister&quot;, &quot;Hear Me Roar!&quot;), new WesterosHouse(&quot;Greyjoy&quot;, &quot;We Do Not Sow&quot;),
   *     new WesterosHouse(&quot;Baratheon&quot;, &quot;Our is the Fury&quot;), new WesterosHouse(&quot;Martell&quot;, &quot;Unbowed, Unbent, Unbroken&quot;),
   *     new WesterosHouse(&quot;Tyrell&quot;, &quot;Growing Strong&quot;) };
   * 
   * // let's verify the words of great houses in Westeros:
   * 
   * assertThat(greatHousesOfWesteros).extractingResultOf(&quot;sayTheWords&quot;, String.class)
   *                                  .contains(&quot;Winter is Comming&quot;, &quot;We Do Not Sow&quot;, &quot;Hear Me Roar&quot;)
   *                                  .doesNotContain(&quot;Lannisters always pay their debts&quot;);
   * </code></pre>
   * 
   * <p>
   * Following requirements have to be met to extract method results:
   * <ul>
   * <li>method has to be public,</li>
   * <li>method can not accept any arguments,</li>
   * <li>method can not return void.</li>
   * </ul>
   * <p>
   * Note that the order of extracted values is consistent with the order of the array under test.
   * 
   * @param method the name of the method which result is to be extracted from the array under test
   * @param extractingType type to return
   * @return a new assertion object whose object under test is the array of extracted values.
   * @throws IllegalArgumentException if no method exists with the given name, or method is not public, or method does
   *           return void, or method accepts arguments.
   */
  public <P> ObjectArrayAssert<P> extractingResultOf(String method, Class<P> extractingType) {
	@SuppressWarnings("unchecked")
	P[] values = (P[]) FieldsOrPropertiesExtractor.extract(actual, resultOf(method));
	return new ObjectArrayAssert<P>(values);
  }

  /**
   * Enable hexadecimal object representation of Itearble elements instead of standard java representation in error
   * messages.
   * <p/>
   * It can be useful to better understand what the error was with a more meaningful error message.
   * <p/>
   * Example
   * 
   * <pre><code class='java'>
   * assertThat(new Byte[] { 0x10, 0x20 }).inHexadecimal().contains(new Byte[] { 0x30 });
   * </code></pre>
   *
   * With standard error message:
   * 
   * <pre><code class='java'>
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

  @Override
  public S inBinary() {
	return super.inBinary();
  }

}
