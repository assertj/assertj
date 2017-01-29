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
package org.assertj.core.presentation;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Mariusz Smykula
 */
public class NumberGrouping_Test {

  @Test
  public void should_group_words_in_byte_hex_value() {
    String hexLiteral = NumberGrouping.toHexLiteral("CA");
    assertThat(hexLiteral).isEqualTo("CA");
  }

  @Test
  public void should_group_words_in_hex_value() {
    String hexLiteral = NumberGrouping.toHexLiteral("01234567");
    assertThat(hexLiteral).isEqualTo("0123_4567");
  }

  @Test
  public void should_group_bytes_in_integer() {
    String literals = NumberGrouping.toBinaryLiteral("00000000000000000000000000000011");
    assertThat(literals).isEqualTo("00000000_00000000_00000000_00000011");
  }

  @Test
  public void should_group_bytes_in_short() {
    String literals = NumberGrouping.toBinaryLiteral("1000000000000011");
    assertThat(literals).isEqualTo("10000000_00000011");
  }

}
