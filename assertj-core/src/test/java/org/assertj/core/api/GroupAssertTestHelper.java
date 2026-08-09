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
package org.assertj.core.api;

import java.util.function.Function;

import org.assertj.core.api.iterable.ThrowingExtractor;
import org.assertj.core.internal.TypeComparators;
import org.assertj.core.testkit.Employee;
import org.assertj.core.util.introspection.PropertyOrFieldSupport;

public class GroupAssertTestHelper {

  // the field is always eagerly initialized by the assertion class itself; reflection-based retrieval is
  // just declared @Nullable generically, NullAway can't verify this specific field is always present.
  @SuppressWarnings("NullAway")
  public static TypeComparators comparatorsByTypeOf(AbstractIterableAssert<?, ?, ?, ?> assertion) {
    return (TypeComparators) PropertyOrFieldSupport.EXTRACTION.getValueOf("comparatorsByType", assertion);
  }

  @SuppressWarnings("NullAway")
  public static TypeComparators comparatorForElementFieldsWithTypeOf(AbstractIterableAssert<?, ?, ?, ?> assertion) {
    return (TypeComparators) PropertyOrFieldSupport.EXTRACTION.getValueOf("comparatorsForElementPropertyOrFieldTypes", assertion);
  }

  // employee.name is null only in the dedicated null-name test scenarios, never for callers of these helpers.
  @SuppressWarnings("NullAway")
  public static final Function<Employee, String> lastNameFunction = employee -> employee.name.getLast();
  @SuppressWarnings("NullAway")
  public static final Function<Employee, String> firstNameFunction = employee -> employee.name.first;
  @SuppressWarnings("NullAway")
  public static final ThrowingExtractor<Employee, String, Exception> throwingFirstNameExtractor = employee -> employee.name.first;
  @SuppressWarnings("NullAway")
  public static final ThrowingExtractor<Employee, String, Exception> throwingLastNameExtractor = employee -> employee.name.getLast();

}
