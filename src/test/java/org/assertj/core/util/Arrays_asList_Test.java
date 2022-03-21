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
 * Copyright 2012-2022 the original author or authors.
 */
package org.assertj.core.util;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.assertj.core.util.Arrays.asList;
import static org.assertj.core.util.Lists.newArrayList;

import java.util.List;

import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

class Arrays_asList_Test {

  @ParameterizedTest
  @MethodSource("dataProvider")
  void should_return_a_list_corresponding_to_the_given_object(Object arrayAsObject, List<Object> expected) {
    assertThat(asList(arrayAsObject)).isEqualTo(expected);
  }

  public static Object[][] dataProvider() {
    return new Object[][] {
        { new String[0], newArrayList() },
        { new String[] { "a", "b", "c" }, newArrayList("a", "b", "c") },
        { new int[] { 1, 2, 3 }, newArrayList(1, 2, 3) }
    };
  }

  @ParameterizedTest
  @MethodSource("notArrays")
  void should_throw_IllegalArgumentException_if_given_object_is_not_an_array(final Object notArray,
                                                                             final String error) {
    // WHEN
    Throwable throwable = asListCall(notArray);
    // THEN
    assertThat(throwable).isInstanceOf(IllegalArgumentException.class)
                         .hasMessage(error);
  }

  private static Throwable asListCall(final Object actual) {
    return catchThrowable(new ThrowingCallable() {
      @Override
      public void call() throws Exception {
        asList(actual);
      }
    });
  }

  public static Object[][] notArrays() {
    return new Object[][] {
        { null, "Given object null is not an array" },
        { "abc", "Given object abc is not an array" },
        { 123, "Given object 123 is not an array" }
    };
  }

}
