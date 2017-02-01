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
 * Tests for <code>{@link WritableAssertionInfo#toString()}</code>.
 * 
 * @author Alex Ruiz
 */
public class WritableAssertionInfo_toString_Test {

  private Description description;
  private String overridingErrorMessage;
  private WritableAssertionInfo info;

  @Before
  public void setUp() {
    description = mock(Description.class);
    overridingErrorMessage = "Jedi";
    info = new WritableAssertionInfo();
    info.description(description);
    info.overridingErrorMessage(overridingErrorMessage);
  }

  @Test
  public void should_implement_toString() {
    when(description.value()).thenReturn("Yoda");
    assertThat(info).hasToString("WritableAssertionInfo[overridingErrorMessage='Jedi', description='Yoda', representation=StandardRepresentation]");
  }
}
