/*
 * Created on Oct 8, 2010
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 *
 * Copyright @2010 the original author or authors.
 */

package org.fest.assertions.group;

import static java.util.Collections.emptyList;
import static org.fest.assertions.group.IsEmptyCheckerStub.*;
import static org.fest.util.Collections.list;
import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

/**
 * Tests for <code>{@link IsNullOrEmptyChecker#isNullOrEmpty(Object)}</code>.
 *
 * @author Alex Ruiz
 */
public class IsNullOrEmptyChecker_isNullOrEmpty_Test {

  private IsNullOrEmptyChecker checker;

  @Before public void setUp() {
    checker = new IsNullOrEmptyChecker();
  }

  @Test public void should_return_true_if_object_is_null() {
    assertTrue(checker.isNullOrEmpty(null));
  }

  @Test public void should_delegate_to_IsEmptyChecker_if_object_is_not_null() {
    Object o = emptyList();
    replaceDelegateCheckersWith(notMatchingChecker(), matchingChecker());
    assertTrue(checker.isNullOrEmpty(o));
  }

  @Test public void should_return_false_if_object_is_not_null_but_there_was_no_IsEmptyChecker_to_handle_it() {
    Object o = new Object();
    replaceDelegateCheckersWith(notMatchingChecker());
    assertFalse(checker.isNullOrEmpty(o));
  }

  private void replaceDelegateCheckersWith(IsEmptyChecker...checkers) {
    checker.checkers.clear();
    checker.checkers.addAll(list(checkers));
  }
}
