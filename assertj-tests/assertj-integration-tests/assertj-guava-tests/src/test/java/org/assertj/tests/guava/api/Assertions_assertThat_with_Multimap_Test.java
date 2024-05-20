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
package org.assertj.tests.guava.api;

import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.guava.api.Assertions.assertThat;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import org.assertj.guava.api.MultimapAssert;
import org.junit.jupiter.api.Test;

/**
 * @author Joel Costigliola
 */
class Assertions_assertThat_with_Multimap_Test {

  @Test
  void should_create_Assert() {
    // GIVEN
    Multimap<String, String> actual = HashMultimap.create();
    // WHEN
    MultimapAssert<String, String> assertion = assertThat(actual);
    // THEN
    then(assertion).isNotNull();
  }

}
