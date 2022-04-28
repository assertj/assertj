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

import static org.assertj.core.test.TestData.someInfo;
import static org.assertj.core.util.Lists.newArrayList;
import static org.mockito.Mockito.spy;

import java.util.Iterator;

import org.assertj.core.api.AssertionInfo;
import org.junit.jupiter.api.BeforeEach;

/**
 * Base class for testing <code>{@link Iterators}</code>.
 * <p>
 * Is in <code>org.assertj.core.internal</code> package to be able to set {@link Iterators#failures} appropriately.
 *
 * @author Natália Struharová
 *
 */
public class IteratorsBaseTest {

  protected static final AssertionInfo INFO = someInfo();

  protected Iterator<String> actual;
  protected Failures failures;
  protected Iterators iterators;

  protected AssertionInfo info;

  @BeforeEach
  public void setUp() {
    actual = newArrayList("Luke", "Yoda", "Leia").iterator();
    failures = spy(new Failures());
    iterators = new Iterators();
    iterators.failures = failures;
    info = someInfo();
  }

}
