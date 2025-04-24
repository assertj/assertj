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

import static org.apache.commons.lang3.reflect.FieldUtils.writeField;
import static org.assertj.tests.core.testkit.TestData.someInfo;
import static org.mockito.Mockito.spy;

import java.util.Set;

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.api.comparisonstrategy.ComparatorBasedComparisonStrategy;
import org.assertj.core.internal.Failures;
import org.assertj.core.internal.Strings;
import org.assertj.tests.core.testkit.CaseInsensitiveStringComparator;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

public class StringsBaseTest {

  protected static final AssertionInfo INFO = someInfo();

  protected Failures failures;
  protected Strings strings;

  protected ComparatorBasedComparisonStrategy comparisonStrategy;
  protected Strings stringsWithCaseInsensitiveComparisonStrategy;

  protected static final Set<Character> NON_BREAKING_SPACES = Set.of('\u00A0', '\u2007', '\u202F');

  @BeforeEach
  public void setUp() throws IllegalAccessException {
    failures = spy(Failures.instance());

    strings = Strings.instance();
    writeField(strings, "failures", failures, true);

    comparisonStrategy = new ComparatorBasedComparisonStrategy(CaseInsensitiveStringComparator.INSTANCE);
    stringsWithCaseInsensitiveComparisonStrategy = new Strings(comparisonStrategy);
    writeField(stringsWithCaseInsensitiveComparisonStrategy, "failures", failures, true);
  }

  @AfterEach
  public void tearDown() throws IllegalAccessException {
    writeField(strings, "failures", Failures.instance(), true);
  }

}
