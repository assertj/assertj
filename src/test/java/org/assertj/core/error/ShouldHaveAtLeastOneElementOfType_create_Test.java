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
 * Copyright 2012-2018 the original author or authors.
 */
package org.assertj.core.error;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.error.ShouldHaveAtLeastOneElementOfType.shouldHaveAtLeastOneElementOfType;
import static org.assertj.core.util.Arrays.array;
import static org.assertj.core.util.Lists.newArrayList;

import org.assertj.core.description.TextDescription;
import org.assertj.core.presentation.StandardRepresentation;
import org.junit.jupiter.api.Test;

/**
 * Tests for <code>{@link ShouldHaveAtLeastOneElementOfType#shouldHaveAtLeastOneElementOfType(Object, Class)}</code>.
 */
public class ShouldHaveAtLeastOneElementOfType_create_Test {

  @Test
  public void should_create_error_message_for_iterable() {
	ErrorMessageFactory factory = shouldHaveAtLeastOneElementOfType(newArrayList("Yoda", "Luke"), Long.class);
	String message = factory.create(new TextDescription("Test"), new StandardRepresentation());
	assertThat(message).isEqualTo(String.format("[Test] %n"
	                              + "Expecting:%n"
	                              + "  <[\"Yoda\", \"Luke\"]>%n"
	                              + "to have at least one element of type:%n"
	                              + "  <java.lang.Long>%n"
	                              + "but had none."));
  }

  @Test
  public void should_create_error_message_for_array() {
	ErrorMessageFactory factory = shouldHaveAtLeastOneElementOfType(array("Yoda", "Luke"), Long.class);
	String message = factory.create(new TextDescription("Test"), new StandardRepresentation());
	assertThat(message).isEqualTo(String.format("[Test] %n"
	                              + "Expecting:%n"
	                              + "  <[\"Yoda\", \"Luke\"]>%n"
	                              + "to have at least one element of type:%n"
	                              + "  <java.lang.Long>%n"
	                              + "but had none."));
  }

}
