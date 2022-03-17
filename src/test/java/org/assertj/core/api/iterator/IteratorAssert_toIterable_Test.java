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
 * Copyright 2012-2022 the original author or authors.
 */
package org.assertj.core.api.iterator;

import org.assertj.core.api.*;
import org.assertj.core.api.abstract_.AbstractAssert_isNull_Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowableOfType;
import static org.assertj.core.error.ShouldBeExhausted.shouldBeExhausted;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.assertj.core.util.Lists.newArrayList;
import static org.mockito.Mockito.*;

/**
 * Tests for <code>{@link IteratorAssert#toIterable()}</code>.
 *
 * @author Sára Juhošová
 */
class IteratorAssert_toIterable_Test {

  private IteratorAssert<Integer> assertions;

  @BeforeEach
  void setup() {
    assertions = new IteratorAssert<>(Arrays.stream(new Integer[]{5, 13, 42, 100}).iterator());
  }

  @Test
  public void should_return_equivalent_iterable() {
    IterableAssert<Integer> returned = assertions.toIterable();
    returned.containsExactly(5, 13, 42, 100);
  }
}
