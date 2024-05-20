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
package org.assertj.core.error;

import static java.lang.String.format;

import org.assertj.core.api.AssertionInfo;

public class UnsatisfiedRequirement {

  private final Object elementNotSatisfyingRequirements;
  private final String errorMessage;

  public UnsatisfiedRequirement(Object elementNotSatisfyingRequirements, String errorMessage) {
    this.elementNotSatisfyingRequirements = elementNotSatisfyingRequirements;
    this.errorMessage = errorMessage;
  }

  public String describe(AssertionInfo info) {
    return format("%s%nerror: %s", info.representation().toStringOf(elementNotSatisfyingRequirements), errorMessage);
  }

  @Override
  public String toString() {
    return format("%s %s", elementNotSatisfyingRequirements, errorMessage);
  }

  public String describe(int index, AssertionInfo info) {
    return format("%s%n" +
                  "- element index: %s%n" +
                  "- error: %s",
                  info.representation().toStringOf(elementNotSatisfyingRequirements), index, errorMessage);
  }
}