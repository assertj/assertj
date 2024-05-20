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
package org.assertj.tests.core.testkit;

import java.util.function.BiPredicate;

public class BiPredicates {

  public static final BiPredicate<Object, Object> ALWAYS_EQUALS = (o1, o2) -> true;
  public static final BiPredicate<Object, Object> ALWAYS_DIFFERENT = (o1, o2) -> false;
  public static final BiPredicate<String, String> STRING_EQUALS = (s1, s2) -> s1.equalsIgnoreCase(s2);
  public static final BiPredicate<Double, Double> DOUBLE_EQUALS = (d1, d2) -> Math.abs(d1 - d2) <= 0.01;

}
