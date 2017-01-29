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
import static org.assertj.core.error.ShouldContainOnlyKeys.shouldContainOnlyKeys;
import static org.assertj.core.test.Maps.mapOf;
import static org.assertj.core.util.Lists.newArrayList;
import static org.assertj.core.util.Sets.newLinkedHashSet;

import java.util.Collections;

import org.assertj.core.description.TextDescription;
import org.assertj.core.presentation.StandardRepresentation;
import org.junit.Test;

/**
 * Tests for
 * <code>{@link ShouldContainOnlyKeys#create(org.assertj.core.description.Description, org.assertj.core.presentation.Representation)}</code>
 * .
 * 
 * @author Joel Costigliola.
 */
public class ShouldContainOnlyKeys_create_Test {

  @Test
  public void should_create_error_message() {
	ErrorMessageFactory factory = shouldContainOnlyKeys(mapOf(entry("name", "Yoda"), entry("color", "green")),
	                                                    newArrayList("jedi", "color"), newLinkedHashSet("jedi"),
	                                                    newLinkedHashSet("name"));

	String message = factory.create(new TextDescription("Test"), new StandardRepresentation());

	assertThat(message).isEqualTo(String.format("[Test] %n"
	                              + "Expecting:%n"
	                              + "  <{\"color\"=\"green\", \"name\"=\"Yoda\"}>%n"
	                              + "to contain only following keys:%n"
	                              + "  <[\"jedi\", \"color\"]>%n"
	                              + "keys not found:%n"
	                              + "  <[\"jedi\"]>%n"
	                              + "and keys not expected:%n"
	                              + "  <[\"name\"]>%n"));
  }

  @Test
  public void should_not_display_unexpected_elements_when_there_are_none() {
	ErrorMessageFactory factory = shouldContainOnlyKeys(mapOf(entry("color", "green")),
	                                                    newArrayList("jedi", "color"), newLinkedHashSet("jedi"),
	                                                    Collections.emptySet());

	String message = factory.create(new TextDescription("Test"), new StandardRepresentation());

	assertThat(message).isEqualTo(String.format("[Test] %n"
	                              + "Expecting:%n"
	                              + "  <{\"color\"=\"green\"}>%n"
	                              + "to contain only following keys:%n"
	                              + "  <[\"jedi\", \"color\"]>%n"
	                              + "but could not find the following keys:%n"
	                              + "  <[\"jedi\"]>%n"));
  }

}
