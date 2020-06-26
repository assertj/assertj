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
package org.assertj.core.api.abstract_;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

import java.util.function.Supplier;

import org.assertj.core.api.AbstractAssert;
import org.junit.jupiter.api.Test;

/**
 * Tests for <code>{@link org.assertj.core.api.AbstractAssert#withFailMessage(String, Object...)}</code>.
 *
 * @author Alexander Bischof
 */
public class AbstractAssert_withFailMessage_Test {

  @Test
  public void should_delegate_to_overridingErrorMessage() {
    // GIVEN
    AbstractAssert<?, ?> suT = spy(assertThat("test"));
    // WHEN
    suT.withFailMessage("test", "eins");
    // THEN
    verify(suT).overridingErrorMessage("test", "eins");
  }

  @Test
  public void should_delegate_to_overridingErrorMessage_using_supplier() {
    // GIVEN
    AbstractAssert<?, ?> suT = spy(assertThat("test"));
    Supplier<String> supplier = () -> "test";
    // WHEN
    suT.withFailMessage(supplier);
    // THEN
    verify(suT).overridingErrorMessage(supplier);
  }
}
