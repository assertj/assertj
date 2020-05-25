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
 * Copyright 2012-2020 the original author or authors.
 */
package org.assertj.core.api.recursive.comparison;

import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.api.recursive.comparison.Color.GREEN;
import static org.assertj.core.api.recursive.comparison.ColorWithCode.RED;
import static org.assertj.core.util.Lists.list;

import java.util.List;
import java.util.stream.Stream;

import org.assertj.core.internal.objects.data.FriendlyPerson;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

@DisplayName("DualValue hasPotentialCyclingValues")
public class DualValue_hasPotentialCyclingValues_Test {

  private static final List<String> PATH = list("foo", "bar");

  @ParameterizedTest(name = "actual {0} / expected {1}")
  @MethodSource("values")
  void should_return_false_when_actual_or_expected_is_a_container_type_and_true_otherwise(Object actual, Object expected,
                                                                                          boolean expectedResult) {
    // GIVEN
    DualValue dualValue = new DualValue(PATH, actual, expected);
    // WHEN
    boolean hasPotentialCyclingValuess = dualValue.hasPotentialCyclingValues();
    // THEN
    then(hasPotentialCyclingValuess).isEqualTo(expectedResult);
  }

  static Stream<Arguments> values() {
    FriendlyPerson person1 = new FriendlyPerson();
    FriendlyPerson person2 = new FriendlyPerson();
    person1.otherFriends.add(person1);
    person1.otherFriends.add(person2);
    person2.otherFriends.add(person2);
    person2.otherFriends.add(person1);

    class LocalClass {
      @Override
      public String toString() {
        return "LocalClass";
      }
    }

    return Stream.of(Arguments.of(null, person2, false),
                     Arguments.of(person1, null, false),
                     Arguments.of(person1, "abc", false),
                     Arguments.of("abc", person2, false),
                     Arguments.of("abc", 2, false),
                     Arguments.of((byte) 1, (byte) 2, false),
                     Arguments.of((short) 1, (short) 2, false),
                     Arguments.of(1, 2, false),
                     Arguments.of(1.0, 2.0, false),
                     Arguments.of(1.0f, 2.0f, false),
                     Arguments.of('a', 'b', false),
                     Arguments.of(person1, person1, true),
                     Arguments.of(person1, person2, true),
                     Arguments.of(list(person1), list(person1), true),
                     Arguments.of(list(person1), list(person2), true),
                     Arguments.of(new LocalClass(), new LocalClass(), true),
                     Arguments.of(new Light(GREEN), new Light(GREEN), true),
                     Arguments.of(new Theme(RED), new Theme(RED), true), // for #1866
                     Arguments.of(new DualValue_hasPotentialCyclingValues_Test().new Inner(),
                                  new DualValue_hasPotentialCyclingValues_Test().new Inner(), true),
                     Arguments.of(list(person1, person2), list(person2, person1), true));
  }

  class Inner {
    @Override
    public String toString() {
      return "Inner Class";
    }

  }

}
