package org.assertj.core.api;

import static org.assertj.core.api.BDDAssertions.then;
import static org.mockito.Mockito.mock;

import java.util.function.Function;

import org.junit.jupiter.api.Test;

/**
 * Tests for <code>{@link Assertions#from(Function)}</code>.
 *
 * @author Stefano Cordio
 */
class Assertions_from_with_Function_Test {

  @Test
  void should_return_the_given_extractor() {
    // GIVEN
    Function<?, ?> extractor = mock(Function.class);
    // WHEN
    Function<?, ?> result = Assertions.from(extractor);
    // THEN
    then(result).isSameAs(extractor);
  }

}
