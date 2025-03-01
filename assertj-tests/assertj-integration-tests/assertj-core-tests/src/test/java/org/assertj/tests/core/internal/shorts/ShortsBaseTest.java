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
package org.assertj.tests.core.internal.shorts;

import static org.assertj.tests.core.testkit.FieldTestUtils.writeField;
import static org.mockito.Mockito.spy;

import org.assertj.core.internal.ComparatorBasedComparisonStrategy;
import org.assertj.core.internal.Failures;
import org.assertj.core.internal.Shorts;
import org.assertj.core.internal.StandardComparisonStrategy;
import org.assertj.tests.core.testkit.AbsValueComparator;
import org.junit.jupiter.api.BeforeEach;

public class ShortsBaseTest {

  private final AbsValueComparator<Short> absValueComparator = new AbsValueComparator<>();
  protected Failures failures;
  protected Shorts shorts;

  protected ComparatorBasedComparisonStrategy absValueComparisonStrategy;
  protected Shorts shortsWithAbsValueComparisonStrategy;

  @BeforeEach
  public void setUp() throws IllegalAccessException {
    failures = spy(Failures.instance());
    shorts = new Shorts(StandardComparisonStrategy.instance());
    writeField(shorts, "failures", failures);

    absValueComparisonStrategy = new ComparatorBasedComparisonStrategy(absValueComparator);
    shortsWithAbsValueComparisonStrategy = new Shorts(absValueComparisonStrategy);
    writeField(shortsWithAbsValueComparisonStrategy, "failures", failures);
  }

}
