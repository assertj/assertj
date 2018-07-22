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
 * Copyright 2012-2018 the original author or authors.
 */
package org.assertj.core.api.assumptions;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assumptions.assumeThat;
import static org.assertj.core.util.Lists.newArrayList;

import java.util.List;
import java.util.stream.Stream;

import org.junit.AssumptionViolatedException;
import org.junit.jupiter.api.Test;

public class Assumptions_assumeThat_with_Stream_Test {

  @Test
  public void stream_test() {
    Stream<String> stream = Stream.of("test");
    assertThatExceptionOfType(AssumptionViolatedException.class).isThrownBy(() -> assumeThat(stream).containsAnyOf("other",
                                                                                                                   "foo"));
  }

  @Test
  public void list_test() {
    List<String> list = newArrayList("test");
    assertThatExceptionOfType(AssumptionViolatedException.class).isThrownBy(() -> assumeThat(list).contains("other",
                                                                                                            "foo"));
  }
}
