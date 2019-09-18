package org.assertj.core.api;

import static org.assertj.core.api.BDDAssertions.then;
import static org.mockito.Mockito.mock;

import org.junit.jupiter.api.Test;

/**
 * Tests for <code>{@link Assertions#as(InstanceOfAssertFactory)}</code>.
 *
 * @author Stefano Cordio
 */
class Assertions_as_with_InstanceOfAssertFactory_Test {

  @Test
  @SuppressWarnings("unchecked")
  void should_return_the_given_assert_factory() {
    // GIVEN
    InstanceOfAssertFactory<?, AbstractAssert<?, ?>> assertFactory = mock(InstanceOfAssertFactory.class);
    // WHEN
    InstanceOfAssertFactory<?, AbstractAssert<?, ?>> result = Assertions.as(assertFactory);
    // THEN
    then(result).isSameAs(assertFactory);
  }

}
