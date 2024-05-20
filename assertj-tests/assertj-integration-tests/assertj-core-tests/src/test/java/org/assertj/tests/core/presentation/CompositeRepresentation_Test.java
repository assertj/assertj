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
package org.assertj.tests.core.presentation;

import static java.util.Collections.emptyList;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.presentation.StandardRepresentation.STANDARD_REPRESENTATION;
import static org.assertj.core.util.Lists.list;

import java.util.List;

import org.assertj.core.presentation.CompositeRepresentation;
import org.assertj.core.presentation.Representation;
import org.junit.jupiter.api.Test;

class CompositeRepresentation_Test extends AbstractBaseRepresentationTest {

  @Test
  void should_use_representation_with_highest_priority() {
    // GIVEN
    Representation representationP1 = representation(1);
    Representation representationP2 = representation(2);
    Representation representationP3 = representation(3);
    List<Representation> representations = list(representationP1, representationP3, representationP2);
    CompositeRepresentation compositeRepresentation = new CompositeRepresentation(representations);
    // WHEN
    String toString = compositeRepresentation.toStringOf("foo");
    String unambiguousToString = compositeRepresentation.unambiguousToStringOf("foo");
    // THEN
    then(toString).isEqualTo("3");
    then(unambiguousToString).isEqualTo("3");
  }

  @Test
  void should_use_standard_representation_if_composite_representation_is_not_given_any_specific_representation() {
    // GIVEN
    CompositeRepresentation compositeRepresentation = new CompositeRepresentation(emptyList());
    // WHEN
    Object longNumber = 123L;
    // THEN
    then(compositeRepresentation.toStringOf(longNumber)).isEqualTo(STANDARD_REPRESENTATION.toStringOf(longNumber));
    then(compositeRepresentation.unambiguousToStringOf(longNumber)).isEqualTo(STANDARD_REPRESENTATION.unambiguousToStringOf(longNumber));
  }

  @Test
  void should_throw_IllegalArgumentException_if_null_list_representations_is_given() {
    assertThatIllegalArgumentException().isThrownBy(() -> new CompositeRepresentation(null));
  }

  @Test
  void should_implement_toString() {
    // GIVEN
    Representation representationP1 = representation(1);
    Representation representationP2 = representation(2);
    CompositeRepresentation compositeRepresentation = new CompositeRepresentation(list(representationP2, representationP1));
    // WHEN/THEN
    then(compositeRepresentation).hasToString("[Representation2, Representation1]");
  }

  @Test
  void should_return_all_representations_used_in_order() {
    // GIVEN
    Representation representationP1 = representation(1);
    Representation representationP2 = representation(2);
    CompositeRepresentation compositeRepresentation = new CompositeRepresentation(list(representationP1, representationP2));
    // WHEN/THEN
    then(compositeRepresentation.getAllRepresentationsOrderedByPriority()).containsExactly(representationP2, representationP1,
                                                                                           STANDARD_REPRESENTATION);
  }

  private static Representation representation(int priority) {
    return new Representation() {

      @Override
      public int getPriority() {
        return priority;
      }

      @Override
      public String unambiguousToStringOf(Object object) {
        return "" + getPriority();
      }

      @Override
      public String toStringOf(Object object) {
        return "" + getPriority();
      }

      @Override
      public String toString() {
        return "Representation" + getPriority();
      }
    };
  }
}
