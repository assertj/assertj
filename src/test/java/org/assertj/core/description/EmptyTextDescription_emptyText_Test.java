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
import static org.assertj.core.description.EmptyTextDescription.emptyDescription;

import org.junit.Test;

/**
 * Tests for <code>{@link EmptyTextDescription#emptyDescription()}</code>.
 * 
 * @author Yvonne Wang
 */
public class EmptyTextDescription_emptyText_Test {

  @Test
  public void should_return_singleton_instance() {
    Description description = emptyDescription();
    for (int i = 0; i < 6; i++)
      assertThat(emptyDescription()).isSameAs(description);
  }

  @Test
  public void should_have_empty_text_as_value() {
    assertThat(emptyDescription().value()).isEmpty();
  }
}
