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
package org.assertj.core.internal.files;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.assertj.core.error.ShouldHaveName.shouldHaveName;
import static org.assertj.core.test.TestData.someInfo;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.internal.FilesBaseTest;
import org.junit.jupiter.api.Test;

/**
 * Tests for
 * <code>{@link org.assertj.core.internal.Files#assertHasName(org.assertj.core.api.AssertionInfo, java.io.File, String)} </code>
 * .
 * 
 * @author Jean-Christophe Gay
 */
class Files_assertHasName_Test extends FilesBaseTest {

  private String expectedName = "expected.name";

  @Test
  void should_throw_error_if_actual_is_null() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> files.assertHasName(someInfo(), null, expectedName))
                                                   .withMessage(actualIsNull());
  }

  @Test
  void should_throw_npe_if_name_is_null() {
    assertThatNullPointerException().isThrownBy(() -> files.assertHasName(someInfo(), actual, null))
                                    .withMessage("The expected name should not be null.");
  }

  @Test
  void should_throw_error_if_actual_does_not_have_the_expected_name() {
    AssertionInfo info = someInfo();
    when(actual.getName()).thenReturn("not.expected.name");

    Throwable error = catchThrowable(() -> files.assertHasName(info, actual, expectedName));

    assertThat(error).isInstanceOf(AssertionError.class);
    verify(failures).failure(info, shouldHaveName(actual, expectedName));
  }

  @Test
  void should_pass_if_actual_has_expected_name() {
    when(actual.getName()).thenReturn(expectedName);
    files.assertHasName(someInfo(), actual, expectedName);
  }
}
