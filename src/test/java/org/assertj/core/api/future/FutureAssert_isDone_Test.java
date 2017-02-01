/**
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 *
 * Copyright 2012-2017 the original author or authors.
 */
package org.assertj.core.api.future;

import org.assertj.core.api.FutureAssert;
import org.assertj.core.api.FutureAssertBaseTest;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.concurrent.Future;

public class FutureAssert_isDone_Test extends FutureAssertBaseTest {

  @Override
  protected FutureAssert<String> invoke_api_method() {
    return assertions.isDone();
  }

  @Override
  protected void verify_internal_effects() {
    verify(futures).assertIsDone(getInfo(assertions), getActual(assertions));
  }

  @Test
  public void should_fail_if_actual_is_not_done() {
    Future<?> actual = mock(Future.class);
    when(actual.isDone()).thenReturn(false);

    thrown.expectAssertionErrorWithMessageContaining("to be done");

    assertThat(actual).isDone();
  }
}
