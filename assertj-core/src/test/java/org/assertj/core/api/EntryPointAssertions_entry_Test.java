/*
 * Copyright © 2024 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.assertj.core.api;

import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.data.MapEntry.entry;

import java.util.function.BiFunction;
import java.util.stream.Stream;
import org.assertj.core.data.MapEntry;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

@DisplayName("EntryPoint assertions entry method")
class EntryPointAssertions_entry_Test extends EntryPointAssertionsBaseTest {

  @ParameterizedTest
  @MethodSource("entryFactories")
  void should_create_entry(BiFunction<String, String, MapEntry<String, String>> entryFactory) {
    // GIVEN
    String key = "key";
    String value = "value";
    // WHEN
    MapEntry<String, String> entry = entryFactory.apply(key, value);
    // THEN
    then(entry).isEqualTo(entry(key, value));
  }

  private static <K, V> Stream<BiFunction<K, V, MapEntry<K, V>>> entryFactories() {
    return Stream.of(Assertions::entry, BDDAssertions::entry, withAssertions::entry);
  }

}
