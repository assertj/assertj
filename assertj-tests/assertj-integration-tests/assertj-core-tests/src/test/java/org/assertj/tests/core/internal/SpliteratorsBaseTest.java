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
 * Copyright 2012-2025 the original author or authors.
 */
package org.assertj.tests.core.internal;

import static org.apache.commons.lang3.reflect.FieldUtils.readField;
import static org.apache.commons.lang3.reflect.FieldUtils.writeField;
import static org.assertj.tests.core.testkit.TestData.someInfo;
import static org.mockito.Mockito.spy;

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.internal.Failures;
import org.assertj.core.internal.Spliterators;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

/**
 * Base class for {@link Spliterators} tests.
 *
 * @author William Bakker
 */
public class SpliteratorsBaseTest {

  protected static final AssertionInfo INFO = someInfo();

  protected Failures failures;
  protected Spliterators spliterators;

  @BeforeEach
  public void setUp() {
    failures = spy(Failures.instance());
    spliterators = new Spliterators();
    setFailures(spliterators, failures);
  }

  @AfterEach
  public void tearDown() {
    setFailures(spliterators, Failures.instance());
  }

  private void setFailures(Spliterators spliterators, Failures failures) {
    try {
      Object iterables = readField(spliterators, "iterables", true);
      writeField(iterables, "failures", failures, true);
    } catch (IllegalAccessException e) {
      throw new RuntimeException(e);
    }

  }

}
