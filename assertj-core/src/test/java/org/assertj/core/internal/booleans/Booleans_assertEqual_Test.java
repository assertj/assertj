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
import static java.lang.String.format;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.error.ShouldBeEqual.shouldBeEqual;
import static org.assertj.core.util.AssertionsUtil.expectAssertionError;
import static org.mockito.Mockito.verify;

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.internal.Booleans;
import org.assertj.core.internal.BooleansBaseTest;
import org.junit.jupiter.api.Test;

/**
 * Tests for <code>{@link Booleans#assertEqual(AssertionInfo, Boolean, boolean)}</code>.
 * 
 * @author Alex Ruiz
 * @author Joel Costigliola
 */
class Booleans_assertEqual_Test extends BooleansBaseTest {

  @Test
  void should_fail_if_actual_is_null_since_the_other_argument_cannot_be_null() {
    // GIVEN
    Boolean actual = null;
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> booleans.assertEqual(INFO, actual, true));
    // THEN
    then(assertionError).hasMessage(format("%n" +
                                           "expected: true%n" +
                                           " but was: null"));
  }

  @Test
  void should_pass_if_booleans_are_equal() {
    booleans.assertEqual(INFO, true, true);
    booleans.assertEqual(INFO, TRUE, true);
    booleans.assertEqual(INFO, FALSE, false);
    booleans.assertEqual(INFO, false, false);
  }

  @Test
  void should_fail_if_booleans_are_not_equal() {
    // GIVEN
    boolean actual = TRUE;
    boolean expected = false;
    // WHEN
    expectAssertionError(() -> booleans.assertEqual(INFO, actual, expected));
    // THEN
    verify(failures).failure(INFO, shouldBeEqual(actual, expected, INFO.representation()));
  }
}
