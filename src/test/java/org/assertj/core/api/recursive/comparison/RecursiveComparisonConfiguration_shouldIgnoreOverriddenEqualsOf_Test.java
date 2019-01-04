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

  @ParameterizedTest(name = "{0} should be ignored with these regexes {1}")
  @MethodSource("ignoringCustomEqualsByRegexesSource")
  public void should_ignore_custom_equals_by_regexes(Class<?> clazz, List<String> ignoredCustomEqualsByRegexes) {
    // GIVEN
    recursiveComparisonConfiguration.ignoreOverriddenEqualsByRegexes(ignoredCustomEqualsByRegexes.toArray(new String[0]));
    // WHEN
    boolean ignored = recursiveComparisonConfiguration.shouldIgnoreOverriddenEqualsOf(clazz);
    // THEN
    assertThat(ignored).as("%s should be ignored with these regexes %s", clazz, ignoredCustomEqualsByRegexes)
                       .isTrue();
  }

  @SuppressWarnings("unused")
  private static Stream<Arguments> ignoringCustomEqualsByRegexesSource() {
    return Stream.of(Arguments.of(Person.class, list("foo", ".*Person")),
                     Arguments.of(Human.class, list("org.assertj.core.internal.*.data\\.Human", "foo")),
                     Arguments.of(Multimap.class, list("com.google.common.collect.*")));
  }

  @ParameterizedTest(name = "{0} should be ignored with these types {1}")
  @MethodSource("ignoringCustomEqualsForTypesSource")
  public void should_ignore_custom_equals_by_types(Class<?> clazz, List<Class<?>> ignoredCustomEqualsByTypes) {
    // GIVEN
    recursiveComparisonConfiguration.ignoreOverriddenEqualsForTypes(ignoredCustomEqualsByTypes.toArray(new Class[0]));
    // WHEN
    boolean ignored = recursiveComparisonConfiguration.shouldIgnoreOverriddenEqualsOf(clazz);
    // THEN
    assertThat(ignored).as("%s should be ignored with these types %s", clazz, ignoredCustomEqualsByTypes)
                       .isTrue();
  }

  @SuppressWarnings("unused")
  private static Stream<Arguments> ignoringCustomEqualsForTypesSource() {
    return Stream.of(Arguments.of(Person.class, list(Human.class, Person.class, String.class)),
                     Arguments.of(Human.class, list(Human.class)));
  }

}
