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
package org.assertj.tests.guava.api;

import com.google.common.collect.LinkedListMultimap;
import com.google.common.collect.Multimap;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;

public class MultimapAssertBaseTest {

  protected Multimap<String, String> actual;

  public MultimapAssertBaseTest() {
    super();
  }

  @BeforeEach
  public void setUp() {
    actual = LinkedListMultimap.create();
    actual.putAll("Lakers", List.of("Kobe Bryant", "Magic Johnson", "Kareem Abdul Jabbar"));
    actual.putAll("Bulls", List.of("Michael Jordan", "Scottie Pippen", "Derrick Rose"));
    actual.putAll("Spurs", List.of("Tony Parker", "Tim Duncan", "Manu Ginobili"));
  }

}
