/*
 * Copyright 2012-2026 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.assertj.core.internal;

import static org.mockito.Mockito.spy;

import org.assertj.core.api.comparisonstrategy.ComparatorBasedComparisonStrategy;
import org.assertj.core.error.AssertionErrorCreator;
import org.assertj.core.testkit.AbsValueComparator;
import org.junit.jupiter.api.BeforeEach;

/**
 * Base class for {@link Bytes} unit tests
 * <p>
 * Is in <code>org.assertj.core.internal</code> package to be able to set {@link Bytes#assertionErrorCreator} appropriately.
 * 
 * @author Joel Costigliola
 * 
 */
public class BytesBaseTest {

  protected AssertionErrorCreator assertionErrorCreator;
  protected Bytes bytes;
  protected ComparatorBasedComparisonStrategy absValueComparisonStrategy;
  protected Bytes bytesWithAbsValueComparisonStrategy;

  @BeforeEach
  public void setUp() {
    assertionErrorCreator = spy(AssertionErrorCreator.instance());
    bytes = new Bytes();
    bytes.setAssertionErrorCreator(assertionErrorCreator);
    absValueComparisonStrategy = new ComparatorBasedComparisonStrategy(new AbsValueComparator<Byte>());
    bytesWithAbsValueComparisonStrategy = new Bytes(absValueComparisonStrategy);
    bytesWithAbsValueComparisonStrategy.assertionErrorCreator = assertionErrorCreator;
  }

  protected void resetAssertionErrorCreator() {
    bytes.resetAssertionErrorCreator();
    bytesWithAbsValueComparisonStrategy.resetAssertionErrorCreator();
  }
}
