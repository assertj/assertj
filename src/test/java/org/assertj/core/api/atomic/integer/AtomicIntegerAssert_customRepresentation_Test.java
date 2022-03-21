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
package org.assertj.core.api.atomic.integer;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

import java.util.concurrent.atomic.AtomicInteger;

import org.assertj.core.presentation.StandardRepresentation;
import org.junit.jupiter.api.Test;

class AtomicIntegerAssert_customRepresentation_Test {

  @Test
  void should_honor_customRepresentation() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> assertThat(new AtomicInteger(0)).withRepresentation(new CustomRepresentation())
                                                                                                     .isEqualTo(-1))
                                                   .withMessageContaining("@0@");
  }

  private class CustomRepresentation extends StandardRepresentation {

    @Override
    protected String toStringOf(AtomicInteger s) {
      return "@" + s + "@";
    }
  }

}
