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
package org.assertj.core.error;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.error.BasicErrorMessageFactory.unquotedString;

import org.junit.Test;

/**
 * Tests for <code>{@link BasicErrorMessageFactory#unquotedString(String)}</code>.
 */
public class BasicErrorMessageFactory_unquotedString_Test {

  @Test
  public void should_implement_toString() {
    assertThat(unquotedString("some value")).hasToString("some value");
  }
}
