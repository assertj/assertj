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
 * Copyright 2012-2017 the original author or authors.
 */
package org.assertj.core.util;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.test.ExpectedException.none;

import org.assertj.core.test.ExpectedException;
import org.junit.Rule;
import org.junit.Test;

/**
 * Tests for <code>{@link Preconditions#checkNotNullOrEmpty(Object[])}</code>.
 * 
 * @author Christian Rösch
 */
public class Preconditions_checkNotNullOrEmpty_GenericArray_Test {
  @Rule
  public ExpectedException thrown = none();

  @Test
  public void should_throw_IllegalArgumentException_if_array_is_empty() {
    thrown.expectIllegalArgumentException(Preconditions.ARGUMENT_EMPTY);
    String[] array = new String[0];
    Preconditions.checkNotNullOrEmpty(array);
  }

  @Test
  public void should_throw_NullPointerException_if_array_is_null() {
    thrown.expectNullPointerException();
    String[] array = null;
    Preconditions.checkNotNullOrEmpty(array);
  }

  @Test
  public void should_return_array_if_it_is_not_null_nor_empty() {
    String[] array = new String[] { "a" };
    String[] result = Preconditions.checkNotNullOrEmpty(array);

    assertThat(result).isEqualTo(array);
  }
}
