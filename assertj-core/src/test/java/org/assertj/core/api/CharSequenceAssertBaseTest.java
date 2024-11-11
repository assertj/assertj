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
 * Copyright 2012-2024 the original author or authors.
 */
package org.assertj.core.api;

import static org.mockito.Mockito.mock;

import org.assertj.core.internal.Strings;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Base class for {@link CharSequenceAssert} tests.
 * 
 * @author Olivier Michallat
 * @author Mikhail Mazursky
 */
public abstract class CharSequenceAssertBaseTest extends BaseTestTemplate<CharSequenceAssert, CharSequence> {
  protected Strings strings;

  protected static final Set<Character> NON_BREAKING_SPACES;

  static {
    Set<Character> nonBreakingSpaces = new HashSet<>();
    nonBreakingSpaces.add('\u00A0');
    nonBreakingSpaces.add('\u2007');
    nonBreakingSpaces.add('\u202F');

    NON_BREAKING_SPACES = Collections.unmodifiableSet(nonBreakingSpaces);
  }

  @Override
  protected CharSequenceAssert create_assertions() {
    return new CharSequenceAssert("Yoda");
  }

  @Override
  protected void inject_internal_objects() {
    super.inject_internal_objects();
    strings = mock(Strings.class);
    assertions.strings = strings;
  }

  protected Strings getStrings(CharSequenceAssert someAssertions) {
    return someAssertions.strings;
  }
}
