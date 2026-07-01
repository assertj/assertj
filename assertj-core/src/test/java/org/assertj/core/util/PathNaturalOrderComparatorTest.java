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
package org.assertj.core.util;

import static org.assertj.core.api.BDDAssertions.then;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("PathNaturalOrderComparator")
class PathNaturalOrderComparatorTest {

  @Test
  public void should_return_a_user_friendly_description() {
    then(PathNaturalOrderComparator.INSTANCE).hasToString("lexicographic comparator (Path natural order)");
  }

  @Test
  public void should_return_zero_when_comparing_equal_paths() {
    Path a = Paths.get("/foo/bar");
    then(PathNaturalOrderComparator.INSTANCE.compare(a, a)).isZero();
  }

  @Test
  public void should_return_negative_when_first_path_is_less_than_second() {
    Path a = Paths.get("/aaa");
    Path b = Paths.get("/zzz");
    then(PathNaturalOrderComparator.INSTANCE.compare(a, b)).isNegative();
  }

  @Test
  public void should_return_positive_when_first_path_is_greater_than_second() {
    Path a = Paths.get("/zzz");
    Path b = Paths.get("/aaa");
    then(PathNaturalOrderComparator.INSTANCE.compare(a, b)).isPositive();
  }

  @Test
  public void should_return_negative_when_first_argument_is_null() {
    Path p = Paths.get("/foo");
    then(PathNaturalOrderComparator.INSTANCE.compare(null, p)).isEqualTo(-1);
  }

  @Test
  public void should_return_positive_when_second_argument_is_null() {
    Path p = Paths.get("/foo");
    then(PathNaturalOrderComparator.INSTANCE.compare(p, null)).isEqualTo(1);
  }

  @Test
  public void should_return_zero_when_both_arguments_are_null() {
    then(PathNaturalOrderComparator.INSTANCE.compare(null, null)).isZero();
  }
}
