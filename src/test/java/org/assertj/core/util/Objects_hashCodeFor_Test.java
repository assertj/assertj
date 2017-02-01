/**
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 *
 * Copyright 2012-2017 the original author or authors.
 */
package org.assertj.core.util;

import static java.util.Arrays.deepHashCode;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.util.Arrays.array;
import static org.assertj.core.util.Objects.hashCodeFor;

import org.junit.Test;

/**
 * Tests for {@link Objects#hashCodeFor(Object)}.
 * 
 * @author Yvonne Wang
 * @author Alex Ruiz
 * @author Joel Costigliola
 */
public class Objects_hashCodeFor_Test {

  @Test
  public void should_return_hashCode_of_given_Object() {
    assertThat(hashCodeFor("Yoda")).isEqualTo("Yoda".hashCode());
  }

  @Test
  public void should_return_hashCode_of_intarray(){
    int[] intArray = new int[] { 1,2,3 };
    assertThat(hashCodeFor(intArray)).isEqualTo(intArray.hashCode());
  }

  @Test
  public void should_return_Arrays_deepHashCode_of_given_array() {
    String[][] array = new String[][] { array("Yoda") };
    assertThat(hashCodeFor(array)).isEqualTo(deepHashCode(array));
    int[][] intArray = new int[][] { new int[] { 5 } };
    assertThat(hashCodeFor(intArray)).isEqualTo(deepHashCode(intArray));
  }

  @Test
  public void should_return_zero_if_Object_is_null() {
	assertThat(hashCodeFor(null)).isZero();
  }
}
