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

import static org.mockito.Mockito.spy;

import org.assertj.core.testkit.CaseInsensitiveCharacterComparator;
import org.junit.jupiter.api.BeforeEach;

/**
 * Base class for Characters unit tests
 * <p>
 * Is in <code>org.assertj.core.internal</code> package to be able to set {@link Characters#failures} appropriately.
 *
 * @author Joel Costigliola
 *
 */
public class CharactersBaseTest {

  protected Failures failures;
  protected Characters characters;

  protected ComparatorBasedComparisonStrategy caseInsensitiveComparisonStrategy;
  protected Characters charactersWithCaseInsensitiveComparisonStrategy;

  @BeforeEach
  public void setUp() {
    failures = spy(new Failures());
    characters = new Characters();
    characters.failures = failures;
    caseInsensitiveComparisonStrategy = new ComparatorBasedComparisonStrategy(CaseInsensitiveCharacterComparator.INSTANCE);
    charactersWithCaseInsensitiveComparisonStrategy = new Characters(caseInsensitiveComparisonStrategy);
    charactersWithCaseInsensitiveComparisonStrategy.failures = failures;
  }

}
