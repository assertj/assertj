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
package org.assertj.tests.core.api.collection;

import org.springframework.http.HttpHeaders;

import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Stream;

import static java.lang.String.CASE_INSENSITIVE_ORDER;
import static org.assertj.core.util.Arrays.array;

abstract class CollectionAssertBaseTest {

  // https://github.com/assertj/assertj/issues/4175
  static Stream<?> setContainingArrayElements() {
    return Stream.of(Set.of(array("foo"), array("bar")));
  }

  static Stream<?> caseInsensitiveSets() {
    Set<String> caseInsensitiveTreeSet = new TreeSet<>(CASE_INSENSITIVE_ORDER);
    caseInsensitiveTreeSet.add("foo");

    // https://github.com/assertj/assertj/issues/4157
    HttpHeaders springHttpHeaders = new HttpHeaders();
    springHttpHeaders.add("foo", "value");
    Set<String> springHttpHeaderNames = springHttpHeaders.headerNames();

    return Stream.of(caseInsensitiveTreeSet, springHttpHeaderNames);
  }

}
