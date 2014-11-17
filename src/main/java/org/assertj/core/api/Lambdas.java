/**
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 *
 * Copyright 2012-2014 the original author or authors.
 */
package org.assertj.core.api;

import java.util.function.IntPredicate;
import java.util.function.Predicate;

import org.assertj.core.api.Condition;

/**
 * A utility class for handling Java 8 lambdas
 * @author arothkopf
 *
 */
public class Lambdas {

  /**
   * Conversion from lambda to Condition for non-primitive types
   * @param lambda
   * @return Condition which tests the predicate
   */
  public static <T> Condition<T> toCondition(final Predicate<T> lambda) {
	return new Condition<T>() {
	  @Override
	  public boolean matches(final T value) {
		return lambda.test(value);
	  }
	};
  }

  /**
   * Conversion from lambda to Condition for int
   * @param lambda
   * @return Condition which tests the predicate
   */
  public static Condition<Integer> toCondition(final IntPredicate lambda) {
	return new Condition<Integer>() {
	  @Override
	  public boolean matches(final Integer value) {
		return lambda.test(value);
	  }
	};
  }
}
