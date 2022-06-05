package org.assertj.core.util;

import static org.assertj.core.api.Assertions.assertThat;

import java.nio.Buffer;
import java.util.function.Predicate;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link Types}.
 *
 * @author Ashley Scopes
 */
class TypesTest {
  @SuppressWarnings("rawtypes")
  @Test
  void can_cast_predicate_type() {
    // This test is a constant condition. If the API does not work due to type incompatibility,
    // then this should immediately fail to compile at all.

    // Given
    Class<Predicate> rawType = Predicate.class;

    // When
    Class<Predicate<? super Buffer>> castType = Types.castClass(rawType);

    // Then
    assertThat(castType).isSameAs(rawType);
  }

  @SuppressWarnings("rawtypes")
  @Test
  void can_cast_iterable_type() {
    // This test is a constant condition. If the API does not work due to type incompatibility,
    // then this should immediately fail to compile at all.

    // Given
    Class<Iterable> rawType = Iterable.class;

    // When
    Class<Iterable<StringBuilder>> castType = Types.castClass(rawType);

    // Then
    assertThat(castType).isSameAs(rawType);
  }
}
