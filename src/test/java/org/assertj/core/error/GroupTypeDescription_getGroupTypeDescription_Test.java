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

import org.assertj.core.test.Jedi;
import org.junit.jupiter.params.provider.Arguments;
import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.error.GroupTypeDescription.getGroupTypeDescription;
import static org.assertj.core.test.Maps.mapOf;
import static org.assertj.core.util.Lists.list;
import static org.assertj.core.util.Sets.newLinkedHashSet;

class GroupTypeDescription_getGroupTypeDescription_Test {

  @ParameterizedTest
  @MethodSource("argumentsStreamProvider")
  void getGroupTypeDescription_test(Object obj, String groupTypeName, String elementTypeName) {
    GroupTypeDescription description = getGroupTypeDescription(obj);
    assertThat(description.getGroupTypeName()).isEqualTo(groupTypeName);
    assertThat(description.getElementTypeName()).isEqualTo(elementTypeName);

  }

  private static Stream<Arguments> argumentsStreamProvider() {
    return Stream.of(Arguments.of(mapOf(entry("1", 2d)), "map", "map entries"),
                     Arguments.of(new int[] { 1, 2 }, "array int[]", "int"),
                     Arguments.of(new double[] { 1, 2 }, "array double[]", "double"),
                     Arguments.of(new float[] { 1f, 2f }, "array float[]", "float"),
                     Arguments.of(new byte[] { 1, 2 }, "array byte[]", "byte"),
                     Arguments.of(new long[] { 1L, 2L }, "array long[]", "long"),
                     Arguments.of(new boolean[] { true }, "array boolean[]", "boolean"),
                     Arguments.of(new char[] { 1, 2 }, "array char[]", "char"),
                     Arguments.of(new short[] { 1, 2 }, "array short[]", "short"),
                     Arguments.of(new String[] { "a", "c" }, "array String[]", "String"),
                     Arguments.of(new Object[] { "a", "c" }, "array Object[]", "elements"),
                     Arguments.of(new Jedi[] { new Jedi("Yoda", "green") }, "array Jedi[]", "elements"),
                     Arguments.of(list(1, 2), "ArrayList", "elements"),
                     Arguments.of(newLinkedHashSet(1, 2), "LinkedHashSet", "elements")

    );
  }

}
