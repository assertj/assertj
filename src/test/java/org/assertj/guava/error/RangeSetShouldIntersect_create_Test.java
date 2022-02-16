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
 * Copyright 2012-2022 the original author or authors.
 */
package org.assertj.guava.error;

import static com.google.common.collect.ImmutableRangeSet.of;
import static com.google.common.collect.Range.closed;
import static com.google.common.collect.Range.open;
import static java.lang.String.format;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.util.Arrays.array;
import static org.assertj.core.util.IterableUtil.iterable;
import static org.assertj.guava.error.RangeSetShouldIntersect.shouldIntersect;

import org.assertj.core.description.TextDescription;
import org.assertj.core.error.ErrorMessageFactory;
import org.assertj.core.presentation.StandardRepresentation;
import org.junit.jupiter.api.Test;

/**
 * Tests for
 * <code>{@link RangeSetShouldIntersect#create(org.assertj.core.description.Description, org.assertj.core.presentation.Representation)}</code>
 *
 * @author Ilya_Koshaleu
 */
public class RangeSetShouldIntersect_create_Test {

  @Test
  void should_create_error_message() {
    // GIVEN
    ErrorMessageFactory factory = shouldIntersect(of(closed(0, 10)),
                                                  array(closed(2, 15), open(-5, 0)),
                                                  iterable(open(-5, 0)));
    // WHEN
    String message = factory.create(new TextDescription("Test"), StandardRepresentation.STANDARD_REPRESENTATION);
    // THEN
    assertThat(message).isEqualTo(format("[Test] %n" +
                                         "Expecting:%n" +
                                         "  [[0..10]]%n" +
                                         "to intersect%n" +
                                         "  [[2..15], (-5..0)]%n" +
                                         "but it does not intersect%n" +
                                         "  [(-5..0)]%n"));
  }
}
