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
 * Copyright 2012-2019 the original author or authors.
 */
package org.assertj.core.api;

import java.util.Map;
import java.util.function.Function;

import org.assertj.core.api.iterable.ThrowingExtractor;
import org.assertj.core.internal.TypeComparators;
import org.assertj.core.test.Employee;
import org.assertj.core.util.introspection.PropertyOrFieldSupport;

public class GroupAssertTestHelper {

  public static TypeComparators comparatorsByTypeOf(AbstractIterableAssert<?, ?, ?, ?> assertion) {
    return (TypeComparators) PropertyOrFieldSupport.EXTRACTION.getValueOf("comparatorsByType", assertion);
  }

  public static TypeComparators comparatorForElementFieldsWithTypeOf(AbstractIterableAssert<?, ?, ?, ?> assertion) {
    return (TypeComparators) PropertyOrFieldSupport.EXTRACTION.getValueOf("comparatorsForElementPropertyOrFieldTypes", assertion);
  }

  public static Map<?, ?> comparatorForElementFieldsWithNamesOf(AbstractIterableAssert<?, ?, ?, ?> assertion) {
    return (Map<?, ?>) PropertyOrFieldSupport.EXTRACTION.getValueOf("comparatorsForElementPropertyOrFieldNames", assertion);
  }

  public static final Function<Employee, String> lastNameFunction = employee -> employee.name.getLast();
  public static final Function<Employee, String> firstNameFunction = employee -> employee.name.first;
  public static final ThrowingExtractor<Employee, String, Exception> throwingFirstNameExtractor = employee -> employee.name.first;

}
