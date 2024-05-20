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
package org.assertj.tests.core.api.recursive.assertion;

import static org.assertj.core.api.BDDAssertions.then;

import org.assertj.core.api.recursive.assertion.RecursiveAssertionNode;
import org.junit.jupiter.api.Test;

import nl.jqno.equalsverifier.EqualsVerifier;

class RecursiveAssertionNode_Test {

  @Test
  void should_honor_equals_contract() {
    EqualsVerifier.simple().forClass(RecursiveAssertionNode.class);
  }

  @Test
  void toString_should_succeed() {
    // GIVEN
    RecursiveAssertionNode recursiveAssertionNode = new RecursiveAssertionNode("rock", "music", String.class);
    // WHEN
    String result = recursiveAssertionNode.toString();
    // THEN
    then(result).isEqualTo("RecursiveAssertionNode[value=rock, name=music, type=class java.lang.String]");
  }

}
