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
 * Copyright 2012-2019 the original author or authors.
 */
package org.assertj.core.api.abstract_;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.willReturn;
import static org.mockito.MockitoAnnotations.initMocks;

import org.assertj.core.api.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

/**
 * Tests for <code>{@link AbstractAssert#instanceOf(InstanceOfAssertFactory)}</code>.
 */
public class AbstractAssert_instanceOf_Test extends AbstractAssertBaseTest {

  @Mock
  private InstanceOfAssertFactory<?, ?> mockFactory;

  @Mock
  private Assert<?, ?> mockAssert;

  @BeforeEach
  public void initMockito() {
    initMocks(this);
  }

  @Test
  public void should_return_factory_result() {
    // Given
    willReturn(mockAssert).given(mockFactory).createAssert(actual);

    // When
    Assert<?, ?> result = assertions.instanceOf(mockFactory);

    // Then
    assertThat(result).isSameAs(mockAssert);
  }

  @Override
  public void should_have_internal_effects() {
    // Test disabled, instanceOf has no internal effect
  }

  @Override
  protected ConcreteAssert invoke_api_method() {
    // instanceOf does not return SELF
    return null;
  }

  @Override
  protected void verify_internal_effects() {
    // instanceOf has no internal effect
  }

  @Override
  public void should_return_this() {
    // Test disabled, instanceOf does not return SELF
  }

}
