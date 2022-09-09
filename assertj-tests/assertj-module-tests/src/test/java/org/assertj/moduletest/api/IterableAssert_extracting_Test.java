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
package org.assertj.moduletest.api;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

class IterableAssert_extracting_Test {

    @BeforeEach
    void setUp() {

    }

    @Test
    void should_allow_assertions_on_property_values_extracted_from_given_iterable() {
        List<String> names = List.of("sith", "jedi", "yoda");

        List<TestObject> testObjects = names.stream().map(TestObject::new).collect(Collectors.toList());

        assertThat(testObjects).extractingResultOf("name").containsAll(names);


    }

    static final class TestObject {
        private final String name;

        TestObject(String name) {
            this.name = name;
        }

        public String name() {
            return name;
        }
    }

}

