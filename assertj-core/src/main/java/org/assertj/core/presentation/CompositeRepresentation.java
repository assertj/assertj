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
package org.assertj.core.presentation;

import static java.util.Comparator.comparingInt;
import static java.util.stream.Collectors.toList;
import static org.assertj.core.presentation.StandardRepresentation.STANDARD_REPRESENTATION;

import java.util.ArrayList;
import java.util.List;

public class CompositeRepresentation implements Representation {

  private final List<Representation> representations;

  public CompositeRepresentation(List<Representation> representations) {
    if (representations == null) throw new IllegalArgumentException("The given representations should not be null");
    this.representations = representations.stream()
                                          .sorted(comparingInt(Representation::getPriority).reversed())
                                          .collect(toList());
  }

  @Override
  public String toStringOf(Object object) {
    // don't create streams for performance reasons and because this code is simple enough (even not as elegant as with stream)
    for (Representation representation : representations) {
      String value = representation.toStringOf(object);
      if (value != null) return value;
    }
    return STANDARD_REPRESENTATION.toStringOf(object);
  }

  @Override
  public String unambiguousToStringOf(Object object) {
    // don't create streams for performance reasons and because this code is simple enough (even not as elegant as with stream)
    for (Representation representation : representations) {
      String value = representation.unambiguousToStringOf(object);
      if (value != null) return value;
    }
    return STANDARD_REPRESENTATION.unambiguousToStringOf(object);
  }

  @Override
  public String toString() {
    return representations.isEmpty() ? STANDARD_REPRESENTATION.toString() : representations.toString();
  }

  public List<Representation> getAllRepresentationsOrderedByPriority() {
    List<Representation> representationsOrderedByPriority = new ArrayList<>(representations);
    representationsOrderedByPriority.add(STANDARD_REPRESENTATION);
    return representationsOrderedByPriority;
  }
}
