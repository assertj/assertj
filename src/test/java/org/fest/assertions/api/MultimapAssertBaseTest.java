/*
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */
package org.fest.assertions.api;

import static com.google.common.collect.Lists.newArrayList;
import static org.junit.rules.ExpectedException.none;

import org.junit.Before;
import org.junit.Rule;
import org.junit.rules.ExpectedException;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;

public class MultimapAssertBaseTest {

  @Rule
  public ExpectedException thrown = none();
  protected Multimap<String, String> actual;

  public MultimapAssertBaseTest() {
    super();
  }

  @Before
  public void setUp() {
    actual = ArrayListMultimap.create();
    actual.putAll("Lakers", newArrayList("Kobe Bryant", "Magic Johnson", "Kareem Abdul Jabbar"));
    actual.putAll("Spurs", newArrayList("Tony Parker", "Tim Duncan", "Manu Ginobili"));
    actual.putAll("Bulls", newArrayList("Michael Jordan", "Scottie Pippen", "Derrick Rose"));
  }

  // TODO : use ExpectedException from fest-test once stable.
  protected void expectException(Class<? extends Throwable> type, String message) {
    thrown.expect(type);
    thrown.expectMessage(message);
  }

}