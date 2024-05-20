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
package org.assertj.core.api.objectarray;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.util.Arrays.array;
import static org.assertj.core.util.Lists.list;
import static org.assertj.core.util.ThrowingConsumerFactory.throwingConsumer;
import static org.mockito.Mockito.verify;

import org.assertj.core.api.ObjectArrayAssert;
import org.assertj.core.api.ObjectArrayAssertBaseTest;
import org.assertj.core.api.ThrowingConsumer;
import org.junit.jupiter.api.Test;

class ObjectArrayAssert_satisfiesExactlyInAnyOrder_with_ThrowingConsumer_Test extends ObjectArrayAssertBaseTest {

  private ThrowingConsumer<Object>[] requirements = array(element -> assertThat(element).isNotNull());

  @Override
  protected ObjectArrayAssert<Object> invoke_api_method() {
    return assertions.satisfiesExactlyInAnyOrder(requirements);
  }

  @Override
  protected void verify_internal_effects() {
    verify(iterables).assertSatisfiesExactlyInAnyOrder(getInfo(assertions), list(getActual(assertions)), requirements);
  }

  @Test
  void should_rethrow_throwables_as_runtime_exceptions() {
    // GIVEN
    Throwable exception = new Throwable("boom!");
    // WHEN
    Throwable throwable = catchThrowable(() -> assertThat(array("foo")).satisfiesExactlyInAnyOrder(throwingConsumer(exception)));
    // THEN
    then(throwable).isInstanceOf(RuntimeException.class)
                   .hasCauseReference(exception);
  }

  @Test
  void should_propagate_RuntimeException_as_is() {
    // GIVEN
    RuntimeException runtimeException = new RuntimeException("boom!");
    // WHEN
    Throwable throwable = catchThrowable(() -> assertThat(array("foo")).satisfiesExactlyInAnyOrder(throwingConsumer(runtimeException)));
    // THEN
    then(throwable).isSameAs(runtimeException);
  }

}
