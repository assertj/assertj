/*
 * Copyright 2012-2026 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.assertj.core.internal.objects;

import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.error.ShouldBeEqual.shouldBeEqual;
import static org.assertj.core.testkit.TestData.someInfo;
import static org.assertj.core.util.AssertionsUtil.expectAssertionError;

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.internal.ObjectsBaseTest;
import org.junit.jupiter.api.Test;

/**
 * Tests for <code>{@link Objects#assertThat(AssertionInfo, Object).isNull()}</code>.
 * 
 * @author Alex Ruiz
 * @author Joel Costigliola
 */
class Objects_assertNull_Test extends ObjectsBaseTest {

  @Test
  void should_pass_if_object_is_null() {
    objects.assertNull(someInfo(), null);
  }

  @Test
  void should_fail_if_object_is_not_null() {
    // GIVEN
    AssertionInfo info = someInfo();
    Object actual = new Object();
    // WHEN
    var error = expectAssertionError(() -> objects.assertNull(info, actual));
    // THEN
    then(error).hasMessage(shouldBeEqual(actual, null, info.representation()).toAssertionError(info).getMessage());
  }
}
