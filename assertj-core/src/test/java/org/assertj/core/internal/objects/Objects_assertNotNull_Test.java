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
package org.assertj.core.internal.objects;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.assertj.core.error.ShouldNotBeNull.shouldNotBeNull;
import static org.assertj.core.test.TestData.someInfo;
import static org.mockito.Mockito.verify;

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.internal.Objects;
import org.assertj.core.internal.ObjectsBaseTest;
import org.junit.jupiter.api.Test;


/**
 * Tests for <code>{@link Objects#assertNotNull(AssertionInfo, Object)}</code>.
 * 
 * @author Alex Ruiz
 * @author Joel Costigliola
 */
class Objects_assertNotNull_Test extends ObjectsBaseTest {

  @Test
  void should_pass_if_object_is_not_null() {
    objects.assertNotNull(someInfo(), "Luke");
  }

  @Test
  void should_fail_if_object_is_null() {
    AssertionInfo info = someInfo();

    Throwable error = catchThrowable(() -> objects.assertNotNull(info, null));

    assertThat(error).isInstanceOf(AssertionError.class);
    verify(failures).failure(info, shouldNotBeNull());
  }
}
