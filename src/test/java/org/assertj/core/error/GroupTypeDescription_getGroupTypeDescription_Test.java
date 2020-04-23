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
package org.assertj.core.error;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.entry;
import static org.assertj.core.error.GroupTypeDescription.getGroupTypeDescription;
import static org.assertj.core.test.Maps.mapOf;
import static org.assertj.core.util.Lists.list;
import static org.assertj.core.util.Sets.newLinkedHashSet;

import java.util.stream.Stream;

import org.assertj.core.test.Jedi;
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
    assertThat(description.getGroupTypeName()).isEqualTo(groupTypeName);
    assertThat(description.getElementTypeName()).isEqualTo(elementTypeName);
  }

  private static Stream<Arguments> argumentsStreamProvider() {
    return Stream.of(Arguments.of(mapOf(entry("1", 2d)), "map", "map entries"),
                     Arguments.of(new int[] { 1, 2 }, "int[]", "int(s)"),
                     Arguments.of(new double[] { 1, 2 }, "double[]", "double(s)"),
                     Arguments.of(new float[] { 1f, 2f }, "float[]", "float(s)"),
                     Arguments.of(new byte[] { 1, 2 }, "byte[]", "byte(s)"),
                     Arguments.of(new long[] { 1L, 2L }, "long[]", "long(s)"),
                     Arguments.of(new boolean[] { true }, "boolean[]", "boolean(s)"),
                     Arguments.of(new char[] { 'a', 'b' }, "char[]", "char(s)"),
                     Arguments.of(new short[] { 1, 2 }, "short[]", "short(s)"),
                     Arguments.of(new String[] { "a", "c" }, "String[]", "string(s)"),
                     Arguments.of(new Object[] { "a", "c" }, "Object[]", "object(s)"),
                     Arguments.of(new Jedi[] { new Jedi("Yoda", "green") }, "Jedi[]", "jedi(s)"),
                     Arguments.of(list(1, 2), "ArrayList", "element(s)"),
                     Arguments.of(newLinkedHashSet(1, 2), "LinkedHashSet", "element(s)"));
  }

}
