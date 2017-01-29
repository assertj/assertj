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

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.data.MapEntry.entry;
import static org.assertj.core.error.ShouldContainValues.shouldContainValues;
import static org.assertj.core.test.Maps.mapOf;
import static org.assertj.core.util.Sets.newLinkedHashSet;

import java.util.Map;

import org.assertj.core.description.TextDescription;
import org.assertj.core.presentation.StandardRepresentation;
import org.junit.Test;

public class ShouldContainValues_create_Test {
  @Test
  public void should_create_error_message_with_multiple_values() {
	Map<?, ?> map = mapOf(entry("name", "Yoda"), entry("color", "green"));
	ErrorMessageFactory factory = shouldContainValues(map, newLinkedHashSet("VeryOld", "Vader"));
	String message = factory.create(new TextDescription("Test"), new StandardRepresentation());
	assertThat(message).isEqualTo(String.format("[Test] %n" +
	                              "Expecting:%n" +
	                              "  <{\"color\"=\"green\", \"name\"=\"Yoda\"}>%n" +
	                              "to contain values:%n" +
	                              "  <[\"VeryOld\", \"Vader\"]>"));
  }

  @Test
  public void should_create_error_message_with_single_value() {
	Map<?, ?> map = mapOf(entry("name", "Yoda"), entry("color", "green"));
	ErrorMessageFactory factory = shouldContainValues(map, newLinkedHashSet("VeryOld"));
	String message = factory.create(new TextDescription("Test"), new StandardRepresentation());
	assertThat(message).isEqualTo(String.format("[Test] %n" +
	                              "Expecting:%n" +
	                              "  <{\"color\"=\"green\", \"name\"=\"Yoda\"}>%n" +
	                              "to contain value:%n" +
	                              "  <\"VeryOld\">"));
  }

}
