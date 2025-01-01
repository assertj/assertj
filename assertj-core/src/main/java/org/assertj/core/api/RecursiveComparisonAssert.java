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
 * Copyright 2012-2025 the original author or authors.
 */
package org.assertj.core.api;

import static org.assertj.core.error.ShouldBeEqualByComparingFieldByFieldRecursively.shouldBeEqualByComparingFieldByFieldRecursively;
import static org.assertj.core.error.ShouldNotBeEqualComparingFieldByFieldRecursively.shouldNotBeEqualComparingFieldByFieldRecursively;

import java.util.Comparator;
import java.util.List;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.OptionalDouble;
import java.util.OptionalInt;
import java.util.OptionalLong;
import java.util.function.BiPredicate;

import org.assertj.core.api.recursive.comparison.ComparisonDifference;
import org.assertj.core.api.recursive.comparison.DefaultRecursiveComparisonIntrospectionStrategy;
import org.assertj.core.api.recursive.comparison.RecursiveComparator;
import org.assertj.core.api.recursive.comparison.RecursiveComparisonConfiguration;
import org.assertj.core.api.recursive.comparison.RecursiveComparisonDifferenceCalculator;
import org.assertj.core.api.recursive.comparison.RecursiveComparisonIntrospectionStrategy;
import org.assertj.core.internal.TypeComparators;
import org.assertj.core.util.CheckReturnValue;
import org.assertj.core.util.introspection.IntrospectionError;

public class RecursiveComparisonAssert<SELF extends RecursiveComparisonAssert<SELF>> extends AbstractAssert<SELF, Object> {

  private final RecursiveComparisonConfiguration recursiveComparisonConfiguration;
  private final RecursiveComparisonDifferenceCalculator recursiveComparisonDifferenceCalculator;

  public RecursiveComparisonAssert(Object actual, RecursiveComparisonConfiguration recursiveComparisonConfiguration) {
    super(actual, RecursiveComparisonAssert.class);
    this.recursiveComparisonConfiguration = recursiveComparisonConfiguration;
    this.recursiveComparisonDifferenceCalculator = new RecursiveComparisonDifferenceCalculator();
  }

  /**
   * Asserts that the object under test (actual) is equal to the given object when compared field by field recursively (including
   * inherited fields are included in the comparison). If the comparison fails it will report all the differences found and which
   * effective {@link RecursiveComparisonConfiguration} was used to help users understand the failure.
   * <p>
   * This is typically useful when actual's {@code equals} was not overridden.
   * <p>
   * The comparison is <b>not symmetrical</b> since it is <b>limited to actual's fields</b>, the algorithm gather all actual's fields
   * and then compare them to the corresponding expected's fields.<br>
   * It is then possible for the expected object to have more fields than actual which is handy when comparing a base type to a subtype.
   * <p>
   * <strong>Strict/lenient recursive comparison</strong>
   * <p>
   * By default, the objects to compare can be of different types but must have the same properties/fields. For example if object under test has a {@code work} field of type {@code Address},
   * the expected object to compare the object under test to must also have one, but it can of a different type like {@code AddressDto}.
   * <p>
   * It is possible to enforce strict type checking by calling {@link #withStrictTypeChecking()} and make the comparison fail whenever the compared objects or their fields are not compatible.<br>
   * Compatible means that the expected object/field types are the same or a subtype of actual/field types, for example if actual is an {@code Animal} and expected a {@code Dog}, they will be compared field by field in strict type checking mode.
   * <p>
   * <strong>Ignoring null fields in the recursive comparison</strong>
   * <p>
   * When an object is partially populated, it can still be interesting to see if its populated values are correct against a fully populated object.
   * <p>
   * This possible by calling {@link #ignoringActualNullFields()} before {@link #isEqualTo(Object) isEqualTo}
   * but bear in mind that <b>only actual null fields are ignored</b>, said otherwise the expected object null fields are used in the comparison.
   * <p>
   * <strong>Recursive comparison use of overridden {@code equals} methods</strong>
   * <p>
   * By default, the recursive comparison is <b>not</b> applied on fields whose classes have overridden the {@code equals} method,
   * concretely it means {@code equals} is used to compare these fields instead of keeping on applying the recursive comparison.
   * The rationale is that if a class has redefined {@code equals} then it should be used to compare instances unless having a good reason.
   * <p>
   * It is possible though to change this behavior and force recursive comparison by calling any of these methods (but before calling {@code isEqualTo} otherwise this has no effect!):
   * <ol>
   * <li> {@link #ignoringOverriddenEqualsForTypes(Class...)} Any fields of these classes are compared recursively</li>
   * <li> {@link #ignoringOverriddenEqualsForFields(String...)} Any given fields are compared recursively</li>
   * <li> {@link #ignoringOverriddenEqualsForFieldsMatchingRegexes(String...)} Any fields matching one of these regexes are compared recursively</li>
   * <li> {@link #ignoringAllOverriddenEquals()} except for java types, all fields are compared field by field recursively.</li>
   * </ol>
   * <strong>Recursive comparison and cycles</strong>
   * <p>
   * The recursive comparison handles cycles.
   * <p>
   * <strong>Comparator used in the recursive comparison</strong>
   * <p>
   * By default {@code floats} are compared with a precision of 1.0E-6 and {@code doubles} with 1.0E-15.
   * <p>
   * You can specify a custom comparator or equals BiPredicate per (nested) fields or type with the methods below (but before calling {@code isEqualTo} otherwise this has no effect!):
   * <ol>
   * <li> {@link #withEqualsForType(BiPredicate, Class)} for a given type</li>
   * <li> {@link #withEqualsForFields(BiPredicate, String...)} for one or multiple fields</li>
   * <li> {@link #withComparatorForType(Comparator, Class)} for a given type</li>
   * <li> {@link #withComparatorForFields(Comparator, String...)} for one or multiple fields</li>
   * </ol>
   * <p>
   * Note that field comparators always take precedence over type comparators.
   * <p>
   * <strong>Example</strong>
   * <p>
   * Here is a basic example with a default {@link RecursiveComparisonConfiguration}, you can find other examples for each of the methods changing the recursive comparison behavior
   * like {@link #ignoringFields(String...)}.
   * <pre><code class='java'> class Person {
   *   String name;
   *   double height;
   *   Home home = new Home();
   * }
   *
   * class Home {
   *   Address address = new Address();
   *   Date ownedSince;
   * }
   *
   * class Address {
   *   int number;
   *   String street;
   * }
   *
   * Person sherlock = new Person("Sherlock", 1.80);
   * sherlock.home.ownedSince = new Date(123);
   * sherlock.home.address.street = "Baker Street";
   * sherlock.home.address.number = 221;
   *
   * Person sherlock2 = new Person("Sherlock", 1.80);
   * sherlock2.home.ownedSince = new Date(123);
   * sherlock2.home.address.street = "Baker Street";
   * sherlock2.home.address.number = 221;
   *
   * // assertion succeeds as the data of both objects are the same.
   * assertThat(sherlock).usingRecursiveComparison()
   *                     .isEqualTo(sherlock2);</code></pre>
   *
   * @param expected the object to compare {@code actual} to.
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual object is {@code null}.
   * @throws AssertionError if the actual and the given objects are not deeply equal property/field by property/field.
   * @throws IntrospectionError if one property/field to compare cannot be found.
   */
  @Override
  public SELF isEqualTo(Object expected) {
    // deals with both actual and expected being null
    if (actual == expected) return myself;
    if (expected == null) {
      // for the assertion to pass, actual must be null, but this is not the case since actual != expected
      objects.assertNull(info, actual);
    }
    // at this point expected is not null, which means actual must not be null for the assertion to pass
    objects.assertNotNull(info, actual);
    // at this point, both actual and expected are not null, we can compare them recursively!
    List<ComparisonDifference> differences = determineDifferencesWith(expected);
    if (!differences.isEmpty()) throw objects.getFailures().failure(info, shouldBeEqualByComparingFieldByFieldRecursively(actual,
                                                                                                                          expected,
                                                                                                                          differences,
                                                                                                                          recursiveComparisonConfiguration,
                                                                                                                          info.representation()));
    return myself;
  }

  /**
   * Asserts that actual object is not equal to the given object based on a recursive property/field by property/field comparison
   * (including inherited ones).
   * <p>
   * This is typically useful when actual's {@code equals} was not overridden.
   * <p>
   * The comparison is <b>not symmetrical</b> since it is <b>limited to actual's fields</b>, the algorithm gather all
   * actual's fields and then compare them to the corresponding expected's fields.<br>
   * It is then possible for the expected object to have more fields than actual which is handy when comparing
   * a base type to a subtype.
   * <p>
   * This method is based on {@link #isEqualTo(Object)}, you can check out more usages in that method.
   * <p>
   * Example
   * <pre><code class='java'> // equals not overridden in TolkienCharacter
   * TolkienCharacter frodo = new TolkienCharacter("Frodo", 33, HOBBIT);
   * TolkienCharacter frodoClone = new TolkienCharacter("Frodo", 33, HOBBIT);
   * TolkienCharacter youngFrodo = new TolkienCharacter("Frodo", 22, HOBBIT);
   *
   * // Pass as equals compares object references
   * assertThat(frodo).isNotEqualTo(frodoClone);
   *
   * // Fail as frodo and frodoClone are equals when doing a field by field comparison.
   * assertThat(frodo).usingRecursiveComparison()
   *                  .isNotEqualTo(frodoClone);
   *
   * // Pass as one the age fields differ between frodo and youngFrodo.
   * assertThat(frodo).usingRecursiveComparison()
   *                  .isNotEqualTo(youngFrodo);</code></pre>
   *
   * @param other the object to compare {@code actual} to.
   * @return {@code this} assertions object
   * @throws AssertionError if the actual object and the given objects are both {@code null}.
   * @throws AssertionError if the actual and the given objects are equals property/field by property/field recursively.
   * @see #isEqualTo(Object)
   * @since 3.17.0
   */
  @Override
  public SELF isNotEqualTo(Object other) {
    if (actual == other) throw objects.getFailures().failure(info, shouldNotBeEqualComparingFieldByFieldRecursively(actual, other,
                                                                                                                    recursiveComparisonConfiguration,
                                                                                                                    info.representation()));
    if (other != null && actual != null) {
      List<ComparisonDifference> differences = determineDifferencesWith(other);
      if (differences.isEmpty())
        throw objects.getFailures().failure(info, shouldNotBeEqualComparingFieldByFieldRecursively(actual, other,
                                                                                                   recursiveComparisonConfiguration,
                                                                                                   info.representation()));
    }
    // either actual or other was null (but not both) or there were no differences
    return myself;
  }

  /**
   * Verifies that the actual value is present in the given array of values, comparing values with the recursive comparison.
   * <p>
   * This assertion always fails if the given array of values is empty.
   * <p>
   * <strong>Example</strong>
   * <p>
   * <pre><code class='java'> class Person {
   *   String name;
   *   double height;
   *   Home home = new Home();
   * }
   *
   * class Home {
   *   Address address = new Address();
   *   Date ownedSince;
   * }
   *
   * class Address {
   *   int number;
   *   String street;
   * }
   *
   * Person sherlock = new Person("Sherlock", 1.80);
   * sherlock.home.ownedSince = new Date(123);
   * sherlock.home.address.street = "Baker Street";
   * sherlock.home.address.number = 221;
   *
   * Person sherlock2 = new Person("Sherlock", 1.80);
   * sherlock2.home.ownedSince = new Date(123);
   * sherlock2.home.address.street = "Baker Street";
   * sherlock2.home.address.number = 221;
   *
   * Person moriarty = new Person("Moriarty", 1.80);
   * moriarty.home.ownedSince = new Date(123);
   * moriarty.home.address.street = "Butcher Street";
   * moriarty.home.address.number = 221;
   *
   * // assertion succeeds as sherlock and sherlock2 data are the same.
   * assertThat(sherlock).usingRecursiveComparison()
   *                     .isIn(sherlock2, moriarty);</code></pre>
   *
   * @param values the given array to search the actual value in.
   * @return {@code this} assertion object.
   * @throws NullPointerException if the given array is {@code null}.
   * @throws AssertionError if the actual value is not present in the given array.
   */
  @Override
  public SELF isIn(Object... values) {
    usingRecursiveComparator();
    return super.isIn(values);
  }

  /**
   * Verifies that the actual value is present in the given iterable, comparing values with the recursive comparison.
   * <p>
   * This assertion always fails if the given iterable is empty.
   * <strong>Example</strong>
   * <p>
   * <pre><code class='java'> class Person {
   *   String name;
   *   double height;
   *   Home home = new Home();
   * }
   *
   * class Home {
   *   Address address = new Address();
   *   Date ownedSince;
   * }
   *
   * class Address {
   *   int number;
   *   String street;
   * }
   *
   * Person sherlock = new Person("Sherlock", 1.80);
   * sherlock.home.ownedSince = new Date(123);
   * sherlock.home.address.street = "Baker Street";
   * sherlock.home.address.number = 221;
   *
   * Person sherlock2 = new Person("Sherlock", 1.80);
   * sherlock2.home.ownedSince = new Date(123);
   * sherlock2.home.address.street = "Baker Street";
   * sherlock2.home.address.number = 221;
   *
   * Person moriarty = new Person("Moriarty", 1.80);
   * moriarty.home.ownedSince = new Date(123);
   * moriarty.home.address.street = "Butcher Street";
   * moriarty.home.address.number = 221;
   *
   * // assertion succeeds as sherlock and sherlock2 data are the same.
   * assertThat(sherlock).usingRecursiveComparison()
   *                     .isIn(Arrays.asList(sherlock2, moriarty));</code></pre>
   *
   * @param values the given iterable to search the actual value in.
   * @return {@code this} assertion object.
   * @throws NullPointerException if the given iterable is {@code null}.
   * @throws AssertionError if the actual value is not present in the given iterable.
   */
  @Override
  public SELF isIn(Iterable<?> values) {
    usingRecursiveComparator();
    return super.isIn(values);
  }

  /**
   * Verifies that the actual value is not present in the given array of values, comparing values with the recursive comparison.
   * <p>
   * This assertion always succeeds if the given array of values is empty.
   * <p>
   * <strong>Example</strong>
   * <p>
   * <pre><code class='java'> class Person {
   *   String name;
   *   double height;
   *   Home home = new Home();
   * }
   *
   * class Home {
   *   Address address = new Address();
   *   Date ownedSince;
   * }
   *
   * class Address {
   *   int number;
   *   String street;
   * }
   *
   * Person sherlock = new Person("Sherlock", 1.80);
   * sherlock.home.ownedSince = new Date(123);
   * sherlock.home.address.street = "Baker Street";
   * sherlock.home.address.number = 221;
   *
   * Person watson = new Person("Watson", 1.70);
   * watson.home.ownedSince = new Date(123);
   * watson.home.address.street = "Baker Street";
   * watson.home.address.number = 221;
   *
   * Person moriarty = new Person("Moriarty", 1.80);
   * moriarty.home.ownedSince = new Date(123);
   * moriarty.home.address.street = "Butcher Street";
   * moriarty.home.address.number = 221;
   *
   * // assertion succeeds as sherlock data is different from watson and moriarty
   * assertThat(sherlock).usingRecursiveComparison()
   *                     .isNotIn(watson, moriarty);</code></pre>
   *
   * @param values the given array to search the actual value in.
   * @return {@code this} assertion object.
   * @throws NullPointerException if the given array is {@code null}.
   * @throws AssertionError if the actual value is present in the given array.
   */
  @Override
  public SELF isNotIn(Object... values) {
    usingRecursiveComparator();
    return super.isNotIn(values);
  }

  /**
   * Verifies that the actual value is not present in the given iterable, comparing values with the recursive comparison.
   * <p>
   * This assertion always succeeds if the given iterable is empty.
   * <p>
   * <strong>Example</strong>
   * <p>
   * <pre><code class='java'> class Person {
   *   String name;
   *   double height;
   *   Home home = new Home();
   * }
   *
   * class Home {
   *   Address address = new Address();
   *   Date ownedSince;
   * }
   *
   * class Address {
   *   int number;
   *   String street;
   * }
   *
   * Person sherlock = new Person("Sherlock", 1.80);
   * sherlock.home.ownedSince = new Date(123);
   * sherlock.home.address.street = "Baker Street";
   * sherlock.home.address.number = 221;
   *
   * Person watson = new Person("Watson", 1.70);
   * watson.home.ownedSince = new Date(123);
   * watson.home.address.street = "Baker Street";
   * watson.home.address.number = 221;
   *
   * Person moriarty = new Person("Moriarty", 1.80);
   * moriarty.home.ownedSince = new Date(123);
   * moriarty.home.address.street = "Butcher Street";
   * moriarty.home.address.number = 221;
   *
   * // assertion succeeds as sherlock data is different from watson and moriarty
   * assertThat(sherlock).usingRecursiveComparison()
   *                     .isNotIn(Arrays.asList(watson, moriarty));</code></pre>
   *
   * @param values the given iterable to search the actual value in.
   * @return {@code this} assertion object.
   * @throws NullPointerException if the given iterable is {@code null}.
   * @throws AssertionError if the actual value is present in the given iterable.
   */
  @Override
  public SELF isNotIn(Iterable<?> values) {
    usingRecursiveComparator();
    return super.isNotIn(values);
  }

  /**
   * use a comparator performing a recursive comparison with the current recursiveComparisonConfiguration.
   */
  private void usingRecursiveComparator() {
    RecursiveComparator recursiveComparator = new RecursiveComparator(recursiveComparisonConfiguration);
    usingComparator(recursiveComparator, recursiveComparator.getDescription());
  }

  /**
   * Makes the recursive comparison to only compare given actual fields and their subfields (no other fields will be compared).
   * <p>
   * Specifying a field will make all its subfields to be compared, for example specifying {@code person} will lead to compare
   * {@code person.name}, {@code person.address} and all other Person fields.<br>
   * On the other hand if you specify {@code person.name}, {@code person} won't be compared but {@code person.name} will be.
   * <p>
   * The fields are specified by name, not by value, for example you can specify {@code person.name} but not {@code "Jack"}
   * as {@code "Jack"} is not a field value.
   * <p>
   * {@code comparingOnlyFields} can be combined with ignoring fields methods to restrict further the fields actually compared,
   * the resulting compared fields = {specified compared fields} {@code -} {specified ignored fields}.<br>For example if the specified compared fields = {"foo", "bar", "baz"}
   * and the ignored fields = {"bar"} then only {"foo", "baz"} fields will be compared.
   * <p>
   * Usage example:
   * <pre><code class='java'> class Person {
   *   String name;
   *   double height;
   *   Home home = new Home();
   * }
   *
   * class Home {
   *   Address address = new Address();
   * }
   *
   * class Address {
   *   int number;
   *   String street;
   * }
   *
   * Person sherlock = new Person("Sherlock", 1.80);
   * sherlock.home.address.street = "Baker Street";
   * sherlock.home.address.number = 221;
   *
   * Person moriarty = new Person("Moriarty", 1.80);
   * moriarty.home.address.street = "Butcher Street";
   * moriarty.home.address.number = 221;
   *
   * // assertion succeeds as name and home.address.street fields are not compared.
   * assertThat(sherlock).usingRecursiveComparison()
   *                     .comparingOnlyFields("height", "home.address.number")
   *                     .isEqualTo(moriarty);
   *
   * // assertion fails as home.address.street fields differ.
   * assertThat(sherlock).usingRecursiveComparison()
   *                     .comparingOnlyFields("height", "home")
   *                     .isEqualTo(moriarty);</code></pre>
   *
   * @param fieldNamesToCompare the fields of the object under test to compare in the comparison.
   * @return this {@link RecursiveComparisonAssert} to chain other methods.
   */
  public SELF comparingOnlyFields(String... fieldNamesToCompare) {
    recursiveComparisonConfiguration.compareOnlyFields(fieldNamesToCompare);
    return myself;
  }

  /**
   * Makes the recursive comparison to only compare given actual fields of the specified types and their subfields (no other fields will be compared).
   * <p>
   * Specifying a compared type will make any fields of this type and its subfields to be compared, for example specifying the {@code Person} type will
   * lead to compare {@code Person.name}, {@code Person.address} and all other {@code Person} fields.<br>
   * In case actual's field is null, expected's field type will be checked to match one of the given types (we assume actual and expected fields have the same type).
   * <p>
   * {@code comparingOnlyFieldsOfTypes} can be combined with {@link #comparingOnlyFields(String...)} to compare fields of the given types <b>or</b> names (union of both sets of fields).
   * <p>
   * {@code comparingOnlyFieldsOfTypes} can be also combined with ignoring fields to restrict further the fields actually compared,
   * the resulting compared fields = {specified compared fields of types} {@code -} {specified ignored fields}.<br>
   * For example, we specify the following compared types: {@code {String.class, Integer.class, Double.class}}, and the
   * object to compare has fields {@code String foo}, {@code Integer baz} and {@code Double bar},
   * if we ignore the {"bar"} field with {@link RecursiveComparisonAssert#ignoringFields(String...)} the comparison will only report differences on {@code {foo, baz}} fields.
   * <p>
   * Usage example:
   * <pre><code class='java'> class Person {
   *   String name;
   *   double height;
   *   Home home = new Home();
   * }
   *
   * class Home {
   *   Address address = new Address();
   * }
   *
   * class Address {
   *   int number;
   *   String street;
   * }
   *
   * Person sherlock = new Person("Sherlock", 1.80);
   * sherlock.home.address.street = "Baker Street";
   * sherlock.home.address.number = 221;
   *
   * Person moriarty = new Person("Moriarty", 1.80);
   * moriarty.home.address.street = "Butcher Street";
   * moriarty.home.address.number = 221;
   *
   *
   * // assertion succeeds as it only compared fields height and home.address.number since their types match compared types
   * assertThat(sherlock).usingRecursiveComparison()
   *                     .comparingOnlyFieldsOfTypes(Integer.class, Double.class)
   *                     .isEqualTo(moriarty);
   *
   * // assertion fails as home.address.street fields differ (Home fields and its subfields were compared)
   * assertThat(sherlock).usingRecursiveComparison()
   *                     .comparingOnlyFieldsOfTypes(Home.class)
   *                     .isEqualTo(moriarty);</code></pre>
   * <p>
   * Note that the recursive comparison checks whether the fields actually exist and throws an {@link IllegalArgumentException} if some of them don't,
   * this is done to catch typos.
   *
   * @param typesToCompare the types to compare in the recursive comparison.
   * @return this {@link RecursiveComparisonAssert} to chain other methods.
   */
  public SELF comparingOnlyFieldsOfTypes(Class<?>... typesToCompare) {
    recursiveComparisonConfiguration.compareOnlyFieldsOfTypes(typesToCompare);
    return myself;
  }

  /**
   * Makes the recursive comparison to ignore all <b>actual null fields</b> (but note that the expected object null fields are used in the comparison).
   * <p>
   * Example:
   * <pre><code class='java'> class Person {
   *   String name;
   *   double height;
   *   Home home = new Home();
   * }
   *
   * class Home {
   *   Address address = new Address();
   * }
   *
   * class Address {
   *   int number;
   *   String street;
   * }
   *
   * Person noName = new Person(null, 1.80);
   * noName.home.address.street = null;
   * noName.home.address.number = 221;
   *
   * Person sherlock = new Person("Sherlock", 1.80);
   * sherlock.home.address.street = "Baker Street";
   * sherlock.home.address.number = 221;
   *
   * // assertion succeeds as name and home.address.street fields are ignored in the comparison
   * assertThat(noName).usingRecursiveComparison()
   *                   .ignoringActualNullFields()
   *                   .isEqualTo(sherlock);
   *
   * // assertion fails as name and home.address.street fields are populated for sherlock but not for noName.
   * assertThat(sherlock).usingRecursiveComparison()
   *                     .ignoringActualNullFields()
   *                     .isEqualTo(noName);</code></pre>
   *
   * @return this {@link RecursiveComparisonAssert} to chain other methods.
   */
  @CheckReturnValue
  public SELF ignoringActualNullFields() {
    recursiveComparisonConfiguration.setIgnoreAllActualNullFields(true);
    return myself;
  }

  /**
   * Makes the recursive comparison to ignore all <b>actual empty optional fields</b> (including {@link Optional}, {@link OptionalInt}, {@link OptionalLong} and {@link OptionalDouble}),
   * note that the expected object empty optional fields are not ignored, this only applies to actual's fields.
   * <p>
   * Example:
   * <pre><code class='java'> class Person {
   *   String name;
   *   OptionalInt age;
   *   OptionalLong id;
   *   OptionalDouble height;
   *   Home home = new Home();
   * }
   *
   * class Home {
   *   String address;
   *   Optional&lt;String&gt; phone;
   * }
   *
   * Person homerWithoutDetails = new Person("Homer Simpson");
   * homerWithoutDetails.home.address.street = "Evergreen Terrace";
   * homerWithoutDetails.home.address.number = 742;
   * homerWithoutDetails.home.phone = Optional.empty();
   * homerWithoutDetails.age = OptionalInt.empty();
   * homerWithoutDetails.id = OptionalLong.empty();
   * homerWithoutDetails.height = OptionalDouble.empty();
   *
   * Person homerWithDetails = new Person("Homer Simpson");
   * homerWithDetails.home.address.street = "Evergreen Terrace";
   * homerWithDetails.home.address.number = 742;
   * homerWithDetails.home.phone = Optional.of("(939) 555-0113");
   * homerWithDetails.age = OptionalInt.of(39);
   * homerWithDetails.id = OptionalLong.of(123456);
   * homerWithDetails.height = OptionalDouble.of(1.83);
   *
   * // assertion succeeds as phone is ignored in the comparison
   * assertThat(homerWithoutDetails).usingRecursiveComparison()
   *                                .ignoringActualEmptyOptionalFields()
   *                                .isEqualTo(homerWithDetails);
   *
   * // assertion fails as phone, age, id and height are not ignored and are populated for homerWithDetails but not for homerWithoutDetails.
   * assertThat(homerWithDetails).usingRecursiveComparison()
   *                             .ignoringActualEmptyOptionalFields()
   *                             .isEqualTo(homerWithoutDetails);</code></pre>
   *
   * @return this {@link RecursiveComparisonAssert} to chain other methods.
   */
  @CheckReturnValue
  public SELF ignoringActualEmptyOptionalFields() {
    recursiveComparisonConfiguration.setIgnoreAllActualEmptyOptionalFields(true);
    return myself;
  }

  /**
   * Makes the recursive comparison to ignore all <b>expected null fields</b>.
   * <p>
   * Example:
   * <pre><code class='java'> class Person {
   *   String name;
   *   double height;
   *   Home home = new Home();
   * }
   *
   * class Home {
   *   Address address = new Address();
   * }
   *
   * class Address {
   *   int number;
   *   String street;
   * }
   *
   * Person sherlock = new Person("Sherlock", 1.80);
   * sherlock.home.address.street = "Baker Street";
   * sherlock.home.address.number = 221;
   *
   * Person noName = new Person(null, 1.80);
   * noName.home.address.street = null;
   * noName.home.address.number = 221;
   *
   * // assertion succeeds as name and home.address.street fields are ignored in the comparison
   * assertThat(sherlock).usingRecursiveComparison()
   *                     .ignoringExpectedNullFields()
   *                     .isEqualTo(noName);
   *
   * // assertion fails as name and home.address.street fields are populated for sherlock but not for noName.
   * assertThat(noName).usingRecursiveComparison()
   *                   .ignoringExpectedNullFields()
   *                   .isEqualTo(sherlock);</code></pre>
   *
   * @return this {@link RecursiveComparisonAssert} to chain other methods.
   */
  @CheckReturnValue
  public SELF ignoringExpectedNullFields() {
    recursiveComparisonConfiguration.setIgnoreAllExpectedNullFields(true);
    return myself;
  }

  /**
   * Makes the recursive comparison to ignore the given object under test fields. Nested fields can be specified like this: {@code home.address.street}.
   * <p>
   * The given fieldNamesToIgnore are matched against field names, not field values.
   * <p>
   * Example:
   * <pre><code class='java'> class Person {
   *   String name;
   *   double height;
   *   Home home = new Home();
   * }
   *
   * class Home {
   *   Address address = new Address();
   * }
   *
   * class Address {
   *   int number;
   *   String street;
   * }
   *
   * Person sherlock = new Person("Sherlock", 1.80);
   * sherlock.home.address.street = "Baker Street";
   * sherlock.home.address.number = 221;
   *
   * Person noName = new Person(null, 1.80);
   * noName.home.address.street = null;
   * noName.home.address.number = 221;
   *
   * // assertion succeeds as name and home.address.street fields are ignored in the comparison
   * assertThat(sherlock).usingRecursiveComparison()
   *                     .ignoringFields("name", "home.address.street")
   *                     .isEqualTo(noName);
   *
   * // assertion fails as home.address.street fields differ and is not ignored.
   * assertThat(sherlock).usingRecursiveComparison()
   *                     .ignoringFields("name")
   *                     .isEqualTo(noName);</code></pre>
   *
   * @param fieldNamesToIgnore the field names of the object under test to ignore in the comparison.
   * @return this {@link RecursiveComparisonAssert} to chain other methods.
   */
  @CheckReturnValue
  public SELF ignoringFields(String... fieldNamesToIgnore) {
    recursiveComparisonConfiguration.ignoreFields(fieldNamesToIgnore);
    return myself;
  }

  /**
   * Makes the recursive comparison to ignore the object under test fields whose name matches the given regexes.
   * <p>
   * Nested fields can be specified by using dots like this: {@code home\.address\.street} ({@code \} is used to escape
   * dots since they have a special meaning in regexes).
   * <p>
   * Example:
   * <pre><code class='java'> class Person {
   *   String name;
   *   double height;
   *   Home home = new Home();
   * }
   *
   * class Home {
   *   Address address = new Address();
   * }
   *
   * class Address {
   *   int number;
   *   String street;
   * }
   *
   * Person sherlock = new Person("Sherlock", 1.80);
   * sherlock.home.address.street = "Baker Street";
   * sherlock.home.address.number = 221;
   *
   * Person noName = new Person(null, 1.80);
   * noName.home.address.street = "Butcher Street";
   * noName.home.address.number = 222;
   *
   * // assertion succeeds as name and all home fields are ignored in the comparison
   * assertThat(sherlock).usingRecursiveComparison()
   *                     .ignoringFieldsMatchingRegexes("n.me", "home.*")
   *                     .isEqualTo(noName);
   *
   * // although home fields are ignored, assertion fails as name fields differ.
   * assertThat(sherlock).usingRecursiveComparison()
   *                     .ignoringFieldsMatchingRegexes("home.*")
   *                     .isEqualTo(noName);</code></pre>
   *
   * @param regexes regexes used to ignore fields in the comparison.
   * @return this {@link RecursiveComparisonAssert} to chain other methods.
   */
  @CheckReturnValue
  public SELF ignoringFieldsMatchingRegexes(String... regexes) {
    recursiveComparisonConfiguration.ignoreFieldsMatchingRegexes(regexes);
    return myself;
  }

  /**
   * Makes the recursive comparison to ignore the object under test fields of the given types.
   * <p>
   * The fields are ignored if their types <b>exactly match one of the ignored types</b>, for example,
   * if a field is a subtype of an ignored type it is not ignored.
   * <p>
   * If {@code strictTypeChecking} mode is disabled then null fields are ignored since their types cannot be known.
   * <p>
   * If {@code strictTypeChecking} mode is enabled and a field of the object under test is null, the recursive
   * comparison evaluates the corresponding expected field's type.
   * <p>
   * Example:
   * <pre><code class='java'> class Person {
   *   String name;
   *   double height;
   *   Home home = new Home();
   * }
   *
   * class Home {
   *   Address address = new Address();
   * }
   *
   * class Address {
   *   int number;
   *   String street;
   * }
   *
   * Person sherlock = new Person("Sherlock", 1.80);
   * sherlock.home.address.street = "Baker Street";
   * sherlock.home.address.number = 221;
   *
   * Person sherlock2 = new Person("Sherlock", 1.90);
   * sherlock2.home.address.street = "Butcher Street";
   * sherlock2.home.address.number = 221;
   *
   * // assertion succeeds as we ignore Address and height
   * assertThat(sherlock).usingRecursiveComparison()
   *                     .ignoringFieldsOfTypes(double.class, Address.class)
   *                     .isEqualTo(sherlock2);
   *
   * // now this assertion fails as expected since the home.address.street fields and height differ
   * assertThat(sherlock).usingRecursiveComparison()
   *                     .isEqualTo(sherlock2);</code></pre>
   *
   * @param typesToIgnore the types we want to ignore in the object under test fields.
   * @return this {@link RecursiveComparisonAssert} to chain other methods.
   */
  public RecursiveComparisonAssert<?> ignoringFieldsOfTypes(Class<?>... typesToIgnore) {
    recursiveComparisonConfiguration.ignoreFieldsOfTypes(typesToIgnore);
    return myself;
  }

  /**
   * Makes the recursive comparison to ignore the fields of the object under test having types matching one of the given regexes.
   * The fields are ignored if their types <b>exactly match one of the regexes</b>, if a field is a subtype of a matched type it is not ignored.
   * <p>
   * One use case of this method is to ignore types that can't be introspected.
   * <p>
   * If {@code strictTypeChecking} mode is enabled and a field of the object under test is null, the recursive
   * comparison evaluates the corresponding expected field's type (if not null), if it is disabled then the field is evaluated as
   * usual (i.e. it is not ignored).
   * <p>
   * <b>Warning</b>: primitive types are not directly supported because under the hood they are converted to their
   * corresponding wrapping types, for example {@code int} to {@code java.lang.Integer}. The preferred way to ignore
   * primitive types is to use {@link #ignoringFieldsOfTypes(Class[])}.
   * Another way is to ignore the wrapping type, for example ignoring {@code java.lang.Integer} ignores both
   * {@code java.lang.Integer} and {@code int} fields.
   * <p>
   * Example:
   * <pre><code class='java'> class Person {
   *   String name;
   *   double height;
   *   Home home = new Home();
   * }
   *
   * class Home {
   *   Address address = new Address();
   * }
   *
   * class Address {
   *   int number;
   *   String street;
   * }
   *
   * Person sherlock = new Person("Sherlock", 1.80);
   * sherlock.home.address.street = "Baker Street";
   * sherlock.home.address.number = 221;
   *
   * Person cherlock = new Person("Cherlock", 1.80);
   * cherlock.home.address.street = "Butcher Street";
   * cherlock.home.address.number = 221;
   *
   * // assertion succeeds as we ignore Address and height
   * assertThat(sherlock).usingRecursiveComparison()
   *                     .ignoringFieldsOfTypes(".*Address", "java\\.util\\.String")
   *                     .isEqualTo(cherlock);
   *
   * // now this assertion fails as expected since the home.address.street fields and name differ
   * assertThat(sherlock).usingRecursiveComparison()
   *                     .isEqualTo(cherlock);</code></pre>
   *
   * @param regexes regexes specifying the types to ignore.
   * @return this {@link RecursiveComparisonAssert} to chain other methods.
   */
  public RecursiveComparisonAssert<?> ignoringFieldsOfTypesMatchingRegexes(String... regexes) {
    recursiveComparisonConfiguration.ignoreFieldsOfTypesMatchingRegexes(regexes);
    return myself;
  }

  /**
   * This method instructs the recursive comparison to compare recursively all fields including the one whose type have overridden equals,
   * <b>except fields with java types</b> (at some point we need to compare something!).
   * <p>
   * Since 3.17.0 this is the default behavior for recursive comparisons, to revert to the previous behavior call {@link #usingOverriddenEquals()}.
   * <p>
   * Example:
   * <pre><code class='java'> class Person {
   *   String name;
   *   double height;
   *   Home home = new Home();
   * }
   *
   * class Home {
   *   Address address = new Address();
   * }
   *
   * class Address {
   *   int number;
   *   String street;
   *
   *   // only compares number, ouch!
   *   {@literal @}Override
   *   public boolean equals(final Object other) {
   *     if (!(other instanceof Address)) return false;
   *     Address castOther = (Address) other;
   *     return Objects.equals(number, castOther.number);
   *   }
   * }
   *
   * Person sherlock = new Person("Sherlock", 1.80);
   * sherlock.home.address.street = "Baker Street";
   * sherlock.home.address.number = 221;
   *
   * Person sherlock2 = new Person("Sherlock", 1.80);
   * sherlock2.home.address.street = "Butcher Street";
   * sherlock2.home.address.number = 221;
   *
   * // Assertion succeeds because:
   * // - overridden equals are used
   * // - Address has overridden equals and does not compare street fields.
   * assertThat(sherlock).usingRecursiveComparison()
   *                     .usingOverriddenEquals()
   *                     .isEqualTo(sherlock2);
   *
   * // To avoid using Address overridden equals, don't call usingOverriddenEquals() or call ignoringAllOverriddenEquals()
   * // (calling ignoringAllOverriddenEquals() is actually not required as this is the default behavior).
   * // This assertion fails as it will compare home.address.street fields which differ
   * assertThat(sherlock).usingRecursiveComparison()
   *                      //.ignoringAllOverriddenEquals() // not needed as this is the default
   *                     .isEqualTo(sherlock2);</code></pre>
   *
   * @return this {@link RecursiveComparisonAssert} to chain other methods.
   */
  public SELF ignoringAllOverriddenEquals() {
    recursiveComparisonConfiguration.ignoreAllOverriddenEquals();
    return myself;
  }

  /**
   * By default, the recursive comparison compare recursively all fields including the ones whose type have overridden equals
   * <b>except fields with java types</b> (at some point we need to compare something!).
   * <p>
   * This method instructs the recursive comparison to use overridden equals.
   * <p>
   * Example:
   * <pre><code class='java'> class Person {
   *   String name;
   *   double height;
   *   Home home = new Home();
   * }
   *
   * class Home {
   *   Address address = new Address();
   * }
   *
   * class Address {
   *   int number;
   *   String street;
   *
   *   // only compares number!
   *   {@literal @}Override
   *   public boolean equals(final Object other) {
   *     if (!(other instanceof Address)) return false;
   *     Address castOther = (Address) other;
   *     return Objects.equals(number, castOther.number);
   *   }
   * }
   *
   * Person sherlock = new Person("Sherlock", 1.80);
   * sherlock.home.address.street = "Baker Street";
   * sherlock.home.address.number = 221;
   *
   * Person sherlock2 = new Person("Sherlock", 1.80);
   * sherlock2.home.address.street = "Butcher Street";
   * sherlock2.home.address.number = 221;
   *
   * // assertion succeeds because Address equals does not compare street fields.
   * assertThat(sherlock).usingRecursiveComparison()
   *                     .usingOverriddenEquals()
   *                     .isEqualTo(sherlock2);
   *
   * // Assertion fails because:
   * // - Address equals is not used.
   * // - street fields are compared and differ.
   * assertThat(sherlock).usingRecursiveComparison()
   *                     .isEqualTo(sherlock2);</code></pre>
   *
   * @return this {@link RecursiveComparisonAssert} to chain other methods.
   * @since 3.17.0
   */
  public SELF usingOverriddenEquals() {
    recursiveComparisonConfiguration.useOverriddenEquals();
    return myself;
  }

  /**
   * In case you have instructed the recursive to use overridden {@code equals} with {@link #usingOverriddenEquals()},
   * this method allows ignoring overridden {@code equals} for the given fields (it adds them to the already registered ones).
   * <p>
   * Since 3.17.0 all overridden {@code equals} so this method is only relevant if you have called {@link #usingOverriddenEquals()} before.
   * <p>
   * Nested fields can be specified by using dots like this: {@code home.address.street}
   * <p>
   * Example:
   * <pre><code class='java'> class Person {
   *   String name;
   *   double height;
   *   Home home = new Home();
   * }
   *
   * class Home {
   *   Address address = new Address();
   * }
   *
   * class Address {
   *   int number;
   *   String street;
   *
   *   // only compares number
   *   {@literal @}Override
   *   public boolean equals(final Object other) {
   *     if (!(other instanceof Address)) return false;
   *     Address castOther = (Address) other;
   *     return Objects.equals(number, castOther.number);
   *   }
   * }
   *
   * Person sherlock = new Person("Sherlock", 1.80);
   * sherlock.home.address.street = "Baker Street";
   * sherlock.home.address.number = 221;
   *
   * Person sherlock2 = new Person("Sherlock", 1.80);
   * sherlock2.home.address.street = "Butcher Street";
   * sherlock2.home.address.number = 221;
   *
   * // Assertion succeeds because:
   * // - overridden equals are used
   * // - Address has overridden equals and does not compare street fields.
   * assertThat(sherlock).usingRecursiveComparison()
   *                     .usingOverriddenEquals()
   *                     .isEqualTo(sherlock2);
   *
   * // ignoringOverriddenEqualsForFields force a recursive comparison on the given field
   * // Assertion fails because:
   * // - Address equals is not used.
   * // - street fields are compared and differ.
   * assertThat(sherlock).usingRecursiveComparison()
   *                     .usingOverriddenEquals()
   *                     .ignoringOverriddenEqualsForFields("home.address")
   *                     .isEqualTo(sherlock2);</code></pre>
   *
   * @param fields the fields we want to force a recursive comparison on.
   * @return this {@link RecursiveComparisonAssert} to chain other methods.
   */
  @CheckReturnValue
  public SELF ignoringOverriddenEqualsForFields(String... fields) {
    recursiveComparisonConfiguration.ignoreOverriddenEqualsForFields(fields);
    return myself;
  }

  /**
   * By default, the recursive comparison uses overridden {@code equals} methods to compare fields,
   * this method allows forcing a recursive comparison for all fields of the given types (it adds them to the already registered ones).
   * <p>
   * Since 3.17.0 all overridden {@code equals} so this method is only relevant if you have called {@link #usingOverriddenEquals()} before.
   * <p>
   * Example:
   * <pre><code class='java'> class Person {
   *   String name;
   *   double height;
   *   Home home = new Home();
   * }
   *
   * class Home {
   *   Address address = new Address();
   * }
   *
   * class Address {
   *   int number;
   *   String street;
   *
   *   // only compares number, ouch!
   *   {@literal @}Override
   *   public boolean equals(final Object other) {
   *     if (!(other instanceof Address)) return false;
   *     Address castOther = (Address) other;
   *     return Objects.equals(number, castOther.number);
   *   }
   * }
   *
   * Person sherlock = new Person("Sherlock", 1.80);
   * sherlock.home.address.street = "Baker Street";
   * sherlock.home.address.number = 221;
   *
   * Person sherlock2 = new Person("Sherlock", 1.80);
   * sherlock2.home.address.street = "Butcher Street";
   * sherlock2.home.address.number = 221;
   *
   * // Assertion succeeds because:
   * // - overridden equals are used
   * // - Address has overridden equals and does not compare street fields.
   * assertThat(sherlock).usingRecursiveComparison()
   *                     .usingOverriddenEquals()
   *                     .isEqualTo(sherlock2);
   *
   * // ignoringOverriddenEqualsForTypes force a recursive comparison on the given types.
   * // Assertion fails because:
   * // - Address equals is not used.
   * // - street fields are compared and differ.
   * assertThat(sherlock).usingRecursiveComparison()
   *                     .usingOverriddenEquals()
   *                     .ignoringOverriddenEqualsForTypes(Address.class)
   *                     .isEqualTo(sherlock2);</code></pre>
   *
   * @param types the types we want to force a recursive comparison on.
   * @return this {@link RecursiveComparisonAssert} to chain other methods.
   */
  @CheckReturnValue
  public SELF ignoringOverriddenEqualsForTypes(Class<?>... types) {
    recursiveComparisonConfiguration.ignoreOverriddenEqualsForTypes(types);
    return myself;
  }

  /**
   * In case you have instructed the recursive comparison to use overridden {@code equals} with {@link #usingOverriddenEquals()},
   * this method allows forcing a recursive comparison for the fields matching the given regexes (it adds them to the already registered ones).
   * <p>
   * Since 3.17.0 all overridden {@code equals} so this method is only relevant if you have called {@link #usingOverriddenEquals()} before.
   * <p>
   * Nested fields can be specified by using dots like: {@code home\.address\.street} ({@code \} is used to escape
   * dots since they have a special meaning in regexes).
   * <p>
   * Example:
   * <pre><code class='java'> class Person {
   *   String name;
   *   double height;
   *   Home home = new Home();
   * }
   *
   * class Home {
   *   Address address = new Address();
   * }
   *
   * class Address {
   *   int number;
   *   String street;
   *
   *   // only compares number, ouch!
   *   {@literal @}Override
   *   public boolean equals(final Object other) {
   *     if (!(other instanceof Address)) return false;
   *     Address castOther = (Address) other;
   *     return Objects.equals(number, castOther.number);
   *   }
   * }
   *
   * Person sherlock = new Person("Sherlock", 1.80);
   * sherlock.home.address.street = "Baker Street";
   * sherlock.home.address.number = 221;
   *
   * Person sherlock2 = new Person("Sherlock", 1.80);
   * sherlock2.home.address.street = "Butcher Street";
   * sherlock2.home.address.number = 221;
   *
   * // assertion succeeds because overridden equals are used and thus street fields are mot compared
   * assertThat(sherlock).usingRecursiveComparison()
   *                     .usingOverriddenEquals()
   *                     .isEqualTo(sherlock2);
   *
   * // ignoringOverriddenEqualsForFields force a recursive comparison on the field matching the regex
   * // now this assertion fails as we expect since the home.address.street fields differ
   * assertThat(sherlock).usingRecursiveComparison()
   *                     .usingOverriddenEquals()
   *                     .ignoringOverriddenEqualsForFieldsMatchingRegexes("home.*")
   *                     .isEqualTo(sherlock2);</code></pre>
   *
   * @param regexes regexes used to specify the fields we want to force a recursive comparison on.
   * @return this {@link RecursiveComparisonAssert} to chain other methods.
   */
  @CheckReturnValue
  public SELF ignoringOverriddenEqualsForFieldsMatchingRegexes(String... regexes) {
    recursiveComparisonConfiguration.ignoreOverriddenEqualsForFieldsMatchingRegexes(regexes);
    return myself;
  }

  /**
   * Makes the recursive comparison to ignore collection order in all fields in the object under test.
   * <p>
   * <b>Important:</b> ignoring collection order has a high performance cost because each element of the actual collection must
   * be compared to each element of the expected collection which is an O(n&sup2;) operation. For example with a collection of 100
   * elements, the number of comparisons is 100x100 = 10 000!
   * <p>
   * Example:
   * <pre><code class='java'> class Person {
   *   String name;
   *   List&lt;Person&gt; friends = new ArrayList&lt;&gt;();
   * }
   *
   * Person sherlock1 = new Person("Sherlock Holmes");
   * sherlock1.friends.add(new Person("Dr. John Watson"));
   * sherlock1.friends.add(new Person("Molly Hooper"));
   *
   * Person sherlock2 = new Person("Sherlock Holmes");
   * sherlock2.friends.add(new Person("Molly Hooper"));
   * sherlock2.friends.add(new Person("Dr. John Watson"));
   *
   * // assertion succeeds as all fields collection order is ignored in the comparison
   * assertThat(sherlock1).usingRecursiveComparison()
   *                      .ignoringCollectionOrder()
   *                      .isEqualTo(sherlock2);
   *
   * // assertion fails as fields collection order is not ignored in the comparison
   * assertThat(sherlock1).usingRecursiveComparison()
   *                      .isEqualTo(sherlock2);</code></pre>
   *
   * @return this {@link RecursiveComparisonAssert} to chain other methods.
   */
  @CheckReturnValue
  public SELF ignoringCollectionOrder() {
    recursiveComparisonConfiguration.ignoreCollectionOrder(true);
    return myself;
  }

  /**
   * Makes the recursive comparison to ignore collection order in the object under test specified fields. Nested fields can be specified like this: {@code home.address.street}.
   * <p>
   * <b>Important:</b> ignoring collection order has a high performance cost because each element of the actual collection must
   * be compared to each element of the expected collection which is a O(n&sup2;) operation. For example with a collection of 100
   * elements, the number of comparisons is 100x100 = 10 000!
   * <p>
   * Example:
   * <pre><code class='java'> class Person {
   *   String name;
   *   List&lt;Person&gt; friends = new ArrayList&lt;&gt;();
   *   List&lt;Person&gt; enemies = new ArrayList&lt;&gt;();
   * }
   *
   * Person sherlock1 = new Person("Sherlock Holmes");
   * sherlock1.friends.add(new Person("Dr. John Watson"));
   * sherlock1.friends.add(new Person("Molly Hooper"));
   * sherlock1.enemies.add(new Person("Jim Moriarty"));
   * sherlock1.enemies.add(new Person("Irene Adler"));
   *
   * Person sherlock2 = new Person("Sherlock Holmes");
   * sherlock2.friends.add(new Person("Molly Hooper"));
   * sherlock2.friends.add(new Person("Dr. John Watson"));
   * sherlock2.enemies.add(new Person("Irene Adler"));
   * sherlock2.enemies.add(new Person("Jim Moriarty"));
   *
   * // assertion succeeds as friends and enemies fields collection order is ignored in the comparison
   * assertThat(sherlock1).usingRecursiveComparison()
   *                      .ignoringCollectionOrderInFields("friends", "enemies")
   *                      .isEqualTo(sherlock2);
   *
   * // assertion fails as enemies field collection order differ and it is not ignored
   * assertThat(sherlock1).usingRecursiveComparison()
   *                      .ignoringCollectionOrderInFields("friends")
   *                      .isEqualTo(sherlock2);</code></pre>
   *
   * @param fieldsToIgnoreCollectionOrder the fields of the object under test to ignore collection order in the comparison.
   * @return this {@link RecursiveComparisonAssert} to chain other methods.
   */
  @CheckReturnValue
  public SELF ignoringCollectionOrderInFields(String... fieldsToIgnoreCollectionOrder) {
    recursiveComparisonConfiguration.ignoreCollectionOrderInFields(fieldsToIgnoreCollectionOrder);
    return myself;
  }

  /**
   * Makes the recursive comparison to ignore collection order in the object under test fields matching the specified regexes.
   * <p>
   * Nested fields can be specified by using dots like this: {@code home\.address\.street} ({@code \} is used to escape
   * dots since they have a special meaning in regexes).
   * <p>
   * <b>Important:</b> ignoring collection order has a high performance cost because each element of the actual collection must
   * be compared to each element of the expected collection which is a O(n&sup2;) operation. For example with a collection of 100
   * elements, the number of comparisons is 100x100 = 10 000!
   * <p>
   * Example:
   * <pre><code class='java'> class Person {
   *   String name;
   *   List&lt;Person&gt; friends = new ArrayList&lt;&gt;();
   *   List&lt;Person&gt; enemies = new ArrayList&lt;&gt;();
   * }
   *
   * Person sherlock1 = new Person("Sherlock Holmes");
   * sherlock1.friends.add(new Person("Dr. John Watson"));
   * sherlock1.friends.add(new Person("Molly Hooper"));
   * sherlock1.enemies.add(new Person("Jim Moriarty"));
   * sherlock1.enemies.add(new Person("Irene Adler"));
   *
   * Person sherlock2 = new Person("Sherlock Holmes");
   * sherlock2.friends.add(new Person("Molly Hooper"));
   * sherlock2.friends.add(new Person("Dr. John Watson"));
   * sherlock2.enemies.add(new Person("Irene Adler"));
   * sherlock2.enemies.add(new Person("Jim Moriarty"));
   *
   * // assertion succeeds as friends and enemies fields collection order is ignored in the comparison
   * assertThat(sherlock1).usingRecursiveComparison()
   *                      .ignoringCollectionOrderInFieldsMatchingRegexes("friend.", "enemie.")
   *                      .isEqualTo(sherlock2);
   *
   * // assertion fails as enemies field collection order differ and it is not ignored
   * assertThat(sherlock1).usingRecursiveComparison()
   *                      .ignoringCollectionOrderInFields("friend.")
   *                      .isEqualTo(sherlock2);</code></pre>
   *
   * @param regexes regexes used to find the object under test fields to ignore collection order in the comparison.
   * @return this {@link RecursiveComparisonAssert} to chain other methods.
   */
  @CheckReturnValue
  public SELF ignoringCollectionOrderInFieldsMatchingRegexes(String... regexes) {
    recursiveComparisonConfiguration.ignoreCollectionOrderInFieldsMatchingRegexes(regexes);
    return myself;
  }

  /**
   * Makes the recursive comparison to check that actual's type is compatible with expected's type (and do the same for each field). <br>
   * Compatible means that the expected's type is the same or a subclass of actual's type.
   * <p>
   * Examples:
   * <pre><code class='java'> class Person {
   *   String name;
   *   double height;
   *   Person bestFriend;
   * }
   *
   * Person sherlock = new Person("Sherlock", 1.80);
   * sherlock.bestFriend = new Person("Watson", 1.70);
   *
   * Person sherlockClone = new Person("Sherlock", 1.80);
   * sherlockClone.bestFriend = new Person("Watson", 1.70);
   *
   * // assertion succeeds as sherlock and sherlockClone have the same data and types
   * assertThat(sherlock).usingRecursiveComparison()
   *                     .withStrictTypeChecking()
   *                     .isEqualTo(sherlockClone);
   *
   * // Let's now define a data structure similar to Person
   *
   * public class PersonDTO {
   *   String name;
   *   double height;
   *   PersonDTO bestFriend;
   * }
   *
   * PersonDTO sherlockDto = new PersonDTO("Sherlock", 1.80);
   * sherlockDto.bestFriend = new PersonDTO("Watson", 1.70);
   *
   * // assertion fails as Person and PersonDTO are not compatible even though they have the same data
   * assertThat(sherlock).usingRecursiveComparison()
   *                     .withStrictTypeChecking()
   *                     .isEqualTo(noName);
   *
   * // Let's define a subclass of Person
   *
   * public class Detective extends Person {
   *   boolean busy;
   * }
   *
   * Detective detectiveSherlock = new Detective("Sherlock", 1.80);
   * detectiveSherlock.bestFriend = new Person("Watson", 1.70);
   * detectiveSherlock.busy = true;
   *
   * // assertion succeeds as Detective inherits from Person and
   * // only Person's fields are included into the comparison.
   * assertThat(sherlock).usingRecursiveComparison()
   *                     .withStrictTypeChecking()
   *                     .isEqualTo(detectiveSherlock);</code></pre>
   *
   * @return this {@link RecursiveComparisonAssert} to chain other methods.
   */
  @CheckReturnValue
  public SELF withStrictTypeChecking() {
    recursiveComparisonConfiguration.strictTypeChecking(true);
    return myself;
  }

  /**
   * Allows registering a {@link BiPredicate} to compare fields with the given locations.
   * A typical usage is to compare double/float fields with a given precision.
   * <p>
   * BiPredicates specified with this method have precedence over the ones registered with {@link #withEqualsForType(BiPredicate, Class)}
   * or the comparators registered with {@link #withComparatorForType(Comparator, Class)}.
   * <p>
   * Note that registering a {@link BiPredicate} for a given field will override the previously registered one (if any).
   * <p>
   * The field locations must be specified from the root object,
   * for example if {@code Foo} has a {@code Bar} field which has an {@code id}, one can register to a comparator for Bar's {@code id} by calling:
   * <pre><code class='java'> withEqualsForFields(idBiPredicate, "foo.id", "foo.bar.id")</code></pre>
   * <p>
   * Complete example:
   * <pre><code class='java'> public class TolkienCharacter {
   *   String name;
   *   double height;
   * }
   *
   * TolkienCharacter frodo = new TolkienCharacter(&quot;Frodo&quot;, 1.2);
   * TolkienCharacter tallerFrodo = new TolkienCharacter(&quot;Frodo&quot;, 1.3);
   * TolkienCharacter reallyTallFrodo = new TolkienCharacter(&quot;Frodo&quot;, 1.9);
   *
   * BiPredicate&lt;Double, Double&gt; closeEnough = (d1, d2) -&gt; Math.abs(d1 - d2) &lt;= 0.5;
   *
   * // assertion succeeds
   * assertThat(frodo).usingRecursiveComparison()
   *                  .withEqualsForFields(closeEnough, &quot;height&quot;)
   *                  .isEqualTo(tallerFrodo);
   *
   * // assertion fails
   * assertThat(frodo).usingRecursiveComparison()
   *                  .withEqualsForFields(closeEnough, &quot;height&quot;)
   *                  .isEqualTo(reallyTallFrodo);</code></pre>
   *
   * @param equals the {@link BiPredicate} to use to compare the given fields
   * @param fieldLocations the location from the root object of the fields the BiPredicate should be used for
   *
   * @return this {@link RecursiveComparisonAssert} to chain other methods.
   * @throws NullPointerException if the given BiPredicate is null.
   */
  @CheckReturnValue
  public SELF withEqualsForFields(BiPredicate<?, ?> equals, String... fieldLocations) {
    recursiveComparisonConfiguration.registerEqualsForFields(equals, fieldLocations);
    return myself;
  }

  /**
   * Allows registering a {@link BiPredicate} to compare fields whose location matches the given regexes.
   * A typical usage is to compare double/float fields with a given precision.
   * <p>
   * The fields are evaluated from the root object, for example if {@code Foo} has a {@code Bar} field and both have an {@code id} field,
   * one can register a BiPredicate for Foo and Bar's {@code id} by calling:
   * <pre><code class='java'> withEqualsForFieldsMatchingRegexes(idBiPredicate, ".*id")</code></pre>
   * or
   * <pre><code class='java'> withEqualsForFieldsMatchingRegexes(idBiPredicate, "foo.*id")</code></pre>
   * <p>
   * BiPredicates registered with this method have precedence over the ones registered with {@link #withEqualsForType(BiPredicate, Class)}
   * or the comparators registered with {@link #withComparatorForType(Comparator, Class)} but don't have precedence over the
   * ones registered with exact location match: {@link #withEqualsForFields(BiPredicate, String...)} or {@link #withComparatorForFields(Comparator, String...)}
   * <p>
   * If registered regexes for different {@link BiPredicate} match a given field, the latest registered regexes {@link BiPredicate} wins.
   * <p>
   * Example:
   * <pre><code class='java'> class TolkienCharacter {
   *   String name;
   *   double height;
   *   double weight;
   * }
   *
   * TolkienCharacter frodo = new TolkienCharacter(&quot;Frodo&quot;, 1.2, 40);
   * TolkienCharacter tallerFrodo = new TolkienCharacter(&quot;Frodo&quot;, 1.3, 40.5);
   * TolkienCharacter hugeFrodo = new TolkienCharacter(&quot;Frodo&quot;, 1.9, 45);
   *
   * BiPredicate&lt;Double, Double&gt; closeEnough = (d1, d2) -&gt; Math.abs(d1 - d2) &lt;= 0.5;
   *
   * // assertion succeeds
   * assertThat(frodo).usingRecursiveComparison()
   *                  .withEqualsForFieldsMatchingRegexes(closeEnough, &quot;.eight&quot;)
   *                  .isEqualTo(tallerFrodo);
   *
   * // assertion fails
   * assertThat(frodo).usingRecursiveComparison()
   *                  .withEqualsForFieldsMatchingRegexes(closeEnough, &quot;.eight&quot;)
   *                  .isEqualTo(hugeFrodo);</code></pre>
   *
   * @param equals the {@link BiPredicate} to use to compare the fields matching the given regexes
   * @param regexes the regexes from the root object of the field locations the BiPredicate should be used for
   *
   * @return this {@link RecursiveComparisonAssert} to chain other methods.
   * @throws NullPointerException if the given BiPredicate is null.
   * @since 3.24.0
   */
  public SELF withEqualsForFieldsMatchingRegexes(BiPredicate<?, ?> equals, String... regexes) {
    recursiveComparisonConfiguration.registerEqualsForFieldsMatchingRegexes(equals, regexes);
    return myself;
  }

  /**
   * Allows registering a comparator to compare fields with the given locations.
   * A typical usage is to compare double/float fields with a given precision.
   * <p>
   * Comparators registered with this method have precedence over comparators registered with {@link #withComparatorForType(Comparator, Class)}
   * or {@link BiPredicate} registered with {@link #withEqualsForType(BiPredicate, Class)}.
   * <p>
   * The field locations must be specified from the root object,
   * for example if {@code Foo} has a {@code Bar} field which has an {@code id}, one can register to a comparator for Bar's {@code id} by calling:
   * <pre><code class='java'> withComparatorForFields(idComparator, "foo.id", "foo.bar.id")</code></pre>
   * <p>
   * Note that registering a {@link Comparator} for a given field will override the previously registered BiPredicate/Comparator (if any).
   * <p>
   * Complete example:
   * <pre><code class='java'> public class TolkienCharacter {
   *   String name;
   *   double height;
   * }
   *
   * TolkienCharacter frodo = new TolkienCharacter(&quot;Frodo&quot;, 1.2);
   * TolkienCharacter tallerFrodo = new TolkienCharacter(&quot;Frodo&quot;, 1.3);
   * TolkienCharacter reallyTallFrodo = new TolkienCharacter(&quot;Frodo&quot;, 1.9);
   *
   * Comparator&lt;Double&gt; closeEnough = (d1, d2) -&gt; Math.abs(d1 - d2) &lt;= 0.5 ? 0 : 1;
   *
   * // assertion succeeds
   * assertThat(frodo).usingRecursiveComparison()
   *                  .withComparatorForFields(closeEnough, &quot;height&quot;)
   *                  .isEqualTo(tallerFrodo);
   *
   * // assertion fails
   * assertThat(frodo).usingRecursiveComparison()
   *                  .withComparatorForFields(closeEnough, &quot;height&quot;)
   *                  .isEqualTo(reallyTallFrodo);</code></pre>
   *
   * @param comparator the {@link java.util.Comparator Comparator} to use to compare the given fields
   * @param fieldLocations the location from the root object of the fields the comparator should be used for
   * @return this {@link RecursiveComparisonAssert} to chain other methods.
   * @throws NullPointerException if the given comparator is null.
   */
  @CheckReturnValue
  public SELF withComparatorForFields(Comparator<?> comparator, String... fieldLocations) {
    recursiveComparisonConfiguration.registerComparatorForFields(comparator, fieldLocations);
    return myself;
  }

  /**
   * Allows registering a comparator to compare the fields with the given type.
   * A typical usage is to compare double/float fields with a given precision.
   * <p>
   * Comparators registered with this method have less precedence than comparators registered with {@link #withComparatorForFields(Comparator, String...) withComparatorForFields(Comparator, String...)}
   * or BiPredicate registered with {@link #withEqualsForFields(BiPredicate, String...) withEqualsForFields(BiPredicate, String...)}.
   * <p>
   * Note that registering a {@link Comparator} for a given type will override the previously registered BiPredicate/Comparator (if any).
   * <p>
   * Example:
   * <pre><code class='java'> public class TolkienCharacter {
   *   String name;
   *   double height;
   * }
   *
   * TolkienCharacter frodo = new TolkienCharacter(&quot;Frodo&quot;, 1.2);
   * TolkienCharacter tallerFrodo = new TolkienCharacter(&quot;Frodo&quot;, 1.3);
   * TolkienCharacter reallyTallFrodo = new TolkienCharacter(&quot;Frodo&quot;, 1.9);
   *
   * Comparator&lt;Double&gt; closeEnough = (d1, d2) -&gt; Math.abs(d1 - d2) &lt;= 0.5 ? 0 : 1;
   *
   * // assertion succeeds
   * assertThat(frodo).usingRecursiveComparison()
   *                  .withComparatorForType(closeEnough, Double.class)
   *                  .isEqualTo(tallerFrodo);
   *
   * // assertion fails
   * assertThat(frodo).usingRecursiveComparison()
   *                  .withComparatorForType(closeEnough, Double.class)
   *                  .isEqualTo(reallyTallFrodo);</code></pre>
   *
   * @param <T> the class type to register a comparator for
   * @param comparator the {@link java.util.Comparator Comparator} to use to compare the given fields
   * @param type the type to be compared with the given comparator.
   *
   * @return this {@link RecursiveComparisonAssert} to chain other methods.
   * @throws NullPointerException if the given comparator is null.
   */
  @CheckReturnValue
  public <T> SELF withComparatorForType(Comparator<? super T> comparator, Class<T> type) {
    recursiveComparisonConfiguration.registerComparatorForType(comparator, type);
    return myself;
  }

  /**
   * Allows registering a {@link BiPredicate} to compare the fields with the given type.
   * A typical usage is to compare double/float fields with a given precision.
   * <p>
   * BiPredicates registered with this method have less precedence than the one registered  with {@link #withEqualsForFields(BiPredicate, String...) withEqualsForFields(BiPredicate, String...)}
   * or comparators registered with {@link #withComparatorForFields(Comparator, String...) withComparatorForFields(Comparator, String...)}.
   * <p>
   * Note that registering a {@link BiPredicate} for a given type will override the previously registered BiPredicate/Comparator (if any).
   * <p>
   * Example:
   * <pre><code class='java'> public class TolkienCharacter {
   *   String name;
   *   double height;
   * }
   *
   * TolkienCharacter frodo = new TolkienCharacter(&quot;Frodo&quot;, 1.2);
   * TolkienCharacter tallerFrodo = new TolkienCharacter(&quot;Frodo&quot;, 1.3);
   * TolkienCharacter reallyTallFrodo = new TolkienCharacter(&quot;Frodo&quot;, 1.9);
   *
   * BiPredicate&lt;Double, Double&gt; closeEnough = (d1, d2) -&gt; Math.abs(d1 - d2) &lt;= 0.5;
   *
   * // assertion succeeds
   * assertThat(frodo).usingRecursiveComparison()
   *                  .withEqualsForType(closeEnough, Double.class)
   *                  .isEqualTo(tallerFrodo);
   *
   * // assertion fails
   * assertThat(frodo).usingRecursiveComparison()
   *                  .withEqualsForType(closeEnough, Double.class)
   *                  .isEqualTo(reallyTallFrodo);</code></pre>
   *
   * @param <T> the class type to register a BiPredicate for
   * @param equals the {@link BiPredicate} to use to compare the given fields
   * @param type the type to be compared with the given comparator.
   *
   * @return this {@link RecursiveComparisonAssert} to chain other methods.
   * @throws NullPointerException if the given BiPredicate is null.
   */
  @CheckReturnValue
  public <T> SELF withEqualsForType(BiPredicate<? super T, ? super T> equals, Class<T> type) {
    recursiveComparisonConfiguration.registerEqualsForType(equals, type);
    return myself;
  }

  /**
   * Overrides an error message which would be shown when differences in the given fields while comparison occurred
   * with the giving error message.
   * <p>
   * The fields must be specified from the root object, for example if {@code Foo} has a {@code Bar} field and both
   * have an {@code id} field, one can register a message for Foo and Bar's {@code id} by calling:
   * <pre><code class='java'> withErrorMessageForFields("some message", "foo.id", "foo.bar.id")</code></pre>
   * <p>
   * Messages registered with this method have precedence over the ones registered with {@link #withErrorMessageForType(String, Class)}.
   * <p>
   * In case of {@code null} as message the default error message will be used (See
   * {@link ComparisonDifference#DEFAULT_TEMPLATE}).
   * <p>
   * Example:
   * <pre><code class='java'> public class TolkienCharacter {
   *   String name;
   *   double height;
   * }
   *
   * TolkienCharacter frodo = new TolkienCharacter(&quot;Frodo&quot;, 1.2);
   * TolkienCharacter tallerFrodo = new TolkienCharacter(&quot;Frodon&quot;, 1.4);
   *
   * String message = &quot;The field 'height' differ.&quot;;
   *
   * // assertion fails
   * assertThat(frodo).usingRecursiveComparison()
   *                  .withErrorMessageForFields(message, "height")
   *                  .isEqualTo(tallerFrodo);</code></pre>
   * and the error will report the height field with the given overridden message instead of the one computed by AssertJ as with the name error:
   * <pre><code>
   * Expecting actual:
   *   TolkienCharacter [name=Frodo, height=1.2]
   * to be equal to:
   *   TolkienCharacter [name=Frodon, height=1.4]
   * when recursively comparing field by field, but found the following 2 differences:
   * 
   * The field 'height' differ.
   * 
   * field/property 'name' differ:
   * - actual value  : "Frodo"
   * - expected value: "Frodon"
   * 
   * The recursive comparison was performed with this configuration:
   * - no overridden equals methods were used in the comparison (except for java types)
   * - these types were compared with the following comparators:
   *   - java.lang.Double -&gt; DoubleComparator[precision=1.0E-15]
   *   - java.lang.Float -&gt; FloatComparator[precision=1.0E-6]
   *   - java.nio.file.Path -&gt; lexicographic comparator (Path natural order)
   * - actual and expected objects and their fields were compared field by field recursively even if they were not of the same type, this allows for example to compare a Person to a PersonDto (call strictTypeChecking(true) to change that behavior).
   * - these fields had overridden error messages:
   *   - height</code></pre>
   *                                                                                                  
   * @param message the error message that will be thrown when comparison error occurred.
   * @param fieldLocations the fields the error message should be used for.
   * @return this {@link RecursiveComparisonAssert} to chain other methods.
   */
  @CheckReturnValue
  public SELF withErrorMessageForFields(String message, String... fieldLocations) {
    recursiveComparisonConfiguration.registerErrorMessageForFields(message, fieldLocations);
    return myself;
  }

  /**
   * Overrides an error message which would be shown when differences for the giving type while comparison occurred with
   * the giving error message.
   * <p>
   * Message registered with this method have less precedence than the ones registered with {@link #withErrorMessageForFields(String, String...)}.
   * <p>
   * In case of {@code null} as message the default error message will be used (See
   * {@link ComparisonDifference#DEFAULT_TEMPLATE}).
   * <p>
   * Example:
   * <pre><code class='java'> public class TolkienCharacter {
   *   String name;
   *   double height;
   * }
   *
   * TolkienCharacter frodo = new TolkienCharacter(&quot;Frodo&quot;, 1.2);
   * TolkienCharacter tallerFrodo = new TolkienCharacter(&quot;Frodon&quot;, 1.4);
   *
   * String message = &quot;Double field differ.&quot;;
   *
   * // assertion fails
   * assertThat(frodo).usingRecursiveComparison()
   *                  .withErrorMessageForType(message, Double.class)
   *                  .isEqualTo(tallerFrodo);</code></pre>
   * and the error will report the height field with the given overridden message instead of the one computed by AssertJ as with the name error:
   * <pre><code>
   * Expecting actual:
   *   TolkienCharacter [name=Frodo, height=1.2]
   * to be equal to:
   *   TolkienCharacter [name=Frodon, height=1.4]
   * when recursively comparing field by field, but found the following 2 differences:
   * 
   * Double field differ.
   * 
   * field/property 'name' differ:
   * - actual value  : "Frodo"
   * - expected value: "Frodon"
   * 
   * The recursive comparison was performed with this configuration:
   * - no overridden equals methods were used in the comparison (except for java types)
   * - these types were compared with the following comparators:
   *   - java.lang.Double -&gt; DoubleComparator[precision=1.0E-15]
   *   - java.lang.Float -&gt; FloatComparator[precision=1.0E-6]
   *   - java.nio.file.Path -&gt; lexicographic comparator (Path natural order)
   * - actual and expected objects and their fields were compared field by field recursively even if they were not of the same type, this allows for example to compare a Person to a PersonDto (call strictTypeChecking(true) to change that behavior).
   * - these types had overridden error messages:
   *   - height</code></pre>
   *
   * @param message the error message that will be thrown when comparison error occurred.
   * @param type the type the error message should be used for.
   * @return this {@link RecursiveComparisonAssert} to chain other methods.
   */
  @CheckReturnValue
  public SELF withErrorMessageForType(String message, Class<?> type) {
    recursiveComparisonConfiguration.registerErrorMessageForType(message, type);
    return myself;
  }

  /**
   * Defines how objects are introspected in the recursive comparison, that is:
   * <ul>
   *  <li>how to traverse the graph of nodes to compare</li>
   *  <li>how to get a node value</li>
   *  </ul>
   * <p>
   * Default to {@link DefaultRecursiveComparisonIntrospectionStrategy} that introspects all fields (including inherited ones).
   *
   * @param introspectionStrategy the {@link RecursiveComparisonIntrospectionStrategy} to use
   * @return this {@link RecursiveComparisonAssert} to chain other methods.
   */
  @CheckReturnValue
  public SELF withIntrospectionStrategy(RecursiveComparisonIntrospectionStrategy introspectionStrategy) {
    recursiveComparisonConfiguration.setIntrospectionStrategy(introspectionStrategy);
    return myself;
  }

  /**
   * Allows the recursive comparison to compare an enum field against a String field and vice versa.
   * <p>
   * Example:
   * <pre><code class='java'> LightString actual = new LightString("GREEN");
   * Light expected = new Light(GREEN);
   *
   * // compares "GREEN" to GREEN
   * assertThat(actual).usingRecursiveComparison()
   *                   .withEnumStringComparison()
   *                   .isEqualTo(expected);
   *
   * // compares GREEN to "GREEN"
   * assertThat(expected).usingRecursiveComparison()
   *                     .withEnumStringComparison()
   *                     .isEqualTo(actual);</code></pre>
   * where {@code Light} and {@code LightString} are defined as:
   * <pre><code class='java'> class Light {
   *   Color color;
   *   Light(Color value) {
   *     this.color = value;
   *   }
   * }
   * 
   * class LightString {
   *   String color;
   *   LightString(String value) {
   *     this.color = value;
   *   }
   * }</code></pre>
   */
  @CheckReturnValue
  public SELF withEnumStringComparison() {
    recursiveComparisonConfiguration.allowComparingEnumAgainstString(true);
    return myself;
  }

  SELF withTypeComparators(TypeComparators typeComparators) {
    Optional.ofNullable(typeComparators)
            .map(TypeComparators::comparatorByTypes)
            .ifPresent(comparatorByTypes -> comparatorByTypes.forEach(this::registerComparatorForType));
    return myself;
  }

  @SuppressWarnings({ "unchecked", "rawtypes" })
  private void registerComparatorForType(Entry<Class<?>, Comparator<?>> entry) {
    withComparatorForType((Comparator) entry.getValue(), entry.getKey());
  }

  /**
   * Returns the {@link RecursiveComparisonConfiguration} currently used.
   *
   * @return the {@link RecursiveComparisonConfiguration} currently used
   */
  public RecursiveComparisonConfiguration getRecursiveComparisonConfiguration() {
    return recursiveComparisonConfiguration;
  }

  private List<ComparisonDifference> determineDifferencesWith(Object expected) {
    return recursiveComparisonDifferenceCalculator.determineDifferences(actual, expected, recursiveComparisonConfiguration);
  }
}
