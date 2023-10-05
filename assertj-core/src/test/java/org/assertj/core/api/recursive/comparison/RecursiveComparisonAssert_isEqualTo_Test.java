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
 * Copyright 2012-2023 the original author or authors.
 */
package org.assertj.core.api.recursive.comparison;

import static java.lang.String.format;
import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.BDDAssertions.entry;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.api.recursive.comparison.ColorWithCode.RED;
import static org.assertj.core.error.ShouldBeEqual.shouldBeEqual;
import static org.assertj.core.error.ShouldNotBeNull.shouldNotBeNull;
import static org.assertj.core.test.AlwaysEqualComparator.ALWAYS_EQUALS_STRING;
import static org.assertj.core.test.Maps.mapOf;
import static org.assertj.core.util.Lists.list;
import static org.assertj.core.util.Maps.newHashMap;
import static org.junit.jupiter.params.provider.Arguments.arguments;
import static org.mockito.Mockito.verify;

import java.io.IOException;
import java.net.InetAddress;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import javax.xml.datatype.DatatypeFactory;

import org.assertj.core.api.RecursiveComparisonAssert_isEqualTo_BaseTest;
import org.assertj.core.internal.objects.data.AlwaysEqualPerson;
import org.assertj.core.internal.objects.data.FriendlyPerson;
import org.assertj.core.internal.objects.data.Giant;
import org.assertj.core.internal.objects.data.Human;
import org.assertj.core.internal.objects.data.Person;
import org.assertj.core.util.DoubleComparator;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

class RecursiveComparisonAssert_isEqualTo_Test extends RecursiveComparisonAssert_isEqualTo_BaseTest {

  @Test
  void should_pass_when_actual_and_expected_are_null() {
    // GIVEN
    Person actual = null;
    Person expected = null;
    // THEN
    assertThat(actual).usingRecursiveComparison()
                      .isEqualTo(expected);
  }

  @Test
  void should_fail_when_actual_is_null_and_expected_is_not() {
    // GIVEN
    Person actual = null;
    Person expected = new Person();
    // WHEN
    compareRecursivelyFailsAsExpected(actual, expected);
    // THEN
    verify(failures).failure(info, shouldNotBeNull());
  }

  @Test
  void should_fail_when_actual_is_not_null_and_expected_is() {
    // GIVEN
    Person actual = new Person();
    Person expected = null;
    // WHEN
    compareRecursivelyFailsAsExpected(actual, expected);
    // THEN
    verify(failures).failure(info, shouldBeEqual(actual, null, objects.getComparisonStrategy(), info.representation()));
  }

  @Test
  void should_propagate_comparators_by_type() {
    // GIVEN
    Person actual = new Person("John");
    // WHEN
    RecursiveComparisonConfiguration assertion = assertThat(actual).usingComparatorForType(ALWAYS_EQUALS_STRING, String.class)
                                                                   .usingRecursiveComparison()
                                                                   .getRecursiveComparisonConfiguration();
    // THEN
    assertThat(assertion.comparatorByTypes()).contains(entry(String.class, ALWAYS_EQUALS_STRING));
  }

  @Test
  void should_not_use_equal_implementation_of_root_objects_to_compare() {
    // GIVEN
    AlwaysEqualPerson actual = new AlwaysEqualPerson();
    actual.name = "John";
    actual.home.address.number = 1;
    AlwaysEqualPerson expected = new AlwaysEqualPerson();
    expected.name = "John";
    expected.home.address.number = 2;
    // WHEN
    compareRecursivelyFailsAsExpected(actual, expected);
    // THEN
    ComparisonDifference numberDifference = diff("home.address.number", actual.home.address.number, expected.home.address.number,
                                                 "Comparison objects are of Java types and were then compared with equals method");
    verifyShouldBeEqualByComparingFieldByFieldRecursivelyCall(actual, expected, numberDifference);
  }

  @Test
  void should_treat_date_as_equal_to_timestamp() {
    // GIVEN
    Person actual = new Person("Fred");
    actual.dateOfBirth = new Date(1000L);
    Person expected = new Person("Fred");
    expected.dateOfBirth = new Timestamp(1000L);
    // THEN
    assertThat(actual).usingRecursiveComparison()
                      .isEqualTo(expected);
  }

  @Test
  void should_be_able_to_compare_objects_with_percentages() {
    // GIVEN
    Person actual = new Person("foo");
    Person expected = new Person("%foo");
    // WHEN
    compareRecursivelyFailsAsExpected(actual, expected);
    // THEN
    ComparisonDifference nameDifference = diff("name", actual.name, expected.name,
                                               "Comparison objects are of Java types and were then compared with equals method");
    verifyShouldBeEqualByComparingFieldByFieldRecursivelyCall(actual, expected, nameDifference);
  }

  @Test
  void should_fail_when_fields_of_different_nesting_levels_differ() {
    // GIVEN
    Person actual = new Person("John");
    actual.home.address.number = 1;
    Person expected = new Person("Jack");
    expected.home.address.number = 2;
    // WHEN
    compareRecursivelyFailsAsExpected(actual, expected);
    // THEN
    ComparisonDifference nameDifference = diff("name", actual.name, expected.name,
                                               "Comparison objects are of Java types and were then compared with equals method");
    ComparisonDifference numberDifference = diff("home.address.number", actual.home.address.number, expected.home.address.number,
                                                 "Comparison objects are of Java types and were then compared with equals method");
    verifyShouldBeEqualByComparingFieldByFieldRecursivelyCall(actual, expected, numberDifference, nameDifference);
  }

  @SuppressWarnings("unused")
  @ParameterizedTest(name = "{2}: actual={0} / expected={1}")
  @MethodSource("recursivelyEqualObjects")
  void should_pass_for_objects_with_the_same_data_when_using_the_default_recursive_comparison(Object actual,
                                                                                              Object expected,
                                                                                              String testDescription) {
    assertThat(actual).usingRecursiveComparison()
                      .isEqualTo(expected);
  }

  private static Stream<Arguments> recursivelyEqualObjects() {
    Person person1 = new Person("John");
    person1.home.address.number = 1;
    Person person2 = new Person("John");
    person2.home.address.number = 1;

    Person person3 = new Person("John");
    person3.home.address.number = 1;
    Human person4 = new Human();
    person4.name = "John";
    person4.home.address.number = 1;

    return Stream.of(arguments(person1, person2, "same data, same type"),
                     arguments(person2, person1, "same data, same type reversed"),
                     arguments(person3, person4, "same data, different type"),
                     arguments(new Theme(RED), new Theme(RED), "same data with enum overriding methods - covers #1866"),
                     arguments(person4, person3, "same data, different type"));
  }

  @Test
  void should_be_able_to_compare_objects_with_direct_cycles() {
    // GIVEN
    Person actual = new Person("John");
    actual.home.address.number = 1;

    Person expected = new Person("John");
    expected.home.address.number = 1;

    // neighbour
    expected.neighbour = actual;
    actual.neighbour = expected;

    // THEN
    assertThat(actual).usingRecursiveComparison()
                      .isEqualTo(expected);
  }

  @Test
  void should_be_able_to_compare_objects_with_cycles_in_ordered_collection() {
    // GIVEN
    FriendlyPerson actual = new FriendlyPerson();
    actual.name = "John";
    actual.home.address.number = 1;

    FriendlyPerson expected = new FriendlyPerson();
    expected.name = "John";
    expected.home.address.number = 1;

    // neighbour
    expected.neighbour = actual;
    actual.neighbour = expected;

    // friends
    FriendlyPerson sherlock = new FriendlyPerson();
    sherlock.name = "Sherlock";
    sherlock.home.address.number = 221;
    actual.friends.add(sherlock);
    actual.friends.add(expected);
    expected.friends.add(sherlock);
    expected.friends.add(actual);

    // THEN
    assertThat(actual).usingRecursiveComparison()
                      .isEqualTo(expected);
  }

  @Test
  void should_be_able_to_compare_objects_with_cycles_in_ordered_and_unordered_collection() {
    // GIVEN
    FriendlyPerson actual = new FriendlyPerson();
    actual.name = "John";
    actual.home.address.number = 1;

    FriendlyPerson expected = new FriendlyPerson();
    expected.name = "John";
    expected.home.address.number = 1;

    // neighbour - direct cycle
    expected.neighbour = actual;
    actual.neighbour = expected;

    // friends cycle with intermediate collection
    FriendlyPerson sherlock = new FriendlyPerson();
    sherlock.name = "Sherlock";
    sherlock.home.address.number = 221;

    // ordered collections
    actual.friends.add(sherlock);
    actual.friends.add(expected);
    expected.friends.add(sherlock);
    expected.friends.add(actual);

    // unordered collections
    // this could cause an infinite recursion if we don't track correctly the visited objects
    actual.otherFriends.add(actual);
    actual.otherFriends.add(expected);
    actual.otherFriends.add(sherlock);
    expected.otherFriends.add(sherlock);
    expected.otherFriends.add(expected);
    expected.otherFriends.add(actual);

    // THEN
    assertThat(actual).usingRecursiveComparison()
                      .isEqualTo(expected);
  }

  @Test
  void should_report_difference_in_collection() {
    // GIVEN
    FriendlyPerson actual = new FriendlyPerson();
    FriendlyPerson actualFriend = new FriendlyPerson();
    actualFriend.home.address.number = 99;
    actual.friends = list(actualFriend);

    FriendlyPerson expected = new FriendlyPerson();
    FriendlyPerson expectedFriend = new FriendlyPerson();
    expectedFriend.home.address.number = 10;
    expected.friends = list(expectedFriend);

    // WHEN
    compareRecursivelyFailsAsExpected(actual, expected);

    // THEN
    ComparisonDifference friendNumberDifference = diff("friends[0].home.address.number", 99, 10,
                                                       "Comparison objects are of Java types and were then compared with equals method");
    verifyShouldBeEqualByComparingFieldByFieldRecursivelyCall(actual, expected, friendNumberDifference);
  }

  @Test
  void should_report_missing_property() {
    // GIVEN
    Giant actual = new Giant();
    actual.name = "joe";
    actual.height = 3.0;
    Human expected = new Human();
    expected.name = "joe";
    // WHEN
    compareRecursivelyFailsAsExpected(actual, expected);
    // THEN
    ComparisonDifference missingFieldDifference = diff("", actual, expected,
                                                       "org.assertj.core.internal.objects.data.Giant can't be compared to org.assertj.core.internal.objects.data.Human as Human does not declare all Giant fields, it lacks these: [height]");
    verifyShouldBeEqualByComparingFieldByFieldRecursivelyCall(actual, expected, missingFieldDifference);
  }

  static class LightString {
    public String color;

    public LightString(String value) {
      this.color = value;
    }
  }

  @Test
  void should_not_treat_Path_as_Iterable_to_avoid_infinite_recursion() {
    final Container container1 = new Container("/tmp/example");
    final Container container2 = new Container("/tmp/example");

    assertThat(container1).usingRecursiveComparison()
                          .isEqualTo(container2)
                          .ignoringAllOverriddenEquals()
                          .isEqualTo(container2);
  }

  // issue #2434
  @Test
  void should_treat_class_cast_exception_as_comparison_difference_when_comparing_lists() {
    // GIVEN
    Wrapper a = new Wrapper(Double.MAX_VALUE);
    Wrapper b = new Wrapper(Integer.MAX_VALUE);
    Wrappers actual = new Wrappers(a, b);
    Wrappers expected = new Wrappers(b, a);
    // WHEN/THEN
    then(actual).usingRecursiveComparison()
                .ignoringCollectionOrderInFields("values")
                .isEqualTo(expected);
  }

  @Test
  void should_report_class_cast_exception_as_comparison_difference() {
    // GIVEN
    Wrapper actual = new Wrapper(1.0);
    Wrapper expected = new Wrapper(5);
    // WHEN
    compareRecursivelyFailsAsExpected(actual, expected);
    // THEN
    verifyShouldBeEqualByComparingFieldByFieldRecursivelyCall(actual, expected, diff("value", 1.0, 5));
  }

  @Test
  void should_treat_class_cast_exception_as_comparison_difference_when_comparing_lists_with_specific_equals() {
    // GIVEN
    Wrapper a = new Wrapper(1.001);
    Wrapper b = new Wrapper(1);
    Wrappers actual = new Wrappers(a, b);
    Wrappers expected = new Wrappers(b, a);
    // WHEN/THEN
    then(actual).usingRecursiveComparison()
                .ignoringCollectionOrderInFields("values")
                .withEqualsForType((x, y) -> Math.abs(x - y) <= 0.05, Double.class)
                .isEqualTo(expected);
  }

  @Test
  void should_treat_class_cast_exception_as_comparison_difference() {
    // GIVEN
    Wrapper a = new Wrapper(Double.MAX_VALUE);
    Wrapper b = new Wrapper(Integer.MAX_VALUE);
    Wrappers actual = new Wrappers(a, b);
    Wrappers expected = new Wrappers(b, a);
    // WHEN/THEN
    then(actual).usingRecursiveComparison()
                .withComparatorForFields(new DoubleComparator(0.01), "values.value")
                .ignoringCollectionOrderInFields("values")
                .isEqualTo(expected);
  }

  @Test
  void should_not_handle_value_node_as_iterable() throws IOException {
    // GIVEN
    ObjectMapper om = new ObjectMapper();
    JsonNode actual = om.readTree("{\"someNotImportantValue\":1,\"importantValue\":\"10\"}");
    JsonNode expected = om.readTree("{\"someNotImportantValue\":10,\"importantValue\":\"1\"}");
    // WHEN
    compareRecursivelyFailsAsExpected(actual, expected);
    // THEN
    ComparisonDifference difference1 = diff("_children.importantValue._value", "10", "1",
                                            "Comparison objects are of Java types and were then compared with equals method");
    ComparisonDifference difference2 = diff("_children.someNotImportantValue._value", 1, 10,
                                            "Comparison objects are of Java types and were then compared with equals method");
    verifyShouldBeEqualByComparingFieldByFieldRecursivelyCall(actual, expected, difference1, difference2);
  }

  // issue #2459
  @Test
  void should_not_handle_object_node_as_iterable() throws IOException {
    // GIVEN
    ObjectMapper om = new ObjectMapper();
    JsonNode actual = om.readTree("{\"someNotImportantValue\":1,\"importantValue\":\"10\"}");
    JsonNode expected = om.readTree("{\"foo\":1,\"bar\":\"10\"}");
    // WHEN
    compareRecursivelyFailsAsExpected(actual, expected);
    // THEN
    ComparisonDifference difference = diff("_children",
                                           mapOf(entry("importantValue", "10"), entry("someNotImportantValue", 1)),
                                           mapOf(entry("bar", "10"), entry("foo", 1)),
                                           format("The following keys were not found in the actual map value:%n  [foo, bar]"));
    verifyShouldBeEqualByComparingFieldByFieldRecursivelyCall(actual, expected, difference);
  }

  @Test
  void issue_2475_example_should_succeed() {
    then(issue2475Map()).usingRecursiveComparison()
                        .isEqualTo(issue2475Map());
  }

  private static Map<String, List<String>> issue2475Map() {
    Map<String, List<String>> map = newHashMap("VMP", list("OztNUFPcnceerHAppabgHT",
                                                           "IW",
                                                           "AfBSmPEYfOBwGzWHzQveOi",
                                                           "dSalYEgeHNTe",
                                                           "mXjwEZBxeimMiWrmRVePVAwWHtRXfqQyD",
                                                           "TGgLRwnPQUbZWFr",
                                                           "pQWceZdDmTXdyQXcJdB",
                                                           "ProMMnAnRXg"));
    map.put("Uko", list("pUisdBNIy",
                        "rfX",
                        "BagGdILqDLrNRfotwKqjCVNOJxSNoYKtSgBLMEJEJymhZjZvDuwvsqBiJuJpmvWOkiuSobCjRkeWVenaqIdlltsiUMPNtKcDMOAKiRRHHfikxUnOotnJFzNjwyYrcbkNBjxlvici",
                        "AR",
                        "dDvIHrhSxskuTvDSdUZwoUDdxFxxaxBWkTiprWPqSPZumdoHkvwPRrecqCLagzeeOjCuSufGwLoKATVaXfIPmjYsVfGuwlyEysXwWbVfPLgbVkaPaQdcVFQfADfDKEJeuQZlKKSsfuXICYWrmOGILeuqXKZyfEXHLnGILUcWmaVRRjrSjXXnHiTXYgdkrDeLEXZnAlbIEUYSblPqOaxuvpmOS"));
    return map;
  }

  static class Wrappers {
    @SuppressWarnings("unused")
    private final List<Wrapper> values;

    public Wrappers(Wrapper a, Wrapper b) {
      this.values = list(a, b);
    }
  }

  static class Wrapper {
    Object value;

    Wrapper(Object value) {
      this.value = value;
    }
  }
  public static class Container {
    private final Path path;

    public Container(String path) {
      this.path = Paths.get(path);
    }

    public Path getPath() {
      return path;
    }
  }

  // https://github.com/assertj/assertj/issues/2928
  @ParameterizedTest(name = "class: {2}")
  @MethodSource
  void should_not_introspect_java_base_classes(Object actual, Object expected,
                                               @SuppressWarnings("unused") String testDescription) {
    assertThat(actual).usingRecursiveComparison()
                      .isEqualTo(expected);
  }

  private static Stream<Arguments> should_not_introspect_java_base_classes() throws Exception {

    return Stream.of(arguments(DatatypeFactory.newInstance().newXMLGregorianCalendar(),
                               DatatypeFactory.newInstance().newXMLGregorianCalendar(),
                               "com.sun.org.apache.xerces.internal.jaxp.datatype.XMLGregorianCalendarImpl"),
                     arguments(InetAddress.getByName("127.0.0.1"),
                               InetAddress.getByName("127.0.0.1"),
                               InetAddress.class.getName()));
  }

  // https://github.com/assertj/assertj/issues/2979

  static class Assignment {
    String assignmentName;
    String assignmentDescription;

    Assignment(String assignmentName, String assignmentDescription) {
      this.assignmentName = assignmentName;
      this.assignmentDescription = assignmentDescription;
    }
  }

  static class Owner {
    String id;
    String name;
    List<Assignment> assignments;

    Owner(String id, String name, List<Assignment> assignments) {
      this.id = id;
      this.name = name;
      this.assignments = assignments;
    }
  }

  public static List<Assignment> generateListOfAssignments(int size, String id) {
    return IntStream.range(0, size)
                    .mapToObj(i -> new Assignment(id, "Assignment " + id))
                    .collect(toList());
  }

  public static List<Owner> generateListOfOwners(int noOfOwners, int noOfAssignments, String id) {
    String ownerId = "owner" + id;
    String ownerName = "TestOwner " + id;
    return IntStream.range(0, noOfOwners)
                    .mapToObj(i -> new Owner(ownerId, ownerName, generateListOfAssignments(noOfAssignments, id)))
                    .collect(toList());
  }

  @Test
  @Disabled("just to check performance of the recursive comparison when using big collections")
  void performance_for_comparing_lots_of_similar_types() {
    // 100 owners with 50 assignments each
    List<Owner> actual = generateListOfOwners(50, 50, "1"); // 2500 assignment

    // 100 different owners with 50 different assignments each
    List<Owner> expected = generateListOfOwners(50, 50, "2"); // 2500 assignment

    // Recursive comparison logic
    RecursiveComparisonConfiguration config = new RecursiveComparisonConfiguration();
    // when uncommented, test is slow as we compare recursively 2500x2500 elements
    config.ignoreCollectionOrder(true);
    // config.ignoreCollectionOrderInFields();
    // config.setIntrospectionStrategy(ComparingFields.COMPARING_FIELDS);

    new RecursiveComparisonDifferenceCalculator().determineDifferences(actual, expected, config);

  }
}
