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
 * Copyright 2012-2020 the original author or authors.
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

import org.assertj.core.api.recursive.comparison.ComparisonDifference;
import org.assertj.core.api.recursive.comparison.RecursiveComparisonConfiguration;
import org.assertj.core.api.recursive.comparison.RecursiveComparisonDifferenceCalculator;
import org.assertj.core.internal.Failures;
import org.assertj.core.internal.TypeComparators;
import org.assertj.core.util.CheckReturnValue;
import org.assertj.core.util.introspection.IntrospectionError;

public class RecursiveComparisonAssert<SELF extends RecursiveComparisonAssert<SELF>> extends AbstractAssert<SELF, Object> {

  private RecursiveComparisonConfiguration recursiveComparisonConfiguration;
  private RecursiveComparisonDifferenceCalculator recursiveComparisonDifferenceCalculator;

  public RecursiveComparisonAssert(Object actual, RecursiveComparisonConfiguration recursiveComparisonConfiguration) {
    super(actual, RecursiveComparisonAssert.class);
    this.recursiveComparisonConfiguration = recursiveComparisonConfiguration;
    recursiveComparisonDifferenceCalculator = new RecursiveComparisonDifferenceCalculator();
    Failures.instance();
  }

  void setRecursiveComparisonConfiguration(RecursiveComparisonConfiguration recursiveComparisonConfiguration) {
    this.recursiveComparisonConfiguration = recursiveComparisonConfiguration;
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
   * By default the objects to compare can be of different types but must have the same properties/fields. For example if object under test has a {@code work} field of type {@code Address},
   * the expected object to compare the object under test to must also have one but it can of a different type like {@code AddressDto}.
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
   * By default the recursive comparison is <b>not</b> applied on fields whose classes have overridden the {@code equals} method,
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
   * You can specify a custom comparator per (nested) fields or type with the methods below (but before calling {@code isEqualTo} otherwise this has no effect!):
   * <ol>
   * <li> {@link #withComparatorForFields(Comparator, String...) withComparatorForFields(Comparator, String...)} for one or multiple fields</li>
   * <li> {@link #withComparatorForType(Comparator, Class)} for a given type</li>
   * </ol>
   * <p>
   * Note that field comparators always take precedence over type comparators.
   * <p>
   * <strong>Example</strong>
   * <p>
   * Here is a basic example with a default {@link RecursiveComparisonConfiguration}, you can find other examples for each of the method changing the recursive comparison behavior
   * like {@link #ignoringFields(String...)}.
   * <pre><code class='java'> public class Person {
   *   String name;
   *   double height;
   *   Home home = new Home();
   * }
   *
   * public class Home {
   *   Address address = new Address();
   *   Date ownedSince;
   * }
   *
   * public static class Address {
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
   * @throws IntrospectionError if one property/field to compare can not be found.
   */
  @Override
  public SELF isEqualTo(Object expected) {
    // deals with both actual and expected being null
    if (actual == expected) return myself;
    if (expected == null) {
      // for the assertion to pass, actual must be null but this is not the case since actual != expected
      // => we fail expecting actual to be null
      objects.assertNull(info, actual);
    }
    // at this point expected is not null, which means actual must not be null for the assertion to pass
    objects.assertNotNull(info, actual);
    // at this point both actual and expected are not null, we can compare them recursively!
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
    if (actual == other) throw objects.getFailures().failure(info,
                                                             shouldNotBeEqualComparingFieldByFieldRecursively(actual, other,
                                                                                                              recursiveComparisonConfiguration,
                                                                                                              info.representation()));
    if (other != null && actual != null) {
      List<ComparisonDifference> differences = determineDifferencesWith(other);
      if (differences.isEmpty())
        throw objects.getFailures().failure(info,
                                            shouldNotBeEqualComparingFieldByFieldRecursively(actual, other,
                                                                                             recursiveComparisonConfiguration,
                                                                                             info.representation()));
    }
    // either one of actual or other was null (but not both) or there were no differences
    return myself;
  }

  /**
   * Makes the recursive comparison to ignore all <b>actual null fields</b> (but note that the expected object null fields are used in the comparison).
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
   * <pre><code class='java'> public class Person {
   *   String name;
   *   OptionalInt age;
   *   OptionalLong id;
   *   OptionalDouble height;
   *   Home home = new Home();
   * }
   *
   * public class Home {
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
  public SELF ignoringFields(String... fieldsToIgnore) {
    recursiveComparisonConfiguration.ignoreFields(fieldsToIgnore);
    return myself;
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
  public SELF ignoringFieldsMatchingRegexes(String... regexes) {
    recursiveComparisonConfiguration.ignoreFieldsMatchingRegexes(regexes);
    return myself;
  }

  /**
   * Makes the recursive comparison to ignore the object under test fields of the given types.
   * The fields are ignored if their types <b>exactly match one of the ignored types</b>, for example if a field is a subtype of an ignored type it is not ignored.
   * <p>
   * If some object under test fields are null it is not possible to evaluate their types unless in {@link #withStrictTypeChecking() strictTypeChecking mode},
   * in that case the corresponding expected field's type is evaluated instead but if strictTypeChecking mode is disabled then null fields are not ignored.
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
   * This method instructs the recursive comparison to compare recursively all fields including the one whose type have overridden equals,
   * <b>except fields with java types</b> (at some point we need to compare something!).
   * <p>
   * Since 3.17.0 this is the default behavior for recursive comparisons, to revert to the previous behavior call {@link #usingOverriddenEquals()}.
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
   * By default the recursive comparison compare recursively all fields including the ones whose type have overridden equals
   * <b>except fields with java types</b> (at some point we need to compare something!).
   * <p>
   * This method instructs the recursive comparison to use overridden equals.
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
   * this method allows to ignore overridden {@code equals} for the given fields (it adds them to the already registered ones).
   * <p>
   * Since 3.17.0 all overridden {@code equals} so this method is only relevant if you have called {@link #usingOverriddenEquals()} before.
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
   * By default the recursive comparison uses overridden {@code equals} methods to compare fields,
   * this method allows to force a recursive comparison for all fields of the given types (it adds them to the already registered ones).
   * <p>
   * Since 3.17.0 all overridden {@code equals} so this method is only relevant if you have called {@link #usingOverriddenEquals()} before.
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
   * In case you have instructed the recursive to use overridden {@code equals} with {@link #usingOverriddenEquals()},
   * this method allows to force a recursive comparison for the fields matching the given regexes (it adds them to the already registered ones).
   * <p>
   * Since 3.17.0 all overridden {@code equals} so this method is only relevant if you have called {@link #usingOverriddenEquals()} before.
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
   * Example:
   * <pre><code class='java'> public class Person {
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
   * Example:
   * <pre><code class='java'> public class Person {
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
   * Example:
   * <pre><code class='java'> public class Person {
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
  public SELF withStrictTypeChecking() {
    recursiveComparisonConfiguration.strictTypeChecking(true);
    return myself;
  }

  /**
   * Allows to register a specific comparator to compare fields with the given locations.
   * A typical usage is for comparing double/float fields with a given precision.
   * <p>
   * Comparators specified by this method have precedence over comparators added with {@link #withComparatorForType(Comparator, Class)}.
   * <p>
   * The field locations must be specified from the root object,
   * for example if {@code Foo} has a {@code Bar} field which has an {@code id}, one can register to a comparator for Bar's {@code id} by calling:
   * <pre><code class='java'> withComparatorForField("bar.id", idComparator)</code></pre>
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
   * // assertions succeed
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
   */
  @CheckReturnValue
  public SELF withComparatorForFields(Comparator<?> comparator, String... fieldLocations) {
    recursiveComparisonConfiguration.registerComparatorForFields(comparator, fieldLocations);
    return myself;
  }

  /**
   * Allows to register a specific comparator to compare the fields with the given type.
   * A typical usage is for comparing double/float fields with a given precision.
   * <p>
   * Comparators specified by this method have less precedence than comparators added with {@link #withComparatorForFields(Comparator, String...) withComparatorForFields(Comparator, String...)}.
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
   * // assertions succeed
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
   */
  @CheckReturnValue
  public <T> SELF withComparatorForType(Comparator<? super T> comparator, Class<T> type) {
    recursiveComparisonConfiguration.registerComparatorForType(comparator, type);
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
