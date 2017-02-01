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
package org.assertj.core.description;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

import org.assertj.core.description.Description;
import org.junit.*;

/**
 * Tests for <code>{@link Description#toString()}</code>.
 * 
 * @author Yvonne Wang
 */
public class Description_toString_Test {

  private ValueSource valueSource;
  private Description description;

  @Before
  public void setUp() {
    valueSource = mock(ValueSource.class);
    description = new TestDescription(valueSource);
  }

  @Test
  public void should_return_value_in_toString() {
    when(valueSource.value()).thenReturn("Yoda");
    assertThat(description).hasToString("Yoda");
  }

  private static interface ValueSource {
    String value();
  }

  private static class TestDescription extends Description {
    private final ValueSource source;

    TestDescription(ValueSource source) {
      this.source = source;
    }

    @Override
    public String value() {
      return source.value();
    }
  }
}
