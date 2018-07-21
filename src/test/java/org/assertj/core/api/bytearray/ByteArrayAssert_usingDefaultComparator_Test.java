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
package org.assertj.core.api.bytearray;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.MockitoAnnotations.initMocks;

import java.util.Comparator;


import org.assertj.core.api.ByteArrayAssert;
import org.assertj.core.api.ByteArrayAssertBaseTest;
import org.assertj.core.internal.ByteArrays;
import org.assertj.core.internal.Objects;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mock;

/**
 * Tests for <code>{@link ByteArrayAssert#usingDefaultComparator()}</code>.
 * 
 * @author Joel Costigliola
 * @author Mikhail Mazursky
 */
public class ByteArrayAssert_usingDefaultComparator_Test extends ByteArrayAssertBaseTest {

  @Mock
  private Comparator<byte[]> comparator;

  private ByteArrays arraysBefore;

  @BeforeEach
  public void before() {
    initMocks(this);
    assertions.usingComparator(comparator);
    arraysBefore = getArrays(assertions);
  }

  @Override
  protected ByteArrayAssert invoke_api_method() {
    return assertions.usingDefaultComparator();
  }

  @Override
  protected void verify_internal_effects() {
    assertThat(Objects.instance()).isSameAs(getObjects(assertions));
    assertThat(arraysBefore).isSameAs(getArrays(assertions));
  }
}
