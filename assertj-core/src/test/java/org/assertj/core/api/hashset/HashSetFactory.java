package org.assertj.core.api.hashset;

import static java.util.Arrays.asList;
import static org.assertj.core.util.Sets.newHashSet;
import static org.junit.jupiter.api.Named.named;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.HashSet;
import java.util.stream.Stream;

import org.assertj.core.util.Sets;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

@FunctionalInterface
interface HashSetFactory {
  <T> HashSet<T> createWith(T... elements);

  static Stream<Arguments> availableFactories() {
    return Stream.of(
                     Arguments.of(named(
                                        "ordinary HashSet",
                                        new HashSetFactory() {
                                          @Override
                                          public <T> HashSet<T> createWith(T... elements) {
                                            return newHashSet(asList(elements));
                                          }
                                        })),
                     Arguments.of(named("LinkedHashSet", (HashSetFactory) Sets::newLinkedHashSet)));
  }

  @ParameterizedTest(name = "{0}")
  @MethodSource("org.assertj.core.api.hashset.HashSetFactory#availableFactories")
  @Target({ ElementType.ANNOTATION_TYPE, ElementType.METHOD })
  @Retention(RetentionPolicy.RUNTIME)
  @Documented
  @interface Test {
  }
}
