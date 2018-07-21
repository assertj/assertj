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
 * Copyright 2012-2018 the original author or authors.
 */
package org.assertj.core.api.atomic.integerarray;

import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;

import java.util.Comparator;


import org.assertj.core.api.AtomicIntegerArrayAssert;
import org.assertj.core.api.AtomicIntegerArrayAssertBaseTest;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mock;

public class AtomicIntegerArrayAssert_isSortedAccordingToComparator_Test extends AtomicIntegerArrayAssertBaseTest {

  @Mock
  private Comparator<Integer> comparator;

  @BeforeEach
  public void before() {
    initMocks(this);
  }

  @Override
  protected AtomicIntegerArrayAssert invoke_api_method() {
    return assertions.isSortedAccordingTo(comparator);
  }

  @Override
  protected void verify_internal_effects() {
    verify(arrays).assertIsSortedAccordingToComparator(info(), internalArray(), comparator);
  }
}
