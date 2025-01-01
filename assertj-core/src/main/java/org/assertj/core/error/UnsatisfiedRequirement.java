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

import static java.lang.String.format;

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.presentation.Representation;

public class UnsatisfiedRequirement {

  private final Object elementNotSatisfyingRequirements;
  private final String errorMessage;
  private final AssertionError error;

  /**
   * @param elementNotSatisfyingRequirements object not satisfying the requirement
   * @param errorMessage                     requirement error message
   * @deprecated use {@link UnsatisfiedRequirement#UnsatisfiedRequirement(Object, AssertionError)} instead.
   */
  @Deprecated
  public UnsatisfiedRequirement(Object elementNotSatisfyingRequirements, String errorMessage) {
    this.elementNotSatisfyingRequirements = elementNotSatisfyingRequirements;
    this.errorMessage = errorMessage;
    this.error = null;
  }

  public UnsatisfiedRequirement(Object elementNotSatisfyingRequirements, AssertionError error) {
    this.elementNotSatisfyingRequirements = elementNotSatisfyingRequirements;
    this.errorMessage = error.getMessage();
    this.error = error;
  }

  public String describe(AssertionInfo info) {
    Representation representation = info.representation();
    return format("%s%nerror: %s", representation.toStringOf(elementNotSatisfyingRequirements), describeError(representation));
  }

  @Override
  public String toString() {
    return format("%s %s", elementNotSatisfyingRequirements, errorMessage);
  }

  public String describe(int index, AssertionInfo info) {
    Representation representation = info.representation();
    return format("%s%n" +
                  "- element index: %s%n" +
                  "- error: %s",
                  representation.toStringOf(elementNotSatisfyingRequirements), index, describeError(representation));
  }

  private String describeError(Representation representation) {
    return error != null ? representation.toStringOf(error) : errorMessage;
  }
}
