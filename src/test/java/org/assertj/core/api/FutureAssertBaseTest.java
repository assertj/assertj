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

import static org.assertj.core.test.ExpectedException.none;
import static org.mockito.Mockito.mock;

import java.util.concurrent.Callable;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.Future;

import org.assertj.core.internal.Futures;
import org.assertj.core.test.ExpectedException;
import org.junit.Rule;

public abstract class FutureAssertBaseTest extends BaseTestTemplate<FutureAssert<String>, Future<String>> {

  @Rule
  public ExpectedException thrown = none();

  protected Futures futures;

  @Override
  protected FutureAssert<String> create_assertions() {
    return new FutureAssert<>(ForkJoinTask.adapt(new Callable<String>() {
      @Override
      public String call() throws Exception {
        return "string";
      }
    }));
  }

  @Override
  protected void inject_internal_objects() {
    super.inject_internal_objects();
    futures = mock(Futures.class);
    assertions.futures = futures;
  }
}
