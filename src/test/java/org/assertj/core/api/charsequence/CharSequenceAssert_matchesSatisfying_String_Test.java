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
package org.assertj.core.api.charsequence;

import java.util.function.Consumer;

import org.assertj.core.api.CharSequenceAssert;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for <code>{@link CharSequenceAssert#containsPatternSatisfying(CharSequence, Consumer)}</code>.
 */
public class CharSequenceAssert_matchesSatisfying_String_Test {
  @Test
  public void matchesSatisfying() {
    assertThat("Yoda").matchesSatisfying(
            "(Yo)da",
            matcher -> assertThat(matcher.group(1)).isEqualTo("Yo"));
  }
}
