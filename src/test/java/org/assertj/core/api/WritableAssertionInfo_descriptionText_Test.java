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
package org.assertj.core.api;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.assertj.core.description.Description;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests for <code>{@link WritableAssertionInfo#descriptionText()}</code>.
 * 
 * @author Yvonne Wang
 */
public class WritableAssertionInfo_descriptionText_Test {

  private WritableAssertionInfo info;

  @Before
  public void setUp() {
    info = new WritableAssertionInfo();
  }

  @Test
  public void should_return_empty_if_no_description_was_set() {
    assertThat(info.descriptionText()).isEmpty();
  }

  @Test
  public void should_return_text_of_description() {
    Description description = mock(Description.class);
    info.description(description);
    when(description.value()).thenReturn("Yoda");
    assertThat(info.descriptionText()).isEqualTo("Yoda");
  }
}
