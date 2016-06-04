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
 * Copyright 2012-2016 the original author or authors.
 */
package org.assertj.core.error;

import java.util.function.Predicate;

import org.assertj.core.presentation.PredicateDescription;

import static java.lang.String.format;
import static java.util.Objects.requireNonNull;

/**
 * Creates an error message indicating that an assertion that verifies that
 * <code>{@link Predicate}</code> not accepting a value failed.
 *
 * @author Filip Hrisafov
 */
public class ShouldNotAccept extends BasicErrorMessageFactory {

  // @format:off
  public static final String ADVICE = format("%n%n"+
	                                         "You can use " +
                                           "'assertThat(Predicate p).as(String|Description).doesNotAccept(\"something\")' " +
                                           "to have a better error message%n" +
	                                         "For example:%n" +
                                           " Predicate<Integer> p = i -> i <= 2;%n" +
	                                         "assertThat(p).as(\"smaller or equal to 2\").doesNot(2);%n" +
	                                         "will give an error message looking like:%n" +
	                                         "%n" +
	                                         "Expecting:%n" +
	                                         "  <'smaller or equal to' predicate>%n" +
	                                         "not to accept <2> but it did");
  // @format:on

  /**
   * Creates a new </code>{@link ShouldNotAccept}</code>.
   *
   * @param <T> guarantees that the type of the value value and the generic type of the {@code Predicate} are the same.
   * @param predicate the {@code Predicate}.
   * @param value the value value in the failed assertion.
   * @param description predicate description to include in the error message,
   * @return the created {@code ErrorMessageFactory}.
   */
  public static <T> ErrorMessageFactory shouldNotAccept(Predicate<? super T> predicate, T value,
                                                        PredicateDescription description) {
    requireNonNull(description, "The predicate description must not be null");
    return new ShouldNotAccept(predicate, value, description);
  }

  private ShouldNotAccept(Predicate<?> predicate, Object value, PredicateDescription description) {
    super("%nExpecting:%n  <%s predicate>%nnot to accept <%s> but it did." + (description.isDefault() ? ADVICE : ""),
          description, value);
  }
}
