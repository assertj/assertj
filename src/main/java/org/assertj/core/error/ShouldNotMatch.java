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
 * Copyright 2012-2017 the original author or authors.
 */
package org.assertj.core.error;

import java.util.function.Predicate;

import org.assertj.core.presentation.PredicateDescription;

import static java.lang.String.format;
import static java.util.Objects.requireNonNull;

/**
 * Creates an error message indicating that an assertion that verifies that a value not satisfying a
 * <code>{@link Predicate}</code> failed.
 */
public class ShouldNotMatch extends BasicErrorMessageFactory {

  // @format:off
  public static final String ADVICE = format("%n%n"+
	                                         "You can use 'doesNotMatch(Predicate p, String description)' to have a better error message%n" +
	                                         "For example:%n" +
	                                         "  assertThat(player).doesNotMatch(p -> p.isRookie(), \"is not rookie\");%n" +
	                                         "will give an error message looking like:%n" +
	                                         "%n" +
	                                         "Expecting:%n" +
	                                         "  <player>%n" +
	                                         "not to match 'is not rookie' predicate");
  // @format:on

  /**
   * Creates a new <code>{@link ShouldNotMatch}</code>.
   *
   * @param <T> guarantees that the type of the actual value and the generic type of the {@code Predicate} are the same.
   * @param actual the actual value in the failed assertion.
   * @param predicate the {@code Predicate}.
   * @param predicateDescription predicate description to include in the error message
   * @return the created {@code ErrorMessageFactory}.
   */
  public static <T> ErrorMessageFactory shouldNotMatch(T actual, Predicate<? super T> predicate,
                                                       PredicateDescription predicateDescription) {
	requireNonNull(predicateDescription, "The predicate description must not be null");
	return new ShouldNotMatch(actual, predicate, predicateDescription);
  }

  private ShouldNotMatch(Object actual, Predicate<?> predicate, PredicateDescription description) {
	super("%nExpecting:%n  <%s>%nnot to match %s predicate." + (description.isDefault() ? ADVICE : ""), actual, description);
  }
}
