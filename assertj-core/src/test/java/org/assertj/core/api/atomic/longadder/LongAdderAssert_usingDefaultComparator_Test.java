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
package org.assertj.core.api.atomic.longadder;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Comparator;
import java.util.concurrent.atomic.LongAdder;

import org.assertj.core.api.LongAdderAssert;
import org.assertj.core.api.LongAdderAssertBaseTest;
import org.assertj.core.internal.Longs;
import org.assertj.core.internal.Objects;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * Tests for <code>{@link LongAdderAssert#usingDefaultComparator()}</code>.
 *
 * @author Stefano Cordio
 */
@ExtendWith(MockitoExtension.class)
class LongAdderAssert_usingDefaultComparator_Test extends LongAdderAssertBaseTest {

  @Mock
  private Comparator<LongAdder> comparator;

  @BeforeEach
  void before() {
    assertions.usingComparator(comparator);
  }

  @Override
  protected LongAdderAssert invoke_api_method() {
    return assertions.usingDefaultComparator();
  }

  @Override
  protected void verify_internal_effects() {
    assertThat(getObjects(assertions)).isSameAs(Objects.instance());
    assertThat(getLongs(assertions)).isSameAs(Longs.instance());
  }

}
