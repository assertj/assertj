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
package org.assertj.core.api.iterable;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

import java.util.List;
import java.util.function.Function;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

/**
 * @author Johannes Schneider (<a href="mailto:js@cedarsoft.com">js@cedarsoft.com</a>)
 */
class Iterable_generics_with_varargs_Test {

  @SuppressWarnings({ "unchecked", "rawtypes" })
  @Test
  void testWithoutGenerics() {
    List strings = asList("a", "b", "c");
    assertThat(strings).contains("a", "b");
  }

  @Test
  void testConcreteType() {
    List<String> strings = asList("a", "b", "c");
    assertThat(strings).contains("a", "b");
  }

  @Test
  @Disabled
  void testListAssertWithGenerics() {
    // List<? extends String> strings = asList("a", "b", "c");
    // does not compile as Java 8 is stricter with generics ...
    // assertThat(strings).contains("a", "b");
  }

  @Test
  void testListAssertWithGenericsAndExtracting() {
    List<? extends String> strings = asList("a", "b", "c");
    Function<? super String, String> doubleFunction = new Function<String, String>() {
      @Override
      public String apply(String s) {
        return s + s;
      }
    };
    assertThat(strings)
                       .extracting(doubleFunction, doubleFunction)
                       .contains(
                                 tuple("aa", "aa"),
                                 tuple("bb", "bb"));
  }

}
