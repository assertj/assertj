package org.assertj.core.api.recursive.comparison;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.util.Lists.list;

import java.util.List;
import java.util.stream.Stream;

import org.assertj.core.internal.objects.data.Human;
import org.assertj.core.internal.objects.data.Person;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import com.google.common.collect.Multimap;

public class RecursiveComparisonConfiguration_shouldIgnoreOverriddenEqualsOf_Test {

  private RecursiveComparisonConfiguration recursiveComparisonConfiguration;

  @BeforeEach
  public void setup() {
    recursiveComparisonConfiguration = new RecursiveComparisonConfiguration();
  }

  @ParameterizedTest(name = "{0} overridden equals should be ignored with these regexes {1}")
  @MethodSource("ignoringOverriddenEqualsByRegexesSource")
  public void should_ignore_overridden_equals_by_regexes(Class<?> clazz, List<String> ignoredOverriddenEqualsByRegexes) {
    // GIVEN
    recursiveComparisonConfiguration.ignoreOverriddenEqualsByRegexes(ignoredOverriddenEqualsByRegexes.toArray(new String[0]));
    // WHEN
    boolean ignored = recursiveComparisonConfiguration.shouldIgnoreOverriddenEqualsOf(clazz);
    // THEN
    assertThat(ignored).as("%s overridden equals should be ignored with these regexes %s",
                           clazz, ignoredOverriddenEqualsByRegexes)
                       .isTrue();
  }

  @SuppressWarnings("unused")
  private static Stream<Arguments> ignoringOverriddenEqualsByRegexesSource() {
    return Stream.of(Arguments.of(Person.class, list("foo", ".*Person")),
                     Arguments.of(Human.class, list("org.assertj.core.internal.*.data\\.Human", "foo")),
                     Arguments.of(Multimap.class, list("com.google.common.collect.*")));
  }

  @ParameterizedTest(name = "{0} overridden equals should be ignored for these types {1}")
  @MethodSource("ignoringOverriddenEqualsForTypesSource")
  public void should_ignore_overridden_equals_by_types(Class<?> clazz, List<Class<?>> ignoredOverriddenEqualsByTypes) {
    // GIVEN
    recursiveComparisonConfiguration.ignoreOverriddenEqualsForTypes(ignoredOverriddenEqualsByTypes.toArray(new Class[0]));
    // WHEN
    boolean ignored = recursiveComparisonConfiguration.shouldIgnoreOverriddenEqualsOf(clazz);
    // THEN
    assertThat(ignored).as("%s overridden equals should be ignored for these types %s", clazz, ignoredOverriddenEqualsByTypes)
                       .isTrue();
  }

  @SuppressWarnings("unused")
  private static Stream<Arguments> ignoringOverriddenEqualsForTypesSource() {
    return Stream.of(Arguments.of(Person.class, list(Human.class, Person.class, String.class)),
                     Arguments.of(Human.class, list(Human.class)));
  }

  @ParameterizedTest(name = "{0} overridden equals should be ignored for these fields {1}")
  @MethodSource("ignoringOverriddenEqualsForFieldsSource")
  public void should_ignore_overridden_equals_by_fields(DualKey dualKey, List<String> ignoredOverriddenEqualsByFields) {
    // GIVEN
    recursiveComparisonConfiguration.ignoreOverriddenEqualsForFields(ignoredOverriddenEqualsByFields.toArray(new String[0]));
    // WHEN
    boolean ignored = recursiveComparisonConfiguration.shouldIgnoreOverriddenEqualsOf(dualKey);
    // THEN
    assertThat(ignored).as("%s overridden equals should be ignored for these fields %s", dualKey, ignoredOverriddenEqualsByFields)
                       .isTrue();
  }

  @SuppressWarnings("unused")
  private static Stream<Arguments> ignoringOverriddenEqualsForFieldsSource() {
    return Stream.of(Arguments.of(dualKeyWithPath("name"), list("name")),
                     Arguments.of(dualKeyWithPath("name"), list("foo", "name", "foo")),
                     Arguments.of(dualKeyWithPath("name", "first"), list("name.first")),
                     Arguments.of(dualKeyWithPath("father", "name", "first"),
                                  list("father", "name.first", "father.name.first")));

  }

  private static DualKey dualKeyWithPath(String... pathElements) {
    return new DualKey(list(pathElements), new Object(), new Object());
  }

}
