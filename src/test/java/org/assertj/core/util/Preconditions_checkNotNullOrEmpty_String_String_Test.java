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
package org.assertj.core.util;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;

import org.junit.jupiter.api.Test;

/**
 * Tests for <code>{@link Preconditions#checkNotNullOrEmpty(String, String)}</code>.
 * 
 * @author Christian Rösch
 */
public class Preconditions_checkNotNullOrEmpty_String_String_Test {
  private final static String CUSTOM_MESSAGE = "Wow, that's an error dude ..";

  @Test
  public void should_throw_illegalargumentexception_if_string_is_empty() {
    assertThatIllegalArgumentException().isThrownBy(() -> {
      String string = "";
      Preconditions.checkNotNullOrEmpty(string, CUSTOM_MESSAGE);
    }).withMessage(CUSTOM_MESSAGE);
  }

  @Test
  public void should_throw_nullpointerexception_if_string_is_null() {
    assertThatNullPointerException().isThrownBy(() -> {
      String string = null;
      Preconditions.checkNotNullOrEmpty(string, CUSTOM_MESSAGE);
    });
  }

  @Test
  public void should_return_string_if_it_is_not_null_nor_empty() {
    String string = "a";
    CharSequence result = Preconditions.checkNotNullOrEmpty(string, CUSTOM_MESSAGE);

    assertThat(result).isEqualTo(string);
  }
}
