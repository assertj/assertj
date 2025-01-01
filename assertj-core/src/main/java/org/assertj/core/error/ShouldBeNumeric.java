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
 * Copyright 2012-2025 the original author or authors.
 */
package org.assertj.core.error;

/**
 * Creates an error message that indicates an assertion that cast string
 * to a number (byte, short, integer, long, float or double) failed.
 *
 * @author hezean
 */
public class ShouldBeNumeric extends BasicErrorMessageFactory {

  public enum NumericType {
    BYTE("byte"), SHORT("short"), INTEGER("int"), LONG("long"), FLOAT("float"), DOUBLE("double");

    private final String typeName;

    NumericType(String typeName) {
      this.typeName = typeName;
    }

    @Override
    public String toString() {
      return typeName;
    }

  }

  /**
   * Creates a new <code>{@link ShouldBeNumeric}</code>.
   * @param actual the actual value in the failed assertion.
   * @param type the type expected to cast.
   * @return the created {@code ErrorMessageFactory}.
   */
  public static ErrorMessageFactory shouldBeNumeric(String actual, NumericType type) {
    return new ShouldBeNumeric(actual, type);
  }

  private ShouldBeNumeric(String actual, NumericType type) {
    super("%nExpecting %s to be a valid %s", actual, type);
  }
}
