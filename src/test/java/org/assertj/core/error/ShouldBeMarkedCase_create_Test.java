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
package org.assertj.core.error;

import static java.lang.String.format;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.error.ShouldBeMarked.shouldBeMarked;

import java.util.concurrent.atomic.AtomicMarkableReference;

import org.assertj.core.description.TextDescription;
import org.assertj.core.presentation.StandardRepresentation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ShouldBeMarkedCase_create_Test {

  private ErrorMessageFactory factory;

  @BeforeEach
  public void setUp() {
    factory = shouldBeMarked(new AtomicMarkableReference<>("actual", false));
  }

  @Test
  public void should_create_error_message() {
    String message = factory.create(new TextDescription("Test"), new StandardRepresentation());
    assertThat(message).isEqualTo(format("[Test] %nExpecting <AtomicMarkableReference[marked=false, reference=\"actual\"]> to be a marked but was not"));
  }
}
