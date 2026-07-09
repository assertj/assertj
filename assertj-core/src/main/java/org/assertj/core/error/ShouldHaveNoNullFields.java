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
package org.assertj.core.error;

import java.util.List;

/**
 * Creates an <code>{@link AssertionError}</code> indicating that an assertion that verifies that an object
 * has no null fields failed.
 *
 * @author Johannes Brodwall
 */
public class ShouldHaveNoNullFields extends BasicErrorMessageFactory {

  private static final String EXPECTED_MULTIPLE = "%nExpecting%n  %s%nto have a property or a field named %s.%n";
  private static final String EXPECTED_SINGLE = "%nExpecting%n  %s%nnot to have any null property or field, but %s was null.%n";
  private static final String COMPARISON = "Check was performed on all fields/properties";
  private static final String EXCLUDING = COMPARISON + " except: %s";
  private static final String DOT = ".";

  /**
   * Creates an error with null and ignored field lists.
   *
   * @param actual the actual object
   * @param rejectedFields the null fields
   * @param ignoredFields the ignored fields
   */
  public ShouldHaveNoNullFields(Object actual, List<String> rejectedFields, List<String> ignoredFields) {
    super(EXPECTED_MULTIPLE + EXCLUDING, actual, rejectedFields, ignoredFields);
  }

  /**
   * Creates an error with null fields.
   *
   * @param actual the actual object
   * @param rejectedFields the null fields
   */
  public ShouldHaveNoNullFields(Object actual, List<String> rejectedFields) {
    super(EXPECTED_MULTIPLE + COMPARISON + DOT, actual, rejectedFields);
  }

  /**
   * Creates an error with one null field.
   *
   * @param actual the actual object
   * @param rejectedField the null field
   */
  public ShouldHaveNoNullFields(Object actual, String rejectedField) {
    super(EXPECTED_SINGLE + COMPARISON + DOT, actual, rejectedField);
  }

  /**
   * Creates an error with one null field and ignored fields.
   *
   * @param actual the actual object
   * @param rejectedField the null field
   * @param ignoredFields the ignored fields
   */
  public ShouldHaveNoNullFields(Object actual, String rejectedField, List<String> ignoredFields) {
    super(EXPECTED_SINGLE + EXCLUDING, actual, rejectedField, ignoredFields);
  }

  /**
   * Creates an error for fields expected to be non-null.
   *
   * @param actual the actual object
   * @param rejectedFields the null fields
   * @param ignoredFields the ignored fields
   * @return the error message factory
   */
  public static ShouldHaveNoNullFields shouldHaveNoNullFieldsExcept(Object actual, List<String> rejectedFields,
                                                                    List<String> ignoredFields) {
    if (rejectedFields.size() == 1) {
      if (ignoredFields.isEmpty()) {
        return new ShouldHaveNoNullFields(actual, rejectedFields.get(0));
      }
      return new ShouldHaveNoNullFields(actual, rejectedFields.get(0), ignoredFields);
    }
    if (ignoredFields.isEmpty()) {
      return new ShouldHaveNoNullFields(actual, rejectedFields);
    }
    return new ShouldHaveNoNullFields(actual, rejectedFields, ignoredFields);
  }

}
