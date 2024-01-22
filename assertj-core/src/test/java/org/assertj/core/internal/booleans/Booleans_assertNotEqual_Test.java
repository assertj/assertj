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
package org.assertj.core.internal.booleans;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;
import static org.assertj.core.error.ShouldNotBeEqual.shouldNotBeEqual;
import static org.assertj.core.util.AssertionsUtil.expectAssertionError;
import static org.mockito.Mockito.verify;

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.internal.Booleans;
import org.assertj.core.internal.BooleansBaseTest;
import org.junit.jupiter.api.Test;

/**
 * Tests for <code>{@link Booleans#assertNotEqual(AssertionInfo, Boolean, boolean)}</code>.
 * 
 * @author Alex Ruiz
 * @author Joel Costigliola
 */
class Booleans_assertNotEqual_Test extends BooleansBaseTest {

  @Test
  void should_pass_if_actual_is_null_since_the_other_argument_cannot_be_null() {
    booleans.assertNotEqual(INFO, null, false);
    booleans.assertNotEqual(INFO, null, FALSE);
    booleans.assertNotEqual(INFO, null, true);
    booleans.assertNotEqual(INFO, null, TRUE);
  }

  @Test
  void should_pass_if_booleans_are_not_equal() {
    booleans.assertNotEqual(INFO, true, false);
    booleans.assertNotEqual(INFO, true, FALSE);
    booleans.assertNotEqual(INFO, TRUE, false);
    booleans.assertNotEqual(INFO, TRUE, FALSE);
    booleans.assertNotEqual(INFO, false, true);
    booleans.assertNotEqual(INFO, false, TRUE);
    booleans.assertNotEqual(INFO, FALSE, true);
    booleans.assertNotEqual(INFO, FALSE, TRUE);
  }

  @Test
  void should_fail_if_booleans_are_equal() {
    // GIVEN
    boolean actual = TRUE;
    boolean expected = true;
    // WHEN
    expectAssertionError(() -> booleans.assertNotEqual(INFO, actual, expected));
    // THEN
    verify(failures).failure(INFO, shouldNotBeEqual(actual, expected));
  }
}
