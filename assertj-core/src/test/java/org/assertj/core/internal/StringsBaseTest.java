/*
 * Copyright © 2024 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.assertj.core.internal;

import static org.assertj.core.testkit.TestData.someInfo;
import static org.mockito.Mockito.spy;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.testkit.CaseInsensitiveStringComparator;
import org.junit.jupiter.api.BeforeEach;

/**
 * 
 * Base class for {@link Strings} tests.
 * <p>
 * Is in <code>org.assertj.core.internal</code> package to be able to set {@link Strings#failures} appropriately.
 * 
 * @author Joel Costigliola
 * 
 */
public class StringsBaseTest {

  protected static final AssertionInfo INFO = someInfo();

  protected Failures failures;
  protected Strings strings;

  protected ComparatorBasedComparisonStrategy comparisonStrategy;
  protected Strings stringsWithCaseInsensitiveComparisonStrategy;

  protected static final Set<Character> NON_BREAKING_SPACES;

  static {
    Set<Character> nonBreakingSpaces = new HashSet<>();
    nonBreakingSpaces.add('\u00A0');
    nonBreakingSpaces.add('\u2007');
    nonBreakingSpaces.add('\u202F');

    NON_BREAKING_SPACES = Collections.unmodifiableSet(nonBreakingSpaces);
  }

  @BeforeEach
  public void setUp() {
    failures = spy(new Failures());
    strings = new Strings();
    strings.failures = failures;
    comparisonStrategy = new ComparatorBasedComparisonStrategy(CaseInsensitiveStringComparator.INSTANCE);
    stringsWithCaseInsensitiveComparisonStrategy = new Strings(comparisonStrategy);
    stringsWithCaseInsensitiveComparisonStrategy.failures = failures;
  }

}
