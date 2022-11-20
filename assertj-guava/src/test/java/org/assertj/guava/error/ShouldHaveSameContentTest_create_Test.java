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
 * Copyright 2012-2022 the original author or authors.
 */
package org.assertj.guava.error;

import static java.lang.String.format;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.guava.error.ShouldHaveSameContent.shouldHaveSameContent;

import org.assertj.core.description.TextDescription;
import org.assertj.core.error.ErrorMessageFactory;
import org.assertj.core.presentation.StandardRepresentation;
import org.junit.jupiter.api.Test;

import com.google.common.io.ByteSource;

class ShouldHaveSameContentTest_create_Test {

  @Test
  void should_create_error_message() {
    // GIVEN
    ErrorMessageFactory factory = shouldHaveSameContent(ByteSource.wrap(new byte[1]), ByteSource.wrap(new byte[] { (byte) 1 }));
    // WHEN
    String message = factory.create(new TextDescription("Test"), StandardRepresentation.STANDARD_REPRESENTATION);
    // THEN
    then(message).isEqualTo(format("[Test] %nexpected: ByteSource.wrap(01)%n but was: ByteSource.wrap(00)"));
  }

}
