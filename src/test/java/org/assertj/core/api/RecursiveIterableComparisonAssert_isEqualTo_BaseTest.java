package org.assertj.core.api;

import org.assertj.core.api.*;
import org.assertj.core.api.recursive.comparison.RecursiveComparisonConfiguration;
import org.assertj.core.api.recursive.comparison.RecursiveComparisonDifferenceComparator;
import org.assertj.core.internal.*;
import org.assertj.core.internal.objects.data.Person;
import org.assertj.core.internal.objects.data.PersonDto;
import org.junit.jupiter.api.BeforeEach;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.util.AssertionsUtil.expectAssertionError;
import static org.mockito.Mockito.spy;

public class RecursiveIterableComparisonAssert_isEqualTo_BaseTest extends RecursiveIterableComparisonBaseTest {

  protected static Person sheldon = new Person("Sheldon");
  protected static Person leonard = new Person("Leonard");
  protected static Person howard = new Person("Howard");
  protected static Person raj = new Person("Rajesh");
  protected static Person penny = new Person("Penny");

  protected static PersonDto sheldonDto = new PersonDto("Sheldon");
  protected static PersonDto leonardDto = new PersonDto("Leonard");
  protected static PersonDto howardDto = new PersonDto("Howard");
  protected static PersonDto rajDto = new PersonDto("Rajesh");

  protected static WritableAssertionInfo info;
  protected static AbstractIterableAssert iterableAssert;

  public AssertionError compareRecursivelyFailsAsExpected(Iterable<?> actual, Iterable<?> expected) {
    iterableAssert = new IterableAssert(actual);
    iterableAssert.objects = objects;
    RecursiveIterableComparisonAssert<?, ?> recursiveIterableComparisonAssert =
        new RecursiveIterableComparisonAssert<>(actual, recursiveComparisonConfiguration, iterableAssert);
    info = recursiveIterableComparisonAssert.info;
    iterableAssert.info = info;
    recursiveIterableComparisonAssert.objects = objects;

    return expectAssertionError(() ->
      recursiveIterableComparisonAssert.isEqualTo(expected));
  }
}
