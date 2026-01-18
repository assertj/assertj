/*
 * Copyright 2012-2026 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.assertj.core.api.map;

import static org.assertj.core.api.BDDAssertions.then;

import java.util.Map;
import java.util.function.BiPredicate;

import org.junit.jupiter.api.Test;

class MapAssert_usingEqualsForValues_Test {
  @Test
  void should_accept_supertype_equals_bipredicate() {
    // GIVEN
    var pet = new Pet();
    Map<String, Pet> map = Map.of("key", pet);
    BiPredicate<Animal, Animal> valuesEqualsPredicate = (v1, v2) -> v1 == v2;
    // WHEN/THEN
    then(map).usingEqualsForValues(valuesEqualsPredicate)
             .containsEntry("key", pet);
  }
}

class Animal {
}

class Pet extends Animal {
}
