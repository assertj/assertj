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
 * Copyright 2012-2018 the original author or authors.
 */
package org.assertj.core.api.atomic.referencearray;

import static org.assertj.core.internal.ErrorMessages.nullSubsequence;
import static org.assertj.core.util.Arrays.array;
import static org.mockito.Mockito.verify;

import java.util.List;

import org.assertj.core.api.AtomicReferenceArrayAssert;
import org.assertj.core.api.AtomicReferenceArrayAssertBaseTest;
import org.assertj.core.util.Lists;
import org.junit.Test;

/**
 * Tests for <code>{@link AtomicReferenceArrayAssert#doesNotContainSubsequence(List)}</code>.
 *
 * @author Chris Arnott
 */
public class AtomicReferenceArrayAssert_doesNotContainSubsequence_List_Test extends AtomicReferenceArrayAssertBaseTest {

  @Override
  protected AtomicReferenceArrayAssert<Object> invoke_api_method() {
    // AtomicReferenceArrayAssertBaseTest is testing AtomicReferenceArray<Object>, so the List type needs to be Object
    // or the {@link AtomicReferenceArrayAssert#doesNotContainsSubSequence(Object...)} method is called.
    return assertions.doesNotContainSubsequence(Lists.newArrayList((Object) "Luke", "Yoda"));
  }

  @Override
  protected void verify_internal_effects() {
    verify(arrays).assertDoesNotContainSubsequence(info(), internalArray(), array("Luke", "Yoda"));
  }

  @Test
  public void should_throw_error_if_subsequence_is_null() {
    thrown.expectNullPointerException(nullSubsequence());
    List<Object> nullList = null;
    assertions.doesNotContainSubsequence(nullList);
  }
}
