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
 * Copyright 2012-2019 the original author or authors.
 */
package org.assertj.core.util;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * Tests for <code>{@link ArrayWrapperList#size()}</code>.
 * 
 * @author Alex Ruiz
 * @author Dan Corder
 */
public class ArrayWrapperList_size_Test {

  public static Stream<Arguments> arrays() {
    return Stream.of(Arguments.of(new int[] { 0, 1, 2 }),
                     Arguments.of(new int[] { 0 }),
                     Arguments.of(new int[] {}));
  }

  @ParameterizedTest
  @MethodSource("arrays")
  public void should_return_size_of_array(int[] array) {
    ArrayWrapperList list = new ArrayWrapperList(array);
    assertThat(list.size()).isEqualTo(array.length);
  }
}
