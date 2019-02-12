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
 * Copyright 2012-2019 the original author or authors.
 */
package org.assertj.core.api.list;

import static java.util.Arrays.asList;
import static org.assertj.core.data.TolkienCharacter.Race.HOBBIT;

import java.util.List;

import org.assertj.core.data.TolkienCharacter;

public class ListAssert_filteredOn_BaseTest {

  protected static List<TolkienCharacter> hobbits() {
    TolkienCharacter frodo = TolkienCharacter.of("Frodo", 33, HOBBIT);
    TolkienCharacter sam = TolkienCharacter.of("Sam", 35, HOBBIT);
    return asList(frodo, sam);
  }

}
