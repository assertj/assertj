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
 * Copyright 2012-2019 the original author or authors.
 */
package org.assertj.core.error;

import static java.lang.Integer.toHexString;
import static java.lang.String.format;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.assertj.core.configuration.ConfigurationProvider.CONFIGURATION_PROVIDER;
import static org.assertj.core.error.ShouldBeEqualByComparingFieldByFieldRecursively.shouldBeEqualByComparingFieldByFieldRecursive;
import static org.assertj.core.error.ShouldBeEqualByComparingFieldByFieldRecursively.shouldBeEqualByComparingFieldByFieldRecursively;
import static org.assertj.core.internal.DeepDifference.determineDifferences;
import static org.assertj.core.util.AssertionsUtil.expectAssertionError;

import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.TreeMap;
import java.util.TreeSet;

import org.assertj.core.api.recursive.comparison.ComparisonDifference;
import org.assertj.core.api.recursive.comparison.RecursiveComparisonConfiguration;
import org.assertj.core.api.recursive.comparison.RecursiveComparisonDifferenceCalculator;
import org.assertj.core.description.TextDescription;
import org.assertj.core.internal.DeepDifference.Difference;
import org.assertj.core.internal.objects.Objects_assertIsEqualToComparingFieldByFieldRecursive_Test.WithCollection;
import org.assertj.core.internal.objects.Objects_assertIsEqualToComparingFieldByFieldRecursive_Test.WithMap;
import org.assertj.core.presentation.Representation;
import org.assertj.core.test.Jedi;
import org.assertj.core.test.Name;
import org.junit.jupiter.api.Test;

public class ShouldBeEqualByComparingFieldByFieldRecursively_create_Test {

  private static final Representation REPRESENTATION = CONFIGURATION_PROVIDER.representation();

  @Test
  @SuppressWarnings("deprecation") // test for deprecated method
  public void should_throw_assertion_error_rather_than_null_pointer_when_one_nested_member_is_null() {
    // GIVEN
    Jedi yoda = new Jedi("Yoda", "Green");
    Jedi noname = new Jedi(null, "Green");
    // WHEN
    Throwable throwable1 = catchThrowable(() -> assertThat(yoda).isEqualToComparingFieldByFieldRecursively(noname));
    Throwable throwable2 = catchThrowable(() -> assertThat(noname).isEqualToComparingFieldByFieldRecursively(yoda));
    // THEN
    assertThat(throwable1).isNotInstanceOf(NullPointerException.class)
                          .isInstanceOf(AssertionError.class);
    assertThat(throwable2).isNotInstanceOf(NullPointerException.class)
                          .isInstanceOf(AssertionError.class);
  }

  @Test
  public void should_use_unambiguous_fields_description_when_standard_description_of_actual_and_expected_collection_fields_values_are_identical() {
    // GIVEN
    WithCollection<String> withHashSet = new WithCollection<>(new LinkedHashSet<String>());
    WithCollection<String> withSortedSet = new WithCollection<>(new TreeSet<String>());
    withHashSet.collection.add("bar");
    withHashSet.collection.add("foo");
    withSortedSet.collection.addAll(withHashSet.collection);
    List<Difference> differences = determineDifferences(withHashSet, withSortedSet, null, null);
    // WHEN
    // @format:off
    String message = shouldBeEqualByComparingFieldByFieldRecursive(withHashSet,
                                                                   withSortedSet,
                                                                   differences,
                                                                   REPRESENTATION)
        .create(new TextDescription("Test"), REPRESENTATION);
    // @format:on
    // THEN
    assertThat(message).isEqualTo(format("[Test] %n" +
                                         "Expecting:%n" +
                                         "  <WithCollection [collection=[bar, foo]]>%n" +
                                         "to be equal to:%n" +
                                         "  <WithCollection [collection=[bar, foo]]>%n" +
                                         "when recursively comparing field by field, but found the following difference(s):%n"
                                         + "%n" +
                                         "Path to difference: <collection>%n" +
                                         "- actual  : <[\"bar\", \"foo\"] (LinkedHashSet@%s)>%n" +
                                         "- expected: <[\"bar\", \"foo\"] (TreeSet@%s)>",
                                         toHexString(System.identityHashCode(withHashSet.collection)),
                                         toHexString(System.identityHashCode(withSortedSet.collection))));
  }

  @Test
  public void should_use_unambiguous_fields_description_when_standard_description_of_actual_and_expected_map_fields_values_are_identical() {
    // GIVEN
    WithMap<Long, Boolean> withLinkedHashMap = new WithMap<>(new LinkedHashMap<>());
    WithMap<Long, Boolean> withTreeMap = new WithMap<>(new TreeMap<>());
    withLinkedHashMap.map.put(1L, true);
    withLinkedHashMap.map.put(2L, false);
    withTreeMap.map.putAll(withLinkedHashMap.map);
    List<Difference> differences = determineDifferences(withLinkedHashMap, withTreeMap, null, null);
    // WHEN
    // @format:off
    String message = shouldBeEqualByComparingFieldByFieldRecursive(withLinkedHashMap,
                                                                   withTreeMap,
                                                                   differences,
                                                                   REPRESENTATION)
                                                          .create(new TextDescription("Test"), REPRESENTATION);
    // @format:on
    // THEN
    assertThat(message).isEqualTo(format("[Test] %n" +
                                         "Expecting:%n" +
                                         "  <WithMap [map={1=true, 2=false}]>%n" +
                                         "to be equal to:%n" +
                                         "  <WithMap [map={1=true, 2=false}]>%n" +
                                         "when recursively comparing field by field, but found the following difference(s):%n" +
                                         "%n" +
                                         "Path to difference: <map>%n" +
                                         "- actual  : <{1L=true, 2L=false} (LinkedHashMap@%s)>%n" +
                                         "- expected: <{1L=true, 2L=false} (TreeMap@%s)>",
                                         toHexString(System.identityHashCode(withLinkedHashMap.map)),
                                         toHexString(System.identityHashCode(withTreeMap.map))));
  }

  @Test
  public void should_precise_missing_fields_when_actual_does_not_declare_all_expected_fields() {
    // GIVEN
    Person person = new Person("John", "Doe");
    PersonDAO personDAO = new PersonDAO("John", "Doe", 1L, 23);
    // THEN
    List<Difference> differences = determineDifferences(person, personDAO, null, null);
    // WHEN
    // @format:off
    String message = shouldBeEqualByComparingFieldByFieldRecursive(person,
                                                                   personDAO,
                                                                   differences,
                                                                   REPRESENTATION)
                                                           .create(new TextDescription("Test"), REPRESENTATION);
    // @format:on
    // THEN
    String personHash = toHexString(person.hashCode());
    String personDAOHash = toHexString(personDAO.hashCode());
    assertThat(message).isEqualTo(format("[Test] %n" +
                                         "Expecting:%n" +
                                         "  <org.assertj.core.error.Person@%s>%n" +
                                         "to be equal to:%n" +
                                         "  <org.assertj.core.error.PersonDAO@%s>%n" +
                                         "when recursively comparing field by field, but found the following difference(s):%n" +
                                         "%n" +
                                         "Path to difference: <>%n" +
                                         "- actual  : <org.assertj.core.error.Person@%s>%n" +
                                         "- expected: <org.assertj.core.error.PersonDAO@%s>%n" +
                                         "- reason  : org.assertj.core.error.Person can't be compared to org.assertj.core.error.PersonDAO as PersonDAO does not declare all Person fields, it lacks these:[firstName, lastName]",
                                         personHash, personDAOHash,
                                         personHash, personDAOHash));
  }

  @Test
  @SuppressWarnings("deprecation") // test for deprecated method
  public void should_not_fall_with_npe_if_field_of_one_of_compared_objects_is_null() {
    // GIVEN
    final Name actualName = new Name("Andy");
    final Name nullName = new Name(null);
    // THEN
    expectAssertionError(() -> assertThat(actualName).isEqualToComparingFieldByFieldRecursively(nullName));
  }

  @Test
  public void should_display_difference_with_percent() {
    // GIVEN
    Jedi yoda1 = new Jedi("Yoda", "Green");
    Jedi yoda2 = new Jedi("%%Yoda%", "Green%");
    // WHEN
    List<Difference> differences = determineDifferences(yoda1, yoda2, null, null);
    // @format:off
    String message = shouldBeEqualByComparingFieldByFieldRecursive(yoda1,
                                                                   yoda2,
                                                                   differences,
                                                                   REPRESENTATION)
                                                           .create(new TextDescription("Test"), REPRESENTATION);
    // @format:on
    // THEN
    assertThat(message).isEqualTo(format("[Test] %n" +
                                         "Expecting:%n" +
                                         "  <Yoda the Jedi>%n" +
                                         "to be equal to:%n" +
                                         "  <%%%%Yoda%% the Jedi>%n" +
                                         "when recursively comparing field by field, but found the following difference(s):%n" +
                                         "%n" +
                                         "Path to difference: <name>%n" +
                                         "- actual  : <\"Yoda\">%n" +
                                         "- expected: <\"%%%%Yoda%%\">%n" +
                                         "%n" +
                                         "Path to difference: <lightSaberColor>%n" +
                                         "- actual  : <\"Green\">%n" +
                                         "- expected: <\"Green%%\">"));
  }

  @Test
  public void should_show_multiple_differences() {
    // GIVEN
    final Name actualName = new Name("Magic", "Johnson");
    final Name nullName = new Name(null, "Ginobili");
    RecursiveComparisonConfiguration recursiveComparisonConfiguration = new RecursiveComparisonConfiguration();
    recursiveComparisonConfiguration.setIgnoreAllActualNullFields(true);
    List<ComparisonDifference> differences = computeDifferences(actualName, nullName, recursiveComparisonConfiguration);
    // WHEN
    // @format:off
    String message = shouldBeEqualByComparingFieldByFieldRecursively(actualName,
                                                                     nullName,
                                                                     differences,
                                                                     recursiveComparisonConfiguration,
                                                                     REPRESENTATION)
                                                           .create(new TextDescription("Test"), REPRESENTATION);
    // @format:on
    // THEN
    assertThat(message).isEqualTo(format("[Test] %n" +
                                         "Expecting:%n" +
                                         "  <Name[first='Magic', last='Johnson']>%n" +
                                         "to be equal to:%n" +
                                         "  <Name[first='null', last='Ginobili']>%n" +
                                         "when recursively comparing field by field, but found the following 2 differences:%n" +
                                         "%n" +
                                         "field/property 'first' differ:%n" +
                                         "- actual value   : \"Magic\"%n" +
                                         "- expected value : null%n" +
                                         "%n" +
                                         "field/property 'last' differ:%n" +
                                         "- actual value   : \"Johnson\"%n" +
                                         "- expected value : \"Ginobili\"%n" +
                                         "%n" +
                                         "The recursive comparison was performed with this configuration:%n%s",
                                         CONFIGURATION_PROVIDER.representation().toStringOf(recursiveComparisonConfiguration)));
  }

  @Test
  public void should_show_one_difference() {
    // GIVEN
    final Name actualName = new Name("Magic", "Johnson");
    final Name nullName = new Name(null, "Johnson");
    RecursiveComparisonConfiguration recursiveComparisonConfiguration = new RecursiveComparisonConfiguration();
    recursiveComparisonConfiguration.setIgnoreAllActualNullFields(true);
    List<ComparisonDifference> differences = computeDifferences(actualName, nullName, recursiveComparisonConfiguration);
    // WHEN
    // @format:off
    String message = shouldBeEqualByComparingFieldByFieldRecursively(actualName,
                                                                     nullName,
                                                                     differences,
                                                                     recursiveComparisonConfiguration,
                                                                     REPRESENTATION).create(new TextDescription("Test"), REPRESENTATION);
    // @format:on
    // THEN
    assertThat(message).isEqualTo(format("[Test] %n" +
                                         "Expecting:%n" +
                                         "  <Name[first='Magic', last='Johnson']>%n" +
                                         "to be equal to:%n" +
                                         "  <Name[first='null', last='Johnson']>%n" +
                                         "when recursively comparing field by field, but found the following difference:%n" +
                                         "%n" +
                                         "field/property 'first' differ:%n" +
                                         "- actual value   : \"Magic\"%n" +
                                         "- expected value : null%n" +
                                         "%n" +
                                         "The recursive comparison was performed with this configuration:%n%s",
                                         CONFIGURATION_PROVIDER.representation().toStringOf(recursiveComparisonConfiguration)));
  }

  @Test
  public void should_show_difference_with_percentage() {
    // GIVEN
    final Name actualName = new Name("%%Ma%gi%", "%Johnson");
    final Name nullName = new Name(null, "%Johnson");
    RecursiveComparisonConfiguration recursiveComparisonConfiguration = new RecursiveComparisonConfiguration();
    recursiveComparisonConfiguration.setIgnoreAllActualNullFields(true);
    List<ComparisonDifference> differences = computeDifferences(actualName, nullName, recursiveComparisonConfiguration);
    // WHEN
    // @format:off
    String message = shouldBeEqualByComparingFieldByFieldRecursively(actualName,
                                                                     nullName,
                                                                     differences,
                                                                     recursiveComparisonConfiguration,
                                                                     REPRESENTATION).create(new TextDescription("Test"), REPRESENTATION);
    // @format:on
    // THEN
    assertThat(message).isEqualTo(format("[Test] %n" +
                                         "Expecting:%n" +
                                         "  <Name[first='%%%%Ma%%gi%%', last='%%Johnson']>%n" +
                                         "to be equal to:%n" +
                                         "  <Name[first='null', last='%%Johnson']>%n" +
                                         "when recursively comparing field by field, but found the following difference:%n" +
                                         "%n" +
                                         "field/property 'first' differ:%n" +
                                         "- actual value   : \"%%%%Ma%%gi%%\"%n" +
                                         "- expected value : null%n" +
                                         "%n" +
                                         "The recursive comparison was performed with this configuration:%n%s",
                                         CONFIGURATION_PROVIDER.representation().toStringOf(recursiveComparisonConfiguration)));
  }

  private List<ComparisonDifference> computeDifferences(Object actual, Object expected,
                                                        RecursiveComparisonConfiguration recursiveComparisonConfiguration) {
    return new RecursiveComparisonDifferenceCalculator().determineDifferences(actual, expected, recursiveComparisonConfiguration);
  }

}
