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

/**
 * Assertion methods for {@link Character}s.
 * <p>
 * To create an instance of this class, invoke <code>{@link Assertions#assertThat(Character)}</code> or
 * <code>{@link Assertions#assertThat(char)}</code>.
 * </p>
 * 
 * @author Yvonne Wang
 * @author David DIDIER
 * @author Ansgar Konermann
 * @author Alex Ruiz
 * @author Joel Costigliola
 * @author Mikhail Mazursky
 */
public class CharacterAssert extends AbstractCharacterAssert<CharacterAssert> {

  protected CharacterAssert(Character actual) {
    super(actual, CharacterAssert.class);
  }
}
