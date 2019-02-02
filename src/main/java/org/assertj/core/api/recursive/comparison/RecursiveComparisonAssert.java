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
 * Copyright 2012-2018 the original author or authors.
 */
package org.assertj.core.api.recursive.comparison;

import static org.assertj.core.api.recursive.comparison.FieldLocation.fielLocation;
import static org.assertj.core.error.ShouldBeEqualByComparingFieldByFieldRecursively.shouldBeEqualByComparingFieldByFieldRecursively;

import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import org.assertj.core.api.WritableAssertionInfo;
import org.assertj.core.configuration.ConfigurationProvider;
import org.assertj.core.internal.Failures;
import org.assertj.core.internal.Objects;
import org.assertj.core.presentation.Representation;
import org.assertj.core.util.CheckReturnValue;
import org.assertj.core.util.VisibleForTesting;
import org.assertj.core.util.introspection.IntrospectionError;

public class RecursiveComparisonAssert {

  private Object actual;
  private Representation customRepresentation = ConfigurationProvider.CONFIGURATION_PROVIDER.representation();
  private RecursiveComparisonConfiguration recursiveComparisonConfiguration;
  private RecursiveComparisonDifferenceCalculator recursiveComparisonDifferenceCalculator;
  @VisibleForTesting
  WritableAssertionInfo assertionInfo;
  @VisibleForTesting
  Failures failures;
  @VisibleForTesting
  Objects objects;

  // TODO propagate assertion info from ...
  public RecursiveComparisonAssert(Object actual, RecursiveComparisonConfiguration recursiveComparisonConfiguration) {
    this.actual = actual;
    this.recursiveComparisonConfiguration = recursiveComparisonConfiguration;
    this.assertionInfo = new WritableAssertionInfo(customRepresentation);
    recursiveComparisonDifferenceCalculator = new RecursiveComparisonDifferenceCalculator();
    failures = Failures.instance();
    objects = Objects.instance();;
  }

  /** {@inheritDoc} */
  @CheckReturnValue
  public RecursiveComparisonAssert as(String description, Object... args) {
    assertionInfo.description(description, args);
    return this;
  }

  /**
   * Asserts that the object under test (actual) is equal to the given object when compared field by field recursively (including
   * inherited fields are included in the comparison).If the comparison fails it will report all the differences found and which
   * effective {@link RecursiveComparisonConfiguration} was used to help users understand the failure.
   * TODO add link to assertj website documentation
   * <p>
   * This is typically useful when actual's {@code equals} was not overridden.
   * <p>
   * The comparison is <b>not symmetrical</b> since it is limited to actual's fields, the algorithm firts gather all actual's fields
   * and then compare them to the corresponding expected's fields.
   * It is then possible for the expected object to have more fields than actual which is handy when comparing a base type to a subtype.
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
   * By default the recursive comparison is <b>not</b> applied on fields whose classes have an overridden {@code equals} implementation,
   * concretely it means {@code equals} is used to compare these fields instead of keeing on applying the recursive comparison.
   * The rationale is that if a class has redefined {@code equals} then it should be used to compare instances unless having a good reason.
   * <p>
   * It is possible though to change this behavior and force recursive comparison by calling any of these methods (but before calling {@code isEqualTo} otherwise this has no effect!):
   * <ol>
   * <li> {@link #ignoringOverriddenEqualsForTypes(Class...)} Any fields of these classes are compared recursively</li>
   * <li> {@link #ignoringOverriddenEqualsForFields(String...)} Any given fields are compared recursively</li>
   * <li> {@link #ignoringOverriddenEqualsForFieldsMatchingRegexes(String...)} Any fields matching one of these regexes are compared recursively</li>
   * </ol>
   * <p>
   * The recursive comparison handles cycles. By default {@code floats} are compared with a precision of 1.0E-6 and {@code doubles} with 1.0E-15.
   * <p>
   * You can specify a custom comparator per (nested) fields or type with respectively {@code #usingComparatorForFields(Comparator, String...) usingComparatorForFields(Comparator, String...)}
   * and {@code #usingComparatorForType(Comparator, Class)}.
   * <p>
   * The objects to compare can be of different types but must have the same properties/fields. For example if actual object has a name String field, it is expected the other object to also have one.
   * If an object has a field and a property with the same name, the property value will be used over the field.
   * <p>
   * Example:
   * <pre><code class='java'> public class Person {
   *   public String name;
   *   public double height;
   *   public Home home = new Home();
   *   public Person bestFriend;
   *   // constructor with name and height omitted for brevity
   * }
   *
   * public class Home {
   *   public Address address = new Address();
   * }
   *
   * public static class Address {
   *   public int number = 1;
   * }
   *
   * Person jack = new Person("Jack", 1.80);
   * jack.home.address.number = 123;
   *
   * Person jackClone = new Person("Jack", 1.80);
   * jackClone.home.address.number = 123;
   *
   * // cycle are handled in comparison
   * jack.bestFriend = jackClone;
   * jackClone.bestFriend = jack;
   *
   * // will fail as equals compares object references
   * assertThat(jack).isEqualTo(jackClone);
   *
   * // jack and jackClone are equals when doing a recursive field by field comparison
   * assertThat(jack).isEqualToComparingFieldByFieldRecursively(jackClone);
   *
   * // any type/field can be compared with a a specific comparator.
   * // let's change  jack's height a little bit
   * jack.height = 1.81;
   *
   * // assertion fails because of the height difference
   * // (the default precision comparison for double is 1.0E-15)
   * assertThat(jack).isEqualToComparingFieldByFieldRecursively(jackClone);
   *
   * // this succeeds because we allow a 0.5 tolerance on double
   * assertThat(jack).usingComparatorForType(new DoubleComparator(0.5), Double.class)
   *                 .isEqualToComparingFieldByFieldRecursively(jackClone);
   *
   * // you can set a comparator on specific fields (nested fields are supported)
   * assertThat(jack).usingComparatorForFields(new DoubleComparator(0.5), "height")
   *                 .isEqualToComparingFieldByFieldRecursively(jackClone);</code></pre>
   *
   * @param expected the object to compare {@code actual} to.
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual object is {@code null}.
   * @throws AssertionError if the actual and the given objects are not deeply equal property/field by property/field.
   * @throws IntrospectionError if one property/field to compare can not be found.
   */
  public RecursiveComparisonAssert isEqualTo(Object expected) {
    // deals with both actual and expected being null
    if (actual == expected) return this;
    if (expected == null) {
      // for the assertion to pass, actual must be null but this is not the case since actual != expected
      // => we fail expecting actual to be null
      objects.assertNull(assertionInfo, actual);
    }
    // at this point expected is not null, which means actual must not be null for the assertion to pass
    objects.assertNotNull(assertionInfo, actual);
    // at this point both actual and expected are not null, we can compare them recursively!
    List<ComparisonDifference> differences = determineDifferencesWith(expected);
    if (!differences.isEmpty()) throw failures.failure(assertionInfo, shouldBeEqualByComparingFieldByFieldRecursively(actual,
                                                                                                                      expected,
                                                                                                                      differences,
                                                                                                                      recursiveComparisonConfiguration,
                                                                                                                      assertionInfo.representation()));
    return this;
  }

  /**
   * Makes the recursive comparison to ignore all <b>actual null fields</b> (but note that the expected object null fields are used in the comparison).
   * <p>
   * Example:
   * <pre><code class='java'> public class Person {
   *   String name;
   *   double height;
   *   Home home = new Home();
   *   Person bestFriend;
   *   // constructor omitted for brevity
   * }
   *
   * public class Home {
   *   Address address = new Address();
   * }
   *
   * public static class Address {
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
   * // assertion fails as name and home.address.street fields are populated for sherlock but not noName.
   * assertThat(sherlock).usingRecursiveComparison()
   *                     .ignoringActualNullFields()
   *                     .isEqualTo(noName);</code></pre>
   *
   * @return this {@link RecursiveComparisonAssert} to chain other methods.
   */
  @CheckReturnValue
  public RecursiveComparisonAssert ignoringActualNullFields() {
    recursiveComparisonConfiguration.setIgnoreAllActualNullFields(true);
    return this;
  }

  /**
   * Makes the recursive comparison to ignore the given the object under test fields. Nested fields can be specified like this: {@code home.address.street}.
   * <p>
   * Example:
   * <pre><code class='java'> public class Person {
   *   String name;
   *   double height;
   *   Home home = new Home();
   * }
   *
   * public class Home {
   *   Address address = new Address();
   * }
   *
   * public static class Address {
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
   * @param fieldsToIgnore the fields of the object under test to ignore in the comparison.
   * @return this {@link RecursiveComparisonAssert} to chain other methods.
   */
  @CheckReturnValue
  public RecursiveComparisonAssert ignoringFields(String... fieldsToIgnore) {
    recursiveComparisonConfiguration.ignoreFields(fieldsToIgnore);
    return this;
  }

  /**
   * Makes the recursive comparison to ignore the object under test fields matching the given regexes.
   * <p>
   * Nested fields can be specified by using dots like this: {@code home\.address\.street} ({@code \} is used to escape
   * dots since they have a special meaning in regexes).
   * <p>
   * Example:
   * <pre><code class='java'> public class Person {
   *   String name;
   *   double height;
   *   Home home = new Home();
   * }
   *
   * public class Home {
   *   Address address = new Address();
   * }
   *
   * public static class Address {
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
  public RecursiveComparisonAssert ignoringFieldsMatchingRegexes(String... regexes) {
    recursiveComparisonConfiguration.ignoreFieldsMatchingRegexes(regexes);
    return this;
  }

  /**
   * By default the recursive comparison uses overridden {@code equals} methods to compare fields,
   * this method allows to force a recursive comparison for all fields at the exception of basic types (i.e. java.lang types)
   * - at some point we need to compare something!
   * <p>
   * For the recursive comparison to use the overridden {@code equals} of a given type anyway (like {@link Date}) you can register
   * a type comparator using {@link #withComparatorForType(Class, Comparator)}.
   * <p>
   * TODO introduce {@code ignoringAllOverriddenEqualsExceptFor(Class<?>... classes)} ?
   * <p>
   * Example:
   * <pre><code class='java'> public class Person {
   *   String name;
   *   double height;
   *   Home home = new Home();
   * }
   *
   * public class Home {
   *   Address address = new Address();
   * }
   *
   * public static class Address {
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
   * // assertion succeeds but that's not what we expected since the home.address.street fields differ
   * // but the equals implemenation in Address does not compare them.
   * assertThat(sherlock).usingRecursiveComparison()
   *                     .isEqualTo(sherlock2);
   *
   * // to avoid the previous issue, we force a recursive comparison on the home.address field
   * // now this assertion fails as we expect since the home.address.street fields differ
   * assertThat(sherlock).usingRecursiveComparison()
   *                     .ignoringAllOverriddenEquals()
   *                     .isEqualTo(sherlock2);</code></pre>
   *
   * @return this {@link RecursiveComparisonAssert} to chain other methods.
   */
  public RecursiveComparisonAssert ignoringAllOverriddenEquals() {
    recursiveComparisonConfiguration.ignoreAllOverriddenEquals();
    return this;
  }

  /**
   * By default the recursive comparison uses overridden {@code equals} methods to compare fields,
   * this method allows to force a recursive comparison for the given fields (it adds them to the already registered ones).
   * <p>
   * Nested fields can be specified by using dots like this: {@code home.address.street}
   * <p>
   * Example:
   * <pre><code class='java'> public class Person {
   *   String name;
   *   double height;
   *   Home home = new Home();
   * }
   *
   * public class Home {
   *   Address address = new Address();
   * }
   *
   * public static class Address {
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
   * // assertion succeeds but that's not what we expected since the home.address.street fields differ
   * // but the equals implemenation in Address does not compare them.
   * assertThat(sherlock).usingRecursiveComparison()
   *                     .isEqualTo(sherlock2);
   *
   * // to avoid the previous issue, we force a recursive comparison on the home.address field
   * // now this assertion fails as we expect since the home.address.street fields differ
   * assertThat(sherlock).usingRecursiveComparison()
   *                     .ignoringOverriddenEqualsForFields("home.address")
   *                     .isEqualTo(sherlock2);</code></pre>
   *
   * @param fields the fields we want to force a recursive comparison on.
   * @return this {@link RecursiveComparisonAssert} to chain other methods.
   */
  @CheckReturnValue
  public RecursiveComparisonAssert ignoringOverriddenEqualsForFields(String... fields) {
    recursiveComparisonConfiguration.ignoreOverriddenEqualsForFields(fields);
    return this;
  }

  /**
   * By default the recursive comparison uses overridden {@code equals} methods to compare fields,
   * this method allows to force a recursive comparison for all fields of the given types (it adds them to the already registered ones).
   * <p>
   * Example:
   * <pre><code class='java'> public class Person {
   *   String name;
   *   double height;
   *   Home home = new Home();
   * }
   *
   * public class Home {
   *   Address address = new Address();
   * }
   *
   * public static class Address {
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
   * // assertion succeeds but that's not what we expected since the home.address.street fields differ
   * // but the equals implemenation in Address does not compare them.
   * assertThat(sherlock).usingRecursiveComparison()
   *                     .isEqualTo(sherlock2);
   *
   * // to avoid the previous issue, we force a recursive comparison on the Address type
   * // now this assertion fails as we expect since the home.address.street fields differ
   * assertThat(sherlock).usingRecursiveComparison()
   *                     .ignoringOverriddenEqualsForTypes(Address.class)
   *                     .isEqualTo(sherlock2);</code></pre>
   *
   * @param types the types we want to force a recursive comparison on.
   * @return this {@link RecursiveComparisonAssert} to chain other methods.
   */
  @CheckReturnValue
  public RecursiveComparisonAssert ignoringOverriddenEqualsForTypes(Class<?>... types) {
    recursiveComparisonConfiguration.ignoreOverriddenEqualsForTypes(types);
    return this;
  }

  /**
   * By default the recursive comparison uses overridden {@code equals} methods to compare fields,
   * this method allows to force a recursive comparison for the fields matching the given regexes (it adds them to the already registered ones).
   * <p>
   * Nested fields can be specified by using dots like: {@code home\.address\.street} ({@code \} is used to escape
   * dots since they have a special meaning in regexes).
   * <p>
   * Example:
   * <pre><code class='java'> public class Person {
   *   String name;
   *   double height;
   *   Home home = new Home();
   * }
   *
   * public class Home {
   *   Address address = new Address();
   * }
   *
   * public static class Address {
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
   * // assertion succeeds but that's not what we expected since the home.address.street fields differ
   * // but the equals implemenation in Address does not compare them.
   * assertThat(sherlock).usingRecursiveComparison()
   *                     .isEqualTo(sherlock2);
   *
   * // to avoid the previous issue, we force a recursive comparison on home and its fields
   * // now this assertion fails as we expect since the home.address.street fields differ
   * assertThat(sherlock).usingRecursiveComparison()
   *                     .ignoringOverriddenEqualsForFieldsMatchingRegexes("home.*")
   *                     .isEqualTo(sherlock2);</code></pre>
   *
   * @param regexes regexes used to specify the fields we want to force a recursive comparison on.
   * @return this {@link RecursiveComparisonAssert} to chain other methods.
   */
  @CheckReturnValue
  public RecursiveComparisonAssert ignoringOverriddenEqualsForFieldsMatchingRegexes(String... regexes) {
    recursiveComparisonConfiguration.ignoreOverriddenEqualsForFieldsMatchingRegexes(regexes);
    return this;
  }

  /**
   * Makes the recursive comparison to check that actual's type is compatible with expected's type (and do the same for each field). <br>
   * Compatible means that the expected's type is the same or a subclass of actual's type.
   * <p>
   * Examples:
   * <pre><code class='java'> public class Person {
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
  public RecursiveComparisonAssert withStrictTypeChecking() {
    recursiveComparisonConfiguration.strictTypeChecking(true);
    return this;
  }

  @CheckReturnValue
  public RecursiveComparisonAssert withComparatorForField(String fieldLocation, Comparator<?> comparator) {
    recursiveComparisonConfiguration.registerComparatorForField(fielLocation(fieldLocation), comparator);
    return this;
  }

  @SafeVarargs
  @CheckReturnValue
  public final RecursiveComparisonAssert withComparatorForFields(Map.Entry<String, Comparator<?>>... comparatorByFields) {
    Stream.of(comparatorByFields).forEach(this::withComparatorForField);
    return this;
  }

  @CheckReturnValue
  public <T> RecursiveComparisonAssert withComparatorForType(Class<T> type, Comparator<? super T> comparator) {
    recursiveComparisonConfiguration.registerComparatorForType(type, comparator);
    return this;
  }

  // can't type Comparator/Class with <T> since each entry is about different types (no reason to be all related to T)
  @SuppressWarnings({ "rawtypes", "unchecked" })
  @SafeVarargs
  @CheckReturnValue
  public final RecursiveComparisonAssert withComparatorForTypes(Map.Entry<Class, Comparator>... comparatorByTypes) {
    Stream.of(comparatorByTypes)
          .forEach(comparatorByType -> withComparatorForType(comparatorByType.getKey(), comparatorByType.getValue()));
    return this;
  }

  @VisibleForTesting
  RecursiveComparisonConfiguration getRecursiveComparisonConfiguration() {
    return recursiveComparisonConfiguration;
  }

  private List<ComparisonDifference> determineDifferencesWith(Object expected) {
    return recursiveComparisonDifferenceCalculator.determineDifferences(actual, expected, recursiveComparisonConfiguration);
  }

  // syntactic sugar
  private RecursiveComparisonAssert withComparatorForField(Map.Entry<String, Comparator<?>> comparatorByField) {
    return withComparatorForField(comparatorByField.getKey(), comparatorByField.getValue());
  }

}
