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
 * Copyright 2012-2024 the original author or authors.
 */
package org.assertj.core.internal.files;

import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.error.ShouldHaveNoParent.shouldHaveNoParent;
import static org.assertj.core.util.AssertionsUtil.expectAssertionError;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.mockito.Mockito.verify;

import java.io.File;

import org.assertj.core.internal.FilesBaseTest;
import org.junit.jupiter.api.Test;

/**
 * Tests for
 * <code>{@link org.assertj.core.internal.Files#assertHasNoParent(org.assertj.core.api.AssertionInfo, java.io.File)}</code>
 * .
 * 
 * @author Jean-Christophe Gay
 */
class Files_assertHasNoParent_Test extends FilesBaseTest {

  @Test
  void should_throw_error_if_actual_is_null() {
    // GIVEN
    File actual = null;
    // WHEN
    AssertionError error = expectAssertionError(() -> underTest.assertHasNoParent(INFO, actual));
    // THEN
    then(error).hasMessage(actualIsNull());
  }

  @Test
  void should_fail_if_actual_has_parent() {
    // GIVEN
    File actual = new File("x/y/z");
    // WHEN
    expectAssertionError(() -> underTest.assertHasNoParent(INFO, actual));
    // THEN
    verify(failures).failure(INFO, shouldHaveNoParent(actual));
  }

  @Test
  void should_pass_if_actual_has_no_parent() {
    File actual = new File("xyz");
    underTest.assertHasNoParent(INFO, actual);
  }
}
