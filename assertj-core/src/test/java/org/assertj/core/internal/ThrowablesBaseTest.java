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
package org.assertj.core.internal;

import static org.apache.commons.lang3.reflect.FieldUtils.writeField;
import static org.assertj.core.testkit.TestData.someInfo;
import static org.mockito.Mockito.spy;

import org.assertj.core.api.AssertionInfo;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;

public class ThrowablesBaseTest {

  protected static final AssertionInfo INFO = someInfo();
  protected Failures failures;
  protected Throwables throwables;
  protected static Throwable actual;

  @BeforeAll
  public static void setUpOnce() {
    actual = new NullPointerException("Throwable message");
  }

  @BeforeEach
  public void setUp() throws IllegalAccessException {
    failures = spy(Failures.instance());
    throwables = Throwables.instance();
    writeField(throwables, "failures", failures, true);
    Objects.instance().failures = failures;
  }

  @AfterEach
  public void tearDown() throws IllegalAccessException {
    writeField(throwables, "failures", Failures.instance(), true);
    Objects.instance().failures = Failures.instance();
  }

}
