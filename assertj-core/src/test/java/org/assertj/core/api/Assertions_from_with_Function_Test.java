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
