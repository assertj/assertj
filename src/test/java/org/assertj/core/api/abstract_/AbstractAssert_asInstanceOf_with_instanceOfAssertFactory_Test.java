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

import org.assertj.core.api.AbstractAssert;
import org.assertj.core.api.Assert;
import org.assertj.core.api.ConcreteAssert;
import org.assertj.core.api.InstanceOfAssertFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * Tests for <code>{@link AbstractAssert#asInstanceOf(InstanceOfAssertFactory)}</code>.
 */
@ExtendWith(MockitoExtension.class)
class AbstractAssert_asInstanceOf_with_instanceOfAssertFactory_Test {

  private AbstractAssert<?, ?> underTest;

  @Mock
  private InstanceOfAssertFactory<?, ?> mockFactory;

  @Mock
  private Assert<?, ?> mockAssert;

  private final Object actual = 6L;

  @BeforeEach
  void setUp() {
    underTest = new ConcreteAssert(actual);
  }

  @Test
  void should_return_factory_result() {
    // GIVEN
    willReturn(mockAssert).given(mockFactory).createAssert(actual);
    // WHEN
    Assert<?, ?> result = underTest.asInstanceOf(mockFactory);
    // THEN
    assertThat(result).isSameAs(mockAssert);
  }

}
