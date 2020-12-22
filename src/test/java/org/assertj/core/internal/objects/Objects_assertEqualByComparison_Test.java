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
 * Copyright 2012-2021 the original author or authors.
 */
package org.assertj.core.internal.objects;

import static org.assertj.core.api.Assertions.catchThrowable;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.test.TestData.someInfo;

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.internal.Objects;
import org.assertj.core.internal.ObjectsBaseTest;
import org.junit.jupiter.api.Test;

/**
 * Tests for <code>{@link Objects#assertEqualByComparison(AssertionInfo, Object, Object)}</code>.
 * 
 * @author Stefano Cordio
 */
class Objects_assertEqualByComparison_Test extends ObjectsBaseTest {

  @Test
  void should_fail_if_comparison_strategy_is_not_comparator_based() {
    // WHEN
    Throwable thrown = catchThrowable(() -> objects.assertEqualByComparison(someInfo(), new Object(), new Object()));
    // THEN
    then(thrown).isInstanceOf(UnsupportedOperationException.class);
  }

  @Test
  void should_pass_if_objects_are_equal_according_to_comparator_based_comparison_strategy() {

  }

}
