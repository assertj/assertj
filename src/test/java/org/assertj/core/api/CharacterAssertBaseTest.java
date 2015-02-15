/**
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 *
 * Copyright 2012-2015 the original author or authors.
 */
package org.assertj.core.api;

import static org.mockito.Mockito.mock;

import org.assertj.core.internal.Characters;


/**
 * Base class for {@link CharacterAssert} tests.
 * 
 * @author Olivier Michallat
 */
public abstract class CharacterAssertBaseTest extends BaseTestTemplate<CharacterAssert, Character> {
  protected Characters characters;

  @Override
  protected CharacterAssert create_assertions() {
    return new CharacterAssert('a');
  }

  @Override
  protected void inject_internal_objects() {
    super.inject_internal_objects();
    characters = mock(Characters.class);
    assertions.characters = characters;
  }

  protected Characters getCharacters(CharacterAssert someAssertions) {
    return someAssertions.characters;
  }
}
