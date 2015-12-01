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
 * Copyright 2012-2015 the original author or authors.
 */
package org.assertj.core.internal.strings;

import static org.assertj.core.test.ErrorMessages.charSequenceToLookForIsNull;
import static org.assertj.core.test.TestData.someInfo;
import org.assertj.core.internal.StringsBaseTest;
import org.junit.Test;

/**
 * Tests for <code>{@link org.assertj.core.internal.Strings#assertNotEqualsIgnoringWhitespace(org.assertj.core.api.AssertionInfo, CharSequence, CharSequence)} </code>.
 *
 * @author Dan Corder
 */
public class Strings_assertNotEqualsIgnoringWhitespace_Test extends StringsBaseTest {

  @Test
  public void should_fail_if_actual_is_not_null_and_expected_is_null() {
    thrown.expectNullPointerException(charSequenceToLookForIsNull());
    strings.assertNotEqualsIgnoringWhitespace(someInfo(), "Luke", null);
  }
}
