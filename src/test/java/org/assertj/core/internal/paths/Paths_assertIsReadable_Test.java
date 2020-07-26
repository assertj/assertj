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
 * Copyright 2012-2020 the original author or authors.
 */
package org.assertj.core.internal.paths;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.ThrowableAssert.catchThrowable;
import static org.assertj.core.error.ShouldBeReadable.shouldBeReadable;
import static org.assertj.core.error.ShouldExist.shouldExist;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;

class Paths_assertIsReadable_Test extends MockPathsBaseTest {

  @Test
  void should_fail_if_actual_is_null() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> paths.assertIsReadable(info, null))
                                                   .withMessage(actualIsNull());
  }

  @Test
  void should_fail_with_should_exist_error_if_actual_does_not_exist() {
    when(nioFilesWrapper.exists(actual)).thenReturn(false);

    Throwable error = catchThrowable(() -> paths.assertIsReadable(info, actual));

    assertThat(error).isInstanceOf(AssertionError.class);
    verify(failures).failure(info, shouldExist(actual));
  }

  @Test
  void should_fail_if_actual_exists_but_is_not_readable() {
    when(nioFilesWrapper.exists(actual)).thenReturn(true);
    when(nioFilesWrapper.isReadable(actual)).thenReturn(false);

    Throwable error = catchThrowable(() -> paths.assertIsReadable(info, actual));

    assertThat(error).isInstanceOf(AssertionError.class);
    verify(failures).failure(info, shouldBeReadable(actual));
  }

  @Test
  void should_succeed_if_actual_exist_and_is_readable() {
    when(nioFilesWrapper.exists(actual)).thenReturn(true);
    when(nioFilesWrapper.isReadable(actual)).thenReturn(true);
    paths.assertIsReadable(info, actual);
  }

}
