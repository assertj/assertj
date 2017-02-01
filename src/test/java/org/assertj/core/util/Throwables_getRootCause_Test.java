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

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

/**
 * Tests for {@link Throwables#getRootCause(Throwable)}.
 * 
 * @author Jean-Christophe Gay
 */
public class Throwables_getRootCause_Test {

  @Test
  public void should_return_null_if_throwable_has_no_cause() {
    assertThat(Throwables.getRootCause(new Throwable())).isNull();
  }

  @Test
  public void should_return_cause_when_throwable_has_cause() {
    IllegalArgumentException expectedCause = new IllegalArgumentException();
    assertThat(Throwables.getRootCause(new Throwable(expectedCause))).isSameAs(expectedCause);
  }

  @Test
  public void should_return_root_cause_when_throwable_has_cause_which_has_cause() {
    NullPointerException expectedCause = new NullPointerException();
    Throwable error = new Throwable(new IllegalArgumentException(expectedCause));

    assertThat(Throwables.getRootCause(error)).isSameAs(expectedCause);
  }
}
