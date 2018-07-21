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
package org.assertj.core.api.objectarray;

import static org.assertj.core.api.Assertions.assertThatNullPointerException;
import static org.assertj.core.internal.ErrorMessages.nullSequence;
import static org.assertj.core.util.Arrays.array;
import static org.mockito.Mockito.verify;

import java.util.List;

import org.assertj.core.api.ObjectArrayAssert;
import org.assertj.core.api.ObjectArrayAssertBaseTest;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Test;

/**
 * Tests for <code>{@link ObjectArrayAssert#doesNotContainSequence(List)}</code>.
 *
 * @author Chris Arnott
 */
public class ObjectArrayAssert_doesNotContainSequence_List_Test extends ObjectArrayAssertBaseTest {

  @Override
  protected ObjectArrayAssert<Object> invoke_api_method() {
    // ObjectArrayAssertBaseTest is testing Object[], so the List type needs to be Object
    // or the {@link ObjectArrayAssert#containsSequence(Object...)} method is called.
    return assertions.doesNotContainSequence(Lists.newArrayList((Object) "Luke", "Yoda"));
  }

  @Override
  protected void verify_internal_effects() {
    verify(arrays).assertDoesNotContainSequence(getInfo(assertions), getActual(assertions), array("Luke", "Yoda"));
  }

  @Test
  public void should_throw_error_if_sequence_is_null() {
    assertThatNullPointerException().isThrownBy(() -> {
      List<Object> nullList = null;
      assertions.doesNotContainSequence(nullList);
    }).withMessage(nullSequence());
  }
}
