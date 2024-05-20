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

import static org.assertj.core.api.Assertions.entry;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.error.GroupTypeDescription.getGroupTypeDescription;
import static org.assertj.core.testkit.Maps.mapOf;
import static org.assertj.core.util.Lists.list;
import static org.assertj.core.util.Sets.newLinkedHashSet;
import static org.junit.jupiter.params.provider.Arguments.arguments;

import java.util.stream.Stream;
import org.assertj.core.testkit.Jedi;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

@DisplayName("GroupTypeDescription getGroupTypeDescription")
class GroupTypeDescription_getGroupTypeDescription_Test {

  @ParameterizedTest(name = "{0}: {1} {2} ")
  @MethodSource("argumentsStreamProvider")
  void should_return_group_description(Object obj, String groupTypeName, String elementTypeName) {
    // WHEN
    GroupTypeDescription description = getGroupTypeDescription(obj);
    // THEN
    then(description.getGroupTypeName()).isEqualTo(groupTypeName);
    then(description.getElementTypeName()).isEqualTo(elementTypeName);
  }

  @ParameterizedTest(name = "{0}: {1} {2} ")
  @MethodSource("argumentsStreamProvider")
  void should_return_group_description_from_class(Object obj, String groupTypeName, String elementTypeName) {
    // WHEN
    GroupTypeDescription description = getGroupTypeDescription(obj.getClass());
    // THEN
    then(description.getGroupTypeName()).isEqualTo(groupTypeName);
    then(description.getElementTypeName()).isEqualTo(elementTypeName);
  }

  private static Stream<Arguments> argumentsStreamProvider() {
    return Stream.of(arguments(mapOf(entry("1", 2d)), "map", "map entries"),
                     arguments(new int[] { 1, 2 }, "int[]", "int(s)"),
                     arguments(new double[] { 1, 2 }, "double[]", "double(s)"),
                     arguments(new float[] { 1f, 2f }, "float[]", "float(s)"),
                     arguments(new byte[] { 1, 2 }, "byte[]", "byte(s)"),
                     arguments(new long[] { 1L, 2L }, "long[]", "long(s)"),
                     arguments(new boolean[] { true }, "boolean[]", "boolean(s)"),
                     arguments(new char[] { 'a', 'b' }, "char[]", "char(s)"),
                     arguments(new short[] { 1, 2 }, "short[]", "short(s)"),
                     arguments(new String[] { "a", "c" }, "String[]", "string(s)"),
                     arguments(new Object[] { "a", "c" }, "Object[]", "object(s)"),
                     arguments(new Jedi[] { new Jedi("Yoda", "green") }, "Jedi[]", "jedi(s)"),
                     arguments(list(1, 2), "ArrayList", "element(s)"),
                     arguments(newLinkedHashSet(1, 2), "LinkedHashSet", "element(s)"));
  }

}
