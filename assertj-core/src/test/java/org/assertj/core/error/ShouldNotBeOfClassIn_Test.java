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
 * Copyright 2012-2023 the original author or authors.
 */
package org.assertj.core.error;

import static java.lang.String.format;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.error.ShouldNotBeOfClassIn.shouldNotBeOfClassIn;
import static org.assertj.core.util.Lists.list;

import org.assertj.core.internal.TestDescription;
import org.assertj.core.presentation.StandardRepresentation;
import org.junit.jupiter.api.Test;

/**
 * Tests for <code>{@link ShouldNotBeOfClassIn#create(org.assertj.core.description.Description, org.assertj.core.presentation.Representation)}</code>.
 *
 * @author Nicolas François
 */
class ShouldNotBeOfClassIn_Test {

  @Test
  void should_create_error_message() {
    // GIVEN
    ErrorMessageFactory factory = shouldNotBeOfClassIn("Yoda", list(Long.class, String.class));
    // WHEN
    String message = factory.create(new TestDescription("Test"), new StandardRepresentation());
    // THEN
    then(message).isEqualTo(format("[Test] %nExpecting actual:%n  \"Yoda\"%nnot to be of any type in:%n  [java.lang.Long, java.lang.String]%nbut was of type: java.lang.String"));
  }
}
