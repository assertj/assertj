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
 * Copyright 2012-2022 the original author or authors.
 */
package org.assertj.core.internal;

import static org.mockito.Mockito.spy;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;

/**
 *
 * Base class for {@link Throwables} tests.
 * <p>
 * Is in <code>org.assertj.core.internal</code> package to be able to set {@link Throwables#failures} appropriately.
 *
 * @author Joel Costigliola
 */
public class ThrowablesBaseTest {

  protected Failures failures;
  protected Throwables throwables;
  protected static Throwable actual;

  @BeforeAll
  public static void setUpOnce() {
    actual = new NullPointerException("Throwable message");
  }

  @BeforeEach
  public void setUp() {
    failures = spy(new Failures());
    throwables = new Throwables();
    throwables.failures = failures;
    Objects.instance().failures = failures;
  }

  @AfterEach
  public void tearDown() {
    Objects.instance().failures = Failures.instance();
  }

}
