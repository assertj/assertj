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
 * Copyright 2012-2017 the original author or authors.
 */
package org.assertj.core.error;


import org.assertj.core.api.TestCondition;
import org.assertj.core.data.Index;
import org.assertj.core.description.*;
import org.assertj.core.presentation.StandardRepresentation;
import org.junit.Test;

import static org.assertj.core.error.ShouldHaveAtIndex.shouldHaveAtIndex;
import static org.assertj.core.util.Lists.newArrayList;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for <code>{@link ShouldHaveAtIndex#create(org.assertj.core.description.Description, org.assertj.core.presentation.Representation)}</code>.
 *
 * @author Bo Gotthardt
 */
public class ShouldHaveAtIndex_create_Test {

  @Test public void should_create_error_message() {
    ErrorMessageFactory factory = shouldHaveAtIndex(newArrayList("Yoda", "Luke"), new TestCondition<String>("red lightsaber"), Index.atIndex(1), "Luke");
    String message = factory.create(new TextDescription("Test"), new StandardRepresentation());
    assertThat(message).isEqualTo(String.format(
        "[Test] %nExpecting:%n <\"Luke\">%nat index <1> to have:%n <red lightsaber>%nin:%n" +
            " <[\"Yoda\", \"Luke\"]>%n"));
  }
}
