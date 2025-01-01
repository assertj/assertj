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
package org.assertj.core.api.recursive.comparison;

import static java.lang.String.format;

import org.assertj.core.internal.UnambiguousRepresentation;
import org.assertj.core.presentation.Representation;

public class ComparisonKeyDifference extends ComparisonDifference {

  static final String TEMPLATE_FOR_KEY_DIFFERENCE = "map key difference:%n" +
                                                    "- actual key  : %s%n" +
                                                    "- expected key: %s";

  final Object actualKey;
  final Object expectedKey;

  public ComparisonKeyDifference(DualValue dualValue, Object actualKey, Object expectedKey) {
    super(dualValue);
    this.actualKey = actualKey;
    this.expectedKey = expectedKey;
  }

  @Override
  public String toString() {
    return format("ComparisonDifference [path=%s, actualKey=%s, expectedKey=%s]", concatenatedPath, actualKey, expectedKey);
  }

  @Override
  public String multiLineDescription(Representation representation) {
    UnambiguousRepresentation unambiguousRepresentation = new UnambiguousRepresentation(representation, actual, expected);
    UnambiguousRepresentation unambiguousKeyRepresentation = new UnambiguousRepresentation(representation, actualKey,
                                                                                           expectedKey);
    return format(DEFAULT_TEMPLATE + "%n" + TEMPLATE_FOR_KEY_DIFFERENCE,
                  fieldPathDescription(),
                  unambiguousRepresentation.getActual(),
                  unambiguousRepresentation.getExpected(),
                  "",
                  unambiguousKeyRepresentation.getActual(),
                  unambiguousKeyRepresentation.getExpected());
  }
}
