/*
 * Copyright © 2024 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.assertj.core.api;

import static org.assertj.core.api.BDDAssertions.then;
import static org.mockito.Answers.CALLS_REAL_METHODS;
import static org.mockito.Mockito.verify;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import org.assertj.core.internal.ComparisonStrategy;
import org.assertj.core.internal.Objects;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class AbstractAssert_areEqual_Test {

  @Mock(answer = CALLS_REAL_METHODS)
  private AbstractAssert<?, Object> underTest;

  @Mock
  private ComparisonStrategy comparisonStrategy;

  @Test
  @SuppressWarnings("deprecation")
  void should_delegate_to_ComparableAssert() {
    // GIVEN
    underTest.objects = new Objects(comparisonStrategy);
    // WHEN
    underTest.areEqual(42, 43);
    // THEN
    verify(comparisonStrategy).areEqual(42, 43);
  }

  @Test
  void should_be_protected() throws NoSuchMethodException {
    // GIVEN
    Method areEqual = AbstractAssert.class.getDeclaredMethod("areEqual", Object.class, Object.class);
    // WHEN
    boolean isProtected = Modifier.isProtected(areEqual.getModifiers());
    // THEN
    then(isProtected).isTrue();
  }

}
