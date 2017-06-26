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

import static java.util.concurrent.CompletableFuture.completedFuture;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

import org.junit.Before;
import org.junit.Test;

public class Assertions_assertThat_with_ComplertionStage_Test {

  private CompletionStage<String> completionStage;

  @Before
  public void setup() {
    completionStage = completedFuture("done");
  }

  @Test
  public void should_create_Assert() {
    Object assertions = assertThat(completionStage);
    assertThat(assertions).isNotNull();
  }

  @Test
  public void should_initialise_actual() {
    CompletableFuture<String> actual = assertThat(completionStage).actual;
    assertThat(actual).isDone()
                      .hasNotFailed();
  }

  @Test
  public void should_allow_null() {
    CompletableFuture<String> actual = assertThat((CompletionStage<String>) null).actual;
    assertThat(actual).isNull();
  }

}
